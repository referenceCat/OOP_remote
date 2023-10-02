package org.referenceCat; /**
 * Author: Mikhail Buyanov
 * Date: 26/09/2023 01:40
 */

import javax.persistence.*;
import org.referenceCat.entities.Person;
import org.referenceCat.entities.CarOwner;
import org.referenceCat.entities.Officer;
import java.util.Date;

public class Application {
    public static void main(String[] args) {
        System.out.println("Hello world");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence_unit");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        CarOwner carOwner = new CarOwner();
        carOwner.setName("name");
        carOwner.setSurname("surname");
        Date date = new Date();
        date.setTime(0);
        carOwner.setBirthDate(date);
        carOwner.setPassportId(4874298);
        carOwner.setLicenseId("rbguhfrjiduehf");
        em.persist(carOwner);

        Officer officer = new Officer();
        officer.setName("name");
        officer.setSurname("surname");
        officer.setBirthDate(date);
        officer.setPassportId(387398);
        officer.setPoliceId("eywhbudnjsbgfuhs");
        em.persist(officer);

        Person person = new Person();
        person.setName("name");
        person.setSurname("surname");
        person.setBirthDate(date);
        person.setPassportId(3389);
        em.persist(person);
        em.getTransaction().commit();

    }
}
