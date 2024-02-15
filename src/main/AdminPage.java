package main;

import java.awt.Component;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import swing.TextAreaCellRenderer;

public class AdminPage extends javax.swing.JFrame {

    private DatabaseConnection dc = new DatabaseConnection();
    private PreparedStatement pst;
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
        jLabel2.setText("Bank Balance");

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
        jLabel3.setText("Bank Profit");

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
                    .addGroup(pnl_dashboardLayout.createSequentialGroup()
                        .addComponent(panelRound3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelRound4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lbl_greet)
                    .addGroup(pnl_dashboardLayout.createSequentialGroup()
                        .addComponent(panelRound1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelRound2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                            .addGroup(pnl_manageAccountsLayout.createSequentialGroup()
                                .addComponent(lbl_greet1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGroup(pnl_manageAccountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addGroup(pnl_newAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_createAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cancel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40))
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
                .addGroup(pnl_updateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_firstName1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_lastName1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                            .addGroup(pnl_transactionLogsLayout.createSequentialGroup()
                                .addComponent(lbl_greet3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            java.util.logging.Logger.getLogger(UserMenu.class.getName()).log(Level.SEVERE, null, ex);
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
    private swing.Button btn_edit;
    private swing.Button btn_saveChanges;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
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
    private javax.swing.JPanel pnl_updateAccount;
    private javax.swing.JTable tbl_data;
    private javax.swing.JTable tbl_logs;
    private swing.TextField txt_brgy;
    private swing.TextField txt_brgy1;
    private swing.TextField txt_city;
    private swing.TextField txt_city1;
    private swing.TextField txt_email;
    private swing.TextField txt_email1;
    private swing.TextField txt_firstName;
    private swing.TextField txt_firstName1;
    private swing.TextField txt_lastName;
    private swing.TextField txt_lastName1;
    private swing.PasswordField txt_password;
    private swing.PasswordField txt_password1;
    private swing.TextField txt_province;
    private swing.TextField txt_province1;
    private swing.TextField txt_search;
    private swing.TextField txt_search1;
    private swing.TextField txt_streetAddress;
    private swing.TextField txt_streetAddress1;
    private swing.TextField txt_username;
    private swing.TextField txt_username1;
    private swing.TextField txt_zip;
    private swing.TextField txt_zip1;
    // End of variables declaration//GEN-END:variables
}
