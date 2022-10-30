package ltd.order.cloud.newbee.api;

import ltd.order.cloud.newbee.openfeign.NewBeeGoodsDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

	@Autowired
	private NewBeeGoodsDemoService newBeeGoodsDemoService;

	@GetMapping("/shop-cart/{cartId}")
	public String cartItemDetail(@PathVariable("cartId") int cartId) {
		String goodsDetail = newBeeGoodsDemoService.goodsDetail1(2025);
		// 根据id查询商品并返回给调用端
		if (cartId < 0 || cartId > 100000) {
			return "查询购物项为空，当前服务的端口号为" + applicationServerPort;
		}
		String cartItem = "购物项" + cartId;
		// 返回信息给调用端
		return cartItem + "，当前服务的端口号为" + applicationServerPort;
	}

	@GetMapping("/shop-cart/page/{pageNum}")
	public String cartItemList(@PathVariable("pageNum") int pageNum) throws InterruptedException {
		// 返回信息给调用端
		return "请求cartItemList，当前服务的端口号为" + applicationServerPort;
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/shop-cart/getGoodsId")
	public int getGoodsId(@RequestParam("cartId") int cartId) {
		// 根据主键id查询购物表
		Map<String, Object> cartItemObject = jdbcTemplate.queryForMap("select * from tb_cart_item where cart_id = " + cartId + " limit 1");
		if (cartItemObject.containsKey("goods_id")) {
			// 返回商品id
			return (int) cartItemObject.get("goods_id");
		}
		return 0;
	}

	@DeleteMapping("/shop-cart/{cartId}")
	public Boolean deleteItem(@PathVariable("cartId") int cartId) {
		// 删除购物车数据
		int result = jdbcTemplate.update("delete from tb_cart_item where cart_id = " + cartId);
		if (result > 0) {
			return true;
		}
		return false;
	}
}