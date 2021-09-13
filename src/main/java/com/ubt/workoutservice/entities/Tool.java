package com.ubt.workoutservice.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ubt.workoutservice.configurations.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="tools")
public class Tool extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column(unique = true)
	private String name;

	@Column
	private String description;

	@Column
	private boolean enabled;

	@JsonBackReference("tool-category")
	@ManyToOne(fetch = FetchType.EAGER)
	private Category category;

	@JsonManagedReference(value = "tool-detail")
	@OneToMany(mappedBy = "tool")
	private List<ToolDetail> toolDetails;

}
