package org.example.dto;

import org.example.entity.Employees;

import java.util.Set;

public class CompanyDTO {
    private long idCompany;
    private String Company_name;
    private Set<Employees> employees;

    public CompanyDTO(long idCompany, String company_name) {
        this.idCompany = idCompany;
        Company_name = company_name;
    }

    public long getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(long idCompany) {
        this.idCompany = idCompany;
    }

    public String getCompany_name() {
        return Company_name;
    }

    public void setCompany_name(String company_name) {
        Company_name = company_name;
    }

    public Set<Employees> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employees> employees) {
        this.employees = employees;
    }

}
