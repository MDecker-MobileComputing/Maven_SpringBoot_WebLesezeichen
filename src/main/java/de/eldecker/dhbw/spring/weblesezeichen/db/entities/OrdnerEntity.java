package de.eldecker.dhbw.spring.weblesezeichen.db.entities;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.AUTO;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
     * der vorliegende Ordner als Unterordner enthalten ist.
     */
    @ManyToOne( fetch = LAZY )
    @JoinColumn( name = "vater_knoten_fk", referencedColumnName = "id" )    
    private OrdnerEntity vater;
    
    
    @OneToMany(mappedBy = "vater", fetch = LAZY)
    private List<OrdnerEntity> kinder;

    // ...

    public List<OrdnerEntity> getKinder() {
        
        return kinder;
    }
    
    public void setKinder( List<OrdnerEntity> kinderListe ) {
        
        kinder = kinderListe;
    }
    
    
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
    
    
    public OrdnerEntity getVater() {
        
        return vater;
    }


    public void setVater( OrdnerEntity vater ) {
        
        this.vater = vater;
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