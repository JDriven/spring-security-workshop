package com.jdriven.repo;

import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractAuditable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@EqualsAndHashCode(callSuper = true, of = {})
@ToString(callSuper = true)
public class Blogpost extends AbstractAuditable<Author, Long> {

	private String title;
	private String content;
	private boolean published;

}
