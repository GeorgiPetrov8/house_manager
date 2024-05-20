package org.example.dto;

import org.example.entity.Buildings;
import org.example.entity.Company;

import java.util.Set;

public class EmployeeDto {
    private long employeeId;
    private String employeeName;
    private int totalBuildings;

    public EmployeeDto(long employeeId, String employeeName, int totalBuildings) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.totalBuildings = totalBuildings;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public long getTotalBuildings() {
        return totalBuildings;
    }

    public void setTotalBuildings(int totalBuildings) {
        this.totalBuildings = totalBuildings;
    }
}
