package DAO;

import koneksi.Connector;
import model.Kamar;
import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;

public class KamarDAO {

    public boolean tambahKamar(Kamar kamar) {
        String sql = "INSERT INTO kamar (nomor_kamar, tipe_kamar, harga_sewa, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = Connector.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Koneksi ke database gagal.", "Database Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            pstmt.setString(1, kamar.getNomorKamar());
            pstmt.setString(2, kamar.getTipeKamar());
            pstmt.setInt(3, kamar.getHargaSewa());
            pstmt.setString(4, kamar.getStatus());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saat menambah kamar: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    public List<Kamar> getAllKamar() {
        List<Kamar> daftarKamar = new ArrayList<>();
        String sql = "SELECT * FROM kamar ORDER BY nomor_kamar ASC";
        try (Connection conn = Connector.connection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Koneksi ke database gagal.", "Database Error", JOptionPane.ERROR_MESSAGE);
                return daftarKamar;
            }
            while (rs.next()) {
                Kamar kamar = new Kamar(
                        rs.getString("nomor_kamar"),
                        rs.getString("tipe_kamar"),
                        rs.getInt("harga_sewa"),
                        rs.getString("status")
                );
                daftarKamar.add(kamar);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saat mengambil data kamar: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return daftarKamar;
    }
    
    public List<Kamar> getKamarKosong() {
        List<Kamar> daftarKamar = new ArrayList<>();
        String sql = "SELECT * FROM kamar WHERE status = 'Kosong' ORDER BY nomor_kamar ASC";
        try (Connection conn = Connector.connection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (conn == null) return daftarKamar;
            while (rs.next()) {
                Kamar kamar = new Kamar(
                        rs.getString("nomor_kamar"),
                        rs.getString("tipe_kamar"),
                        rs.getInt("harga_sewa"),
                        rs.getString("status")
                );
                daftarKamar.add(kamar);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarKamar;
    }

    public Kamar getKamarByNomor(String nomorKamar) {
        String sql = "SELECT * FROM kamar WHERE nomor_kamar = ?";
        try (Connection conn = Connector.connection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) return null;
            pstmt.setString(1, nomorKamar);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Kamar(
                        rs.getString("nomor_kamar"),
                        rs.getString("tipe_kamar"),
                        rs.getInt("harga_sewa"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
             JOptionPane.showMessageDialog(null, "Error saat mencari kamar: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
             e.printStackTrace();
        }
        return null;
    }

    public boolean updateKamar(String nomorKamarAsli, Kamar kamar) {
        String sql = "UPDATE kamar SET tipe_kamar = ?, harga_sewa = ?, status = ? WHERE nomor_kamar = ?";
//        if (!nomorKamarAsli.equals(kamar.getNomorKamar())) {
//             // Jika PK (nomor_kamar) diizinkan untuk diubah (jarang untuk PK)
//             // sql = "UPDATE kamar SET nomor_kamar = ?, tipe_kamar = ?, harga_sewa = ?, status = ? WHERE nomor_kamar = ?";
//             // Tapi ini membuat parameter indexnya berbeda.
//             // Untuk kasus ini, kita asumsikan nomor_kamar TIDAK diubah saat edit, hanya atribut lain.
//             // Jika KamarView mengizinkan edit nomor_kamar, perlu logika tambahan di controller.
//             // Untuk sekarang, kita update berdasarkan nomorKamarAsli.
//        }

        try (Connection conn = Connector.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) return false;
            pstmt.setString(1, kamar.getTipeKamar());
            pstmt.setInt(2, kamar.getHargaSewa());
            pstmt.setString(3, kamar.getStatus());
            pstmt.setString(4, nomorKamarAsli);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saat update kamar: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateStatusKamar(String nomorKamar, String status) {
        String sql = "UPDATE kamar SET status = ? WHERE nomor_kamar = ?";
        try (Connection conn = Connector.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) return false;
            pstmt.setString(1, status);
            pstmt.setString(2, nomorKamar);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hapusKamar(String nomorKamar) {
        String sql = "DELETE FROM kamar WHERE nomor_kamar = ?";
        try (Connection conn = Connector.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) return false;
            pstmt.setString(1, nomorKamar);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}