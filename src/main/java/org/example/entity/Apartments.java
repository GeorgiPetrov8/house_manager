package org.example.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "apartments")
public class Apartments {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long idApartments;
    @ManyToOne
    @JoinColumn(name = "idBuilding")
    private Buildings building;
    @NotNull
    private int floor_number;
    @NotNull
    private double area;
    @NotNull
    private String owner;
    @OneToMany(mappedBy = "apartments",fetch = FetchType.EAGER)
    private Set<Residents> residents;
    @OneToMany(mappedBy = "apartments")
    private Set<Fees> fees;

    public Apartments(){}

    public Apartments(Buildings building, int floor_number, double area, String owner) {
        this.building = building;
        this.floor_number = floor_number;
        this.area = area;
        this.owner = owner;
    }

    public long getIdApartments() {
        return idApartments;
    }

    public void setIdApartments(long idApartments) {
        this.idApartments = idApartments;
    }

    public Buildings getBuilding() {
        return building;
    }

    public void setBuilding(Buildings building) {
        this.building = building;
    }

    public int getFloor_number() {
        return floor_number;
    }

    public void setFloor_number(int floor_number) {
        this.floor_number = floor_number;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Set<Residents> getResidents() {
        return residents;
    }

    public void setResidents(Set<Residents> residents) {
        this.residents = residents;
    }

    public Set<Fees> getFees() { return fees; }

    public void setFees(Set<Fees> fees) {this.fees = fees;}

    @Override
    public String toString() {
        return "Apartments{" +
                "idApartments=" + idApartments +
                ", building=" + building +
                ", floor_number=" + floor_number +
                ", area=" + area +
                ", owner='" + owner + '\'' +
                '}';
    }
}
