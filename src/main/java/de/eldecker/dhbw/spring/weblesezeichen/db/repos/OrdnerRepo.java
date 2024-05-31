package de.eldecker.dhbw.spring.weblesezeichen.db.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import de.eldecker.dhbw.spring.weblesezeichen.db.entities.LesezeichenEntity;
import de.eldecker.dhbw.spring.weblesezeichen.db.entities.OrdnerEntity;


/**
 * Repo-Bean für Zugriff auf {@link OrdnerEntity}-Objekte, die die Lesezeichen enthalten.
 * <br><br>
 *
 * Annotation {@code RepositoryRestResource}:
 * <ul>
 * <li>
 *   URL für lokalen Zugriff auf den REST-Endpunkt: http://localhost:8080/rest/ordner
 * </li>
 * <li>
 *   Es wird Paginierung unterstützt, z.B.: http://localhost:8080/rest/ordner?page=0&size=5
 *   Achtung: die erste Seite ist {@code page=0}
 * </li>
 * <li>In der Datei {@code application.properties} gibt es die folgende Annotation, damit
 *     vor dem mit dem Attribut {@code path} angegebenen Pfad noch {@code rest} auftaucht:</li>
 *     {@code spring.data.rest.base-path=/rest}.
 *     Es ist nicht erlaubt im Attribut {@code path} Unterordner anzugeben.
 * </li>
 * <li>
 *  Mit dem Interface {@link InlineLesezeichen} als Wert für das Attribut {@code excerptProjection}
 *  wird bestimmt, welche Attribute in der JSON-Datei enthalten sein sollen. Hierdurch wird insb.
 *  ermöglich, auch gleich die Werte der im Ordner enthaltenen
 *  {@link LesezeichenEntity}-Objekte anzuzeigen.
 * </li>
 * </ul>
 */
@RepositoryRestResource( path = "ordner", excerptProjection = InlineLesezeichen.class)
public interface OrdnerRepo extends JpaRepository<OrdnerEntity, Long>{

    /**
     * Query-Methode mit JPQL, gibt eine flache Liste aller Ordner zurück.
     *
     * @return Liste aller Ordner, sortiert nach Ordnername (case-insensitive).
     */
    @Query( "SELECT o FROM OrdnerEntity o ORDER BY LOWER(o.name) ASC" )
    List<OrdnerEntity> findAllByOrderByNameIgnoreCase();


    /**
     * Derived Query Method: Suche nach allen direkten Unterordnern eines über
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


    /**
     * Derived Query Method: Suche nach allen Ordnern mit {@code name} (case-insensitive).
     * Diese Methode ist erforderlich, um zu überprüfen, ob es einen neu anzulegenden
     * Ordner schon gibt (weil für das Attribut {@code name} in der Klasse {@code OrdnerEntity}
     * eine {@code unique}-Constraint definiert ist).
     *
     * @param name Name des Ordners
     *
     * @return Liste der Ordner mit dem gesuchten Namen; kann leer sein.
     */
    List<OrdnerEntity> findByNameIgnoreCase( String name );


    /**
     * Derived Query Method: Wurzelordner holen (ist die einzige
     * {@link OrdnerEntity} ohne Vater.
     * <br><br>
     *
     * Diese Methode ist erforderlich, weil wir nicht sicher sein können,
     * dann der Wurzeln-Knoten immer die ID 1 gibt (die ID wird von JPA
     * zugewiesen).
     *
     * @return Optional enthält den Wurzelordner; wenn es leer ist,
     *         dann wurde die Datenbank noch nicht intialisiert?
     */
    Optional<OrdnerEntity> findByVaterIsNull();


    /** Schreibenden Zugriff über REST abschalten. */
    @SuppressWarnings("unchecked")
    @Override
    @RestResource( exported = false )
    OrdnerEntity save( OrdnerEntity entity );

    /** HTTP-Delete für einzelne Datensätze abschalten. */
    @Override
    @RestResource( exported = false )
    void delete( OrdnerEntity entity );

    /** HTTP-Delete für alle Datensätze abschalten. */
    @Override
    @RestResource( exported = false )
    void deleteAll();
}
