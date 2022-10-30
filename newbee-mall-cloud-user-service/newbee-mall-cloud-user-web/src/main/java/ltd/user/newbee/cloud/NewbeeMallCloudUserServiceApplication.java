package ltd.user.newbee.cloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan(basePackages = "ltd.user.newbee.cloud.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class NewbeeMallCloudUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewbeeMallCloudUserServiceApplication.class, args);
	}

}
