package ltd.order.cloud.newbee.api;

import ltd.order.cloud.newbee.openfeign.NewBeeGoodsDemoService;
import ltd.order.cloud.newbee.openfeign.NewBeeShopCartDemoService;
import ltd.order.cloud.newbee.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
public class NewBeeCloudOrderAPI {
//	@Resource
//	private RestTemplate restTemplate;
//
//	// 商品服务调用地址
//	private final String CLOUD_GOODS_SERVICE_URL = "http://newbee-cloud-goods-service";
//
//	// 购物车服务调用地址
//	private final String CLOUD_SHOPCART_SERVICE_URL = "http://newbee-cloud-shopcart-service";


//
//	@GetMapping("/order/saveOrder")
//	public String saveOrder(@RequestParam("cartId") int cartId, @RequestParam("goodsId") int goodsId) {
//		// 简单的模拟下单流程，包括服务间的调用流程。后续openfeign相关的改造和优化将基于当前项目进行改造。
//
//		// 调用商品服务
//		String goodsResult = restTemplate.getForObject(CLOUD_GOODS_SERVICE_URL + "/goods/" + goodsId, String.class);
//
//		// 调用购物车服务
//		String cartResult = restTemplate.getForObject(CLOUD_SHOPCART_SERVICE_URL + "/shop-cart/" + cartId, String.class);
//
//		// 执行下单逻辑
//
//		return "success! goodsResult={" + goodsResult + "},cartResult={" + cartResult + "}";
//	}


	@Autowired
	private NewBeeGoodsDemoService newBeeGoodsDemoService;

	@Autowired
	private NewBeeShopCartDemoService newBeeShopCartDemoService;

	@GetMapping("/order/saveOrder")
	public String saveOrder(@RequestParam("cartId") int cartId, @RequestParam("goodsId") int goodsId) {
		// 简单的模拟下单流程，包括服务间的调用流程。后续openfeign相关的改造和优化将基于当前项目进行改造。

		// 调用商品服务
		String goodsResult = newBeeGoodsDemoService.goodsDetail(goodsId);

		// 调用购物车服务
		String cartResult = newBeeShopCartDemoService.cartItemDetail(cartId);

		// 执行下单逻辑

		return "success! goodsResult={" + goodsResult + "},cartResult={" + cartResult + "}";
	}

	@Autowired
	private OrderService orderService;

	@GetMapping("/order/saveOrder/tx")
	public Boolean saveOrder(@RequestParam("cartId") int cartId) {
		return orderService.saveOrder(cartId);
	}

	@GetMapping("/order/testChainApi1")
	public String testChainApi1() {
		String result = orderService.getNumber(2022);
		if ("BLOCKED".equals(result)) {
			return "testChainApi1 error! " + result;
		}
		return "testChainApi1 success! " + result;
	}

	@GetMapping("/order/testChainApi2")
	public String testChainApi2() {
		String result = orderService.getNumber(2025);
		if ("BLOCKED".equals(result)) {
			return "testChainApi2 error! " + result;
		}
		return "testChainApi2 success! " + result;
	}

	@GetMapping("/order/testRelateApi1")
	public String testRelateApi1() {
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			return "testRelateApi1 error!";
//		}
		return "testRelateApi1 success!";
	}

	@GetMapping("/order/testRelateApi2")
	public String testRelateApi2() {
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			return "testRelateApi2 error!";
//		}
		return "testRelateApi2 success!";
	}
}