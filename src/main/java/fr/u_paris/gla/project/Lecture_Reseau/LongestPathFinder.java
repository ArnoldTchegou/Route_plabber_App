package fr.u_paris.gla.project.Lecture_Reseau;

//import java.util.HashMap;
import java.util.List;
//import java.util.Map;

public class LongestPathFinder {

    public static void findLongestPathsForEachLine(Network network) {
        for (Line line : network.getLines()) {
            // Récupération des arrêts de la ligne
            List<Stop> stops = line.getLineStops();
            int n = stops.size();

            // Initialisation de la matrice de distance
            double[][] dist = new double[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i == j) {
                        dist[i][j] = 0;
                    } else {
                        dist[i][j] = Double.POSITIVE_INFINITY;
                    }
                }
            }

            // Remplissage de la matrice de distance avec les données des liens
            for (Link link : line.getLineLinks()) {
                int srcIndex = stops.indexOf(link.getSource());
                int destIndex = stops.indexOf(link.getDestination());
                dist[srcIndex][destIndex] = link.getDistance();
            }

            // Algorithme de Floyd-Warshall
            for (int k = 0; k < n; k++) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (dist[i][k] + dist[k][j] < dist[i][j]) {
                            dist[i][j] = dist[i][k] + dist[k][j];
                        }
                    }
                }
            }

            // Identification du chemin le plus long pour la ligne courante
            double longestPath = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][j] > longestPath && dist[i][j] != Double.POSITIVE_INFINITY) {
                        longestPath = dist[i][j];
                    }
                }
            }

            System.out.println("La plus longue distance pour la ligne " + line.getLineName() + " est : " + longestPath + "m");
        }
    }

    // Ajouter d'autres méthodes si nécessaire
}
