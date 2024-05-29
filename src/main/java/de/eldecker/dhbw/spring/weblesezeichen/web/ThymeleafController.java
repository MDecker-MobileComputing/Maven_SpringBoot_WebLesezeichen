package de.eldecker.dhbw.spring.weblesezeichen.web;

import static java.lang.String.format;

import de.eldecker.dhbw.spring.weblesezeichen.db.entities.OrdnerEntity;
import de.eldecker.dhbw.spring.weblesezeichen.db.repos.OrdnerRepo;
import de.eldecker.dhbw.spring.weblesezeichen.logik.OrdnerException;

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
import org.springframework.web.bind.annotation.RequestMapping;
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
    
    /**
     * Repo-Bean für Zugriff auf Tabelle mit Ordnern.
     */
    @Autowired
    private OrdnerRepo _ordnerRepo;


    /**
     * Konstruktor für <i>Dependency Injection</i>.
     */
    @Autowired
    public ThymeleafController( OrdnerRepo ordnerRepo ) {

        _ordnerRepo = ordnerRepo;
    }
    
    
    /**
     * Fehlerbehandlung für {@link OrdnerException }.
     * 
     * @param ex Von Controller-Methode geworfene Exception
     * 
     * @param model Objekt für Platzhalterwerte, die vom Template benötigt werden.
     * 
     * @return Name der Template-Datei "fehler.html" ohne Datei-Endung
     */
    @ExceptionHandler( OrdnerException.class )
    public String ordnerExceptionBehandeln( OrdnerException ex, Model model ) {
        
        final String fehlertext = ex.getMessage();
        
        LOG.error( fehlertext );        
        model.addAttribute( "fehlermeldung", fehlertext );
        
        return "fehler";
    }


    /**
     * Fehlerbehandlung für {@code MethodArgumentTypeMismatchException }.
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
    public String flacheListe( Model model ) {

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
     */
    @GetMapping( "/ordner/{id}" )
    public String ordner( @PathVariable Long id,
                          Model model ) throws OrdnerException {

        final Optional<OrdnerEntity> ordnerOptional = _ordnerRepo.findById( id );
        if ( ordnerOptional.isEmpty() ) {
            
            throw new OrdnerException( "Kein Ordner mit ID=" + id + " gefunden." );
        }
        
        final OrdnerEntity ordner = ordnerOptional.get();
                                        
        final List<OrdnerEntity> unterordnerListe = 
                _ordnerRepo.findByVater_IdOrderByNameAsc( id );
        
        model.addAttribute( "ordner"          , ordner           );
        model.addAttribute( "unterordnerliste", unterordnerListe );
        
        return "ordner-details";
    }


}
