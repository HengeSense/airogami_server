package com.airogami.persistence.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.apache.struts2.json.annotations.JSON;

/**
 * Category entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CATEGORY", catalog = "Airogami")
public class Category implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;

	private Short categoryId;

	private String name;

	private String description;

	private List<Plane> planes = new ArrayList<Plane>(0);

	// Constructors

	/** default constructor */
	public Category() {
	}

	/** minimal constructor */
	public Category(String name, String description) {
		this.name = name;
		this.description = description;
	}

	/** full constructor */
	public Category(String name, String description, List<Plane> planes) {
		this.name = name;
		this.description = description;
		this.planes = planes;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "CATEGORY_ID", unique = true, nullable = false)
	public Short getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(Short categoryId) {
		this.categoryId = categoryId;
	}

	@Column(name = "NAME", nullable = false, length = 256)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", nullable = false, length = 256)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "category")
	public List<Plane> getPlanes() {
		return this.planes;
	}

	public void setPlanes(List<Plane> planes) {
		this.planes = planes;
	}

}