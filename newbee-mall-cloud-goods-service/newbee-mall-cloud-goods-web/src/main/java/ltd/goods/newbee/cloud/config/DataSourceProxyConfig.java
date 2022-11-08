package ltd.goods.newbee.cloud.config;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : TODO </li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年11⽉08⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
@Configuration
@EnableAutoDataSourceProxy
public class DataSourceProxyConfig {

	/*
	 * 解决druid 日志报错：discard long time none received connection:xxx
	 * */
	@PostConstruct
	public void setProperties() {
		System.setProperty("druid.mysql.usePingMethod", "false");
	}
}