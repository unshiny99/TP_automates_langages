package traitement;

import donnees.Exploitant;
import donnees.Liaison;
import donnees.Ligne;
import donnees.Station;
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
    public FichierXML(String nom, Scanner scanner) {
        super(nom, scanner);
    }

    public void lireFichier() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            try {
                //System.out.println("data/"+nom);
                Document doc = db.parse(new File("data/"+nom));
                doc.getDocumentElement().normalize();
                String nomRacine = doc.getDocumentElement().getNodeName();
                // formateur de la date
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");

                if (nomRacine.equals("horaires")) {
                    Exploitant exploitant = new Exploitant("train");
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
                    System.out.println(exploitant);
                } else if (nomRacine.equals("reseau")) {
                    System.out.println("fichier tram.xml");
                } else {
                    System.out.println("Racine incorrecte, merci de vérifier le fichier.");
                }
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}
