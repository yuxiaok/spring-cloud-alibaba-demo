package ltd.newbee.cloud.predicate;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.cloud.gateway.handler.predicate.RoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : 自定义Predicate，默认规则：xxxRoutePredicateFactory中xxx就是配置文件中引用的名称，
 *
 * @see RoutePredicateFactory#name()
 *
 * </li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年10⽉23⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
@Component
public class GoodsIdRoutePredicateFactory extends AbstractRoutePredicateFactory<GoodsIdRoutePredicateFactory.Config> {

	//构造函数，传入配置类
	public GoodsIdRoutePredicateFactory() {
		super(Config.class);
	}

	//指定配置参数顺序
	@Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList("minValue", "maxValue");
	}

	@Override
	public Predicate<ServerWebExchange> apply(Config config) {
		return new GatewayPredicate() {
			@Override
			public boolean test(ServerWebExchange serverWebExchange) {
				String goodsId = serverWebExchange.getRequest().getQueryParams().getFirst("goodsId");
				if (StringUtils.hasText(goodsId)) {
					int numberId = Integer.parseInt(goodsId);
					//判断goodsId是否在配置区间内
					if (numberId > config.getMinValue() && numberId < config.getMaxValue()) {
						//符合条件，则返回true
						return true;
					}
				}
				//不符合条件，返回false
				return false;
			}
		};
	}

	/**
	 * 接受配置文件中配置的最大值和最小值
	 */
	@Validated
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