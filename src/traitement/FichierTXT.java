package traitement;

import donnees.Liaison;
import donnees.Ligne;
import donnees.Station;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FichierTXT extends Fichier {
    public FichierTXT(String nom, Scanner entree) {
        super(nom, entree);
    }

    public void lireFichier() {
        String nomLigne = nom.substring(0,nom.lastIndexOf("."));
        Ligne ligne = new Ligne(nomLigne);

        // décrire les différents patterns
        while(entree.hasNextLine()) {
            String ligneTexte = entree.nextLine();

            // vérification station
            String patternStation = "^([A-Z]([a-z])*)(\\s+([A-Z]([a-z])*))*\\s*$";
            if(ligneTexte.matches(patternStation)) {
                System.out.println("Station trouvée !");

                // implémenter les stations
                String[] stations = ligneTexte.split("\s");
                for(String station : stations) {
                    Station stationObj = new Station(station);
                    if(!ligne.getStations().contains(stationObj)) {
                        ligne.getStations().add(stationObj);
                    }
                }
            }
            /*
            TODO (future version) :
             vérifier que les 2 lignes existent pour la liaison
             (sinon les créer ou renvoyer une erreur)
            */
            // vérification liaison
            String patternLigne = "^([A-Z]([a-z])*)\\s+([A-Z]([a-z])*)\\s+([0-9]{1,})\\s*$";
            Pattern patternL = Pattern.compile(patternLigne);
            if (ligneTexte.matches(patternLigne)) {
                System.out.println("Liaison trouvée !");
                Matcher matcher = patternL.matcher(ligneTexte);

                // implémenter les liaisons
                if (matcher.find()) {
                    Station stationDepart = ligne.getStation(matcher.group(1));
                    if (stationDepart == null) {
                        stationDepart = new Station(matcher.group(1));
                    }
                    Station stationArrivee = ligne.getStation(matcher.group(3));
                    if (stationArrivee == null) {
                        stationArrivee = new Station(matcher.group(3));
                    }
                    Liaison liaison = new Liaison(stationDepart,stationArrivee, Integer.parseInt(matcher.group(5)));
                    ligne.getLiaisons().add(liaison);
                }
            }
        }
        System.out.println(ligne);
    }
}
