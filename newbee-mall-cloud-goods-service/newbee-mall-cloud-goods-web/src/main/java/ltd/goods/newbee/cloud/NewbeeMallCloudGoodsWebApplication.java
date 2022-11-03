package ltd.goods.newbee.cloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan(basePackages = "ltd.goods.newbee.cloud.dao")
@EnableFeignClients(basePackageClasses = {ltd.user.newbee.cloud.openfeign.NewBeeCloudAdminUserServiceFeign.class})
@EnableDiscoveryClient
@SpringBootApplication
public class NewbeeMallCloudGoodsWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewbeeMallCloudGoodsWebApplication.class, args);
	}

}
