package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.services.ConsumoApi;
import br.com.alura.screenmatch.services.Conversor;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura;
    private ConsumoApi consumo;
    private Conversor conversor;

    //https://www.omdbapi.com/?t=arcane&apikey=6938fd4d

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6938fd4d";

    public Principal(){
        this.leitura = new Scanner(System.in);
        this.consumo = new ConsumoApi();
        this.conversor = new Conversor();
    }

    public void inciarMenu(){
        System.out.println("Digite a série que deseja consultar: ");

        String serieString = leitura.nextLine();
        String enderecoCompleto = ENDERECO + serieString.replace(" ", "+") + API_KEY;

        String json = consumo.obterDados(enderecoCompleto);

        DadosSerie dadosSerie = conversor.converteDados(json, DadosSerie.class);

        System.out.println(dadosSerie);

        ArrayList<DadosTemporada> temporadas = new ArrayList<>();
        for(int i = 1; i <= dadosSerie.totalTemporadas(); i++){
            enderecoCompleto = ENDERECO + serieString.replace(" ", "+") + "&season=" + i + API_KEY;
            json = consumo.obterDados(enderecoCompleto);
            DadosTemporada dadosTemporada = conversor.converteDados(json, DadosTemporada.class);

            temporadas.add(dadosTemporada);
        }
        System.out.println("Todos os episódios");
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> todosEpisodios =  temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("//////////////////");
        System.out.println("Top 5 episódios:");
        todosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio :: avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);


        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                                .map(d -> new Episodio(t.numeroTemporada(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("Digite a partir de qual data que deseja filtrar os episódios:");
        var ano = Integer.parseInt(leitura.nextLine());

        LocalDate dataFiltro = LocalDate.of(ano, 1, 1);
        DateTimeFormatter fmt1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataFiltro))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                ", Episódio: " + e.getEpisodioNumero() +
                                " " + e.getTitulo() +
                                ", Lançamento: " + e.getDataLancamento().format(fmt1)
                ));


    }
}
