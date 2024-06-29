package fr.u_paris.gla.project.Lecture_Reseau;

import java.util.ArrayList;
import java.util.List;

public class Line {
    //Nom de l'arrêt
    String LineName;
    //Les arrêts
    private List<Stop> LineStops;
    //Les liens entre les arrêts
    private List<Link> LineLinks;
    private String terminus_1;
    private String terminus_2;
    private String terminus_3;

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

    public List<Stop> getLineStops() {
        return this.LineStops;
    }

    public void setLineStops(List<Stop> LineStops) {
        this.LineStops = LineStops;
    }

    public List<Link> getLineLinks() {
        return this.LineLinks;
    }

    public void setLineLinks(List<Link> LineLinks) {
        this.LineLinks = LineLinks;
    }

    public void addLineStop(Stop stop) {
        LineStops.add(stop);
    }

    public void addLineLink(Link link) {
        LineLinks.add(link);
    }


    @Override
    public String toString() {
        return "{" +
            "LineStops='" + getLineStops() + "'" +
            "LineLinks='" + getLineLinks() + "'" +
            "}";
    }
}
