package br.com.alura.screenmatch.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episodio {

    private String titulo;
    private Integer temporada;
    private Integer episodioNumero;
    private Double avaliacao;
    private LocalDate dataLancamento;

    public Episodio(){

    }

    public Episodio(Integer temporada, DadosEpisodio dadosEpisodio){
        this.temporada = temporada;
        this.titulo = dadosEpisodio.titulo();
        this.episodioNumero = dadosEpisodio.numeroEpisodio();
        try{
            this.avaliacao = Double.parseDouble(dadosEpisodio.avaliacao());

        }catch(NumberFormatException ex){
            this.avaliacao = 0.0;
        }
        try{
            this.dataLancamento = LocalDate.parse(dadosEpisodio.dataLancamento());

        }catch(DateTimeParseException ex){
            this.dataLancamento = null;
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public Integer getEpisodioNumero() {
        return episodioNumero;
    }

    public void setEpisodioNumero(Integer episodioNumero) {
        this.episodioNumero = episodioNumero;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    @Override
    public String toString(){
        return getTitulo() + ", Temporada: " + getTemporada() +
                ", Episódio: " + getEpisodioNumero() +
                ", Avaliação: " + getAvaliacao() +
                ", Lançamento: " + getDataLancamento();
    }
}
