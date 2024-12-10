package br.com.alura.screenmatch;
import br.com.alura.screenmatch.principal.Principal;
import br.com.alura.screenmatch.principal.PrincipalV2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		PrincipalV2 menu = new PrincipalV2();
		menu.exibirMenu();

//		Principal menu = new Principal();
//		menu.inciarMenu();
	}
}
