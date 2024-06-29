package fr.User_interface;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;
import java.awt.geom.Point2D;



import fr.u_paris.gla.project.App;
import fr.Lecture_Reseau.Link;
import fr.Lecture_Reseau.Network;
import fr.Lecture_Reseau.Stop;
import fr.Lecture_Reseau.WalkPath;

public class UI_Terminale {
/**
* 
*/
private static final String UNSPECIFIED = "Unspecified";         //$NON-NLS-1$

 /* Affichage dans le terminale */
 /*public void showTerminale(Network network){
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
    //Chemin le plus rapide
    LocalTime departureTime = LocalTime.now();
    List<Link> shortestPath = network.dijkstra_time(s1, s2,departureTime);
     //Si walk= true alors on laisse l'option de marcher à pied sinon non
        boolean walk = askForWalking();
        WalkPath.evaluateWalkingOptions(shortestPath, departureTime,walk);

        //Affichage du temps de trajet
        System.out.println("\n\nLe chemin le plus rapide pour aller de " + s1.getStopName() + " à " + s2.getStopName() + " est le suivant:");
        double totalTime1 = 0;
        for (Link link : shortestPath) {
            System.out.println("\t[ " + link.getDestination().getStopName() + ", " + link.getTime() + "s, " + link.getDistance() + "m, ligne: " + link.getLineName() + " ]");
            totalTime1 += link.getTime();
        }
        //Transformation du temps de trajet en long
        long tot = (long) totalTime1;
        System.out.println("\n####Heure d'arrivée prévue: " + departureTime.plusSeconds(tot) +"###\n\n");
        sc.close();
    }

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
    }*/
    
    public void showTerminale(Network network){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Voulez-vous rentrer le nom de la gare ou les coordonnées ? Gare=1/Coordonnees=2");
                String response = scanner.nextLine().toLowerCase(); 
                if (response.equals("1")) {
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
                            //Chemin le plus rapide
                            LocalTime departureTime = LocalTime.now();
                            List<Link> shortestPath = network.dijkstra_time(s1, s2,departureTime);
                            //Si walk= true alors on laisse l'option de marcher à pied sinon non
                                boolean walk = askForWalking();
                                WalkPath.evaluateWalkingOptions(shortestPath, departureTime,walk);

                                //Affichage du temps de trajet
                                System.out.println("\n\nLe chemin le plus rapide pour aller de " + s1.getStopName() + " à " + s2.getStopName() + " est le suivant:");
                                double totalTime1 = 0;
                                for (Link link : shortestPath) {
                                    System.out.println("\t[ " + link.getDestination().getStopName() + ", " + link.getTime() + "s, " + link.getDistance() + "m, ligne: " + link.getLineName() + " ]");
                                    totalTime1 += link.getTime();
                                }
                                //Transformation du temps de trajet en long
                                long tot = (long) totalTime1;
                                System.out.println("\n####Heure d'arrivée prévue: " + departureTime.plusSeconds(tot) +"###\n\n");
                                sc.close();
                    
                }
                else{
                    Scanner sc = new Scanner(System.in).useLocale(Locale.US);
                    System.out.println("Quelle est la coordonnees de l'addresse de départ ? (Latitude,Longitude)");
                    double x1,y1,x2,y2;
                    System.out.print("Latitude : ");
                    x1=sc.nextDouble();
                    System.out.print("Longitude : ");
                    y1=sc.nextDouble();
                    System.out.println("Quelle est la coordonnees de l'addresse de d'arrivée ? (Latitude,Longitude)");
                    System.out.print("Latitude : ");
                    x2=sc.nextDouble();
                    System.out.print("Longitude : ");
                    y2=sc.nextDouble();
                    Point2D.Double Gare_Départ= new Point2D.Double(x1,y1);
                    Point2D.Double Gare_Arrivé= new Point2D.Double(x2,y2);
                    LocalTime departureTime1 = LocalTime.now();
                    network.Affich(Gare_Départ,Gare_Arrivé,departureTime1);

                }
            
    }

    

    public static boolean askForWalking() {
        Scanner scanner = new Scanner(System.in);
        boolean walk;
        while (true) {
            System.out.println("Voulez-vous faire la marche à pied ? (oui/non)");
            String response = scanner.nextLine().toLowerCase();
            if (response.equals("oui")) {
                walk = true;
                break; // Sort de la boucle si la réponse est valide
            } else if (response.equals("non")) {
                walk = false;
                break; // Sort de la boucle si la réponse est valide
            } else {
                System.out.println("Veuillez répondre par 'oui' ou 'non'.");
            }
        }
        scanner.close();
        return walk;
    }

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
