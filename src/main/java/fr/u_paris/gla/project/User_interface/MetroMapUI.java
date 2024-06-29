package fr.u_paris.gla.project.User_interface;
import javax.swing.*;

import fr.u_paris.gla.project.Lecture_Reseau.Link;
import fr.u_paris.gla.project.Lecture_Reseau.Network;
import fr.u_paris.gla.project.Lecture_Reseau.Stop;
import fr.u_paris.gla.project.Lecture_Reseau.WalkPath;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.util.Map;
import java.util.List;

public class MetroMapUI extends JFrame {
    private JComboBox<String> departureField;
    private JComboBox<String> arrivalField;
    private JButton refreshButton;
    private double zoomFactor = 4.9;
    MetroMapPanel mapPanel;
    private JTextArea textArea;
    private JTextArea pathResult;
    protected boolean walk;

    public MetroMapUI(Network network) {
        super("Metro Map UI");
        refreshButton = new JButton("Refresh");
        setLayout(new GridLayout(1, 2));
        
        JPanel graphe = new JPanel(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        mapPanel = new MetroMapPanel(network, zoomFactor);
        JScrollPane scrollPane = new JScrollPane(mapPanel);
        graphe.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton zoomInButton = new JButton("Zoom In");
        JButton zoomOutButton = new JButton("Zoom Out");
        JButton showall = new JButton("Show Network");
        buttonPanel.add(zoomInButton);
        buttonPanel.add(zoomOutButton);
        buttonPanel.add(showall);
        graphe.add(buttonPanel, BorderLayout.SOUTH);

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
        JPanel legendPanel = new JPanel(new GridLayout(0, 2));
        Map<String, Color> lineColorMap = Factory.createLineColorMap();

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
        graphe.add(legendPanel, BorderLayout.EAST);

        add(graphe);
        setVisible(true);
    }
    private void zoomIn() {
        mapPanel.zoomFactor += 0.1;
    }

    private void zoomOut() {
        if (mapPanel.zoomFactor > 0.1) {
            mapPanel.zoomFactor -= 0.1;
        }
    }

    private void filterGraphData(String lineName, Network network) {
        network.filterLinksByLine(lineName);
        repaint();
    }
    
    public void showMap(Network network) {
        createInputPanel(network);
        mapPanel.setNetwork(network);
    }

    public void createInputPanel(Network network) {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        inputPanel.setBackground(Color.lightGray);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        // ComboBox pour le départ
        departureField = new JComboBox<>(new DefaultComboBoxModel<>(network.getAllStationNames().toArray(new String[0])));
        departureField.setEditable(true);
    
        // ComboBox pour l'arrivée
        arrivalField = new JComboBox<>(new DefaultComboBoxModel<>(network.getAllStationNames().toArray(new String[0])));
        arrivalField.setEditable(true);
        JLabel departureLabel = new JLabel("Departure:");
        JLabel arrivalLabel = new JLabel("Arrival:");
        JButton findPathButton = new JButton("Find Path");
        findPathButton.setBackground(Color.blue);
        findPathButton.setForeground(Color.white);
        refreshButton.setBackground(Color.green);
        refreshButton.setForeground(Color.white);
        // Ajout de la JTextArea non éditable à la fin du inputPanel
        textArea = new JTextArea(1, 4);
        textArea.setFocusable(false);
        pathResult = new JTextArea(2, 4);
        pathResult.setFocusable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JScrollPane scrollPaneResult = new JScrollPane(pathResult);

        inputPanel.add(departureLabel, gbc);
        inputPanel.add(departureField, gbc);
        inputPanel.add(arrivalLabel, gbc);
        inputPanel.add(arrivalField, gbc);
        inputPanel.add(findPathButton, gbc);
        inputPanel.add(refreshButton, gbc);
        inputPanel.add(scrollPane, gbc);
        inputPanel.add(scrollPaneResult, gbc);
    

        // Ajout de la JCheckBox à côté du bouton "Find Path"
        JCheckBox checkBox = new JCheckBox("Walk");
        checkBox.setSelected(true); // Par défaut, la case est cochée
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                walk = checkBox.isSelected();
            }
        });
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        inputPanel.add(checkBox, gbc);
        gbc.gridwidth = 1; // Réinitialisation de la largeur de la cellule
    
        // Ajout du bouton "Find Path"
        gbc.weightx = 0.5;
        inputPanel.add(findPathButton, gbc);
        gbc.weightx = 0.0; // Réinitialisation du poids
        
        findPathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                computepath(network);
            }
        });
    
        actionRefreshButton(network);
        add(inputPanel);
    }
    public void computepath(Network network) {
        String departure = (String) departureField.getSelectedItem();
        String arrival = (String) arrivalField.getSelectedItem();

        Stop s1 = network.findStopByName(departure);
        Stop s2 = network.findStopByName(arrival);

        if (s1 == null || s2 == null) {
            return;
        }

        LocalTime departureTime = LocalTime.now();
        List<Link> shortestPath = network.dijkstra_time(s1, s2, departureTime);
        //Si walk= true alors on laisse l'option de marcher à pied sinon non
        boolean walk = true;
        WalkPath.evaluateWalkingOptions(shortestPath, departureTime,walk);
        double totalTime1 = 0;
        for (Link link : shortestPath) {
            totalTime1 += link.getTime();
        }
        //Transformation du temps de trajet en long
        long tot = (long) totalTime1;
        String arrivalText = "Heure d'arrivée: " + departureTime.plusSeconds(tot).toString();
        textArea.setText(arrivalText);
        // Créer une chaîne pour stocker le résultat
        StringBuilder resultBuilder = new StringBuilder("pathResult: ");

        // Parcourir les liens et ajouter les noms des arrêts de destination à la chaîne de résultat avec des flèches entre eux
        for (Link link : shortestPath) {
            resultBuilder.append(link.getDestination().getStopName())
                        .append(" -> ");
        }

        // Supprimer la dernière flèche inutile
        resultBuilder.delete(resultBuilder.length() - 4, resultBuilder.length());

        // Ajouter la chaîne de résultat à votre JTextArea
        pathResult.setText(resultBuilder.toString());
        network.restoreOriginalLinks();
        network.saveOriginalLinks();
        network.setLinks(shortestPath);
        repaint();
    }
    private void actionRefreshButton(Network network) {
        this.refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                departureField.setSelectedIndex(0);
                arrivalField.setSelectedIndex(0);
            }
        });
    }
}