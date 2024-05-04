package fr.u_paris.gla.project.Lecture_Reseau;

import java.text.Normalizer;
import java.util.Objects;

public class Stop {
    private String stopName;
    private double longitude;
    private double latitude;

    public Stop() {
    }

    public Stop(String stopName,  double latitude, double longitude) {
        this.stopName = stopName;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getStopName() {
        return this.stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stop stop = (Stop) o;
        return Objects.equals(stopName, stop.stopName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stopName);
    }


    @Override
    public String toString() {
        return "\n\t{" +
            "stopName= " + getStopName()+ "\n\t" +
            //"latitude= " + getLatitude() + "\n\t" +
            //"longitude= " + getLongitude() + "\n\t" +
            "}\n";
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

}
