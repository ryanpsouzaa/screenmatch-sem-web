package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {

//    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);
    @Query("select s from Serie s where s.titulo ILIKE %:nomeSerie%")
    Optional<Serie> buscarPorTitulo(String nomeSerie);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();


//    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String ator, Double avaliacao);
    @Query("select s from Serie s where s.atores = :ator AND s.avaliacao >= :avaliacao")
    List<Serie> buscarPorAtorAvaliacao(String ator, double avaliacao);


//    List<Serie> findByGenero(Categoria categoria);
    @Query("select s from Serie s where s.genero = :genero")
    List<Serie> buscarPorGenero(Categoria genero);


//    List<Serie> findByTotalTemporadasIsLessThanEqualAndAvaliacaoGreaterThanEqual(Integer totalTemporadas, Double avaliacao);
    @Query("select s from Serie s Where s.totalTemporadas <= :totalTemporadas  AND s.avaliacao >= :avaliacao")
    List<Serie> buscarPorTemporadaAvaliacao(int totalTemporadas, double avaliacao);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trecho%")
    List<Episodio> buscarTrechoEpisodio(String trecho);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> buscarTop5Episodios(Serie serie);

    @Query("SELECT COUNT(e) > 0 FROM Episodio e WHERE e.serie = :serie AND e.temporada = :temporada AND e.titulo = :titulo")
    boolean existeSerieComTemporadaETitulo(@Param("serie") Serie serie,
                                           @Param("temporada") int temporada,
                                           @Param("titulo") String titulo);
}
