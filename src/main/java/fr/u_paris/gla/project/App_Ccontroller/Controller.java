package fr.u_paris.gla.project.App_Ccontroller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.u_paris.gla.project.Lecture_Reseau.Line;
import fr.u_paris.gla.project.Lecture_Reseau.Link;
import fr.u_paris.gla.project.Lecture_Reseau.Network;
import fr.u_paris.gla.project.Lecture_Reseau.Stop;
import fr.u_paris.gla.project.User_interface.UI_Terminale;
import fr.u_paris.gla.project.User_interface.UI_graphique;
import fr.u_paris.gla.project.User_interface.MetroMapUI;

public class Controller {
    private Network controller_network;
    private UI_Terminale controller_terminal;
    private UI_graphique controller_GUI;
    private MetroMapUI controller_Map;

    public Network getController_network() {
        return controller_network;
    }

    public void setController_network(Network controller_network) {
        this.controller_network = controller_network;
    }


    public UI_Terminale getController_terminal() {
        return controller_terminal;
    }

    public void setController_terminal(UI_Terminale controller_terminal) {
        this.controller_terminal = controller_terminal;
    }

    public UI_graphique getController_GUI() {
        return controller_GUI;
    }

    public void setController_GUI(UI_graphique controller_GUI) {
        this.controller_GUI = controller_GUI;
    }
    public MetroMapUI getController_Map() {
        return controller_Map;
    }

    public void setController_Map(MetroMapUI controller_Map) {
        this.controller_Map= controller_Map;
    }
//constructeur utilisé pour les tests
    public Controller(){
        this.controller_network = new Network();
        CreateFinal_NetworkFromCSV("test.csv", this.controller_network);
        updateTerminusInfo("horaire.csv",controller_network);
        updateLinkPassages("horaire.csv",controller_network);
    }

    public Controller(Network n, UI_Terminale t){
        this.controller_network = n;
        this.controller_terminal = t;
    }

    public Controller(Network n, UI_graphique g){
        this.controller_network = n;
        this.controller_GUI = g;
    }
    public Controller(Network n){
        this.controller_network = n;
        this.controller_Map = new MetroMapUI(n);
    }

    //Convertit le temps en secondes
    public static int convertToSec(int minutes, int seconds){
        return minutes * 60 + seconds;
    }

    //Renvoi la ligne si elle existe déjà ou le créer sinon
    public static Line getOrCreateLine(Network Final_Network, String LineName){
        for(Line l : Final_Network.getAll_line()){
            if (l.getLineName().equals(LineName)){
                return l;
            }
        }
        Line NewLine  = new Line(LineName);
        Final_Network.getAll_line().add(NewLine);
        return NewLine;
    }    

    //Renvoi l'arrêt si il existe déjà ou le créer sinon
    public static Stop getOrCreateStop(Network Final_Network, String name, double latitude, double longitude, Line Line){
        Stop newStop = new Stop(name, latitude, longitude);
        //On vérifie que l'arret ne soit pas dans la ligne
        for(Stop s : Line.getLineStops()){
            if(s.getStopName().equals(name)){
                return s;
            }
        }
        Line.addLineStop(newStop);
        //On verifie si l'arret existe déja dans le réseau
        boolean contains = false;
        for(Stop s : Final_Network.getStops()){
            if (s.getStopName().equals(name)){
                contains=true;
            }
        }
        if(contains == false){
            Final_Network.addStop(newStop);
            //On ajoute l'arrêt en tant que key pour la liste d'adjacence
            List<Link> ListLink = new ArrayList<>();
            Final_Network.getAdjacencyList().put(newStop, ListLink); 
        }
        return newStop;
    }

    public static void CreateFinal_NetworkFromCSV(String filename, Network Final_Network){
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))){
            while ((line = br.readLine()) != null){
                //On coupe la ligne pour extraire ce qui nous interrese
                String[] parts = line.split(";");
                if (parts.length == 8){
                    String lineName = parts[0].trim().replace("\"", "");
                    String sourceName = parts[2].trim().replace("\"", "");
                    String coordinatesSource = parts[3].trim().replace("\"", "");
                    String[] coordinatesSourceArray = coordinatesSource.split(",");
                    double sourceLatitude = Double.parseDouble(coordinatesSourceArray[0].trim());
                    double sourceLongitude = Double.parseDouble(coordinatesSourceArray[1].trim());
                    String destinationName = parts[4].trim().replace("\"", "");
                    String coordinatesDest = parts[5].trim().replace("\"", "");
                    String[] coordinatesDestArray = coordinatesDest.split(",");
                    double destinationLatitude = Double.parseDouble(coordinatesDestArray[0].trim());
                    double destinationLongitude = Double.parseDouble(coordinatesDestArray[1].trim());
                    String[] timeParts = parts[6].trim().split(":");
                    int hours = Integer.parseInt(timeParts[0].replace("\"", ""));
                    int minutes = Integer.parseInt(timeParts[1].replace("\"", ""));
                    int time = convertToSec(hours, minutes);
                    double distance = Double.parseDouble(parts[7].trim().replace("\"", ""));

                    //On vérifie si la ligne existe ou pas encore
                    Line net_line = getOrCreateLine(Final_Network, lineName);
                    //On vérifie si les arrêts existent ou pas encore
                    Stop source = getOrCreateStop(Final_Network, sourceName, sourceLatitude, sourceLongitude, net_line);
                    Stop destination = getOrCreateStop(Final_Network, destinationName, destinationLatitude, destinationLongitude, net_line);
                    
                    Link link = new Link(lineName, time, distance, source, destination);
                    net_line.addLineLink(link);
                    Final_Network.addLink(link);
                    //On ajoute le liens à la liste d'adjacence 
                    Final_Network.getAdjacencyList().get(source).add(link);
                }
            }
        } 
        catch (IOException e){
            e.printStackTrace();
        }
        //Final_Network.printAdjacencyList();
    }

    //Associe aux lignes leurs terminus
    public static void updateTerminusInfo(String filename, Network network) {
        Map<String, Set<String>> lineToTermini = new HashMap<>();
        //Extraction des infos
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 4) {
                    String lineName = parts[0].trim().replace("\"", "");
                    String terminus = parts[2].trim().replace("\"", "");
    
                    // Stocker les terminus pour chaque ligne
                    lineToTermini.computeIfAbsent(lineName, k -> new HashSet<>()).add(terminus);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Mettre à jour les informations des terminus pour chaque ligne
        for (Line line : network.getLines()) {
            if (lineToTermini.containsKey(line.getLineName())) {
                List<String> termini = new ArrayList<>(lineToTermini.get(line.getLineName()));
                if (termini.size() > 0) {
                    line.setTerminus1(termini.get(0));
                    if (termini.size() > 1) {
                        line.setTerminus2(termini.get(1));
                    }
                    // Gérer spécifiquement les lignes 7 et 13 pour le troisième terminus
                    if ((line.getLineName().equals("7") || line.getLineName().equals("13")) && termini.size() > 2) {
                        line.setTerminus3(termini.get(2));
                    }
                }
            }
        }
    }

    //Fonction très moche à cause des spécificités
    public static void updateLinkPassages(String filename, Network network) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 4) {
                    String lineName = parts[0].trim().replace("\"", "");
                    int bifur = Integer.parseInt(parts[1].trim().replace("\"", "").replace("[", "").replace("]", ""));
                    String stationStart = parts[2].trim().replace("\"", "");
                    LocalTime departureTime = LocalTime.parse(parts[3].trim().replace("\"", ""), formatter);
                    String stationArrive="";
                    String previousstation="";
                    // Trouver la ligne correspondante
                    for(Line l: network.getLines()){
                        if(l.getLineName().equals(lineName)){
                            // On ajoute les horaires a tous les liens de cette ligne tant qu'on arrive pas au terminus 
                            while(!stationArrive.equals(l.getTerminus1()) && !stationArrive.equals(l.getTerminus2()) && !stationArrive.equals(l.getTerminus3())){
                                // Trouver le lien correspondant dans le réseau et mettre à jour H_passage
                                for (Link link : network.getLinks()){
                                    //Traitement des cas spécifiques
                                    if(l.getLineName().equals("13") && bifur==1 && stationStart.equals("La Fourche")){
                                        if(!link.getDestinationName().equals("Guy Môquet")){
                                            continue;
                                        }
                                    }
                                    if(l.getLineName().equals("13") && bifur==2 && stationStart.equals("La Fourche")){
                                        if(!link.getDestinationName().equals("Brochant")){
                                            continue;
                                        }
                                    }
                                    if(l.getLineName().equals("13") && bifur==0 && stationStart.equals("La Fourche")){
                                        if(!link.getDestinationName().equals("Place de Clichy")){
                                            continue;
                                        }
                                    }

                                    if(l.getLineName().equals("7") && bifur==1 && stationStart.equals("Maison Blanche")){
                                        if(!link.getDestinationName().equals("Porte d'Italie")){
                                            continue;
                                        }
                                    }
                                    if(l.getLineName().equals("7") && bifur==2 && stationStart.equals("Maison Blanche")){
                                        if(!link.getDestinationName().equals("Le Kremlin-Bicêtre")){
                                            continue;
                                        }
                                    }
                                    if(l.getLineName().equals("7") && bifur==0 && stationStart.equals("Maison Blanche")){
                                        if(!link.getDestinationName().equals("Tolbiac")){
                                            continue;
                                        }
                                    }
                                    if(l.getLineName().equals("7B") && stationStart.equals("Botzaris") && previousstation.equals("Danube")){
                                        if(!link.getDestinationName().equals("Buttes Chaumont")){
                                            continue;
                                        }
                                    }
                                    if(l.getLineName().equals("10") && stationStart.equals("Boulogne Jean Jaurès") && previousstation.equals("Porte d'Auteuil")){
                                        if(!link.getDestinationName().equals("Boulogne Pont de Saint-Cloud")){
                                            continue;
                                        }
                                    }


                                    if(link.getLineName().equals(lineName) && link.getSourceName().equals(stationStart)&& !link.getDestinationName().equals(previousstation)){
                                        link.getHPassage().add(departureTime);
                                        stationArrive=link.getDestinationName();
                                        System.out.println("\nAvec la ligne " + l.getLineName()+ " De "+stationStart+ " à "+stationArrive +" aux horaires "+link.getHPassage());
                                        //On verfie qu'on ne repart pas dans le même sens
                                        previousstation=stationStart;
                                        //On met à jour la station de départ
                                        stationStart=stationArrive;
                                        //On met à jour l'heure de départ de la nouvelle station
                                        departureTime=departureTime.plusSeconds(link.getTime());
                                        break;
                                    }
                                }  
                            }
                            break;      
                        }
                    }      
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }  

    public void LaunchApp_GUI(){
        CreateFinal_NetworkFromCSV("test.csv", this.controller_network);
        updateTerminusInfo("horaire.csv",controller_network);
        updateLinkPassages("horaire.csv",controller_network);
        this.controller_GUI.createInputPanel(this.controller_network);
    }

    public void LaunchApp_Terminale(){
        CreateFinal_NetworkFromCSV("test.csv", this.controller_network);
        updateTerminusInfo("horaire.csv",controller_network);
        updateLinkPassages("horaire.csv",controller_network);
        this.controller_terminal.showTerminale(this.controller_network);
    }
    public void LaunchApp_Map(){
        CreateFinal_NetworkFromCSV("test.csv", this.controller_network);
        updateTerminusInfo("horaire.csv",controller_network);
        updateLinkPassages("horaire.csv",controller_network);
        this.controller_Map.showMap(this.controller_network);
    }

    public void App_Info(){
        this.controller_terminal.printAppInfos(System.out);
    }
}