/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.recommend.newbee.cloud.service;

import ltd.common.newbee.cloud.dto.PageQueryUtil;
import ltd.common.newbee.cloud.dto.PageResult;
import ltd.recommend.newbee.cloud.controller.vo.NewBeeMallIndexConfigGoodsVO;
import ltd.recommend.newbee.cloud.entity.IndexConfig;

import java.util.List;

public interface NewBeeMallIndexConfigService {

	/**
	 * 后台分页
	 *
	 * @param pageUtil
	 * @return
	 */
	PageResult getConfigsPage(PageQueryUtil pageUtil);

	String saveIndexConfig(IndexConfig indexConfig);

	String updateIndexConfig(IndexConfig indexConfig);

	IndexConfig getIndexConfigById(Long id);

	Boolean deleteBatch(Long[] ids);

	List<NewBeeMallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number);
}
