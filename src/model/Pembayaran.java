package model;

import java.util.Date;

public class Pembayaran {
    private int idPembayaran;
    private int idPenghuni;
    private int bulanTagihan;
    private int tahunTagihan;
    private Date tanggalBayar;
    private int jumlahBayar;
    private String statusPembayaran; 
    private int sisaTagihan;

    public Pembayaran(int idPembayaran, int idPenghuni, int bulanTagihan, int tahunTagihan, Date tanggalBayar, int jumlahBayar, String statusPembayaran, int sisaTagihan) {
        this.idPembayaran = idPembayaran;
        this.idPenghuni = idPenghuni;
        this.bulanTagihan = bulanTagihan;
        this.tahunTagihan = tahunTagihan;
        this.tanggalBayar = tanggalBayar;
        this.jumlahBayar = jumlahBayar;
        this.statusPembayaran = statusPembayaran;
        this.sisaTagihan = sisaTagihan;
    }

    public Pembayaran(int idPenghuni, int bulanTagihan, int tahunTagihan, Date tanggalBayar, int jumlahBayar, String statusPembayaran, int sisaTagihan) {
        this.idPenghuni = idPenghuni;
        this.bulanTagihan = bulanTagihan;
        this.tahunTagihan = tahunTagihan;
        this.tanggalBayar = tanggalBayar;
        this.jumlahBayar = jumlahBayar;
        this.statusPembayaran = statusPembayaran;
        this.sisaTagihan = sisaTagihan;
    }

    // Getter dan Setter
    public int getIdPembayaran() { return idPembayaran; }
    public void setIdPembayaran(int idPembayaran) { this.idPembayaran = idPembayaran; }
    public int getIdPenghuni() { return idPenghuni; }
    public void setIdPenghuni(int idPenghuni) { this.idPenghuni = idPenghuni; }
    public int getBulanTagihan() { return bulanTagihan; } 
    public void setBulanTagihan(int bulanTagihan) { this.bulanTagihan = bulanTagihan; }
    public int getTahunTagihan() { return tahunTagihan; }
    public void setTahunTagihan(int tahunTagihan) { this.tahunTagihan = tahunTagihan; }
    public Date getTanggalBayar() { return tanggalBayar; }
    public void setTanggalBayar(Date tanggalBayar) { this.tanggalBayar = tanggalBayar; }
    public int getJumlahBayar() { return jumlahBayar; }
    public void setJumlahBayar(int jumlahBayar) { this.jumlahBayar = jumlahBayar; }
    public String getStatusPembayaran() { return statusPembayaran; }
    public void setStatusPembayaran(String statusPembayaran) { this.statusPembayaran = statusPembayaran; }
    public int getSisaTagihan() { return sisaTagihan; }
    public void setSisaTagihan(int sisaTagihan) { this.sisaTagihan = sisaTagihan; }
}