package ltd.order.cloud.newbee.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : TODO </li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年10⽉29⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
@Configuration
public class OpenfeignConfiguration {

	@Bean
	public Logger.Level openFeignLevel() {
		//设置OpenFeign日志级别
		return Logger.Level.FULL;
	}
}