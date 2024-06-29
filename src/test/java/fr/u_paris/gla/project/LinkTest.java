package fr.u_paris.gla.project;
import fr.u_paris.gla.project.Lecture_Reseau.Link;
import fr.u_paris.gla.project.Lecture_Reseau.Stop;

public class LinkTest {
    public static void main(String[] args) {
        // Création de quelques arrêts pour les tests
        Stop source = new Stop("Source", 0, 0);
        Stop destination = new Stop("Destination", 0, 0);

        // Création d'un objet Link pour les tests
        Link link = new Link("Ligne A", 60, 10.5, source, destination);

        // Affichage des détails du lien
        System.out.println("Détails du lien :");
        System.out.println(link);

        // Modification de la ligne du lien
        link.setLineName("Ligne B");

        // Affichage des détails mis à jour du lien
        System.out.println("Détails du lien après modification :");
        System.out.println(link);
    }
}
