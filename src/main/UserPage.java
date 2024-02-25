package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class UserPage extends javax.swing.JFrame {

    private final DatabaseConnection dc = new DatabaseConnection();
    private PreparedStatement pst, pst1, pst2, pst3, pst4, pst5;
    private ResultSet rs;
//    public static UserMenu userMenu;
    private DefaultTableModel tableModel;
    private final double feePercentage = 0.05;

    public UserPage() {
        initComponents();
        init();
    }
    
    

    private void init() {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Date");
        tableModel.addColumn("Amount");
        tableModel.addColumn("Type");
        tableModel.addColumn("Reference #");
        jTable1.setModel(tableModel);
        
        TableColumn column = jTable1.getColumnModel().getColumn(0);
        column.setPreferredWidth(120);
        
        jTable1.setDefaultEditor(Object.class, null); // disable editing of cells
        loadTransactionHistory();

        menu1.addEventMenuSelected((int index) -> {
            switch (index) {
                case 0 ->
                    showForm(pnl_dashboard);
                case 1 ->
                    showForm(pnl_deposit);
                case 2 ->
                    showForm(pnl_withdraw);
                case 3 ->
                    showForm(pnl_transfer);
                case 4 ->
                    showForm(pnl_bills);
                case 10 ->
                    showForm(pnl_settings);
                case 11 -> {
                    new LogIn().show();
                    this.dispose();
                }
                default -> {
                }
            }
        });
        showForm(pnl_dashboard);
    }

    private void showForm(Component com) {
        body.removeAll();
        body.add(com);
        body.revalidate();
        body.repaint();
    }

    public void setUserDetails(int accountNumber, double balance, String firstName, String lastName, String email,
            String street, String zip, String barangay, String city, String province) {
        lbl_accNum.setText(Integer.toString(accountNumber));
        lbl_balance.setText("PHP " + String.format("%.2f", balance));
        lbl_balance1.setText("PHP " + String.format("%.2f", balance));
        lbl_balance2.setText("PHP " + String.format("%.2f", balance));
        lbl_balance3.setText("PHP " + String.format("%.2f", balance));
        lbl_greet.setText("Hello, " + firstName);
        txt_firstName.setText(firstName);
        txt_lastName.setText(lastName);
        txt_email.setText(email);
        txt_streetAddress.setText(street);
        txt_zip.setText(zip);
        txt_brgy.setText(barangay);
        txt_city.setText(city);
        txt_province.setText(province);
    }

    public void loadTransactionHistory() {
        int account_number = Integer.parseInt(lbl_accNum.getText());
        String sql = "SELECT * FROM transactions WHERE transaction_accNum = ?";

        try {
            Connection conn = dc.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, account_number);
            rs = pst.executeQuery();

            while (rs.next()) {
                String timestamp = rs.getString("transaction_date");
                double amount = rs.getDouble("transaction_amount");
                String type = rs.getString("transaction_type");
                String referenceNum = rs.getString("reference_num");

                String formattedAmount;
                if (type.equals("DEPOSIT") || type.equals("RECEIVED")) {
                    formattedAmount = "+";
                } else {
                    formattedAmount = "-";
                }
                formattedAmount += String.format("%.2f", Math.abs(amount));
                Object[] row = {timestamp, formattedAmount, type, referenceNum};

//                jTable1.getColumnModel().getColumn(1).setCellRenderer(new AmountCellRenderer());
                jTable1.getTableHeader().setReorderingAllowed(false);
                tableModel.addRow(row);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    // generate reference number for transactions
    private String generateRefID() {
        StringBuilder refID = new StringBuilder("OOP");
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            refID.append(random.nextInt(10));
        }
        refID.append("24");

        return refID.toString();
    }

    private void updateBalanceLabel(double newBalance) {
        lbl_balance.setText("PHP " + String.format("%.2f", newBalance));
        lbl_balance1.setText("PHP " + String.format("%.2f", newBalance));
        lbl_balance2.setText("PHP " + String.format("%.2f", newBalance));
        lbl_balance3.setText("PHP " + String.format("%.2f", newBalance));
    }

    public String getCurrentTimestamp() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    // display transaction details
    private void setTransacDetails(String type, Double amount, Double fee, String timestamp, String transactionNumber) {
        lbl_message.setText(type);
        lbl_amount.setText("PHP " + String.format("%.2f", amount));
        lbl_fee.setText("PHP " + String.format("%.2f", fee));
        lbl_dateTime.setText(String.valueOf(timestamp));
        lbl_refNo.setText(transactionNumber);

        showForm(pnl_receipt);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpanel2 = new javax.swing.JPanel();
        menu1 = new swing.menu.Menu();
        body = new javax.swing.JPanel();
        pnl_dashboard = new javax.swing.JPanel();
        lbl_greet = new javax.swing.JLabel();
        panelRound1 = new swing.PanelRound();
        jLabel2 = new javax.swing.JLabel();
        lbl_balance = new javax.swing.JLabel();
        panelRound2 = new swing.PanelRound();
        jLabel4 = new javax.swing.JLabel();
        lbl_accNum = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        pnl_deposit = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txt_depositAmount = new swing.TextField();
        jLabel15 = new javax.swing.JLabel();
        lbl_balance1 = new javax.swing.JLabel();
        btn_deposit = new swing.Button();
        pnl_withdraw = new javax.swing.JPanel();
        btn_withdraw = new swing.Button();
        jLabel16 = new javax.swing.JLabel();
        txt_withdrawAmount = new swing.TextField();
        jLabel17 = new javax.swing.JLabel();
        lbl_balance2 = new javax.swing.JLabel();
        pnl_transfer = new javax.swing.JPanel();
        btn_transfer = new swing.Button();
        jLabel18 = new javax.swing.JLabel();
        txt_destinationAccNum = new swing.TextField();
        jLabel19 = new javax.swing.JLabel();
        lbl_balance3 = new javax.swing.JLabel();
        txt_transferAmount = new swing.TextField();
        txt_destinationName = new swing.TextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        pnl_bills = new javax.swing.JPanel();
        tabbed2 = new swing.Tabbed();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        txt_CAN = new swing.TextField();
        txt_electricAmount = new swing.TextField();
        jLabel24 = new javax.swing.JLabel();
        btn_payElectric = new swing.Button();
        jLabel29 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        txt_waterAccNum = new swing.TextField();
        jLabel26 = new javax.swing.JLabel();
        txt_waterAmount = new swing.TextField();
        btn_payWater = new swing.Button();
        txt_waterName = new swing.TextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        txt_wifiAccNum = new swing.TextField();
        jLabel31 = new javax.swing.JLabel();
        txt_wifiName = new swing.TextField();
        jLabel32 = new javax.swing.JLabel();
        txt_wifiAmount = new swing.TextField();
        jLabel33 = new javax.swing.JLabel();
        btn_payWifi = new swing.Button();
        pnl_settings = new javax.swing.JPanel();
        tabbed1 = new swing.Tabbed();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        txt_firstName = new swing.TextField();
        txt_lastName = new swing.TextField();
        txt_email = new swing.TextField();
        txt_streetAddress = new swing.TextField();
        txt_zip = new swing.TextField();
        txt_brgy = new swing.TextField();
        txt_city = new swing.TextField();
        txt_province = new swing.TextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btn_saveChanges = new swing.Button();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        btn_resetPassword = new swing.Button();
        txt_currPass = new swing.PasswordField();
        txt_newPass = new swing.PasswordField();
        txt_conPass = new swing.PasswordField();
        pnl_receipt = new javax.swing.JPanel();
        panelRound3 = new swing.PanelRound();
        jLabel36 = new javax.swing.JLabel();
        lbl_dateTime = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        lbl_fee = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        lbl_amount = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        lbl_message = new javax.swing.JLabel();
        lbl_refNo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jpanel2.setBackground(new java.awt.Color(255, 255, 255));

        body.setBackground(new java.awt.Color(255, 255, 255));
        body.setLayout(new java.awt.CardLayout());

        pnl_dashboard.setBackground(new java.awt.Color(255, 255, 255));

        lbl_greet.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_greet.setText("Greetings");

        panelRound1.setBackground(new java.awt.Color(198, 181, 242));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Total Balance ");

        lbl_balance.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_balance.setText("PHP 0000000000");

        javax.swing.GroupLayout panelRound1Layout = new javax.swing.GroupLayout(panelRound1);
        panelRound1.setLayout(panelRound1Layout);
        panelRound1Layout.setHorizontalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_balance)
                    .addComponent(jLabel2))
                .addContainerGap(144, Short.MAX_VALUE))
        );
        panelRound1Layout.setVerticalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_balance)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        panelRound2.setBackground(new java.awt.Color(254, 192, 167));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Account Number");

        lbl_accNum.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_accNum.setText("00000000");

        javax.swing.GroupLayout panelRound2Layout = new javax.swing.GroupLayout(panelRound2);
        panelRound2.setLayout(panelRound2Layout);
        panelRound2Layout.setHorizontalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_accNum)
                    .addComponent(jLabel4))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        panelRound2Layout.setVerticalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_accNum)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(100);
        }

        javax.swing.GroupLayout pnl_dashboardLayout = new javax.swing.GroupLayout(pnl_dashboard);
        pnl_dashboard.setLayout(pnl_dashboardLayout);
        pnl_dashboardLayout.setHorizontalGroup(
            pnl_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_dashboardLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(pnl_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbl_greet)
                    .addGroup(pnl_dashboardLayout.createSequentialGroup()
                        .addComponent(panelRound1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelRound2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        pnl_dashboardLayout.setVerticalGroup(
            pnl_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_dashboardLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(lbl_greet)
                .addGap(30, 30, 30)
                .addGroup(pnl_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelRound2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelRound1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        body.add(pnl_dashboard, "card8");

        pnl_deposit.setBackground(new java.awt.Color(255, 255, 255));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel14.setText("Deposit");

        txt_depositAmount.setHint("PHP 0.00");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Balance");

        lbl_balance1.setText("PHP 0.00");

        btn_deposit.setBackground(new java.awt.Color(0, 0, 0));
        btn_deposit.setForeground(new java.awt.Color(255, 255, 255));
        btn_deposit.setText("Deposit");
        btn_deposit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_deposit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_depositActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_depositLayout = new javax.swing.GroupLayout(pnl_deposit);
        pnl_deposit.setLayout(pnl_depositLayout);
        pnl_depositLayout.setHorizontalGroup(
            pnl_depositLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_depositLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(pnl_depositLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addGroup(pnl_depositLayout.createSequentialGroup()
                        .addGroup(pnl_depositLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btn_deposit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_depositAmount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_balance1)))
                .addContainerGap(217, Short.MAX_VALUE))
        );
        pnl_depositLayout.setVerticalGroup(
            pnl_depositLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_depositLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel14)
                .addGap(30, 30, 30)
                .addGroup(pnl_depositLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_depositAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(lbl_balance1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 332, Short.MAX_VALUE)
                .addComponent(btn_deposit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
        );

        body.add(pnl_deposit, "card7");

        pnl_withdraw.setBackground(new java.awt.Color(255, 255, 255));

        btn_withdraw.setBackground(new java.awt.Color(0, 0, 0));
        btn_withdraw.setForeground(new java.awt.Color(255, 255, 255));
        btn_withdraw.setText("Withdraw");
        btn_withdraw.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_withdraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_withdrawActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel16.setText("Withdraw");

        txt_withdrawAmount.setHint("PHP 0.00");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setText("Balance");

        lbl_balance2.setText("PHP 0.00");

        javax.swing.GroupLayout pnl_withdrawLayout = new javax.swing.GroupLayout(pnl_withdraw);
        pnl_withdraw.setLayout(pnl_withdrawLayout);
        pnl_withdrawLayout.setHorizontalGroup(
            pnl_withdrawLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_withdrawLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(pnl_withdrawLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addGroup(pnl_withdrawLayout.createSequentialGroup()
                        .addGroup(pnl_withdrawLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btn_withdraw, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_withdrawAmount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_balance2)))
                .addContainerGap(217, Short.MAX_VALUE))
        );
        pnl_withdrawLayout.setVerticalGroup(
            pnl_withdrawLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_withdrawLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel16)
                .addGap(30, 30, 30)
                .addGroup(pnl_withdrawLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_withdrawAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(lbl_balance2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 332, Short.MAX_VALUE)
                .addComponent(btn_withdraw, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
        );

        body.add(pnl_withdraw, "card6");

        pnl_transfer.setBackground(new java.awt.Color(255, 255, 255));

        btn_transfer.setBackground(new java.awt.Color(0, 0, 0));
        btn_transfer.setForeground(new java.awt.Color(255, 255, 255));
        btn_transfer.setText("Transfer");
        btn_transfer.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_transfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_transferActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel18.setText("Transfer");

        txt_destinationAccNum.setHint("Account number");
        txt_destinationAccNum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_destinationAccNumKeyReleased(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setText("Balance");

        lbl_balance3.setText("PHP 0.00");

        txt_transferAmount.setHint("PHP 0.00");

        txt_destinationName.setEditable(false);
        txt_destinationName.setCaretColor(new java.awt.Color(255, 255, 255));
        txt_destinationName.setHint("Name");

        jLabel20.setText("Account Number");

        jLabel21.setText("Transfer to");

        jLabel22.setText("Amount");

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel34.setText("A PHP 15.00 fee will be charged per transaction.");

        javax.swing.GroupLayout pnl_transferLayout = new javax.swing.GroupLayout(pnl_transfer);
        pnl_transfer.setLayout(pnl_transferLayout);
        pnl_transferLayout.setHorizontalGroup(
            pnl_transferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_transferLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(pnl_transferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_transferLayout.createSequentialGroup()
                        .addGroup(pnl_transferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addGroup(pnl_transferLayout.createSequentialGroup()
                                .addGroup(pnl_transferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnl_transferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(btn_transfer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txt_destinationAccNum, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                                        .addComponent(txt_transferAmount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE))
                                    .addComponent(jLabel20))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnl_transferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_destinationName, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                                    .addGroup(pnl_transferLayout.createSequentialGroup()
                                        .addGroup(pnl_transferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel21)
                                            .addGroup(pnl_transferLayout.createSequentialGroup()
                                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lbl_balance3)))
                                        .addGap(0, 160, Short.MAX_VALUE)))))
                        .addGap(57, 57, 57))
                    .addGroup(pnl_transferLayout.createSequentialGroup()
                        .addGroup(pnl_transferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(jLabel34))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        pnl_transferLayout.setVerticalGroup(
            pnl_transferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_transferLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel18)
                .addGap(30, 30, 30)
                .addGroup(pnl_transferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_transferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_destinationAccNum, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_destinationName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel22)
                .addGap(4, 4, 4)
                .addGroup(pnl_transferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_transferAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(lbl_balance3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel34)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 212, Short.MAX_VALUE)
                .addComponent(btn_transfer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
        );

        body.add(pnl_transfer, "card5");

        pnl_bills.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane3.setBorder(null);
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel23.setText("Customer Account Number (CAN)");

        txt_CAN.setHint("Enter 10-digit Account Number");

        txt_electricAmount.setHint("PHP 0.00");

        jLabel24.setText("Amount");

        btn_payElectric.setBackground(new java.awt.Color(0, 0, 0));
        btn_payElectric.setForeground(new java.awt.Color(255, 255, 255));
        btn_payElectric.setText("Confirm");
        btn_payElectric.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_payElectric.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_payElectricActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel29.setText("Biller Convenience Fee (BCF) may apply");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel24)
                        .addComponent(txt_electricAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                        .addComponent(txt_CAN, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                        .addComponent(jLabel23)
                        .addComponent(btn_payElectric, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(466, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_CAN, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_electricAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 212, Short.MAX_VALUE)
                .addComponent(btn_payElectric, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
        );

        jScrollPane3.setViewportView(jPanel2);

        tabbed2.addTab("Electric Utility", jScrollPane3);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel25.setText("12-14 Digit Account Number");

        txt_waterAccNum.setHint("Enter 12-14 Digit Account Number");

        jLabel26.setText("Amount");

        txt_waterAmount.setHint("PHP 0.00");

        btn_payWater.setBackground(new java.awt.Color(0, 0, 0));
        btn_payWater.setForeground(new java.awt.Color(255, 255, 255));
        btn_payWater.setText("Confirm");
        btn_payWater.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_payWater.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_payWaterActionPerformed(evt);
            }
        });

        txt_waterName.setHint("Enter Account Name");

        jLabel27.setText("Account Name");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel28.setText("Biller Convenience Fee (BCF) may apply");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(txt_waterName, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel26)
                        .addComponent(txt_waterAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                        .addComponent(txt_waterAccNum, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                        .addComponent(jLabel25)
                        .addComponent(btn_payWater, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(323, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_waterAccNum, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_waterName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_waterAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel28)
                .addGap(132, 132, 132)
                .addComponent(btn_payWater, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );

        tabbed2.addTab("Water Utility", jPanel4);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel30.setText("Account Number (13-Digit)");

        txt_wifiAccNum.setHint("Enter Account Number (13-Digit)");

        jLabel31.setText("Account Name");

        txt_wifiName.setHint("Enter Account Name");

        jLabel32.setText("Amount");

        txt_wifiAmount.setHint("PHP 0.00");

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel33.setText("Biller Convenience Fee (BCF) may apply");

        btn_payWifi.setBackground(new java.awt.Color(0, 0, 0));
        btn_payWifi.setForeground(new java.awt.Color(255, 255, 255));
        btn_payWifi.setText("Confirm");
        btn_payWifi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_payWifi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_payWifiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel31)
                    .addComponent(txt_wifiName, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel32)
                        .addComponent(txt_wifiAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                        .addComponent(txt_wifiAccNum, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                        .addComponent(jLabel30)
                        .addComponent(btn_payWifi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(323, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_wifiAccNum, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_wifiName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel32)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_wifiAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel33)
                .addGap(132, 132, 132)
                .addComponent(btn_payWifi, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );

        tabbed2.addTab("Cable and Internet", jPanel6);

        javax.swing.GroupLayout pnl_billsLayout = new javax.swing.GroupLayout(pnl_bills);
        pnl_bills.setLayout(pnl_billsLayout);
        pnl_billsLayout.setHorizontalGroup(
            pnl_billsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbed2, javax.swing.GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
        );
        pnl_billsLayout.setVerticalGroup(
            pnl_billsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_billsLayout.createSequentialGroup()
                .addGap(0, 16, Short.MAX_VALUE)
                .addComponent(tabbed2, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        body.add(pnl_bills, "card2");

        pnl_settings.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        txt_firstName.setHint("First name");

        txt_lastName.setHint("Last name");

        txt_email.setHint("Email");

        txt_streetAddress.setHint("Street address");

        txt_zip.setHint("ZIP code");

        txt_brgy.setHint("Barangay");

        txt_city.setHint("CIty/Municipality");

        txt_province.setHint("Province");

        jLabel1.setText("First name");

        jLabel3.setText("Last name");

        jLabel5.setText("Email");

        jLabel6.setText("Street address");

        jLabel7.setText("ZIP code");

        jLabel8.setText("Barangay");

        jLabel9.setText("City/Municipality");

        jLabel10.setText("Province");

        btn_saveChanges.setBackground(new java.awt.Color(0, 0, 0));
        btn_saveChanges.setForeground(new java.awt.Color(255, 255, 255));
        btn_saveChanges.setText("Save Changes");
        btn_saveChanges.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_saveChanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveChangesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(txt_email, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_streetAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_firstName, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(txt_lastName, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_zip, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(txt_brgy, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_city, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(txt_province, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btn_saveChanges, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(203, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_firstName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_lastName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_streetAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_zip, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_brgy, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_city, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_province, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addComponent(btn_saveChanges, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );

        jScrollPane2.setViewportView(jPanel1);

        tabbed1.addTab("Personal Info", jScrollPane2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setText("Current Password");

        jLabel12.setText("New Password");

        jLabel13.setText("Confirm Password");

        btn_resetPassword.setBackground(new java.awt.Color(0, 0, 0));
        btn_resetPassword.setForeground(new java.awt.Color(255, 255, 255));
        btn_resetPassword.setText("Confirm");
        btn_resetPassword.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_resetPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_resetPasswordActionPerformed(evt);
            }
        });

        txt_currPass.setHint("Current password");

        txt_newPass.setHint("New password");

        txt_conPass.setHint("Confirm password");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(btn_resetPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(326, 326, 326))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel12)
                            .addComponent(jLabel11))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_conPass, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                            .addComponent(txt_newPass, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_currPass, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(316, 316, 316))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_currPass, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_newPass, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_conPass, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(152, 152, 152)
                .addComponent(btn_resetPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );

        tabbed1.addTab("Password and Security", jPanel3);

        javax.swing.GroupLayout pnl_settingsLayout = new javax.swing.GroupLayout(pnl_settings);
        pnl_settings.setLayout(pnl_settingsLayout);
        pnl_settingsLayout.setHorizontalGroup(
            pnl_settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbed1, javax.swing.GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
        );
        pnl_settingsLayout.setVerticalGroup(
            pnl_settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_settingsLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(tabbed1, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        body.add(pnl_settings, "card3");

        pnl_receipt.setBackground(new java.awt.Color(255, 255, 255));

        panelRound3.setBackground(new java.awt.Color(248, 249, 250));

        jLabel36.setText("Ref No. ");

        lbl_dateTime.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbl_dateTime.setText("2024-02-16 10:05:12");

        jLabel38.setText("Convenience Fee");

        lbl_fee.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbl_fee.setText("PHP 0000");

        jLabel40.setText("Amount");

        lbl_amount.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbl_amount.setText("PHP 000000");

        jLabel42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/check.png"))); // NOI18N

        lbl_message.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbl_message.setText("Successfully sent to NAME NAME NAME");

        lbl_refNo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbl_refNo.setText("OOP580724");

        javax.swing.GroupLayout panelRound3Layout = new javax.swing.GroupLayout(panelRound3);
        panelRound3.setLayout(panelRound3Layout);
        panelRound3Layout.setHorizontalGroup(
            panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound3Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRound3Layout.createSequentialGroup()
                        .addComponent(jLabel36)
                        .addGap(2, 2, 2)
                        .addComponent(lbl_refNo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_dateTime))
                    .addGroup(panelRound3Layout.createSequentialGroup()
                        .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel38)
                            .addComponent(jLabel40))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_fee, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbl_amount, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(70, 70, 70))
            .addGroup(panelRound3Layout.createSequentialGroup()
                .addGap(116, 116, 116)
                .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound3Layout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addGap(116, 116, 116))
                    .addGroup(panelRound3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lbl_message)
                        .addContainerGap(103, Short.MAX_VALUE))))
        );
        panelRound3Layout.setVerticalGroup(
            panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound3Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel42)
                .addGap(18, 18, 18)
                .addComponent(lbl_message)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(lbl_amount))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(lbl_fee))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(lbl_dateTime)
                    .addComponent(lbl_refNo))
                .addGap(55, 55, 55))
        );

        javax.swing.GroupLayout pnl_receiptLayout = new javax.swing.GroupLayout(pnl_receipt);
        pnl_receipt.setLayout(pnl_receiptLayout);
        pnl_receiptLayout.setHorizontalGroup(
            pnl_receiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_receiptLayout.createSequentialGroup()
                .addContainerGap(87, Short.MAX_VALUE)
                .addComponent(panelRound3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(86, Short.MAX_VALUE))
        );
        pnl_receiptLayout.setVerticalGroup(
            pnl_receiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_receiptLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(panelRound3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        body.add(pnl_receipt, "card4");

        javax.swing.GroupLayout jpanel2Layout = new javax.swing.GroupLayout(jpanel2);
        jpanel2.setLayout(jpanel2Layout);
        jpanel2Layout.setHorizontalGroup(
            jpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanel2Layout.createSequentialGroup()
                .addComponent(menu1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpanel2Layout.setVerticalGroup(
            jpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(menu1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jpanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(body, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_depositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_depositActionPerformed
        String timestamp = getCurrentTimestamp();
        String accountNumber = lbl_accNum.getText();
        double amount = Double.parseDouble(txt_depositAmount.getText());
        String strAmount = String.format("%.2f", amount);
        double depositAmount = Double.parseDouble(strAmount);
        String referenceID = generateRefID();

        String sql = "INSERT INTO transactions (transaction_date, transaction_accNum, transaction_amount, transaction_type, reference_num, transaction_fee) VALUES (?,?,?,?,?,?)";
        try {
            if (depositAmount < 100 || depositAmount > 100000) {
                JOptionPane.showMessageDialog(null, "Deposit amount must be between PHP 100 and 100,000.");
                txt_depositAmount.setText("");
                return;
            }

            Connection conn = dc.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, timestamp);
            pst.setString(2, accountNumber);
            pst.setDouble(3, depositAmount);
            pst.setString(4, "DEPOSIT");
            pst.setString(5, referenceID);
            pst.setDouble(6, 0);
            pst.execute();

            try {
                String sql1 = "SELECT * FROM sign_up WHERE account_number=?";
                pst1 = conn.prepareStatement(sql1);
                pst1.setString(1, accountNumber);
                rs = pst1.executeQuery();

                if (rs.next()) {
                    double currentBalance = rs.getDouble("account_balance");
                    double sum = depositAmount + currentBalance;
                    String num = String.format("%.2f", sum);
                    double newBalance = Double.parseDouble(num);
                    updateBalanceLabel(newBalance);

                    try {
                        String sql2 = "UPDATE sign_up SET account_balance=? WHERE account_number=?";
                        pst2 = conn.prepareStatement(sql2);
                        pst2.setDouble(1, newBalance);
                        pst2.setString(2, accountNumber);
                        pst2.execute();
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, e);
                    }

                    loadTransactionHistory();
                    JOptionPane.showMessageDialog(null, "Deposit Successful.");
                    txt_depositAmount.setText("");
                    setTransacDetails("Deposit Successful", depositAmount, 0.0, timestamp, referenceID);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_depositActionPerformed

    private void btn_withdrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_withdrawActionPerformed
        String timestamp = getCurrentTimestamp();
        String accountNumber = lbl_accNum.getText();
        double amount = Double.parseDouble(txt_withdrawAmount.getText());
        String strAmount = String.format("%.2f", amount);
        double withdrawAmount = Double.parseDouble(strAmount);
        String referenceID = generateRefID();

        try {
            if (amount < 100 || amount > 100000) {
                JOptionPane.showMessageDialog(null, "Withdraw amount must be between PHP 100 and 100,000.");
                txt_withdrawAmount.setText("");
                return;
            }

            String sql = "SELECT * FROM sign_up WHERE account_number=?";
            Connection conn = dc.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, accountNumber);
            rs = pst.executeQuery();

            if (rs.next()) {
                JPasswordField pf = new JPasswordField();
                int m = JOptionPane.showConfirmDialog(null, pf, "Please enter password to proceed", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (m == JOptionPane.OK_OPTION) {
                    String password = String.valueOf(pf.getPassword());

                    if (password == null ? rs.getString("password") != null : !password.equals(rs.getString("password"))) {
                        JOptionPane.showMessageDialog(null, "Invalid password.");
                    } else {
                        double currentBalance = rs.getDouble("account_balance");
                        double diff = currentBalance - withdrawAmount;

                        if (diff < 0) {
                            JOptionPane.showMessageDialog(null, "Insufficient Balance.");
                            txt_withdrawAmount.setText("");
                        } else {
                            String num = String.format("%.2f", diff);
                            double newBalance = Double.parseDouble(num);
                            updateBalanceLabel(newBalance);

                            try {
                                String sql1 = "UPDATE sign_up SET account_balance=? WHERE account_number=?";
                                pst1 = conn.prepareStatement(sql1);
                                pst1.setDouble(1, newBalance);
                                pst1.setString(2, accountNumber);
                                pst1.execute();

                                try {
                                    String sql2 = "INSERT INTO transactions (transaction_date, transaction_accNum, transaction_amount, transaction_type, reference_num, transaction_fee) VALUES (?,?,?,?,?,?)";
                                    pst2 = conn.prepareStatement(sql2);
                                    pst2.setString(1, timestamp);
                                    pst2.setString(2, accountNumber);
                                    pst2.setDouble(3, withdrawAmount);
                                    pst2.setString(4, "WITHDRAW");
                                    pst2.setString(5, referenceID);
                                    pst2.setDouble(6, 0);
                                    pst2.execute();

                                    loadTransactionHistory();
                                    JOptionPane.showMessageDialog(null, "Withdraw Successful.");
                                    txt_withdrawAmount.setText("");
                                    setTransacDetails("Withdraw Successful", withdrawAmount, 0.0, timestamp, referenceID);
                                } catch (SQLException e) {
                                    JOptionPane.showMessageDialog(null, e);
                                }
                            } catch (SQLException e) {
                                JOptionPane.showMessageDialog(null, e);
                            }
                        }
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_withdrawActionPerformed

    private void btn_transferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_transferActionPerformed
        try {
            String timestamp = getCurrentTimestamp();
            String sourceAccount = lbl_accNum.getText();
            String destinationAccount = txt_destinationAccNum.getText();
            String destinationName = txt_destinationName.getText();
            double amount = Double.parseDouble(txt_transferAmount.getText());
            String strAmount = String.format("%.2f", amount);
            double transferAmount = Double.parseDouble(strAmount);
            String referenceID = generateRefID();

            if (sourceAccount.equals(destinationAccount)) {
                JOptionPane.showMessageDialog(null, "Cannot transfer to your own account.");
                txt_destinationAccNum.setText("");
                txt_transferAmount.setText("");
                txt_destinationName.setText("");
                return;
            }

            Connection conn = dc.getConnection();
            String checkDestinationAccountSql = "SELECT * FROM sign_up WHERE account_number = ?";
            pst5 = conn.prepareStatement(checkDestinationAccountSql);
            pst5.setString(1, destinationAccount);
            ResultSet rsDestAccount = pst5.executeQuery();

            if (!rsDestAccount.next()) {
                JOptionPane.showMessageDialog(null, "Invalid destination account number.");
                txt_destinationAccNum.setText("");
                txt_transferAmount.setText("");
                txt_destinationName.setText("");
                return;
            }

            String sql = "SELECT account_balance FROM sign_up WHERE account_number=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, sourceAccount);
            rs = pst.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("account_balance");
                double convenienceFee = 15;
                double totalAmount = transferAmount + convenienceFee;

                if (transferAmount >= 100.0 && totalAmount <= currentBalance) {
                    double newBalance = currentBalance - totalAmount;

                    String sql1 = "UPDATE sign_up SET account_balance=account_balance-? WHERE account_number=?";
                    pst1 = conn.prepareStatement(sql1);
                    pst1.setDouble(1, totalAmount);
                    pst1.setString(2, sourceAccount);
                    pst1.executeUpdate();

                    String sql2 = "UPDATE sign_up SET account_balance=account_balance+? WHERE account_number=?";
                    pst2 = conn.prepareStatement(sql2);
                    pst2.setDouble(1, transferAmount);
                    pst2.setString(2, destinationAccount);
                    pst2.executeUpdate();

                    String sql3 = "INSERT INTO transactions (transaction_date, transaction_accNum, transaction_amount, transaction_type, reference_num, transaction_fee) VALUES (?,?,?,?,?,?)";
                    pst3 = conn.prepareStatement(sql3);
                    pst3.setString(1, timestamp);
                    pst3.setString(2, sourceAccount);
                    pst3.setDouble(3, transferAmount);
                    pst3.setString(4, "TRANSFER");
                    pst3.setString(5, referenceID);
                    pst3.setDouble(6, convenienceFee);
                    pst3.executeUpdate();
                    loadTransactionHistory();

                    String sql4 = "INSERT INTO transactions (transaction_date, transaction_accNum, transaction_amount, transaction_type, reference_num, transaction_fee) VALUES (?,?,?,?,?,?)";
                    pst4 = conn.prepareStatement(sql4);
                    pst4.setString(1, timestamp);
                    pst4.setString(2, destinationAccount);
                    pst4.setDouble(3, transferAmount);
                    pst4.setString(4, "RECEIVED");
                    pst4.setString(5, referenceID);
                    pst4.setDouble(6, 0);
                    pst4.executeUpdate();

                    updateBalanceLabel(newBalance);
                    JOptionPane.showMessageDialog(null, "Transaction Success.");
                    txt_destinationAccNum.setText("");
                    txt_transferAmount.setText("");
                    txt_destinationName.setText("");
                    setTransacDetails("Successfully sent to " + destinationName,
                            transferAmount, convenienceFee, timestamp, referenceID);
                } else {
                    JOptionPane.showMessageDialog(null, "Transfer amount must be between PHP 100 and PHP " + (currentBalance - convenienceFee) + " (including convenience fee).");
                    txt_transferAmount.setText("");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error retrieving balance information.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_transferActionPerformed

    private void btn_payElectricActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_payElectricActionPerformed
        String timestamp = getCurrentTimestamp();

        String accountNumberStr = txt_CAN.getText();
        if (accountNumberStr.length() != 10 || !accountNumberStr.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Invalid Account Number. Please enter a 10-digit numeric account number.");
            txt_CAN.setText("");
            return;
        }

        String accountNumber = lbl_accNum.getText();
        double amount = Double.parseDouble(txt_electricAmount.getText());
        String strAmount = String.format("%.2f", amount);
        double amountPay = Double.parseDouble(strAmount);
        String referenceID = generateRefID();

        try {
            String sql = "SELECT * FROM sign_up WHERE account_number=?";
            Connection conn = dc.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, accountNumber);
            rs = pst.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("account_balance");
                double convenienceFee = feePercentage * amountPay;
                double totalAmount = amountPay + convenienceFee;
                double diff = currentBalance - totalAmount;

                if (amountPay >= 10.0 && totalAmount <= currentBalance) {
                    String num = String.format("%.2f", diff);
                    double newBalance = Double.parseDouble(num);
                    updateBalanceLabel(newBalance);

                    try {
                        String sql1 = "UPDATE sign_up SET account_balance=? WHERE account_number=?";
                        pst1 = conn.prepareStatement(sql1);
                        pst1.setDouble(1, newBalance);
                        pst1.setString(2, accountNumber);
                        pst1.execute();

                        try {
                            String sql2 = "INSERT INTO transactions (transaction_date, transaction_accNum, transaction_amount, transaction_type, reference_num, transaction_fee) VALUES (?,?,?,?,?,?)";
                            pst2 = conn.prepareStatement(sql2);
                            pst2.setString(1, timestamp);
                            pst2.setString(2, accountNumber);
                            pst2.setDouble(3, totalAmount);
                            pst2.setString(4, "PAY BILLS");
                            pst2.setString(5, referenceID);
                            pst2.setDouble(6, convenienceFee);
                            pst2.execute();

                            JOptionPane.showMessageDialog(null, "Payment Successful.");
                            loadTransactionHistory();
                            txt_electricAmount.setText("");
                            txt_CAN.setText("");
                            setTransacDetails("Payment Successful", amountPay, convenienceFee, timestamp, referenceID);
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, e);
                        }
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Payment Failed.");
                    txt_electricAmount.setText("");
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_payElectricActionPerformed

    private void btn_payWaterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_payWaterActionPerformed
        String timestamp = getCurrentTimestamp();

        String accountNumberStr = txt_waterAccNum.getText();
        if (accountNumberStr.length() < 12 || accountNumberStr.length() > 14 || !accountNumberStr.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Invalid Account Number. Please enter a numeric account number with a length between 12 and 14 digits.");
            txt_waterAccNum.setText("");
            return;
        }

        String name = txt_waterName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Name cannot be empty. Please enter a valid name.");
            return;
        }

        int accountNumber = Integer.parseInt(lbl_accNum.getText());
        double amount = Double.parseDouble(txt_waterAmount.getText());
        String strAmount = String.format("%.2f", amount);
        double amountPay = Double.parseDouble(strAmount);
        String referenceID = generateRefID();

        try {
            String sql = "SELECT * FROM sign_up WHERE account_number=?";
            Connection conn = dc.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, accountNumber);
            rs = pst.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("account_balance");
                double convenienceFee = feePercentage * amountPay;
                double totalAmount = amountPay + convenienceFee;
                double diff = currentBalance - totalAmount;

                if (amountPay >= 10.0 && totalAmount <= currentBalance) {
                    String num = String.format("%.2f", diff);
                    double newBalance = Double.parseDouble(num);
                    updateBalanceLabel(newBalance);

                    try {
                        String sql1 = "UPDATE sign_up SET account_balance=? WHERE account_number=?";
                        pst1 = conn.prepareStatement(sql1);
                        pst1.setDouble(1, newBalance);
                        pst1.setInt(2, accountNumber);
                        pst1.execute();

                        try {
                            String sql2 = "INSERT INTO transactions (transaction_date, transaction_accNum, transaction_amount, transaction_type, reference_num, transaction_fee) VALUES (?,?,?,?,?,?)";
                            pst2 = conn.prepareStatement(sql2);
                            pst2.setString(1, timestamp);
                            pst2.setInt(2, accountNumber);
                            pst2.setDouble(3, totalAmount);
                            pst2.setString(4, "PAY BILLS");
                            pst2.setString(5, referenceID);
                            pst2.setDouble(6, convenienceFee);
                            pst2.execute();

                            JOptionPane.showMessageDialog(null, "Payment Successful.");
                            loadTransactionHistory();
                            txt_waterAmount.setText("");
                            txt_waterAccNum.setText("");
                            txt_waterName.setText("");

                            setTransacDetails("Payment Successful", amountPay, convenienceFee, timestamp, referenceID);
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, e);
                        }
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Payment Failed.");
                    txt_waterAmount.setText("");
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_payWaterActionPerformed

    private void btn_payWifiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_payWifiActionPerformed
        String timestamp = getCurrentTimestamp();

        String accountNumberStr = txt_wifiAccNum.getText();
        if (accountNumberStr.length() != 13 || !accountNumberStr.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Invalid Account Number. Please enter a 13-digit numeric account number.");
            txt_wifiAccNum.setText("");
            return;
        }

        String name = txt_wifiName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Name cannot be empty. Please enter a valid name.");
            return;
        }

        String accountNumber = lbl_accNum.getText();
        double amount = Double.parseDouble(txt_wifiAmount.getText());
        String strAmount = String.format("%.2f", amount);
        double amountPay = Double.parseDouble(strAmount);
        String referenceID = generateRefID();

        try {
            String sql = "SELECT * FROM sign_up WHERE account_number=?";
            Connection conn = dc.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, accountNumber);
            rs = pst.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("account_balance");
                double convenienceFee = feePercentage * amountPay;
                double totalAmount = amountPay + convenienceFee;
                double diff = currentBalance - totalAmount;

                if (amountPay >= 10.0 && totalAmount <= currentBalance) {
                    String num = String.format("%.2f", diff);
                    double newBalance = Double.parseDouble(num);
                    updateBalanceLabel(newBalance);

                    try {
                        String sql1 = "UPDATE sign_up SET account_balance=? WHERE account_number=?";
                        pst1 = conn.prepareStatement(sql1);
                        pst1.setDouble(1, newBalance);
                        pst1.setString(2, accountNumber);
                        pst1.execute();

                        try {
                            String sql2 = "INSERT INTO transactions (transaction_date, transaction_accNum, transaction_amount, transaction_type, reference_num, transaction_fee) VALUES (?,?,?,?,?,?)";
                            pst2 = conn.prepareStatement(sql2);
                            pst2.setString(1, timestamp);
                            pst2.setString(2, accountNumber);
                            pst2.setDouble(3, totalAmount);
                            pst2.setString(4, "PAY BILLS");
                            pst2.setString(5, referenceID);
                            pst2.setDouble(6, convenienceFee);
                            pst2.execute();

                            JOptionPane.showMessageDialog(null, "Payment Successful.");
                            loadTransactionHistory();
                            txt_wifiAmount.setText("");
                            txt_wifiAccNum.setText("");
                            txt_wifiName.setText("");
                            setTransacDetails("Payment Successful", amountPay, convenienceFee, timestamp, referenceID);
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, e);
                        }
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Payment Failed.");
                    txt_wifiAmount.setText("");
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_payWifiActionPerformed

    private void btn_saveChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveChangesActionPerformed
        try {
            String accNum = lbl_accNum.getText();
            String firstName = txt_firstName.getText();
            String lastName = txt_lastName.getText();
            String email = txt_email.getText();
            String streetAdd = txt_streetAddress.getText();
            String zip = txt_zip.getText();
            String brgy = txt_brgy.getText();
            String city = txt_city.getText();
            String prov = txt_province.getText();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || streetAdd.isEmpty()
                    || zip.isEmpty() || brgy.isEmpty() || city.isEmpty() || prov.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all the fields.");
                return;
            }

            String sql = "UPDATE sign_up SET first_name=?, last_name=?, email=?, street_address=?, zip_code=?, "
                    + "barangay=?, city=?, province=? WHERE account_number=?";

            Connection conn = dc.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, email);
            pst.setString(4, streetAdd);
            pst.setString(5, zip);
            pst.setString(6, brgy);
            pst.setString(7, city);
            pst.setString(8, prov);
            pst.setString(9, accNum);
            pst.execute();

            JOptionPane.showMessageDialog(null, "Successfully changed details.");
            lbl_greet.setText("Hello, " + firstName);

        } catch (SQLException | ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_saveChangesActionPerformed

    private void btn_resetPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_resetPasswordActionPerformed
        try {
            String currentPassword = new String(txt_currPass.getPassword());
            String newPassword = new String(txt_newPass.getPassword());
            String confirmPassword = new String(txt_conPass.getPassword());
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords don't match.");
                txt_conPass.setText("");
                return;
            }

            String sql = "UPDATE sign_up SET password=? WHERE account_number=? AND password=?";
            Connection conn = dc.getConnection();
            pst = conn.prepareStatement(sql);
            String accountNumber = lbl_accNum.getText();
            pst.setString(1, newPassword);
            pst.setString(2, accountNumber);
            pst.setString(3, currentPassword);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Password changed successfully.");
                txt_currPass.setText("");
                txt_newPass.setText("");
                txt_conPass.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Wrong password. Please try again.");
                txt_currPass.setText("");
            }
        } catch (HeadlessException | SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_btn_resetPasswordActionPerformed

    private void txt_destinationAccNumKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_destinationAccNumKeyReleased
        String accountNumber = txt_destinationAccNum.getText();

        if (!accountNumber.isEmpty()) {
            try {
                String sql = "SELECT first_name, last_name FROM sign_up WHERE account_number=?";
                try (Connection conn = dc.getConnection()) {
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, accountNumber);
                    rs = pst.executeQuery();

                    if (rs.next()) {
                        String firstName = rs.getString("first_name");
                        String lastName = rs.getString("last_name");
                        String fullName = firstName + " " + lastName;
                        txt_destinationName.setText(fullName);
                    } else {
                        txt_destinationName.setText("Not Found");
                    }
                }
            } catch (SQLException | ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(UserPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_txt_destinationAccNumKeyReleased

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UserPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel body;
    private swing.Button btn_deposit;
    private swing.Button btn_payElectric;
    private swing.Button btn_payWater;
    private swing.Button btn_payWifi;
    private swing.Button btn_resetPassword;
    private swing.Button btn_saveChanges;
    private swing.Button btn_transfer;
    private swing.Button btn_withdraw;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel jpanel2;
    private javax.swing.JLabel lbl_accNum;
    private javax.swing.JLabel lbl_amount;
    private javax.swing.JLabel lbl_balance;
    private javax.swing.JLabel lbl_balance1;
    private javax.swing.JLabel lbl_balance2;
    private javax.swing.JLabel lbl_balance3;
    private javax.swing.JLabel lbl_dateTime;
    private javax.swing.JLabel lbl_fee;
    private javax.swing.JLabel lbl_greet;
    private javax.swing.JLabel lbl_message;
    private javax.swing.JLabel lbl_refNo;
    private swing.menu.Menu menu1;
    private swing.PanelRound panelRound1;
    private swing.PanelRound panelRound2;
    private swing.PanelRound panelRound3;
    private javax.swing.JPanel pnl_bills;
    private javax.swing.JPanel pnl_dashboard;
    private javax.swing.JPanel pnl_deposit;
    private javax.swing.JPanel pnl_receipt;
    private javax.swing.JPanel pnl_settings;
    private javax.swing.JPanel pnl_transfer;
    private javax.swing.JPanel pnl_withdraw;
    private swing.Tabbed tabbed1;
    private swing.Tabbed tabbed2;
    private swing.TextField txt_CAN;
    private swing.TextField txt_brgy;
    private swing.TextField txt_city;
    private swing.PasswordField txt_conPass;
    private swing.PasswordField txt_currPass;
    private swing.TextField txt_depositAmount;
    private swing.TextField txt_destinationAccNum;
    private swing.TextField txt_destinationName;
    private swing.TextField txt_electricAmount;
    private swing.TextField txt_email;
    private swing.TextField txt_firstName;
    private swing.TextField txt_lastName;
    private swing.PasswordField txt_newPass;
    private swing.TextField txt_province;
    private swing.TextField txt_streetAddress;
    private swing.TextField txt_transferAmount;
    private swing.TextField txt_waterAccNum;
    private swing.TextField txt_waterAmount;
    private swing.TextField txt_waterName;
    private swing.TextField txt_wifiAccNum;
    private swing.TextField txt_wifiAmount;
    private swing.TextField txt_wifiName;
    private swing.TextField txt_withdrawAmount;
    private swing.TextField txt_zip;
    // End of variables declaration//GEN-END:variables
}
