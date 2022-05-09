import traitement.FichierTXT;

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
                System.out.println(chemin);
                String nomFichier = fichier.getName(); // nom du fichier

                if (nomFichier.endsWith(".txt")) {
                    FichierTXT fichierTXT = new FichierTXT(nomFichier, entree);
                    //System.out.println(fichierTXT);
                    fichierTXT.lireFichier();
                } else if (nomFichier.endsWith(".json")) {
                    System.out.println("fichier JSON !");
                } else if (nomFichier.endsWith(".xml")) {
                    System.out.println("fichier XML !");
                } else {
                    System.out.println("Fichier non pris en charge.");
                }
                entree.close();
            }
        } catch (FileNotFoundException e) { // arrêt du programme si fichier inexistant
            System.out.println(e.getMessage());
        }
    }
}
