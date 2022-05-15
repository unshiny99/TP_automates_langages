package donnees;

public class Jonction {
    private Station stationDepart;
    private Station stationArrivee;
    private int duree;

    public Jonction(Station stationDepart, Station stationArrivee, int duree) {
        this.stationDepart = stationDepart;
        this.stationArrivee = stationArrivee;
        this.duree = duree;
    }

    public Station getStationDepart() {
        return stationDepart;
    }

    public void setStationDepart(Station stationDepart) {
        this.stationDepart = stationDepart;
    }

    public Station getStationArrivee() {
        return stationArrivee;
    }

    public void setStationArrivee(Station stationArrivee) {
        this.stationArrivee = stationArrivee;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Jonction jonction)) return false;
        return duree == jonction.duree && stationDepart.equals(jonction.stationDepart) && stationArrivee.equals(jonction.stationArrivee);
    }

    @Override
    public String toString() {
        return "Jonction{" +
                "stationDepart=" + stationDepart +
                ", stationArrivee=" + stationArrivee +
                ", duree=" + duree +
                '}';
    }
}
