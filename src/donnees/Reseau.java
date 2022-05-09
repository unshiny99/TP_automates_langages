package donnees;

import java.util.ArrayList;
import java.util.List;

public class Reseau {
    private String nom;
    private List<Exploitant> exploitants;

    public Reseau(String nom) {
        this.nom = nom;
        this.exploitants = new ArrayList<>();
    }

    public Reseau(String nom, List<Exploitant> exploitants) {
        this.nom = nom;
        this.exploitants = exploitants;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Exploitant> getExploitants() {
        return exploitants;
    }

    public void setExploitants(List<Exploitant> exploitants) {
        this.exploitants = exploitants;
    }

    @Override
    public String toString() {
        return "Reseau{" +
                "nom='" + nom + '\'' +
                ", exploitants=" + exploitants +
                '}';
    }

    public static void main(String[] args) {
        Reseau reseau = new Reseau("Métropole Européenne de Lille");
        System.out.println(reseau);
    }
}
