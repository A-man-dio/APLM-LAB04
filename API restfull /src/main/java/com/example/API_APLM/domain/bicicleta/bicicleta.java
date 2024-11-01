package com.example.API_APLM.domain.bicicleta;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "bicicleta")
@Entity(name = "bicicleta")
@EqualsAndHashCode(of = "id")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class bicicleta {

    public bicicleta(RequestBicicleta data){
        this.nome = data.nome();
    }

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id ;

    private String nome;
}
