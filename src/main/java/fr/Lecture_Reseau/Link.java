package fr.Lecture_Reseau;
import java.time.LocalTime;
import java.util.*;

/**
 * Représente une connexion entre deux arrêts dans un réseau de transport. 
 * Cette connexion comprend le nom de la ligne, le temps de parcours, la distance, 
 * et les arrêts source et destination.
 */
public class Link {
    private String lineName;
    private int time; // Temps en secondes
    private double distance; // Distance en kilomètres
    private Stop source;
    private Stop destination;
    private List<LocalTime> H_passage;

    /**
     * Constructeur par défaut pour créer un lien vide.
     * @author SamySWEID*/
    public Link() {
    }

    /**
     * Construit un lien avec des informations spécifiques.
     *
     * @param s Le nom de la ligne.
     * @param time Le temps de parcours entre les deux arrêts en secondes.
     * @param distance La distance entre les deux arrêts en kilomètres.
     * @param source L'arrêt de départ de ce lien.
     * @param destination L'arrêt d'arrivée de ce lien.
     */
    public Link(String s, int time, double distance, Stop source, Stop destination) {
        this.lineName = s;
        this.time = time;
        this.distance = distance;
        this.source = source;
        this.destination = destination;
        this.H_passage = new ArrayList<>();
    }

    /**
     * Renvoie le nom de la ligne associée à ce lien.
     *
     * @return Le nom de la ligne.
     */
    public String getLineName() {
        return this.lineName;
    }

    /**
     * Définit le nom de la ligne pour ce lien.
     *
     * @param s Le nouveau nom de la ligne.
     */
    public void setLineName(String s) {
        this.lineName = s;
    }

    /**
     * Renvoie le temps de parcours en secondes.
     *
     * @return Le temps de parcours.
     */
    public int getTime() {
        return this.time;
    }

    /**
     * Définit le temps de parcours pour ce lien.
     *
     * @param time Le nouveau temps de parcours en secondes.
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * Renvoie la distance en kilomètres.
     *
     * @return La distance.
     */
    public double getDistance() {
        return this.distance;
    }

    /**
     * Définit la distance pour ce lien.
     *
     * @param distance La nouvelle distance en kilomètres.
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * Renvoie le nom de l'arrêt source.
     *
     * @return Le nom de l'arrêt source.
     */
    public String getSourceName() {
        return this.source.getStopName();
    }

    /**
     * Renvoie l'arrêt source de ce lien.
     *
     * @return L'arrêt source.
     */
    public Stop getSource() {
        return this.source;
    }

    /**
     * Définit l'arrêt source pour ce lien.
     *
     * @param source Le nouvel arrêt source.
     */
    public void setSource(Stop source) {
        this.source = source;
    }

    /**
     * Renvoie le nom de l'arrêt destination.
     *
     * @return Le nom de l'arrêt destination.
     */
    public String getDestinationName() {
        return this.destination.getStopName();
    }

    /**
     * Renvoie l'arrêt destination de ce lien.
     *
     * @return L'arrêt destination.
     */
    public Stop getDestination() {
        return this.destination;
    }

    /**
     * Définit l'arrêt destination pour ce lien.
     *
     * @param destination Le nouvel arrêt destination.
     */
    public void setDestination(Stop destination) {
        this.destination = destination;
    }

    public List<LocalTime> getHPassage() {
        return this.H_passage;
    }

   
    @Override
    public String toString() {
        String formattedDistance = String.format("%.3f", getDistance());
        return "\n\nLink: {\n" +
            "Line= " + getLineName() + "\n" +
            "Time= " + getTime() + " (sec)\n" +
            "Distance =" + formattedDistance + "km\n" +
            "\tFROM= " + getSource() + "\n" +
            "\tTO= " + getDestination() + "" +
            "}\n";
    }
}
