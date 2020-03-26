package com.canosaa.examenmercadolibre.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class AdnMutante {
    
    @JsonAlias("dna")
    private String[] cadenasAdn;
    
}
