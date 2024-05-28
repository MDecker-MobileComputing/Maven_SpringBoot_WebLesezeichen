package de.eldecker.dhbw.spring.weblesezeichen.web;

import de.eldecker.dhbw.spring.weblesezeichen.db.entities.OrdnerEntity;
import de.eldecker.dhbw.spring.weblesezeichen.db.repos.OrdnerRepo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Controller-Klasse für Thymeleaf-Templates. Jede Mapping-Methode
 * gibt den String mit dem Namen der Template-Datei (ohne Datei-Endung)
 * zurück, die angezeigt werden soll.
 */
@Controller
@RequestMapping( "/app/" )
public class ThymeleafController {

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
     * Methode zur Anzeige einer flachen Liste aller Ordner. 
     * 
     * @param model Objekt für Platzhalterwerte, die vom Template benötigt werden.
     * 
     * @return Name der Template-Datei "ordner-liste.html" ohne Datei-Endung.
     */
    @GetMapping( "/ordnerliste" )
    public String flacheListe( Model model ) {
        
        final List<OrdnerEntity> ordnerListe = _ordnerRepo.findAllByOrderByNameIgnoreCase();
        
        model.addAttribute( "ordner_liste", ordnerListe );
        
        return "ordner-liste";
    }
    
}
