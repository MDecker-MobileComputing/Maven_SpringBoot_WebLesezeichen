package de.eldecker.dhbw.spring.weblesezeichen.db;

import static jakarta.persistence.GenerationType.AUTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * Ein Ordner mit Lesezeichen, der 0 bis n Unterordner haben kann; 
 * die Ordner bilden also eine Baumstruktor.
 */
@Entity
@Table(name = "Ordner")
public class OrdnerEntity {

    /**
     * Primärschlüssel, wird von JPA verwaltet.
     */
    @Id
    @GeneratedValue( strategy = AUTO )
    private Long id;

    
    /** Ordnername (Anzeigename), z.B. "Nachrichtenseiten". */
    private String name;
    
    /**
     * Direkter Vorgängerknoten, also der Ordner, in dem
     * der vorliegende Ordner enthalten ist.
     */
    private OrdnerEntity vater;

    
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

    
    public Long getId() {
        return id;
    }

            
    public String getName() {
        
        return name;
    }


    public void setName( String name ) {
        
        this.name = name;
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
    
}
