package fr.Lecture_Reseau;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


/**
 * Ajoute les infos sur les temps de trajet et permet si souhaité de faire des sections à pied si cela est évalué plus rapide
 *@author SamySWEID
 *@author AriazHashemi
 */

public class WalkPath {

    /**
     * Évalue les options de marche à pied pour chaque segment du chemin et modifie le chemin en conséquence.
     * Cette méthode peut ajouter des segments de marche à pied si cela est jugé plus rapide que d'attendre ou d'utiliser le transport en commun,
     * en fonction du paramètre b qui active ou désactive cette évaluation.
     * 
     * @param path La liste initiale des liens représentant le chemin de transit.
     * @param startTime L'heure de départ initiale pour le commencement du chemin.
     * @param b Si vrai, la fonction évalue la possibilité de remplacer certains segments de transport par la marche à pied.
     */
    public static void evaluateWalkingOptions(List<Link> path, LocalTime startTime, boolean b) {
        List<Link> updatedPath = new ArrayList<>();
        double WALKING_SPEED_MS = 1.39; // Vitesse de marche en m/s (environ 5 km/h)
        int DISTANCE_MAX_METERS = 300; // Seuil de distance pour envisager de marcher
    
        for (int i = 0; i < path.size(); i++) {
            Link currentLink = path.get(i);
            double distanceInMeters = currentLink.getDistance() * 1000; // Convertir la distance de km en mètres
            LocalTime nextTrainDeparture = findEarliestDeparture(currentLink.getHPassage(), startTime);
            long waitTime = ChronoUnit.SECONDS.between(startTime, nextTrainDeparture); // Calculer le temps d'attente pour le métro
    
            // Calculer le temps de trajet en métro et le temps d'arrivée prévu
            long metroTravelTimeSeconds = currentLink.getTime();
            LocalTime metroArrivalTime = nextTrainDeparture.plusSeconds(metroTravelTimeSeconds);
            
            if(b==true){
                 // Vérifier si la distance est assez courte pour envisager de marcher
                 if (distanceInMeters <= DISTANCE_MAX_METERS) {
                    // Calculer le temps de marche
                    double walkingTimeSeconds = distanceInMeters / WALKING_SPEED_MS;
                    LocalTime walkingArrivalTime = startTime.plusSeconds((long) walkingTimeSeconds);

                    if (walkingArrivalTime.isBefore(metroArrivalTime)) {
                        // Marcher est plus rapide, donc créer un nouveau lien de type marche
                        Link walkingLink = new Link("Walk", (int) walkingTimeSeconds, distanceInMeters, currentLink.getSource(), currentLink.getDestination());
                        updatedPath.add(walkingLink);
                        startTime = walkingArrivalTime; // Mettre à jour l'heure de départ pour les calculs suivants
                        continue; // Passer à la prochaine itération de la boucle
                    }
                }
            }
   

            // Si marcher n'est pas plus rapide ou la distance est trop grande, utiliser le métro et inclure le temps d'attente
            if (waitTime > 0) {
                // Créer un lien spécial pour le temps d'attente, si significatif
                Link waitingLink = new Link("Waiting Time", (int) waitTime, 0, currentLink.getSource(), currentLink.getSource());
                updatedPath.add(waitingLink);
            }
            updatedPath.add(currentLink);
            startTime = metroArrivalTime; // Mise à jour de l'heure de départ pour le prochain lien
        }
    
        // Remplacer le chemin original par le nouveau
        path.clear();
        path.addAll(updatedPath);
    }
    
    /**
     * Trouve le prochain départ disponible après l'heure actuelle parmi une liste de départs.
     * 
     * @param departures Liste des heures de départ programmées pour un lien de transport.
     * @param currentTime L'heure actuelle pour laquelle trouver le prochain départ.
     * @return L'heure du prochain départ possible, ou l'heure actuelle si aucun départ n'est disponible après.
     */
    private static LocalTime findEarliestDeparture(List<LocalTime> departures, LocalTime currentTime) {
        return departures.stream()
            .filter(time -> !time.isBefore(currentTime))
            .min(LocalTime::compareTo)
            .orElse(currentTime); // Utiliser le temps actuel si aucun départ n'est disponible après
    }
}
