package fr.u_paris.gla.project.User_interface;
import javax.swing.*;

import fr.u_paris.gla.project.Lecture_Reseau.Link;
import fr.u_paris.gla.project.Lecture_Reseau.Network;
import fr.u_paris.gla.project.Lecture_Reseau.Stop;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class MetroMapUI extends JFrame {
    private Network network = new Network();
    private String currentStopName = "";
    private double zoomFactor = 4.9;
    private int offsetX = 1;
    private int offsetY = 2;

    public MetroMapUI(Network network) {
        super("Metro Map UI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        MetroMapPanel mapPanel = new MetroMapPanel();
        JScrollPane scrollPane = new JScrollPane(mapPanel);
        add(scrollPane);

        JPanel buttonPanel = new JPanel();
        JButton zoomInButton = new JButton("Zoom In");
        JButton zoomOutButton = new JButton("Zoom Out");
        JButton showall = new JButton("Show Network");
        buttonPanel.add(zoomInButton);
        buttonPanel.add(zoomOutButton);
        buttonPanel.add(showall);
        add(buttonPanel, BorderLayout.SOUTH);

        zoomInButton.addActionListener(e -> {
            zoomIn();
            mapPanel.repaint();
        });
        zoomOutButton.addActionListener(e -> {
            zoomOut();
            mapPanel.repaint();
        });

        showall.addActionListener(e -> {
            network.restoreOriginalLinks();
            repaint();
        });

       // Légende des couleurs de lignes
JPanel legendPanel = new JPanel();
legendPanel.setLayout(new GridLayout(0, 2));
Map<String, Color> lineColorMap = createLineColorMap();

// Boucle à travers les entrées de lineColorMap
for (Map.Entry<String, Color> entry : lineColorMap.entrySet()) {
    // Récupérer le nom de la ligne et sa couleur
    String lineName = entry.getKey();
    Color color = entry.getValue();

    // Créer un label avec le nom de la ligne
    JLabel label = new JLabel(lineName);

    // Créer un panel de couleur avec la couleur de la ligne
    JPanel colorPanel = new JPanel();
    colorPanel.setBackground(color);

    // Ajouter un écouteur de clic au label
    label.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            // Récupérer le nom de la ligne associé à ce label
            String clickedLineName = label.getText();
            // Filtrer les données du graphique pour n'afficher que les données de cette ligne
            network.restoreOriginalLinks();
            network.saveOriginalLinks();
            filterGraphData(clickedLineName, network);
        }
    });

    // Ajouter le label et le panel de couleur au panneau de légende
    legendPanel.add(label);
    legendPanel.add(colorPanel);
}

    // Ajouter le panneau de légende à votre interface

    add(legendPanel, BorderLayout.EAST);

        setVisible(true);
    }

    class MetroMapPanel extends JPanel implements MouseListener, MouseMotionListener {
        private final int threshold = 10;
        private int prevMouseX;
        private int prevMouseY;

        public MetroMapPanel() {
            addMouseListener(this);
            addMouseMotionListener(this);
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

            Map<String, Color> lineColorMap = createLineColorMap();

            for (Stop stop : network.getStops()) {
                int x = centerX + (int) ((stop.getLongitude() - 2.2976831860125837) * panelWidth * zoomFactor);
                int y = centerY + (int) ((stop.getLatitude() - 48.88484432273985) * panelHeight * zoomFactor);
                g.setColor(Color.GREEN);
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

    private void zoomIn() {
        zoomFactor += 0.1;
    }

    private void zoomOut() {
        if (zoomFactor > 0.1) {
            zoomFactor -= 0.1;
        }
    }

    private Map<String, Color> createLineColorMap() {
        Map<String, Color> lineColorMap = new HashMap<>();
        lineColorMap.put("3", Color.GREEN);
        lineColorMap.put("4", Color.YELLOW);
        lineColorMap.put("5", new Color(255, 127, 0));
        lineColorMap.put("6", new Color(139, 69, 19));
        lineColorMap.put("7", Color.MAGENTA);
        lineColorMap.put("8", Color.PINK);
        lineColorMap.put("9", new Color(148, 0, 211));
        lineColorMap.put("10", new Color(0, 255, 255));
        lineColorMap.put("11", new Color(255, 0, 255));
        lineColorMap.put("12", new Color(0, 100, 0));
        lineColorMap.put("13", new Color(210, 105, 30));
        lineColorMap.put("3B", Color.CYAN);
        lineColorMap.put("7B", Color.LIGHT_GRAY);
        lineColorMap.put("14", Color.RED);
        return lineColorMap;
    }

    private void filterGraphData(String lineName, Network network) {
        network.filterLinksByLine(lineName);
        repaint();
    }
    
    public void showMap(Network network) {
        setNetwork(network);
    }

    public void setNetwork(Network network) {
        this.network = network;
        repaint();
    }
}