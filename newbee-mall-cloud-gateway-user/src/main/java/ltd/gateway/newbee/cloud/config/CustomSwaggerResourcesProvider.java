package ltd.gateway.newbee.cloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : TODO </li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年11⽉05⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
@Primary
@Component
public class CustomSwaggerResourcesProvider implements SwaggerResourcesProvider {

	/**
	 * Swagger Doc的URL后缀
	 */
	public static final String API_DOCS_URL = "/v3/api-docs";

	@Autowired
	private GatewayProperties gatewayProperties;

	@Override
	public List<SwaggerResource> get() {
		List<SwaggerResource> resources = new ArrayList<>();
		List<String> routes = new ArrayList<>();
		//需要聚合的路由配置
		routes.add("user-service-swagger-route");
		routes.add("recommend-service-swagger-route");
		routes.add("goods-service-swagger-route");
		routes.add("order-service-swagger-route");
		routes.add("shop-cart-service-swagger-route");
		gatewayProperties.getRoutes().stream()
				.filter(routeDefinition -> routes.contains(routeDefinition.getId()))
				.forEach(routeDefinition -> routeDefinition.getPredicates().stream()
						.filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
						.forEach(predicateDefinition ->
								resources.add(swaggerResource(routeDefinition.getId(),
										predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")
												.replace("/**", API_DOCS_URL)))));
		return resources;
	}

	private SwaggerResource swaggerResource(String name, String url) {
		SwaggerResource swaggerResource = new SwaggerResource();
		swaggerResource.setName(name);
		swaggerResource.setLocation(url);
		swaggerResource.setSwaggerVersion("3.0.3");
		return swaggerResource;
	}
}