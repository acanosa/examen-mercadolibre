package com.canosaa.examenmercadolibre.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EstadisticaTestMutantes {
    
    @JsonProperty("count_mutant_dna")
    private int cantidadMutantes;
    @JsonProperty("count_human_dna")
    private int cantidadHumanos;
    @JsonProperty("ratio")
    private BigDecimal proporcion;
    
}
