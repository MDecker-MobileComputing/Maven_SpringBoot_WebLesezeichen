package de.eldecker.dhbw.spring.weblesezeichen.logik;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import de.eldecker.dhbw.spring.weblesezeichen.db.entities.OrdnerEntity;
import de.eldecker.dhbw.spring.weblesezeichen.db.repos.OrdnerRepo;


/**
 * Die Bean dieser Klasse importiert bei Bedarf unmittelbar nach dem Start
 * der Anwendung einige Beispieldaten.
 */
@Component
public class BeispielDatenImporter implements ApplicationRunner {

    private final static Logger LOG = LoggerFactory.getLogger( BeispielDatenImporter.class );
    
    /**
     * Repo-Bean für Zugriff auf Tabelle mit Ordnern.
     */
    @Autowired
    private OrdnerRepo _ordnerRepo;
    
    
    /**
     * Diese Methode wird unmittelbar nach Initialisierung der Spring-Boot-App ausgeführt.
     * Sie überprüft, ob die Ordner-Tabelle leer ist; wenn dies der Fall ist, dann 
     * fügt Sie einige Beispieldatensätze ein.
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
            
            final OrdnerEntity wurzelOrdner = new OrdnerEntity( "Wurzel" );
            
            final OrdnerEntity wurzelPrivat  = new OrdnerEntity( "Privat"  );
            final OrdnerEntity wurzelStudium = new OrdnerEntity( "Studium" );
            
            wurzelPrivat.setVater( wurzelOrdner );
            wurzelStudium.setVater( wurzelOrdner );
            
            final OrdnerEntity wurzelStudiumWiwi = new OrdnerEntity( "WiWi" );
            final OrdnerEntity wurzelStudiumInfo = new OrdnerEntity( "WiInfo" );
            
            wurzelStudiumWiwi.setVater( wurzelStudium );
            wurzelStudiumInfo.setVater( wurzelStudium );
            
            List<OrdnerEntity> ordnerListe = List.of( wurzelOrdner, 
                                                      wurzelPrivat, wurzelStudium,
                                                      wurzelStudiumWiwi, wurzelStudiumInfo );            
            _ordnerRepo.saveAll( ordnerListe );           
            
            // https://www.kicker.de/
            // https://www.spiegel.de/
        }
    }
    
}
