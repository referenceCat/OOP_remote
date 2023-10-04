package org.referenceCat.entities;
/**
 * Author: Mikhail Buyanov
 * Date: 11/09/2023 12:35
 */

import javax.persistence.*;
import java.util.Date;
import org.referenceCat.entities.Person;

@Entity
@Table(name="traffic_police_db.vehicles")
public class Vehicle {
    private int id;
    private CarOwner owner;
    private String regNumber, model, color;
    private Date maintenanceDate;

    public Vehicle() {}

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "owner_id")
    public CarOwner getOwner() {
        return owner;
    }

    public void setOwner(CarOwner owner) {
        this.owner = owner;
    }

    @Column(name="reg_number")
    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    @Column(name="model")
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column(name="color")
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Column(name="maintenance_date")
    public Date getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(Date maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }
}
