package donnees;

import java.util.List;

public class Reseau {
    private String nom;
    private List<Exploitant> exploitants;

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

    public static void main(String[] args) {

    }
}
