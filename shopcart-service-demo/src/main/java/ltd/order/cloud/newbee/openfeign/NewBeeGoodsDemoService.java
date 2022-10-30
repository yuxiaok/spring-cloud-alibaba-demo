package ltd.order.cloud.newbee.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * Enum
 * AnnotationType
 * <ul style="margin:15px;">
 * <li>Description : TODO </li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年10⽉29⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
@FeignClient(value = "newbee-cloud-goods-service", path = "/goods")
public interface NewBeeGoodsDemoService {

	@GetMapping("")
	String goodsDetail1(@RequestParam("goodsId") int goodsId);
}