package br.com.alura.screenmatch.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ManyToAny;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;


@Entity
@Table(name = "episodios")
public class Episodio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Serie serie;

    @Column (unique = true)
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

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Episodio episodio = (Episodio) o;
        return Objects.equals(id, episodio.id) && Objects.equals(serie, episodio.serie) && Objects.equals(titulo, episodio.titulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serie, titulo);
    }

    @Override
    public String toString(){
        return serie.getTitulo() + " - Temporada: " + getTemporada() + " - Episódio: " + getEpisodioNumero() + " - " + getTitulo() +
                " - Avaliação: " + getAvaliacao() + " - Data: " + getDataLancamento();
    }
}
