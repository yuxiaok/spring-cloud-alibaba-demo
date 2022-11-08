package ltd.shopcart.newbee.cloud.openfeign;

import ltd.common.newbee.cloud.dto.NewBeeMallShoppingCartItemDTO;
import ltd.common.newbee.cloud.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
 * <li>Creation : 2022年11⽉05⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
@FeignClient(value = "newbee-mall-cloud-shop-cart-service", path = "/shop-cart")
public interface NewBeeCloudShopCartServiceFeign {

	@GetMapping(value = "/listByCartItemIds")
	Result<List<NewBeeMallShoppingCartItemDTO>> listByCartItemIds(@RequestParam(value = "cartItemIds") List<Long> cartItemIds);

	@DeleteMapping(value = "/deleteByCartItemIds")
	Result<Boolean> deleteByCartItemIds(@RequestParam(value = "cartItemIds") List<Long> cartItemIds);
}