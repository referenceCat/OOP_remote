package org.referenceCat.entities;
/**
 * Author: Mikhail Buyanov
 * Date: 11/09/2023 12:36
 */

import java.util.Date;

public class Violation {
    private int id;
    private Vehicle vehicle;
    private String commentary;
    private String penalty;
    private int debt;
    private Date date;
    private Officer officer;

    public Violation() {}

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public String getPenalty() {
        return penalty;
    }

    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }

    public int getDebt() {
        return debt;
    }

    public void setDebt(int debt) {
        this.debt = debt;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Officer getOfficer() {
        return officer;
    }

    public void setOfficer(Officer officer) {
        this.officer = officer;
    }

    public int getId() {
        return id;
    }
}
