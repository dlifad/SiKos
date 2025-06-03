package DAO;

import koneksi.Connector;
import model.User;
import java.sql.*;
import javax.swing.JOptionPane;
import java.security.*;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;

public class UserDAO {

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, hash);
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length() < 64) { 
                hexString.insert(0, '0');
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Gagal hashing password: Algoritma SHA-256 tidak ditemukan", e);
        }
    }

    public boolean registerUser(User user) {
        if (getUserByUsername(user.getUsername()) != null) {
            JOptionPane.showMessageDialog(null, "Username '" + user.getUsername() + "' sudah terdaftar.", "Registrasi Gagal", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty() && getUserByEmail(user.getEmail()) != null) {
            JOptionPane.showMessageDialog(null, "Email '" + user.getEmail() + "' sudah terdaftar.", "Registrasi Gagal", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = Connector.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (conn == null) {
                 JOptionPane.showMessageDialog(null, "Koneksi ke database gagal untuk registrasi.", "Database Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, hashPassword(user.getPassword())); 
            pstmt.setString(3, user.getEmail());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setIdUser(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saat registrasi user: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }

    public User loginUser(String username, String plainPassword) {
        User user = getUserByUsername(username);
        if (user != null) {
            if (user.getPassword().equals(hashPassword(plainPassword))) {
                return user; 
            } else {
                return null; 
            }
        }
        return null; 
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT id_user, username, password, email FROM users WHERE username = ?";
        try (Connection conn = Connector.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) return null;
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id_user"),
                        rs.getString("username"),
                        rs.getString("password"), 
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saat mencari user: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT id_user, username, password, email FROM users WHERE email = ?";
        try (Connection conn = Connector.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) return null;
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id_user"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error saat mencari user by email: " + e.getMessage());
            e.printStackTrace(); 
        }
        return null;
    }
}