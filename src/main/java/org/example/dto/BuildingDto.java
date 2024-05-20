package org.example.dto;

import com.sun.istack.NotNull;
import org.example.entity.Apartments;
import org.example.entity.Employees;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

public class BuildingDto {
    private Employees employee;

    public BuildingDto( Employees employee) {
        this.employee = employee;
    }

    public Employees getEmployee() {
        return employee;
    }
}
