package org.example.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "residents")
public class Residents {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idResident;
    @ManyToOne
    @JoinColumn(name = "idApartments")
    private Apartments apartments;
    @NotNull
    private String name;
    @NotNull
    private boolean uses_elevator;
    @NotNull
    private boolean Has_pet_uses_common_areas;
    @NotNull
    private int age;

    public Residents(){}

    public Residents(Apartments apartments, String name, boolean uses_elevator, boolean Has_pet_uses_common_areas, int age) {
        this.apartments = apartments;
        this.name = name;
        this.uses_elevator = uses_elevator;
        this.Has_pet_uses_common_areas = Has_pet_uses_common_areas;
        this.age = age;
    }

    public long getIdResident() {
        return idResident;
    }

    public void setIdResident(long idResident) {
        this.idResident = idResident;
    }

    public Apartments getApartment() {
        return apartments;
    }

    public void setApartment(Apartments apartments) {
        this.apartments = apartments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUses_elevator() {
        return uses_elevator;
    }

    public void setUses_elevator(boolean uses_elevator) {
        this.uses_elevator = uses_elevator;
    }

    public boolean isHas_pet_uses_common_areas() {
        return Has_pet_uses_common_areas;
    }

    public void setHas_pet_uses_common_areas(boolean Has_pet_uses_common_areas) {
        this.Has_pet_uses_common_areas = Has_pet_uses_common_areas;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Residents{" +
                "idResident=" + idResident +
                ", apartment=" + apartments +
                ", name='" + name +
                ", uses_elevator=" + uses_elevator +
                ", has_pet=" + Has_pet_uses_common_areas +
                ", has_pet=" + age + '\'' +
                '}';
    }
}
