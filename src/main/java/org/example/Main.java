package org.example;

import org.example.Enum.StatusType;
import org.example.Schedual.FeeScheduler;
import org.example.configuration.SessionFactoryUtil;
import org.example.dao.*;
import org.example.dto.EmployeeDto;
import org.example.entity.*;
import org.hibernate.SessionFactory;
import org.hibernate.type.EnumType;
import org.hibernate.type.TrueFalseType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.example.dao.ApartmentsDAO.getApartments;
import static org.example.dao.BuildingsDAO.getBuildingApartments;
import static org.example.dao.BuildingsDAO.getBuildings;
import static org.example.dao.CompanyDAO.*;
import static org.example.dao.CompanyDAO.getCompanies;
import static org.example.dao.EmployeesDAO.*;
import static org.example.dao.FeesDAO.*;
import static org.example.dao.FeesDAO.getNumberOfFeesEmployeesList;
import static org.example.dao.ResidentsDAO.*;

public class Main {
    public static void main(String[] args) {
        Company company = new Company("nestle");
        Company company1 = new Company("dsa");
        Company company2 = new Company("asd");

        // Create company
        CompanyDAO.saveCompany(company);
        CompanyDAO.saveCompany(company1);
        CompanyDAO.saveCompany(company2);

        // Get company by id
        //Company company5 = CompanyDAO.getCompanyById(1);
        //System.out.println(company5);

        // Get companies
        //CompanyDAO.getCompanies().stream().forEach(System.out::println);

        // Update company with id = 1
        // company.setId(1);
        // CompanyDao.updateCompany(company);

        // Delete Company with id 5
        // CompanyDao.deleteCompany(company5);

        Employees employee = new Employees("John", 0, company);
        Employees employee1 = new Employees("Boan", 0, company1);
        Employees employee2 = new Employees("Asa", 0, company);
        Employees employee3 = new Employees("Mana", 0, company2);

        EmployeesDAO.saveEmployee(employee);
        EmployeesDAO.saveEmployee(employee1);
        EmployeesDAO.saveEmployee(employee2);
        EmployeesDAO.saveEmployee(employee3);

        //String address, int floors, int numberOfApartments
        Buildings building = new Buildings("123", 2, 10);
        Buildings building1 = new Buildings("asd", 3, 20);
        Buildings building2 = new Buildings("gfd", 3, 20);
        Buildings building3 = new Buildings("1243", 2, 20);
        Buildings building4 = new Buildings("45", 2, 10);


        BuildingsDAO.saveBuilding(building);
        BuildingsDAO.saveBuilding(building1);
        BuildingsDAO.saveBuilding(building2);
        BuildingsDAO.saveBuilding(building3);
        BuildingsDAO.saveBuilding(building4);

        //Employees employees123 = EmployeesDAO.getEmployeeById(1);
        //System.out.println(employees123);

        EmployeesDAO.updateEmployee(CompanyDAO.distributeBuildingToEmployee(building, 1));
        EmployeesDAO.updateEmployee(CompanyDAO.distributeBuildingToEmployee(building1, 1));
        EmployeesDAO.updateEmployee(CompanyDAO.distributeBuildingToEmployee(building2, 2));
        EmployeesDAO.updateEmployee(CompanyDAO.distributeBuildingToEmployee(building3, 1));
        EmployeesDAO.updateEmployee(CompanyDAO.distributeBuildingToEmployee(building4, 3));

        EmployeesDAO.deleteEmployee(employee.getIdEmployee());

        Apartments apartments = new Apartments(building,1, 22.0, "Pesho");
        Apartments apartments1 = new Apartments(building,1, 22.0, "Misha");
        Apartments apartments2 = new Apartments(building2,1, 25.0, "Tosho");
        Apartments apartments3 = new Apartments(building4,1, 23.0, "Ana");
        Apartments apartments4 = new Apartments(building4,1, 23.0, "Bob");

        ApartmentsDAO.saveApartment(apartments);
        ApartmentsDAO.saveApartment(apartments1);
        ApartmentsDAO.saveApartment(apartments2);
        ApartmentsDAO.saveApartment(apartments3);
        ApartmentsDAO.saveApartment(apartments4);

        Residents residents = new Residents(apartments, "Pesho", false,false, 20);
        Residents residents1 = new Residents(apartments, "Pepa", true,true, 7);
        Residents residents2 = new Residents(apartments2, "Tosho", false,false, 20);
        Residents residents3 = new Residents(apartments3, "Ana", false,false, 20);
        Residents residents4 = new Residents(apartments4, "Bob", false,false, 20);

        ResidentsDAO.saveResident(residents);
        ResidentsDAO.saveResident(residents1);
        ResidentsDAO.saveResident(residents2);
        ResidentsDAO.saveResident(residents3);
        ResidentsDAO.saveResident(residents4);

        Fees fees = new Fees(apartments, new BigDecimal(10.0), new BigDecimal(5.0), new BigDecimal(3.0), StatusType.NOT_PAID);
        Fees fees1 = new Fees(apartments2, new BigDecimal(10.0), new BigDecimal(5.0), new BigDecimal(3.0), StatusType.NOT_PAID);
        Fees fees2 = new Fees(apartments3, new BigDecimal(10.0), new BigDecimal(5.0), new BigDecimal(3.0), StatusType.NOT_PAID);
        Fees fees3 = new Fees(apartments4, new BigDecimal(10.0), new BigDecimal(5.0), new BigDecimal(3.0), StatusType.NOT_PAID);

        FeesDAO.saveFee(fees);
        FeesDAO.saveFee(fees1);
        FeesDAO.saveFee(fees2);
        FeesDAO.saveFee(fees3);

        System.out.println(FeesDAO.calculateFee(apartments, fees));
        System.out.println(FeesDAO.calculateFee(apartments2, fees1));
        System.out.println(FeesDAO.calculateFee(apartments3, fees2));

        FeesDAO.payFees(apartments);
        FeesDAO.payFees(apartments2);
        FeesDAO.payFees(apartments3);

        FeeScheduler feeScheduler = new FeeScheduler();
        feeScheduler.scheduleMonthlyFeeCreation(apartments);
        feeScheduler.shutdownScheduler();

        FeeScheduler feeScheduler1 = new FeeScheduler();
        feeScheduler1.scheduleMonthlyFeeCreation(apartments2);
        feeScheduler1.shutdownScheduler();

        FeeScheduler feeScheduler2 = new FeeScheduler();
        feeScheduler2.scheduleMonthlyFeeCreation(apartments3);
        feeScheduler2.shutdownScheduler();

        System.out.println("\nSort by Income");
        List<Company> sortedCompaniesByRevenue = getCompaniesSortedByRevenue();
        for (Company c : sortedCompaniesByRevenue) {
            System.out.println("Company: " + c.getCompany_name() + ", Revenue: " + c.getIncome());
        }

        System.out.println("\nFilter by Income");
        List<Company> companiesFindByInitialCapitalBetween = companiesFindByInitialCapitalBetween(230, 250);
        for (Company c : companiesFindByInitialCapitalBetween) {
            System.out.println("Company: " + c.getCompany_name() + ", Revenue: " + c.getIncome());
        }

        System.out.println("\nSort by Buildings");
        List<Employees> getSortedEmployeesByBuildings = getSortedEmployeesByBuildings();
        for (Employees e : getSortedEmployeesByBuildings) {
            System.out.println("Employee: " + e.getName() + ", Buildings: " + e.getNumberOfBuildings());
        }

        System.out.println("\nFilter by Buildings");
        List<Employees> employeesFindByBuildingsBetween = employeesFindByBuildingsBetween(2,3);
        for (Employees e : employeesFindByBuildingsBetween) {
            System.out.println("Employee: " + e.getName() + ", Buildings: " + e.getNumberOfBuildings());
        }

        System.out.println("\nSort by Name Employees");
        List<Employees> getSortedEmployeesByName = getSortedEmployeesByName();
        for (Employees e : getSortedEmployeesByName) {
            System.out.println("Employee: " + e.getName());
        }

        System.out.println("\nFilter by Name");
        List<Employees> getEmployeesByName = getEmployeesFilteredByName("Asa");
        for (Employees e : getEmployeesByName) {
            System.out.println("Employee: " + e.getName());
        }

        System.out.println("\nSort by Name Residents");
        List<Residents> getSortedResidentsByName = getSortedResidentsByName();
        for (Residents r : getSortedResidentsByName) {
            System.out.println("Residents: " + r.getName());
        }

        System.out.println("\nFilter by Name");
        List<Residents> getResidentsFilteredByName = getResidentsFilteredByName("Pepa");
        for (Residents r : getResidentsFilteredByName) {
            System.out.println("Residents: " + r.getName());
        }

            System.out.println("\nSort by Age");
        List<Residents> getSortedResidentsByAge = getSortedResidentsByAge();
        for (Residents r : getSortedResidentsByAge) {
            System.out.println("Residents: " + r.getName() + ", Age: " + r.getAge());
        }

        System.out.println("\nFilter by Age");
        List<Residents> residentsFindByAgeBetween = residentsFindByAgeBetween(10,20);
        for (Residents r : residentsFindByAgeBetween) {
            System.out.println("Residents: " + r.getName() + ", Age: " + r.getAge());
        }

        System.out.println("\nEmployees and Buildings By Company ID DTO");
        List<EmployeeDto> getEmployeesBuildingsDTO = getEmployeesBuildingsDTO(company1.getIdCompany());
        for (EmployeeDto e : getEmployeesBuildingsDTO) {
            System.out.println("Employee: " + e.getEmployeeName() + ", Buildings: " + e.getTotalBuildings());
        }

        System.out.println("\nList of Employees");
        Map<Set<Employees>,Set<Buildings>> getBuildingEmployees = getBuildingEmployees();
        for (Map.Entry<Set<Employees>, Set<Buildings>> entry : getBuildingEmployees.entrySet()) {
            Set<Employees> en = entry.getKey();
            Set<Buildings> bil = entry.getValue();

            for (Employees e : en) {
                System.out.println("Employee ID: " + e.getIdEmployee() + ", Name: " + e.getName() + ", Company: " + e.getCompany().getCompany_name());
            }
            for (Buildings b : bil) {
                System.out.println("Building ID: " + b.getIdBuilding() + ", Apartments: " + b.getNumberOfApartments());
            }
            System.out.println("\n");
        }

        System.out.println("\nBuildings and Apartments");
        List<Buildings> getBuildings = getBuildings();
        for (Buildings b : getBuildings) {
            System.out.println("Building ID: " + b.getIdBuilding() + ", Apartments: " + b.getApartments().size());
        }

        System.out.println("\nList of Buildings");
        Map<Set<Buildings>,Set<Apartments>> getBuildingApartments = getBuildingApartments();
        for (Map.Entry<Set<Buildings>, Set<Apartments>> entry : getBuildingApartments.entrySet()) {
            Set<Buildings> bil = entry.getKey();
            Set<Apartments> ap = entry.getValue();

            for (Buildings b : bil) {
                System.out.println("Building ID: " + b.getIdBuilding() + ", Apartments: " + b.getNumberOfApartments() + ", Address: " + b.getAddress());
            }
            for (Apartments a : ap) {
                System.out.println("Apartments ID: " + a.getIdApartments() + ", Owner: " + a.getOwner());
            }
            System.out.println("\n");
        }

        System.out.println("\nApartments and Residents");
        List<Apartments> getApartments = getApartments();
        for (Apartments a : getApartments) {
            System.out.println("Apartments ID: " + a.getIdApartments() + ", Resident: " + a.getResidents().size());
        }

        System.out.println("\nList of Residents");
        Map<Set<Apartments>, Set<Residents>> getApartmentsResidents = getApartmentsResidents();
        for (Map.Entry<Set<Apartments>, Set<Residents>> entry : getApartmentsResidents.entrySet()) {
            Set<Apartments> ap = entry.getKey();
            Set<Residents> res = entry.getValue();

            for (Apartments a : ap) {
                System.out.println("Apartments ID: " + a.getIdApartments() + ", Owner: " + a.getOwner());
            }
            for (Residents r : res) {
                System.out.println("Residents Name: " + r.getName() + ", Age: " + r.getAge());
            }
            System.out.println("\n");
        }

        System.out.println("\nPayments For Companies With Paid Fees");
        Map<Company, BigDecimal> getNumberOfFees = getNumberOfFees();
        for (Map.Entry<Company, BigDecimal> entry : getNumberOfFees.entrySet()) {
            Company com = entry.getKey();
            BigDecimal num = entry.getValue();

            System.out.println("Company: " + com.getCompany_name() + ", Number of Fees: " + num);
        }

        System.out.println("\nPayments For Buildings With Paid Fees");
        Map<Buildings, BigDecimal> getNumberOfFeesBuildings = getNumberOfFeesBuildings();
        for (Map.Entry<Buildings, BigDecimal> entry : getNumberOfFeesBuildings.entrySet()) {
            Buildings buil = entry.getKey();
            BigDecimal num = entry.getValue();

            System.out.println("Building Id: " + buil.getIdBuilding() + ", Number of Fees: " + num);
        }

        System.out.println("\nPayments For Employees With Paid Fees");
        Map<Employees, BigDecimal> getNumberOfFeesEmployees = getNumberOfFeesEmployees();
        for (Map.Entry<Employees, BigDecimal> entry : getNumberOfFeesEmployees.entrySet()) {
            Employees emp = entry.getKey();
            BigDecimal num = entry.getValue();

            System.out.println("Employees: " + emp.getName() + ", Number of Fees: " + num);
        }

        System.out.println("\nPayments For Companies List With Paid Fees");
        Map<Company, Fees> getNumberOfFeesList = getNumberOfFeesList();
        for (Map.Entry<Company, Fees> entry : getNumberOfFeesList.entrySet()) {
            Company com = entry.getKey();
            Fees fe = entry.getValue();

            System.out.println("Company: " + com.getCompany_name() + ", Fees ID: " + fe.getIdFee());
        }

        System.out.println("\nPayments For Buildings List With Paid Fees");
        Map<Buildings, Fees> getNumberOfFeesBuildingsList = getNumberOfFeesBuildingsList();
        for (Map.Entry<Buildings, Fees> entry : getNumberOfFeesBuildingsList.entrySet()) {
            Buildings buil = entry.getKey();
            Fees fe = entry.getValue();

            System.out.println("Building Id: " + buil.getIdBuilding() + ", Fees ID: " + fe.getIdFee());
        }

        System.out.println("\nPayments For Employees List With Paid Fees");
        Map<Employees, Fees> getNumberOfFeesEmployeesList = getNumberOfFeesEmployeesList();
        for (Map.Entry<Employees, Fees> entry : getNumberOfFeesEmployeesList.entrySet()) {
            Employees emp = entry.getKey();
            Fees fe = entry.getValue();

            System.out.println("Employees: " + emp.getName() + ", Fees ID: " + fe.getIdFee());
        }

        System.out.println("\nCompanies With NonPaid Fees");
        Map<Company, BigDecimal> getNumberOfFeesForNonPaid = getNumberOfFeesForNonPaid();
        for (Map.Entry<Company, BigDecimal> entry : getNumberOfFeesForNonPaid.entrySet()) {
            Company com = entry.getKey();
            BigDecimal num = entry.getValue();

            System.out.println("Company: " + com.getCompany_name() + ", Number of Fees: " + num);
        }

        System.out.println("\nBuildings With NonPaid Fees");
        Map<Buildings, BigDecimal> getNumberOfFeesBuildingsForNonPaid = getNumberOfFeesBuildingsForNonPaid();
        for (Map.Entry<Buildings, BigDecimal> entry : getNumberOfFeesBuildingsForNonPaid.entrySet()) {
            Buildings buil = entry.getKey();
            BigDecimal num = entry.getValue();

            System.out.println("Building Id: " + buil.getIdBuilding() + ", Number of Fees: " + num);
        }

        System.out.println("\nEmployees With NonPaid Fees");
        Map<Employees, BigDecimal> getNumberOfFeesEmployeesForNonPaid = getNumberOfFeesEmployeesForNonPaid();
        for (Map.Entry<Employees, BigDecimal> entry : getNumberOfFeesEmployeesForNonPaid.entrySet()) {
            Employees emp = entry.getKey();
            BigDecimal num = entry.getValue();

            System.out.println("Employees: " + emp.getName() + ", Number of Fees: " + num);
        }

        System.out.println("\nCompanies List With NonPaid Fees");
        Map<Company, Fees> getNumberOfFeesListForNonPaid = getNumberOfFeesListForNonPaid();
        for (Map.Entry<Company, Fees> entry : getNumberOfFeesListForNonPaid.entrySet()) {
            Company com = entry.getKey();
            Fees fe = entry.getValue();

            System.out.println("Company: " + com.getCompany_name() + ", Fees ID: " + fe.getIdFee());
        }

        System.out.println("\nBuildings List With NonPaid Fees");
        Map<Buildings, Fees> getNumberOfFeesBuildingsListForNonPaid = getNumberOfFeesBuildingsListForNonPaid();
        for (Map.Entry<Buildings, Fees> entry : getNumberOfFeesBuildingsListForNonPaid.entrySet()) {
            Buildings buil = entry.getKey();
            Fees fe = entry.getValue();

            System.out.println("Building Id: " + buil.getIdBuilding() + ", Fees ID: " + fe.getIdFee());
        }

        System.out.println("\nEmployees List With NonPaid Fees");
        Map<Employees, Fees> getNumberOfFeesEmployeesListForNonPaid = getNumberOfFeesEmployeesListForNonPaid();
        for (Map.Entry<Employees, Fees> entry : getNumberOfFeesEmployeesListForNonPaid.entrySet()) {
            Employees emp = entry.getKey();
            Fees fe = entry.getValue();

            System.out.println("Employees: " + emp.getName() + ", Fees ID: " + fe.getIdFee());
        }
    }
}