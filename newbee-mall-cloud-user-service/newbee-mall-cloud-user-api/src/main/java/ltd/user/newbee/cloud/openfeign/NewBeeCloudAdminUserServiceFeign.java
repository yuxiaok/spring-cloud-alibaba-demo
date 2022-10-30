package ltd.user.newbee.cloud.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : TODO </li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年10⽉30⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
@FeignClient(value = "newbee-mall-cloud-user-service", path = "/users")
public interface NewBeeCloudAdminUserServiceFeign {

	@GetMapping(value = "/admin/{token}")
	String getAdminUserByToken(@PathVariable(value = "token") String token);
}