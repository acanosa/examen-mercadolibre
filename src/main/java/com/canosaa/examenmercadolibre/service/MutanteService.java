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

        int cantidadSecuencias = 0;
        cantidadSecuencias = analizarAdn(adn);
        boolean esMutante = cantidadSecuencias > 1;
        
        guardarTestDeAdn(adn, esMutante);
        
        return esMutante;
    }

    private int analizarAdn(String[] adn) throws IllegalArgumentException {
        int indiceCaracterIterado = 0;
        int cantidadSecuencias = 0;
        int cantidadFilas = adn.length;
        Pattern pattern = Pattern.compile("[A]{4}|[T]{4}|[G]{4}|[C]{4}");
        for (int i = 0; i < cantidadFilas; i++) {
            Matcher matcher = pattern.matcher(adn[i]);
            if (matcher.find()) {
                cantidadSecuencias++;
                if (cantidadSecuencias > 1) {
                    break;
                }
            }
            int longitudPalabra = adn[i].length();
            
            if (laPalabraTieneLongitudDistintaALaCantidadDeFilas(adn, i, cantidadFilas)) {
                throw new IllegalArgumentException("Todas las cadenas de caracteres deben tener una longitud "
                        + "igual a la cantidad de cadenas que tiene el ADN para determinar la validez del mutante");
            }

            while (indiceCaracterIterado < longitudPalabra) {
                if (hay4FilasOMasPorRecorrer(i, cantidadFilas)) {
                    cantidadSecuencias = buscarCaracteresIgualesVertical(adn, i, indiceCaracterIterado, cantidadSecuencias);
                    cantidadSecuencias = buscarCaracteresIgualesDiagonalDerecha(indiceCaracterIterado, longitudPalabra, adn, i, cantidadSecuencias);
                }
                if (hay4CaracteresOMasALaIzquierda(indiceCaracterIterado) && hay4FilasOMasPorRecorrer(i, cantidadFilas)) {
                    cantidadSecuencias = buscarCaracteresIgualesDiagonalIzquierda(adn, i, indiceCaracterIterado, cantidadSecuencias);
                }
                if (cantidadSecuencias > 1) {
                    break;
                }
                indiceCaracterIterado++;
            }
            indiceCaracterIterado = 0;
        }
        return cantidadSecuencias;
    }

    private void guardarTestDeAdn(String[] adn, boolean esMutante) {
        Mutante mutante = new Mutante(String.join(", ", adn), esMutante);
        mutanteRepository.save(mutante);
    }

    private static boolean hay4CaracteresOMasALaIzquierda(int caracterIterado) {
        return caracterIterado - 3 > 0;
    }

    private static boolean laPalabraTieneLongitudDistintaALaCantidadDeFilas(String[] adn, int i, int cantidadFilas) {
        return adn[i].length() != cantidadFilas;
    }

    private int buscarCaracteresIgualesDiagonalIzquierda(String[] adn, int i, int caracterIterado, int cantidadSecuencias) {
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

    private int buscarCaracteresIgualesVertical(String[] adn, int i, int caracterIterado, int cantidadSecuencias) {
        if (adn[i].toCharArray()[caracterIterado] == adn[i + 1].toCharArray()[caracterIterado]
                && adn[i].toCharArray()[caracterIterado] == adn[i + 2].toCharArray()[caracterIterado]
                && adn[i].toCharArray()[caracterIterado] == adn[i + 3].toCharArray()[caracterIterado]) {
            cantidadSecuencias++;
        }
        return cantidadSecuencias;
    }

    private int buscarCaracteresIgualesDiagonalDerecha(int caracterIterado, int longitudPalabra, String[] adn, int i, int cantidadSecuencias) {
        if (caracterIterado + 3 < longitudPalabra) {
            if (adn[i].toCharArray()[caracterIterado] == adn[i + 1].toCharArray()[caracterIterado + 1]
                    && adn[i].toCharArray()[caracterIterado] == adn[i + 2].toCharArray()[caracterIterado + 2]
                    && adn[i].toCharArray()[caracterIterado] == adn[i + 3].toCharArray()[caracterIterado + 3]) {
                cantidadSecuencias++;
            }
        }
        return cantidadSecuencias;
    }
    
    public EstadisticaTestMutantes obtenerEstadisticas(){
        int cantidadMutantes = mutanteRepository.findByEsMutante(true).size();
        int cantidadHumanos = mutanteRepository.findByEsMutante(false).size();

        BigDecimal proporcion = BigDecimal.valueOf(cantidadMutantes).divide(BigDecimal.valueOf(cantidadHumanos)).setScale(2);
        
        EstadisticaTestMutantes estadisticas = new EstadisticaTestMutantes(cantidadMutantes, cantidadHumanos, proporcion);
        
        return estadisticas;
    }

}
