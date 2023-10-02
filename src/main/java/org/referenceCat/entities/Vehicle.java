package org.referenceCat.entities;
/**
 * Author: Mikhail Buyanov
 * Date: 11/09/2023 12:35
 */

import java.util.Date;

public class Vehicle {
    private int id;
    private Person owner;
    private String regNumber, model, color;
    private Date maintenanceDate;

    public Vehicle() {}

    public int getId() {
        return id;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Date getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(Date maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }
    // enum type??

}
