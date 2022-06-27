import donnees.Reseau;
import org.xml.sax.SAXException;
import traitement.FichierJSON;
import traitement.FichierTXT;
import traitement.FichierXML;
import traitement.InvalideFormatException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class FonctionPrincipale extends Throwable {

    public static void main(String[] args) throws InvalideFormatException, IOException, SAXException {
        // liste statiques des 5 fichiers à lire
        String[] fichiers = {"bus.json","InterCites.txt", "metro.txt", "train.xml", "tram.xml"};
        Reseau reseau = new Reseau("TP automates");
        for (String fichier : fichiers) {
            String chemin = "data/" + fichier;
            File fichierFile = new File(chemin);
            Scanner entree;
            try {
                entree = new Scanner(fichierFile);
                String nomFichier = fichierFile.getName(); // nom du fichier

                if (nomFichier.endsWith(".txt")) {
                    FichierTXT fichierTXT = new FichierTXT(nomFichier, chemin, entree);
                    fichierTXT.lireFichier(reseau);
                } else if (nomFichier.endsWith(".json")) {
                    FichierJSON fichierJSON = new FichierJSON(nomFichier, chemin, entree);
                    fichierJSON.lireFichier(reseau);
                } else if (nomFichier.endsWith(".xml")) {
                    FichierXML fichierXML = new FichierXML(nomFichier, chemin, entree);
                    fichierXML.lireFichier(reseau);
                } else {
                    throw new InvalideFormatException("Fichier non pris en charge. Vous devez spécifier un fichier au format TXT, JSON ou XML.");
                }
                entree.close();
            } catch (FileNotFoundException e) {
                throw new FileNotFoundException("Erreur d'entrée : le fichier n'a pas été trouvé !");
            }
        }
        // affichage du réseau instancié
        System.out.println(reseau);
    }
}
