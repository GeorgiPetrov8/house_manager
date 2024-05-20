package org.example.dao;

import org.example.Enum.StatusType;
import org.example.Schedual.FeeScheduler;
import org.example.configuration.SessionFactoryUtil;
import org.example.entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class FeesDAO {
/**
 * Saves a Fees object into the database.
 *
 * @param fee The Fees object to be saved.
 *
 */
    public static void saveFee(Fees fee){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(fee);
            transaction.commit();
        }
    }

/**
 * Retrieves a Fees object from the database based on its ID.
 *
 * @param id The ID of the Fees object to retrieve.
 * @return The Fees object retrieved from the database, or null if not found.
 *
 */
    public static Fees getFeeById (long id){

        Fees fee;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            fee = session.get(Fees.class, id);
            transaction.commit();
        }
        return fee;
    }

/**
 * Retrieves a list of all Fees objects from the database.
 *
 * @return A list containing all Fees objects retrieved from the database.
 *
 */
    public static List<Fees> getFees(){
        List<Fees> fees;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            fees = session.createQuery("Select a From Fees a", Fees.class).getResultList();
            transaction.commit();
        }
        return fees;
    }

/**
 * Updates the specified Fees object in the database.
 *
 * @param fee The Fees object to be updated in the database.
 *
 */
    public static void updateFee(Fees fee){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.update(fee);
            transaction.commit();
        }
    }

/**
 * Deletes the specified Fees object from the database.
 *
 * @param fee The Fees object to be deleted from the database.
 *
 */
    public static void deleteFee(Fees fee){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.delete(fee);
            transaction.commit();
        }
    }

    /**
     * Calculates the payment for an apartment based on specified fees and apartment details.
     *
     * @param apartment The Apartments object for which the fee is being calculated.
     * @param fee       The Fees object containing fee details.
     * @return The calculated payment for the apartment.
     *
     */
    public static BigDecimal calculateFee(Apartments apartment, Fees fee){
        BigDecimal payment;
        BigDecimal additionalFees = new BigDecimal(0.0);

        int numberOfResidents = 0;

        for (Residents residents : ApartmentsDAO.getApartmentResidents(apartment.getIdApartments())){

            if(residents.getAge() > 7 && residents.isUses_elevator() == true){
                numberOfResidents++;
            }

            additionalFees = additionalFees.add(fee.getAdditionalFee().multiply(BigDecimal.valueOf(numberOfResidents)));
            numberOfResidents = 0;
        }

        for (Residents residents : ApartmentsDAO.getApartmentResidents(apartment.getIdApartments())){

            if(residents.isHas_pet_uses_common_areas() == true){
                additionalFees = additionalFees.add(fee.getAdditionalFeeForPet());
                break;
            }

        }

        payment = BigDecimal.valueOf(apartment.getArea()).multiply(fee.getBaseAmountPerMeter());
        payment = payment.add(additionalFees);

        fee.setTotalFee(payment);
        FeesDAO.updateFee(fee);

        return payment;
    }

    /**
     * Pays the fees for an apartment if there are unpaid fees.
     *
     * @param apartments The Apartments object for which fees are being paid.
     *
     */
    public static void payFees(Apartments apartments){

        Set<Fees> payingFees = ApartmentsDAO.getFees(apartments.getIdApartments());

        if(!payingFees.isEmpty()) {

            for (Fees fees : ApartmentsDAO.getFees(apartments.getIdApartments())) {

                if(fees.getPaymentStatus() == StatusType.NOT_PAID) {
                    fees.setPaymentStatus(StatusType.PAID);
                    fees.setDateOfPayment(LocalDate.now());
                    FeesDAO.updateFee(fees);

                    Company company = CompanyDAO.getCompanyById(BuildingsDAO.getBuildingById(apartments.getBuilding().getIdBuilding()).getEmployee().getCompany().getIdCompany());
                    if(company.getIncome() == null){company.setIncome(BigDecimal.valueOf(0));}
                    company.setIncome(company.getIncome().add(fees.getTotalFee()));

                    CompanyDAO.updateCompany(company);

                    writePaymentToFile(company.getCompany_name(), BuildingsDAO.getBuildingById(apartments.getBuilding().getIdBuilding()).getEmployee().getName(), apartments.getBuilding().getIdBuilding(), apartments.getIdApartments(), fees.getTotalFee());
                }

            }

        }
    }

    /**
     * Writes tax payment information to a text file.
     *
     * @param company   The name of the company making the payment.
     * @param employee  The name of the employee associated with the payment.
     * @param building  The building number where the payment is made.
     * @param apartment The apartment number where the payment is made.
     * @param amount    The payment amount.
     *
     */
    public static void writePaymentToFile(String company, String employee, long building, long apartment, BigDecimal amount) {
        String uniqueId = UUID.randomUUID().toString(); // Генериране на уникален идентификатор
        String directoryPath = "C:\\Users\\hyper\\Desktop\\house_manager\\payments";
        String fileName = "tax_payment_" + uniqueId + ".txt";

        try (FileWriter fileWriter = new FileWriter(new File(directoryPath, fileName));
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String paymentDate = dateFormat.format(new Date());

            printWriter.println("Company: " + company);
            printWriter.println("Employee: " + employee);
            printWriter.println("BuildingNumber: " + building);
            printWriter.println("ApartmentNumber: " + apartment);
            printWriter.println("Amount: " + amount);
            printWriter.println("Payment Date: " + paymentDate);
            printWriter.println("----------------------------------");

            System.out.println("Tax payment information successfully written to file.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    /**
     * Calculates the total number of paid fees for each company.
     *
     * @return A map containing the total number of paid fees for each company.
     *
     */
    public static Map<Company, BigDecimal> getNumberOfFees(){
        Map<Company, BigDecimal> totalPaymentPerCompany = new HashMap<>();
        List<Company> companies = CompanyDAO.getCompanies();

        for (Company c : companies) {
            BigDecimal number = BigDecimal.valueOf(0);
            List<Fees> feesList = getFees();

            for(Fees f : feesList){
                if (f.getApartments().getBuilding().getEmployee().getCompany().getCompany_name().equals(c.getCompany_name()) && f.getPaymentStatus() == StatusType.PAID) {
                    number = number.add(BigDecimal.valueOf(1));
                }
            }

            totalPaymentPerCompany.put(c, number);
        }

        return totalPaymentPerCompany;
    }

    /**
     * Calculates the total number of paid fees for each building.
     *
     * @return A map containing the total number of paid fees for each building.
     *
     */
    public static Map<Buildings, BigDecimal> getNumberOfFeesBuildings(){
        Map<Buildings, BigDecimal> totalPaymentPerCompany = new HashMap<>();
        List<Buildings> buildings = BuildingsDAO.getBuildings();

        for (Buildings b : buildings) {
            BigDecimal number = BigDecimal.valueOf(0);
            List<Fees> feesList = getFees();

            for(Fees f : feesList){
                if (f.getApartments().getBuilding().getIdBuilding() == b.getIdBuilding() && f.getPaymentStatus() == StatusType.PAID) {
                    number = number.add(BigDecimal.valueOf(1));
                }
            }

            totalPaymentPerCompany.put(b, number);
        }

        return totalPaymentPerCompany;
    }

    /**
     * Calculates the total number of paid fees for each employee.
     *
     * @return A map containing the total number of paid fees for each employee.
     *
     */
    public static Map<Employees, BigDecimal> getNumberOfFeesEmployees(){
        Map<Employees, BigDecimal> totalPaymentPerCompany = new HashMap<>();
        List<Employees> employees = EmployeesDAO.getEmployees();

        for (Employees e : employees) {
            BigDecimal number = BigDecimal.valueOf(0);
            List<Fees> feesList = getFees();

            for(Fees f : feesList){
                if (f.getApartments().getBuilding().getEmployee().getIdEmployee() == e.getIdEmployee() && f.getPaymentStatus() == StatusType.PAID) {
                    number = number.add(BigDecimal.valueOf(1));
                }
            }

            totalPaymentPerCompany.put(e, number);
        }

        return totalPaymentPerCompany;
    }

    /**
     * Retrieves a mapping of companies to their paid fees.
     *
     * @return A map containing companies as keys and their corresponding paid fees.
     *
     */
    public static Map<Company, Fees> getNumberOfFeesList(){
        Map<Company, Fees> totalPaymentPerCompany = new HashMap<>();
        List<Company> companies = CompanyDAO.getCompanies();

        for (Company c : companies) {
            List<Fees> feesList = getFees();

            for(Fees f : feesList){
                if (f.getApartments().getBuilding().getEmployee().getCompany().getCompany_name().equals(c.getCompany_name()) && f.getPaymentStatus() == StatusType.PAID) {
                    totalPaymentPerCompany.put(c, f);
                }
            }

        }

        return totalPaymentPerCompany;
    }

    /**
     * Retrieves a mapping of buildings to their paid fees.
     *
     * @return A map containing buildings as keys and their corresponding paid fees.
     *
     */
    public static Map<Buildings, Fees> getNumberOfFeesBuildingsList(){
        Map<Buildings, Fees> totalPaymentPerCompany = new HashMap<>();
        List<Buildings> buildings = BuildingsDAO.getBuildings();

        for (Buildings b : buildings) {
            List<Fees> feesList = getFees();

            for(Fees f : feesList){
                if (f.getApartments().getBuilding().getIdBuilding() == b.getIdBuilding() && f.getPaymentStatus() == StatusType.PAID) {
                    totalPaymentPerCompany.put(b, f);
                }
            }

        }

        return totalPaymentPerCompany;
    }

    /**
     * Retrieves a mapping of employees to their paid fees.
     *
     * @return A map containing employees as keys and their corresponding paid fees.
     *
     */
    public static Map<Employees, Fees> getNumberOfFeesEmployeesList(){
        Map<Employees, Fees> totalPaymentPerCompany = new HashMap<>();
        List<Employees> employees = EmployeesDAO.getEmployees();

        for (Employees e : employees) {
            List<Fees> feesList = getFees();

            for(Fees f : feesList){
                if (f.getApartments().getBuilding().getEmployee().getIdEmployee() == e.getIdEmployee() && f.getPaymentStatus() == StatusType.PAID) {
                    totalPaymentPerCompany.put(e, f);
                }
            }

        }

        return totalPaymentPerCompany;
    }

    /**
     * Retrieves the number of non-paid fees per company.
     *
     * @return A map containing companies as keys and the number of non-paid fees as values.
     *
     */
    public static Map<Company, BigDecimal> getNumberOfFeesForNonPaid(){
        Map<Company, BigDecimal> totalPaymentPerCompany = new HashMap<>();
        List<Company> companies = CompanyDAO.getCompanies();

        for (Company c : companies) {
            BigDecimal number = BigDecimal.valueOf(0);
            List<Fees> feesList = getFees();

            for(Fees f : feesList){
                if (f.getApartments().getBuilding().getEmployee().getCompany().getCompany_name().equals(c.getCompany_name()) && f.getPaymentStatus() == StatusType.NOT_PAID) {
                    number = number.add(BigDecimal.valueOf(1));
                }
            }

            totalPaymentPerCompany.put(c, number);
        }

        return totalPaymentPerCompany;
    }

    /**
     * Retrieves the number of non-paid fees per building.
     *
     * @return A map containing buildings as keys and the number of non-paid fees as values.
     *
     */
    public static Map<Buildings, BigDecimal> getNumberOfFeesBuildingsForNonPaid(){
        Map<Buildings, BigDecimal> totalPaymentPerCompany = new HashMap<>();
        List<Buildings> buildings = BuildingsDAO.getBuildings();

        for (Buildings b : buildings) {
            BigDecimal number = BigDecimal.valueOf(0);
            List<Fees> feesList = getFees();

            for(Fees f : feesList){
                if (f.getApartments().getBuilding().getIdBuilding() == b.getIdBuilding() && f.getPaymentStatus() == StatusType.NOT_PAID) {
                    number = number.add(BigDecimal.valueOf(1));
                }
            }

            totalPaymentPerCompany.put(b, number);
        }

        return totalPaymentPerCompany;
    }


    /**
     * Retrieves the number of non-paid fees per employee.
     *
     * @return A map containing employees as keys and the number of non-paid fees as values.
     *
     */
    public static Map<Employees, BigDecimal> getNumberOfFeesEmployeesForNonPaid(){
        Map<Employees, BigDecimal> totalPaymentPerCompany = new HashMap<>();
        List<Employees> employees = EmployeesDAO.getEmployees();

        for (Employees e : employees) {
            BigDecimal number = BigDecimal.valueOf(0);
            List<Fees> feesList = getFees();

            for(Fees f : feesList){
                if (f.getApartments().getBuilding().getEmployee().getIdEmployee() == e.getIdEmployee() && f.getPaymentStatus() == StatusType.NOT_PAID) {
                    number = number.add(BigDecimal.valueOf(1));
                }
            }

            totalPaymentPerCompany.put(e, number);
        }

        return totalPaymentPerCompany;
    }

    /**
     * Retrieves non-paid fees for each company.
     *
     * @return A map containing companies as keys and their respective non-paid fees as values.
     *
     */
    public static Map<Company, Fees> getNumberOfFeesListForNonPaid(){
        Map<Company, Fees> totalPaymentPerCompany = new HashMap<>();
        List<Company> companies = CompanyDAO.getCompanies();

        for (Company c : companies) {
            List<Fees> feesList = getFees();

            for(Fees f : feesList){
                if (f.getApartments().getBuilding().getEmployee().getCompany().getCompany_name().equals(c.getCompany_name()) && f.getPaymentStatus() == StatusType.NOT_PAID) {
                    totalPaymentPerCompany.put(c, f);
                }
            }

        }

        return totalPaymentPerCompany;
    }

    /**
     * Retrieves non-paid fees for each building.
     *
     * @return A map containing buildings as keys and their respective non-paid fees as values.
     *
     */
    public static Map<Buildings, Fees> getNumberOfFeesBuildingsListForNonPaid(){
        Map<Buildings, Fees> totalPaymentPerCompany = new HashMap<>();
        List<Buildings> buildings = BuildingsDAO.getBuildings();

        for (Buildings b : buildings) {
            List<Fees> feesList = getFees();

            for(Fees f : feesList){
                if (f.getApartments().getBuilding().getIdBuilding() == b.getIdBuilding() && f.getPaymentStatus() == StatusType.NOT_PAID) {
                    totalPaymentPerCompany.put(b, f);
                }
            }

        }

        return totalPaymentPerCompany;
    }

    /**
     * Retrieves non-paid fees for each employee.
     *
     * @return A map containing employees as keys and their respective non-paid fees as values.
     *
     */
    public static Map<Employees, Fees> getNumberOfFeesEmployeesListForNonPaid(){
        Map<Employees, Fees> totalPaymentPerCompany = new HashMap<>();
        List<Employees> employees = EmployeesDAO.getEmployees();

        for (Employees e : employees) {
            List<Fees> feesList = getFees();

            for(Fees f : feesList){
                if (f.getApartments().getBuilding().getEmployee().getIdEmployee() == e.getIdEmployee() && f.getPaymentStatus() == StatusType.NOT_PAID) {
                    totalPaymentPerCompany.put(e, f);
                }
            }

        }

        return totalPaymentPerCompany;
    }
}
