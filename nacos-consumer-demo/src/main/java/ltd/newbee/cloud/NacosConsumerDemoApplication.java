package ltd.newbee.cloud;

import ltd.newbee.cloud.config.NewBeeCloudLoadBalancerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;

@LoadBalancerClient(value = "newbee-cloud-goods-service", configuration = NewBeeCloudLoadBalancerConfiguration.class)
@SpringBootApplication
public class NacosConsumerDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(NacosConsumerDemoApplication.class, args);
	}

}
