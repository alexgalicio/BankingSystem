package swing.menu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class MenuAdmin extends javax.swing.JPanel {

    private EventMenuSelected event;

    public MenuAdmin() {
        initComponents();
        setOpaque(false);
        listMenu1.setOpaque(false);
        init();
    }
    
    public void addEventMenuSelected(EventMenuSelected event) {
        this.event = event;
        listMenu1.addEventMenuSelected(event);
    }
    
    private void init() {
        listMenu1.addItem(new ModelMenu("dashboard", "Dashboard", ModelMenu.MenuType.MENU));
        listMenu1.addItem(new ModelMenu("add", "Manage Account", ModelMenu.MenuType.MENU));
        listMenu1.addItem(new ModelMenu("manage", "New Accounts", ModelMenu.MenuType.MENU));
        listMenu1.addItem(new ModelMenu("logs", "Transaction Logs", ModelMenu.MenuType.MENU));
        listMenu1.addItem(new ModelMenu("", "", ModelMenu.MenuType.EMPTY));
        listMenu1.addItem(new ModelMenu("", "", ModelMenu.MenuType.EMPTY));
        listMenu1.addItem(new ModelMenu("", "", ModelMenu.MenuType.EMPTY));
        listMenu1.addItem(new ModelMenu("", "", ModelMenu.MenuType.EMPTY));
        listMenu1.addItem(new ModelMenu("", "", ModelMenu.MenuType.EMPTY));
        listMenu1.addItem(new ModelMenu("", "", ModelMenu.MenuType.EMPTY));
        listMenu1.addItem(new ModelMenu("", "", ModelMenu.MenuType.EMPTY));
        listMenu1.addItem(new ModelMenu("logout", "Logout", ModelMenu.MenuType.MENU));
    }

    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(248, 249, 250));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
        g2.fillRect(getWidth() - 20, 0, getWidth(), getHeight());
        super.paintChildren(g);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_title = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        listMenu1 = new swing.menu.ListMenu<>();

        pnl_title.setBackground(new java.awt.Color(248, 249, 250));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("GIZMO");

        javax.swing.GroupLayout pnl_titleLayout = new javax.swing.GroupLayout(pnl_title);
        pnl_title.setLayout(pnl_titleLayout);
        pnl_titleLayout.setHorizontalGroup(
            pnl_titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_titleLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addContainerGap(127, Short.MAX_VALUE))
        );
        pnl_titleLayout.setVerticalGroup(
            pnl_titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_titleLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(listMenu1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnl_title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listMenu1, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private swing.menu.ListMenu<String> listMenu1;
    private javax.swing.JPanel pnl_title;
    // End of variables declaration//GEN-END:variables
}
