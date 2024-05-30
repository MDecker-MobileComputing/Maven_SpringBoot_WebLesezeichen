package de.eldecker.dhbw.spring.weblesezeichen.db.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.eldecker.dhbw.spring.weblesezeichen.db.entities.OrdnerEntity;


/**
 * Repo-Bean für Zugriff auf {@link OrdnerEntity}-Objekte, die die Lesezeichen enthalten.
 */
public interface OrdnerRepo extends JpaRepository<OrdnerEntity, Long>{

    /**
     * Query-Methode mit JPQL, gibt eine flache Liste aller Ordner zurück.
     *
     * @return Liste aller Ordner, sortiert nach Ordnername (case-insensitive).
     */
    @Query( "SELECT o FROM OrdnerEntity o ORDER BY LOWER(o.name) ASC" )
    List<OrdnerEntity> findAllByOrderByNameIgnoreCase();


    /**
     * Derived Query: Suche nach allen direkten Unterordnern eines über
     * die ID spezifizierten Ordners.
     *
     * Der Unterstrich {@code Vater_Id} wird verwendet, um die Assoziation zwischen
     * {@link OrdnerEntity} und seinem 'vater' zu navigieren.
     *
     * @param vaterId ID des Ordners, dessen Unterordner gesucht werden.
     *
     * @return Liste der direkten Unterordner des Ordners, sortiert aufsteigend
     *         nach Name
     *
     */
    List<OrdnerEntity> findByVater_IdOrderByNameAsc( Long vaterId );
}
