package com.canosaa.examenmercadolibre.repository;

import com.canosaa.examenmercadolibre.domain.Mutante;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MutanteRepository extends JpaRepository<Mutante, Long>{

    public List<Mutante> findByEsMutante(boolean b);

}
