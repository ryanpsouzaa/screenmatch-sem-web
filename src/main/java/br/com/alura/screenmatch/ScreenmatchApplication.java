package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.services.ConverteDadosSerie;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import br.com.alura.screenmatch.services.ConsumoApi;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		var consumoApi = new ConsumoApi();
		String json = consumoApi.obterDados("https://www.omdbapi.com/?t=game+of+thrones&apikey=6938fd4d");

		System.out.println();
		System.out.println(json);

		ConverteDadosSerie conversor = new ConverteDadosSerie();
		DadosSerie dadosSerie = conversor.converteDados(json, DadosSerie.class);

		System.out.println();
		System.out.println(dadosSerie.toString());

	}
}
