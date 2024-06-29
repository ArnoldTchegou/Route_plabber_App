package fr.u_paris.gla.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.u_paris.gla.project.Lecture_Reseau.Line;
import fr.u_paris.gla.project.Lecture_Reseau.Link;
import fr.u_paris.gla.project.Lecture_Reseau.Stop;

public class LineTest {
    private Line line;

    @BeforeEach
    void SetUp(){
        line = new Line("line");
    }

    @Test
    void testAddLineStop(){
        Stop stop = new Stop("testStop", 0,0);
        line.addLineStop(stop);
        List<Stop> stops = line.getLineStops();
        assertNotNull(stops);
        assertEquals(1, stops.size());
        assertEquals(stop, stops.get(0));
    }

    @Test
    void testAddLineLink() {
        Link link = new Link("TestLine", 10, 5.0, new Stop("Source", 0.0, 0.0), new Stop("Destination", 1.0, 1.0));
        line.addLineLink(link);
        List<Link> links = line.getLineLinks();
        assertNotNull(links);
        assertEquals(1, links.size());
        assertEquals(link, links.get(0));
    }

    
}
