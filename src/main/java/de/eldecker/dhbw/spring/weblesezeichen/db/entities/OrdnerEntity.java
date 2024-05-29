package de.eldecker.dhbw.spring.weblesezeichen.db.entities;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.AUTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;


/**
 * Ein Ordner mit Lesezeichen, der 0 bis n Unterordner haben kann;
 * die Ordner bilden also eine Baumstruktur.
 */
@Entity
@Table( name = "Ordner" )
public class OrdnerEntity {

    /**
     * Primärschlüssel, wird von JPA verwaltet.
     */
    @Id
    @GeneratedValue( strategy = AUTO )
    private Long id;


    /**
     * Ordnername (Anzeigename), z.B. "Nachrichtenseiten".
     * Über die Annotation wird der Wert dieser Spalte als eindeutig
     * definiert, es wird damit auch automatisch ein Index angelegt.
     */
    @Column( unique = true )
    private String name;


    /**
     * Direkter Vorgängerknoten, also der Ordner, in dem
     * der vorliegende Ordner als Unterordner enthalten ist.
     */
    @ManyToOne( fetch = LAZY )
    @JoinColumn( name = "vater_knoten_fk", referencedColumnName = "id" )
    private OrdnerEntity vater;


    /**
     * Im Ordner enthaltene Lesezeichen, welche aufsteigend nach Name
     * sortiert sind; kann leer sein, weil ein Ordner evtl. noch
     * keine Lesezeichen enthält oder nur Unterordner enthalten soll.
     */
    @OneToMany( mappedBy = "ordner" )
    @OrderBy( "name ASC" )
    private List<LesezeichenEntity> lesezeichen = new ArrayList<>( 10 );


    /**
     * Default-Konstruktor, obligatorisch für JPA!
     */
    public OrdnerEntity() {

        name = "";
    }


    /**
     * Konstruktor um Ordner mit {@code name} anzulegen.
     *
     * @param name Name des Ordners, z.B. "Nachrichtenseiten"
     */
    public OrdnerEntity( String name ) {

        this.name = name;
    }


    /**
     * Getter für Primärschlüssel.
     *
     * @return ID
     */
    public Long getId() {

        return id;
    }


    /**
     * Getter für Anzeigename von Ordner.
     *
     * @return Name des Ordners
     */
    public String getName() {

        return name;
    }


    /**
     * Setter für Anzeigename von Ordner.
     *
     * @param name Name des Ordners
     */
    public void setName( String name ) {

        this.name = name;
    }

    
    /**
     * Getter für direkten Vorgängerknoten; ist für
     * Wurzelordner {@code null}.
     *
     * @return Vorgängerknoten
     */
    public OrdnerEntity getVater() {

        return vater;
    }

    /**
     * Setter für direkten Vorgängerknoten.
     * (Wurzelknoten hat {@code null} als Vorgängerknoten).
     *
     * @param vater Vorgängerknoten
     */
    public void setVater( OrdnerEntity vater ) {

        this.vater = vater;
    }


    /**
     * Convenience-Methode zur Abfrage, ob Ordner der Wurzelordner
     * ist (oberster Ordner in der Ordnerhierarchie).
     * 
     * @return {@code true} wenn Ordner keinen Vaterknoten hat, 
     *         also {@code getVater() == null}
     */
    public  boolean istWurzel() {
        
        return getVater() == null;
    }
    
    

    /**
     * Getter für Lesezeichen aus diesem Ordner.
     * <br><br>
     * 
     * Es gibt keinen zugehörigen Setter in dieser Klasse,
     * weil neue Lesezeichen über die {@link LesezeichenEntity}, 
     * die die entsprechende Fremdschlüsselinformation speichert, 
     * vorgenommen werden sollen.
     * 
     * @return Liste der Lesezeichen
     */
    public List<LesezeichenEntity> getLesezeichen() {
        
        return lesezeichen;
    }


    /**
     * String-Repräsentation des Objekts zurückgeben.
     *
     * @return String-Repräsentation des Objekts, enthält u.a. Ordnername
     */
    @Override
    public String toString() {

        return "Lesezeichenordner \"" + name + "\".";
    }


    /**
     * Hashcode von Objekt berechnen.
     * 
     * @return Hashcode, berücksichtigt Attribute "name", "vater" und "lesezeichen".
     */
    @Override
    public int hashCode() {
        
        return Objects.hash( name, vater, lesezeichen );
    }


    /**
     * Prüft aufrufendes Objekt auf Gleichheit mit {@code obj}. 
     * 
     * @return {@code true} gdw. {@code obj} auch eine Instanz von {@link OrdnerEntity}
     *         ist und die Attribute "name", "vater" und "lesezeichen" denselben Wert
     *         haben.
     */
    @Override
    public boolean equals( Object obj ) {
        
        if ( this == obj ) {
            
            return true;
        }            
        if ( obj == null ) {
            
            return false;
        }
        
        if ( obj instanceof OrdnerEntity andererOrdner ) {
        
            return Objects.equals( name       , andererOrdner.name        ) && 
                   Objects.equals( vater      , andererOrdner.vater       ) &&
                   Objects.equals( lesezeichen, andererOrdner.lesezeichen );
            
        } else {
            
            return false;
        }               
    }

}
