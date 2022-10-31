package ltd.gateway.newbee.cloud.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ltd.common.newbee.cloud.dto.Result;
import ltd.common.newbee.cloud.dto.ResultGenerator;
import ltd.common.newbee.cloud.pojo.AdminUserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : TODO </li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年10⽉31⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
@Component
public class ValidTokenGlobalFilter implements GlobalFilter, Ordered {

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		//如果是登录接口，直接放行
		if ("/users/admin/login".equals(exchange.getRequest().getURI().getPath())) {
			return chain.filter(exchange);
		}

		HttpHeaders headers = exchange.getRequest().getHeaders();
		if (headers == null || headers.isEmpty()) {
			return wrapErrorResponse(exchange, chain);
		}

		String token = headers.getFirst("token");
		if (!StringUtils.hasLength(token)) {
			return wrapErrorResponse(exchange, chain);
		}

		ValueOperations<String, AdminUserToken> valueOperations = redisTemplate.opsForValue();
		AdminUserToken adminUserToken = valueOperations.get(token);
		if (Objects.isNull(adminUserToken)) {
			return wrapErrorResponse(exchange, chain);
		}
		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	private Mono<Void> wrapErrorResponse(ServerWebExchange exchange, GatewayFilterChain chain) {
		Result result = ResultGenerator.genErrorResult(419, "无权限访问");
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.valueToTree(result);
		byte[] bytes = jsonNode.toString().getBytes(StandardCharsets.UTF_8);
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.OK);
		DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);
		return response.writeWith(Flux.just(dataBuffer));
	}
}