package fr.u_paris.gla.project.Extract_Time;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * La classe ScheduleGenerator permet de générer un fichier d'horaire pour différentes lignes de transport
 * à partir des données fournies dans un fichier CSV. Cette classe gère également la détection et l'élimination des arrêts en double.
 * Elle est conçue pour être utilisée avec des fichiers CSV spécifiques contenant des données de transport.
 *@author AriazHashemi*/

public class ScheduleGenerator {

    /**
     * Point d'entrée principal de l'application. Prend deux arguments de ligne de commande pour les chemins de fichier d'entrée et de sortie.
     * Il lit les données du fichier CSV d'entrée, génère les horaires, et écrit les résultats dans un fichier CSV de sortie.
     *
     * @param args Les arguments de ligne de commande, où args[0] est le chemin du fichier d'entrée et args[1] est le chemin du fichier de sortie.
    */
    public static void main(String[] args) {
        String inputFilePath = args[0];  // fichier CSV d'entrée
        String outputFilePath = args[1];  // fichier CSV de sortie

        try {
            Map<String, Set<String>> stopsByLine = new HashMap<>();
            Map<String, Set<String>> duplicateStopsByLine = new HashMap<>();
            readInputFile(inputFilePath, stopsByLine, duplicateStopsByLine);
            writeFile(outputFilePath, stopsByLine, duplicateStopsByLine);
            System.out.println("Fichier 'horaire.csv' créé avec succès.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Une erreur est survenue lors de la lecture ou de l'écriture des fichiers.");
        }
    }
    
    /**
     * Lit le fichier d'entrée CSV spécifié et extrait les informations des lignes et des arrêts.
     * Les arrêts en double sont identifiés et stockés séparément pour éviter les répétitions dans le traitement final.
     *
     * @param filePath Chemin d'accès au fichier d'entrée CSV.
     * @param stopsByLine Map pour stocker les lignes avec leurs arrêts correspondants.
     * @param duplicateStopsByLine Map pour stocker les lignes avec leurs arrêts en double.
     * @throws IOException Si une erreur se produit lors de la lecture du fichier.
     */
    private static void readInputFile(String filePath, Map<String, Set<String>> stopsByLine, Map<String, Set<String>> duplicateStopsByLine) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                String lineId = values[0];
                String stopName = values[2].replace("\"", "").trim();

                stopsByLine.putIfAbsent(lineId, new HashSet<>());
                if (!stopsByLine.get(lineId).add(stopName)) {
                    duplicateStopsByLine.putIfAbsent(lineId, new HashSet<>());
                    duplicateStopsByLine.get(lineId).add(stopName);
                }
            }
        }
    }

    /**
     * Écrit les horaires calculés dans le fichier CSV de sortie spécifié.
     * Cette méthode prend en compte les doublons retirés et rédige un horaire pour chaque arrêt valide restant.
     *
     * @param filePath Chemin d'accès au fichier de sortie CSV.
     * @param stopsByLine Map contenant les lignes et leurs arrêts correspondants sans doublons.
     * @param duplicateStopsByLine Map contenant les lignes et leurs arrêts en double.
     * @throws IOException Si une erreur se produit lors de l'écriture du fichier.
     */
    private static void writeFile(String filePath, Map<String, Set<String>> stopsByLine, Map<String, Set<String>> duplicateStopsByLine) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            generateSchedules(writer, stopsByLine, duplicateStopsByLine);
        }
    }

    /**
     * Génère les horaires pour chaque arrêt et chaque ligne, écrit chaque entrée dans le fichier de sortie.
     * Cette fonction traite également les arrêts spéciaux qui ne doivent pas inclure des horaires réguliers.
     *
     * @param writer Le FileWriter utilisé pour écrire dans le fichier de sortie.
     * @param stopsByLine Map contenant les lignes et leurs arrêts correspondants.
     * @param duplicateStopsByLine Map contenant les lignes et leurs arrêts en double.
     * @throws IOException Si une erreur se produit lors de l'écriture dans le fichier.
     */
    private static void generateSchedules(FileWriter writer, Map<String, Set<String>> stopsByLine, Map<String, Set<String>> duplicateStopsByLine) throws IOException {
        LocalTime startTime = LocalTime.of(8, 30);
        LocalTime endTime = LocalTime.of(22, 0);

        int countChat = 0;
        int countCour = 0;

        for (Map.Entry<String, Set<String>> entry : stopsByLine.entrySet()) {
            String lineId = entry.getKey();
            Set<String> stops = entry.getValue();
            Set<String> duplicates = duplicateStopsByLine.getOrDefault(lineId, new HashSet<>());

            stops.removeAll(duplicates);

            int interval = 8;
            int temp = 1;
            for (String terminus : stops) {
                if (!isSpecialStop(terminus)) {
                    LocalTime time = startTime.plusMinutes(temp);
                    while (time.isBefore(endTime)) {
                        String valueColumn = "[0]";
                        if ("Châtillon-Montrouge".equals(terminus)) {
                            valueColumn = countChat % 2 == 0 ? "[1]" : "[2]";
                            countChat++;
                        }
                        if ("La Courneuve - 8 Mai 1945".equals(terminus)) {
                            valueColumn = countCour % 2 == 0 ? "[1]" : "[2]";
                            countCour++;
                        }
                        writer.write(lineId + "; " + valueColumn + "; " + terminus + "; " + time.toString() + ";\n");
                        time = time.plusMinutes(interval + temp);
                    }
                    temp++;
                }
            }
        }
        LocalTime time9 = startTime.plusMinutes(21);
        while (time9.isBefore(endTime)) {
            writer.write("9" + "; " + "[0] ;"+"Mairie de Montreuil" + "; " + time9.toString() + ";\n");
            time9 = time9.plusMinutes(6);
        }
    }

    /**
     * Vérifie si un terminus est considéré comme un arrêt spécial.
     * Ces arrêts spéciaux sont traités différemment pour éviter des duplications de données dans le fichier de sortie.
     *
     * @param terminus Le nom de l'arrêt à vérifier.
     * @return true si l'arrêt est spécial, false sinon.
     */
    private static boolean isSpecialStop(String terminus) {
        return terminus.equals("Danube") || terminus.equals("Place des Fêtes") || terminus.equals("Porte d'Auteuil") || terminus.equals("Michel-Ange - Auteuil") || terminus.equals("Michel-Ange - Molitor") || terminus.equals("Mirabeau") || terminus.equals("Chardon Lagache") || terminus.equals("Église d'Auteuil");
    }
}
