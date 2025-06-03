package model;

public class Kamar {
    private String nomorKamar;
    private String tipeKamar;
    private int hargaSewa;
    private String status;

    public Kamar(String nomorKamar, String tipeKamar, int hargaSewa, String status) {
        this.nomorKamar = nomorKamar;
        this.tipeKamar = tipeKamar;
        this.hargaSewa = hargaSewa;
        this.status = status;
    }

    // Getter dan Setter
    public String getNomorKamar() { return nomorKamar; }
    public void setNomorKamar(String nomorKamar) { this.nomorKamar = nomorKamar; }

    public String getTipeKamar() { return tipeKamar; }
    public void setTipeKamar(String tipeKamar) { this.tipeKamar = tipeKamar; }

    public int getHargaSewa() { return hargaSewa; }
    public void setHargaSewa(int hargaSewa) { this.hargaSewa = hargaSewa; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return nomorKamar + " (" + tipeKamar + " - Rp " + hargaSewa + ")";
    }
}