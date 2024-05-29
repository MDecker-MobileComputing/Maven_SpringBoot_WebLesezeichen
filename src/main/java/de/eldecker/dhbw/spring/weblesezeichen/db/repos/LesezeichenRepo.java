package de.eldecker.dhbw.spring.weblesezeichen.db.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.eldecker.dhbw.spring.weblesezeichen.db.entities.LesezeichenEntity;


public interface LesezeichenRepo extends JpaRepository<LesezeichenEntity, Long> {

    /**
     * Derived Query Method: Gibt Liste aller Lesezeichen zurück.
     *
     * @return Liste aller Lesezeichen, aufsteigend sortiert nach Name.
     */
    List<LesezeichenEntity> findAllByOrderByNameAsc();
}
