package fr.Lecture_Reseau;
import java.time.LocalTime;

import java.time.temporal.ChronoUnit;
import java.awt.geom.Point2D;
import java.util.*;

import fr.u_paris.gla.project.utils.GPS;


    /**
     * Classe qui représente le graph du réseau de métro
     * @author SamySWEID
     * @author AriazHashemi
     * @author Paskal*/

public class Network {
    private List<Line> All_Lines;
    private List<Stop> stops;
    private List<Link> links;
    private Map<Stop, List<Link>> adjacencyList;
    private List<Link> originalLinks;

    /**
     * Constructeur de la classe Network qui initialise les listes et la carte pour le stockage des lignes,
     * arrêts, liens et listes d'adjacence.
     */
    public Network() {
        this.All_Lines = new ArrayList<>();
        this.stops = new ArrayList<>();
        this.links = new ArrayList<>();
        this.adjacencyList = new HashMap<>();
        this.originalLinks = new ArrayList<>();
    }

    public List<Line> getLines() {
        return All_Lines;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> link) {
        this.links = link;
    }

    public List<Stop> getStops() {
        return stops;
    }

    /**
     * Ajoute un arrêt au réseau.
     *
     * @param stop L'arrêt à ajouter.
     */
    public void addStop(Stop stop) {
        stops.add(stop);
    }

    /**
     * Ajoute un lien au réseau.
     *
     * @param link Le lien à ajouter.
     */
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

    /**
     * Retourne la liste d'adjacence du réseau.
     *
     * @return La liste d'adjacence représentant les connexions entre les arrêts.
     */
    public Map<Stop, List<Link>> getAdjacencyList() {
        return this.adjacencyList;
    }

    /**
     * Affiche la liste d'adjacence du réseau pour faciliter le débogage.
     */
    public void printAdjacencyList() {
        for (Stop stop : adjacencyList.keySet()) {
            System.out.print(stop.getStopName() + ":\n");
            List<Link> links = adjacencyList.get(stop);
            for (Link link : links) {
                System.out.println("\t(" + link.getDestination().getStopName() + ", " + link.getTime() + "s, " + link.getDistance() + "m, opéré par la ligne: " + link.getLineName() + ")");
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

    /*
    public List<Link> dijkstra_without_time(Stop source, Stop destination) {
        Map<Stop, Double> arrivalTimes = new HashMap<>();
        Map<Stop, Boolean> visited = new HashMap<>();
        Map<Stop, Link> previousLink = new HashMap<>();
        PriorityQueue<Stop> pq = new PriorityQueue<>(Comparator.comparingDouble(arrivalTimes::get));
    
        // Initialiser les temps d'arrivée et les visites
        for (Stop stop : stops) {
            arrivalTimes.put(stop, Double.MAX_VALUE);
            visited.put(stop, false);
        }
        arrivalTimes.put(source, 0.0);
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
        //Si on n'a pas trouvé la destination, on retourne une liste vide
        return new ArrayList<>();
    }*/
    


    /**
     * Implémente l'algorithme de Dijkstra pour trouver le chemin le plus rapide en temps entre deux arrêts,
     * en prenant en compte les horaires de départ des liens.
     * 
     * @param source L'arrêt de départ pour le calcul du chemin.
     * @param destination L'arrêt de destination pour le calcul du chemin.
    * @param currentTime L'heure actuelle à laquelle le parcours commence.
    * @return Une liste de liens représentant le chemin le plus court en temps de l'arrêt source à l'arrêt destination.
     *         Retourne une liste vide si aucun chemin n'est trouvé.
     */
    public List<Link> dijkstra_time(Stop source, Stop destination, LocalTime currentTime) {
        Map<Stop, LocalTime> arrivalTimes = new HashMap<>();
        Map<Stop, Link> previousLink = new HashMap<>();
        PriorityQueue<Stop> pq = new PriorityQueue<>(Comparator.comparingDouble(s -> arrivalTimes.get(s).toSecondOfDay()));
        Map<Stop, Boolean> visited = new HashMap<>();
    
        //Initialiser les temps d'arrivée et les visites
        for (Stop stop : getStops()) {
            arrivalTimes.put(stop, LocalTime.MAX);
            visited.put(stop, false);
        }
        arrivalTimes.put(source, currentTime);
        pq.add(source);
    
        while (!pq.isEmpty()) {
            Stop current = pq.poll();
            visited.put(current, true);
    
            if (current.equals(destination)) {
                List<Link> path = new ArrayList<>();
                Stop step = destination;
                while (previousLink.containsKey(step)) {
                    Link link = previousLink.get(step);
                    //Ajouter en 1er pour pas inverser le chemin
                    path.add(0, link);
                    step = link.getSource();
                }
                return path;
            }
    
            for (Link link : getAdjacencyList().get(current)) {
                Stop neighbor = link.getDestination();
                if (!visited.get(neighbor)) {
                    LocalTime earliestDeparture = findEarliestDeparture(link.getHPassage(), arrivalTimes.get(current));
                    if (!earliestDeparture.equals(LocalTime.MAX)) {
                        long waitTime = ChronoUnit.SECONDS.between(arrivalTimes.get(current), earliestDeparture);
                        LocalTime arrivalTime = earliestDeparture.plusSeconds(link.getTime() + waitTime);
                        if (arrivalTime.isBefore(arrivalTimes.get(neighbor))) {
                            arrivalTimes.put(neighbor, arrivalTime);
                            pq.add(neighbor);
                            previousLink.put(neighbor, link);
                        }
                    }
                }
            }
        }
        //Si on n'a pas trouvé la destination, on retourne une liste vide
        return new ArrayList<>();  
    }
    

    /**
     * Trouve le prochain départ possible après l'heure actuelle à partir d'une liste de départs programmés.
    * 
     * @param departures La liste des heures de départ programmées pour un lien.
     * @param currentTime L'heure actuelle pour laquelle le prochain départ est recherché.
     * @return L'heure du prochain départ possible. Si aucun départ n'est trouvé après l'heure actuelle, retourne MIN pour signifier aucun départ disponible.
     */
    private LocalTime findEarliestDeparture(List<LocalTime> departures, LocalTime currentTime) {
        return departures.stream()
                         .filter(time -> time.isAfter(currentTime))
                         .min(LocalTime::compareTo)
                         .orElse(LocalTime.MIN);
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

    /**
     * Méthode pour trouver l'arrêt le plus proche à partir de coordonnées géographiques données.
     * @param coordinates les coordonnées géographiques pour lesquelles trouver l'arrêt le plus proche.
     * @return l'arrêt le plus proche.
     */
    public Stop findNearestStop(Point2D.Double coordinates) {
        Stop nearestStop = null;
        double shortestDistance = Double.MAX_VALUE;

        // Parcourir tous les arrêts pour trouver le plus proche
        for (Stop stop : stops) {
            double distance = coordinates.distance(new Point2D.Double(stop.getLatitude(), stop.getLongitude()));
            if (distance < shortestDistance) {
                shortestDistance = distance;
                nearestStop = stop;
            }
        }

        return nearestStop;
    }

    
    /**
     * Trouve le chemin optimal entre deux points géographiques en utilisant l'algorithme de Dijkstra basé sur le temps.
     * @param sourceCoordinates les coordonnées du point de départ.
     * @param destinationCoordinates les coordonnées du point d'arrivée.
     * @param currentTime l'heure actuelle pour calculer les départs.
     * @return une liste des liens formant le chemin optimal.
     */
    public List<Link> findPathFromCoordinates(Point2D.Double sourceCoordinates, Point2D.Double destinationCoordinates, LocalTime currentTime) {
        Stop sourceStop = findNearestStop(sourceCoordinates);
        Stop destinationStop = findNearestStop(destinationCoordinates);

        if (sourceStop == null || destinationStop == null) {
            System.out.println("Impossible de trouver des arrêts pour les coordonnées spécifiées.");
            return new ArrayList<>(); // Retourner une liste vide si les arrêts ne sont pas trouvés
        }

        // Utiliser l'algorithme Dijkstra pour trouver le chemin optimal entre les arrêts
        return dijkstra_time(sourceStop, destinationStop, currentTime);
    }
    
    public void Affich(Point2D.Double sourceCoordinates, Point2D.Double destinationCoordinates, LocalTime currentTime){
    
    LocalTime departureTime1 = LocalTime.now();

    // Trouver le chemin optimal entre les deux points dans Paris
    List<Link> shortestPath1 = findPathFromCoordinates(sourceCoordinates, destinationCoordinates, departureTime1);
    WalkPath.evaluateWalkingOptions(shortestPath1, departureTime1, false);
    // Afficher le chemin trouvé
    System.out.println("\n\nLe chemin le plus rapide pour aller des coordonnées " + sourceCoordinates + " aux coordonnées " + destinationCoordinates + " est le suivant:");
    double totalTime2 = 0;
    for (Link link : shortestPath1) {
        System.out.println("\t[ " + link.getDestination().getStopName() + ", " + link.getTime() + "s, " + link.getDistance() + "m, opéré par la ligne: " + link.getLineName() + " ]");
        totalTime2 += link.getTime();
    }
        
    // Calculer le temps de marche jusqu'à la station de départ
    Stop sourceStop = findNearestStop(sourceCoordinates);
    double sourceDistance = GPS.distance(sourceCoordinates.getX(), sourceCoordinates.getY(), sourceStop.getLatitude(), sourceStop.getLongitude());
    int walkingTimeSource = (int) Math.ceil(sourceDistance / 5 * 60 * 60); // en minutes

    // Calculer le temps de marche jusqu'à la station d'arrivée
    Stop destinationStop =findNearestStop(destinationCoordinates);
    double destinationDistance = GPS.distance(destinationCoordinates.getX(), destinationCoordinates.getY(), destinationStop.getLatitude(), destinationStop.getLongitude());
    int walkingTimeDestination = (int) Math.ceil(destinationDistance / 5 * 60 * 60); // en minutes

    // Calculer le temps total de marche
    int totalWalkingTime = (walkingTimeSource + walkingTimeDestination);

    // Transformation du temps de trajet en long
    long tot1 = (long) totalTime2;
    System.out.println("\nMarche de "+ sourceCoordinates + " en " + walkingTimeSource + " sec Jusqu'a " +sourceStop.getStopName() +"\n####Heure d'arrivée prévue a "+destinationStop.getStopName() +": " + departureTime1.plusSeconds(tot1 + walkingTimeSource) + "###\n\n Il faut marcher "+ walkingTimeDestination + " sec jusqu'a "+ destinationCoordinates+" donc arrivé finale prévu à "+ departureTime1.plusSeconds(tot1+totalWalkingTime));
    }

}