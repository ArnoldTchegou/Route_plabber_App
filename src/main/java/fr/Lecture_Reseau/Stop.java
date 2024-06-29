package fr.Lecture_Reseau;
import java.text.Normalizer;
import java.util.*;

/**
 * Représente un arrêt dans un réseau de transport, avec des informations de nom, longitude et latitude.
 * @author SamySWEID*/

public class Stop {
    private String stopName;
    private double longitude;
    private double latitude;

    /**
     * Constructeur par défaut pour un arrêt. Initialise un arrêt sans nom ni coordonnées.
     */
    public Stop() {
    }

    /**
     * Constructeur pour un arrêt avec un nom, une latitude et une longitude spécifiés.
     *
     * @param stopName Le nom de l'arrêt.
     * @param latitude La latitude de l'arrêt.
     * @param longitude La longitude de l'arrêt.
     */
    public Stop(String stopName, double latitude, double longitude) {
        this.stopName = stopName;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Retourne le nom de l'arrêt.
     *
     * @return Le nom de l'arrêt.
     */
    public String getStopName() {
        return this.stopName;
    }

    /**
     * Définit le nom de l'arrêt.
     *
     * @param stopName Le nouveau nom de l'arrêt.
     */
    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    /**
     * Retourne la longitude de l'arrêt.
     *
     * @return La longitude de l'arrêt.
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * Définit la longitude de l'arrêt.
     *
     * @param longitude La nouvelle longitude de l'arrêt.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Retourne la latitude de l'arrêt.
     *
     * @return La latitude de l'arrêt.
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * Définit la latitude de l'arrêt.
     *
     * @param latitude La nouvelle latitude de l'arrêt.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Compare cet arrêt avec un autre objet pour l'égalité, basée sur le nom de l'arrêt.
     *
     * @param o L'objet à comparer.
     * @return true si les deux arrêts ont le même nom, false sinon.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stop stop = (Stop) o;
        return Objects.equals(stopName, stop.stopName);
    }

    public boolean matchesName(String name) {
        String normalizedStopName = normalizeString(stopName);
        String normalizedName = normalizeString(name);
        return normalizedStopName.equalsIgnoreCase(normalizedName);
    }

    private String normalizeString(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD)
                           .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                           .toLowerCase();
    }

    /**
     * Génère un hash code pour cet arrêt, basé principalement sur son nom.
     *
     * @return Le hash code de cet arrêt.
     */
    @Override
    public int hashCode() {
        return Objects.hash(stopName);
    }

    /**
     * Retourne une représentation en chaîne de caractères de cet arrêt, incluant son nom.
     *
     * @return Une représentation en chaîne de caractères de cet arrêt.
     */
    @Override
    /** Retourne une représentation en */
    public String toString() {
        return "\n\t{" +
            "stopName= " + getStopName() + "\n\t" +
            //"latitude= " + getLatitude() + "\n\t" +
            //"longitude= " + getLongitude() + "\n\t" +
            "}\n";
    }
}
