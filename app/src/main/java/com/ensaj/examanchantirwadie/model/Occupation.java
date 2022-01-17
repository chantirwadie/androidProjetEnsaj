package com.ensaj.examanchantirwadie.model;

public class Occupation {
    String id;
  SalleO salle;
  Chrono chrono;
  String dateResrvation;

    public Occupation(String id, SalleO salle, Chrono chrono) {
        this.id = id;
        this.salle = salle;
        this.chrono = chrono;
    }

    public Occupation(String id, SalleO salle, Chrono chrono, String dateResrvation) {
        this.id = id;
        this.salle = salle;
        this.chrono = chrono;
        this.dateResrvation = dateResrvation;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SalleO getSalle() {
        return salle;
    }

    public void setSalle(SalleO salle) {
        this.salle = salle;
    }

    public Chrono getChrono() {
        return chrono;
    }

    public void setChrono(Chrono chrono) {
        this.chrono = chrono;
    }

    public String getDateResrvation() {
        return dateResrvation;
    }

    public void setDateResrvation(String dateResrvation) {
        this.dateResrvation = dateResrvation;
    }

    @Override
    public String toString() {
        return "Occupation{" +
                "id='" + id + '\'' +
                ", salle=" + salle +
                ", chrono=" + chrono +
                ", dateResrvation='" + dateResrvation + '\'' +
                '}';
    }
}
