package model;

import java.util.Date;

public class Penghuni {
    private int idPenghuni;
    private String nik;
    private String namaPenghuni;
    private String nomorKontak;
    private String nomorKamarDitempati;
    private Date tanggalSewa;

    public Penghuni(String nik, String namaPenghuni, String nomorKontak, String nomorKamarDitempati, Date tanggalSewa) {
        this.nik = nik;
        this.namaPenghuni = namaPenghuni;
        this.nomorKontak = nomorKontak;
        this.nomorKamarDitempati = nomorKamarDitempati;
        this.tanggalSewa = tanggalSewa;
    }

    public Penghuni(int idPenghuni, String nik, String namaPenghuni, String nomorKontak, String nomorKamarDitempati, Date tanggalSewa) {
        this.idPenghuni = idPenghuni;
        this.nik = nik;
        this.namaPenghuni = namaPenghuni;
        this.nomorKontak = nomorKontak;
        this.nomorKamarDitempati = nomorKamarDitempati;
        this.tanggalSewa = tanggalSewa;
    }
    
    // Getter dan Setter
    public int getIdPenghuni() { return idPenghuni; }
    public void setIdPenghuni(int idPenghuni) { this.idPenghuni = idPenghuni; }

    public String getNik() { return nik; }
    public void setNik(String nik) { this.nik = nik; }

    public String getNamaPenghuni() { return namaPenghuni; }
    public void setNamaPenghuni(String namaPenghuni) { this.namaPenghuni = namaPenghuni; }

    public String getNomorKontak() { return nomorKontak; }
    public void setNomorKontak(String nomorKontak) { this.nomorKontak = nomorKontak; }

    public String getNomorKamarDitempati() { return nomorKamarDitempati; }
    public void setNomorKamarDitempati(String nomorKamarDitempati) { this.nomorKamarDitempati = nomorKamarDitempati; }

    public Date getTanggalSewa() { return tanggalSewa; }
    public void setTanggalSewa(Date tanggalSewa) { this.tanggalSewa = tanggalSewa; }

    @Override
    public String toString() { 
        return namaPenghuni + " (NIK: " + nik + ")";
    }
}