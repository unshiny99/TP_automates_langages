package traitement;

import java.util.Scanner;

public class Fichier {
    String nom;
    String chemin;
    Scanner entree;

    public Fichier(String nom, String chemin, Scanner entree) {
        this.nom = nom;
        this.chemin = chemin;
        this.entree = entree;
    }

    @Override
    public String toString() {
        return "Fichier{" +
                "nom='" + nom + '\'' +
                ", chemin='" + chemin + '\'' +
                ", entree=" + entree +
                '}';
    }
}
