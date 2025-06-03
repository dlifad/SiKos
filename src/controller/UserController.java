package controller;

import DAO.UserDAO;
import model.User;
import view.LoginView;
import view.DaftarView;
import javax.swing.JOptionPane;

public class UserController {
    private UserDAO userDAO;
    private LoginView loginView; 
    private DaftarView registerView; 

    public UserController(LoginView view) {
        this.loginView = view;
        this.userDAO = new UserDAO();
    }

    public UserController(DaftarView view) {
        this.registerView = view;
        this.userDAO = new UserDAO();
    }

    public void prosesLogin() {
        if (loginView == null) return; 

        String username = loginView.getUsername();
        String password = loginView.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            loginView.tampilkanPesan("Username dan Password tidak boleh kosong.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = userDAO.loginUser(username, password);
        if (user != null) {
            loginView.loginBerhasil();
        } else {
            loginView.loginGagal();
        }
    }

    public void prosesRegistrasi() {
        if (registerView == null) return; 

        String username = registerView.getUsername();
        String password = registerView.getPassword();
        String konfirmasiPassword = registerView.getKonfirmasiPassword();
        String email = registerView.getEmail();

        if (username.isEmpty() || password.isEmpty() || konfirmasiPassword.isEmpty() || email.isEmpty()) {
            registerView.tampilkanPesan("Semua field (Username, Password, Konfirmasi, Email) wajib diisi.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(konfirmasiPassword)) {
            registerView.tampilkanPesan("Password dan Konfirmasi Password tidak cocok.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            registerView.tampilkanPesan("Format email tidak valid.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        User newUser = new User(username, password, email);
        if (userDAO.registerUser(newUser)) {
            registerView.registrasiBerhasil();
        } else {
            registerView.tampilkanPesan("Registrasi gagal. Silakan coba lagi.", "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }
}