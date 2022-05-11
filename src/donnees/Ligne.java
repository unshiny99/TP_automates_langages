package donnees;

import java.util.ArrayList;
import java.util.List;

public class Ligne {
    private String nom;
    private List<Station> stations;
    private List<Liaison> liaisons;

    public Ligne(String nom) {
        this.nom = nom;
        this.stations = new ArrayList<>();
        this.liaisons = new ArrayList<>();
    }

    public Ligne(String nom, List<Liaison> liaisons, List<Station> stations) {
        this.nom = nom;
        this.stations = stations;
        this.liaisons = liaisons;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Station getStation(String nomStation) {
        for (Station station : this.getStations()) {
            if (station.getNom().equals(nomStation)) {
                return station;
            }
        }
        return null;
    }

    public Station getStation(int index) {
        if (index >=0 && index < this.stations.size()) {
            return this.stations.get(index);
        }
        return null;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    public List<Liaison> getLiaisons() {
        return liaisons;
    }

    public void setLiaisons(List<Liaison> liaisons) {
        this.liaisons = liaisons;
    }

    @Override
    public String toString() {
        String ligne = "Ligne{" +
                "\nnom='" + nom + '\'' +
                ",\nstations=[\n";
        for(Station station : stations) {
            ligne += "\t" + station + "\n";
        }
        ligne += "]\nliaisons=[\n";
        for(Liaison liaison : liaisons) {
            ligne += "\t" + liaison + "\n";
        }
        ligne += "]\n}";

        return ligne;
    }
}
