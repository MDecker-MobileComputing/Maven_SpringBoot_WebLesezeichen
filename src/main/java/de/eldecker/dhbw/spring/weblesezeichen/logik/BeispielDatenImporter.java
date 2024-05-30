package de.eldecker.dhbw.spring.weblesezeichen.logik;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import de.eldecker.dhbw.spring.weblesezeichen.db.entities.LesezeichenEntity;
import de.eldecker.dhbw.spring.weblesezeichen.db.entities.OrdnerEntity;
import de.eldecker.dhbw.spring.weblesezeichen.db.repos.LesezeichenRepo;
import de.eldecker.dhbw.spring.weblesezeichen.db.repos.OrdnerRepo;


/**
 * Die Bean dieser Klasse importiert bei Bedarf unmittelbar nach dem Start
 * der Anwendung einige Beispieldaten.
 */
@Component
public class BeispielDatenImporter implements ApplicationRunner {

    private final static Logger LOG = LoggerFactory.getLogger( BeispielDatenImporter.class );
    
    /** Repo-Bean für Zugriff auf Ordner. */
    private OrdnerRepo _ordnerRepo;
    
    /** Repo-Bean für Zugriff auf Lesezeichen. */
    private LesezeichenRepo _lesezeichenRepo;
    
    /** Liste der anzulegenden Ordner, wird auf einmal auf DB geschrieben. */
    private List<OrdnerEntity> _ordnerListe = new ArrayList<>( 10 );
    
    /** Liste der anzulegenden Lesezeichen, wird auf einmal auf DB geschrieben. */
    private List<LesezeichenEntity> _lesezeichenListe = new ArrayList<>( 10 );
    
    
    /**
     * Konstruktor für <i>Dependency Injection</i>:
     */
    @Autowired
    public BeispielDatenImporter( OrdnerRepo ordnerRepo,
                                  LesezeichenRepo lesezeichenRepo ) {
        
        _ordnerRepo      = ordnerRepo;
        _lesezeichenRepo = lesezeichenRepo;
    }
    
    
    /**
     * Hilfsmethode um neuen Ordner anzulegen. Der neue Ordner wird im Member-Array
     * gespeichert, damit alle Ordner in einer Batch-Operation auf die DB geschrieben
     * werden können.
     * 
     * @param name Anzeigename neues Lesezeichen
     * 
     * @param vaterOrdner Ordner, in dem der anzulegende Ordner enthalten ist;
     *                    für Wurzelordner (obersten Ordner) ist {@code null}
     *                    zu übergeben.
     *                    
     * @return neu angelegtes Ordnerobjekt; diese Referenz wird u.U. benötigt,
     *         wenn ein weiterer Ordner in dem gerade neu angelegten Ordner
     *         enthalten sein soll.
     */
    private OrdnerEntity neuerOrdner( String name, OrdnerEntity vaterOrdner ) {
    	
    	final OrdnerEntity ordner = new OrdnerEntity( name );
    	
    	if ( vaterOrdner != null ) {
    		
    		ordner.setVater( vaterOrdner );
    	}
    		
    	_ordnerListe.add( ordner );
    	return ordner;
    }
    
    
    /**
     * Neues Web-Lesezeichen anlegen.
     * 
     * @param name Anzeigename des Lesezeichen
     * 
     * @param url Eigentliche URL
     * 
     * @param ordner Ordner, in dem das Lesezeichen enthalten sein soll
     */
    private void neuesLesezeichen( String name, String url, OrdnerEntity ordner ) {
    	
    	final LesezeichenEntity lesezeichen = new LesezeichenEntity( name, url, ordner );
    	
    	_lesezeichenListe.add( lesezeichen );
    }
    
    
    /**
     * Diese Methode wird unmittelbar nach Initialisierung der Spring-Boot-App ausgeführt.
     * Sie überprüft, ob die Ordner-Tabelle leer ist; wenn dies der Fall ist, dann 
     * fügt Sie einige Beispieldatensätze ein.
     * <br><br>
     * 
     * Ordnerhierachie:
     * <pre>
     * Wurzel
     *   Studium
     *     Informatik
     *     Wirtschaft	
     *   Privat
     * </pre>
     * 
     * @param args Wird nicht ausgewertet
     */
    @Override
    public void run( ApplicationArguments args ) throws Exception {
    
        long anzahlOrdner = _ordnerRepo.count();
        if ( anzahlOrdner > 0 ) {
            
            LOG.info( "Es sind schon {} Ordner vorhanden, deshalb kein Import von Beispieldaten.", 
                      anzahlOrdner );             
        } else {
            
            final OrdnerEntity ordnerWurzel = neuerOrdner( "Wurzel", null );
            
            final OrdnerEntity ordnerPrivat  = neuerOrdner( "Privat" , ordnerWurzel );
            final OrdnerEntity ordnerStudium = neuerOrdner( "Studium", ordnerWurzel );
            
            final OrdnerEntity ordnerWirtschaft = neuerOrdner( "Wirtschaft", ordnerStudium );
            final OrdnerEntity ordnerInformatik = neuerOrdner( "Informatik", ordnerStudium );
            
            final OrdnerEntity ordnerJava = neuerOrdner( "Java", ordnerInformatik );
                      
            _ordnerRepo.saveAll( _ordnerListe );                 
            LOG.info( "Anzahl Ordner angelegt: {}", _ordnerRepo.count() );

            
            neuesLesezeichen( "Fußballnachrichten",
            		          "https://www.kicker.de/",
            		          ordnerPrivat );
            		
            neuesLesezeichen( "FAZ (Nachrichten)", 
            		          "https://www.faz.net/faz-live",
            		          ordnerPrivat );
            		
           neuesLesezeichen( "IT-Nachrichten von Heise",
        		             "https://www.heise.de/newsticker/",
        		             ordnerInformatik );

           neuesLesezeichen( "Spring: JPA Query Methods", 
                             "https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html#jpa.query-methods.query-creation",
                             ordnerJava );
           
           neuesLesezeichen( "Java 21: API-Doc", 
        		             "https://docs.oracle.com/en/java/javase/21/docs/api/java.base/module-summary.html",
        		             ordnerJava );  
           
           neuesLesezeichen( "Unicum: Tipps fürs Studium",
        		             "https://www.unicum.de/studium-tipps",
                             ordnerStudium );
           
           neuesLesezeichen( "Handelsblatt-Ticket",
        		             "https://www.handelsblatt.com/ticker/",
        		             ordnerWirtschaft );
           
           neuesLesezeichen( "WiSu", 
        		             "https://www.wisu.de/",
        		             ordnerWirtschaft );
                   
           _lesezeichenRepo.saveAll( _lesezeichenListe );
           LOG.info( "Anzahl Lesezeichen angelegt: {}", _lesezeichenRepo.count() );
        }
    }
    
}
