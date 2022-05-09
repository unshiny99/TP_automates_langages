package traitement;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FichierTXT extends Fichier {
    public FichierTXT(String nom, Scanner entree) {
        super(nom, entree);
    }

    public void lireFichier() {
        // décrire les différents patterns
        while(entree.hasNextLine()) {
            String ligne = entree.nextLine();
            System.out.println(ligne);
            Pattern p = Pattern.compile("^([A-Z]([a-z])*)(\\s([A-Z]([a-z])*))*$"); // ligne des stations (regex OK sur regex101.com)
            // pour les liaisons : ^([A-Z]([a-z])*)\s([A-Z]([a-z])*)\s[0-9]{1,}$ // OK sur le site

            Matcher m = p.matcher(ligne);
            if (Pattern.matches("^([A-Z]([a-z])*)(\s([A-Z]([a-z])*))*$",ligne)) {
                System.out.println(ligne);
            }
        }
    }
}
