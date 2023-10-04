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

        Date date = new Date();
        date.setTime(0);

        Officer officer = new Officer();
        officer.setName("name");
        officer.setSurname("surname");
        officer.setBirthDate(date);
        officer.setPassportId("4018000001");
        officer.setPoliceId("Б000001");
        em.persist(officer);
        em.getTransaction().commit();
    }
}
