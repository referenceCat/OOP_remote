package org.referenceCat; /**
 * Author: Mikhail Buyanov
 * Date: 26/09/2023 01:40
 */

import javax.persistence.*;
import org.referenceCat.entities.Person;
import org.referenceCat.entities.CarOwner;
import org.referenceCat.entities.Officer;
import org.referenceCat.entities.Vehicle;
import org.referenceCat.entities.Violation;
import java.util.Date;


public class Application {
    public static void main(String[] args) {
        System.out.println("Hello world");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence_unit");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Officer officer = em.find(Officer.class, 12);
        Vehicle vehicle = em.find(Vehicle.class, 1);
        Violation violation = new Violation();
        violation.setOfficer(officer);
        violation.setVehicle(vehicle);
        violation.setPenalty("Debt");
        violation.setDebt(1000);
        Date date = new Date();
        date.setTime(0);
        violation.setDate(date);
        em.persist(violation);
        em.getTransaction().commit();

    }
}
