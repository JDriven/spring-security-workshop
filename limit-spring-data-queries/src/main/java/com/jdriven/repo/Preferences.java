package com.jdriven.repo;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@Entity
@EqualsAndHashCode(callSuper = true, of = {})
@ToString(callSuper = true)
public class Preferences extends AbstractPersistable<Long> {

	boolean darkMode = false;

	@OneToOne(optional = false)
	private User user;

}
