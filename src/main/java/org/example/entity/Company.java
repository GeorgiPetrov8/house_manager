package org.example.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "company")
public class Company {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY  )
    private long idCompany;
    @NotNull
    private String Company_name;
    @OneToMany(mappedBy = "company")
    private Set<Employees> employees;
    private BigDecimal income;

    public Company(){}

    public Company(String company_name) {
        Company_name = company_name;
    }

    public BigDecimal getIncome() {return income;}

    public void setIncome(BigDecimal income) {this.income = income;}

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

    @Override
    public String toString() {
        return "Company{" +
                "idCompany=" + idCompany +
                ", Company_name='" + Company_name +
                ", income='" + income + '\'' +
                '}';
    }
}
