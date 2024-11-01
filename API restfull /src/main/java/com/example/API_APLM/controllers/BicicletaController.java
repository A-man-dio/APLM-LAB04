package com.example.API_APLM.controllers;

import com.example.API_APLM.domain.bicicleta.BicicletaRepository;
import com.example.API_APLM.domain.bicicleta.RequestBicicleta;
import com.example.API_APLM.domain.bicicleta.bicicleta;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bicicleta") //para a url
public class BicicletaController {
    @Autowired
    private BicicletaRepository repository;

    @GetMapping
    public ResponseEntity getAllBicicletas(){
        var allBicicletas = this.repository.findAll();
        return ResponseEntity.ok(allBicicletas);
    }

    @PostMapping
    public ResponseEntity addBicicleta(@RequestBody @Valid RequestBicicleta data){
        //System.out.println(data);
        bicicleta novaBike = new bicicleta(data);
        repository.save(novaBike);
        return ResponseEntity.ok().build();

    }
}
 