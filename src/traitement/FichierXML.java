package traitement;

import donnees.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class FichierXML extends Fichier {
    public FichierXML(String nom, String chemin, Scanner scanner) {
        super(nom, chemin, scanner);
    }

    public void lireFichier() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            try {
                Document doc = db.parse(new File(chemin));
                doc.getDocumentElement().normalize();
                String nomRacine = doc.getDocumentElement().getNodeName();

                // formater la date par rapport au type attendu
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");

                if (nomRacine.equals("horaires")) {
                    this.lireFichierTrain(doc,formatter);
                } else if (nomRacine.equals("reseau")) {
                    this.lireFichierTram(doc,formatter);
                } else {
                    System.out.println("Racine incorrecte, merci de vérifier le fichier.");
                }
            } catch (SAXException | IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void lireFichierTrain(Document doc, DateTimeFormatter formatter) {
        // création du réseau et de l'exploitant
        Reseau reseau = new Reseau("train");
        Exploitant exploitant = new Exploitant("train");
        reseau.getExploitants().add(exploitant);
        NodeList nodeList = doc.getElementsByTagName("line");
        for (int i=0;i<nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            // récupérer le nom de ligne (suppression des espaces)
            String nomLigne = node.getChildNodes().item(0).getTextContent().trim();
            Ligne ligne = new Ligne(nomLigne);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                NodeList jonctions = element.getElementsByTagName("junction");
                for (int j=0;j<jonctions.getLength();j++) {
                    Node jonction = jonctions.item(j);
                    if (jonction.getNodeType() == Node.ELEMENT_NODE) {
                        Element jonctionElt = (Element) jonction;
                        String stationDepartTexte = jonctionElt.getElementsByTagName("start-station").item(0).getTextContent();
                        String stationArriveeTexte = jonctionElt.getElementsByTagName("arrival-station").item(0).getTextContent();
                        String heureDepartTexte = jonctionElt.getElementsByTagName("start-hour").item(0).getTextContent();
                        LocalTime heureDepart = LocalTime.parse(heureDepartTexte, formatter);
                        String heureArriveeTexte = jonctionElt.getElementsByTagName("arrival-hour").item(0).getTextContent();
                        LocalTime heureArrivee = LocalTime.parse(heureArriveeTexte, formatter);
                        long dureeLong = Duration.between(heureDepart, heureArrivee).toMinutes();
                        int duree = (int) dureeLong;
                        // créer les stations
                        Station stationDepart = ligne.getStation(stationDepartTexte);
                        // apprentissage des stations par les liaisons (sans doublon)
                        if (stationDepart == null) {
                            stationDepart = new Station(stationDepartTexte);
                            ligne.getStations().add(stationDepart);
                        }
                        Station stationArrivee = ligne.getStation(stationArriveeTexte);
                        if (stationArrivee == null) {
                            stationArrivee = new Station(stationArriveeTexte);
                            ligne.getStations().add(stationArrivee);
                        }
                        // créer la liaison et l'ajouter
                        Liaison liaison = new Liaison(stationDepart,stationArrivee,heureDepartTexte,heureArriveeTexte,duree);
                        ligne.getLiaisons().add(liaison);
                    }
                }
            }
            exploitant.getLignes().add(ligne);
        }
        System.out.println(reseau);
    }

    public void lireFichierTram(Document doc, DateTimeFormatter formatter) {
        // création du réseau et de l'exploitant
        Reseau reseau = new Reseau("tram");
        Exploitant exploitant = new Exploitant("tram");
        reseau.getExploitants().add(exploitant);

        NodeList nodeList = doc.getElementsByTagName("reseau");
        for (int i=0;i<nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                NodeList lignes = element.getElementsByTagName("ligne");
                for (int j=0;j<lignes.getLength();j++) {
                    Node ligne = lignes.item(j);
                    // récupérer le nom de la Ligne
                    String nomLigne = ligne.getChildNodes().item(0).getTextContent().trim();

                    // instancier les Lignes
                    Ligne ligneObj = new Ligne(nomLigne);
                    if (ligne.getNodeType() == Node.ELEMENT_NODE) {
                        Element ligneElt = (Element) ligne;
                        // récupérer ordre des stations de la ligne
                        Node stationsLigne = ligneElt.getElementsByTagName("stations").item(0);
                        String stationsLigneTexte = stationsLigne.getTextContent();
                        String[] stations = stationsLigneTexte.split("\s");
                        // instanciation des Stations (sans doublon)
                        for (String stationTexte : stations) {
                            Station station = ligneObj.getStation(stationTexte);
                            if (station == null) {
                                station = new Station(stationTexte);
                                ligneObj.getStations().add(station);
                            }
                        }
                        NodeList heuresPassageJournee = ligneElt.getElementsByTagName("heures-passage");
                        for (int k=0;k<heuresPassageJournee.getLength();k++) {
                            Node heurePassageLigneTexte = heuresPassageJournee.item(k);
                            String heuresPassageTexte = heurePassageLigneTexte.getTextContent();

                            // découper les heures de passage par le séparateur espace
                            String[] heuresPassage = heuresPassageTexte.split("\s");

                            // récupérer les tableaux splités pour les instancier
                            for(int index=0;index<heuresPassage.length-1;index++) {
                                String stationDepartTexte = stations[index];
                                Station stationDepart = ligneObj.getStation(stationDepartTexte);
                                String stationArriveeTexte = stations[index+1];
                                Station stationArrivee = ligneObj.getStation(stationArriveeTexte);
                                String heureDepartTexte = heuresPassage[index];
                                LocalTime heureDepart = LocalTime.parse(heureDepartTexte, formatter);
                                String heureArriveeTexte = heuresPassage[index+1];
                                LocalTime heureArrivee = LocalTime.parse(heureArriveeTexte, formatter);
                                long dureeLong = Duration.between(heureDepart, heureArrivee).toMinutes();
                                int duree = (int) dureeLong;

                                // créer la liaison et l'ajouter
                                Liaison liaison = new Liaison(stationDepart,stationArrivee,heureDepartTexte,heureArriveeTexte,duree);
                                ligneObj.getLiaisons().add(liaison);
                            }
                        }
                    }
                    exploitant.getLignes().add(ligneObj);
                }
            }
        }
        System.out.println(reseau);
    }
}
