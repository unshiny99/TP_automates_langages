package traitement;

import donnees.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FichierTXT extends Fichier {

    public FichierTXT(String nom, String chemin, Scanner entree) {
        super(nom, chemin, entree);
    }

    public void lireFichier() {
        String nomLigne = nom.substring(0,nom.lastIndexOf("."));
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(chemin));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        // obtenir et analyser la première ligne
        try {
            Reseau reseau = new Reseau(nomLigne);
            Exploitant exploitant = new Exploitant(nomLigne);
            reseau.getExploitants().add(exploitant);

            // instancier la Ligne de transport
            Ligne ligne = new Ligne(nomLigne);
            exploitant.getLignes().add(ligne);

            String line = reader.readLine();
            // traitement en fonction du fichier
            if (Objects.equals(line, "% métro")) {
                this.lireFichierMetro(ligne);
            } else if (Objects.equals(line, "% Car Inter-Cité")) {
                this.lireFichierInterCites(ligne);
            }
            System.out.println(reseau);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void lireFichierMetro(Ligne ligne) {
        // formatteur des heures renseignée
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        List<Jonction> jonctionsTemp = new ArrayList<>();
        // déclarations des variables permettant de stocket les infos de génération dynamique
        int tempsArret = 0;
        int intervalleHeurePleine = 0;
        LocalTime heureDepartMatin = null;
        LocalTime heureFinMatin = null;
        LocalTime heureDepartApM = null;
        LocalTime heureFinApM = null;
        int intervalleHeureCreuse = 0;
        LocalTime heureDernier = null;

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
            String patternLigne = "^([A-Z]([a-z])*)\\s+([A-Z]([a-z])*)\\s+([0-9]+)\\s*$";
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

                    // ajout de toutes les jonctions du fichier dans la liste
                    Jonction jonction = new Jonction(stationDepart, stationArrivee, Integer.parseInt(matcher.group(5)));
                    jonctionsTemp.add(jonction);
                }
            }

            // vérif du temps d'arrêt
            String patternTempsArretTxt = "^%\\s+a([A-Za-zÀ-ÿ]*\\s+)*([0-9]*)\\s+([A-Za-zÀ-ÿ]*\\s*)*$";
            Pattern patternTempsArret = Pattern.compile(patternTempsArretTxt);
            if (ligneTexte.matches(patternTempsArretTxt)) {
                Matcher matcherTpsArret = patternTempsArret.matcher(ligneTexte);
                if (matcherTpsArret.find()) {
                    tempsArret = Integer.parseInt(matcherTpsArret.group(2));
                }
            }

            // vérif des horaires en heure pleine
            String patternHorairesJourneeTxt = "^%\\s+([A-Za-zÀ-ÿ]*\\s+)*([0-9]+)\\s+([A-Za-zÀ-ÿ]*\\s+)*(([0-1][1-9]|2[0-3]):[0-5][0-9])\\s+[A-Za-zÀ-ÿ]*\\s+(([0-1][1-9]|2[0-3]):[0-5][0-9])\\s+([A-Za-zÀ-ÿ]*\\s+)*(([0-1][1-9]|2[0-3]):[0-5][0-9])\\s+[A-Za-zÀ-ÿ]*\\s+(([0-1][1-9]|2[0-3]):[0-5][0-9])\\s*$";
            Pattern patternHorairesJournee = Pattern.compile(patternHorairesJourneeTxt);
            if (ligneTexte.matches(patternHorairesJourneeTxt)) {
                Matcher matcherHorairesJournee = patternHorairesJournee.matcher(ligneTexte);
                if (matcherHorairesJournee.find()) {
                    intervalleHeurePleine = Integer.parseInt(matcherHorairesJournee.group(2));

                    String departMatin = matcherHorairesJournee.group(4);
                    heureDepartMatin = LocalTime.parse(departMatin, formatter);

                    String finMatin = matcherHorairesJournee.group(6);
                    heureFinMatin = LocalTime.parse(finMatin, formatter);

                    String departApM = matcherHorairesJournee.group(9);
                    heureDepartApM = LocalTime.parse(departApM, formatter);

                    String finApM = matcherHorairesJournee.group(11);
                    heureFinApM = LocalTime.parse(finApM, formatter);
                }
            }

            // vérif intervalle heure creuse
            String patternInterHeureCreuseTxt = "^%\\s+d([A-Za-zÀ-ÿ]*\\s+)*([0-9]*)\\s+([A-Za-zÀ-ÿ]*\\s*)*$";
            Pattern patternInterHeureCreuse = Pattern.compile(patternInterHeureCreuseTxt);
            if (ligneTexte.matches(patternInterHeureCreuseTxt)) {
                Matcher matcherTpsHeureCreuse = patternInterHeureCreuse.matcher(ligneTexte);
                if (matcherTpsHeureCreuse.find()) {
                    intervalleHeureCreuse = Integer.parseInt(matcherTpsHeureCreuse.group(2));
                }
            }

            // vérif heure dernier départ
            String patternHeureDernierTxt = "^%\\s+([A-Za-zÀ-ÿ]*\\s+)*(([0-1][1-9]|2[0-3]):[0-5][0-9])\\s*$";
            Pattern patternHeureDernier = Pattern.compile(patternHeureDernierTxt);
            if (ligneTexte.matches(patternHeureDernierTxt)) {
                Matcher matcherHeureDernier = patternHeureDernier.matcher(ligneTexte);
                if (matcherHeureDernier.find()) {
                    String heureDernierTxt = matcherHeureDernier.group(2);
                    heureDernier = LocalTime.parse(heureDernierTxt,formatter);
                }
            }
        }

        List<List<Jonction>> ensJonctions = new ArrayList<>();
        List<Jonction> j = null;
        for (int i=0; i<jonctionsTemp.size();i++) {
            if (jonctionsTemp.get(i).getStationDepart().getNom().equals("Gare")) {
                if (j != null)
                    ensJonctions.add(j);
                j = new ArrayList<>();
                j.add(jonctionsTemp.get(i));
            } else {
                if (j != null) {
                    j.add(jonctionsTemp.get(i));
                }
            }
            if (i==jonctionsTemp.size()-1 && j!=null) {
                ensJonctions.add(j);
            }
        }

        // gestion de la fourchette matin
        if(heureDepartMatin != null && heureFinMatin != null) {
            for (List<Jonction> jonctions : ensJonctions) {
                int diff = heureFinMatin.compareTo(heureDepartMatin);
                LocalTime heureDepart = heureDepartMatin;
                LocalTime heureDifferee = heureDepartMatin;
                while(diff >= 0) {
                    for (int i=0; i<jonctions.size();i++) {
                        if (i==0) {
                            LocalTime heureArrivee = heureDifferee.plusMinutes(jonctions.get(i).getDuree());
                            Liaison liaison = new Liaison(jonctions.get(i).getStationDepart(), jonctions.get(i).getStationArrivee(), heureDifferee.toString(), heureArrivee.toString(), jonctions.get(i).getDuree());
                            ligne.getLiaisons().add(liaison);
                            heureDepart = heureDifferee.plusMinutes(jonctions.get(i).getDuree() + tempsArret);
                            //System.out.println(heureDepart);
                        } else {
                            LocalTime heureArrivee = heureDepart.plusMinutes(jonctions.get(i).getDuree());
                            Liaison liaison = new Liaison(jonctions.get(i).getStationDepart(), jonctions.get(i).getStationArrivee(), heureDepart.toString(), heureArrivee.toString(), jonctions.get(i).getDuree());
                            ligne.getLiaisons().add(liaison);
                            heureDepart = heureDepart.plusMinutes(jonctions.get(i).getDuree() + tempsArret);
                        }
                    }
                    heureDifferee = heureDifferee.plusMinutes(intervalleHeurePleine);
                    diff = heureFinMatin.compareTo(heureDifferee);
                }
            }
        }

        // gestion de la fourchette heure creuse
        if(heureFinMatin != null && heureDepartApM != null) {
            for (List<Jonction> jonctions : ensJonctions) {
                int diff = heureDepartApM.compareTo(heureFinMatin);
                LocalTime heureDepart = heureFinMatin;
                LocalTime heureDifferee = heureFinMatin;
                while(diff >= 0) {
                    for (int i=0; i<jonctions.size();i++) {
                        if (i==0) {
                            LocalTime heureArrivee = heureDifferee.plusMinutes(jonctions.get(i).getDuree());
                            Liaison liaison = new Liaison(jonctions.get(i).getStationDepart(), jonctions.get(i).getStationArrivee(), heureDifferee.toString(), heureArrivee.toString(), jonctions.get(i).getDuree());
                            ligne.getLiaisons().add(liaison);
                            heureDepart = heureDifferee.plusMinutes(jonctions.get(i).getDuree() + tempsArret);
                            //System.out.println(heureDepart);
                        } else {
                            LocalTime heureArrivee = heureDepart.plusMinutes(jonctions.get(i).getDuree());
                            Liaison liaison = new Liaison(jonctions.get(i).getStationDepart(), jonctions.get(i).getStationArrivee(), heureDepart.toString(), heureArrivee.toString(), jonctions.get(i).getDuree());
                            ligne.getLiaisons().add(liaison);
                            heureDepart = heureDepart.plusMinutes(jonctions.get(i).getDuree() + tempsArret);
                        }
                    }
                    heureDifferee = heureDifferee.plusMinutes(intervalleHeureCreuse);
                    diff = heureDepartApM.compareTo(heureDifferee);
                }
            }
        }

        // gestion de la fourchette après-midi
        if(heureDepartApM != null && heureFinApM != null) {
            for (List<Jonction> jonctions : ensJonctions) {
                int diff = heureFinApM.compareTo(heureDepartApM);
                LocalTime heureDepart = heureDepartApM;
                LocalTime heureDifferee = heureDepartApM;
                while(diff >= 0) {
                    for (int i=0; i<jonctions.size();i++) {
                        if (i==0) {
                            LocalTime heureArrivee = heureDifferee.plusMinutes(jonctions.get(i).getDuree());
                            Liaison liaison = new Liaison(jonctions.get(i).getStationDepart(), jonctions.get(i).getStationArrivee(), heureDifferee.toString(), heureArrivee.toString(), jonctions.get(i).getDuree());
                            ligne.getLiaisons().add(liaison);
                            heureDepart = heureDifferee.plusMinutes(jonctions.get(i).getDuree() + tempsArret);
                            //System.out.println(heureDepart);
                        } else {
                            LocalTime heureArrivee = heureDepart.plusMinutes(jonctions.get(i).getDuree());
                            Liaison liaison = new Liaison(jonctions.get(i).getStationDepart(), jonctions.get(i).getStationArrivee(), heureDepart.toString(), heureArrivee.toString(), jonctions.get(i).getDuree());
                            ligne.getLiaisons().add(liaison);
                            heureDepart = heureDepart.plusMinutes(jonctions.get(i).getDuree() + tempsArret);
                        }
                    }
                    heureDifferee = heureDifferee.plusMinutes(intervalleHeurePleine);
                    diff = heureFinApM.compareTo(heureDifferee);
                }
            }
        }

        // gestion fourchette soirée
        if(heureFinApM != null && heureDernier != null) {
            for (List<Jonction> jonctions : ensJonctions) {
                int diff = heureDernier.compareTo(heureFinApM);
                LocalTime heureDepart = heureFinApM;
                LocalTime heureDifferee = heureFinApM;
                while(diff >= 0) {
                    for (int i=0; i<jonctions.size();i++) {
                        if (i==0) {
                            LocalTime heureArrivee = heureDifferee.plusMinutes(jonctions.get(i).getDuree());
                            Liaison liaison = new Liaison(jonctions.get(i).getStationDepart(), jonctions.get(i).getStationArrivee(), heureDifferee.toString(), heureArrivee.toString(), jonctions.get(i).getDuree());
                            ligne.getLiaisons().add(liaison);
                            heureDepart = heureDifferee.plusMinutes(jonctions.get(i).getDuree() + tempsArret);
                            //System.out.println(heureDepart);
                        } else {
                            LocalTime heureArrivee = heureDepart.plusMinutes(jonctions.get(i).getDuree());
                            Liaison liaison = new Liaison(jonctions.get(i).getStationDepart(), jonctions.get(i).getStationArrivee(), heureDepart.toString(), heureArrivee.toString(), jonctions.get(i).getDuree());
                            ligne.getLiaisons().add(liaison);
                            heureDepart = heureDepart.plusMinutes(jonctions.get(i).getDuree() + tempsArret);
                        }
                    }
                    heureDifferee = heureDifferee.plusMinutes(intervalleHeureCreuse);
                    diff = heureDernier.compareTo(heureDifferee);
                }
            }
        }
    }

    public void lireFichierInterCites(Ligne ligne) {
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

                    // obtenir le suivant pour calculer la durée du trajet
                    Scanner scanLigneSuiv;
                    try {
                        scanLigneSuiv = new Scanner(new File(chemin));

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
}
