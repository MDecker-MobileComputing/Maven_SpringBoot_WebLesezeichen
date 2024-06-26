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
                                                  Model model ) {

        final String argName = ex.getName();
        final Object argWert = ex.getValue();

        final String fehlertext = format( "Ungültiger Wert \"%s\" für Argument \"%s\" übergeben.",
                                          argWert, argName );
        LOG.error( fehlertext );
        model.addAttribute( "fehlermeldung", fehlertext );

        return "fehler";
    }


    /**
     * Hilfsmethode, die Ordner mit {@code ordnerID} holten.
     *
     * @param ordnerID ID von Ordner, der zu holen ist
     *
     * @return Ordner mit {@code ordnerId}
     *
     * @throws LesezeichenException Wenn kein Ordner mit {@code ordnerId} gefunden
     */
    private OrdnerEntity holeOrdner( long ordnerID ) throws LesezeichenException {

        final Optional<OrdnerEntity> ordnerOptional = _ordnerRepo.findById( ordnerID );
        if ( ordnerOptional.isEmpty() ) {

            throw new LesezeichenException( "Kein Ordner mit ID=" + ordnerID + " gefunden." );

        } else {

            return ordnerOptional.get();
        }
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
     * @param id ID des Ordners
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

        final OrdnerEntity ordner = holeOrdner( id ); // throws LesezeichenException

        final List<OrdnerEntity> unterordnerListe =
                        _ordnerRepo.findByVater_IdOrderByNameAsc( id );

        model.addAttribute( "ordner"          , ordner           );
        model.addAttribute( "unterordnerliste", unterordnerListe );

        return "ordner-details";
    }


    /**
     * Methode zum Anzeigen des Wurzelordners; verwendet intern die Methode
     * {@link #zeigeOrdner(Long, Model)}.
     *
     * @param model Objekt für Platzhalterwerte, die vom Template benötigt werden.
     *
     * @return Name der Template-Datei "ordner-details.html" ohne Datei-Endung
     *
     * @throws LesezeichenException Wenn Wurzelknoten nicht gefunden (Datenbank
     *                              wurde noch nicht initialisiert?)
     */
    @GetMapping( "/ordner/wurzel" )
    public String zeigeWurzelOrdner( Model model ) throws LesezeichenException {

        final Optional<OrdnerEntity> ordnerOptional = _ordnerRepo.findByVaterIsNull();
        if ( ordnerOptional.isEmpty() ) {

            throw new LesezeichenException( "Wurzelknoten nicht gefunden" );
        }

        final long ordnerId = ordnerOptional.get().getId();

        return zeigeOrdner( ordnerId, model );
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
    public String lesezeichenNeuFormular( @RequestParam("ordnerId") Long ordnerId,
    		                              Model model ) throws LesezeichenException {

        final OrdnerEntity ordner = holeOrdner( ordnerId ); // throws LesezeichenException

    	model.addAttribute( "ordner", ordner );

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
     *            musst mindestens 12 Zeichen haben und entweder mit
     *            "http://" oder "https://" anfangen. Das input-Element
     *            hat {@code type="url"}, deshalb sollten aktuelle
     *            Browser ein Absenden des Formulars nur erlauben, wenn
     *            eine gültige URL eingegeben ist.
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

        final OrdnerEntity ordner = holeOrdner( ordnerId ); // throws LesezeichenException

    	anzeigename = anzeigename.trim();
    	if ( anzeigename.isBlank() ) {

    		throw new LesezeichenException( "Anzeigename für neues Lesezeichen ist leer" );
    	}

    	url = url.trim();
    	if ( url.length() < 12 ) { // kürzestes URL? http://ix.de (hat 12 Zeichen)

    		throw new LesezeichenException( "URL für neues Lesezeichen zu kurz" );
    	}
    	if ( !url.startsWith( "http://") && !url.startsWith( "https://") ) {

    		throw new LesezeichenException( "URL für neues Lesezeichen fängt nicht mit http(s):// an." );
    	}

    	LesezeichenEntity lesezeichen = new LesezeichenEntity( anzeigename, url, ordner );
    	lesezeichen = _lesezeichenRepo.save( lesezeichen );
    	LOG.info( "Neues Lesezeichen \"{}\" mit ID={} angelegt.", anzeigename, lesezeichen.getId() );

        final List<OrdnerEntity> unterordnerListe =
                _ordnerRepo.findByVater_IdOrderByNameAsc( ordnerId );

        final String nachricht = "Neues Lesezeichen \"" + anzeigename + "\" angelegt.";

		model.addAttribute( "ordner"          , ordner           );
		model.addAttribute( "unterordnerliste", unterordnerListe );
		model.addAttribute( "nachricht"       , nachricht        );

    	return "ordner-details";
    }


    /**
     * Methode zum Anzeigen der Seite/Formular für Anlegen Unterordner.
     *
     * @param ordnerId ID von Ordner, in dem der neue Ordner als Unterordner angelegt
     *                 werden soll
     *
     * @param model Objekt für Platzhalterwerte, die vom Template benötigt werden
     *
     * @return Name der Template-Datei "ordner-neu.html" ohne Datei-Endung
     *
     *  @throws LesezeichenException Ordner mit {@code ordnerId} nicht gefunden
     */
    @GetMapping( "/ordner/neu_formular")
    public String ordnerNeuFormular( @RequestParam(value = "ordnerId", required = true ) long ordnerId,
                                     Model model ) throws LesezeichenException {

        final OrdnerEntity ordner = holeOrdner( ordnerId ); // throws LesezeichenException

        model.addAttribute( "ordner", ordner );

        return "ordner-neu";
    }


    /**
     * Methode für eigentliches Anlegen von neuem Unterordner.
     *
     * @param ordnerId ID von Ordner, in dem der neue Ordner als Unterordner angelegt werden soll;
     *                 Pflichtparameter
     *
     * @param ordnername Name von neuem Ordner; Pflichtparameter.
     *                   Es darf nicht schon einen anderen Ordner mit diesem Namen geben (case-insensitiver
     *                   Vergleich), siehe auch die {@code unique}-Constraint für das Attribut {@code name}
     *                   der Klasse {@link OrdnerEntity}.
     *
     * @param model Objekt für Platzhalterwerte, die vom Template benötigt werden
     *
     * @return Name der Template-Datei "ordner-details.html" ohne Datei-Endung
     *
     * @throws LesezeichenException Ordner mit {@code ordnerId} wurde nicht gefunden,
     *                              {@code ordnername} ist leer oder wird schon für einen
     *                              anderen Ordner verwendet
     */
    @PostMapping( "/ordner/neu")
    public String ordnerNeu( @RequestParam(value = "ordnerId"  , required = true  ) long   ordnerId  ,
                             @RequestParam(value = "ordnername", required = true  ) String ordnername,
                             Model model ) throws LesezeichenException {

        final OrdnerEntity ordner = holeOrdner( ordnerId ); // throws LesezeichenException

        ordnername = ordnername.trim();
        if ( ordnername.isBlank() ) {

            throw new LesezeichenException( "Leerer Name für neuen Ordner" );
        }

        final List<OrdnerEntity> ordnerListe = _ordnerRepo.findByNameIgnoreCase( ordnername );
        if ( !ordnerListe.isEmpty() ) {

            throw new LesezeichenException( "Es gibt schon einen Ordner mit dem Namen \"" + ordnername + "\"." );
        }

        OrdnerEntity ordnerNeu = new OrdnerEntity( ordnername, ordner );
        ordnerNeu = _ordnerRepo.save( ordnerNeu );

        LOG.info( "Neuer Ordner \"{}\" mit ID={} angelegt.", ordnername, ordnerNeu.getId() );

        model.addAttribute( "ordner"          , ordnerNeu );
        model.addAttribute( "unterordnerliste", null      );

        return "ordner-details";
    }

}
