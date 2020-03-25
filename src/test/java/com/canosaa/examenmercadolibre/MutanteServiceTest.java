package com.canosaa.examenmercadolibre;

import com.canosaa.examenmercadolibre.service.MutanteService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MutanteServiceTest extends ExamenMercadolibreApplicationTests {

    @Autowired
    MutanteService mutanteService;

    @Test
    public void esMutante_conCadenaAdnMutanteConLetrasConjuntasHorizontalYEnDiagonalDerecha_devuelveTrue() {
        String[] adn = {"ACTACG", "CATTTT", "GTACAT", "GATAGG", "CGCCTA", "TCACTG"};

        boolean esMutante = mutanteService.esMutante(adn);

        assertThat(esMutante).isTrue();
    }

    @Test
    public void esMutante_conCadenaAdnMutanteConLetrasConjuntasHorizontalYVertical_devuelveTrue() {
        String[] adn = {"GCTACG", "GATTTT", "GTTCAT", "GATGGG", "CGCCTA", "TCACTG"};

        boolean esMutante = mutanteService.esMutante(adn);

        assertThat(esMutante).isTrue();
    }

    @Test
    public void esMutante_conCadenaAdnMutanteConLetrasConjuntasDiagonalDerechaYVertical_devuelveTrue() {
        String[] adn = {"GATACG", "GCACAT", "GTCAAT", "GATAAG", "CGCCTA", "TCACTG"};

        boolean esMutante = mutanteService.esMutante(adn);

        assertThat(esMutante).isTrue();
    }

    @Test
    public void esMutante_conCadenaAdnMutanteConLetrasConjuntasDiagonalIzquierdaYVertical_devuelveTrue() {
        String[] adn = {"GCTCCG", "GCACGT", "GTCGCT", "GAGAAG", "CGCCTA", "TCACTG"};

        boolean esMutante = mutanteService.esMutante(adn);

        assertThat(esMutante).isTrue();
    }

    @Test
    public void esMutante_conCadenaAdnMutanteConLetrasInvalidas_devuelveFalse() {
        String[] adn = {"HHHHLK", "MMMMQR", "ZXCVBB", "NMHJKB", "LLKKMM", "YYUUII"};

        boolean esMutante = mutanteService.esMutante(adn);

        assertThat(esMutante).isFalse();
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
}
