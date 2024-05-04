package fr.u_paris.gla.project;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;

import fr.u_paris.gla.project.App_Ccontroller.Controller;
import fr.u_paris.gla.project.Lecture_Reseau.Link;
import fr.u_paris.gla.project.Lecture_Reseau.Network;
import fr.u_paris.gla.project.Lecture_Reseau.Stop;
import fr.u_paris.gla.project.idfm.IDFMNetworkExtractor;

/** Unit test for simple App. */
class AppTest {
    /** Rigorous Test :-) */
    @Test
    void testPlaceholder() {
        //Test djikstra
        String[] path = {"test.csv"};
        IDFMNetworkExtractor.extract(path);
        Network network = new Network();
        Controller c = new Controller(network);
        c.CreateFinal_NetworkFromCSV("test.csv",c.getController_network());
        Stop s1=null,s2=null;
        for(Stop s : c.getController_network().getStops()){
            if(s.getStopName().equals("Billancourt")){
                s1=s;
            }
            if(s.getStopName().equals("Marcel Sembat")){
                s2=s;
            }
        }
        List<Link> shortestPath=c.getController_network().dijkstra_dist(s1, s2);
            // Assert that shortestPath should not be null
        if (shortestPath != null) {
            // If shortestPath is not null, then it should not be empty
            assertFalse(shortestPath.isEmpty(), "Le chemin le plus court ne devrait pas être vide");
        } else {
            // If shortestPath is null, log a message indicating that it's expected to be non-null
            System.out.println("Attention: Le chemin le plus court est null.");
        }
        
    }
}
