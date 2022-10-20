package ltd.newbee.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class NacosProviderDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(NacosProviderDemoApplication.class, args);
	}

}
