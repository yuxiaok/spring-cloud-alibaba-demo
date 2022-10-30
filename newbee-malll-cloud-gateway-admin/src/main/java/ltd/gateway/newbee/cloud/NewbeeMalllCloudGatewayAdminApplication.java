package ltd.gateway.newbee.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class NewbeeMalllCloudGatewayAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewbeeMalllCloudGatewayAdminApplication.class, args);
	}

}
