package de.eldecker.dhbw.spring.weblesezeichen.db.entities;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.AUTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


/**
 * Eigentliches Lesezeichen, ist in genau einem Ordner enhalten.
 */
@Entity
@Table(name = "Lesezeichen")
public class LesezeichenEntity {

    /**
     * Primärschlüssel, wird von JPA verwaltet.
     */
    @Id
    @GeneratedValue( strategy = AUTO )
    private Long id;

    /** Name des Lesezeichen, z.B. "Homepage von Max Mustermann". */
    private String name;

    /** URL des Lesezeichen, z.B. "http://www.heise.de". */
    private String url;

    /**
     * Referenz auf Ordner, in dem dieses Lesezeichen enthalten ist.
     * Diese Beziehung wird durch die Spalte {@code ordner_fk} in der
     * Tabelle {@code Lesezeichen} realisiert, also ist dies die
     * <i>owning side</i> der Beziehung. Neue Beziehungen sollten
     * an der <i>owning side</i> gesetzt werden.
     */
    @ManyToOne( fetch = EAGER )
    @JoinColumn( name = "ordner_fk", referencedColumnName = "id" )
    private OrdnerEntity ordner;


    /**
     * Für JPA obligatorisches Default-Konstruktor.
     */
    public LesezeichenEntity() {

        this( "", "", null );
    }


    /**
     * Konstruktor um {@code name}, {@code url} und {@code ordner},
     * in dem das Lesezeichen enthalten ist, zu setzen.
     */
    public LesezeichenEntity( String name, String url, OrdnerEntity ordner ) {

        this.name   = name;
        this.url    = url;
        this.ordner = ordner;
    }


    /**
     * Getter für Primärschlüssel des Lesezeichens. Es gibt keinen analogen Setter,
     * weil die ID von JPA verwaltet wird.
     *
     * @return ID des Lesezeichens
     */
    public Long getId() {

        return id;
    }


    /**
     * Anzeigename des Lesezeichen.
     *
     * @return Name von Lesezeichen, z.B. "Fußballnachrichten".
     */
    public String getName() {

        return name;
    }


    public void setName( String name ) {

        this.name = name;
    }


    public String getUrl() {

        return url;
    }


    public void setUrl( String url ) {

        this.url = url;
    }


    /**
     * Getter für den Ordner, in dem das Lesezeichen liegt.
     *
     * @return Ordner
     */
    public OrdnerEntity getOrdner() {

        return ordner;
    }


    /**
     * Setter für den Ordner, in dem das Lesezeichen liegt.
     *
     * @param ordner Ordner
     */
    public void setOrdner( OrdnerEntity ordner ) {

        this.ordner = ordner;
    }


    /**
     * Methode liefert String-Repräsentation des Objekts zurück.
     *
     * @return String mit Name und URL
     */
    @Override
    public String toString() {

        return "Lesezeichen \"" + name + "\": " + url;
    }

}
