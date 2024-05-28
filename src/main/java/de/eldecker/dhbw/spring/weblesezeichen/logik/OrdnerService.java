package de.eldecker.dhbw.spring.weblesezeichen.logik;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.eldecker.dhbw.spring.weblesezeichen.db.entities.LesezeichenEntity;
import de.eldecker.dhbw.spring.weblesezeichen.db.entities.OrdnerEntity;
import de.eldecker.dhbw.spring.weblesezeichen.db.repos.OrdnerRepo;


/**
 * Diese Service-Bean-Klasse enthält Methoden mit Logik für die Arbeit
 * mit {@link OrdnerEntity}-Objekten.
 */
@Service
public class OrdnerService {

    @Autowired
    private OrdnerRepo _ordnerRepo;
    
    
    /**
     * Gibt den Pfad vom gegebenen {@link OrdnerEntity} Knoten zur Wurzel zurück.
     *
     * @param ordner Der {@link OrdnerEntity} Knoten, von dem aus der Pfad startet. Dieser Knoten und alle seine Vorfahren
     *              werden in der zurückgegebenen Liste enthalten sein.
     *
     * @return Eine Liste von {@link OrdnerEntity} Objekten, die den Pfad vom gegebenen Knoten zur Wurzel darstellt.
     *         Die Liste beginnt mit dem gegebenen Knoten {@code ordner} und endet mit der Wurzel.
     *         Wenn der gegebene Knoten die Wurzel ist, enthält die Liste nur diesen Knoten.
     */
    public List<OrdnerEntity> getPfadZurWurzel( OrdnerEntity ordner ) {

        final List<OrdnerEntity> ergebnisListe = new ArrayList<>();

        OrdnerEntity aktuellerOrdner = ordner;

        while (aktuellerOrdner != null) {

            ergebnisListe.add( aktuellerOrdner );
            aktuellerOrdner = aktuellerOrdner.getVater();
        }

        return ergebnisListe;
    }
    
    public void addLesezeichen( OrdnerEntity ordner, LesezeichenEntity lesezeichen ) {
        
        lesezeichen.setOrdner( ordner );
        _ordnerRepo.save( ordner );
    }
    
    public OrdnerEntity addLesezeichen( long ordnerId, LesezeichenEntity lesezeichen ) 
           throws OrdnerException {
        
        final Optional<OrdnerEntity> ordnerOptional = _ordnerRepo.findById( ordnerId );
        if ( ordnerOptional.isPresent() ) {
         
            final OrdnerEntity ordner = ordnerOptional.get();
            addLesezeichen( ordner, lesezeichen );
            return ordner;
            
        } else {

            throw new OrdnerException( "Kein Ordner mit ID=" + ordnerId + " gefunden." );
        }        
    }

}
