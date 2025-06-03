package controller;

import DAO.KamarDAO;
import model.Kamar;
import view.KamarView;
import javax.swing.JOptionPane;
import java.util.List;

public class KamarController {
    private KamarView view;
    private KamarDAO dao;

    public KamarController(KamarView view) {
        this.view = view;
        this.dao = new KamarDAO();
    }

    public void loadAllKamar() {
        List<Kamar> daftarKamar = dao.getAllKamar();
        view.tampilkanSemuaKamar(daftarKamar);
    }

    public void simpanKamar() {
        Kamar kamar = view.getKamarFormData();
        if (kamar == null) {
            return; 
        }

        boolean sukses;
        if (view.isModeEdit()) {
            String nomorKamarAsli = view.getNomorKamarUntukUpdate(); 
            sukses = dao.updateKamar(nomorKamarAsli, kamar); 
            if (sukses) {
                view.tampilkanPesan("Data kamar berhasil diperbarui!", "Sukses Update", JOptionPane.INFORMATION_MESSAGE);
            } else {
                view.tampilkanPesan("Gagal memperbarui data kamar. Pastikan tidak ada duplikasi nomor kamar .", "Gagal Update", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            if (dao.getKamarByNomor(kamar.getNomorKamar()) != null) {
                view.tampilkanPesan("Nomor kamar '" + kamar.getNomorKamar() + "' sudah terdaftar! Gunakan nomor lain.", "Error Duplikasi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            sukses = dao.tambahKamar(kamar);
            if (sukses) {
                view.tampilkanPesan("Kamar baru berhasil ditambahkan!", "Sukses Tambah", JOptionPane.INFORMATION_MESSAGE);
            } else {
                view.tampilkanPesan("Gagal menambahkan kamar baru.", "Gagal Tambah", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        if (sukses) {
            view.resetViewSetelahOperasiSukses();
        }
    }
    
    private boolean isRoomEmptyForUpdate(String nomorKamar) {
        return true;
    }


    public void hapusKamarTerpilih() {
        String nomorKamar = view.getNomorKamarTerpilih();
        if (nomorKamar == null) {
            view.tampilkanPesan("Pilih kamar yang akan dihapus dari tabel!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(view,
                "Anda yakin ingin menghapus kamar nomor '" + nomorKamar + "'?\n",
                "Konfirmasi Hapus", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            boolean sukses = dao.hapusKamar(nomorKamar);
            if (sukses) {
                view.tampilkanPesan("Kamar '" + nomorKamar + "' berhasil dihapus!", "Sukses Hapus", JOptionPane.INFORMATION_MESSAGE);
                view.resetViewSetelahOperasiSukses();
            } else {
                view.tampilkanPesan("Masih terdapat Penghuni, Gagal menghapus kamar '" + nomorKamar + "'.", "Gagal Hapus", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public Kamar getKamarByNomor(String nomorKamar) {
        if (nomorKamar == null || nomorKamar.trim().isEmpty()) {
            return null;
        }
        return dao.getKamarByNomor(nomorKamar);
    }
}