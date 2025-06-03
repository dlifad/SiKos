package DAO;

import koneksi.Connector;
import model.Penghuni;
import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;

public class PenghuniDAO {

    public boolean isNikExist(String nik, int idPenghuniToExclude) {
        String sql = "SELECT COUNT(*) FROM penghuni WHERE nik = ? AND id != ?";                                               
        try (Connection conn = Connector.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) {
                System.err.println("Koneksi ke database gagal pada isNikExist.");
                return false; 
            }

            pstmt.setString(1, nik);
            pstmt.setInt(2, idPenghuniToExclude);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saat cek NIK: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false; 
    }
    
    public boolean tambahPenghuni(Penghuni penghuni) {
        String sql = "INSERT INTO penghuni (nik, nama, kontak, nomor_kamar, tanggal_sewa) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Connector.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (conn == null) return false;
            pstmt.setString(1, penghuni.getNik());
            pstmt.setString(2, penghuni.getNamaPenghuni());
            pstmt.setString(3, penghuni.getNomorKontak());
            
            if (penghuni.getNomorKamarDitempati() == null || penghuni.getNomorKamarDitempati().trim().isEmpty()) {
                pstmt.setNull(4, Types.VARCHAR);
            } else {
                pstmt.setString(4, penghuni.getNomorKamarDitempati());
            }
            
            if (penghuni.getTanggalSewa() != null) {
                pstmt.setDate(5, new java.sql.Date(penghuni.getTanggalSewa().getTime()));
            } else {
                pstmt.setNull(5, Types.DATE);
            }
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        penghuni.setIdPenghuni(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saat menambah penghuni: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    public List<Penghuni> getAllPenghuni() {
        List<Penghuni> daftarPenghuni = new ArrayList<>();
        String sql = "SELECT * FROM penghuni";
        try (Connection conn = Connector.connection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (conn == null) return daftarPenghuni;
            while (rs.next()) {
                Penghuni penghuni = new Penghuni(
                        rs.getInt("id"),
                        rs.getString("nik"),
                        rs.getString("nama"),
                        rs.getString("kontak"),
                        rs.getString("nomor_kamar"),
                        rs.getDate("tanggal_sewa")
                );
                daftarPenghuni.add(penghuni);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saat mengambil data penghuni: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return daftarPenghuni;
    }
    
    public List<Map<String, Object>> getAllPenghuniWithHargaSewa() {
        List<Map<String, Object>> resultList = new ArrayList<>();
        String sql = "SELECT p.id AS id_penghuni, p.nama, p.nomor_kamar, p.tanggal_sewa, k.harga_sewa " +
                     "FROM penghuni p " +
                     "LEFT JOIN kamar k ON p.nomor_kamar = k.nomor_kamar " +
                     "ORDER BY p.tanggal_sewa ASC, p.nama ASC";

        try (Connection conn = Connector.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (conn == null) return resultList;

            while (rs.next()) {
                Map<String, Object> data = new HashMap<>();
                data.put("id_penghuni", rs.getInt("id_penghuni"));
                data.put("nama", rs.getString("nama"));
                data.put("nomor_kamar", rs.getString("nomor_kamar"));
                data.put("tanggal_sewa", rs.getDate("tanggal_sewa"));
                data.put("harga_sewa", rs.getInt("harga_sewa"));
                resultList.add(data);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saat mengambil data penghuni dengan harga sewa: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return resultList;
    }
    
    public Penghuni getPenghuniById(int idPenghuni) {
        String sql = "SELECT * FROM penghuni WHERE id = ?";
        try (Connection conn = Connector.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) return null;
            pstmt.setInt(1, idPenghuni);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Penghuni(
                        rs.getInt("id"),
                        rs.getString("nik"),
                        rs.getString("nama"),
                        rs.getString("kontak"),
                        rs.getString("nomor_kamar"),
                        rs.getDate("tanggal_sewa")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Penghuni getPenghuniByNIK(String nik) {
        String sql = "SELECT * FROM penghuni WHERE nik = ?";
         try (Connection conn = Connector.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) return null;
            pstmt.setString(1, nik);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Penghuni(
                        rs.getInt("id"),
                        rs.getString("nik"),
                        rs.getString("nama"),
                        rs.getString("kontak"),
                        rs.getString("nomor_kamar"),
                        rs.getDate("tanggal_sewa")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updatePenghuni(Penghuni penghuni) {
        String sql = "UPDATE penghuni SET nik = ?, nama = ?, kontak = ?, " +
                     "nomor_kamar = ?, tanggal_sewa = ? WHERE id = ?";
        try (Connection conn = Connector.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) return false;
            pstmt.setString(1, penghuni.getNik());
            pstmt.setString(2, penghuni.getNamaPenghuni());
            pstmt.setString(3, penghuni.getNomorKontak());
            
            if (penghuni.getNomorKamarDitempati() == null || penghuni.getNomorKamarDitempati().trim().isEmpty()) {
                pstmt.setNull(4, Types.VARCHAR);
            } else {
                pstmt.setString(4, penghuni.getNomorKamarDitempati());
            }
            
            if (penghuni.getTanggalSewa() != null) {
                 pstmt.setDate(5, new java.sql.Date(penghuni.getTanggalSewa().getTime()));
            } else {
                pstmt.setNull(5, Types.DATE);
            }
            pstmt.setInt(6, penghuni.getIdPenghuni());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saat update penghuni: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    public boolean hapusPenghuni(int idPenghuni) {
        String sql = "DELETE FROM penghuni WHERE id = ?";
        try (Connection conn = Connector.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) return false;
            pstmt.setInt(1, idPenghuni);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saat menghapus penghuni: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
}