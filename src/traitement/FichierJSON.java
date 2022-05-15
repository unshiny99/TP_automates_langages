package traitement;

import donnees.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class FichierJSON extends Fichier {
    public FichierJSON(String nom, String chemin, Scanner scanner) {
        super(nom, chemin, scanner);
    }

    public void lireFichier() {
        JSONParser jsonParser = new JSONParser();
        try {
            // création du réseau et de l'exploitant
            Reseau reseau = new Reseau("bus");
            Exploitant exploitant = new Exploitant("bus");
            reseau.getExploitants().add(exploitant);

            JSONObject objetJson = (JSONObject) jsonParser.parse(new FileReader(chemin));
            // récupérer l'attribut ligne
            String nomLigne = (String) objetJson.get("ligne");
            // ajout de la ligne à l'exploitant
            Ligne ligneTransport = new Ligne(nomLigne);
            exploitant.getLignes().add(ligneTransport);

            JSONArray horaires = (JSONArray) objetJson.get("horaires");
            for (Object ligne : horaires) {
                JSONObject ligneObj = (JSONObject) ligne;
                JSONArray stations = (JSONArray) ligneObj.get("stations");
                JSONArray passages = (JSONArray) ligneObj.get("passages");

                // ajout des stations (sans doublon)
                for (Object s : stations) {
                    JSONObject stationObj = (JSONObject) s;
                    Station station = ligneTransport.getStation((String) stationObj.get("station"));
                    if (station == null) {
                        station = new Station((String) stationObj.get("station"));
                        ligneTransport.getStations().add(station);
                    }
                }

                // initialisation des tranformateurs
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");

                // ajout des liaisons
                for (Object tournee : passages) {
                    JSONArray tourneeObj = (JSONArray) tournee;
                    for (int i=0;i<stations.size()-1;i++) {
                        String heureDepartTexte = (String) tourneeObj.get(i);
                        LocalTime heureDepart = LocalTime.parse(heureDepartTexte, formatter);
                        String heureArriveeTexte = (String) tourneeObj.get(i+1);
                        LocalTime heureArrivee = LocalTime.parse(heureArriveeTexte, formatter);

                        long dureeLong = Duration.between(heureDepart, heureArrivee).toMinutes();
                        int duree = (int) dureeLong;

                        JSONObject stationDepartObj = (JSONObject) stations.get(i);
                        JSONObject stationArriveeObj = (JSONObject) stations.get(i+1);

                        String stationDepart = (String) stationDepartObj.get("station");
                        String stationArrivee = (String) stationArriveeObj.get("station");

                        Liaison liaison = new Liaison(ligneTransport.getStation(stationDepart),ligneTransport.getStation(stationArrivee),heureDepartTexte,heureArriveeTexte,duree);
                        ligneTransport.getLiaisons().add(liaison);
                    }
                }
            }
            System.out.println(reseau);
        } catch (IOException e) {
            System.out.println("Le fichier n'a pas été trouvé.");
        } catch (ParseException e) {
            System.out.println("Erreur dans le fichier JSON.");
        }
    }
}
