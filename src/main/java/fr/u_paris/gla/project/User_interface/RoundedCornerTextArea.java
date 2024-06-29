package fr.u_paris.gla.project.User_interface;
import javax.swing.*;
import java.awt.*;

public class RoundedCornerTextArea extends JScrollPane {
    private JTextArea textArea;
    private Color backgroundColor = new Color(240, 240, 240); // Couleur de fond gris clair
    private int cornerRadius = 45; // Rayon pour les coins arrondis

    public RoundedCornerTextArea() {
        textArea = new JTextArea();
        textArea.setOpaque(false); // Rendre la zone de texte transparente
        textArea.setLineWrap(true); // Activer le retour à la ligne automatique
        textArea.setWrapStyleWord(true); // Activer la césure de mots
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Ajouter une marge intérieure
        textArea.setEditable(false);

        // Définir la couleur de bordure du JTextArea
        textArea.setBorder(BorderFactory.createLineBorder(Color.GREEN)); // Définir la bordure en bleu

        // Définir la vue du JScrollPane comme la JTextArea
        setViewportView(textArea);

        // Définir la couleur de fond du JScrollPane
        setBackground(new Color(240, 240, 240)); // Définir la couleur de fond du JScrollPane

        // Définir la taille préférée de la RoundedCornerTextArea
        setPreferredSize(new Dimension(200, 200)); // Vous pouvez ajuster la taille selon vos besoins
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int x = 10; 
        int y = 20;

        // Dessiner un arrière-plan personnalisé avec des coins arrondis
        g2.setColor(backgroundColor);
        g2.fillRoundRect(x, y, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        // Dessiner la bordure avec la couleur par défaut du composant
        g2.setColor(getForeground());
        g2.drawRoundRect(x, y, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        super.paintComponent(g2);

        g2.dispose();
    }

    // Méthode getter pour la zone de texte
    public JTextArea getTextArea() {
        return textArea;
    }

    // Pour tester la classe RoundedCornerTextArea
    public static void main(String[] args) {
        JFrame frame = new JFrame("RoundedCornerTextArea Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        RoundedCornerTextArea roundedTextArea = new RoundedCornerTextArea();
        frame.getContentPane().add(roundedTextArea);

        frame.setSize(300, 300);
        frame.setVisible(true);
    }
}