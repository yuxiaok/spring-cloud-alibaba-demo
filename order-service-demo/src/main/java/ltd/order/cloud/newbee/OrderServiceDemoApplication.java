package ltd.order.cloud.newbee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "ltd.order.cloud.newbee.openfeign")
@SpringBootApplication
public class OrderServiceDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceDemoApplication.class, args);
	}

}
