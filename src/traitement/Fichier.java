package traitement;

import java.util.Scanner;

public class Fichier {
    String nom;
    Scanner entree;

    public Fichier(String nom, Scanner entree) {
        this.nom = nom;
        this.entree = entree;
    }

    @Override
    public String toString() {
        return "Fichier{" +
                "nom='" + nom + '\'' +
                '}';
    }
}
