package ltd.newbee.cloud.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : TODO </li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年10⽉18⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
@RestController
public class ConsumerTestController {

	private static final String SERVICE_URL = "http://newbee-cloud-goods-service";

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/nacosRegTest")
	public String nacosRegTest() {
		return "nacosRegTest";
	}

	@GetMapping("/consumerTest")
	public String consumerTest() {
		return restTemplate.getForObject(SERVICE_URL + "/goodsServiceTest", String.class);
	}
}