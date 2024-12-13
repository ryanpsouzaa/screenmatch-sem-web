package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.services.ConsumoApi;
import br.com.alura.screenmatch.services.Conversor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class PrincipalV2 {

    private Scanner leitura;

    private ConsumoApi consumo;

    private Conversor conversor;

    private final String ENDERECO = "https://www.omdbapi.com/?t=";

    private final String API_KEY = "&apikey=6938fd4d";

    SerieRepository repositorio;

    private List<Serie> seriesPesquisadas;

    public PrincipalV2(SerieRepository repositorio){
        this.leitura = new Scanner(System.in);
        this.consumo = new ConsumoApi();
        this.conversor = new Conversor();
        this.repositorio = repositorio;
        this.seriesPesquisadas = new ArrayList<>();
    }

    public void exibirMenu(){

        var opcao = -1;
        while(opcao != 0) {
            System.out.print("""
                             \nOPÇÕES
                     (1) Buscar Série
                     (2) Buscar Épisodios
                     (3) Exibir Série(s) pesquisada(s)
                     
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
                    buscarEpisodios();
                    break;

                case 3:
                    listarSeriesPesquisadas();
                    break;

                }

            }
        }

    public Serie buscarSerie(String tituloSerie){
        String json = ENDERECO + tituloSerie.replace(" ", "+") + API_KEY;
        json = consumo.obterDados(json);

        Serie serie = new Serie(conversor.converteDados(json, DadosSerie.class));
        seriesPesquisadas.add(serie);
        repositorio.save(serie);

        return serie;
    }

    public void buscarEpisodios(){
        List<Serie> listaSeries = listarSeriesPesquisadas();
        listaSeries.stream().forEach(e -> System.out.println(
                e.getTitulo()
        ));

        System.out.println("Digite a série que deseja exibir os episódios: ");
        String nomeSerie = leitura.nextLine();

        Optional<Serie> serieOptional = listaSeries.stream()
                .filter(e -> e.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
                .findFirst();

        if(serieOptional.isPresent()){
            var serieEncontrada = serieOptional.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for(int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++){
                String endereco = ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") +
                        "&season=" + i + API_KEY;
                String json = consumo.obterDados(endereco);
                DadosTemporada temporada = conversor.converteDados(json, DadosTemporada.class);
                temporadas.add(temporada);
            }
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(t -> t.episodios().stream()
                            .map(e -> new Episodio(t.numeroTemporada(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
            episodios.forEach(System.out::println);

        } else{
            System.out.println("Erro ao achar a serie");
        }
    }

    public List<Serie> listarSeriesPesquisadas(){
        List<Serie> seriesPesquisadas = repositorio.findAll().stream()
                .sorted(Comparator.comparing(Serie :: getGenero))
                .collect(Collectors.toList());
        return seriesPesquisadas;
    }
}
