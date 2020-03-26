package com.canosaa.examenmercadolibre.controller;

import com.canosaa.examenmercadolibre.dto.AdnMutante;
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
    public ResponseEntity esMutante(@RequestBody AdnMutante adnMutante){
        Boolean esMutante = mutanteService.esMutante(adnMutante.getCadenasAdn());
        ResponseEntity responseEntity;
        if(!esMutante){
            responseEntity = new ResponseEntity(esMutante, HttpStatus.FORBIDDEN);
        }else{
            responseEntity = new ResponseEntity(esMutante, HttpStatus.OK);
        }
        return responseEntity;
    }
    
}
