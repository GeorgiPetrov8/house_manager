package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Buildings;
import org.example.entity.Company;
import org.example.entity.Employees;
import org.example.entity.Fees;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

public class CompanyDAO {

/**
 * Saves company details into the database.
 *
 * @param company The Company object to be saved.
 *
 */
    public static void saveCompany(Company company){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(company);
            transaction.commit();
        }
    }

/**
 * Retrieves a company from the database based on its ID.
 *
 * @param id The unique identifier of the company to retrieve.
 * @return A Company object representing the retrieved company, or null if not found.
 *
 */
    public static Company getCompanyById (long id){

        Company company;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            company = session.get(Company.class, id);
            transaction.commit();
        }
        return company;
    }

/**
 * Retrieves a list of all companies stored in the database.
 *
 * @return A list containing Company objects representing all companies in the database.
 *         The list may be empty if no companies are found.
 *
 */
    public static List<Company> getCompanies(){
        List<Company> companies;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            companies = session.createQuery("Select c From Company c", Company.class).getResultList();
            transaction.commit();
        }
        return companies;
    }

/**
 * Updates the details of an existing company in the database.
 *
 * @param company The Company object containing updated information to be saved.
 *
 */
    public static void updateCompany(Company company){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.update(company);
            transaction.commit();
        }
    }

/**
 * Deletes a company from the database.
 *
 * @param company The Company object representing the company to be deleted.
 *
 */
    public static void deleteCompany(Company company){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.delete(company);
            transaction.commit();
        }
    }

/**
 * Retrieves employees belonging to a specific company based on the company's ID.
 *
 * @param id The ID of the company whose employees are to be retrieved.
 * @return A set containing Employee objects representing employees of the specified company.
 *
 */
    public static Set<Employees> getCompanyEmployees(long id) {
        Company company;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            company = session.createQuery(
                            "select c from Company c" +
                                    " join fetch c.employees" +
                                    " where c.id = :id",
                            Company.class)
                    .setParameter("id", id)
                    .getSingleResult();
            transaction.commit();
        }
        return company.getEmployees();
    }

    /**
     * Distributes a new building to the least assigned employee within a company based on their workload.
     *
     * @param newBuilding The Building object representing the new building to be assigned.
     * @param id          The ID of the company to retrieve employees and perform distribution.
     * @return The Employee object representing the least assigned employee, after assigning the new building.
     *
     */
    public static Employees distributeBuildingToEmployee(Buildings newBuilding, long id) {

        Employees leastAssignedEmployee = null;
        int minBuildingsAssigned = Integer.MAX_VALUE;

        for (Employees employee : getCompanyEmployees(id)) {
            if (employee.getNumberOfBuildings() < minBuildingsAssigned) {
                minBuildingsAssigned = employee.getNumberOfBuildings();
                leastAssignedEmployee = employee;
            }
        }

        if (leastAssignedEmployee != null) {
           leastAssignedEmployee.setBuildings(newBuilding);
           newBuilding.setEmployee(leastAssignedEmployee);
           BuildingsDAO.updateBuilding(newBuilding);
        }

        return leastAssignedEmployee;
    }

/**
 * Retrieves a list of companies sorted by their income (revenue) in descending order.
 *
 * @return A list containing Company objects representing companies sorted by income.
 *
 */
    public static List<Company> getCompaniesSortedByRevenue() {
        List<Company> companies;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            companies = session.createQuery("Select c From Company c", Company.class).getResultList();
            transaction.commit();
        }

        companies.sort((c1, c2) -> c2.getIncome().compareTo(c1.getIncome()));

        return companies;
    }

/**
 * Retrieves a list of companies whose income (initial capital) falls within a specified range.
 *
 * @param bottom The lower limit of the income range.
 * @param top    The upper limit of the income range.
 * @return A list containing Company objects whose income falls within the specified range.
 *
 */
    public static List<Company> companiesFindByInitialCapitalBetween(double bottom, double top) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Company> cr = cb.createQuery(Company.class);
            Root<Company> root = cr.from(Company.class);

            cr.select(root).where(cb.between(root.get("income"), bottom, top));

            Query<Company> query = session.createQuery(cr);
            List<Company> companies = query.getResultList();
            return companies;
        }
    }

}
