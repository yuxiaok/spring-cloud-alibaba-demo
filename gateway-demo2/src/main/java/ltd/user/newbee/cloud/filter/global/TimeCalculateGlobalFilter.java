package ltd.newbee.cloud.filter.global;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : 全局过滤器：统计接口调用时间 (无需指定引用)</li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年10⽉24⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
@Component
public class TimeCalculateGlobalFilter implements GlobalFilter, Ordered {

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		long startTime = System.currentTimeMillis();
		String requestURL = String.format("Host:%s Path:%s Params:%s",
				exchange.getRequest().getURI().getHost(),
				exchange.getRequest().getURI().getPath(),
				exchange.getRequest().getQueryParams());

		System.out.println(requestURL);

		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			long endTime = System.currentTimeMillis();
			long requestTime = endTime - startTime;
			System.out.println(exchange.getRequest().getURI().getPath() + "请求时间为：" + requestTime + "ms");
		}));
	}
}