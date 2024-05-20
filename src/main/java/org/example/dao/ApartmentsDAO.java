package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.BuildingDto;
import org.example.dto.EmployeeDto;
import org.example.entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Set;

public class ApartmentsDAO {

/**
 * Saves the details of an apartment into the database.
 * @param apartment The apartment object to be saved.
 *
 */
    public static void saveApartment(Apartments apartment){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(apartment);
            transaction.commit();
        }
    }

    /**
     * Retrieves an apartment from the database based on its ID.
     *
     * @param id The unique identifier of the apartment to retrieve.
     * @return An Apartments object representing the retrieved apartment, or null if not found.
     *
     */
    public static Apartments getApartmentById (long id){

        Apartments apartment;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            apartment = session.get(Apartments.class, id);
            transaction.commit();
        }
        return apartment;
    }

/**
 * Retrieves a list of all apartments stored in the database.
 *
 * @return A list containing Apartments objects representing all apartments in the database.
 *         The list may be empty if no apartments are found.
*/
    public static List<Apartments> getApartments(){
        List<Apartments> apartments;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            apartments = session.createQuery("Select a From Apartments a", Apartments.class).getResultList();
            transaction.commit();
        }
        return apartments;
    }

/**
 * Updates the details of an existing apartment in the database.
 *
 * @param apartment The Apartments object containing updated information to be saved.
 *
 */
    public static void updateApartment(Apartments apartment){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.update(apartment);
            transaction.commit();
        }
    }

/**
 * Deletes an apartment from the database.
 *
 * @param apartment The Apartments object representing the apartment to be deleted.
 *
 */
    public static void deleteApartment(Apartments apartment){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.delete(apartment);
            transaction.commit();
        }
    }

/**
 * Retrieves the residents of a specific apartment from the database.
 *
 * @param id The unique identifier of the apartment.
 * @return A set containing Residents objects representing the residents of the specified apartment.
 *
 */
    public static Set<Residents> getApartmentResidents(long id) {
        Apartments apartments;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            apartments = session.createQuery(
                            "select a from Apartments a" +
                                    " join fetch a.residents" +
                                    " where a.id = :id",
                            Apartments.class)
                    .setParameter("id", id)
                    .getSingleResult();
            transaction.commit();
        }
        return apartments.getResidents();
    }

/**
 * Retrieves the fees associated with a specific apartment from the database.
 *
 * @param id The unique identifier of the apartment.
 * @return A set containing Fees objects representing the fees associated with the specified apartment.
 *
 */
    public static Set<Fees> getFees(long id) {
        Apartments apartments;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            apartments = session.createQuery(
                            "select a from Apartments a" +
                                    " join fetch a.fees" +
                                    " where a.id = :id",
                            Apartments.class)
                    .setParameter("id", id)
                    .getSingleResult();
            transaction.commit();
        }
        return apartments.getFees();
    }

}
