package de.eldecker.dhbw.spring.weblesezeichen.db.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import de.eldecker.dhbw.spring.weblesezeichen.db.entities.LesezeichenEntity;


public interface LesezeichenRepo extends JpaRepository<LesezeichenEntity, Long> {

}
