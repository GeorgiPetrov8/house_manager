package org.example.entity;

import com.sun.istack.NotNull;
import org.intellij.lang.annotations.Pattern;

import javax.persistence.*;
import javax.persistence.criteria.Fetch;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table (name = "employees")
public class Employees {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idEmployee;
    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
    private Set<Buildings> buildings;
    @NotNull
    private String name;
    @NotNull
    private int numberOfBuildings;
    @ManyToOne
    @JoinColumn(name = "idCompany")
    private Company company;

    public Employees(){}

    public Employees(String name, int numberOfBuildings, Company company) {
        this.name = name;
        this.numberOfBuildings = numberOfBuildings;
        this.company = company;
    }

    public long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public Set<Buildings> getBuildings() {
        return buildings;
    }

    public void setBuildings(Buildings building) {
        this.buildings.add(building);
        this.numberOfBuildings++;
    }

    public void removeAllBuilding() {
        this.buildings.clear();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfBuildings() {
        return numberOfBuildings;
    }

    public void setNumberOfBuildings(int numberOfBuildings) {
        this.numberOfBuildings = numberOfBuildings;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employees employees = (Employees) o;
        return idEmployee == employees.idEmployee;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmployee);
    }

    @Override
    public String toString() {
        return "Employees{" +
                "idEmployee=" + idEmployee +
                ", name='" + name +
                ", numberOfBuildings=" + numberOfBuildings +
                ", company=" + company + '\'' +
                '}';
    }

}
