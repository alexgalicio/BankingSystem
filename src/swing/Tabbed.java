package swing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalTabbedPaneUI;
import org.jdesktop.animation.timing.TimingTarget;

public class Tabbed extends JTabbedPane {

    public Tabbed() {
        setUI(new MaterialTabbedUI());
    }

    public class MaterialTabbedUI extends MetalTabbedPaneUI {

        private Rectangle currentRectangle;
        private TimingTarget target;

        public MaterialTabbedUI() {
        }

        public void setCurrentRectangle(Rectangle currentRectangle) {
            this.currentRectangle = currentRectangle;
            repaint();
        }

        @Override
        public void installUI(JComponent jc) {
            super.installUI(jc);
            tabPane.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent ce) {
                    int selected = tabPane.getSelectedIndex();
                    if (selected != -1) {
                        if (currentRectangle != null) {
                            currentRectangle = getTabBounds(selected, calcRect);
                        }
                    }
                }
            });
        }

        @Override
        protected Insets getTabInsets(int i, int i1) {
            return new Insets(10, 10, 10, 10);
        }

        @Override
        protected void paintTabBorder(Graphics grphcs, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            Graphics2D g2 = (Graphics2D) grphcs.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            g2.setColor(new Color(3, 155, 216));
            if (currentRectangle == null) {
                if (isSelected) {
                    currentRectangle = new Rectangle(x, y, w, h);
                }
            }
            if (currentRectangle != null) {
                g2.fillRect(currentRectangle.x, currentRectangle.y + currentRectangle.height - 3, currentRectangle.width, 3);
            }
            g2.dispose();
        }

        @Override
        protected void paintContentBorder(Graphics grphcs, int tabPlacement, int selectedIndex) {
            Graphics2D g2 = (Graphics2D) grphcs.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            g2.setColor(new Color(200, 200, 200));

            calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
            g2.dispose();
        }

        @Override
        protected void paintFocusIndicator(Graphics grphcs, int i, Rectangle[] rctngls, int i1, Rectangle rctngl, Rectangle rctngl1, boolean bln) {

        }

        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            if (tabPane.isOpaque()) {
                super.paintTabBackground(g, tabPlacement, tabIndex, x, y, w, h, isSelected);
            }
        }
    }
}
