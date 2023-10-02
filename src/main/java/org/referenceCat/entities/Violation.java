package org.referenceCat.entities;
/**
 * Author: Mikhail Buyanov
 * Date: 11/09/2023 12:36
 */

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="traffic_police_db.violations")
public class Violation {
    private int id;
    private Vehicle vehicle;
    private String commentary;
    private String penalty;
    private int debt;
    private Date date;
    private Officer officer;

    public Violation() {}

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
    @JoinColumn(name = "vehicle_id")
    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Column(name="commentary")
    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    @Column(name="penalty")
    public String getPenalty() {
        return penalty;
    }

    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }

    @Column(name="debt")
    public int getDebt() {
        return debt;
    }

    public void setDebt(int debt) {
        this.debt = debt;
    }

    @Column(name="date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @ManyToOne
    @JoinColumn(name = "officer_id")
    public Officer getOfficer() {
        return officer;
    }

    public void setOfficer(Officer officer) {
        this.officer = officer;
    }
}
