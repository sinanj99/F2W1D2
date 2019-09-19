/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.util.Date;
import javax.persistence.Temporal;

/**
 *
 * @author sinanjasar
 */
public class PersonDTO {
    private String firstName,lastName,phone;
    private Date created,lastEdited;

    public PersonDTO(Person p) {
        this.firstName = p.getFirstName();
        this.lastName = p.getLastName();
        this.phone = p.getPhone();
        this.created = p.getCreated();
        this.lastEdited = p.getLastEdited();
    }
    
}
