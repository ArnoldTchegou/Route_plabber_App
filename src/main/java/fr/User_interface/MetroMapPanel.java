package fr.User_interface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import fr.Lecture_Reseau.Link;
import fr.Lecture_Reseau.Network;
import fr.Lecture_Reseau.Stop;

public class MetroMapPanel extends JPanel implements MouseListener, MouseMotionListener {
    private final int threshold = 10;
    private int prevMouseX;
    private int prevMouseY;
    private Network network;
    private String currentStopName = "";
    private int offsetX = 1;
    private int offsetY = 2;
    public double zoomFactor;

    public MetroMapPanel(Network network, double zoomFactor){
        this.network  = new Network();
        this.zoomFactor = zoomFactor;
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    public void setNetwork(Network network) {
        this.network = network;
        repaint();
    }

    public void setZoomfactor(double x){
        this.zoomFactor = x;
    }

    public double getZoomfactor(){
        return this.zoomFactor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMetroStopsAndLines(g, offsetX, offsetY);
    }

    private void drawMetroStopsAndLines(Graphics g, int offsetX, int offsetY) {
        final int panelWidth = getWidth();
        final int panelHeight = getHeight();
        final int centerX = panelWidth / 2 + offsetX;
        final int centerY = panelHeight / 2 + offsetY;

        Map<String, Color> lineColorMap = Factory.createLineColorMap();

        for (Stop stop : network.getStops()) {
            int x = centerX + (int) ((stop.getLongitude() - 2.2976831860125837) * panelWidth * this.zoomFactor);
            int y = centerY + (int) ((stop.getLatitude() - 48.88484432273985) * panelHeight * this.zoomFactor);
            g.setColor(Color.BLACK);
            g.fillOval(x - 5, y - 5, 10, 10);

            Point mousePosition = getMousePosition();
            if (mousePosition != null) {
                int mouseX = mousePosition.x;
                int mouseY = mousePosition.y;
                double distance = Math.sqrt(Math.pow(mouseX - x, 2) + Math.pow(mouseY - y, 2));

                if (distance < threshold) {
                    currentStopName = stop.getStopName();
                }
            }

            if (currentStopName.equals(stop.getStopName())) {
                g.setColor(Color.BLACK);
                g.drawString(currentStopName, x + 10, y + 10);
            }
        }

        for (Link link : network.getLinks()) {
            String lineName = link.getLineName();
            Color lineColor = lineColorMap.getOrDefault(lineName, Color.BLACK);

            g.setColor(lineColor);

            int x1 = centerX + (int) ((link.getSource().getLongitude() - 2.2976831860125837) * panelWidth * zoomFactor);
            int y1 = centerY + (int) ((link.getSource().getLatitude() - 48.88484432273985) * panelHeight * zoomFactor);
            int x2 = centerX + (int) ((link.getDestination().getLongitude() - 2.2976831860125837) * panelWidth * zoomFactor);
            int y2 = centerY + (int) ((link.getDestination().getLatitude() - 48.88484432273985) * panelHeight * zoomFactor);
            g.drawLine(x1, y1, x2, y2);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (Stop stop : network.getStops()) {
            int x = calculateXCoordinate(stop);
            int y = calculateYCoordinate(stop);

            if (isClickOnStop(e.getX(), e.getY(), x, y)) {
                showStationInfo(stop);
                return;
            }
        }
    }

    private int calculateXCoordinate(Stop stop) {
        final int centerX = getWidth() / 2 + offsetX;
        return centerX + (int) ((stop.getLongitude() - 2.2976831860125837) * getWidth() * zoomFactor);
    }

    private int calculateYCoordinate(Stop stop) {
        final int centerY = getHeight() / 2 + offsetY;
        return centerY + (int) ((stop.getLatitude() - 48.88484432273985) * getHeight() * zoomFactor);
    }

    private boolean isClickOnStop(int mouseX, int mouseY, int stopX, int stopY) {
        final int clickThreshold = 20;
        double distance = Math.sqrt(Math.pow(mouseX - stopX, 2) + Math.pow(mouseY - stopY, 2));
        return distance < clickThreshold;
    }

    private void showStationInfo(Stop stop) {
        JOptionPane.showMessageDialog(
                this,
                "Station: " + stop.getStopName() + "\nLatitude: " + stop.getLatitude() + "\nLongitude: " + stop.getLongitude(),
                "Station Information",
                JOptionPane.INFORMATION_MESSAGE
        );
        // Réinitialiser le nom de la station sélectionnée
        currentStopName = "";
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {
        prevMouseX = e.getX();
        prevMouseY = e.getY();
    }
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {
        int dx = e.getX() - prevMouseX;
        int dy = e.getY() - prevMouseY;

        offsetX += dx;
        offsetY += dy;

        repaint();

        prevMouseX = e.getX();
        prevMouseY = e.getY();
    }
    @Override
    public void mouseMoved(MouseEvent e) {}
}
