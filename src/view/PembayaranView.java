package view;

import controller.PembayaranController;
import model.Penghuni; 
import model.Pembayaran; 

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.DefaultComboBoxModel;
import com.toedter.calendar.JDateChooser; 
import com.toedter.calendar.JMonthChooser; 
import com.toedter.calendar.JYearChooser;  
import java.util.Calendar; 
import java.util.Locale; 
import java.text.NumberFormat; 

public class PembayaranView extends javax.swing.JFrame {

    private PembayaranController pembayaranController;
    private DefaultTableModel tableModelPembayaran;
    private List<Penghuni> daftarPenghuniUntukComboBox; 

    public PembayaranView() {
        initComponents(); 
        this.pembayaranController = new PembayaranController(this);

        String[] columnNames = {"Nama", "No Kamar", "Periode", "Tanggal Bayar", "Jumlah Bayar", "Status", "Sisa Tagihan"};
        this.tableModelPembayaran = new DefaultTableModel(new Object[][]{}, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4 || columnIndex == 6) return String.class; 
                if (columnIndex == 3) return String.class; 
                return String.class;
            }
        };
        jTable_pembayaran.setModel(this.tableModelPembayaran);


        jTable_pembayaran.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Manajemen Pembayaran Kos");

        jDateChooser_TglBayar.setDateFormatString("dd-MM-yyyy");
        setupComboBoxStatus(); 
        setupDefaultPeriode();

        jComboBox_nama.addActionListener(evt -> jComboBox_namaActionPerformed(evt));
        
        jComboBox_noKamar.setEnabled(false);
        jComboBox_status.setEditable(false); 

        aturUIAwal();
        muatDataPembayaran();
        pembayaranController.loadPenghuniUntukComboBox(); 
    }
    
    private void setupComboBoxStatus() {
        jComboBox_status.setModel(new DefaultComboBoxModel<>(new String[]{"Belum Lunas", "Lunas"}));
        jComboBox_status.setSelectedItem("Belum Lunas"); 
    }

    private void setupDefaultPeriode() {
        Calendar now = Calendar.getInstance();
        jMonthChooser_BulanTghn.setMonth(now.get(Calendar.MONTH)); 
        jYearChooser_TahunTghn.setYear(now.get(Calendar.YEAR));
        jMonthChooser_BulanTghn.setLocale(new Locale("id", "ID")); 
    }

    private void aturUIAwal() {
        tampilkanFormInput(false);
        jLabel1.setText("Manajemen Pembayaran Kos"); 
        jButton_tambah.setEnabled(true);
        jButton_tambah.setVisible(true);
    }

    private void tampilkanFormInput(boolean tampil) {
        jLabel2.setVisible(tampil); 
        jComboBox_nama.setVisible(tampil);
        jComboBox_nama.setEnabled(tampil);

        jLabel8.setVisible(tampil); 
        jComboBox_noKamar.setVisible(tampil); 
        jComboBox_noKamar.setEnabled(false);  

        jLabel9.setVisible(tampil); 
        jMonthChooser_BulanTghn.setVisible(tampil); 
        jMonthChooser_BulanTghn.setEnabled(tampil);

        jLabel10.setVisible(tampil); 
        jYearChooser_TahunTghn.setVisible(tampil); 
        jYearChooser_TahunTghn.setEnabled(tampil);

        jLabel11.setVisible(tampil); 
        jDateChooser_TglBayar.setVisible(tampil); 
        jDateChooser_TglBayar.setEnabled(tampil);

        jLabel12.setVisible(tampil); 
        jTextField_jumlahBayar.setVisible(tampil);
        jTextField_jumlahBayar.setEditable(tampil);

        jLabel13.setVisible(false); 
        jComboBox_status.setVisible(false);
        jComboBox_status.setEnabled(false);
        
        jButton_tambah.setVisible(false);
        jButton_simpan.setVisible(tampil);
        jButton_simpan.setEnabled(tampil);
        jButton_batal.setVisible(tampil);
        jButton_batal.setEnabled(tampil);
        jButton_clear.setVisible(tampil);
        jButton_clear.setEnabled(tampil);
        
        if (!tampil) {
            jLabel1.setText("Manajemen Pembayaran Kos");
        }
    }

    private void kondisiFormTambah() {
        jLabel1.setText("Catat Pembayaran Baru");
        tampilkanFormInput(true);
        clearFormFields(); 
        jDateChooser_TglBayar.setDate(new Date()); 
        jComboBox_nama.requestFocus();
        jButton_tambah.setEnabled(false); 
    }

    private void kondisiSetelahOperasiForm() {
        clearFormFields();
        tampilkanFormInput(false);
        jLabel1.setText("Manajemen Pembayaran Kos"); 
        jButton_tambah.setVisible(true);
        jButton_tambah.setEnabled(true);
        jTable_pembayaran.clearSelection();
    }

    public void clearFormFields() {
        if (jComboBox_nama.getItemCount() > 0) { 
            jComboBox_nama.setSelectedIndex(0);
            jComboBox_namaActionPerformed(null); 
        } else {
            jComboBox_noKamar.removeAllItems();
            jComboBox_noKamar.addItem("-");
        }
        setupDefaultPeriode(); 
        jDateChooser_TglBayar.setDate(null);
        jTextField_jumlahBayar.setText("");
        if (jComboBox_status.getItemCount() > 0) {
            jComboBox_status.setSelectedItem("Belum Lunas"); 
        }
    }
    
    public void muatDataPembayaran() {
        pembayaranController.loadSemuaPeriodeTagihanPerPenghuni();
    }
    
    public void tampilkanSemuaPembayaran(List<Object[]> daftarPembayaranObjects) { 
        tableModelPembayaran.setRowCount(0); 
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        if (daftarPembayaranObjects != null) {
            for (Object[] rowData : daftarPembayaranObjects) {
                Object[] displayRow = new Object[7]; 
                displayRow[0] = rowData[0]; 
                displayRow[1] = rowData[1]; 
                displayRow[2] = rowData[2]; 
                if (rowData[3] instanceof Date) { 
                    displayRow[3] = sdf.format((Date)rowData[3]); 
                } else {
                    displayRow[3] = "-";
                }
                if (rowData[4] instanceof Number) {
                     displayRow[4] = currencyFormat.format(((Number)rowData[4]).doubleValue());
                } else {
                     displayRow[4] = rowData[4];
                }
                displayRow[5] = rowData[5]; 
                if (rowData[6] instanceof Number) {
                     displayRow[6] = currencyFormat.format(((Number)rowData[6]).doubleValue());
                } else {
                     displayRow[6] = rowData[6];
                }
                tableModelPembayaran.addRow(displayRow);
            }
        }
        jTable_pembayaran.clearSelection();
    }
    
    public void isiComboBoxPenghuni(List<Penghuni> daftarPenghuni) { 
        this.daftarPenghuniUntukComboBox = daftarPenghuni; 
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        jComboBox_noKamar.removeAllItems();

        if (daftarPenghuni != null && !daftarPenghuni.isEmpty()) {
            for (Penghuni p : daftarPenghuni) {
                model.addElement(p.getNamaPenghuni()); 
            }
            jComboBox_nama.setModel(model); 
            jComboBox_nama.setSelectedIndex(0);
            jComboBox_namaActionPerformed(null); 
        } else {
            jComboBox_nama.setModel(model); 
            jComboBox_noKamar.addItem("-");
        }
    }

    public void resetViewSetelahOperasiSukses() { 
        kondisiSetelahOperasiForm();
        muatDataPembayaran();
        pembayaranController.loadPenghuniUntukComboBox(); 
    }
    public void tampilkanPesan(String pesan, String judul, int messageType) { 
        JOptionPane.showMessageDialog(this, pesan, judul, messageType);
    }

    public Pembayaran getPembayaranFormData() {
        int selectedPenghuniIndex = jComboBox_nama.getSelectedIndex();
        if (selectedPenghuniIndex < 0 || daftarPenghuniUntukComboBox == null || daftarPenghuniUntukComboBox.isEmpty()) {
            tampilkanPesan("Pilih penghuni terlebih dahulu.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        Penghuni penghuniTerpilih = daftarPenghuniUntukComboBox.get(selectedPenghuniIndex);
        int idPenghuni = penghuniTerpilih.getIdPenghuni();

        int bulanTagihan = jMonthChooser_BulanTghn.getMonth() + 1; 
        int tahunTagihan = jYearChooser_TahunTghn.getYear();
        Date tanggalBayar = jDateChooser_TglBayar.getDate(); 
        String jumlahBayarStr = jTextField_jumlahBayar.getText().trim();
        
        String statusPembayaranPilihanUser = jComboBox_status.getSelectedItem().toString();
        
        int sisaTagihanAwal = 0;

        if (tanggalBayar == null || jumlahBayarStr.isEmpty()) {
            tampilkanPesan("Field Penghuni, Periode, Tanggal Bayar, dan Jumlah Bayar wajib diisi.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        int jumlahBayar;
        try {
            jumlahBayar = Integer.parseInt(jumlahBayarStr);
             if (jumlahBayar <= 0) { 
                 tampilkanPesan("Jumlah bayar harus lebih dari 0.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } catch (NumberFormatException e) {
            tampilkanPesan("Jumlah Bayar harus berupa angka.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
                
        return new Pembayaran(idPenghuni, bulanTagihan, tahunTagihan, tanggalBayar, jumlahBayar, statusPembayaranPilihanUser, sisaTagihanAwal);
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
        jTable_pembayaran = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jButton_tambah = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField_jumlahBayar = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jButton_simpan = new javax.swing.JButton();
        jButton_batal = new javax.swing.JButton();
        jButton_clear = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jComboBox_status = new javax.swing.JComboBox<>();
        jComboBox_nama = new javax.swing.JComboBox<>();
        jComboBox_noKamar = new javax.swing.JComboBox<>();
        jButton_backMenuUtama = new javax.swing.JButton();
        jMonthChooser_BulanTghn = new com.toedter.calendar.JMonthChooser();
        jYearChooser_TahunTghn = new com.toedter.calendar.JYearChooser();
        jDateChooser_TglBayar = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(245, 247, 250));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 600));

        jTable_pembayaran.setAutoCreateRowSorter(true);
        jTable_pembayaran.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Nama", "No Kamar", "Bulan/Tahun", "Tanggal Bayar", "Jumlah Bayar", "Status", "Sisa Tagihan"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable_pembayaran.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable_pembayaran.setFillsViewportHeight(true);
        jTable_pembayaran.setRowHeight(24);
        jScrollPane1.setViewportView(jTable_pembayaran);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Manajemen Pembayaran Kos");

        jButton_tambah.setBackground(new java.awt.Color(25, 135, 84));
        jButton_tambah.setForeground(new java.awt.Color(255, 255, 255));
        jButton_tambah.setText("Update Pembayaran");
        jButton_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_tambahActionPerformed(evt);
            }
        });

        jLabel2.setText("Nama");

        jLabel8.setText("No Kamar");

        jLabel9.setText("Bulan Tagihan");

        jLabel10.setText("Tahun Tagihan");

        jLabel11.setText("Tanggal Bayar");

        jTextField_jumlahBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_jumlahBayarActionPerformed(evt);
            }
        });

        jLabel12.setText("Jumlah Bayar");

        jButton_simpan.setBackground(new java.awt.Color(25, 135, 84));
        jButton_simpan.setForeground(new java.awt.Color(255, 255, 255));
        jButton_simpan.setText("Simpan");
        jButton_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_simpanActionPerformed(evt);
            }
        });

        jButton_batal.setBackground(new java.awt.Color(108, 117, 125));
        jButton_batal.setForeground(java.awt.Color.white);
        jButton_batal.setText("Batal");
        jButton_batal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_batalActionPerformed(evt);
            }
        });

        jButton_clear.setBackground(new java.awt.Color(108, 117, 125));
        jButton_clear.setForeground(java.awt.Color.white);
        jButton_clear.setText("Clear");
        jButton_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_clearActionPerformed(evt);
            }
        });

        jLabel13.setText("Status");

        jComboBox_status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_status.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_statusActionPerformed(evt);
            }
        });

        jComboBox_nama.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_nama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_namaActionPerformed(evt);
            }
        });

        jComboBox_noKamar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

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
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 659, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton_simpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel13))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox_status, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jTextField_jumlahBayar)
                                        .addComponent(jComboBox_nama, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jComboBox_noKamar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jMonthChooser_BulanTghn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                        .addComponent(jYearChooser_TahunTghn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jDateChooser_TglBayar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addComponent(jButton_tambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(24, 24, 24))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1005, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton_tambah)
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jComboBox_nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jComboBox_noKamar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jMonthChooser_BulanTghn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jYearChooser_TahunTghn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jDateChooser_TglBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jTextField_jumlahBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jComboBox_status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addComponent(jButton_simpan)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_clear)
                            .addComponent(jButton_batal)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton_backMenuUtama)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addGap(8, 8, 8))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1012, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 612, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField_jumlahBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_jumlahBayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_jumlahBayarActionPerformed

    private void jButton_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_simpanActionPerformed
        // TODO add your handling code here:
        Pembayaran pembayaran = getPembayaranFormData(); 
        if (pembayaran != null) {
            pembayaranController.catatPembayaranBaru(pembayaran); 
        }
    }//GEN-LAST:event_jButton_simpanActionPerformed

    private void jButton_backMenuUtamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_backMenuUtamaActionPerformed
        // TODO add your handling code here:
        new DashboardView().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton_backMenuUtamaActionPerformed

    private void jComboBox_namaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_namaActionPerformed
        // TODO add your handling code here:
        int selectedIndex = jComboBox_nama.getSelectedIndex();
        jComboBox_noKamar.removeAllItems(); 
        if (selectedIndex != -1 && daftarPenghuniUntukComboBox != null && selectedIndex < daftarPenghuniUntukComboBox.size()) {
            Penghuni penghuniTerpilih = daftarPenghuniUntukComboBox.get(selectedIndex);
            jComboBox_noKamar.addItem(penghuniTerpilih.getNomorKamarDitempati());
            if(jComboBox_noKamar.getItemCount() > 0) jComboBox_noKamar.setSelectedIndex(0);
        } else {
            jComboBox_noKamar.addItem("-");
        }
    }//GEN-LAST:event_jComboBox_namaActionPerformed

    private void jButton_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_tambahActionPerformed
        // TODO add your handling code here:
        kondisiFormTambah();
    }//GEN-LAST:event_jButton_tambahActionPerformed

    private void jButton_batalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_batalActionPerformed
        // TODO add your handling code here:
        kondisiSetelahOperasiForm(); 
        pembayaranController.loadPenghuniUntukComboBox(); 
    }//GEN-LAST:event_jButton_batalActionPerformed

    private void jButton_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_clearActionPerformed
        // TODO add your handling code here:
        clearFormFields();
    }//GEN-LAST:event_jButton_clearActionPerformed

    private void jComboBox_statusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_statusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox_statusActionPerformed

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
            java.util.logging.Logger.getLogger(PembayaranView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PembayaranView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PembayaranView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PembayaranView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PembayaranView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_backMenuUtama;
    private javax.swing.JButton jButton_batal;
    private javax.swing.JButton jButton_clear;
    private javax.swing.JButton jButton_simpan;
    private javax.swing.JButton jButton_tambah;
    private javax.swing.JComboBox<String> jComboBox_nama;
    private javax.swing.JComboBox<String> jComboBox_noKamar;
    private javax.swing.JComboBox<String> jComboBox_status;
    private com.toedter.calendar.JDateChooser jDateChooser_TglBayar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private com.toedter.calendar.JMonthChooser jMonthChooser_BulanTghn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_pembayaran;
    private javax.swing.JTextField jTextField_jumlahBayar;
    private com.toedter.calendar.JYearChooser jYearChooser_TahunTghn;
    // End of variables declaration//GEN-END:variables
}
