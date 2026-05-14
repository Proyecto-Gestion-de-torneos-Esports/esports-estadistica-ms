package Torneo.Estadistica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class EstadisticaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EstadisticaApplication.class, args);
	}

}
