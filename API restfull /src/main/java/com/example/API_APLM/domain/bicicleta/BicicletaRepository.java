
package com.example.API_APLM.domain.bicicleta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BicicletaRepository extends JpaRepository<bicicleta, String> {

}
