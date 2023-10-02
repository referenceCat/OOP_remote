package org.referenceCat; /**
 * Author: Mikhail Buyanov
 * Date: 26/09/2023 01:40
 */

import javax.persistence.*;
import org.referenceCat.entities.Person;
import org.referenceCat.entities.CarOwner;
import org.referenceCat.entities.Officer;
import org.referenceCat.entities.Vehicle;
import java.util.Date;


public class Application {
    public static void main(String[] args) {
        System.out.println("Hello world");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence_unit");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        CarOwner carOwner = em.find(CarOwner.class, 8);
        Vehicle vehicle = new Vehicle();
        vehicle.setRegNumber("456");
        vehicle.setOwner(carOwner);
        em.persist(vehicle);
        em.getTransaction().commit();

    }
}
