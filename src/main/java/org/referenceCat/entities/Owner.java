package org.referenceCat.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Author: Mikhail Buyanov
 * Date: 11/09/2023 15:45
 */

@Entity
public class Owner extends Person {
    private String licenseId;

    public Owner() {
    }

    @Column(name = "license_id")
    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }
}
