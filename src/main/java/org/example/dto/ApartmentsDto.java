package org.example.dto;

import org.example.entity.Buildings;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class ApartmentsDto {
    private long idApartments;
    private Buildings building;

    public ApartmentsDto(long idApartments, Buildings building) {
        this.idApartments = idApartments;
        this.building = building;
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
}
