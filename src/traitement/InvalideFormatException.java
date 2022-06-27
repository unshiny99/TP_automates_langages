package traitement;

import java.time.LocalTime;

public class InvalideFormatException extends Exception {
    public InvalideFormatException() {
    }

    public InvalideFormatException(String message) {
        super(message);
    }

    static void validerHeureUnique(String heureTexte) throws InvalideFormatException {
        if(!heureTexte.matches("(([0-1][0-9]|2[0-3])[0-5][0-9])")){
            // throw an object of user defined exception
            throw new InvalideFormatException("L'heure n'est pas au bon format !");
        }
    }

    static void validerHeureMultiple(String heureTexte) throws InvalideFormatException {
        if(!heureTexte.matches("((([0-1][0-9]|2[0-3])[0-5][0-9])\\s+)*((([0-1][0-9]|2[0-3])[0-5][0-9])\\s*)")){
            // throw an object of user defined exception
            throw new InvalideFormatException("La liste d'heures n'est pas au bon format !");
        }
    }

    static void validerNomUnique(String nom) throws InvalideFormatException {
        if(!nom.matches("[A-Za-zÀ-ÿ]*\\s*")){
            throw new InvalideFormatException("Le nom n'est pas au bon format !");
        }
    }

    static void validerNomMultiple(String nom) throws InvalideFormatException {
        if(!nom.matches("([A-Za-zÀ-ÿ]*\\s+)*([A-Za-zÀ-ÿ]*\\s*)")){
            throw new InvalideFormatException("La liste de noms n'est pas au bon format !");
        }
    }

    static void validerTransportTXT(String nom) throws InvalideFormatException {
        System.out.println(nom);
        if(!nom.matches("% métro") || !nom.matches("% Car Inter-Cité")){
            throw new InvalideFormatException("Le type de transport est invalide dans le fichier texte (l.1)");
        }
    }
}
