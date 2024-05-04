package fr.u_paris.gla.project.App_Ccontroller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.u_paris.gla.project.Lecture_Reseau.Line;
import fr.u_paris.gla.project.Lecture_Reseau.Link;
import fr.u_paris.gla.project.Lecture_Reseau.Network;
import fr.u_paris.gla.project.Lecture_Reseau.Stop;
import fr.u_paris.gla.project.User_interface.UI_Terminale;
import fr.u_paris.gla.project.User_interface.UI_graphique;

public class Controller {
    private Network controller_network;
    private UI_Terminale controller_terminal;
    private UI_graphique controller_GUI;

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

    public Controller(Network n){
        this.controller_network = n;
    }

    public Controller(Network n, UI_Terminale t){
        this.controller_network = n;
        this.controller_terminal = t;
    }

    public Controller(Network n, UI_graphique g){
        this.controller_network = n;
        this.controller_GUI = g;
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
    public void LaunchApp_GUI(){
        CreateFinal_NetworkFromCSV("test.csv", this.controller_network);
        this.controller_GUI.createInputPanel(this.controller_network);
    }

    public void LaunchApp_Terminale(){
        CreateFinal_NetworkFromCSV("test.csv", this.controller_network);
        this.controller_terminal.showTerminale(this.controller_network);
    }

    public void App_Info(){
        this.controller_terminal.printAppInfos(System.out);
    }
}