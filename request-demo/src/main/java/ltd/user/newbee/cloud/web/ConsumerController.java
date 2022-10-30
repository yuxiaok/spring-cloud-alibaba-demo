package ltd.newbee.cloud.web;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : TODO </li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年10⽉16⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
@RestController
public class ConsumerController {


//	private final String SERVICE_URL = "http://localhost:8081";

	private final String SERVICE_URL = "http://localhost:8082";

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/httpClientTest")
	public String httpClientTest() throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(SERVICE_URL + "/hello");
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String s = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
				System.out.println(s);
			}
		} finally {
			if (httpResponse != null) {
				httpResponse.close();
			}
			httpClient.close();
		}
		return "success";
	}

	@GetMapping("/restTemplateTest")
	public String restTemplateTest() {
		String content = restTemplate.getForObject(SERVICE_URL + "/hello", String.class);
		System.out.println(content);
		return "success";
	}
}