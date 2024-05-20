package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Apartments;
import org.example.entity.Buildings;
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

public class ResidentsDAO {

    /**
     * Saves a Resident entity to the database.
     *
     * @param resident The Resident object to be saved.
     *
     */
    public static void saveResident(Residents resident){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(resident);
            transaction.commit();
        }
    }

    /**
     * Retrieves a Resident entity from the database based on the provided ID.
     *
     * @param id The ID of the Resident to be retrieved.
     * @return The Resident object if found, otherwise null.
     */
    public static Residents getResidentById (long id){

        Residents resident;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            resident = session.get(Residents.class, id);
            transaction.commit();
        }
        return resident;
    }

    /**
     * Retrieves a list of all Residents from the database.
     *
     * @return A list of Resident objects if found, otherwise an empty list.
     *
     */
    public static List<Residents> getResidents(){
        List<Residents> residents;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            residents = session.createQuery("Select r From Residents r", Residents.class).getResultList();
            transaction.commit();
        }
        return residents;
    }

    /**
     * Updates a Resident in the database.
     *
     * @param resident The Resident object to be updated.
     *
     */
    public static void updateResident(Residents resident){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.update(resident);
            transaction.commit();
        }
    }

    /**
     * Deletes a Resident from the database.
     *
     * @param resident The Resident object to be deleted.
     *
     */
    public static void deleteResident(Residents resident){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.delete(resident);
            transaction.commit();
        }
    }

    /**
     * Retrieves a list of Residents sorted by name.
     *
     * @return A sorted list of Residents by name.
     *
     */
    public static List<Residents> getSortedResidentsByName() {
        List<Residents> residents;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            residents = session.createQuery("Select r From Residents r", Residents.class).getResultList();
            transaction.commit();
        }

        residents.sort(Comparator.comparing(Residents::getName));

        return residents;
    }

    /**
     * Retrieves a filtered list of Residents by name.
     *
     * @param name The name to filter residents by.
     * @return A list of Residents matching the provided name.
     *
     */
    public static List<Residents> getResidentsFilteredByName(String name) {
        List<Residents> residents;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            residents = session.createQuery("Select r From Residents r", Residents.class).getResultList();
            transaction.commit();
        }

        residents = residents.stream().filter(resident -> resident.getName().equals(name)).collect(Collectors.toList());

        return residents;
    }

    /**
     * Retrieves a sorted list of Residents by age in descending order.
     *
     * @return A sorted list of Residents by age in descending order.
     *
     */
    public static List<Residents> getSortedResidentsByAge() {
        List<Residents> residents;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            residents = session.createQuery("Select r From Residents r", Residents.class).getResultList();
            transaction.commit();
        }

        residents.sort(Comparator.comparingInt(resident -> -resident.getAge()));

        return residents;
    }

    /**
     * Retrieves a list of Residents whose age falls within the specified range.
     *
     * @param bottom The lower bound of the age range.
     * @param top    The upper bound of the age range.
     * @return A list of Residents within the specified age range.
     *
     */
    public static List<Residents> residentsFindByAgeBetween(double bottom, double top) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Residents> cr = cb.createQuery(Residents.class);
            Root<Residents> root = cr.from(Residents.class);

            cr.select(root).where(cb.between(root.get("age"), bottom, top));

            Query<Residents> query = session.createQuery(cr);
            List<Residents> residents = query.getResultList();
            return residents;
        }
    }

    /**
     * Retrieves a mapping of Apartments to their respective Residents.
     *
     * @return A map containing sets of Apartments mapped to their corresponding Residents.
     *
     */
    public static Map<Set<Apartments>, Set<Residents>> getApartmentsResidents() {
        Map<Set<Apartments>, Set<Residents>> apartmentsResidentsMap = new HashMap<>();

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            List<Object[]> resultList = session.createQuery(
                            "select a, r from Apartments a join a.residents r", Object[].class)
                    .getResultList();

            for (Object[] row : resultList) {
                Apartments apartments = (Apartments) row[0];
                Residents residents = (Residents) row[1];

                Set<Apartments> apartmentsSet = new HashSet<>();
                apartmentsSet.add(apartments);

                Set<Residents> residentsSet = apartmentsResidentsMap.getOrDefault(apartmentsSet, new HashSet<>());
                residentsSet.add(residents);

                apartmentsResidentsMap.put(apartmentsSet, residentsSet);
            }
            transaction.commit();
        }
        return apartmentsResidentsMap;
    }
}
