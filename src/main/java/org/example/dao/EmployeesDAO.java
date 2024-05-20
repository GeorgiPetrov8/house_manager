package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.EmployeeDto;
import org.example.entity.Buildings;
import org.example.entity.Company;
import org.example.entity.Employees;
import org.example.entity.Residents;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

public class EmployeesDAO {

/**
 * Saves an employee entity to the database.
 *
 * @param employee The Employees object representing the employee to be saved.
 *
 */
    public static void saveEmployee(Employees employee){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(employee);
            transaction.commit();
        }
    }

/**
 * Retrieves an employee entity from the database based on the provided ID.
 *
 * @param id The ID of the employee to retrieve.
 * @return An Employees object representing the employee with the specified ID, or null if not found.
 *
 */
    public static Employees getEmployeeById (long id){

        Employees employee;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            employee = session.get(Employees.class, id);
            transaction.commit();
        }
        return employee;
    }

/**
 * Retrieves a list of all employees from the database.
 *
 * @return A list containing Employees objects representing all employees stored in the database.
 *
 */
    public static List<Employees> getEmployees(){
        List<Employees> employees;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            employees = session.createQuery("Select e From Employees e", Employees.class).getResultList();
            transaction.commit();
        }
        return employees;
    }

/**
 * Updates an existing employee entity in the database.
 *
 * @param employee The Employees object representing the employee to be updated.
 *
 */
    public static void updateEmployee(Employees employee){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.update(employee);
            transaction.commit();
        }
    }

/**
 * Deletes an employee from the database by their ID.
 * Additionally, redistributes buildings and updates affected employees after deletion.
 *
 * @param id The ID of the employee to be deleted.
 *
 */
public static void deleteEmployee(long id){

        Employees employee = EmployeesDAO.getEmployeeById(id);

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();

            Set<Employees> updatedEmployees = reDistributeBuildingToEmployee(employee.getBuildings(), employee.getCompany().getIdCompany(), employee);

            for(Employees updating : updatedEmployees){ updateEmployee(updating);}
            System.out.println();
            session.delete(employee);
            transaction.commit();
        }
    }

    /**
     * Redistributes buildings among employees after removing an employee and updates the affected employees.
     *
     * @param buildings        The set of buildings previously assigned to the deleted employee.
     * @param id               The ID of the company to which the employees belong.
     * @param deletedEmployee  The employee that has been removed and needs redistribution.
     * @return A set containing updated employees after redistributing buildings.
     *
     */
    public static Set<Employees> reDistributeBuildingToEmployee(Set<Buildings> buildings, long id, Employees deletedEmployee){
        Set<Employees> updatedEmployees = new HashSet<>();
        Set<Employees> allEmployees = CompanyDAO.getCompanyEmployees(id);

        if(allEmployees.contains(deletedEmployee)) {
            allEmployees.remove(deletedEmployee);
        }

        for(Buildings newBuilding : buildings){

            Employees leastAssignedEmployee = null;
            int minBuildingsAssigned = Integer.MAX_VALUE;

            for (Employees employee : allEmployees) {
                if (employee.getNumberOfBuildings() < minBuildingsAssigned && !employee.equals(deletedEmployee)) {
                    minBuildingsAssigned = employee.getNumberOfBuildings();
                    leastAssignedEmployee = employee;
                }
            }

            if (leastAssignedEmployee != null) {
                leastAssignedEmployee.setBuildings(newBuilding);

                newBuilding.setEmployee(leastAssignedEmployee);
                BuildingsDAO.updateBuilding(newBuilding);

                if(allEmployees.contains(leastAssignedEmployee)) {
                    allEmployees.remove(leastAssignedEmployee);
                    allEmployees.add(leastAssignedEmployee);
                }
                else {
                    allEmployees.add(leastAssignedEmployee);
                }
            }
    }
        return allEmployees;
    }

/**
 * Retrieves a list of employees from the database and sorts them based on the number of buildings each employee is assigned to.
 *
 * @return A list of Employees objects sorted in descending order based on the number of buildings assigned.
 *
 */
    public static List<Employees> getSortedEmployeesByBuildings() {
        List<Employees> employees;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            employees = session.createQuery("Select e From Employees e", Employees.class).getResultList();
            transaction.commit();
        }

        employees.sort(Comparator.comparingInt(employee -> -employee.getNumberOfBuildings()));

        return employees;
    }

/**
 * Finds employees whose number of buildings falls within a specified range.
 *
 * @param bottom The lower bound of the range for the number of buildings.
 * @param top    The upper bound of the range for the number of buildings.
 * @return A list of Employees objects whose number of buildings falls within the specified range.
 *
 */
    public static List<Employees> employeesFindByBuildingsBetween(double bottom, double top) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Employees> cr = cb.createQuery(Employees.class);
            Root<Employees> root = cr.from(Employees.class);

            cr.select(root).where(cb.between(root.get("numberOfBuildings"), bottom, top));

            Query<Employees> query = session.createQuery(cr);
            List<Employees> employees = query.getResultList();
            return employees;
        }
    }

/**
 * Retrieves a list of employees from the database and sorts them alphabetically by name.
 *
 * @return A list of Employees objects sorted alphabetically by name.
 *
 */
    public static List<Employees> getSortedEmployeesByName() {
        List<Employees> employees;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            employees = session.createQuery("Select e From Employees e", Employees.class).getResultList();
            transaction.commit();
        }

        employees.sort(Comparator.comparing(Employees::getName));

        return employees;
    }

/**
 * Retrieves a list of employees from the database and filters them by a specified name.
 *
 * @param name The name used for filtering employees.
 * @return A list of Employees objects filtered by the specified name.
 *
 */
    public static List<Employees> getEmployeesFilteredByName(String name) {
        List<Employees> employees;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            employees = session.createQuery("Select e From Employees e", Employees.class).getResultList();
            transaction.commit();
        }

        employees = employees.stream().filter(employee -> employee.getName().equals(name)).collect(Collectors.toList());

        return employees;
    }

/**
 * Retrieves a mapping between sets of employees and sets of buildings where each employee is associated with the buildings they are linked to.
 *
 * @return A mapping (Map) of sets of Employees to sets of Buildings.
 *
 */
    public static Map<Set<Employees>, Set<Buildings>> getBuildingEmployees() {
        Map<Set<Employees>, Set<Buildings>> employeesBuildingsMap = new HashMap<>();

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            List<Object[]> resultList = session.createQuery(
                            "select e, b from Employees e join e.buildings b", Object[].class)
                    .getResultList();

            for (Object[] row : resultList) {
                Employees employee = (Employees) row[0];
                Buildings building = (Buildings) row[1];

                Set<Employees> employeesSet = new HashSet<>();
                employeesSet.add(employee);

                Set<Buildings> buildingsSet = employeesBuildingsMap.getOrDefault(employeesSet, new HashSet<>());
                buildingsSet.add(building);

                employeesBuildingsMap.put(employeesSet, buildingsSet);
            }
            transaction.commit();
        }
        return employeesBuildingsMap;
    }

    /**
     * Retrieves a list of Employee DTOs representing employees and the number of buildings they are associated with in a specific company.
     *
     * @param id The identifier of the company to retrieve employee details for.
     * @return A list of EmployeeDto objects containing details of employees and the count of buildings associated with each employee in the specified company.
     *
     */
    public static List<EmployeeDto> getEmployeesBuildingsDTO(long id) {
        List<EmployeeDto> employees;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            employees = session.createQuery(
                        "select new org.example.dto.EmployeeDto(e.id, e.name, e.numberOfBuildings) from Employees e" +
                                " join e.buildings b " +
                                "where e.company.id = :id",
                        EmployeeDto.class)
                .setParameter("id", id)
                .getResultList();
            transaction.commit();
        }
        return employees;
    }
}
