package com.ubt.workoutservice.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name="plan_programs", uniqueConstraints = {@UniqueConstraint(columnNames = {"day", "person_id", "category_id"})})
public class PlanProgram extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String day;

    @Column
    private String personFullName;

    @Column
    private boolean enabled;

	@Column(name = "person_id")
	private Long personId;

    @JsonBackReference(value = "category-planprograms")
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;
}
