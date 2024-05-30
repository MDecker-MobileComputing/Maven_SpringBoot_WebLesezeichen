package de.eldecker.dhbw.spring.weblesezeichen.web;

import static java.lang.String.format;

import de.eldecker.dhbw.spring.weblesezeichen.db.entities.LesezeichenEntity;
import de.eldecker.dhbw.spring.weblesezeichen.db.entities.OrdnerEntity;
import de.eldecker.dhbw.spring.weblesezeichen.db.repos.LesezeichenRepo;
import de.eldecker.dhbw.spring.weblesezeichen.db.repos.OrdnerRepo;
import de.eldecker.dhbw.spring.weblesezeichen.logik.LesezeichenException;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


/**
 * Controller-Klasse für Thymeleaf-Templates. Jede Mapping-Methode
 * gibt den String mit dem Namen der Template-Datei (ohne Datei-Endung)
 * zurück, die angezeigt werden soll.
 */
@Controller
@RequestMapping( "/app/" )
public class ThymeleafController {

    private final static Logger LOG = LoggerFactory.getLogger( ThymeleafController.class );
    
    /** Repo-Bean für Zugriff auf Tabelle mit Ordnern. */
    private OrdnerRepo _ordnerRepo;
    
    /** Repo-Bean für Zugriff auf Tabelle mit Lesezeichen. */
    private LesezeichenRepo _lesezeichenRepo;


    /**
     * Konstruktor für <i>Dependency Injection</i>.
     */
    @Autowired
    public ThymeleafController( OrdnerRepo ordnerRepo,
                                LesezeichenRepo lesezeichenRepo ) {

        _ordnerRepo      = ordnerRepo;
        _lesezeichenRepo = lesezeichenRepo;
    }
    
    
    /**
     * Fehlerbehandlung für {@link LesezeichenException }: Fehlerseite anzeigen.
     * 
     * @param ex Von Controller-Methode geworfene Exception
     * 
     * @param model Objekt für Platzhalterwerte, die vom Template benötigt werden.
     * 
     * @return Name der Template-Datei "fehler.html" ohne Datei-Endung
     */
    @ExceptionHandler( LesezeichenException.class )
    public String ordnerExceptionBehandeln( LesezeichenException ex, Model model ) {
        
        final String fehlertext = ex.getMessage();
        
        LOG.error( fehlertext );        
        model.addAttribute( "fehlermeldung", fehlertext );
        
        return "fehler";
    }


    /**
     * Fehlerbehandlung für {@code MethodArgumentTypeMismatchException }:
     * Fehlerseite anzeigen.
     * 
     * @param ex Von Controller-Methode geworfene Exception
     * 
     * @param model Objekt für Platzhalterwerte, die vom Template benötigt werden.
     * 
     * @return Name der Template-Datei "fehler.html" ohne Datei-Endung
     */
    @ExceptionHandler( MethodArgumentTypeMismatchException.class )
    public String argumentTypeExceptionBehandeln( MethodArgumentTypeMismatchException ex, 
                                                  Model model) {
    
        final String argName = ex.getName();
        final Object argWert = ex.getValue();
        
        final String fehlertext = format( "Ungültiger Wert \"%s\" für Argument \"%s\" übergeben.", 
                                          argWert, argName );        
        LOG.error( fehlertext );        
        model.addAttribute( "fehlermeldung", fehlertext );
        
        return "fehler";
    }

    
    /**
     * Methode zur Anzeige einer flachen Liste aller Ordner.
     *
     * @param model Objekt für Platzhalterwerte, die vom Template benötigt werden.
     *
     * @return Name der Template-Datei "ordner-liste.html" ohne Datei-Endung
     */
    @GetMapping( "/ordnerliste" )
    public String ordnerListe( Model model ) {

        final List<OrdnerEntity> ordnerListe = _ordnerRepo.findAllByOrderByNameIgnoreCase();

        model.addAttribute( "ordner_liste", ordnerListe );

        return "ordner-liste";
    }


    /**
     * Methode zum Anzeigen eines einzelnen Ordners.
     *
     * @param model Objekt für Platzhalterwerte, die vom Template benötigt werden.
     *
     * @return Name der Template-Datei "ordner-details.html" ohne Datei-Endung
     * 
     * @throws LesezeichenException Ordner mit {@code id} wurde nicht gefunden
     */
    @GetMapping( "/ordner/{id}" )
    public String zeigeOrdner( @PathVariable Long id,
                               Model model ) throws LesezeichenException {

        final Optional<OrdnerEntity> ordnerOptional = _ordnerRepo.findById( id );
        if ( ordnerOptional.isEmpty() ) {
            
            throw new LesezeichenException( "Kein Ordner mit ID=" + id + " gefunden." );
        }
        
        final OrdnerEntity ordner = ordnerOptional.get();
                                        
        final List<OrdnerEntity> unterordnerListe = 
                        _ordnerRepo.findByVater_IdOrderByNameAsc( id );
        
        model.addAttribute( "ordner"          , ordner           );
        model.addAttribute( "unterordnerliste", unterordnerListe );
        
        return "ordner-details";
    }
    
    
    /**
     * Methoden zum Anzeigen einer flachen Liste aller Lesezeichen.
     * 
     * @param model Objekt für Platzhalterwerte, die vom Template benötigt werden
     * 
     * @return Name der Template-Datei "lesezeichen-liste.html" ohne Datei-Endung  
     */
    @GetMapping( "/lesezeichenliste" )
    public String lesezeichenListe( Model model ) {
        
        final List<LesezeichenEntity> lesezeichenListe = 
                                _lesezeichenRepo.findAllByOrderByNameAsc();
        
        model.addAttribute( "lesezeichenliste", lesezeichenListe );
        
        return "lesezeichen-liste";
    }
    
    
    /**
     * Controller um Seite für Anlegen eines neuen Lesezeichens in einem 
     * bestimmten Ordner zurückzuliefern.
     * 
     * @param ordnerId ID von Ordner, in dem neues Lesezeichen angelegt werden soll.
     * 
     * @param model Objekt für Platzhalterwerte, die vom Template benötigt werden
     * 
     * @return Name der Template-Datei "lesezeichen-neu.html" ohne Datei-Endung  
     * 
     * @throws LesezeichenException Ordner mit {@code ordnerId} wurde nicht gefunden
     */
    @GetMapping( "/lesezeichen/neu_formular")
    public String lesezeichenFormular( @RequestParam("ordnerId") Long ordnerId, 
    		                           Model model ) throws LesezeichenException {
    	
    	final Optional<OrdnerEntity> ordnerOptional = _ordnerRepo.findById( ordnerId );
    	if ( ordnerOptional.isEmpty() ) {
    		
    		throw new LesezeichenException( "Kein Ordner mit ID=" + ordnerId + " gefunden." );
    	}
    	
    	model.addAttribute( "ordner", ordnerOptional.get() );
    	
    	return "lesezeichen-neu";
    }
    
    
    /**
     * Methode für eigentliches Anlegen von neuem Lesezeichen.
     * 
     * @param model Objekt für Platzhalterwerte, die vom Template benötigt werden
     * 
     * @param anzeigename Name des neuen Lesezeichens, Pflichtparameter;
     *                    darf nicht nur aus Leerzeichen bestehen
     * 
     * @param url URL des neuen Lesezeichens, Pflichtparameter; 
     *            musst mindestens 12 Zeichen haben
     * 
     * @param ordnerId ID des Ordners, in dem das Lesezeichen angelegt werden soll,
     *                 Pflichtparameter
     * 
     * @return Name der Template-Datei "ordner-details.html" ohne Datei-Endung
     * 
     * @throws LesezeichenException Wenn kein Ordner mit {@code ordnerId} gefunden oder
     *                              ungültige Werte für {@code anzeigename} oder
     *                             {@code url}
     */
    @PostMapping( "/lesezeichen/neu" )
    public String lesezeichenNeu( Model model,
    		                      @RequestParam(value = "anzeigename", required = true  ) String anzeigename,
    		                      @RequestParam(value = "url"        , required = true  ) String url,
    		                      @RequestParam(value = "ordnerId"   , required = true  ) long   ordnerId ) 
    		          throws LesezeichenException {
    	
    	final Optional<OrdnerEntity> ordnerOptional = _ordnerRepo.findById( ordnerId );
    	if ( ordnerOptional.isEmpty() ) {
    		
    		throw new LesezeichenException( "Zielordner für neues Lesezeichen mit ID=" + ordnerId + " nicht gefunden." );
    	}
    	
    	anzeigename = anzeigename.trim();
    	if ( anzeigename.isBlank() ) {
    		
    		throw new LesezeichenException( "Anzeigename für neues Lesezeichen ist leer" );
    	}
    	
    	url = url.trim();
    	if ( url.length() < 12 ) { // kürzestes URL? http://ix.de (hat 12 Zeichen)
    		
    		throw new LesezeichenException( "URL für neues Lesezeichen zu kurz" );
    	}
    	
    	final OrdnerEntity ordner = ordnerOptional.get();
    	
    	LesezeichenEntity lesezeichen = new LesezeichenEntity( anzeigename, url, ordner );
    	lesezeichen = _lesezeichenRepo.save( lesezeichen );
    	LOG.info( "Neues Lesezeichen mit ID={} angelegt.", lesezeichen.getId() );
    	
        final List<OrdnerEntity> unterordnerListe = 
                _ordnerRepo.findByVater_IdOrderByNameAsc( ordnerId );

        final String nachricht = "Neues Lesezeichen \"" + anzeigename + "\" angelegt.";
        
		model.addAttribute( "ordner"          , ordner           );
		model.addAttribute( "unterordnerliste", unterordnerListe );
		model.addAttribute( "nachricht"       , nachricht        );
    	
    	return "ordner-details";
    }

}
