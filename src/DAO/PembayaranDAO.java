package DAO;

import koneksi.Connector;
import model.Pembayaran;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Date;
import java.time.Month;
import java.time.format.TextStyle;

public class PembayaranDAO {

    private static final String ERROR_PREFIX_CATAT_PEMBAYARAN = "Error saat mencatat pembayaran: ";
    private static final String ERROR_PREFIX_TOTAL_BAYAR_PERIODE = "Error saat menghitung total bayar periode: ";
    private static final String ERROR_PREFIX_GET_ALL_PEMBAYARAN = "Error saat mengambil semua detail pembayaran: ";
    private static final String ERROR_PREFIX_GET_TANGGAL_BAYAR_TERAKHIR = "Error saat mengambil tanggal bayar terakhir untuk periode: ";
    private static final String ERROR_KONEKSI_NULL = "Koneksi database null.";

    public boolean catatPembayaran(Pembayaran pembayaran) {
        String sql = "INSERT INTO pembayaran (id_penghuni, bulan_tagihan, tahun_tagihan, tanggal_bayar, jumlah_bayar, status_pembayaran, sisa_tagihan) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Connector.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (conn == null) {
                System.err.println(ERROR_PREFIX_CATAT_PEMBAYARAN + ERROR_KONEKSI_NULL);
                return false;
            }

            pstmt.setInt(1, pembayaran.getIdPenghuni());
            pstmt.setInt(2, pembayaran.getBulanTagihan());
            pstmt.setInt(3, pembayaran.getTahunTagihan());

            if (pembayaran.getTanggalBayar() != null) {
                pstmt.setDate(4, new java.sql.Date(pembayaran.getTanggalBayar().getTime()));
            } else {
                pstmt.setNull(4, Types.DATE);
            }

            pstmt.setInt(5, pembayaran.getJumlahBayar());
            pstmt.setString(6, pembayaran.getStatusPembayaran());
            pstmt.setInt(7, pembayaran.getSisaTagihan());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pembayaran.setIdPembayaran(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println(ERROR_PREFIX_CATAT_PEMBAYARAN + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public int getTotalSudahBayarUntukPeriode(int idPenghuni, int bulanTagihan, int tahunTagihan) {
        String sql = "SELECT SUM(jumlah_bayar) as total_dibayar FROM pembayaran " +
                     "WHERE id_penghuni = ? AND bulan_tagihan = ? AND tahun_tagihan = ?";
        try (Connection conn = Connector.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println(ERROR_PREFIX_TOTAL_BAYAR_PERIODE + ERROR_KONEKSI_NULL);
                return 0;
            }

            pstmt.setInt(1, idPenghuni);
            pstmt.setInt(2, bulanTagihan);
            pstmt.setInt(3, tahunTagihan);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_dibayar");
                }
            }
        } catch (SQLException e) {
            System.err.println(ERROR_PREFIX_TOTAL_BAYAR_PERIODE + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public Date getTanggalBayarTerakhirUntukPeriode(int idPenghuni, int bulanTagihan, int tahunTagihan) {
        String sql = "SELECT MAX(tanggal_bayar) FROM pembayaran WHERE id_penghuni = ? AND bulan_tagihan = ? AND tahun_tagihan = ?";
        try (Connection conn = Connector.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println(ERROR_PREFIX_GET_TANGGAL_BAYAR_TERAKHIR + ERROR_KONEKSI_NULL);
                return null;
            }

            pstmt.setInt(1, idPenghuni);
            pstmt.setInt(2, bulanTagihan);
            pstmt.setInt(3, tahunTagihan);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDate(1);
                }
            }
        } catch (SQLException e) {
            System.err.println(ERROR_PREFIX_GET_TANGGAL_BAYAR_TERAKHIR + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Object[]> getAllPembayaranWithDetails() {
        List<Object[]> resultList = new ArrayList<>();
        // VERIFIKASI 'pgn.id' (pada JOIN) dan 'pb.id_pembayaran' (pada ORDER BY) di query bawah agar sesuai skema DB Anda.
        String sql = "SELECT pgn.nama, pgn.nomor_kamar, " +
                     "       pb.bulan_tagihan, pb.tahun_tagihan, pb.tanggal_bayar, " +
                     "       pb.jumlah_bayar, pb.status_pembayaran, pb.sisa_tagihan " +
                     "FROM pembayaran pb " +
                     "JOIN penghuni pgn ON pb.id_penghuni = pgn.id " + 
                     "ORDER BY pb.tanggal_bayar DESC, pgn.nama ASC, pb.tahun_tagihan DESC, pb.bulan_tagihan DESC, pb.id_pembayaran DESC";

        try (Connection conn = Connector.connection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (conn == null) {
                System.err.println(ERROR_PREFIX_GET_ALL_PEMBAYARAN + ERROR_KONEKSI_NULL);
                return resultList;
            }

            Locale localeIndonesia = new Locale("id", "ID");
            while (rs.next()) {
                int angkaBulanDb = rs.getInt("bulan_tagihan");
                String namaBulanDb = (angkaBulanDb >= 1 && angkaBulanDb <= 12) ?
                                     Month.of(angkaBulanDb).getDisplayName(TextStyle.FULL, localeIndonesia) :
                                     "Bulan Invalid (" + angkaBulanDb + ")";

                Object[] row = new Object[]{
                    rs.getString("nama"),
                    rs.getString("nomor_kamar"),
                    namaBulanDb + "/" + rs.getInt("tahun_tagihan"),
                    rs.getDate("tanggal_bayar"),
                    rs.getInt("jumlah_bayar"),
                    rs.getString("status_pembayaran"),
                    rs.getInt("sisa_tagihan")
                };
                resultList.add(row);
            }
        } catch (SQLException e) {
            System.err.println(ERROR_PREFIX_GET_ALL_PEMBAYARAN + e.getMessage());
            e.printStackTrace();
        }
        return resultList;
    }
}