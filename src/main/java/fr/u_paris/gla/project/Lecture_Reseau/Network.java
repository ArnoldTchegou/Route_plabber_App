package fr.u_paris.gla.project.Lecture_Reseau;

import java.util.*;

public class Network {
    //Liste contenant toutes les lignes du réseau
    private List<Line> All_Lines;
    //Les arrêts (stations)
    private List<Stop> stops;
    //Les liens entre les arrêts (stations)
    private List<Link> links;
    //Liste d'adjacence
    private Map<Stop, List<Link>> adjacencyList;
    private List<Link> originalLinks;


    public Network() {
        this.All_Lines = new ArrayList<>();
        this.stops = new ArrayList<>();
        this.links = new ArrayList<>();
        this.originalLinks = new ArrayList<>();
        this.adjacencyList = new HashMap<>();
    }

    public List<Line> getLines() {
        return All_Lines;
    }

    public List<Link> getLinks() {
        return links;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void addStop(Stop stop) {
        stops.add(stop);
    }

    public void addLink(Link link) {
        links.add(link);
    }
    public Stop findStopByName(String name) {
        for (Stop s : this.getStops()) {
            if (s.matchesName(name)) {
                return s;
            }
        }
        return null; // Retourne null si aucun arrêt correspondant n'est trouvé
    }

    public List<Line> getAll_line() {
        return All_Lines;
    }

    public Map<Stop, List<Link>> getAdjacencyList() {
        return adjacencyList;
    }

    public void printAdjacencyList() {
        for (Stop stop : adjacencyList.keySet()) {
            System.out.print(stop.getStopName() + ":\n");
            List<Link> links = adjacencyList.get(stop);
            for (Link link : links) {
                System.out.println("\t(" + link.getDestination().getStopName() + ", " + link.getTime() + "s, " + link.getDistance() + "m, opéré par la ligne: " + link.getLineName()+" )");
            }
            System.out.println();
        }
    }

    // Méthode pour récupérer les noms de toutes les stations
    public List<String> getAllStationNames() {
        List<String> stationNames = new ArrayList<>();
        // Boucle sur vos stations pour récupérer les noms
        for (Stop stop : stops) {
            stationNames.add(stop.getStopName());
        }
        Collections.sort(stationNames);
        return stationNames;
    }

    public List<Link> dijkstra_dist(Stop source, Stop destination) {
        Map<Stop, Double> distances = new HashMap<>();
        Map<Stop, Boolean> visited = new HashMap<>();
        Map<Stop, Link> previousLink = new HashMap<>();
        PriorityQueue<Stop> pq = new PriorityQueue<>(Comparator.comparingDouble(distances::get));
    
        // Initialiser les distances et les visites
        for (Stop stop : stops) {
            distances.put(stop, Double.MAX_VALUE);
            visited.put(stop, false);
        }
        if(source != null){
            distances.put(source, 0.0);
            pq.add(source);
        }
    
        while (!pq.isEmpty()) {
            Stop current = pq.poll();
            visited.put(current, true);
    
            // Si on atteint la destination, on retourne le chemin
            if (current.equals(destination)) {
                List<Link> path = new ArrayList<>();
                Stop step = destination;
                while (previousLink.containsKey(step)) {
                    Link link = previousLink.get(step);
                    path.add(link);
                    step = link.getSource();
                }
                Collections.reverse(path);
                return path;
            }
    
            for (Link link : adjacencyList.get(current)) {
                Stop neighbor = link.getDestination();
                if (!visited.get(neighbor)) {
                    double newDistance = distances.get(current) + link.getDistance();
                    if (newDistance < distances.get(neighbor)) {
                        distances.put(neighbor, newDistance);
                        pq.add(neighbor);
                        previousLink.put(neighbor, link);
                    }
                }
            }
        }
        // Si on n'a pas trouvé la destination, on retourne une liste vide
        return new ArrayList<>();
    }

    public List<Link> dijkstra_time(Stop source, Stop destination, double startTime) {
        Map<Stop, Double> arrivalTimes = new HashMap<>();
        Map<Stop, Boolean> visited = new HashMap<>();
        Map<Stop, Link> previousLink = new HashMap<>();
        PriorityQueue<Stop> pq = new PriorityQueue<>(Comparator.comparingDouble(arrivalTimes::get));
    
        // Initialiser les temps d'arrivée et les visites
        for (Stop stop : stops) {
            arrivalTimes.put(stop, Double.MAX_VALUE);
            visited.put(stop, false);
        }
        arrivalTimes.put(source, startTime);
        pq.add(source);
    
        while (!pq.isEmpty()) {
            Stop current = pq.poll();
            visited.put(current, true);
    
            // Si on atteint la destination, on retourne le chemin
            if (current.equals(destination)) {
                List<Link> path = new ArrayList<>();
                Stop step = destination;
                while (previousLink.containsKey(step)) {
                    Link link = previousLink.get(step);
                    path.add(link);
                    step = link.getSource();
                }
                Collections.reverse(path);
                return path;
            }
    
            for (Link link : adjacencyList.get(current)) {
                Stop neighbor = link.getDestination();
                if (!visited.get(neighbor)) {
                    double arrivalTime = arrivalTimes.get(current) + link.getTime();
                    if (arrivalTime < arrivalTimes.get(neighbor)) {
                        arrivalTimes.put(neighbor, arrivalTime);
                        pq.add(neighbor);
                        previousLink.put(neighbor, link);
                    }
                }
            }
        }
        // Si on n'a pas trouvé la destination, on retourne une liste vide
        return new ArrayList<>();
    }
    public void filterLinksByLine(String lineName) {
        List<Link> filteredLinks = new ArrayList<>();
        for (Link link : links) {
            if (link.getLineName().equals(lineName)) {
                filteredLinks.add(link);
            }
        }
        // Remplacer la liste de liens actuelle par la liste filtrée
        this.links = filteredLinks;
    }
    public void saveOriginalLinks() {
        this.originalLinks = new ArrayList<>(this.links);
    }

    public void restoreOriginalLinks() {
        if(!this.originalLinks.isEmpty()){
            this.links = new ArrayList<>(this.originalLinks);
        }
    }
}