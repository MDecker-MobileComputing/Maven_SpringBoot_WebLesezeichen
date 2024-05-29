package de.eldecker.dhbw.spring.weblesezeichen.db.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.eldecker.dhbw.spring.weblesezeichen.db.entities.OrdnerEntity;


/**
 * Repo-Bean für Zugriff auf Ordner, die die Lesezeichen enthalten.
 */
public interface OrdnerRepo extends JpaRepository<OrdnerEntity, Long>{

    /**
     * Query-Methode mit JPQL, gibt eine flache Liste aller Ordner zurück.
     * 
     * @return Liste aller Ordner, sortiert nach Ordnername (case-insensitive).
     */
    @Query("SELECT o FROM OrdnerEntity o ORDER BY LOWER(o.name) ASC")
    List<OrdnerEntity> findAllByOrderByNameIgnoreCase();
}
