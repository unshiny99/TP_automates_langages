package traitement;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FonctionPrincipale extends Throwable {

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                String chemin = args[0];
                File fichier = new File(chemin);
                Scanner entree = new Scanner(fichier);
                //System.out.println(chemin);
                String nomFichier = fichier.getName(); // nom du fichier

                if (nomFichier.endsWith(".txt")) {
                    FichierTXT fichierTXT = new FichierTXT(nomFichier, entree);
                    fichierTXT.lireFichier();
                } else if (nomFichier.endsWith(".json")) {
                    FichierJSON fichierJSON = new FichierJSON(nomFichier, entree);
                    fichierJSON.lireFichier();
                } else if (nomFichier.endsWith(".xml")) {
                    System.out.println("fichier XML !");
                } else {
                    System.out.println("Fichier non pris en charge. Vous devez spécifier un fichier au format TXT, JSON ou XML.");
                }
                entree.close();
            }
        } catch (FileNotFoundException e) { // arrêt du programme si fichier inexistant
            System.out.println(e.getMessage());
        }
    }
}
