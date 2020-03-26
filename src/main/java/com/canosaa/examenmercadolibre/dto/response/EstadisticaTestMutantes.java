package com.canosaa.examenmercadolibre.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EstadisticaTestMutantes {
    
    @JsonAlias("count_mutant_dna")
    private int cantidadMutantes;
    @JsonAlias("count_human_dna")
    private int cantidadHumanos;
    @JsonAlias("ratio")
    private BigDecimal proporcion;
    
}
