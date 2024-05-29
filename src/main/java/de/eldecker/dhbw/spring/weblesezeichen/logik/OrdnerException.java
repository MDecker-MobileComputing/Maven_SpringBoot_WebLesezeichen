package de.eldecker.dhbw.spring.weblesezeichen.logik;


/**
 * Applikations-spezifische Exception-Klasse.
 */
@SuppressWarnings("serial")
public class OrdnerException extends Exception {

    /**
     * Konstruktor, um Exception mit Fehlerbeschreibung
     * zu erzeugen.
     * 
     * @param msg Fehlerbeschreibung
     */
    public OrdnerException( String msg ) {

        super( msg );
    }
    
}
