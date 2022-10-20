package ltd.newbee.cloud.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : TODO </li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年10⽉16⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
@RestController
public class ConsumerController {


//	private final String SERVICE_URL = "http://localhost:8081";

	private final String SERVICE_URL = "http://localhost:8082";

	private final WebClient webClient = WebClient.builder()
			.baseUrl(SERVICE_URL)
			.build();

	@GetMapping("/webClientTest")
	public String restTemplateTest() {
		Mono<String> mono = webClient
				.get()//请求方式
				.uri("/hello")//请求地址
				.retrieve()//获取响应结果
				.bodyToMono(String.class);//响应结果转换
		mono.subscribe(res -> {
			System.out.println(res);
		});
		return "success";
	}
}