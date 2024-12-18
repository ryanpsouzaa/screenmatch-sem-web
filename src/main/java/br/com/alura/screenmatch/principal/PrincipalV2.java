package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.exception.SerieNaoEncontrada;
import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.services.ConsumoApi;
import br.com.alura.screenmatch.services.Conversor;

import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

public class PrincipalV2 {

    private Scanner leitura;

    private ConsumoApi consumo;

    private Conversor conversor;

    private final String ENDERECO = "https://www.omdbapi.com/?t=";

    private final String API_KEY = "&apikey=6938fd4d";

    SerieRepository repositorio;

    private List<Serie> listaSeriesPesquisadas;

    public PrincipalV2(SerieRepository repositorio){

        this.leitura = new Scanner(System.in);
        this.consumo = new ConsumoApi();
        this.conversor = new Conversor();
        this.repositorio = repositorio;
        this.listaSeriesPesquisadas = new ArrayList<>();
    }

    public void exibirMenu(){

        var opcao = -1;
        while(opcao != 0) {
            System.out.print("""
                     =======================================================
                             OPÇÕES
                     (1) Cadastrar Série
                     
                             PESQUISA
                     (2) Exibir Séries pesquisadas
                     (3) Pesquisar Série por título
                     (4) Buscar Épisodios
                     (5) Buscar Séries por ator + avaliação
                     (6) Buscar séries por Gênero
                     (7) Buscar séries por número de temporadas + avaliação
                     (8) Buscar Episódio(s) por trecho do título
                     (9) Pesquisar episódios a partir de um ano
                   
                              TOP 5
                     (10) Listar TOP 5 das séries pesquisadas
                     (11) Pesquisar TOP 5 episódios de uma série
                     
                     (0) Sair
                    """);

            opcao = Integer.parseInt(leitura.nextLine());

            switch (opcao) {
                case 1:

                    System.out.println("Digite o nome da série: ");
                    String tituloSerie = leitura.nextLine();

                    try {
                        Serie serie = buscarSerie(tituloSerie);

                        System.out.println("Série encontrada:");
                        System.out.println(serie);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case 2:

                    System.out.println("Séries pesquisadas: ");
                    List<Serie> seriesPesquisadas = buscarSeriesPesquisadas();
                    seriesPesquisadas.forEach(System.out::println);

                    break;

                case 3:

                    System.out.println("Digite o título da séries que deseja pesquisar: ");
                    var nomeSerie = leitura.nextLine();

                    try{
                        Serie serieBuscada = buscarSeriePorTitulo(nomeSerie);
                        System.out.println(serieBuscada.toString());

                    }catch(SerieNaoEncontrada e){
                        System.out.println(e.getMessage());
                    }

                    break;

                case 4:

                    seriesPesquisadas = buscarSeriesPesquisadas();
                    seriesPesquisadas.forEach(s -> System.out.println(s.getTitulo()));

                    System.out.println("Digite o título que deseja buscar os episódios: ");
                    nomeSerie = leitura.nextLine();
                    try {
                        Serie seriePesquisada = buscarSeriePorTitulo(nomeSerie);
                        List<Episodio> episodiosPesquisado = buscarEpisodios(nomeSerie);
                        System.out.println("Episódios de " + seriePesquisada.getTitulo() + ":");
                        episodiosPesquisado.forEach(System.out::println);

                    } catch (SerieNaoEncontrada e) {
                        throw new RuntimeException(e);
                    }

//                    System.out.println("Séries da sua lista: ");
//                    List<Serie> seriesPesquisadas = buscarSeriesPesquisadas();
//                    seriesPesquisadas.stream()
//                                    .forEach(s -> System.out.println(
//                                            s.getTitulo()
//                                    ));
//                    System.out.println("Digite o nome da série que deseja pesquisar: ");
//                    var nomeSerie = leitura.nextLine();
//                    try {
//                        Serie serie = buscarSeriePorTitulo(nomeSerie);
//
//                    } catch (SerieNaoEncontrada e) {
//                        throw new RuntimeException(e);
//                    }
//                    try{
//                        List<Episodio> episodiosBuscados = buscarEpisodios();
//                        episodiosBuscados.forEach(System.out::println);
//
//                    } catch(SerieNaoEncontrada e){
//                        System.out.println(e.getMessage());
//                    }
//
                    break;

                case 5:

                    System.out.println("Digite ator/Atriz que deseja pesquisar:");
                    String nomeAtor = leitura.nextLine();

                    System.out.println("A partir de qual avaliação que deseja filtrar?");
                    Double avaliacao = Double.parseDouble(leitura.nextLine());

                    try{
                        List<Serie> seriesPesquisadasAtores = buscarPorAtorEAvaliacao(nomeAtor, avaliacao);
                        System.out.println("Séries em que " + nomeAtor.toUpperCase() + " trabalhou:");
                        seriesPesquisadasAtores.forEach(e -> System.out.println(
                                e.getTitulo() + ", avaliacão: " + e.getAvaliacao()
                        ));

                    }catch(SerieNaoEncontrada e){
                        System.out.println(e.getMessage());
                    }

                    break;

                case 6:

                    System.out.println("Digite o gênero que deseja pesquisar: ");
                    var categoriaString = leitura.nextLine();

                    Categoria categoria = Categoria.fromPortugues(categoriaString);
                    try{
                        List<Serie> generosPesquisado = buscarPorGenero(categoria);
                        System.out.println("Séries do gênero: " + categoriaString);
                        generosPesquisado.forEach(e -> System.out.println(
                                e.getTitulo() + ", avaliação: " + e.getAvaliacao() + ", temporadas: " + e.getTotalTemporadas()
                        ));

                    } catch(SerieNaoEncontrada e){
                        System.out.println(e.getMessage());
                    }

                    break;

                case 7:

                    System.out.println("Digite o número máximo de temporadas que a série deva ter: ");
                    Integer temporadasMax = Integer.parseInt(leitura.nextLine());

                    System.out.println("A partir de qual avaliação que deseja filtrar?");
                    avaliacao = Double.parseDouble(leitura.nextLine());

                    try{
                        List<Serie> seriesPesquisadasPorTemporadaEAvaliacao = buscarPorTotalTemporadasEAvaliacao(temporadasMax, avaliacao);
                        System.out.println("Séries encontradas: ");

                        seriesPesquisadasPorTemporadaEAvaliacao.forEach(e -> System.out.println(
                                e.getTitulo() + ", Temporadas: " + e.getTotalTemporadas() + ", Avaliação: " + e.getAvaliacao()
                        ));

                    }catch(NullPointerException e){
                        System.out.println(e.getMessage());
                    }

                    break;

                case 8:

                    System.out.println("Digite o trecho do título do episódio: ");
                    String trecho = leitura.nextLine();

                    try{
                        List<Episodio> listaEpisodiosTrecho = buscarEpisodioPorTrecho(trecho);
                        listaEpisodiosTrecho.forEach(e -> System.out.println(
                                e.getSerie().getTitulo() + " - Temporada: " +
                                        e.getTemporada() + " - Episódio: " +
                                        e.getEpisodioNumero() + " - " + e.getTitulo()
                        ));
                    } catch(NullPointerException e){
                        System.out.println(e.getMessage());
                    }

                    break;

                case 9:

                    System.out.println("Séries pesquisadas: ");
                    buscarSeriesPesquisadas().stream()
                                    .forEach(e -> System.out.println(e.getTitulo()));
                    System.out.println("Digite o nome da série que deseja pesquisar: ");
                    nomeSerie = leitura.nextLine();

                    System.out.println("A partir de qual ano deseja filtrar os episódios?");
                    int ano = Integer.parseInt(leitura.nextLine());

                    try{
                        List<Episodio> episodiosFiltrados = buscarEpisodiosAPartirDoAno(nomeSerie, ano);

                        episodiosFiltrados.forEach(System.out::println);

                    } catch(NullPointerException e){
                        System.out.println(e.getMessage());
                    }

                    break;

                case 10:

                    try{
                        List<Serie> listaTop5 = buscarTop5();
                        System.out.println("TOP séries por avaliação:");
                        for(int i = 0; i < listaTop5.size(); i++){
                            System.out.println("TOP " + (i+1) + "- " + listaTop5.get(i).getTitulo() +
                                    ", avaliação: " + listaTop5.get(i).getAvaliacao());
                        }

                    }catch(NullPointerException e){
                        System.out.println(e.getMessage());
                    }

                    break;

                case 11:

                    seriesPesquisadas = buscarSeriesPesquisadas();
                    System.out.println("Títulos já pesquisados: ");
                    seriesPesquisadas.stream().forEach(e -> System.out.println(
                            e.getTitulo()
                    ));

                    System.out.println("\nDigite o título da série: ");
                    nomeSerie = leitura.nextLine();

                    List<Episodio> top5Episodios = buscarTop5Episodios(nomeSerie);
                    System.out.println("TOP 5 Episódios de " + top5Episodios.get(0).getSerie().getTitulo() + ":");
                    top5Episodios.forEach(e ->
                            System.out.println(
                                    "Temporada: " + e.getTemporada() + " - Episódio: " + e.getEpisodioNumero() +
                                            " - " + e.getTitulo() + " - Avaliação: " + e.getAvaliacao()
                            ));

                    break;

                }

            }
        }

    public void salvarEpisodios(Serie serie){
        try {
            List<Episodio> episodiosSerie = buscarEpisodios(serie.getTitulo());

        } catch (SerieNaoEncontrada e) {
            throw new RuntimeException(e);
        }

    }

    public Serie buscarSerie(String tituloSerie){
        String json = ENDERECO + tituloSerie.replace(" ", "+") + API_KEY;
        json = consumo.obterDados(json);

        Serie serie = new Serie(conversor.converteDados(json, DadosSerie.class));

        repositorio.save(serie);

        salvarEpisodios(serie);

        return serie;
    }
    public List<DadosTemporada> buscarTodasTemporadas(Serie serie){
        List<DadosTemporada> temporadas = new ArrayList<>();

        for(int i = 1; i <= serie.getTotalTemporadas(); i++){
            String endereco = ENDERECO + serie.getTitulo().replace(" ", "+") +
                    "&season=" + i + API_KEY;
            String json = consumo.obterDados(endereco);
            DadosTemporada temporada = conversor.converteDados(json, DadosTemporada.class);
            temporadas.add(temporada);
        }
        if(temporadas.isEmpty()){
            throw new NullPointerException("Não foram encontradas temporadas");

        }else{
            return temporadas;
        }
    }

    public List<Episodio> buscarEpisodios(String nomeSerie) throws SerieNaoEncontrada {



        Optional<Serie> seriePesquisada = repositorio.buscarPorTitulo(nomeSerie);

        Optional<Serie> serieOptional = listaSeriesPesquisadas.stream()
                .filter(e -> e.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
                .findFirst();

        if(seriePesquisada.isPresent()){
            var serieEncontrada = seriePesquisada.get();
            List<DadosTemporada> temporadas = buscarTodasTemporadas(serieEncontrada);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(t -> t.episodios().stream()
                            .map(e -> new Episodio(t.numeroTemporada(), e)))
                    .collect(Collectors.toList());

            if(!repositorio.existeSerieComTemporadaETitulo(serieEncontrada, temporadas.get(0).numeroTemporada(),episodios.get(0).getTitulo())){
                serieEncontrada.setEpisodios(episodios);
                repositorio.save(serieEncontrada);
            }

            serieEncontrada.setEpisodios(episodios);
            if(episodios.isEmpty()){
                throw new NullPointerException("Nenhum episódio foi encontrado");

            }else{
                return episodios;
            }
        } else{
            throw new SerieNaoEncontrada("Série não encontrada");
        }
    }

    public List<Serie> buscarSeriesPesquisadas(){
        List<Serie> seriesPesquisadas = repositorio.findAll().stream()
                .sorted(Comparator.comparing(Serie :: getGenero))
                .collect(Collectors.toList());
        return seriesPesquisadas;
    }

    public Serie buscarSeriePorTitulo(String nomeSerie) throws SerieNaoEncontrada {

        Optional<Serie> serieBuscada = repositorio.buscarPorTitulo(nomeSerie);
        if(serieBuscada.isPresent()){
            return serieBuscada.get();

        }else{
            throw new SerieNaoEncontrada("Série não encontrada");
        }

    }

    public List<Serie> buscarPorAtorEAvaliacao(String nomeAtor, Double avaliacao) throws SerieNaoEncontrada {
        List<Serie> seriesPesquisadas = repositorio.buscarPorAtorAvaliacao(nomeAtor, avaliacao);

        if(seriesPesquisadas.isEmpty()){
            throw new SerieNaoEncontrada("Não foram encontradas séries em que " + nomeAtor.toUpperCase() + " trabalhou");

        } else{
            return seriesPesquisadas;
        }
    }

    public List<Serie> buscarTop5(){
        List<Serie> listaTop5 = repositorio.findTop5ByOrderByAvaliacaoDesc();

        if(listaTop5.isEmpty()){
            throw new NullPointerException("Não há séries cadastradas o suficiente");
        } else{
            return listaTop5;
        }
    }

    public List<Serie> buscarPorGenero(Categoria categoria) throws SerieNaoEncontrada {

        List<Serie> listaPesquisada = repositorio.buscarPorGenero(categoria);

        if(listaPesquisada.isEmpty()){
            throw new NullPointerException("Nenhuma série encontrada");

        }else{
            return listaPesquisada;
        }

    }

    public List<Serie> buscarPorTotalTemporadasEAvaliacao(Integer totalTemporadas, Double avaliacao){

        List<Serie> listaPesquisada = repositorio.buscarPorTemporadaAvaliacao(totalTemporadas, avaliacao);

        if(listaPesquisada.isEmpty()){
            throw new NullPointerException("Não foi encontrada nenhuma série");

        }else{
            return listaPesquisada;
        }

    }

    public List<Episodio> buscarEpisodioPorTrecho(String trecho){
        List<Episodio> listaPesquisada = repositorio.buscarTrechoEpisodio(trecho);

        if(listaPesquisada.isEmpty()){
            throw new NullPointerException("Nenhum episódio foi encontrado");

        }else{
            return listaPesquisada;
        }
    }

    public List<Episodio> buscarTop5Episodios(String nomeSerie){
        try {
            Serie serie = buscarSeriePorTitulo(nomeSerie);
            List<Episodio> top5 = repositorio.buscarTop5Episodios(serie);
            if(top5.isEmpty()){
                throw new NullPointerException("Não foram encontrados episódios da série");
            } else{
                return top5;
            }
        } catch (SerieNaoEncontrada e) {
            throw new RuntimeException(e);
        }

    }

    public List<Episodio> buscarEpisodiosAPartirDoAno(String nomeSerie, int anoSerie){
        try{
            Serie serieBuscada = buscarSeriePorTitulo(nomeSerie);
            List<Episodio> episodiosPesquisados = repositorio.buscarEpisodiosPartirData(serieBuscada, anoSerie);
            if(episodiosPesquisados.isEmpty()){
                throw new NullPointerException("Não foram encontrados episódios a partir de " + anoSerie);

            }else{
                return episodiosPesquisados;
            }

        } catch (SerieNaoEncontrada e) {
            throw new RuntimeException(e);
        }

    }
}
