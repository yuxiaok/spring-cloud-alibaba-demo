package ltd.order.cloud.newbee.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
public class NewBeeCloudShopCartAPI {
	@Value("${server.port}")
	private String applicationServerPort;// 读取当前应用的启动端口

	@GetMapping("/shop-cart/{cartId}")
	public String cartItemDetail(@PathVariable("cartId") int cartId) {
		// 根据id查询商品并返回给调用端
		if (cartId < 0 || cartId > 100000) {
			return "查询购物项为空，当前服务的端口号为" + applicationServerPort;
		}
		String cartItem = "购物项" + cartId;
		// 返回信息给调用端
		return cartItem + "，当前服务的端口号为" + applicationServerPort;
	}
}