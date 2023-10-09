package org.referenceCat.entities;
/**
 * Author: Mikhail Buyanov
 * Date: 11/09/2023 12:34
 */

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "traffic_police_db.persons")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Person {
    private int id;
    private Date birthDate;
    private String Name, Surname, Patronymic, passportId;

    public Person() {
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "birth_date")
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Column(name = "name")
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Column(name = "surname")
    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    @Column(name = "patronymic")
    public String getPatronymic() {
        return Patronymic;
    }

    public void setPatronymic(String patronymic) {
        Patronymic = patronymic;
    }

    @Column(name = "passport_id")
    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }
}
