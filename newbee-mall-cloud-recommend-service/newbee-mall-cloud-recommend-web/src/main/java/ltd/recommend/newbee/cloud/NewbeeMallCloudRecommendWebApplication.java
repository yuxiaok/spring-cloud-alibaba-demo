package ltd.recommend.newbee.cloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan(basePackages = "ltd.recommend.newbee.cloud.dao")
@EnableFeignClients(basePackages = {"ltd.user.newbee.cloud.openfeign", "ltd.goods.newbee.cloud.openfeign"})
@EnableDiscoveryClient
@SpringBootApplication
public class NewbeeMallCloudRecommendWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewbeeMallCloudRecommendWebApplication.class, args);
	}

}
