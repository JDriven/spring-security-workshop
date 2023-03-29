package com.jdriven.repo;

import org.springframework.data.jpa.domain.AbstractPersistable;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@EqualsAndHashCode(callSuper = true, of = {})
@ToString(callSuper = true)
public class Preferences extends AbstractPersistable<Long> {

	boolean darkMode = false;

	@OneToOne(optional = false)
	private User user;

}
