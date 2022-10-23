package ltd.newbee.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class NacosProviderDemo3Application {

	public static void main(String[] args) {
		SpringApplication.run(NacosProviderDemo3Application.class, args);
	}

}
