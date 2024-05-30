package de.eldecker.dhbw.spring.weblesezeichen.logik;


/**
 * Applikations-spezifische Exception-Klasse.
 */
@SuppressWarnings("serial")
public class LesezeichenException extends Exception {

    /**
     * Konstruktor, um Exception mit Fehlerbeschreibung zu erzeugen.
     * 
     * @param msg Fehlerbeschreibung
     */
    public LesezeichenException( String msg ) {

        super( msg );
    }
    
}
