package view;

import controller.KamarController;
import model.Kamar;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.table.DefaultTableCellRenderer; 

public class KamarView extends javax.swing.JFrame {

    private KamarController kamarController;
    private DefaultTableModel tableModelKamar;
    private boolean modeEdit = false;
    private String nomorKamarSedangDiedit = null;

    public KamarView() {
        initComponents();
        this.kamarController = new KamarController(this);

        if (jTable_kamar.getModel() instanceof DefaultTableModel) {
            this.tableModelKamar = (DefaultTableModel) jTable_kamar.getModel();
        } else {
            this.tableModelKamar = new DefaultTableModel(
                new Object [][] {},
                new String [] {"No Kamar", "Tipe Kamar", "Harga Sewa", "Status"}
            );
            jTable_kamar.setModel(this.tableModelKamar);
        }
        this.tableModelKamar.setRowCount(0);

        jTable_kamar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Manajemen Data Kamar");

        aturUIAwal();
        muatDataKamar();
        tambahkanTableListener();
    }

    private void aturUIAwal() {
        jLabel_nama.setVisible(false);
        jTextField_noKamar.setVisible(false);
        jLabel_tipe.setVisible(false);
        jTextField_tipeKamar.setVisible(false);
        jLabel_harga.setVisible(false);
        jTextField_HargaSewa.setVisible(false);
        jLabel_status.setVisible(false);
        jComboBox_statusKamar.setVisible(false);
        jButton_simpan.setVisible(false);
        jButton_batal.setVisible(false);
        jButton_clear.setVisible(false);

        jTextField_noKamar.setEditable(false);
        jTextField_tipeKamar.setEditable(false);
        jTextField_HargaSewa.setEditable(false);
        jComboBox_statusKamar.setEnabled(false);
        jLabel1.setText("Manajemen Kamar Kos");

        jButton_tambah.setEnabled(true);
        jButton_tambah.setVisible(true);
        jButton_edit.setEnabled(false);
        jButton_edit.setVisible(false);
        jButton_hapus.setEnabled(false);
        jButton_hapus.setVisible(false);
    }

    private void tampilkanFormInput(boolean tampil) {
        jLabel_nama.setVisible(tampil);
        jTextField_noKamar.setVisible(tampil);
        jLabel_tipe.setVisible(tampil);
        jTextField_tipeKamar.setVisible(tampil);
        jTextField_tipeKamar.setEditable(tampil);
        jLabel_harga.setVisible(tampil);
        jTextField_HargaSewa.setVisible(tampil);
        jTextField_HargaSewa.setEditable(tampil);
        jLabel_status.setVisible(tampil);
        jComboBox_statusKamar.setVisible(tampil);
        jComboBox_statusKamar.setEnabled(tampil);
        jButton_simpan.setVisible(tampil);
        jButton_simpan.setEnabled(tampil);
        jButton_batal.setVisible(tampil);
        jButton_batal.setEnabled(tampil);
        jButton_clear.setVisible(tampil);
        jButton_clear.setEnabled(tampil);
        jButton_tambah.setVisible(false);

        if (!tampil) {
            jLabel1.setText("Manajemen Kamar Kos");
            jTextField_noKamar.setEditable(false);
        }
    }

    private void kondisiFormTambah() {
        modeEdit = false;
        nomorKamarSedangDiedit = null;
        jLabel1.setText("Tambah Kamar Baru");
        tampilkanFormInput(true);

        jTextField_noKamar.setEditable(true);
        jTextField_noKamar.setText("");
        jTextField_tipeKamar.setText("");
        jTextField_HargaSewa.setText("");
        jComboBox_statusKamar.setSelectedIndex(0);

        jButton_tambah.setEnabled(false);
        jButton_tambah.setVisible(false);
        jButton_edit.setVisible(false);
        jButton_hapus.setVisible(false);
        jTable_kamar.clearSelection();
        jTextField_noKamar.requestFocus();
    }

    private void kondisiFormEdit(Kamar kamar) {
        modeEdit = true;
        nomorKamarSedangDiedit = kamar.getNomorKamar();
        jLabel1.setText("Edit Kamar: " + nomorKamarSedangDiedit);
        tampilkanFormInput(true);

        jTextField_noKamar.setText(kamar.getNomorKamar());
        jTextField_noKamar.setEditable(false);
        jTextField_tipeKamar.setText(kamar.getTipeKamar());
        jTextField_HargaSewa.setText(String.valueOf(kamar.getHargaSewa()));
        jComboBox_statusKamar.setSelectedItem(kamar.getStatus());

        jButton_tambah.setEnabled(false);
        jButton_tambah.setVisible(false);
        jButton_edit.setVisible(false);
        jButton_hapus.setVisible(false);
        jTextField_tipeKamar.requestFocus();
    }

    private void kondisiSetelahOperasiForm() {
        modeEdit = false;
        nomorKamarSedangDiedit = null;
        clearFormFields();
        tampilkanFormInput(false);

        jButton_tambah.setEnabled(true);
        jButton_tambah.setVisible(true);

        boolean isRowSelected = jTable_kamar.getSelectedRow() != -1;
        jButton_edit.setEnabled(isRowSelected);
        jButton_edit.setVisible(isRowSelected);
        jButton_hapus.setEnabled(isRowSelected);
        jButton_hapus.setVisible(isRowSelected);

        if (!isRowSelected) {
            jTable_kamar.clearSelection();
        }
    }

    public void clearFormFields() {
        if (modeEdit && nomorKamarSedangDiedit != null) {
            Kamar kamar = kamarController.getKamarByNomor(nomorKamarSedangDiedit);
            if (kamar != null) {
                jTextField_tipeKamar.setText(kamar.getTipeKamar());
                jTextField_HargaSewa.setText(String.valueOf(kamar.getHargaSewa()));
                jComboBox_statusKamar.setSelectedItem(kamar.getStatus());
            } else {
                jTextField_tipeKamar.setText("");
                jTextField_HargaSewa.setText("");
                jComboBox_statusKamar.setSelectedIndex(0);
            }
        } else {
            jTextField_noKamar.setText("");
            jTextField_tipeKamar.setText("");
            jTextField_HargaSewa.setText("");
            jComboBox_statusKamar.setSelectedIndex(0);
        }
    }

    public void muatDataKamar() {
        kamarController.loadAllKamar();
    }

    public void tampilkanSemuaKamar(List<Kamar> daftarKamar) {
        tableModelKamar.setRowCount(0);
        if (daftarKamar != null) {
            for (Kamar kamar : daftarKamar) {
                tableModelKamar.addRow(new Object[]{
                    kamar.getNomorKamar(),
                    kamar.getTipeKamar(),
                    kamar.getHargaSewa(),
                    kamar.getStatus()
                });
            }
        }
        jTable_kamar.clearSelection();
        jButton_edit.setEnabled(false);
        jButton_edit.setVisible(false);
        jButton_hapus.setEnabled(false);
        jButton_hapus.setVisible(false);
    }

    public Kamar getKamarFormData() {
        String nomor = jTextField_noKamar.getText().trim();
        String tipe = jTextField_tipeKamar.getText().trim();
        String hargaStr = jTextField_HargaSewa.getText().trim();

        if ((!modeEdit && nomor.isEmpty()) || tipe.isEmpty() || hargaStr.isEmpty()) {
            String fieldKosong = "";
            if (!modeEdit && nomor.isEmpty()) fieldKosong += "No Kamar, ";
            if (tipe.isEmpty()) fieldKosong += "Tipe Kamar, ";
            if (hargaStr.isEmpty()) fieldKosong += "Harga Sewa, ";
            if (fieldKosong.endsWith(", ")) fieldKosong = fieldKosong.substring(0, fieldKosong.length() - 2);
            tampilkanPesan(fieldKosong + " wajib diisi!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        int harga;
        try {
            harga = Integer.parseInt(hargaStr);
            if (harga < 0) {
                tampilkanPesan("Harga sewa tidak boleh negatif!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } catch (NumberFormatException e) {
            tampilkanPesan("Harga sewa harus berupa angka yang valid!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        String status = (String) jComboBox_statusKamar.getSelectedItem();

        String nomorUntukObjek = modeEdit ? nomorKamarSedangDiedit : nomor;
        return new Kamar(nomorUntukObjek, tipe, harga, status);
    }

    public String getNomorKamarTerpilih() {
        int barisTerpilih = jTable_kamar.getSelectedRow();
        if (barisTerpilih != -1) {
            return (String) tableModelKamar.getValueAt(barisTerpilih, 0);
        }
        return null;
    }

    public boolean isModeEdit() {
        return modeEdit;
    }

    public String getNomorKamarUntukUpdate() {
        return nomorKamarSedangDiedit;
    }

    public void tampilkanPesan(String pesan, String judul, int messageType) {
        JOptionPane.showMessageDialog(this, pesan, judul, messageType);
    }

    public void resetViewSetelahOperasiSukses() {
        kondisiSetelahOperasiForm();
        muatDataKamar();
    }

    private void tambahkanTableListener() {
        jTable_kamar.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    boolean isRowSelected = jTable_kamar.getSelectedRow() != -1;
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

        jLabel8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_kamar = new javax.swing.JTable();
        jButton_tambah = new javax.swing.JButton();
        jButton_edit = new javax.swing.JButton();
        jButton_hapus = new javax.swing.JButton();
        jButton_batal = new javax.swing.JButton();
        jLabel_nama = new javax.swing.JLabel();
        jTextField_noKamar = new javax.swing.JTextField();
        jTextField_tipeKamar = new javax.swing.JTextField();
        jLabel_tipe = new javax.swing.JLabel();
        jTextField_HargaSewa = new javax.swing.JTextField();
        jLabel_harga = new javax.swing.JLabel();
        jLabel_status = new javax.swing.JLabel();
        jComboBox_statusKamar = new javax.swing.JComboBox<>();
        jButton_simpan = new javax.swing.JButton();
        jButton_clear = new javax.swing.JButton();
        jButton_backMenuUtama = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("© Copyright 2025. All Rights Reserved By SiKos");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(245, 247, 250));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 600));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Manajemen Kamar Kos");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jTable_kamar.setAutoCreateRowSorter(true);
        jTable_kamar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "No Kamar", "Tipe Kamar", "Harga Sewa", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable_kamar.setFillsViewportHeight(true);
        jTable_kamar.setRowHeight(24);
        jScrollPane1.setViewportView(jTable_kamar);

        jButton_tambah.setBackground(new java.awt.Color(25, 135, 84));
        jButton_tambah.setForeground(new java.awt.Color(255, 255, 255));
        jButton_tambah.setText(" Tambah Data Kamar");
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

        jButton_batal.setBackground(new java.awt.Color(108, 117, 125));
        jButton_batal.setForeground(new java.awt.Color(255, 255, 255));
        jButton_batal.setText("Batal");
        jButton_batal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_batalActionPerformed(evt);
            }
        });

        jLabel_nama.setText("No Kamar");

        jLabel_tipe.setText("Tipe Kamar");

        jLabel_harga.setText("Harga Sewa");

        jLabel_status.setText("Status");

        jComboBox_statusKamar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kosong", "Terisi" }));

        jButton_simpan.setBackground(new java.awt.Color(25, 135, 84));
        jButton_simpan.setForeground(new java.awt.Color(255, 255, 255));
        jButton_simpan.setText("Simpan");
        jButton_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_simpanActionPerformed(evt);
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

        jButton_backMenuUtama.setText("Kembali");
        jButton_backMenuUtama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_backMenuUtamaActionPerformed(evt);
            }
        });

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("© Copyright 2025. All Rights Reserved By SiKos");

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
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jButton_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jButton_simpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel_harga)
                                        .addComponent(jLabel_status))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jComboBox_statusKamar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField_HargaSewa, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel_nama)
                                        .addComponent(jLabel_tipe))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextField_tipeKamar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField_noKamar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(24, 24, 24))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 1000, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                            .addComponent(jButton_edit)
                            .addComponent(jButton_hapus))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_nama)
                            .addComponent(jTextField_noKamar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_tipe)
                            .addComponent(jTextField_tipeKamar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_harga)
                            .addComponent(jTextField_HargaSewa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_status)
                            .addComponent(jComboBox_statusKamar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addComponent(jButton_simpan)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_batal)
                            .addComponent(jButton_clear)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton_backMenuUtama)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1012, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 612, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_editActionPerformed
        // TODO add your handling code here:
        String nomorKamar = getNomorKamarTerpilih();
        if (nomorKamar != null) {
            Kamar kamar = kamarController.getKamarByNomor(nomorKamar);
            if (kamar != null) {
                kondisiFormEdit(kamar);
            } else {
                tampilkanPesan("Data kamar '" + nomorKamar + "' tidak ditemukan untuk diedit.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            tampilkanPesan("Pilih kamar dari tabel untuk diedit.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton_editActionPerformed

    private void jButton_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_hapusActionPerformed
        // TODO add your handling code here:
        String nomorKamar = getNomorKamarTerpilih();
        if (nomorKamar != null) {
            kamarController.hapusKamarTerpilih();
        } else {
            tampilkanPesan("Pilih kamar dari tabel untuk dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton_hapusActionPerformed

    private void jButton_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_simpanActionPerformed
        // TODO add your handling code here:
        kamarController.simpanKamar();
        
    }//GEN-LAST:event_jButton_simpanActionPerformed

    private void jButton_backMenuUtamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_backMenuUtamaActionPerformed
        // TODO add your handling code here:
        new DashboardView().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton_backMenuUtamaActionPerformed

    private void jButton_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_tambahActionPerformed
        // TODO add your handling code here:
        kondisiFormTambah();
    }//GEN-LAST:event_jButton_tambahActionPerformed

    private void jButton_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_clearActionPerformed
        // TODO add your handling code here:
        clearFormFields(); 
    }//GEN-LAST:event_jButton_clearActionPerformed

    private void jButton_batalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_batalActionPerformed
        // TODO add your handling code here:
        kondisiSetelahOperasiForm(); 
    }//GEN-LAST:event_jButton_batalActionPerformed

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
            java.util.logging.Logger.getLogger(KamarView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(KamarView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(KamarView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(KamarView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new KamarView().setVisible(true);
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
    private javax.swing.JComboBox<String> jComboBox_statusKamar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel_harga;
    private javax.swing.JLabel jLabel_nama;
    private javax.swing.JLabel jLabel_status;
    private javax.swing.JLabel jLabel_tipe;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_kamar;
    private javax.swing.JTextField jTextField_HargaSewa;
    private javax.swing.JTextField jTextField_noKamar;
    private javax.swing.JTextField jTextField_tipeKamar;
    // End of variables declaration//GEN-END:variables
}
