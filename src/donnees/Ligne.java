package donnees;

import java.util.List;

public class Ligne {
    private String nom;
    private String type;
    private String description;
    private List<Liaison> liaisons;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Liaison> getLiaisons() {
        return liaisons;
    }

    public void setLiaisons(List<Liaison> liaisons) {
        this.liaisons = liaisons;
    }
}
