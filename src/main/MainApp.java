package main;

import view.LoginView;
import javax.swing.SwingUtilities;
import koneksi.Connector;
import javax.swing.JOptionPane;

public class MainApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (Connector.connection() == null) {
                    JOptionPane.showMessageDialog(null,
                            "Gagal terhubung ke database saat memulai aplikasi.\n" +
                            "Pastikan server database berjalan dan konfigurasi koneksi benar.",
                            "Kesalahan Koneksi Database",
                            JOptionPane.ERROR_MESSAGE);
                    System.exit(1); 
                }
                
                new LoginView().setVisible(true);
            }
        });
    }
}
