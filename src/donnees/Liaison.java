package donnees;

public class Liaison {
    private Station stationDepart;
    private Station stationArrivee;
    private String heureDepart;
    private String heureArrivee;
    private int duree;

    public Liaison(Station stationDepart, Station stationArrivee, int duree) {
        this.stationDepart = stationDepart;
        this.stationArrivee = stationArrivee;
        this.duree = duree;
    }

    public Liaison(Station stationDepart, Station stationArrivee, String heureDepart, String heureArrivee, int duree) {
        this.stationDepart = stationDepart;
        this.stationArrivee = stationArrivee;
        this.heureDepart = heureDepart;
        this.heureArrivee = heureArrivee;
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

    public String getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(String heureDepart) {
        this.heureDepart = heureDepart;
    }

    public String getHeureArrivee() {
        return heureArrivee;
    }

    public void setHeureArrivee(String heureArrivee) {
        this.heureArrivee = heureArrivee;
    }

    @Override
    public String toString() {
        return "Liaison{" +
                "stationDepart=" + stationDepart +
                ", stationArrivee=" + stationArrivee +
                ", heureDepart='" + heureDepart + '\'' +
                ", heureArrivee='" + heureArrivee + '\'' +
                ", duree=" + duree +
                '}';
    }
}
