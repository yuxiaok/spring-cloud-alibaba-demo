package ltd.newbee.cloud.config;

import org.springframework.context.annotation.Configuration;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : java config的方式配置 </li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年10⽉23⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
@Configuration
public class GatewayConfig {

//	@Bean
//	public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
//		return routeLocatorBuilder.routes()
//				.route(r -> r.host("**.abc.org").and().path("/image/png")
//						.uri("http://httpbin.org:80")
//				)
//				.route(r -> r.path("/image/webp")
//						.uri("http://httpbin.org:80")
//				)
//				.route(r -> r.order(-1)
//						.host("**.throttle.org").and().path("/get")
//						.uri("http://httpbin.org:80")
//				)
//				.build();
//	}
}