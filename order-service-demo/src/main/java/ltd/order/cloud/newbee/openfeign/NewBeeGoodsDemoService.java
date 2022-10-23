package ltd.order.cloud.newbee.openfeign;

import ltd.newbee.cloud.entity.NewBeeGoodsInfo;
import ltd.newbee.cloud.entity.param.ComplexObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * Enum
 * AnnotationType
 * <ul style="margin:15px;">
 * <li>Description : TODO </li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年10⽉23⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
@FeignClient(value = "newbee-cloud-goods-service", path = "/goods")
public interface NewBeeGoodsDemoService {

	@GetMapping("/{goodsId}")
	String goodsDetail(@PathVariable("goodsId") int goodsId);

	//方法名称可以不和provider的一致
	@GetMapping(value = "/detail")
	String getGoodsDetail3(@RequestParam(value = "goodsId") int goodsId, @RequestParam(value = "sellStatus") int sellStatus);

	@GetMapping(value = "/listByIdArray")
	List<String> getGoodsArray(@RequestParam(value = "goodsIds") Integer[] goodsIds);

	@GetMapping(value = "/listByIdList")
	List<String> getGoodsList(@RequestParam(value = "goodsIds") List<Integer> goodsIds);

	@PostMapping(value = "/updNewBeeGoodsInfo")
	NewBeeGoodsInfo updNewBeeGoodsInfo(@RequestBody NewBeeGoodsInfo newBeeGoodsInfo);

	@PostMapping(value = "/testComplexObject")
	ComplexObject testComplexObject(@RequestBody ComplexObject complexObject);
}