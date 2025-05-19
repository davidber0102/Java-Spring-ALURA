package com.aluracurso.screnmatch_frases.Repositorio;

import com.aluracurso.screnmatch_frases.Model.Frase;
import com.aluracurso.screnmatch_frases.dto.FraseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FraseRepository extends JpaRepository<Frase, Long> {
    @Query("SELECT f FROM f order by function('RANDOM') LIMIT 1")
      public Frase obtenerFraseAleatoria();
}

