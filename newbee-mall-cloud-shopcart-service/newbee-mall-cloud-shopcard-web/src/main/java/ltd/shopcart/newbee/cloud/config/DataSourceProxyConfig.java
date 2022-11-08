package ltd.shopcart.newbee.cloud.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

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
public class DataSourceProxyConfig {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DruidDataSource druidDataSource() {
		return new DruidDataSource();
	}

	@Primary
	@Bean("dataSource")
	public DataSource dataSource(DruidDataSource druidDataSource) {
		return new DataSourceProxy(druidDataSource);
	}

}