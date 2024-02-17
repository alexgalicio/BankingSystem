package main;
// TODO: UPDATE TRANSACTION LOGS
// 

import java.awt.Component;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import swing.TextAreaCellRenderer;

public class AdminPage extends javax.swing.JFrame {

    private DatabaseConnection dc = new DatabaseConnection();
    private PreparedStatement pst, pst1, pst2, pst3, pst4, pst5;
    private ResultSet rs;

    public AdminPage() {
        initComponents();
        init();
    }

    private void init() {
        lbl_greet.setText("Hello, Admin");
        String totalBalance = String.format("%.2f", getTotalBalance());
        lbl_totalBalance.setText("PHP " + totalBalance);
        int totalAccount = getTotalAccounts();
        lbl_totalAccounts.setText(Integer.toString(totalAccount));
        String totalProfit = String.format("%.2f", getTotalProfit());
        lbl_totalProfit.setText("PHP " + totalProfit);

        fetchData();
        transactionTable();
        menuAdmin1.addEventMenuSelected((int index) -> {
            switch (index) {
                case 0 ->
                    showForm(pnl_dashboard);
                case 1 -> {
                    showForm(pnl_manageAccounts);
                }
                case 2 -> {
                    showForm(pnl_newAccount);
                }
                case 3 -> {
                    showForm(pnl_transactions);
                }
                case 4 -> {
                    showForm(pnl_transactionLogs);
                }
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

    public void fetchData() {
        String sql = "SELECT * FROM sign_up";

        try {
            Connection conn = dc.getConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) tbl_data.getModel();
            model.setRowCount(0);
            tbl_data.getTableHeader().setReorderingAllowed(false);

            setCustomRendererForColumn("Name");
            setCustomRendererForColumn("Email");
            setCustomRendererForColumn("Address");

            while (rs.next()) {
                Vector v2 = new Vector<>();
                v2.add(rs.getString("account_number"));
                v2.add(rs.getString("username"));
                v2.add(rs.getString("first_name") + " " + rs.getString("last_name"));
                v2.add(rs.getString("account_balance"));
                v2.add(rs.getString("email"));
                v2.add(rs.getString("street_address") + " " + rs.getString("barangay") + ", "
                        + rs.getString("city") + ", " + rs.getString("province"));

                // Add the row to the data Vector
                model.addRow(v2);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            java.util.logging.Logger.getLogger(AdminPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    private void transactionTable() {
        String sql = "SELECT * FROM transactions";

        try {
            Connection conn = dc.getConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) tbl_logs.getModel();
            model.setRowCount(0);
            tbl_logs.getTableHeader().setReorderingAllowed(false);

            while (rs.next()) {
                Vector v2 = new Vector<>();
                v2.add(rs.getString("transaction_accNum"));
                v2.add(rs.getString("transaction_date"));
                v2.add(rs.getString("transaction_amount"));
                v2.add(rs.getString("transaction_type"));
                v2.add(rs.getString("reference_num"));

                model.addRow(v2);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            java.util.logging.Logger.getLogger(AdminPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    private void setCustomRendererForColumn(String columnName) {
        TableColumnModel columnModel = tbl_data.getColumnModel();

        for (int i = 0; i < tbl_data.getColumnCount(); i++) {
            if (tbl_data.getColumnName(i).equals(columnName)) {
                columnModel.getColumn(i).setCellRenderer(new TextAreaCellRenderer());
                return;
            }
        }
    }

    private double getTotalBalance() {
        String sql = "SELECT SUM(account_balance) AS total_balance FROM sign_up";
        try {
            Connection conn = dc.getConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getDouble("total_balance");
            }
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error getting total balance: " + e.getMessage());
        }

        return 0; // Default return in case of an error
    }

    public int getTotalAccounts() {
        String sql = "SELECT COUNT(*) AS total_accounts FROM sign_up";
        try {
            Connection conn = dc.getConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt("total_accounts");
            }
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error getting total accounts: " + e.getMessage());
        }

        return 0;
    }

    public double getTotalProfit() {
        String sql = "SELECT SUM(transaction_fee) AS total_profit FROM transactions";
        try {
            Connection conn = dc.getConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getDouble("total_profit");
            }
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error getting total profit: " + e.getMessage());
        }
        return 0;
    }

    private int generateAccountNumber() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) % 100;
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int formattedMonth = Integer.parseInt(String.format("%02d", month));
        int formattedDay = Integer.parseInt(String.format("%02d", day));

        int accountNumber = year * 1000000 + formattedMonth * 10000 + formattedDay * 100 + generateRandomDigits(2);
        return accountNumber;
    }

    private int generateRandomDigits(int numberOfDigits) {
        int min = (int) Math.pow(10, numberOfDigits - 1);
        int max = (int) (Math.pow(10, numberOfDigits) - 1);
        return (int) (Math.random() * (max - min + 1) + min);
    }

    private boolean isUsernameTaken(String username) {
        if ("admin".equalsIgnoreCase(username)) {
            return true; // Username cannot be "admin"
        }

        String sql = "SELECT username FROM sign_up WHERE LOWER(username) = LOWER(?)";

        try {
            Connection conn = dc.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, username.toLowerCase());
            rs = pst.executeQuery();

            return rs.next();

        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error checking username availability: " + e.getMessage());
            return true;
        }
    }

    public String getCurrentTimestamp() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    private String generateRefID() {
        StringBuilder refID = new StringBuilder("OOP");
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            refID.append(random.nextInt(10));
        }
        refID.append("24");

        return refID.toString();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        menuAdmin1 = new swing.menu.MenuAdmin();
        body = new javax.swing.JPanel();
        pnl_dashboard = new javax.swing.JPanel();
        lbl_greet = new javax.swing.JLabel();
        panelRound1 = new swing.PanelRound();
        jLabel2 = new javax.swing.JLabel();
        lbl_totalBalance = new javax.swing.JLabel();
        panelRound2 = new swing.PanelRound();
        jLabel4 = new javax.swing.JLabel();
        lbl_totalAccounts = new javax.swing.JLabel();
        panelRound3 = new swing.PanelRound();
        jLabel3 = new javax.swing.JLabel();
        lbl_totalProfit = new javax.swing.JLabel();
        panelRound4 = new swing.PanelRound();
        jLabel5 = new javax.swing.JLabel();
        lbl_accNum1 = new javax.swing.JLabel();
        pnl_manageAccounts = new javax.swing.JPanel();
        lbl_greet1 = new javax.swing.JLabel();
        txt_search = new swing.TextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_data = new javax.swing.JTable();
        btn_edit = new swing.Button();
        btn_delete = new swing.Button();
        pnl_newAccount = new javax.swing.JPanel();
        lbl_greet2 = new javax.swing.JLabel();
        txt_lastName = new swing.TextField();
        txt_firstName = new swing.TextField();
        txt_email = new swing.TextField();
        txt_streetAddress = new swing.TextField();
        txt_brgy = new swing.TextField();
        txt_zip = new swing.TextField();
        txt_city = new swing.TextField();
        txt_province = new swing.TextField();
        txt_username = new swing.TextField();
        txt_password = new swing.PasswordField();
        btn_createAccount = new swing.Button();
        btn_cancel1 = new swing.Button();
        pnl_updateAccount = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_firstName1 = new swing.TextField();
        jLabel6 = new javax.swing.JLabel();
        txt_lastName1 = new swing.TextField();
        jLabel7 = new javax.swing.JLabel();
        txt_email1 = new swing.TextField();
        jLabel8 = new javax.swing.JLabel();
        txt_streetAddress1 = new swing.TextField();
        jLabel9 = new javax.swing.JLabel();
        txt_zip1 = new swing.TextField();
        jLabel10 = new javax.swing.JLabel();
        txt_city1 = new swing.TextField();
        txt_province1 = new swing.TextField();
        jLabel11 = new javax.swing.JLabel();
        txt_brgy1 = new swing.TextField();
        jLabel12 = new javax.swing.JLabel();
        txt_username1 = new swing.TextField();
        txt_password1 = new swing.PasswordField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lbl_greet4 = new javax.swing.JLabel();
        btn_saveChanges = new swing.Button();
        btn_cancel = new swing.Button();
        pnl_transactionLogs = new javax.swing.JPanel();
        lbl_greet3 = new javax.swing.JLabel();
        txt_search1 = new swing.TextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_logs = new javax.swing.JTable();
        pnl_transactions = new javax.swing.JPanel();
        tabbed1 = new swing.Tabbed();
        jPanel4 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        txt_accNum = new swing.TextField();
        jLabel21 = new javax.swing.JLabel();
        txt_name = new swing.TextField();
        jLabel22 = new javax.swing.JLabel();
        txt_depositAmount = new swing.TextField();
        txt_balance = new swing.TextField();
        jLabel23 = new javax.swing.JLabel();
        btn_deposit = new swing.Button();
        jPanel5 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        txt_accNum1 = new swing.TextField();
        jLabel25 = new javax.swing.JLabel();
        txt_name1 = new swing.TextField();
        jLabel26 = new javax.swing.JLabel();
        txt_withdrawAmount = new swing.TextField();
        btn_withdraw = new swing.Button();
        txt_balance1 = new swing.TextField();
        jLabel27 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        btn_transfer = new swing.Button();
        txt_transferAmount = new swing.TextField();
        jLabel30 = new javax.swing.JLabel();
        txt_sourceBalance = new swing.TextField();
        jLabel31 = new javax.swing.JLabel();
        txt_sourceName = new swing.TextField();
        txt_sourceAccNum = new swing.TextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        txt_destinationAccNum = new swing.TextField();
        txt_destinationName = new swing.TextField();
        jLabel35 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        body.setBackground(new java.awt.Color(255, 255, 255));
        body.setLayout(new java.awt.CardLayout());

        pnl_dashboard.setBackground(new java.awt.Color(255, 255, 255));

        lbl_greet.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_greet.setText("Greetings");

        panelRound1.setBackground(new java.awt.Color(198, 181, 242));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Total Balance");

        lbl_totalBalance.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_totalBalance.setText("PHP 0000000000");

        javax.swing.GroupLayout panelRound1Layout = new javax.swing.GroupLayout(panelRound1);
        panelRound1.setLayout(panelRound1Layout);
        panelRound1Layout.setHorizontalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_totalBalance)
                    .addComponent(jLabel2))
                .addContainerGap(144, Short.MAX_VALUE))
        );
        panelRound1Layout.setVerticalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_totalBalance)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        panelRound2.setBackground(new java.awt.Color(235, 243, 249));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Total Accounts");

        lbl_totalAccounts.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_totalAccounts.setText("000");

        javax.swing.GroupLayout panelRound2Layout = new javax.swing.GroupLayout(panelRound2);
        panelRound2.setLayout(panelRound2Layout);
        panelRound2Layout.setHorizontalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_totalAccounts)
                    .addComponent(jLabel4))
                .addContainerGap(84, Short.MAX_VALUE))
        );
        panelRound2Layout.setVerticalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_totalAccounts)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        panelRound3.setBackground(new java.awt.Color(254, 192, 167));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Total Revenue");

        lbl_totalProfit.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_totalProfit.setText("PHP 0000000000");

        javax.swing.GroupLayout panelRound3Layout = new javax.swing.GroupLayout(panelRound3);
        panelRound3.setLayout(panelRound3Layout);
        panelRound3Layout.setHorizontalGroup(
            panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound3Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_totalProfit)
                    .addComponent(jLabel3))
                .addContainerGap(144, Short.MAX_VALUE))
        );
        panelRound3Layout.setVerticalGroup(
            panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_totalProfit)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        panelRound4.setBackground(new java.awt.Color(235, 243, 249));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("jlabel");

        lbl_accNum1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_accNum1.setText("jlabel");

        javax.swing.GroupLayout panelRound4Layout = new javax.swing.GroupLayout(panelRound4);
        panelRound4.setLayout(panelRound4Layout);
        panelRound4Layout.setHorizontalGroup(
            panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound4Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_accNum1)
                    .addComponent(jLabel5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelRound4Layout.setVerticalGroup(
            panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound4Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_accNum1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
                    .addGroup(pnl_dashboardLayout.createSequentialGroup()
                        .addComponent(panelRound3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelRound4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(39, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelRound4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelRound3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(216, Short.MAX_VALUE))
        );

        body.add(pnl_dashboard, "card6");

        pnl_manageAccounts.setBackground(new java.awt.Color(255, 255, 255));

        lbl_greet1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_greet1.setText("List of All Accounts");

        txt_search.setHint("Search Account #");
        txt_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_searchKeyReleased(evt);
            }
        });

        tbl_data.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Account #", "Username", "Name", "Balance", "Email", "Address"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbl_data);
        if (tbl_data.getColumnModel().getColumnCount() > 0) {
            tbl_data.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbl_data.getColumnModel().getColumn(1).setPreferredWidth(50);
        }

        btn_edit.setBackground(new java.awt.Color(0, 0, 0));
        btn_edit.setForeground(new java.awt.Color(255, 255, 255));
        btn_edit.setText("Edit");
        btn_edit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editActionPerformed(evt);
            }
        });

        btn_delete.setBackground(new java.awt.Color(214, 37, 84));
        btn_delete.setForeground(new java.awt.Color(255, 255, 255));
        btn_delete.setText("Delete");
        btn_delete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_manageAccountsLayout = new javax.swing.GroupLayout(pnl_manageAccounts);
        pnl_manageAccounts.setLayout(pnl_manageAccountsLayout);
        pnl_manageAccountsLayout.setHorizontalGroup(
            pnl_manageAccountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_manageAccountsLayout.createSequentialGroup()
                .addGroup(pnl_manageAccountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnl_manageAccountsLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_delete, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl_manageAccountsLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl_manageAccountsLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(pnl_manageAccountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_greet1)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE))))
                .addGap(40, 40, 40))
        );
        pnl_manageAccountsLayout.setVerticalGroup(
            pnl_manageAccountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_manageAccountsLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(lbl_greet1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_manageAccountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_delete, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        body.add(pnl_manageAccounts, "card4");

        pnl_newAccount.setBackground(new java.awt.Color(255, 255, 255));

        lbl_greet2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_greet2.setText("New Account");

        txt_lastName.setHint("Last name");

        txt_firstName.setHint("First name");

        txt_email.setHint("Email");

        txt_streetAddress.setHint("Street address");

        txt_brgy.setHint("Barangay");

        txt_zip.setHint("ZIP code");

        txt_city.setHint("CIty/Municipality");

        txt_province.setHint("Province");

        txt_username.setHint("Username");

        txt_password.setHint("Password");

        btn_createAccount.setBackground(new java.awt.Color(0, 0, 0));
        btn_createAccount.setForeground(new java.awt.Color(255, 255, 255));
        btn_createAccount.setText("Save");
        btn_createAccount.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_createAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_createAccountActionPerformed(evt);
            }
        });

        btn_cancel1.setBackground(new java.awt.Color(248, 249, 250));
        btn_cancel1.setText("Cancel");
        btn_cancel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_cancel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancel1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_newAccountLayout = new javax.swing.GroupLayout(pnl_newAccount);
        pnl_newAccount.setLayout(pnl_newAccountLayout);
        pnl_newAccountLayout.setHorizontalGroup(
            pnl_newAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_newAccountLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(pnl_newAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_newAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(pnl_newAccountLayout.createSequentialGroup()
                            .addComponent(txt_username, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txt_password, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(pnl_newAccountLayout.createSequentialGroup()
                            .addComponent(txt_city, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txt_province, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnl_newAccountLayout.createSequentialGroup()
                            .addComponent(txt_zip, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txt_brgy, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnl_newAccountLayout.createSequentialGroup()
                            .addComponent(txt_firstName, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txt_lastName, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(txt_email, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_streetAddress, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 514, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lbl_greet2)
                    .addGroup(pnl_newAccountLayout.createSequentialGroup()
                        .addComponent(btn_createAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_cancel1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        pnl_newAccountLayout.setVerticalGroup(
            pnl_newAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_newAccountLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(lbl_greet2)
                .addGap(30, 30, 30)
                .addGroup(pnl_newAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_firstName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_lastName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txt_streetAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_newAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_zip, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_brgy, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_newAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_city, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_province, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_newAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_username, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_password, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addGroup(pnl_newAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_createAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cancel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33))
        );

        body.add(pnl_newAccount, "card5");

        pnl_updateAccount.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("First name");

        txt_firstName1.setHint("First name");

        jLabel6.setText("Last name");

        txt_lastName1.setHint("Last name");

        jLabel7.setText("Email");

        txt_email1.setHint("Email");

        jLabel8.setText("Street address");

        txt_streetAddress1.setHint("Street address");

        jLabel9.setText("ZIP code");

        txt_zip1.setHint("ZIP code");

        jLabel10.setText("City/Municipality");

        txt_city1.setHint("CIty/Municipality");

        txt_province1.setHint("Province");

        jLabel11.setText("Province");

        txt_brgy1.setHint("Barangay");

        jLabel12.setText("Barangay");

        txt_username1.setHint("Username");

        txt_password1.setHint("Password");

        jLabel13.setText("Username");

        jLabel14.setText("Password");

        lbl_greet4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_greet4.setText("List of All Accounts");

        btn_saveChanges.setBackground(new java.awt.Color(0, 0, 0));
        btn_saveChanges.setForeground(new java.awt.Color(255, 255, 255));
        btn_saveChanges.setText("Save");
        btn_saveChanges.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_saveChanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveChangesActionPerformed(evt);
            }
        });

        btn_cancel.setBackground(new java.awt.Color(248, 249, 250));
        btn_cancel.setText("Cancel");
        btn_cancel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_updateAccountLayout = new javax.swing.GroupLayout(pnl_updateAccount);
        pnl_updateAccount.setLayout(pnl_updateAccountLayout);
        pnl_updateAccountLayout.setHorizontalGroup(
            pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_updateAccountLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_updateAccountLayout.createSequentialGroup()
                        .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnl_updateAccountLayout.createSequentialGroup()
                                .addComponent(txt_city1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_province1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel10)
                                .addGroup(pnl_updateAccountLayout.createSequentialGroup()
                                    .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txt_username1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel13))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel14)
                                        .addComponent(txt_password1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                        .addGap(65, 65, 65))
                    .addGroup(pnl_updateAccountLayout.createSequentialGroup()
                        .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnl_updateAccountLayout.createSequentialGroup()
                                .addComponent(btn_saveChanges, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(txt_streetAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, 514, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(pnl_updateAccountLayout.createSequentialGroup()
                                    .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_updateAccountLayout.createSequentialGroup()
                                            .addComponent(jLabel9)
                                            .addGap(217, 217, 217))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_updateAccountLayout.createSequentialGroup()
                                            .addComponent(txt_zip1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                    .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel12)
                                        .addComponent(txt_brgy1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel11))))
                            .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel7)
                                .addComponent(txt_email1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnl_updateAccountLayout.createSequentialGroup()
                                    .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnl_updateAccountLayout.createSequentialGroup()
                                            .addComponent(jLabel1)
                                            .addGap(209, 209, 209))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_updateAccountLayout.createSequentialGroup()
                                            .addComponent(txt_firstName1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                    .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel6)
                                        .addComponent(txt_lastName1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(lbl_greet4))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        pnl_updateAccountLayout.setVerticalGroup(
            pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_updateAccountLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(lbl_greet4)
                .addGap(18, 18, 18)
                .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_lastName1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_firstName1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_email1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_streetAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_zip1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_brgy1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_city1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_province1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_username1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_password1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_saveChanges, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        body.add(pnl_updateAccount, "card3");

        pnl_transactionLogs.setBackground(new java.awt.Color(255, 255, 255));

        lbl_greet3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_greet3.setText("Transaction Logs");

        txt_search1.setHint("Search Account #");
        txt_search1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_search1KeyReleased(evt);
            }
        });

        tbl_logs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Account #", "Date", "Amount", "Type", "Reference #"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tbl_logs);
        if (tbl_logs.getColumnModel().getColumnCount() > 0) {
            tbl_logs.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbl_logs.getColumnModel().getColumn(1).setPreferredWidth(100);
            tbl_logs.getColumnModel().getColumn(3).setPreferredWidth(50);
            tbl_logs.getColumnModel().getColumn(4).setPreferredWidth(50);
        }

        javax.swing.GroupLayout pnl_transactionLogsLayout = new javax.swing.GroupLayout(pnl_transactionLogs);
        pnl_transactionLogs.setLayout(pnl_transactionLogsLayout);
        pnl_transactionLogsLayout.setHorizontalGroup(
            pnl_transactionLogsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_transactionLogsLayout.createSequentialGroup()
                .addGroup(pnl_transactionLogsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnl_transactionLogsLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(pnl_transactionLogsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_greet3)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)))
                    .addGroup(pnl_transactionLogsLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_search1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(40, 40, 40))
        );
        pnl_transactionLogsLayout.setVerticalGroup(
            pnl_transactionLogsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_transactionLogsLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(lbl_greet3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_search1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        body.add(pnl_transactionLogs, "card2");

        pnl_transactions.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel20.setText("Account Number");

        txt_accNum.setHint("Account number");
        txt_accNum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_accNumKeyReleased(evt);
            }
        });

        jLabel21.setText("Name");

        txt_name.setEditable(false);
        txt_name.setCaretColor(new java.awt.Color(255, 255, 255));
        txt_name.setHint("Name");

        jLabel22.setText("Amount");

        txt_depositAmount.setHint("PHP 0.00");

        txt_balance.setEditable(false);
        txt_balance.setCaretColor(new java.awt.Color(255, 255, 255));
        txt_balance.setHint("Balance");

        jLabel23.setText("Balance");

        btn_deposit.setBackground(new java.awt.Color(0, 0, 0));
        btn_deposit.setForeground(new java.awt.Color(255, 255, 255));
        btn_deposit.setText("Submit");
        btn_deposit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_deposit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_depositActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(txt_balance, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 46, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btn_deposit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_depositAmount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                            .addComponent(txt_accNum, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_accNum, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_balance, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addGap(46, 46, 46)))
                .addGap(18, 18, 18)
                .addComponent(jLabel22)
                .addGap(4, 4, 4)
                .addComponent(txt_depositAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 157, Short.MAX_VALUE)
                .addComponent(btn_deposit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        tabbed1.addTab("Deposit", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel24.setText("Account Number");

        txt_accNum1.setHint("Account number");
        txt_accNum1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_accNum1KeyReleased(evt);
            }
        });

        jLabel25.setText("Name");

        txt_name1.setEditable(false);
        txt_name1.setCaretColor(new java.awt.Color(255, 255, 255));
        txt_name1.setHint("Name");

        jLabel26.setText("Amount");

        txt_withdrawAmount.setHint("PHP 0.00");

        btn_withdraw.setBackground(new java.awt.Color(0, 0, 0));
        btn_withdraw.setForeground(new java.awt.Color(255, 255, 255));
        btn_withdraw.setText("Submit");
        btn_withdraw.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_withdraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_withdrawActionPerformed(evt);
            }
        });

        txt_balance1.setEditable(false);
        txt_balance1.setCaretColor(new java.awt.Color(255, 255, 255));
        txt_balance1.setHint("Balance");

        jLabel27.setText("Balance");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addComponent(txt_name1, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27)
                            .addComponent(txt_balance1, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 46, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btn_withdraw, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_withdrawAmount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                            .addComponent(txt_accNum1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.LEADING))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_accNum1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_name1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_balance1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addGap(46, 46, 46)))
                .addGap(18, 18, 18)
                .addComponent(jLabel26)
                .addGap(4, 4, 4)
                .addComponent(txt_withdrawAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 157, Short.MAX_VALUE)
                .addComponent(btn_withdraw, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        tabbed1.addTab("Withdraw", jPanel5);

        jScrollPane3.setBorder(null);
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        btn_transfer.setBackground(new java.awt.Color(0, 0, 0));
        btn_transfer.setForeground(new java.awt.Color(255, 255, 255));
        btn_transfer.setText("Submit");
        btn_transfer.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_transfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_transferActionPerformed(evt);
            }
        });

        txt_transferAmount.setHint("PHP 0.00");

        jLabel30.setText("Amount");

        txt_sourceBalance.setEditable(false);
        txt_sourceBalance.setCaretColor(new java.awt.Color(255, 255, 255));
        txt_sourceBalance.setHint("Balance");

        jLabel31.setText("Balance");

        txt_sourceName.setEditable(false);
        txt_sourceName.setCaretColor(new java.awt.Color(255, 255, 255));
        txt_sourceName.setHint("Name");

        txt_sourceAccNum.setHint("Account number");
        txt_sourceAccNum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_sourceAccNumKeyReleased(evt);
            }
        });

        jLabel32.setText("Account Number");

        jLabel33.setText("Name");

        jLabel34.setText("Transfer to");

        txt_destinationAccNum.setHint("Account number");
        txt_destinationAccNum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_destinationAccNumKeyReleased(evt);
            }
        });

        txt_destinationName.setEditable(false);
        txt_destinationName.setCaretColor(new java.awt.Color(255, 255, 255));
        txt_destinationName.setHint("Name");

        jLabel35.setText("Name");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel30)
                            .addComponent(txt_transferAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_sourceAccNum, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel32))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_destinationAccNum, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel34))))
                        .addContainerGap(46, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31)
                            .addComponent(txt_sourceBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel33)
                                    .addComponent(txt_sourceName, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel35)
                                    .addComponent(txt_destinationName, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(btn_transfer, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_sourceAccNum, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_destinationAccNum, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_sourceName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_destinationName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 19, Short.MAX_VALUE)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_sourceBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel30)
                .addGap(4, 4, 4)
                .addComponent(txt_transferAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                .addComponent(btn_transfer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        jScrollPane3.setViewportView(jPanel3);

        tabbed1.addTab("Transfer", jScrollPane3);

        javax.swing.GroupLayout pnl_transactionsLayout = new javax.swing.GroupLayout(pnl_transactions);
        pnl_transactions.setLayout(pnl_transactionsLayout);
        pnl_transactionsLayout.setHorizontalGroup(
            pnl_transactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbed1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnl_transactionsLayout.setVerticalGroup(
            pnl_transactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_transactionsLayout.createSequentialGroup()
                .addGap(0, 16, Short.MAX_VALUE)
                .addComponent(tabbed1, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        body.add(pnl_transactions, "card7");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(menuAdmin1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menuAdmin1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editActionPerformed
        try {
            int selectedRow = tbl_data.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a record to edit.");
                return;
            }

            String accNum = tbl_data.getValueAt(selectedRow, 0).toString();

            String sql = "SELECT * FROM sign_up WHERE account_number=?";
            Connection conn = dc.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, accNum);
            rs = pst.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String streetAdd = rs.getString("street_address");
                String zip = rs.getString("zip_code");
                String brgy = rs.getString("barangay");
                String city = rs.getString("city");
                String prov = rs.getString("province");
                String username = rs.getString("username");
                String password = rs.getString("password");

                // populate the text fields
                txt_firstName1.setText(firstName);
                txt_lastName1.setText(lastName);
                txt_email1.setText(email);
                txt_streetAddress1.setText(streetAdd);
                txt_zip1.setText(zip);
                txt_brgy1.setText(brgy);
                txt_city1.setText(city);
                txt_province1.setText(prov);
                txt_username1.setText(username);
                txt_password1.setText(password);
            }
            showForm(pnl_updateAccount);
        } catch (HeadlessException | ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_btn_editActionPerformed

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        int selectedRow = tbl_data.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete.");
            return;
        }

        String accNum = tbl_data.getValueAt(selectedRow, 0).toString();

        try {
            int confirmDelete = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirmDelete == JOptionPane.YES_OPTION) {
                String sql = "DELETE FROM sign_up WHERE account_number=?";
                Connection conn = dc.getConnection();
                pst = conn.prepareStatement(sql);
                pst.setString(1, accNum);
                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Record deleted successfully.");
                    fetchData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete the record.");
                }
            }

        } catch (HeadlessException | ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_btn_deleteActionPerformed

    private void txt_searchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchKeyReleased
        String accNum = txt_search.getText();
        try {
            String sql;
            if ("".equals(accNum)) {
                sql = "SELECT * FROM sign_up";
            } else {
                sql = "SELECT * FROM sign_up WHERE account_number=?";
            }

            Connection conn = dc.getConnection();
            pst = conn.prepareStatement(sql);

            if (!"".equals(accNum)) {
                pst.setString(1, accNum);
            }
            rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) tbl_data.getModel();
            model.setRowCount(0); // Clear existing rows in the table

            while (rs.next()) {
                Vector v2 = new Vector<>();
                v2.add(rs.getString("account_number"));
                v2.add(rs.getString("username"));
                v2.add(rs.getString("first_name") + " " + rs.getString("last_name"));
                v2.add(rs.getString("account_balance"));
                v2.add(rs.getString("email"));
                v2.add(rs.getString("street_address") + " " + rs.getString("barangay") + ", "
                        + rs.getString("city") + ", " + rs.getString("province"));

                model.addRow(v2);
            }
        } catch (HeadlessException | ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_txt_searchKeyReleased

    private void btn_saveChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveChangesActionPerformed
        try {
            int selectedRow = tbl_data.getSelectedRow();
            String accNum = tbl_data.getValueAt(selectedRow, 0).toString();

            String firstName = txt_firstName1.getText();
            String lastName = txt_lastName1.getText();
            String email = txt_email1.getText();
            String streetAdd = txt_streetAddress1.getText();
            String zip = txt_zip1.getText();
            String brgy = txt_brgy1.getText();
            String city = txt_city1.getText();
            String prov = txt_province1.getText();
            String username = txt_username1.getText();
            String password = txt_password1.getText();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || streetAdd.isEmpty() || zip.isEmpty()
                    || brgy.isEmpty() || city.isEmpty() || prov.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                return;
            }

            if (isUsernameTaken(username)) {
                JOptionPane.showMessageDialog(null, "Username is already taken.");
                return;
            }

            if (username.length() > 6) {
                JOptionPane.showMessageDialog(null, "Username should be limited to 6 characters.");
                return;
            }

            String sql = "UPDATE sign_up SET username=?, password=?, first_name=?, last_name=?, email=?, "
                    + "street_address=?, zip_code=?, barangay=?, city=?, province=? WHERE account_number=?";

            Connection conn = dc.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, firstName);
            pst.setString(4, lastName);
            pst.setString(5, email);
            pst.setString(6, streetAdd);
            pst.setString(7, zip);
            pst.setString(8, brgy);
            pst.setString(9, city);
            pst.setString(10, prov);
            pst.setString(11, accNum);
            pst.execute();

            fetchData();
            JOptionPane.showMessageDialog(null, "Successfully changed details.");
        } catch (SQLException | ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_saveChangesActionPerformed

    private void btn_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelActionPerformed
        txt_username1.setText("");
        txt_password1.setText("");
        txt_firstName1.setText("");
        txt_lastName1.setText("");
        txt_email1.setText("");
        txt_streetAddress1.setText("");
        txt_zip1.setText("");
        txt_brgy1.setText("");
        txt_city1.setText("");
        txt_province1.setText("");
    }//GEN-LAST:event_btn_cancelActionPerformed

    private void btn_createAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_createAccountActionPerformed
        int account_number = generateAccountNumber();
        double account_balance = 0;
        java.util.Date currentDate = new java.util.Date();
        java.sql.Timestamp timestamp = new java.sql.Timestamp(currentDate.getTime());
        String firstName = txt_firstName.getText();
        String lastName = txt_lastName.getText();
        String email = txt_email.getText();
        String username = txt_username.getText();
        String password = txt_password.getText();
        String streetAddress = txt_streetAddress.getText();
        String zipCode = txt_zip.getText();
        String barangay = txt_brgy.getText();
        String city = txt_city.getText();
        String province = txt_province.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()
                || streetAddress.isEmpty() || zipCode.isEmpty() || barangay.isEmpty() || city.isEmpty() || province.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all the fields");
            return;
        }

        if (isUsernameTaken(username)) {
            JOptionPane.showMessageDialog(null, "Username is already taken.");
            return;
        }

        if (username.length() > 6) {
            JOptionPane.showMessageDialog(null, "Username should be limited to 6 characters.");
            return;
        }

        String sql = "INSERT INTO sign_up (account_number, username, password, account_balance, date_added, first_name, last_name, email,"
                + "street_address, zip_code, barangay, city, province) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            Connection conn = dc.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, account_number);
            pst.setString(2, username);
            pst.setString(3, password);
            pst.setDouble(4, account_balance);
            pst.setTimestamp(5, timestamp);
            pst.setString(6, firstName);
            pst.setString(7, lastName);
            pst.setString(8, email);
            pst.setString(9, streetAddress);
            pst.setString(10, zipCode);
            pst.setString(11, barangay);
            pst.setString(12, city);
            pst.setString(13, province);
            pst.execute();

            fetchData();
            JOptionPane.showMessageDialog(null, "Account created successfully");

            txt_username.setText("");
            txt_password.setText("");
            txt_firstName.setText("");
            txt_lastName.setText("");
            txt_email.setText("");
            txt_streetAddress.setText("");
            txt_zip.setText("");
            txt_brgy.setText("");
            txt_city.setText("");
            txt_province.setText("");

        } catch (HeadlessException | ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_btn_createAccountActionPerformed

    private void btn_cancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancel1ActionPerformed
        txt_username.setText("");
        txt_password.setText("");
        txt_firstName.setText("");
        txt_lastName.setText("");
        txt_email.setText("");
        txt_streetAddress.setText("");
        txt_zip.setText("");
        txt_brgy.setText("");
        txt_city.setText("");
        txt_province.setText("");
    }//GEN-LAST:event_btn_cancel1ActionPerformed

    private void txt_search1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_search1KeyReleased
        String accNum = txt_search1.getText();
        try {
            String sql;
            if ("".equals(accNum)) {
                sql = "SELECT * FROM transactions";
            } else {
                sql = "SELECT * FROM transactions WHERE transaction_accNum=?";
            }

            Connection conn = dc.getConnection();
            pst = conn.prepareStatement(sql);

            if (!"".equals(accNum)) {
                pst.setString(1, accNum);
            }
            rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) tbl_logs.getModel();
            model.setRowCount(0); // Clear existing rows in the table

            while (rs.next()) {
                Vector v2 = new Vector<>();
                v2.add(rs.getString("transaction_accNum"));
                v2.add(rs.getString("transaction_date"));
                v2.add(rs.getString("transaction_amount"));
                v2.add(rs.getString("transaction_type"));
                v2.add(rs.getString("reference_num"));

                model.addRow(v2);
            }

        } catch (HeadlessException | ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_txt_search1KeyReleased

    private void txt_accNumKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_accNumKeyReleased
        String accountNumber = txt_accNum.getText();

        if (!accountNumber.isEmpty()) {
            try {
                String sql = "SELECT first_name, last_name, account_balance FROM sign_up WHERE account_number=?";
                try (Connection conn = dc.getConnection()) {
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, accountNumber);
                    rs = pst.executeQuery();

                    if (rs.next()) {
                        String firstName = rs.getString("first_name");
                        String lastName = rs.getString("last_name");
                        String fullName = firstName + " " + lastName;
                        double balance = rs.getDouble("account_balance");
                        txt_name.setText(fullName);
                        txt_balance.setText(Double.toString(balance));

                    } else {
                        txt_name.setText("Not Found");
                        txt_balance.setText("0");
                    }
                }
            } catch (SQLException | ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(AdminPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_txt_accNumKeyReleased

    private void btn_depositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_depositActionPerformed
        String timestamp = getCurrentTimestamp();
        String accountNumber = txt_accNum.getText();
        double amount = Double.parseDouble(txt_depositAmount.getText());
        String strAmount = String.format("%.2f", amount);
        double depositAmount = Double.parseDouble(strAmount);
        String referenceID = generateRefID();

        try {
            if (depositAmount < 100 || depositAmount > 100000) {
                JOptionPane.showMessageDialog(null, "Deposit amount must be between PHP 100 and 100,000.");
                txt_depositAmount.setText("");
                return;
            }

            try {
                String sql1 = "SELECT * FROM sign_up WHERE account_number=?";
                Connection conn = dc.getConnection();
                pst1 = conn.prepareStatement(sql1);
                pst1.setString(1, accountNumber);
                rs = pst1.executeQuery();

                if (rs.next()) {
                    double currentBalance = rs.getDouble("account_balance");
                    double sum = depositAmount + currentBalance;
                    String num = String.format("%.2f", sum);
                    double newBalance = Double.parseDouble(num);

                    try {
                        String sql2 = "UPDATE sign_up SET account_balance=? WHERE account_number=?";
                        pst2 = conn.prepareStatement(sql2);
                        pst2.setDouble(1, newBalance);
                        pst2.setString(2, accountNumber);
                        pst2.execute();
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, e);
                    }

                    JOptionPane.showMessageDialog(null, "Deposit Successful.");
                    txt_name.setText("");
                    txt_accNum.setText("");
                    txt_balance.setText("");
                    txt_depositAmount.setText("");

                    try {
                        String sql = "INSERT INTO transactions (transaction_date, transaction_accNum, transaction_amount, transaction_type, reference_num, transaction_fee) VALUES (?,?,?,?,?,?)";
                        pst = conn.prepareStatement(sql);
                        pst.setString(1, timestamp);
                        pst.setString(2, accountNumber);
                        pst.setDouble(3, depositAmount);
                        pst.setString(4, "DEPOSIT");
                        pst.setString(5, referenceID);
                        pst.setDouble(6, 0);
                        pst.execute();
                        transactionTable();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Account number not found. Please enter a valid account number.");
                    txt_name.setText("");
                    txt_accNum.setText("");
                    txt_balance.setText("");
                    txt_depositAmount.setText("");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_depositActionPerformed

    private void txt_accNum1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_accNum1KeyReleased
        String accountNumber = txt_accNum1.getText();

        if (!accountNumber.isEmpty()) {
            try {
                String sql = "SELECT first_name, last_name, account_balance FROM sign_up WHERE account_number=?";
                try (Connection conn = dc.getConnection()) {
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, accountNumber);
                    rs = pst.executeQuery();

                    if (rs.next()) {
                        String firstName = rs.getString("first_name");
                        String lastName = rs.getString("last_name");
                        String fullName = firstName + " " + lastName;
                        double balance = rs.getDouble("account_balance");
                        txt_name1.setText(fullName);
                        txt_balance1.setText(Double.toString(balance));

                    } else {
                        txt_name1.setText("Not Found");
                        txt_balance1.setText("0");
                    }
                }
            } catch (SQLException | ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(AdminPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_txt_accNum1KeyReleased

    private void btn_withdrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_withdrawActionPerformed
        String timestamp = getCurrentTimestamp();
        String accountNumber = txt_accNum1.getText();
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

                                    transactionTable();
                                    JOptionPane.showMessageDialog(null, "Withdraw Successful.");
                                    txt_name1.setText("");
                                    txt_accNum1.setText("");
                                    txt_balance1.setText("");
                                    txt_withdrawAmount.setText("");
                                } catch (SQLException e) {
                                    JOptionPane.showMessageDialog(null, e);
                                }
                            } catch (SQLException e) {
                                JOptionPane.showMessageDialog(null, e);
                            }
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Account number not found. Please enter a valid account number.");
                txt_name1.setText("");
                txt_accNum1.setText("");
                txt_balance1.setText("");
                txt_withdrawAmount.setText("");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_withdrawActionPerformed

    private void txt_sourceAccNumKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_sourceAccNumKeyReleased
        String accountNumber = txt_sourceAccNum.getText();

        if (!accountNumber.isEmpty()) {
            try {
                String sql = "SELECT first_name, last_name, account_balance FROM sign_up WHERE account_number=?";
                try (Connection conn = dc.getConnection()) {
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, accountNumber);
                    rs = pst.executeQuery();

                    if (rs.next()) {
                        String firstName = rs.getString("first_name");
                        String lastName = rs.getString("last_name");
                        String fullName = firstName + " " + lastName;
                        double balance = rs.getDouble("account_balance");
                        txt_sourceName.setText(fullName);
                        txt_sourceBalance.setText(Double.toString(balance));

                    } else {
                        txt_sourceName.setText("Not Found");
                        txt_sourceBalance.setText("0");
                    }
                }
            } catch (SQLException | ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(AdminPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_txt_sourceAccNumKeyReleased

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
                java.util.logging.Logger.getLogger(AdminPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_txt_destinationAccNumKeyReleased

    private void btn_transferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_transferActionPerformed
        try {
            String timestamp = getCurrentTimestamp();
            String sourceAccount = txt_sourceAccNum.getText();
            String destinationAccount = txt_destinationAccNum.getText();
            double amount = Double.parseDouble(txt_transferAmount.getText());
            String strAmount = String.format("%.2f", amount);
            double transferAmount = Double.parseDouble(strAmount);
            String referenceID = generateRefID();

            if (sourceAccount.equals(destinationAccount)) {
                JOptionPane.showMessageDialog(null, "Cannot transfer to your own account.");
                return;
            }

            Connection conn = dc.getConnection();
            String checkDestinationAccountSql = "SELECT * FROM sign_up WHERE account_number = ?";
            pst5 = conn.prepareStatement(checkDestinationAccountSql);
            pst5.setString(1, destinationAccount);
            ResultSet rsDestAccount = pst5.executeQuery();

            if (!rsDestAccount.next()) {
                JOptionPane.showMessageDialog(null, "Invalid destination account number.");
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
                    transactionTable();

                    String sql4 = "INSERT INTO transactions (transaction_date, transaction_accNum, transaction_amount, transaction_type, reference_num, transaction_fee) VALUES (?,?,?,?,?,?)";
                    pst4 = conn.prepareStatement(sql4);
                    pst4.setString(1, timestamp);
                    pst4.setString(2, destinationAccount);
                    pst4.setDouble(3, transferAmount);
                    pst4.setString(4, "RECEIVED");
                    pst4.setString(5, referenceID);
                    pst4.setDouble(6, 0);
                    pst4.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Transaction Success.");
                } else {
                    JOptionPane.showMessageDialog(null, "Transfer amount must be between PHP 100 and PHP " + (currentBalance - convenienceFee) + " (including convenience fee).");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Account number not found. Please enter a valid account number.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminPage.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Clear text fields regardless of the condition
            txt_sourceAccNum.setText("");
            txt_sourceName.setText("");
            txt_sourceBalance.setText("");
            txt_destinationAccNum.setText("");
            txt_destinationName.setText("");
            txt_transferAmount.setText("");
        }
    }//GEN-LAST:event_btn_transferActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel body;
    private swing.Button btn_cancel;
    private swing.Button btn_cancel1;
    private swing.Button btn_createAccount;
    private swing.Button btn_delete;
    private swing.Button btn_deposit;
    private swing.Button btn_edit;
    private swing.Button btn_saveChanges;
    private swing.Button btn_transfer;
    private swing.Button btn_withdraw;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbl_accNum1;
    private javax.swing.JLabel lbl_greet;
    private javax.swing.JLabel lbl_greet1;
    private javax.swing.JLabel lbl_greet2;
    private javax.swing.JLabel lbl_greet3;
    private javax.swing.JLabel lbl_greet4;
    private javax.swing.JLabel lbl_totalAccounts;
    private javax.swing.JLabel lbl_totalBalance;
    private javax.swing.JLabel lbl_totalProfit;
    private swing.menu.MenuAdmin menuAdmin1;
    private swing.PanelRound panelRound1;
    private swing.PanelRound panelRound2;
    private swing.PanelRound panelRound3;
    private swing.PanelRound panelRound4;
    private javax.swing.JPanel pnl_dashboard;
    private javax.swing.JPanel pnl_manageAccounts;
    private javax.swing.JPanel pnl_newAccount;
    private javax.swing.JPanel pnl_transactionLogs;
    private javax.swing.JPanel pnl_transactions;
    private javax.swing.JPanel pnl_updateAccount;
    private swing.Tabbed tabbed1;
    private javax.swing.JTable tbl_data;
    private javax.swing.JTable tbl_logs;
    private swing.TextField txt_accNum;
    private swing.TextField txt_accNum1;
    private swing.TextField txt_balance;
    private swing.TextField txt_balance1;
    private swing.TextField txt_brgy;
    private swing.TextField txt_brgy1;
    private swing.TextField txt_city;
    private swing.TextField txt_city1;
    private swing.TextField txt_depositAmount;
    private swing.TextField txt_destinationAccNum;
    private swing.TextField txt_destinationName;
    private swing.TextField txt_email;
    private swing.TextField txt_email1;
    private swing.TextField txt_firstName;
    private swing.TextField txt_firstName1;
    private swing.TextField txt_lastName;
    private swing.TextField txt_lastName1;
    private swing.TextField txt_name;
    private swing.TextField txt_name1;
    private swing.PasswordField txt_password;
    private swing.PasswordField txt_password1;
    private swing.TextField txt_province;
    private swing.TextField txt_province1;
    private swing.TextField txt_search;
    private swing.TextField txt_search1;
    private swing.TextField txt_sourceAccNum;
    private swing.TextField txt_sourceBalance;
    private swing.TextField txt_sourceName;
    private swing.TextField txt_streetAddress;
    private swing.TextField txt_streetAddress1;
    private swing.TextField txt_transferAmount;
    private swing.TextField txt_username;
    private swing.TextField txt_username1;
    private swing.TextField txt_withdrawAmount;
    private swing.TextField txt_zip;
    private swing.TextField txt_zip1;
    // End of variables declaration//GEN-END:variables
}
