package cl.plataforma_gimnasio.ms_pago;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MsPagoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsPagoApplication.class, args);
	}

}
