package de.eldecker.dhbw.spring.weblesezeichen.db.repos;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;
import de.eldecker.dhbw.spring.weblesezeichen.db.entities.LesezeichenEntity;
import de.eldecker.dhbw.spring.weblesezeichen.db.entities.OrdnerEntity;


/**
 * Dieses Interface wird für die Annotation {@code RepositoryRestResource}
 * benötigt, mit der die Klasse {@link OrdnerRepo} versehen ist. Es wird
 * dadurch gesteuert, welche Attribute in der JSON-Antwort des automatisch
 * erzeugten REST-Endpunkts auftauchen.
 */
@Projection(name = "inlineLesezeichen", types = { OrdnerEntity.class })
public interface InlineLesezeichen {

    /** Name des Ordners soll in der REST-Antwort enthalten sein. */ 
    String getName();
    
    /** Die Lesezeichen selbst sollen auch in der REST-Antwort enthalten sein. */ 
    List<LesezeichenEntity> getLesezeichen();
    
}
