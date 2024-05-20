package org.example.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "building")
public class Buildings {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY  )
    private long idBuilding;
    @NotNull
    private String address;
    @NotNull
    private int floors;
    @NotNull
    private int numberOfApartments;
    @OneToMany(mappedBy = "building", fetch = FetchType.EAGER)
    private Set<Apartments> apartments;

    @ManyToOne
    @JoinColumn(name = "idEmployee")
    private Employees employee;

    public Buildings(){}

    public Buildings(String address, int floors, int numberOfApartments) {
        this.address = address;
        this.floors = floors;
        this.numberOfApartments = numberOfApartments;
    }

    public long getIdBuilding() {
        return idBuilding;
    }

    public void setIdBuilding(long idBuilding) {
        this.idBuilding = idBuilding;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public int getNumberOfApartments() {
        return numberOfApartments;
    }

    public void setNumberOfApartments(int numberOfApartments) {
        this.numberOfApartments = numberOfApartments;
    }

    public Set<Apartments> getApartments() {
        return apartments;
    }

    public void setApartments(Set<Apartments> apartments) {
        this.apartments = apartments;
    }

    public void setEmployee(Employees employee) {
        this.employee = employee;
    }

    public Employees getEmployee() {
        return employee;
    }

    @Override
    public String toString() {
        return "Buildings{" +
                "idBuilding=" + idBuilding +
                ", address='" + address +
                ", floors=" + floors +
                ", numberOfApartments=" + numberOfApartments +
                ", employee=" + employee + '\'' +
                '}';
    }
}
