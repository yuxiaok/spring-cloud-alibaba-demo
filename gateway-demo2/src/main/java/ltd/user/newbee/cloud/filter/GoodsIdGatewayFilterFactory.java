package ltd.newbee.cloud.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description :局部过滤器， xxxGatewayFilterFactory，xxx就是引用的名称，@see org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory
 * .name </li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年10⽉24⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
@Component
public class GoodsIdGatewayFilterFactory extends AbstractGatewayFilterFactory<GoodsIdGatewayFilterFactory.Config> {

	//构造函数，传入配置类
	public GoodsIdGatewayFilterFactory() {
		super(Config.class);
	}

	//配置参数顺序
	@Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList("minValue", "maxValue");
	}

	@Override
	public GatewayFilter apply(Config config) {
		return new GatewayFilter() {

			@Override
			public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
				String goodsIdParam = exchange.getRequest().getQueryParams().getFirst("goodsId");
				if (!StringUtils.isEmpty(goodsIdParam)) {
					int goodsId = Integer.parseInt(goodsIdParam);
					if (goodsId > config.getMinValue() && goodsId < config.getMaxValue()) {
						//判断goodsId在区间内，直接放行
						return chain.filter(exchange);
					} else {
						//不符合条件，返回错误的提示信息，不进行后续的路由
						byte[] bytes = ("BAD REQUEST").getBytes(StandardCharsets.UTF_8);
						DataBuffer wrap = exchange.getResponse().bufferFactory().wrap(bytes);
						exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
						return exchange.getResponse().writeWith(Flux.just(wrap));
					}
				}
				//直接放行
				return chain.filter(exchange);
			}
		};
	}

	public static class Config {
		private int minValue;

		private int maxValue;

		public int getMinValue() {
			return minValue;
		}

		public void setMinValue(int minValue) {
			this.minValue = minValue;
		}

		public int getMaxValue() {
			return maxValue;
		}

		public void setMaxValue(int maxValue) {
			this.maxValue = maxValue;
		}
	}
}