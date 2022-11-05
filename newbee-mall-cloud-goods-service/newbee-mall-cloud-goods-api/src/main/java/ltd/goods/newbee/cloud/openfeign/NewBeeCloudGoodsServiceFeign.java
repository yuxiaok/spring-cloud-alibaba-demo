package ltd.goods.newbee.cloud.openfeign;

import ltd.common.newbee.cloud.dto.NewBeeMallGoodsDTO;
import ltd.common.newbee.cloud.dto.Result;
import ltd.common.newbee.cloud.dto.UpdateStockNumDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
 * <li>Creation : 2022年11⽉03⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
@FeignClient(value = "newbee-mall-cloud-goods-service", path = "/goods")
public interface NewBeeCloudGoodsServiceFeign {

	@GetMapping("/admin/detail")
	Result<NewBeeMallGoodsDTO> goodsDetail(@RequestParam("goodsId") Long goodsId);

	@GetMapping("/admin/listByGoodsIds")
	Result<List<NewBeeMallGoodsDTO>> getNewBeeMallGoodsByIds(@RequestParam("goodsIds") List<Long> goodsIds);

	@PutMapping(value = "/admin/updateStock")
	Result<Boolean> updateStock(@RequestBody UpdateStockNumDTO updateStockNumDTO);
}