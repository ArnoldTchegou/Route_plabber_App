package fr.u_paris.gla.project.User_interface;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;

import fr.u_paris.gla.project.Lecture_Reseau.Link;
import fr.u_paris.gla.project.Lecture_Reseau.Network;
import fr.u_paris.gla.project.Lecture_Reseau.Stop;

public class UI_graphique {
    private JFrame frame;
    private JPanel mainPanel;
    private RoundedCornerTextArea r;
    private JComboBox<String> departureField;
    private JComboBox<String> arrivalField;
    private JButton refreshButton;

    public UI_graphique() {
        frame = new JFrame("Transport Network");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        mainPanel = new JPanel(new GridLayout(1, 2));
        frame.add(mainPanel, BorderLayout.CENTER);
        refreshButton = new JButton("Refresh");



        createResultArea();

        // Load the image and create a JLabel with the ImageIcon
        //ImageIcon imageIcon = new ImageIcon(getImage("src/main/ressources/images/universit_paris_cit_logo.jpg"));
        //JLabel imageLabel = new JLabel(imageIcon);
        //mainPanel.add(imageLabel); // Add the image label to the main panel

        frame.setVisible(true);
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

        JLabel departureLabel = new JLabel("Departure:");
        JLabel arrivalLabel = new JLabel("Arrival:");
        
        // ComboBox pour le départ
        departureField = new JComboBox<>(new DefaultComboBoxModel<>(network.getAllStationNames().toArray(new String[0])));
        departureField.setEditable(true);

        // ComboBox pour l'arrivée
        arrivalField = new JComboBox<>(new DefaultComboBoxModel<>(network.getAllStationNames().toArray(new String[0])));
        arrivalField.setEditable(true);


        JButton findPathButton = new JButton("Find Path");
        findPathButton.setBackground(Color.blue);
        findPathButton.setForeground(Color.white);
        refreshButton.setBackground(Color.green);
        refreshButton.setForeground(Color.white);

        inputPanel.add(departureLabel, gbc);
        inputPanel.add(departureField, gbc);
        inputPanel.add(arrivalLabel, gbc);
        inputPanel.add(arrivalField, gbc);
        inputPanel.add(findPathButton, gbc);
        inputPanel.add(refreshButton, gbc);
        

        findPathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                computepath(network);
            }
        });

        actionRefreshButton(network);

        mainPanel.add(inputPanel);
    }

    private void createResultArea() {
        r = new RoundedCornerTextArea();

        // Adjust the font size and style
        r.getTextArea().setFont(new Font("Arial", Font.BOLD, 12)); 

        // Adjust the text color
        r.getTextArea().setForeground(Color.BLUE); 

        // Create a JScrollPane with your RoundedCornerTextArea
        JScrollPane scrollPane = new JScrollPane(r.getTextArea());

        // Add the JScrollPane to your main panel
        mainPanel.add(scrollPane);
    }

    public void computepath(Network network) {
        String departure = (String) departureField.getSelectedItem();
        String arrival = (String) arrivalField.getSelectedItem();

        Stop s1 = network.findStopByName(departure);
        Stop s2 = network.findStopByName(arrival);

        if (s1 == null || s2 == null) {
            r.getTextArea().setText("One of the specified stops does not exist in the network.");
            return;
        }

        List<Link> shortestPath = network.dijkstra_dist(s1, s2);
        StringBuilder sb = new StringBuilder();
        sb.append("The shortest path from ").append(s1.getStopName()).append(" to ").append(s2.getStopName()).append(" is:\n");
        // Append shortest path details...
        appendPathDetails(sb, shortestPath);

        List<Link> fastestPath = network.dijkstra_time(s1, s2, 8 * 3600);
        sb.append("\nThe fastest path from ").append(s1.getStopName()).append(" to ").append(s2.getStopName()).append(" is:\n");
        // Append fastest path details...
        appendPathDetails(sb, fastestPath);

        r.getTextArea().setText(sb.toString());
    }

    private void appendPathDetails(StringBuilder sb, List<Link> path) {
        double totalTime = 0;
        for (Link link : path) {
            sb.append("\t(").append(link.getDestination().getStopName()).append(", ").append(link.getTime()).append("s, ")
                    .append(link.getDistance()).append("m, operated by line: ").append(link.getLineName()).append(")\n");
            totalTime += link.getTime();
        }
        sb.append("Total travel time is ").append(totalTime).append(" seconds.\n");
    }

    private void actionRefreshButton(Network network) {
        this.refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                departureField.setSelectedIndex(0);
                arrivalField.setSelectedIndex(0);
                r.getTextArea().setText("");
            }
        });
    }

    // Function to load an image from the resources folder
    public static Image getImage(final String pathAndFileName) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(pathAndFileName);
        if (url != null) {
            return Toolkit.getDefaultToolkit().getImage(url);
        } else {
            System.err.println("Image not found: " + pathAndFileName);
            return null;
        }
    }
}