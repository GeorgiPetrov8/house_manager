package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.BuildingDto;
import org.example.dto.EmployeeDto;
import org.example.entity.Apartments;
import org.example.entity.Buildings;
import org.example.entity.Company;
import org.example.entity.Employees;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

public class BuildingsDAO {

/**
 * Saves the details of a building into the database.
 *
 * @param building The Buildings object to be saved.
 *
 */
    public static void saveBuilding(Buildings building){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(building);
            transaction.commit();
        }
    }

/**
 * Retrieves a building from the database based on its ID.
 *
 * @param id The unique identifier of the building to retrieve.
 * @return A Buildings object representing the retrieved building, or null if not found.
 *
 */
    public static Buildings getBuildingById (long id){

        Buildings building;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            building = session.get(Buildings.class, id);
            transaction.commit();
        }
        return building;
    }

/**
 * Retrieves a list of all buildings stored in the database.
 *
 * @return A list containing Buildings objects representing all buildings in the database.
 *         The list may be empty if no buildings are found.
 *
 */
    public static List<Buildings> getBuildings(){
        List<Buildings> buildings;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            buildings = session.createQuery("Select b From Buildings b", Buildings.class).getResultList();
            transaction.commit();
        }
        return buildings;
    }

/**
 * Updates the details of an existing building in the database.
 *
 * @param building The Buildings object containing updated information to be saved.
 *
 */
    public static void updateBuilding(Buildings building){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.update(building);
            transaction.commit();
        }
    }

/**
 * Deletes a building from the database.
 *
 * @param building The Buildings object representing the building to be deleted.
 *
 */
public static void deleteBuilding(Buildings building){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.delete(building);
            transaction.commit();
        }
    }

/**
 * Retrieves a mapping of Buildings to Apartments from the database.
 *
 * @return A map containing a set of Buildings as keys and corresponding sets of Apartments as values.
 *
 */
    public static Map<Set<Buildings>, Set<Apartments>> getBuildingApartments() {
        Map<Set<Buildings>, Set<Apartments>> buildingsApartmentsMap = new HashMap<>();

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            List<Object[]> resultList = session.createQuery(
                            "select b, a from Buildings b join b.apartments a", Object[].class)
                    .getResultList();

            for (Object[] row : resultList) {
                Buildings building = (Buildings) row[0];
                Apartments apartments = (Apartments) row[1];

                Set<Buildings> buildingSet = new HashSet<>();
                buildingSet.add(building);

                Set<Apartments> apartmentsSet = buildingsApartmentsMap.getOrDefault(buildingSet, new HashSet<>());
                apartmentsSet.add(apartments);

                buildingsApartmentsMap.put(buildingSet, apartmentsSet);
            }
            transaction.commit();
        }
        return buildingsApartmentsMap;
    }

}
