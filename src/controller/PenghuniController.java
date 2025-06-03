package controller;

import model.Penghuni;
import model.Kamar;
import DAO.KamarDAO; 
import DAO.PenghuniDAO; 
import view.PenghuniView;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class PenghuniController {
    private PenghuniView view;
    private PenghuniDAO penghuniDAO; 
    private KamarDAO kamarDAO;

    public PenghuniController(PenghuniView view) {
        this.view = view;
        this.penghuniDAO = new PenghuniDAO(); 
        this.kamarDAO = new KamarDAO();
    }

    public void loadAllPenghuni() {
        List<Penghuni> penghuniList = penghuniDAO.getAllPenghuni();
        view.tampilkanSemuaPenghuni(penghuniList);
    }

    public void loadAvailableKamar() {
        List<Kamar> kamarKosong = kamarDAO.getKamarKosong(); 
        view.tampilkanPilihanKamar(kamarKosong, null);
    }
    
    public void loadAvailableKamarWithCurrent(String nomorKamarDitempati) {
        List<Kamar> kamarTersedia = new ArrayList<>(kamarDAO.getKamarKosong()); 
        Kamar kamarSaatIni = kamarDAO.getKamarByNomor(nomorKamarDitempati);

        if (kamarSaatIni != null) {
            boolean found = false;
            for(Kamar k : kamarTersedia) {
                if(k.getNomorKamar().equals(nomorKamarDitempati)) {
                    found = true;
                    break;
                }
            }
            if(!found) {
                 kamarTersedia.add(kamarSaatIni); 
            }
        }
        view.tampilkanPilihanKamar(kamarTersedia, nomorKamarDitempati);
    }

    public Penghuni getPenghuniById(int idPenghuni) { 
        return penghuniDAO.getPenghuniById(idPenghuni); 
    }
    
    private boolean isNikExist(String nik, int currentIdToExclude) {
        return penghuniDAO.isNikExist(nik, currentIdToExclude);
    }

    public void simpanAtauUpdatePenghuni(Penghuni penghuniDataForm, boolean isEdit, int idPenghuniLama) {
        int idToExcludeForNikCheck = isEdit ? idPenghuniLama : 0; 
        if (isNikExist(penghuniDataForm.getNik(), idToExcludeForNikCheck)) {
            view.tampilkanPesan("NIK " + penghuniDataForm.getNik() + " sudah terdaftar untuk penghuni lain.", "Error Duplikasi NIK", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Kamar kamarPilihan = kamarDAO.getKamarByNomor(penghuniDataForm.getNomorKamarDitempati());
        if (kamarPilihan == null) {
            view.tampilkanPesan("Nomor kamar yang dipilih ("+ penghuniDataForm.getNomorKamarDitempati() +") tidak valid.", "Error Kamar", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean sukses = false;

        if (isEdit) {
            Penghuni penghuniAsli = penghuniDAO.getPenghuniById(idPenghuniLama);
            if (penghuniAsli == null) {
                view.tampilkanPesan("Gagal update, data penghuni asli dengan ID " + idPenghuniLama + " tidak ditemukan.", "Error Data", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String kamarLamaDitempati = penghuniAsli.getNomorKamarDitempati();
            String kamarBaruDitempati = penghuniDataForm.getNomorKamarDitempati();

            if (!kamarLamaDitempati.equals(kamarBaruDitempati)) {
                Kamar kamarBaruObj = kamarDAO.getKamarByNomor(kamarBaruDitempati);
                if (kamarBaruObj == null || !kamarBaruObj.getStatus().equalsIgnoreCase("Kosong")) {
                    view.tampilkanPesan("Kamar " + kamarBaruDitempati + " tidak tersedia atau sudah terisi.", "Error Kamar", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                kamarDAO.updateStatusKamar(kamarLamaDitempati, "Kosong"); 
                kamarDAO.updateStatusKamar(kamarBaruDitempati, "Terisi"); 
            }
            
            penghuniDataForm.setIdPenghuni(idPenghuniLama); 
            sukses = penghuniDAO.updatePenghuni(penghuniDataForm); 
            
            if (sukses) view.tampilkanPesan("Data penghuni berhasil diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);

        } else { // Mode Tambah
            if (!kamarPilihan.getStatus().equalsIgnoreCase("Kosong")) {
                view.tampilkanPesan("Kamar " + penghuniDataForm.getNomorKamarDitempati() + " sudah terisi. Pilih kamar lain.", "Error Kamar", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            sukses = penghuniDAO.tambahPenghuni(penghuniDataForm); 

            if (sukses) {
                kamarDAO.updateStatusKamar(penghuniDataForm.getNomorKamarDitempati(), "Terisi");
                view.tampilkanPesan("Penghuni baru berhasil ditambahkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
        if (sukses) {
            view.resetViewSetelahOperasiSukses();
        } else {
             if (!isEdit) { 
                 view.tampilkanPesan("Operasi penambahan penghuni gagal.", "Gagal Tambah", JOptionPane.ERROR_MESSAGE);
             } else {
                 view.tampilkanPesan("Operasi pembaruan penghuni gagal.", "Gagal Update", JOptionPane.ERROR_MESSAGE);
             }
        }
    }

    public void hapusPenghuni(int idPenghuni, String noKamarYgDitinggalkan) { 
        boolean suksesHapusPenghuni = penghuniDAO.hapusPenghuni(idPenghuni); 

        if (suksesHapusPenghuni) {
            if (noKamarYgDitinggalkan != null && !noKamarYgDitinggalkan.isEmpty()) {
                 boolean suksesUpdateKamar = kamarDAO.updateStatusKamar(noKamarYgDitinggalkan, "Kosong");
                 if (!suksesUpdateKamar) {
                     System.err.println("Peringatan: Penghuni ID " + idPenghuni + " berhasil dihapus, tetapi gagal mengupdate status kamar " + noKamarYgDitinggalkan);
                     view.tampilkanPesan("Penghuni berhasil dihapus, tetapi ada masalah saat mengupdate status kamar.", "Info Sebagian Sukses", JOptionPane.WARNING_MESSAGE);
                 } else {
                    view.tampilkanPesan("Data penghuni berhasil dihapus dan status kamar telah dikosongkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                 }
            } else {
                view.tampilkanPesan("Data penghuni berhasil dihapus (tidak ada informasi kamar yang valid untuk dikosongkan).", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
            view.resetViewSetelahOperasiSukses();
        } else {
            view.tampilkanPesan("Gagal menghapus data penghuni.", "Error Penghapusan", JOptionPane.ERROR_MESSAGE);
        }
    }
}