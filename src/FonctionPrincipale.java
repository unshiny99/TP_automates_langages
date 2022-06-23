import donnees.Reseau;
import traitement.FichierJSON;
import traitement.FichierTXT;
import traitement.FichierXML;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FonctionPrincipale extends Throwable {

    public static void main(String[] args) {
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
                    System.out.println("Fichier non pris en charge. Vous devez spécifier un fichier au format TXT, JSON ou XML.");
                }
                entree.close();
            } catch (FileNotFoundException e) {
                System.out.println("Erreur d'entrée : le fichier n'a pas été trouvé !");
                //throw new RuntimeException(e);
            }
        }
        System.out.println(reseau);
    }
}
