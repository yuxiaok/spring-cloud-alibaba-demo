package ltd.order.cloud.newbee.config;

import org.springframework.context.annotation.Configuration;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : TODO </li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年10⽉23⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
@Configuration
public class RestTemplateConfig {
	//引入openfeign之后，可以无需配置了

//	@Bean
//	public ClientHttpRequestFactory clientHttpRequestFactory() {
//		SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
//		simpleClientHttpRequestFactory.setConnectTimeout(5 * 1000);
//		simpleClientHttpRequestFactory.setReadTimeout(10 * 1000);
//		return simpleClientHttpRequestFactory;
//	}
//
//	@LoadBalanced
//	@Bean
//	public RestTemplate restTemplate(ClientHttpRequestFactory httpRequestFactory) {
//		RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
//		restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(Charset.forName("UTF-8")));
//		return restTemplate;
//	}
}