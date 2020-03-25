package com.canosaa.examenmercadolibre.controller;

import com.canosaa.examenmercadolibre.service.MutanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MutanteController {
    
    @Autowired
    MutanteService mutanteService;
    
    @PostMapping("/mutant")
    @ResponseBody
    public ResponseEntity esMutante(@RequestBody String[] adn){
        Boolean esMutante = mutanteService.esMutante(adn);
        if(!esMutante){
            return new ResponseEntity(esMutante, HttpStatus.FORBIDDEN);
        }else{
            return new ResponseEntity(esMutante, HttpStatus.OK);
        }
    }
    
}
