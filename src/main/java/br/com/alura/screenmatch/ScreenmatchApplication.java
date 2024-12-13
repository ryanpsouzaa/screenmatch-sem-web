package br.com.alura.screenmatch;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.principal.Principal;
import br.com.alura.screenmatch.principal.PrincipalV2;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	@Autowired
	private SerieRepository repositorio;

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		PrincipalV2 menu = new PrincipalV2(repositorio);
		menu.exibirMenu();

//		Principal menu = new Principal();
//		menu.inciarMenu();
	}
}
