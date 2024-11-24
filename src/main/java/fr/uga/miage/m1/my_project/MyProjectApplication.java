package fr.uga.miage.m1.my_project;

import fr.uga.miage.m1.my_project.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyProjectApplication {

	@Autowired
	private ServerService serverService;


	public static void main(String[] args) {
		SpringApplication.run(MyProjectApplication.class, args);
	}

}



