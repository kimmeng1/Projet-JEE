package com.conference.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "registrations")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "conferenceName")
    @Length(min = 5, message = "*Your conference name must have at least 5 characters")
    //@NotEmpty(message = "*Please provide a conference name")
    private String conferenceName;
    
    @Column(name = "creationDate")
	private String creationDate;
    
    @Column(name = "title")
    //@NotEmpty(message = "*Please provide your title")
    private String title;

    @Column(name = "firstName")
    //@NotEmpty(message = "*Please provide your first_name")
    private String firstName;
    
    @Column(name = "lastName")
    //@NotEmpty(message = "*Please provide your last name")
    private String lastName;
    
    @Column(name = "institution")
    //@NotEmpty(message = "*Please provide your institution")
    private String institution;
    
    @Column(name = "address")
    //@NotEmpty(message = "*Please provide your address")
    private String address;
    
    @Column(name = "zip")
    //@NotEmpty(message = "*Please provide your zip")
    private String zip;
    
    @Column(name = "city")
    //@NotEmpty(message = "*Please provide your city")
    private String city;
    
    @Column(name = "country")
    //@NotEmpty(message = "*Please provide your country")
    private String country;
    
    @Column(name = "email")
    //@NotEmpty(message = "*Please provide your email")
    private String email;
    
    @Column(name = "phone")
    //@NotEmpty(message = "*Please provide your phone")
    private String phone;
    
    @Column(name = "type")
    //@NotEmpty(message = "*Please provide your type")
    private String type;
    
    @Column(name="fileTitle")
    private String fileTitle;
    
    @Column(name="file", columnDefinition="MEDIUMBLOB")
    @Lob
    private byte[] file;
    
    @Column(name="status")
    private String status;
}


