package org.referenceCat.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Author: Mikhail Buyanov
 * Date: 11/09/2023 15:46
 */

@Entity
public class Officer extends Person {
    private String policeId;
    
    public Officer() {}

    @Column(name="police_id")
    public String getPoliceId() {
        return policeId;
    }

    public void setPoliceId(String policeId) {
        this.policeId = policeId;
    }
}
