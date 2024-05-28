package de.eldecker.dhbw.spring.weblesezeichen.db;

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
@Table(name = "Ordner")
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
     */
    @ManyToOne( fetch = EAGER )
    @JoinColumn( name = "ordner_fk", referencedColumnName = "id" )
    private OrdnerEntity ordner;           

    
    /**
     * Für JPA obligatorisches Default-Konstruktor.
     */
    public LesezeichenEntity() {
        
        name  = "";
        url   = "";
    }


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


    public Long getId() {
        
        return id;
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
