package br.com.alura.screenmatch.dto;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import jakarta.persistence.*;

import java.util.List;

public record SerieDTO (Long id,

                        String titulo,

                        Integer totalTemporadas,

                        Double avaliacao,

                        String atores,

                        String sinopse,

                        String poster,

                        Categoria genero){

}

