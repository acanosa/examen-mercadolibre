package com.canosaa.examenmercadolibre.service;

import com.canosaa.examenmercadolibre.ExamenMercadolibreApplicationTests;
import com.canosaa.examenmercadolibre.domain.Mutante;
import com.canosaa.examenmercadolibre.dto.response.EstadisticaTestMutantes;
import com.canosaa.examenmercadolibre.repository.MutanteRepository;
import java.math.BigDecimal;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MutanteServiceTest extends ExamenMercadolibreApplicationTests {

    @Autowired
    MutanteService mutanteService;
    
    @Autowired
    MutanteRepository mutanteRepository;

    @Test
    public void esMutante_conCadenaAdnMutanteConLetrasConjuntasHorizontalYEnDiagonalDerecha_devuelveTrueYGuardaMutanteEnBaseDeDatos() {
        String[] adn = {"ACTACG", "CATTTT", "GTACAT", "GATAGG", "CGCCTA", "TCACTG"};

        boolean esMutante = mutanteService.esMutante(adn);

        assertThat(esMutante).isTrue();
        assertMutanteGuardado(adn, esMutante);
    }

    @Test
    public void esMutante_conCadenaAdnMutanteConLetrasConjuntasHorizontalYVertical_devuelveTrue() {
        String[] adn = {"GCTACG", "GATTTT", "GTTCAT", "GATGGG", "CGCCTA", "TCACTG"};

        boolean esMutante = mutanteService.esMutante(adn);

        assertThat(esMutante).isTrue();
        assertMutanteGuardado(adn, esMutante);
    }

    @Test
    public void esMutante_conCadenaAdnMutanteConLetrasConjuntasDiagonalDerechaYVertical_devuelveTrue() {
        String[] adn = {"GATACG", "GCACAT", "GTCAAT", "GATAAG", "CGCCTA", "TCACTG"};

        boolean esMutante = mutanteService.esMutante(adn);

        assertThat(esMutante).isTrue();
        assertMutanteGuardado(adn, esMutante);
    }

    @Test
    public void esMutante_conCadenaAdnMutanteConLetrasConjuntasDiagonalIzquierdaYVertical_devuelveTrue() {
        String[] adn = {"GCTCCG", "GCACGT", "GTCGCT", "GAGAAG", "CGCCTA", "TCACTG"};

        boolean esMutante = mutanteService.esMutante(adn);

        assertThat(esMutante).isTrue();
        assertMutanteGuardado(adn, esMutante);
    }

    @Test
    public void esMutante_conCadenaAdnMutanteConLetrasInvalidas_devuelveFalse() {
        String[] adn = {"HHHHLK", "MMMMQR", "ZXCVBB", "NMHJKB", "LLKKMM", "YYUUII"};

        boolean esMutante = mutanteService.esMutante(adn);

        assertThat(esMutante).isFalse();
        assertMutanteGuardado(adn, esMutante);
    }

    @Test
    public void esMutante_conCadenaAdnMutanteNull_lanzaExcepcion() {
        String[] adn = null;

        assertThatThrownBy(() -> mutanteService.esMutante(adn))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("La cadena de ADN es obligatoria");
    }

    @Test
    public void esMutante_conCadenaAdnConDiferentesCantidadesDeLetrasEnCadaComponente_lanzaExcepcion() {
        String[] adn = {"CGT", "GCACGT", "G", "U", "CGCCTA", "TCACTG"};

        assertThatThrownBy(() -> mutanteService.esMutante(adn))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Todas las cadenas de caracteres deben tener una longitud "
                        + "igual a la cantidad de cadenas que tiene el ADN para determinar la validez del mutante");
    }

    @Test
    public void esMutante_conCadenaAdnMutanteVacia_lanzaExcepcion() {
        String[] adn = {};

        assertThatThrownBy(() -> mutanteService.esMutante(adn))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("La cadena de ADN es obligatoria");
    }
    
    @Test
    public void obtenerEstadisticas_con1MutanteY1Humano_devuelveUnMutanteDosHumanosYCeroComaCincoDeProporcion(){
        String[] adnMutante = {"ACTACG", "CATTTT", "GTACAT", "GATAGG", "CGCCTA", "TCACTG"};
        String[] adnHumano = {"AATTCG", "CGTTTT", "GTGCAT", "TATCGG", "AGCCTA", "TCACTG"};
        String[] adnHumano2 = {"AAGTCG", "CGTATT", "GTTCAT", "CATCGG", "AGCCTA", "TCACTG"};
        Mutante mutante = new Mutante(String.join(", ", adnMutante), true);
        Mutante humano = new Mutante(String.join(", ", adnHumano), false);
        Mutante humano2 = new Mutante(String.join(", ", adnHumano2), false);
        
        mutanteRepository.deleteAll();
        
        mutanteRepository.save(mutante);
        mutanteRepository.save(humano);
        mutanteRepository.save(humano2);
        
        EstadisticaTestMutantes estadisticas = mutanteService.obtenerEstadisticas();
        assertThat(estadisticas.getCantidadMutantes()).isEqualTo(1);
        assertThat(estadisticas.getCantidadHumanos()).isEqualTo(2);
        assertThat(estadisticas.getProporcion()).isEqualTo(new BigDecimal("0.50"));
    }
    
    private void assertMutanteGuardado(String[] adn, boolean esMutante) {
        long idUltimoMutanteGuardado = mutanteRepository.count();
        Mutante mutante = new Mutante(String.join(", ", adn), esMutante);
        Optional<Mutante> mutanteGuardado = mutanteRepository.findById(idUltimoMutanteGuardado);
        assertThat(mutanteGuardado).isNotEmpty();
        assertThat(mutanteGuardado.get().getAdn()).isEqualTo(mutante.getAdn());
        assertThat(mutanteGuardado.get().isEsMutante()).isEqualTo(mutante.isEsMutante());
    }
    
}
