package ltd.order.newbee.cloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan(basePackages = {"ltd.order.newbee.cloud.dao"})
@EnableFeignClients(basePackages = {"ltd.goods.newbee.cloud.openfeign", "ltd.shopcart.newbee.cloud.openfeign", "ltd.user.newbee.cloud.openfeign"})
@EnableDiscoveryClient
@SpringBootApplication
public class NewbeeMallCloudOrderWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewbeeMallCloudOrderWebApplication.class, args);
	}

}
