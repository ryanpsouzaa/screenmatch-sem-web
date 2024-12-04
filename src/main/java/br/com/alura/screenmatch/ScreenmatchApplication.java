package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.services.ConverteDadosSerie;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import br.com.alura.screenmatch.services.ConsumoApi;

import java.util.ArrayList;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		var consumoApi = new ConsumoApi();
		String jsonSerie = consumoApi.obterDados("https://www.omdbapi.com/?t=arcane&apikey=6938fd4d");
		String jsonEpisodio = consumoApi.obterDados("https://www.omdbapi.com/?t=arcane&season=1&episode=6&apikey=6938fd4d");


		System.out.println("\nJson Bruto:");
		System.out.println(jsonSerie);

		System.out.println("////////////////////////////");
		ConverteDadosSerie conversor = new ConverteDadosSerie();
		DadosSerie dadosSerie = conversor.converteDados(jsonSerie, DadosSerie.class);
		DadosEpisodio dadosEpisodio = conversor.converteDados(jsonEpisodio, DadosEpisodio.class);

		ArrayList<DadosTemporada> temporadas = new ArrayList<>();

		for(int i = 1; i <= dadosSerie.totalTemporadas(); i++){
			String jsonTemporada = consumoApi.obterDados("https://www.omdbapi.com/?t=arcane&season=" + i + "&apikey=6938fd4d");
			DadosTemporada dadosTemporada = conversor.converteDados(jsonTemporada, DadosTemporada.class);

			temporadas.add(dadosTemporada);
		}

		/* Realizando para Gson:

		DadosSerie dadosSerie = conversor.converteDados(json, DadosSerie.class);
		 */

		System.out.println();
		System.out.println(dadosSerie);
		System.out.println(dadosEpisodio);
		temporadas.forEach(System.out::println);


	}
}
