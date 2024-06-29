package fr.u_paris.gla.project;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.Lecture_Reseau.Link;
import fr.Lecture_Reseau.Network;
import fr.Lecture_Reseau.Stop;
import fr.Lecture_Reseau.WalkPath;
import fr.u_paris.gla.project.idfm.IDFMNetworkExtractor;
import fr.App_Ccontroller.Controller;
import fr.Extract_Time.ScheduleGenerator;

/** Unit test for simple App. */
class AppTest {
    /** Rigorous Test :-) */
    @Test
    void testPlaceholder() {
        //Test horaire
        String[] path1 = {"test.csv","horaire.csv"};
        ScheduleGenerator.main(path1);
        //Test djikstra
        String[] path = {"test.csv"};
        IDFMNetworkExtractor.extract(path);
        Network network = new Network();
        Controller.CreateFinal_NetworkFromCSV("test.csv",network);
        Stop s1=null,s2=null;
        for(Stop s : network.getStops()){
            if(s.getStopName().equals("Billancourt")){
                s1=s;
            }
            if(s.getStopName().equals("La Muette")){
                s2=s;
            }
        }
        LocalTime departureTime = LocalTime.now();
        List<Link> shortestPath=network.dijkstra_time(s1, s2,departureTime);
        double totalTime1 = 0;
        for (Link link : shortestPath) {
            totalTime1 += link.getTime();
        }

        assertNotNull(shortestPath, "Le chemin le plus court ne devrait pas être null");
        assertFalse(shortestPath.isEmpty(), "Le chemin le plus court ne devrait pas être vide");

        boolean walk = false;
        WalkPath.evaluateWalkingOptions(shortestPath, departureTime,walk);
        double totalTime2 = 0;
        for (Link link : shortestPath) {
            totalTime2 += link.getTime();
        }

        assertTrue(totalTime1<=totalTime2, "Le chemin qui prends en compte les horaires est plus long ou égal que celui qui ne les prends pas");

        //Fin TEST
        assertTrue(true, "It should be true that true is true...");
        
    }
}
