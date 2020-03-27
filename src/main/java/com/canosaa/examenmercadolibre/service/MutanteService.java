package com.canosaa.examenmercadolibre.service;

import com.canosaa.examenmercadolibre.domain.Mutante;
import com.canosaa.examenmercadolibre.dto.response.EstadisticaTestMutantes;
import com.canosaa.examenmercadolibre.repository.MutanteRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@Transactional
public class MutanteService {

    @Autowired
    private MutanteRepository mutanteRepository;

    public boolean esMutante(String[] adn) {
        Assert.notEmpty(adn, "La cadena de ADN es obligatoria");

        int cantidadSecuenciasMutantes = 0;
        cantidadSecuenciasMutantes = analizarAdn(adn);
        boolean esMutante = cantidadSecuenciasMutantes > 1;

        guardarTestDeAdn(adn, esMutante);

        return esMutante;
    }

    private int analizarAdn(String[] adn) throws IllegalArgumentException {
        int indiceCaracterIterado = 0;
        int cantidadSecuenciasMutantes = 0;
        int cantidadFilas = adn.length;
        Pattern pattern = Pattern.compile("[A]{4}|[T]{4}|[G]{4}|[C]{4}");
        for (int i = 0; i < cantidadFilas; i++) {
            if (tiene4PalabrasConjuntasHorizontalmente(adn[i], pattern)) {
                cantidadSecuenciasMutantes++;
            }
            if (laPalabraTieneLongitudDistintaALaCantidadDeFilas(adn, adn[i])) {
                throw new IllegalArgumentException("Todas las cadenas de caracteres deben tener una longitud "
                        + "igual a la cantidad de cadenas que tiene el ADN para determinar la validez del mutante");
            }

            cantidadSecuenciasMutantes = sumarGruposDeCaracteresConjuntosEnDiagonalYVertical(indiceCaracterIterado, i, cantidadSecuenciasMutantes, adn);
            
            if (cantidadSecuenciasMutantes > 1) {
                break;
            }
            indiceCaracterIterado = 0;
        }
        return cantidadSecuenciasMutantes;
    }
    
    private boolean tiene4PalabrasConjuntasHorizontalmente(String cadena, Pattern pattern) {
        Matcher matcher = pattern.matcher(cadena);
        return matcher.find();
    }
    
    private static boolean laPalabraTieneLongitudDistintaALaCantidadDeFilas(String[] adn, String cadena) {
        int cantidadFilas = adn.length;
        return cadena.length() != cantidadFilas;
    }

    private int sumarGruposDeCaracteresConjuntosEnDiagonalYVertical(int indiceCaracterIterado, int indiceFilaActual, int cantidadSecuenciasMutantes, String[] adn) {
        int longitudPalabra = adn[indiceFilaActual].length();
        int cantidadFilas = adn.length;
        while (indiceCaracterIterado < longitudPalabra) {
            if (hay4FilasOMasPorRecorrer(indiceFilaActual, cantidadFilas)) {
                cantidadSecuenciasMutantes = sumarCaracteresIgualesVertical(adn, indiceFilaActual, indiceCaracterIterado, cantidadSecuenciasMutantes);
                cantidadSecuenciasMutantes = sumarCaracteresIgualesDiagonalDerecha(indiceCaracterIterado, longitudPalabra, adn, indiceFilaActual, cantidadSecuenciasMutantes);
            }
            if (hay4CaracteresOMasALaIzquierda(indiceCaracterIterado) && hay4FilasOMasPorRecorrer(indiceFilaActual, cantidadFilas)) {
                cantidadSecuenciasMutantes = sumarCaracteresIgualesDiagonalIzquierda(adn, indiceFilaActual, indiceCaracterIterado, cantidadSecuenciasMutantes);
            }
            
            indiceCaracterIterado++;
        }
        return cantidadSecuenciasMutantes;
    }

    private void guardarTestDeAdn(String[] adn, boolean esMutante) {
        Mutante mutante = new Mutante(String.join(", ", adn), esMutante);
        mutanteRepository.save(mutante);
    }

    private static boolean hay4CaracteresOMasALaIzquierda(int caracterIterado) {
        return caracterIterado - 3 > 0;
    }

    

    private int sumarCaracteresIgualesDiagonalIzquierda(String[] adn, int i, int caracterIterado, int cantidadSecuencias) {
        if (adn[i].toCharArray()[caracterIterado] == adn[i + 1].toCharArray()[caracterIterado - 1]
                && adn[i].toCharArray()[caracterIterado] == adn[i + 2].toCharArray()[caracterIterado - 2]
                && adn[i].toCharArray()[caracterIterado] == adn[i + 3].toCharArray()[caracterIterado - 3]) {
            cantidadSecuencias++;
        }
        return cantidadSecuencias;
    }

    private static boolean hay4FilasOMasPorRecorrer(int i, int cantidadFilas) {
        return (i + 3) < cantidadFilas;
    }

    private int sumarCaracteresIgualesVertical(String[] adn, int i, int caracterIterado, int cantidadSecuencias) {
        if (adn[i].toCharArray()[caracterIterado] == adn[i + 1].toCharArray()[caracterIterado]
                && adn[i].toCharArray()[caracterIterado] == adn[i + 2].toCharArray()[caracterIterado]
                && adn[i].toCharArray()[caracterIterado] == adn[i + 3].toCharArray()[caracterIterado]) {
            cantidadSecuencias++;
        }
        return cantidadSecuencias;
    }

    private int sumarCaracteresIgualesDiagonalDerecha(int caracterIterado, int longitudPalabra, String[] adn, int i, int cantidadSecuencias) {
        if (caracterIterado + 3 < longitudPalabra) {
            if (adn[i].toCharArray()[caracterIterado] == adn[i + 1].toCharArray()[caracterIterado + 1]
                    && adn[i].toCharArray()[caracterIterado] == adn[i + 2].toCharArray()[caracterIterado + 2]
                    && adn[i].toCharArray()[caracterIterado] == adn[i + 3].toCharArray()[caracterIterado + 3]) {
                cantidadSecuencias++;
            }
        }
        return cantidadSecuencias;
    }

    public EstadisticaTestMutantes obtenerEstadisticas() {
        int cantidadMutantes = mutanteRepository.findByEsMutante(true).size();
        int cantidadHumanos = mutanteRepository.findByEsMutante(false).size();

        BigDecimal proporcion = BigDecimal.valueOf(cantidadMutantes).divide(BigDecimal.valueOf(cantidadHumanos)).setScale(2);

        EstadisticaTestMutantes estadisticas = new EstadisticaTestMutantes(cantidadMutantes, cantidadHumanos, proporcion);

        return estadisticas;
    }

}
