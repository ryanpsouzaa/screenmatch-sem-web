package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.services.ConsumoApi;
import br.com.alura.screenmatch.services.Conversor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PrincipalV2 {

    private Scanner leitura;
    private ConsumoApi consumo;
    private Conversor conversor;
    private List<Serie> seriesPesquisadas;

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6938fd4d";

    public PrincipalV2(){
        this.leitura = new Scanner(System.in);
        this.consumo = new ConsumoApi();
        this.conversor = new Conversor();
        this.seriesPesquisadas = new ArrayList<>();
    }

    public void exibirMenu(){

        var opcao = -1;
        while(opcao != 0){
            System.out.print("""
                            \nOPÇÕES
                    (1) Buscar Série
                    (2) Buscar Épisodios
                    (3) Exibir Série(s) pesquisada(s)
                    
                    (0) Sair
                   """);

            opcao = Integer.parseInt(leitura.nextLine());

            switch(opcao){
                case 1:
                    System.out.println("Digite o nome da série: ");
                    String tituloSerie = leitura.nextLine();

                    try{
                        Serie serie = buscarSerie(tituloSerie);

                        if(!seriesPesquisadas.contains(serie)){
                            seriesPesquisadas.add(serie);
                        }

                        System.out.println("Série encontrada:");
                        System.out.println(serie);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case 2:
                    System.out.println("Digite o nome da série");
                    tituloSerie = leitura.nextLine();

                    try{
                        System.out.println("\nEpisódios de " + tituloSerie);
                        List<DadosEpisodio> todosEpisodios = buscarEpisodios(tituloSerie);
                        todosEpisodios.stream().
                                forEach(System.out::println);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case 3:
                    if(seriesPesquisadas.isEmpty()){
                        System.out.println("Nenhuma série foi pesquisada");
                    } else{
                        seriesPesquisadas.stream()
                                .sorted(Comparator.comparing(Serie::getGenero))
                                .forEach(System.out::println);
                    }

            }
        }
    }

    public Serie buscarSerie(String tituloSerie){
        String json = ENDERECO + tituloSerie.replace(" ", "+") + API_KEY;
        json = consumo.obterDados(json);

        return new Serie(conversor.converteDados(json, DadosSerie.class));

    }

    public List<DadosTemporada> buscarTemporadas(String tituloSerie){
        List<DadosTemporada> temporadas = new ArrayList<>();
        Serie serie = buscarSerie(tituloSerie);

        for(int i = 1; i <= serie.getTotalTemporadas(); i++){
            String json = ENDERECO + tituloSerie.replace(" ", "+")
                    + "&season=" + i + API_KEY;
            json = consumo.obterDados(json);
            DadosTemporada temporada = conversor.converteDados(json, DadosTemporada.class);
            temporadas.add(temporada);
        }
        return temporadas;
    }

    public List<DadosEpisodio> buscarEpisodios(String tituloSerie){
        List<DadosTemporada> temporadas = buscarTemporadas(tituloSerie);

        List<DadosEpisodio> todosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        return todosEpisodios;
    }
}
