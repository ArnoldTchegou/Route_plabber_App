package fr.u_paris.gla.project.User_interface;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;


import fr.u_paris.gla.project.App;
import fr.u_paris.gla.project.Lecture_Reseau.Link;
import fr.u_paris.gla.project.Lecture_Reseau.Network;
import fr.u_paris.gla.project.Lecture_Reseau.Stop;

public class UI_Terminale {
/**
* 
*/
private static final String UNSPECIFIED = "Unspecified";         //$NON-NLS-1$

 /* Affichage dans le terminale */
 public void showTerminale(Network network){
    Scanner sc = new Scanner(System.in);
    System.out.println("Quelle est la gare de départ ?");
    String str1 = sc.nextLine();
    System.out.println("Quelle est la gare de d'arrivée ?");
    String str2 = sc.nextLine();
    Stop s1 = network.findStopByName(str1);
    Stop s2 = network.findStopByName(str2);
    if (s1 == null || s2 == null) {
        System.out.println("L'une des gares spécifiées n'existe pas dans le réseau.");
        sc.close();
        return; // Exit the program or handle the invalid input accordingly
    }

    //Chemin le plus court
    List<Link> shortestPath=network.dijkstra_dist(s1, s2);
    System.out.println("Le chemin le plus court pour aller de "+ s1.getStopName() + " à " + s2.getStopName() + " est le suivant:");
    for (Link link : shortestPath) {
        System.out.println("\t(" + link.getDestination().getStopName() + ", " + link.getTime() + "s, " + link.getDistance() + "m, opéré par la ligne: " + link.getLineName()+" )");
    }
    
    //Chemin le plus rapide
    List<Link> shortestPath2 = network.dijkstra_time(s1, s2, 8 * 3600);
    System.out.println("Le chemin le plus rapide pour aller de " + s1.getStopName() + " à " + s2.getStopName() + " est le suivant :");
    double totalTime = 0;
    for (Link link : shortestPath2) {
        System.out.println("\t(" + link.getDestination().getStopName() + ", " + link.getTime() + "s, " + link.getDistance() + "m, opéré par la ligne: " + link.getLineName() + " )");
        totalTime += link.getTime();
    }
    System.out.println("Le temps de trajet total est de " + totalTime + " secondes.");
    sc.close();
    }
    /** @param out */
    public void printAppInfos(PrintStream out) {
        Properties props = new Properties();
        try (InputStream is = App.class.getResourceAsStream("application.properties")) { //$NON-NLS-1$
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read application informations", e); //$NON-NLS-1$
        }

        out.println("Application: " + props.getProperty("app.name", UNSPECIFIED)); //$NON-NLS-1$ //$NON-NLS-2$
        out.println("Version: " + props.getProperty("app.version", UNSPECIFIED)); //$NON-NLS-1$ //$NON-NLS-2$
        out.println("By: " + props.getProperty("app.team", UNSPECIFIED)); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
