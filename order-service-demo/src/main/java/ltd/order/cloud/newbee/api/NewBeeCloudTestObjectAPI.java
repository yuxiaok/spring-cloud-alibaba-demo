package ltd.order.cloud.newbee.api;

import ltd.newbee.cloud.entity.NewBeeCartItem;
import ltd.newbee.cloud.entity.NewBeeGoodsInfo;
import ltd.newbee.cloud.entity.param.ComplexObject;
import ltd.order.cloud.newbee.openfeign.NewBeeGoodsDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
public class NewBeeCloudTestObjectAPI {

	@Autowired
	private NewBeeGoodsDemoService simpleObjectService;

	@GetMapping("/order/simpleObjectTest")
	public String simpleObjectTest1() {

		NewBeeGoodsInfo newBeeGoodsInfo = new NewBeeGoodsInfo();
		newBeeGoodsInfo.setGoodsId(2022);
		newBeeGoodsInfo.setGoodsName("Spring Cloud Alibaba 微服务架构");
		newBeeGoodsInfo.setStock(2035);

		NewBeeGoodsInfo result = simpleObjectService.updNewBeeGoodsInfo(newBeeGoodsInfo);

		return result.toString();
	}


	@GetMapping("/order/complexbjectTest")
	public String complexbjectTest() {

		ComplexObject complexObject = new ComplexObject();

		complexObject.setRequestNum(13);

		List<Integer> cartIds = new ArrayList<>();
		cartIds.add(2022);
		cartIds.add(13);
		complexObject.setCartIds(cartIds);

		NewBeeCartItem newBeeCartItem = new NewBeeCartItem();
		newBeeCartItem.setItemId(2023);
		newBeeCartItem.setCartString("newbee cloud");
		complexObject.setNewBeeCartItem(newBeeCartItem);

		List<NewBeeGoodsInfo> newBeeGoodsInfos = new ArrayList<>();
		NewBeeGoodsInfo newBeeGoodsInfo1 = new NewBeeGoodsInfo();
		newBeeGoodsInfo1.setGoodsName("Spring Cloud Alibaba 大型微服务项目实战（上册）");
		newBeeGoodsInfo1.setGoodsId(2024);
		newBeeGoodsInfo1.setStock(10000);

		NewBeeGoodsInfo newBeeGoodsInfo2 = new NewBeeGoodsInfo();
		newBeeGoodsInfo2.setGoodsName("Spring Cloud Alibaba 大型微服务项目实战（下册）");
		newBeeGoodsInfo2.setGoodsId(2025);
		newBeeGoodsInfo2.setStock(10000);
		newBeeGoodsInfos.add(newBeeGoodsInfo1);
		newBeeGoodsInfos.add(newBeeGoodsInfo2);

		complexObject.setNewBeeGoodsInfos(newBeeGoodsInfos);

		// 以上这些代码相当于平时开发时的请求参数整理

		ComplexObject result = simpleObjectService.testComplexObject(complexObject);
		return result.toString();
	}
}