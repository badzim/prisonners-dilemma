package fr.uga.miage.m1.my_project;

import fr.uga.miage.m1.my_project.component.RencontreComponent;
import fr.uga.miage.m1.my_project.model.Server;
import fr.uga.miage.m1.my_project.service.RencontreManagerService;
import fr.uga.miage.m1.my_project.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyProjectApplication implements CommandLineRunner {

	@Autowired
	private ServerService serverService;


	public static void main(String[] args) {
		SpringApplication.run(MyProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Server server = new Server();
		serverService.start(server);
	}

}



