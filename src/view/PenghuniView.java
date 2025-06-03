package view;

import controller.PenghuniController;
import model.Penghuni;
import model.Kamar;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.UIManager; 
import com.toedter.calendar.JDateChooser;

public class PenghuniView extends javax.swing.JFrame {

    private PenghuniController penghuniController;
    private DefaultTableModel tableModelPenghuni;
    private boolean modeEdit = false;
    private int idPenghuniSedangDiedit = 0; 

    public PenghuniView() {
        initComponents(); 

        this.penghuniController = new PenghuniController(this);

        if (jTable_penghuni.getModel() instanceof DefaultTableModel) {
            this.tableModelPenghuni = (DefaultTableModel) jTable_penghuni.getModel();
        } else {
            this.tableModelPenghuni = new DefaultTableModel(
                new Object [][] {},
                new String [] {"ID", "NIK", "Nama", "Kontak", "No Kamar", "Tgl Mulai Sewa"}
            );
            jTable_penghuni.setModel(this.tableModelPenghuni);
        }
        this.tableModelPenghuni.setRowCount(0);


        jTable_penghuni.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Manajemen Data Penghuni");

        jDateChooser_tglSewa.setDateFormatString("dd-MM-yyyy");

        aturUIAwal(); 
        muatDataPenghuni(); 
        muatPilihanKamar(); 
        tambahkanTableListener(); 
    }

    private void aturUIAwal() {
        tampilkanFormInput(false); 

        jLabel1.setText("Manajemen Penghuni Kos");

        jButton_tambah.setEnabled(true);
        jButton_tambah.setVisible(true);
        jButton_edit.setEnabled(false);
        jButton_edit.setVisible(false); 
        jButton_hapus.setEnabled(false);
        jButton_hapus.setVisible(false);
    }

    private void tampilkanFormInput(boolean tampil) {
        jLabel2.setVisible(tampil);
        jTextField_idPenghuni.setVisible(tampil);
        jTextField_idPenghuni.setEditable(false); 
        jTextField_idPenghuni.setBackground(UIManager.getColor("TextField.disabledBackground"));

        jLabel8.setVisible(tampil);
        jTextField_NIK.setVisible(tampil);
        jTextField_NIK.setEditable(tampil);
        if(tampil) jTextField_NIK.setBackground(UIManager.getColor("TextField.background")); else jTextField_NIK.setBackground(UIManager.getColor("TextField.disabledBackground"));

        jLabel9.setVisible(tampil);
        jTextField_nama.setVisible(tampil);
        jTextField_nama.setEditable(tampil);
        if(tampil) jTextField_nama.setBackground(UIManager.getColor("TextField.background")); else jTextField_nama.setBackground(UIManager.getColor("TextField.disabledBackground"));

        jLabel10.setVisible(tampil); 
        jTextField_kontak.setVisible(tampil);
        jTextField_kontak.setEditable(tampil);
        if(tampil) jTextField_kontak.setBackground(UIManager.getColor("TextField.background")); else jTextField_kontak.setBackground(UIManager.getColor("TextField.disabledBackground"));
        
        jLabel11.setVisible(tampil);
        jComboBox_noKamar.setVisible(tampil); 
        jComboBox_noKamar.setEnabled(tampil);

        jLabel12.setVisible(tampil);
        jDateChooser_tglSewa.setVisible(tampil);
        jDateChooser_tglSewa.setEnabled(tampil);

        jButton_simpan.setVisible(tampil);
        jButton_simpan.setEnabled(tampil);
        jButton_batal.setVisible(tampil);
        jButton_batal.setEnabled(tampil);
        jButton_clear.setVisible(tampil);
        jButton_clear.setEnabled(tampil);
        
        jButton_tambah.setVisible(false);

        if (!tampil) {
            jLabel1.setText("Manajemen Penghuni Kos"); 
        }
    }

    private void kondisiFormTambah() {
        modeEdit = false;
        idPenghuniSedangDiedit = 0; 
        jLabel1.setText("Tambah Penghuni Baru");
        tampilkanFormInput(true);

        clearFormFields();
        jTextField_idPenghuni.setText("(Otomatis)");
        jTextField_idPenghuni.setVisible(false);
        jLabel2.setVisible(false);
        jDateChooser_tglSewa.setDate(new Date()); 

        jButton_tambah.setEnabled(false);
        jButton_tambah.setVisible(false);
        jButton_edit.setVisible(false); 
        jButton_hapus.setVisible(false);

        jTable_penghuni.clearSelection();
        jTextField_NIK.requestFocus();
    }

    private void kondisiFormEdit(Penghuni penghuni) {
        modeEdit = true;
        idPenghuniSedangDiedit = penghuni.getIdPenghuni();
        jLabel1.setText("Edit Data Penghuni: " + penghuni.getNamaPenghuni());
        tampilkanFormInput(true);

        jTextField_idPenghuni.setText(String.valueOf(penghuni.getIdPenghuni()));
        jTextField_NIK.setText(penghuni.getNik());
        jTextField_nama.setText(penghuni.getNamaPenghuni()); 
        jTextField_kontak.setText(penghuni.getNomorKontak());
        
        muatPilihanKamarUntukEdit(penghuni.getNomorKamarDitempati()); 
        jComboBox_noKamar.setSelectedItem(penghuni.getNomorKamarDitempati());
        
        jDateChooser_tglSewa.setDate(penghuni.getTanggalSewa());

        jButton_tambah.setEnabled(false);
        jButton_tambah.setVisible(false);
        jButton_edit.setVisible(false);
        jButton_hapus.setVisible(false);
        jTextField_NIK.requestFocus();
    }

    private void kondisiSetelahOperasiForm() {
        modeEdit = false;
        idPenghuniSedangDiedit = 0;
        clearFormFields();
        tampilkanFormInput(false);

        jButton_tambah.setEnabled(true);
        jButton_tambah.setVisible(true);
        
        boolean isRowSelected = jTable_penghuni.getSelectedRow() != -1;
        jButton_edit.setEnabled(isRowSelected);
        jButton_edit.setVisible(isRowSelected); 
        jButton_hapus.setEnabled(isRowSelected);
        jButton_hapus.setVisible(isRowSelected); 

        if (!isRowSelected) {
             jTable_penghuni.clearSelection(); 
        }
    }

    public void clearFormFields() {
        if (modeEdit) {
            Penghuni p = penghuniController.getPenghuniById(idPenghuniSedangDiedit);
            if (p != null) {
                jTextField_NIK.setText(p.getNik());
                jTextField_nama.setText(p.getNamaPenghuni());
                jTextField_kontak.setText(p.getNomorKontak());
                jComboBox_noKamar.setSelectedItem(p.getNomorKamarDitempati());
                jDateChooser_tglSewa.setDate(p.getTanggalSewa());
            } else { 
                resetInputFieldsToEmpty();
            }
        } else {
            resetInputFieldsToEmpty();
            jTextField_idPenghuni.setText("(Otomatis)"); 
        }
    }
    
    private void resetInputFieldsToEmpty() {
        jTextField_idPenghuni.setText(""); 
        jTextField_NIK.setText("");
        jTextField_nama.setText(""); 
        jTextField_kontak.setText(""); 
        if (jComboBox_noKamar.getItemCount() > 0) {
            jComboBox_noKamar.setSelectedIndex(0); 
        } else {
            jComboBox_noKamar.removeAllItems(); 
        }
        jDateChooser_tglSewa.setDate(null); 
    }


    public void muatDataPenghuni() {
        penghuniController.loadAllPenghuni();
    }

    public void tampilkanSemuaPenghuni(List<Penghuni> daftarPenghuni) {
        tableModelPenghuni.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (daftarPenghuni != null) {
            for (Penghuni p : daftarPenghuni) {
                String tglSewaStr = (p.getTanggalSewa() != null) ? sdf.format(p.getTanggalSewa()) : "-";
                tableModelPenghuni.addRow(new Object[]{
                    p.getIdPenghuni(),
                    p.getNik(),
                    p.getNamaPenghuni(),     
                    p.getNomorKontak(),    
                    p.getNomorKamarDitempati(),
                    tglSewaStr
                });
            }
        }
        jTable_penghuni.clearSelection(); 
        jButton_edit.setEnabled(false);
        jButton_edit.setVisible(false);
        jButton_hapus.setEnabled(false);
        jButton_hapus.setVisible(false);
    }

    public void muatPilihanKamar() {
        penghuniController.loadAvailableKamar();
    }

    public void muatPilihanKamarUntukEdit(String kamarDitempatiSaatIni) {
        penghuniController.loadAvailableKamarWithCurrent(kamarDitempatiSaatIni);
    }

    public void tampilkanPilihanKamar(List<Kamar> daftarKamar, String kamarUntukDiseleksi) {
        jComboBox_noKamar.removeAllItems();
        boolean kamarSeleksiAdaDiDaftar = false;

        if (daftarKamar != null) {
            for (Kamar kamar : daftarKamar) {
                jComboBox_noKamar.addItem(kamar.getNomorKamar());
                if (kamar.getNomorKamar().equals(kamarUntukDiseleksi)) {
                    kamarSeleksiAdaDiDaftar = true;
                }
            }
        }

        if (modeEdit && kamarUntukDiseleksi != null && !kamarSeleksiAdaDiDaftar) {
            boolean found = false;
            for (int i = 0; i < jComboBox_noKamar.getItemCount(); i++) {
                if (jComboBox_noKamar.getItemAt(i).equals(kamarUntukDiseleksi)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                 jComboBox_noKamar.addItem(kamarUntukDiseleksi);
            }
        }
        
        if (kamarUntukDiseleksi != null) {
            jComboBox_noKamar.setSelectedItem(kamarUntukDiseleksi);
        } else if (jComboBox_noKamar.getItemCount() > 0) {
            jComboBox_noKamar.setSelectedIndex(0); 
        }
    }

    public Penghuni getPenghuniFormData() {
        String nik = jTextField_NIK.getText().trim();
        String nama = jTextField_nama.getText().trim(); 
        String kontak = jTextField_kontak.getText().trim();
        String noKamar = (jComboBox_noKamar.getSelectedItem() != null) ? jComboBox_noKamar.getSelectedItem().toString() : null;
        Date tglSewa = jDateChooser_tglSewa.getDate();

        if (nik.isEmpty() || nama.isEmpty() || kontak.isEmpty() || noKamar == null || tglSewa == null) {
            tampilkanPesan("Semua field (NIK, Nama, Kontak, No Kamar, Tgl Mulai Sewa) wajib diisi.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        if (nik.length() != 16 || !nik.matches("\\d+")) {
            tampilkanPesan("NIK harus 16 digit angka.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        if (!kontak.matches("\\d{10,15}")) { 
            tampilkanPesan("Nomor kontak tidak valid (harus 10-15 digit angka).", "Input Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        if (modeEdit) {
            return new Penghuni(idPenghuniSedangDiedit, nik, nama, kontak, noKamar, tglSewa);
        } else {
            return new Penghuni(nik, nama, kontak, noKamar, tglSewa);
        }
    }

    public int getIdPenghuniTerpilih() { 
        int barisTerpilih = jTable_penghuni.getSelectedRow();
        if (barisTerpilih != -1) {
            Object idObj = tableModelPenghuni.getValueAt(barisTerpilih, 0);
            if (idObj instanceof Integer) {
                return (Integer) idObj;
            } else {
                try {
                    return Integer.parseInt(idObj.toString());
                } catch (NumberFormatException e) {
                    tampilkanPesan("Error membaca ID penghuni dari tabel.", "Error", JOptionPane.ERROR_MESSAGE);
                    return 0; 
                }
            }
        }
        return 0; 
    }

    public void tampilkanPesan(String pesan, String judul, int messageType) {
        JOptionPane.showMessageDialog(this, pesan, judul, messageType);
    }

    public void resetViewSetelahOperasiSukses() {
        kondisiSetelahOperasiForm();
        muatDataPenghuni();
        muatPilihanKamar(); 
    }

    private void tambahkanTableListener() {
        jTable_penghuni.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    boolean isRowSelected = jTable_penghuni.getSelectedRow() != -1;
                    if (!jButton_simpan.isVisible()) {
                        jButton_edit.setEnabled(isRowSelected);
                        jButton_edit.setVisible(isRowSelected);
                        jButton_hapus.setEnabled(isRowSelected);
                        jButton_hapus.setVisible(isRowSelected);
                    } else { 
                        jButton_edit.setVisible(false);
                        jButton_hapus.setVisible(false);
                    }
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_penghuni = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jButton_tambah = new javax.swing.JButton();
        jButton_edit = new javax.swing.JButton();
        jButton_hapus = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextField_idPenghuni = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField_NIK = new javax.swing.JTextField();
        jTextField_nama = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField_kontak = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jButton_simpan = new javax.swing.JButton();
        jButton_batal = new javax.swing.JButton();
        jButton_clear = new javax.swing.JButton();
        jComboBox_noKamar = new javax.swing.JComboBox<>();
        jButton_backMenuUtama = new javax.swing.JButton();
        jDateChooser_tglSewa = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(245, 247, 250));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 600));

        jTable_penghuni.setAutoCreateRowSorter(true);
        jTable_penghuni.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "NIK", "Nama", "Kontak", "No Kamar", "Tanggal Sewa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable_penghuni.setAutoscrolls(false);
        jTable_penghuni.setFillsViewportHeight(true);
        jTable_penghuni.setRowHeight(24);
        jScrollPane1.setViewportView(jTable_penghuni);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Manajemen Penghuni Kos");

        jButton_tambah.setBackground(new java.awt.Color(25, 135, 84));
        jButton_tambah.setForeground(new java.awt.Color(255, 255, 255));
        jButton_tambah.setText("Tambah Data Penghuni");
        jButton_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_tambahActionPerformed(evt);
            }
        });

        jButton_edit.setBackground(new java.awt.Color(255, 193, 7));
        jButton_edit.setForeground(new java.awt.Color(255, 255, 255));
        jButton_edit.setText("Edit");
        jButton_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_editActionPerformed(evt);
            }
        });

        jButton_hapus.setBackground(new java.awt.Color(220, 53, 69));
        jButton_hapus.setForeground(new java.awt.Color(255, 255, 255));
        jButton_hapus.setText("Hapus");
        jButton_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_hapusActionPerformed(evt);
            }
        });

        jLabel2.setText("ID");

        jTextField_idPenghuni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_idPenghuniActionPerformed(evt);
            }
        });

        jLabel8.setText("NIK");

        jTextField_NIK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_NIKActionPerformed(evt);
            }
        });

        jTextField_nama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_namaActionPerformed(evt);
            }
        });

        jLabel9.setText("Nama");

        jLabel10.setText("Kontak");

        jTextField_kontak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_kontakActionPerformed(evt);
            }
        });

        jLabel11.setText("No Kamar");

        jLabel12.setText("Tanggal Sewa");

        jButton_simpan.setBackground(new java.awt.Color(25, 135, 84));
        jButton_simpan.setForeground(new java.awt.Color(255, 255, 255));
        jButton_simpan.setText("Simpan");
        jButton_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_simpanActionPerformed(evt);
            }
        });

        jButton_batal.setBackground(new java.awt.Color(108, 117, 125));
        jButton_batal.setForeground(new java.awt.Color(255, 255, 255));
        jButton_batal.setText("Batal");
        jButton_batal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_batalActionPerformed(evt);
            }
        });

        jButton_clear.setBackground(new java.awt.Color(108, 117, 125));
        jButton_clear.setForeground(new java.awt.Color(255, 255, 255));
        jButton_clear.setText("Clear");
        jButton_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_clearActionPerformed(evt);
            }
        });

        jComboBox_noKamar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton_backMenuUtama.setText("Kembali");
        jButton_backMenuUtama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_backMenuUtamaActionPerformed(evt);
            }
        });

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("© Copyright 2025. All Rights Reserved By SiKos");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton_backMenuUtama)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 681, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12))
                                .addGap(6, 6, 6)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField_kontak)
                                    .addComponent(jComboBox_noKamar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jDateChooser_tglSewa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField_idPenghuni, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField_NIK)
                                    .addComponent(jTextField_nama)))
                            .addComponent(jButton_tambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton_simpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(24, 24, 24))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton_tambah)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_hapus)
                            .addComponent(jButton_edit))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextField_idPenghuni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jTextField_NIK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jTextField_nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jTextField_kontak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jComboBox_noKamar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel12)
                            .addComponent(jDateChooser_tglSewa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addComponent(jButton_simpan)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_clear)
                            .addComponent(jButton_batal)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton_backMenuUtama)
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1012, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 612, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_editActionPerformed
        // TODO add your handling code here:
        int idTerpilih = getIdPenghuniTerpilih(); 
        if (idTerpilih != 0) { 
            Penghuni penghuni = penghuniController.getPenghuniById(idTerpilih);
            if (penghuni != null) {
                kondisiFormEdit(penghuni);
            } else {
                tampilkanPesan("Data penghuni tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            tampilkanPesan("Pilih data penghuni yang akan diedit dari tabel.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton_editActionPerformed

    private void jButton_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_hapusActionPerformed
        // TODO add your handling code here:
        int idTerpilih = getIdPenghuniTerpilih();
        if (idTerpilih != 0) {
            Penghuni p = penghuniController.getPenghuniById(idTerpilih);
            String infoPenghuni = (p != null) ? p.getNamaPenghuni() + " (Kamar " + p.getNomorKamarDitempati() + ")" : "dengan ID " + idTerpilih;
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Anda yakin ingin menghapus penghuni: " + infoPenghuni + "?\nOperasi ini juga akan mengosongkan status kamar yang bersangkutan.", 
                    "Konfirmasi Hapus", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                penghuniController.hapusPenghuni(idTerpilih, (p != null ? p.getNomorKamarDitempati() : null));
            }
        } else {
            tampilkanPesan("Pilih data penghuni yang akan dihapus dari tabel.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton_hapusActionPerformed

    private void jTextField_idPenghuniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_idPenghuniActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_idPenghuniActionPerformed

    private void jTextField_NIKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_NIKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_NIKActionPerformed

    private void jTextField_namaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_namaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_namaActionPerformed

    private void jTextField_kontakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_kontakActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_kontakActionPerformed

    private void jButton_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_simpanActionPerformed
        // TODO add your handling code here:
        Penghuni penghuni = getPenghuniFormData();
        if (penghuni != null) {
            penghuniController.simpanAtauUpdatePenghuni(penghuni, modeEdit, idPenghuniSedangDiedit);
        }
    }//GEN-LAST:event_jButton_simpanActionPerformed

    private void jButton_backMenuUtamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_backMenuUtamaActionPerformed
        // TODO add your handling code here:
        new DashboardView().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton_backMenuUtamaActionPerformed

    private void jButton_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_tambahActionPerformed
        kondisiFormTambah();
    }//GEN-LAST:event_jButton_tambahActionPerformed

    private void jButton_batalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_batalActionPerformed
        // TODO add your handling code here:
        kondisiSetelahOperasiForm();
        muatPilihanKamar(); 
    }//GEN-LAST:event_jButton_batalActionPerformed

    private void jButton_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_clearActionPerformed
        // TODO add your handling code here:
        clearFormFields();
    }//GEN-LAST:event_jButton_clearActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PenghuniView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PenghuniView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PenghuniView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PenghuniView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PenghuniView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_backMenuUtama;
    private javax.swing.JButton jButton_batal;
    private javax.swing.JButton jButton_clear;
    private javax.swing.JButton jButton_edit;
    private javax.swing.JButton jButton_hapus;
    private javax.swing.JButton jButton_simpan;
    private javax.swing.JButton jButton_tambah;
    private javax.swing.JComboBox<String> jComboBox_noKamar;
    private com.toedter.calendar.JDateChooser jDateChooser_tglSewa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_penghuni;
    private javax.swing.JTextField jTextField_NIK;
    private javax.swing.JTextField jTextField_idPenghuni;
    private javax.swing.JTextField jTextField_kontak;
    private javax.swing.JTextField jTextField_nama;
    // End of variables declaration//GEN-END:variables
}
