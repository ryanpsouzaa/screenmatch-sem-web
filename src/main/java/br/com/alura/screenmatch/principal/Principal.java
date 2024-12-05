package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.services.ConsumoApi;
import br.com.alura.screenmatch.services.Conversor;

import java.util.ArrayList;
import java.util.Scanner;

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

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));


    }
}
