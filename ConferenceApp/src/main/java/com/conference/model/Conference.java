package com.conference.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "conference")
public class Conference {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@Column(name = "conferenceName")
	@Length(min = 5, message = "*Your conference name must have at least 5 characters")
	@NotEmpty(message = "*Please provide a conference name")
	private String conferenceName;

	@Column(name = "creationDate")
	private String creationDate;

	@Column(name = "date")
	private String date;

	@Column(name = "start")
	private String start;

	@Column(name = "end")
	private String end;

	@Column(name = "location")
	private String location;

	@Column(name = "title")
	private boolean title;

	@Column(name = "firstName")
	private boolean firstName;

	@Column(name = "lastName")
	private boolean lastName;

	@Column(name = "institution")
	private boolean institution;

	@Column(name = "address")
	private boolean address;

	@Column(name = "zip")
	private boolean zip;

	@Column(name = "city")
	private boolean city;

	@Column(name = "country")
	private boolean country;

	@Column(name = "email")
	private boolean email;

	@Column(name = "phone")
	private boolean phone;

	@Column(name = "types")
	@ElementCollection
	//@OneToMany(targetEntity = Conference.class, mappedBy = "types", fetch = FetchType.EAGER)
	// @NotEmpty(message = "*Please provide your type")
	private List<String> types = new ArrayList<String>();
}
