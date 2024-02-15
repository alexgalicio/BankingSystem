
package swing.menu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class MenuItem extends javax.swing.JPanel {

    private boolean selected;

    public MenuItem(ModelMenu data) {
        initComponents();
        setOpaque(false);
        if (data.getType() == ModelMenu.MenuType.MENU) {
            lb_icon.setIcon(data.toIcon());
            lb_name.setText(data.getName());
        } else if (data.getType() == ModelMenu.MenuType.TITLE) {
            lb_icon.setText(data.getName());
            lb_name.setVisible(false);
        } else {
            lb_name.setText(" ");
        }
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (selected) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.BLACK);
//            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
            g2.fillRect(0, 0, 3, getHeight());
            
        }
        super.paintComponent(g);
    }
    


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lb_icon = new javax.swing.JLabel();
        lb_name = new javax.swing.JLabel();

        lb_name.setText("Menu Name");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(lb_icon)
                .addGap(18, 18, 18)
                .addComponent(lb_name)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lb_icon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lb_name, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lb_icon;
    private javax.swing.JLabel lb_name;
    // End of variables declaration//GEN-END:variables
}
