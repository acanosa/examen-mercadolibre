package com.canosaa.examenmercadolibre.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mutante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String adn;
    private boolean esMutante;
    
    public Mutante(String adn, boolean esMutante){
        this.adn = adn;
        this.esMutante = esMutante;
    }
    
}
