package fr.Lecture_Reseau;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une ligne de transport contenant des arrêts et des connexions (liens) entre ces arrêts.
 * Cette classe gère les informations concernant les arrêts et les liens d'une ligne spécifique.
 * @author SamySWEID*/

public class Line {
    // Nom de la ligne
    private String LineName;
    // Liste des arrêts de la ligne
    private List<Stop> LineStops;
    // Liste des liens entre les arrêts
    private List<Link> LineLinks;
    private String terminus_1;
    private String terminus_2;
    private String terminus_3;

    /**
     * Construit une nouvelle instance de Line avec un nom spécifié.
     *
     * @param LineName Le nom de la ligne de transport.
     */
    public Line(String LineName) {
        this.LineName = LineName;
        this.LineStops = new ArrayList<>();
        this.LineLinks = new ArrayList<>();
    }

    public String getTerminus1(){
        return this.terminus_1;
    }

    public String getTerminus2(){
        return this.terminus_2;
    }

    public String getTerminus3(){
        return this.terminus_3;
    }

    public void setTerminus1(String s){
        this.terminus_1=s;
    }

    public void setTerminus2(String s){
        this.terminus_2=s;
    }

    public void setTerminus3(String s){
        this.terminus_3=s;
    }

    public String getLineName() {
        return this.LineName;
    }

    /**
     * Renvoie la liste des arrêts de cette ligne.
     *
     * @return Une liste contenant les arrêts de la ligne.
     */
    public List<Stop> getLineStops() {
        return this.LineStops;
    }

    /**
     * Définit les arrêts de la ligne.
     *
     * @param LineStops Une liste d'arrêts à affecter à la ligne.
     */
    public void setLineStops(List<Stop> LineStops) {
        this.LineStops = LineStops;
    }

    /**
     * Renvoie la liste des liens entre les arrêts de la ligne.
     *
     * @return Une liste contenant les liens entre les arrêts de la ligne.
     */
    public List<Link> getLineLinks() {
        return this.LineLinks;
    }

    /**
     * Définit les liens entre les arrêts de la ligne.
     *
     * @param LineLinks Une liste de liens à affecter à la ligne.
     */
    public void setLineLinks(List<Link> LineLinks) {
        this.LineLinks = LineLinks;
    }

    /**
     * Ajoute un arrêt à la liste des arrêts de la ligne.
     *
     * @param stop L'arrêt à ajouter à la ligne.
     */
    public void addLineStop(Stop stop) {
        LineStops.add(stop);
    }

    /**
     * Ajoute un lien à la liste des liens de la ligne.
     *
     * @param link Le lien à ajouter entre les arrêts de la ligne.
     */
    public void addLineLink(Link link) {
        LineLinks.add(link);
    }

    /**
     * Renvoie une représentation en chaîne de caractères de la ligne, incluant les arrêts et les liens.
     *
     * @return Une chaîne de caractères représentant la ligne avec ses arrêts et ses liens.
     */
    @Override
    public String toString() {
        return "{" +
            "LineStops='" + getLineStops() + "'" +
            "LineLinks='" + getLineLinks() + "'" +
            "}";
    }
}
