package controller;

import model.Pembayaran;
import DAO.PembayaranDAO;
import DAO.PenghuniDAO;
import DAO.KamarDAO;
import model.Penghuni;
import model.Kamar;
import view.PembayaranView;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.time.Month;
import java.time.format.TextStyle;

public class PembayaranController {
    private PembayaranView view;
    private PembayaranDAO pembayaranDAO;
    private PenghuniDAO penghuniDAO;
    private KamarDAO kamarDAO;

    private static class TagihanEntry {
        String namaPenghuni;
        String noKamar;
        String periodeDisplay;
        int tahunPeriode;
        int bulanPeriode;
        Date tanggalBayarUntukTabel;
        int totalSudahBayar;
        String statusPembayaran;
        int sisaTagihan;

        public TagihanEntry(String namaPenghuni, String noKamar, String periodeDisplay, int tahunPeriode, int bulanPeriode,
                            Date tanggalBayarUntukTabel, int totalSudahBayar, String statusPembayaran, int sisaTagihan) {
            this.namaPenghuni = namaPenghuni;
            this.noKamar = noKamar;
            this.periodeDisplay = periodeDisplay;
            this.tahunPeriode = tahunPeriode;
            this.bulanPeriode = bulanPeriode;
            this.tanggalBayarUntukTabel = tanggalBayarUntukTabel;
            this.totalSudahBayar = totalSudahBayar;
            this.statusPembayaran = statusPembayaran;
            this.sisaTagihan = sisaTagihan;
        }

        public String getNamaPenghuni() { return namaPenghuni; }
        public String getNoKamar() { return noKamar; }
        public String getPeriodeDisplay() { return periodeDisplay; }
        public int getTahunPeriode() { return tahunPeriode; }
        public int getBulanPeriode() { return bulanPeriode; }
        public Date getTanggalBayarUntukTabel() { return tanggalBayarUntukTabel; }
        public int getTotalSudahBayar() { return totalSudahBayar; }
        public String getStatusPembayaran() { return statusPembayaran; }
        public int getSisaTagihan() { return sisaTagihan; }
    }

    public PembayaranController(PembayaranView view) {
        this.view = view;
        this.pembayaranDAO = new PembayaranDAO();
        this.penghuniDAO = new PenghuniDAO();
        this.kamarDAO = new KamarDAO();
    }

    public void loadSemuaPeriodeTagihanPerPenghuni() {
        List<Map<String, Object>> daftarPenghuniData = penghuniDAO.getAllPenghuniWithHargaSewa();
        List<TagihanEntry> semuaTagihanEntri = new ArrayList<>();
        Locale localeIndonesia = new Locale("id", "ID");
        Calendar kalenderSekarang = Calendar.getInstance();
        int tahunSekarang = kalenderSekarang.get(Calendar.YEAR);
        int bulanSekarang = kalenderSekarang.get(Calendar.MONTH) + 1;

        for (Map<String, Object> dataPenghuni : daftarPenghuniData) {
            Date tanggalSewa = (Date) dataPenghuni.get("tanggal_sewa");
            if (tanggalSewa == null) {
                System.err.println("Penghuni " + dataPenghuni.get("nama") + " tidak memiliki tanggal sewa, dilewati.");
                continue;
            }

            Calendar kalenderMulaiSewa = Calendar.getInstance();
            kalenderMulaiSewa.setTime(tanggalSewa);
            int tahunMulaiSewa = kalenderMulaiSewa.get(Calendar.YEAR);
            int bulanMulaiSewa = kalenderMulaiSewa.get(Calendar.MONTH) + 1;

            int idPenghuni = (int) dataPenghuni.get("id_penghuni");
            String nama = (String) dataPenghuni.get("nama");
            String noKamar = (String) dataPenghuni.get("nomor_kamar");
            int hargaSewa = (dataPenghuni.get("harga_sewa") != null) ? (int) dataPenghuni.get("harga_sewa") : 0;

            if (noKamar == null || noKamar.isEmpty()) {
                System.err.println("PERINGATAN: Penghuni " + nama + " tidak memiliki nomor kamar. Tagihan tidak dapat dihitung.");
                continue;
            }

            for (int tahunIter = tahunMulaiSewa; tahunIter <= tahunSekarang; tahunIter++) {
                int bulanAwalUntukLoop = (tahunIter == tahunMulaiSewa) ? bulanMulaiSewa : 1;
                int bulanAkhirUntukLoop = (tahunIter == tahunSekarang) ? bulanSekarang : 12;

                for (int bulanIter = bulanAwalUntukLoop; bulanIter <= bulanAkhirUntukLoop; bulanIter++) {
                    int totalSudahBayarPeriodeIni = pembayaranDAO.getTotalSudahBayarUntukPeriode(idPenghuni, bulanIter, tahunIter);
                    int sisaTagihan = hargaSewa - totalSudahBayarPeriodeIni;
                    sisaTagihan = Math.max(0, sisaTagihan);
                    String statusPembayaran = (hargaSewa == 0 || sisaTagihan <= 0) ? "Lunas" : "Belum Lunas";
                    Date tanggalBayarUntukTabel = null;

                    if (totalSudahBayarPeriodeIni > 0) {
                        tanggalBayarUntukTabel = pembayaranDAO.getTanggalBayarTerakhirUntukPeriode(idPenghuni, bulanIter, tahunIter);
                    }
                    
                    if (hargaSewa == 0) {
                         statusPembayaran = "Lunas";
                    }

                    String namaBulanDisplay = Month.of(bulanIter).getDisplayName(TextStyle.FULL, localeIndonesia);
                    String periodeDisplay = namaBulanDisplay + "/" + tahunIter;

                    semuaTagihanEntri.add(new TagihanEntry(
                            nama,
                            noKamar,
                            periodeDisplay,
                            tahunIter,
                            bulanIter,
                            tanggalBayarUntukTabel,
                            totalSudahBayarPeriodeIni,
                            statusPembayaran,
                            sisaTagihan
                    ));
                }
            }
        }

        semuaTagihanEntri.sort(Comparator
                .comparingInt(TagihanEntry::getTahunPeriode).reversed()
                .thenComparing(Comparator.comparingInt(TagihanEntry::getBulanPeriode).reversed())
                .thenComparing(TagihanEntry::getNamaPenghuni, String.CASE_INSENSITIVE_ORDER)
        );

        List<Object[]> dataUntukTabel = new ArrayList<>();
        for (TagihanEntry entry : semuaTagihanEntri) {
            dataUntukTabel.add(new Object[]{
                    entry.getNamaPenghuni(),
                    entry.getNoKamar(),
                    entry.getPeriodeDisplay(),
                    entry.getTanggalBayarUntukTabel(),
                    entry.getTotalSudahBayar(),
                    entry.getStatusPembayaran(),
                    entry.getSisaTagihan()
            });
        }
        view.tampilkanSemuaPembayaran(dataUntukTabel);
    }

    public void loadPenghuniUntukComboBox() {
        List<Penghuni> daftarPenghuni = penghuniDAO.getAllPenghuni();
        view.isiComboBoxPenghuni(daftarPenghuni);
    }

    public void catatPembayaranBaru(Pembayaran pembayaranDataForm) {
        if (pembayaranDataForm.getIdPenghuni() == 0) {
            view.tampilkanPesan("Penghuni belum dipilih.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (pembayaranDataForm.getTanggalBayar() == null) {
            view.tampilkanPesan("Tanggal bayar wajib diisi.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (pembayaranDataForm.getJumlahBayar() <= 0) {
            view.tampilkanPesan("Jumlah bayar harus lebih dari 0.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Penghuni penghuni = penghuniDAO.getPenghuniById(pembayaranDataForm.getIdPenghuni());
        if (penghuni == null || penghuni.getNomorKamarDitempati() == null) {
            view.tampilkanPesan("Data penghuni atau informasi kamar tidak lengkap.", "Error Data Penghuni", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Kamar kamar = kamarDAO.getKamarByNomor(penghuni.getNomorKamarDitempati());
        if (kamar == null) {
            view.tampilkanPesan("Informasi harga sewa kamar tidak ditemukan untuk kamar " + penghuni.getNomorKamarDitempati(), "Error Data Kamar", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int hargaSewa = kamar.getHargaSewa();
        int jumlahBayarSaatIni = pembayaranDataForm.getJumlahBayar();
        int totalSudahBayarSebelumnya = pembayaranDAO.getTotalSudahBayarUntukPeriode(
                pembayaranDataForm.getIdPenghuni(),
                pembayaranDataForm.getBulanTagihan(),
                pembayaranDataForm.getTahunTagihan()
        );

        if (hargaSewa > 0) {
            int sisaTagihanPeriodeIniSebelumBayar = hargaSewa - totalSudahBayarSebelumnya;
            if (sisaTagihanPeriodeIniSebelumBayar <= 0) {
                if (jumlahBayarSaatIni > 0) { // Mencoba bayar padahal sudah lunas/lebih
                    view.tampilkanPesan("Tagihan untuk periode ini sudah lunas. Tidak dapat melakukan pembayaran lagi.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            } else if (jumlahBayarSaatIni > sisaTagihanPeriodeIniSebelumBayar) {
                view.tampilkanPesan("Jumlah bayar (Rp " + jumlahBayarSaatIni + ") melebihi sisa tagihan (Rp " + sisaTagihanPeriodeIniSebelumBayar + ").\n" +
                                     "Silakan bayar sejumlah sisa tagihan atau kurang.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else { // hargaSewa == 0
             if (jumlahBayarSaatIni > 0) {
                 System.out.println("Info: Mencatat pembayaran (Rp " + jumlahBayarSaatIni + ") untuk kamar dengan harga sewa 0.");
             }
        }
        
        int totalAkanTerbayarUntukPeriode = totalSudahBayarSebelumnya + jumlahBayarSaatIni;
        int sisaTagihanTerhitung = hargaSewa - totalAkanTerbayarUntukPeriode;
        pembayaranDataForm.setSisaTagihan(Math.max(0, sisaTagihanTerhitung)); 
        
        if (hargaSewa > 0) {
            pembayaranDataForm.setStatusPembayaran((pembayaranDataForm.getSisaTagihan() <= 0) ? "Lunas" : "Belum Lunas");
        } else {
            pembayaranDataForm.setStatusPembayaran("Lunas");
            pembayaranDataForm.setSisaTagihan(0);
        }

        boolean sukses = pembayaranDAO.catatPembayaran(pembayaranDataForm);
        if (sukses) {
            String pesanTambahan = "";
            if (hargaSewa > 0) {
                 pesanTambahan = ", Sisa tagihan periode ini: Rp " + pembayaranDataForm.getSisaTagihan();
            } else {
                 pesanTambahan = ". (Harga sewa kamar adalah Rp 0)";
            }
            view.tampilkanPesan("Pembayaran berhasil dicatat. Status periode: " + pembayaranDataForm.getStatusPembayaran() + pesanTambahan, "Sukses", JOptionPane.INFORMATION_MESSAGE);
            view.resetViewSetelahOperasiSukses();
        } else {
            view.tampilkanPesan("Gagal mencatat pembayaran ke database.", "Gagal Simpan", JOptionPane.ERROR_MESSAGE);
        }
    }
}