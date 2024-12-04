package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) /* A desserialização irá ignorar todos os outros
atributos do json, apenas irá ir nos atributos de Title, totalSeasons e imdbRating */
public record DadosSerie (@JsonAlias("Title") String titulo,
                          @JsonAlias("totalSeasons")Integer totalTemporadas,
                          @JsonAlias("imdbRating")String avaliacao) {
}

/* realizando para o Gson:

public record DadosSerie(String title, Integer totalSeasons, String imdbRating){}

 */
