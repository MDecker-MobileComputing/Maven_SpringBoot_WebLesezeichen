package de.eldecker.dhbw.spring.weblesezeichen.db.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.eldecker.dhbw.spring.weblesezeichen.db.entities.LesezeichenEntity;


/**
 * Repo-Bean für Zugriff auf {@link LesezeichenEntity}-Objekte.
 * <br><br>
 * 
 * Dies Daten dieses Repos werden auch über REST bereitgestellt, auch ohne
 * explizite Annotation {@code RepositoryRestResource}. Diese Annotation wird
 * nur benötigt um die REST-Bereitstellung zu konfigurieren, z.B. den Pfad.
 * 
 * URL mit REST-Endpunkt: http://localhost:8080/rest/lesezeichenEntities
 */
public interface LesezeichenRepo extends JpaRepository<LesezeichenEntity, Long> {

    /**
     * Derived Query Method: Gibt Liste aller Lesezeichen zurück.
     *
     * @return Liste aller Lesezeichen, aufsteigend sortiert nach Name.
     */
    List<LesezeichenEntity> findAllByOrderByNameAsc();
}
