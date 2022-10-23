package ltd.order.cloud.newbee.api;

import ltd.order.cloud.newbee.openfeign.NewBeeGoodsDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
public class NewBeeCloudTestSimpleParamAPI {

	@Autowired
	private NewBeeGoodsDemoService simpleParamService;

	@GetMapping("/order/simpleParamTest")
	public String simpleParamTest2(@RequestParam("sellStatus") int sellStatus, @RequestParam("goodsId") int goodsId) {
		String resultString = simpleParamService.getGoodsDetail3(goodsId, sellStatus);
		return resultString;
	}

	@GetMapping("/order/listByIdArray")
	public String listByIdArray() {

		Integer[] goodsIds = new Integer[4];
		goodsIds[0] = 1;
		goodsIds[1] = 3;
		goodsIds[2] = 5;
		goodsIds[3] = 7;

		List<String> result = simpleParamService.getGoodsArray(goodsIds);
		String resultString = "";
		for (String s : result) {
			resultString += s + " ";
		}
		return resultString;
	}

	@GetMapping("/order/listByIdList")
	public String listByIdList() {
		List<Integer> goodsIds = new ArrayList<>();
		goodsIds.add(2);
		goodsIds.add(4);
		goodsIds.add(6);
		goodsIds.add(8);

		List<String> result = simpleParamService.getGoodsList(goodsIds);
		String resultString = "";
		for (String s : result) {
			resultString += s + " ";
		}
		return resultString;
	}
}