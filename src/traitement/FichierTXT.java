package traitement;

import donnees.Liaison;
import donnees.Ligne;
import donnees.Station;

import java.io.*;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FichierTXT extends Fichier {
    private String type;
    public FichierTXT(String nom, Scanner entree) {
        super(nom, entree);
        this.type = "";
    }

    public void lireFichier() {
        String nomLigne = nom.substring(0,nom.lastIndexOf("."));
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("data/"+nom));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        // obtenir et analyser la première ligne
        try {
            String line = reader.readLine();
            // définition du type de fichier texte (métro/intercités)
            if (Objects.equals(line, "% métro")) {
                this.type = "metro";
            } else if (Objects.equals(line, "% Car Inter-Cité")) {
                this.type = "intercites";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Ligne ligne = new Ligne(nomLigne);

        if (Objects.equals(this.type, "metro")) {
            // décrire les différents patterns
            while(entree.hasNextLine()) {
                String ligneTexte = entree.nextLine();

                // vérification station
                String patternStation = "^([A-Z]([a-z])*)(\\s+([A-Z]([a-z])*))*\\s*$";
                if (ligneTexte.matches(patternStation)) {
                    // implémenter les stations
                    String[] stations = ligneTexte.split("\s");
                    for (String station : stations) {
                        Station stationObj = new Station(station);
                        if (!ligne.getStations().contains(stationObj)) {
                            ligne.getStations().add(stationObj);
                        }
                    }
                }
                // vérification liaison
                String patternLigne = "^([A-Z]([a-z])*)\\s+([A-Z]([a-z])*)\\s+([0-2][0-9][0-5][0-9])\\s*$";
                Pattern patternL = Pattern.compile(patternLigne);
                if (ligneTexte.matches(patternLigne)) {
                    Matcher matcher = patternL.matcher(ligneTexte);

                    // implémenter les liaisons
                    if (matcher.find()) {
                        Station stationDepart = ligne.getStation(matcher.group(1));
                        // on crée les stations si elles n'existent pas
                        if (stationDepart == null) {
                            stationDepart = new Station(matcher.group(1));
                        }
                        Station stationArrivee = ligne.getStation(matcher.group(3));
                        if (stationArrivee == null) {
                            stationArrivee = new Station(matcher.group(3));
                        }
                        Liaison liaison = new Liaison(stationDepart, stationArrivee, Integer.parseInt(matcher.group(5)));
                        ligne.getLiaisons().add(liaison);
                    }
                }
            }
        }
        if (Objects.equals(this.type, "intercites")) {
            boolean slashsTrouves = false;
            int nbLigne = 0;

            // décrire les différents patterns
            while(entree.hasNextLine()) {
                String ligneTexte = entree.nextLine();
                nbLigne++;
                if (ligneTexte.equals("//")) {
                    slashsTrouves = true;
                }

                // vérification liaison
                String patternLigne = "^([A-Z]([a-z])*)\\s+([A-Z]([a-z])*)\\s+([0-9]{3,4})\\s*$";
                Pattern patternL = Pattern.compile(patternLigne);
                if (slashsTrouves) {
                    Matcher matcher = patternL.matcher(ligneTexte);

                    // implémenter les liaisons
                    if (matcher.find()) {
                        Station stationDepart = ligne.getStation(matcher.group(1));
                        // apprentissage des stations par les liaisons (sans doublon)
                        if (stationDepart == null) {
                            stationDepart = new Station(matcher.group(1));
                            ligne.getStations().add(stationDepart);
                        }
                        Station stationArrivee = ligne.getStation(matcher.group(3));
                        if (stationArrivee == null) {
                            stationArrivee = new Station(matcher.group(3));
                            ligne.getStations().add(stationArrivee);
                        }
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");

                        String heureDepartTexte = matcher.group(5);
                        LocalTime heureDepart = LocalTime.parse(heureDepartTexte, formatter);

                        // obtenir le suivant pour caclculer la durée du trajet
                        Scanner scanLigneSuiv;
                        try {
                            scanLigneSuiv = new Scanner(new File("data/"+nom));

                            for (int i=0; i<nbLigne; i++) {
                                scanLigneSuiv.nextLine(); // go to previous position (first scanner)
                            }

                            if (scanLigneSuiv.hasNextLine()) {
                                String ligneSuivante = scanLigneSuiv.nextLine();
                                Matcher matcherNext = patternL.matcher(ligneSuivante);

                                // récupérer l'heure de départ de la prochaine liaison
                                if (matcherNext.find()) {
                                    String heureArriveeTexte = matcherNext.group(5);
                                    LocalTime heureArrivee = LocalTime.parse(heureArriveeTexte, formatter);

                                    long dureeLong = Duration.between(heureDepart, heureArrivee).toMinutes();
                                    int duree = (int) dureeLong;

                                    // création de la liaison complète et ajout à la Ligne
                                    Liaison liaison = new Liaison(stationDepart, stationArrivee, heureDepartTexte, heureArriveeTexte, duree);
                                    ligne.getLiaisons().add(liaison);
                                }
                            }
                            scanLigneSuiv.close();
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        System.out.println(ligne);
    }
}
