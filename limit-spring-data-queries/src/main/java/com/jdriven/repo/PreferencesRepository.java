package com.jdriven.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

public interface PreferencesRepository extends Repository<Preferences, Long> {

	@PreAuthorize("false") // TODO Add security expression
	Preferences save(Preferences entity);

	@PostAuthorize("false") // TODO Add security expression
	Preferences findById(Long id);

	@Query("select p from #{#entityName} p") // TODO Add where clause
	Optional<Preferences> findUserPreferences();

}
