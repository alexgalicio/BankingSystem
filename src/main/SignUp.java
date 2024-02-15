
package main;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import javax.swing.JOptionPane;

public class SignUp extends javax.swing.JFrame {

    private DatabaseConnection dc = new DatabaseConnection();
    private PreparedStatement pst;
    private ResultSet rs;
    
    public SignUp() {
        initComponents();
    }
    
    private int generateAccountNumber() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) % 100; // Taking only last two digits of the year
        int month = calendar.get(Calendar.MONTH) + 1; // Month is zero-based
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int formattedMonth = Integer.parseInt(String.format("%02d", month));
        int formattedDay = Integer.parseInt(String.format("%02d", day));

        // Concatenate year, month, day, and 2 random digits
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

            return rs.next(); // Returns true if username is already taken

        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error checking username availability: " + e.getMessage());
            return true; // Assume an error occurred and block the registration
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txt_firstName = new swing.TextField();
        jLabel1 = new javax.swing.JLabel();
        txt_lastName = new swing.TextField();
        txt_email = new swing.TextField();
        txt_zip = new swing.TextField();
        txt_streetAddress = new swing.TextField();
        txt_brgy = new swing.TextField();
        txt_city = new swing.TextField();
        txt_username = new swing.TextField();
        txt_province = new swing.TextField();
        btn_signUp = new swing.Button();
        jLabel2 = new javax.swing.JLabel();
        btn_logIn = new javax.swing.JButton();
        txt_password = new swing.PasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        txt_firstName.setHint("First name");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Create an account");

        txt_lastName.setHint("Last name");

        txt_email.setHint("Email");

        txt_zip.setHint("ZIP code");

        txt_streetAddress.setHint("Street address");

        txt_brgy.setHint("Barangay");

        txt_city.setHint("CIty/Municipality");

        txt_username.setHint("Username");

        txt_province.setHint("Province");

        btn_signUp.setBackground(new java.awt.Color(0, 0, 0));
        btn_signUp.setForeground(new java.awt.Color(255, 255, 255));
        btn_signUp.setText("Sign Up");
        btn_signUp.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_signUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_signUpActionPerformed(evt);
            }
        });

        jLabel2.setText("Already have an account?");

        btn_logIn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_logIn.setForeground(new java.awt.Color(11, 87, 208));
        btn_logIn.setText("Log In ");
        btn_logIn.setBorder(null);
        btn_logIn.setContentAreaFilled(false);
        btn_logIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_logInActionPerformed(evt);
            }
        });

        txt_password.setHint("Password");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(168, 168, 168)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txt_username, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_password, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txt_city, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_province, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txt_zip, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_brgy, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txt_firstName, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_lastName, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txt_email, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_streetAddress, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_signUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(168, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_logIn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel1)
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_firstName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_lastName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txt_streetAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_zip, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_brgy, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_city, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_province, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_username, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_password, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(btn_signUp, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(btn_logIn))
                .addGap(49, 49, 49))
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

    private void btn_logInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_logInActionPerformed
        new LogIn().show();
        this.dispose();
    }//GEN-LAST:event_btn_logInActionPerformed

    private void btn_signUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_signUpActionPerformed
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

        // check if any of the fields is not empty
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()
                || streetAddress.isEmpty() || zipCode.isEmpty() || barangay.isEmpty() || city.isEmpty() || province.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all the fields");
            return;
        }

        // Check if username is already taken
        if (isUsernameTaken(username)) {
            JOptionPane.showMessageDialog(null, "Username is already taken.");
            return;
        }

        // Check if username is limited to 6 characters
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
    }//GEN-LAST:event_btn_signUpActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SignUp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_logIn;
    private swing.Button btn_signUp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private swing.TextField txt_brgy;
    private swing.TextField txt_city;
    private swing.TextField txt_email;
    private swing.TextField txt_firstName;
    private swing.TextField txt_lastName;
    private swing.PasswordField txt_password;
    private swing.TextField txt_province;
    private swing.TextField txt_streetAddress;
    private swing.TextField txt_username;
    private swing.TextField txt_zip;
    // End of variables declaration//GEN-END:variables
}
