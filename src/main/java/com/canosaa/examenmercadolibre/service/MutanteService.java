package com.canosaa.examenmercadolibre.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class MutanteService {

    public boolean esMutante(String[] adn) {
        Assert.notEmpty(adn, "La cadena de ADN es obligatoria");

        int cantidadSecuencias = 0;
        int caracterIterado = 0;
        int cantidadFilas = adn.length;
        Pattern pattern = Pattern.compile("[A]{4}|[T]{4}|[G]{4}|[C]{4}");
        for (int i = 0; i < cantidadFilas; i++) {
            //horizontal
            Matcher matcher = pattern.matcher(adn[i]);
            if (matcher.find()) {
                cantidadSecuencias++;
                if (cantidadSecuencias > 1) {
                    break;
                }
            }
            int longitudPalabra = adn[i].length();
            if (adn[i].length() != cantidadFilas) {
                throw new IllegalArgumentException("Todas las cadenas de caracteres deben tener una longitud "
                        + "igual a la cantidad de cadenas que tiene el ADN para determinar la validez del mutante");
            }

            while (caracterIterado < longitudPalabra) {
                //vertical
                if ((i + 3) < cantidadFilas) {
                    if (adn[i].toCharArray()[caracterIterado] == adn[i + 1].toCharArray()[caracterIterado]
                            && adn[i].toCharArray()[caracterIterado] == adn[i + 2].toCharArray()[caracterIterado]
                            && adn[i].toCharArray()[caracterIterado] == adn[i + 3].toCharArray()[caracterIterado]) {
                        cantidadSecuencias++;
//                        diagonal    
                    }
                    if (caracterIterado + 3 < longitudPalabra) {
                        if (adn[i].toCharArray()[caracterIterado] == adn[i + 1].toCharArray()[caracterIterado + 1]
                                && adn[i].toCharArray()[caracterIterado] == adn[i + 2].toCharArray()[caracterIterado + 2]
                                && adn[i].toCharArray()[caracterIterado] == adn[i + 3].toCharArray()[caracterIterado + 3]) {
                            cantidadSecuencias++;
                        }
                    }

                }
                if (caracterIterado - 3 > 0 && i + 3 < cantidadFilas) {
                    if (adn[i].toCharArray()[caracterIterado] == adn[i + 1].toCharArray()[caracterIterado - 1]
                            && adn[i].toCharArray()[caracterIterado] == adn[i + 2].toCharArray()[caracterIterado - 2]
                            && adn[i].toCharArray()[caracterIterado] == adn[i + 3].toCharArray()[caracterIterado - 3]) {
                        cantidadSecuencias++;
                    }
                }
                if (cantidadSecuencias > 1) {
                    break;
                }
                caracterIterado++;
            }
            caracterIterado = 0;
        }
        return cantidadSecuencias > 1;
    }

}
