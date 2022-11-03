/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.recommend.newbee.cloud.service.impl;

import ltd.common.newbee.cloud.common.ServiceResultEnum;
import ltd.common.newbee.cloud.dto.NewBeeMallGoodsDTO;
import ltd.common.newbee.cloud.dto.PageQueryUtil;
import ltd.common.newbee.cloud.dto.PageResult;
import ltd.common.newbee.cloud.dto.Result;
import ltd.goods.newbee.cloud.openfeign.NewBeeCloudGoodsServiceFeign;
import ltd.recommend.newbee.cloud.dao.IndexConfigMapper;
import ltd.recommend.newbee.cloud.entity.IndexConfig;
import ltd.recommend.newbee.cloud.service.NewBeeMallIndexConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NewBeeMallIndexConfigServiceImpl implements NewBeeMallIndexConfigService {

	@Autowired
	private IndexConfigMapper indexConfigMapper;

	@Autowired
	private NewBeeCloudGoodsServiceFeign newBeeCloudGoodsServiceFeign;

	@Override
	public PageResult getConfigsPage(PageQueryUtil pageUtil) {
		List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigList(pageUtil);
		int total = indexConfigMapper.getTotalIndexConfigs(pageUtil);
		PageResult pageResult = new PageResult(indexConfigs, total, pageUtil.getLimit(), pageUtil.getPage());
		return pageResult;
	}

	@Override
	public String saveIndexConfig(IndexConfig indexConfig) {
		Result<NewBeeMallGoodsDTO> goodsDetailResult = newBeeCloudGoodsServiceFeign.goodsDetail(indexConfig.getGoodsId());
		if (goodsDetailResult == null || goodsDetailResult.getResultCode() != 200) {
			// 商品数据不存在则直接返回错误提示
			return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
		}
		if (indexConfigMapper.selectByTypeAndGoodsId(indexConfig.getConfigType(), indexConfig.getGoodsId()) != null) {
			return ServiceResultEnum.SAME_INDEX_CONFIG_EXIST.getResult();
		}
		if (indexConfigMapper.insertSelective(indexConfig) > 0) {
			return ServiceResultEnum.SUCCESS.getResult();
		}
		return ServiceResultEnum.DB_ERROR.getResult();
	}

	@Override
	public String updateIndexConfig(IndexConfig indexConfig) {
		Result<NewBeeMallGoodsDTO> goodsDetailResult = newBeeCloudGoodsServiceFeign.goodsDetail(indexConfig.getGoodsId());
		if (goodsDetailResult == null || goodsDetailResult.getResultCode() != 200) {
			// 商品数据不存在则直接返回错误提示
			return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
		}
		IndexConfig temp = indexConfigMapper.selectByPrimaryKey(indexConfig.getConfigId());
		if (temp == null) {
			return ServiceResultEnum.DATA_NOT_EXIST.getResult();
		}
		IndexConfig temp2 = indexConfigMapper.selectByTypeAndGoodsId(indexConfig.getConfigType(), indexConfig.getGoodsId());
		if (temp2 != null && !temp2.getConfigId().equals(indexConfig.getConfigId())) {
			//goodsId相同且不同id 不能继续修改
			return ServiceResultEnum.SAME_INDEX_CONFIG_EXIST.getResult();
		}
		indexConfig.setUpdateTime(new Date());
		if (indexConfigMapper.updateByPrimaryKeySelective(indexConfig) > 0) {
			return ServiceResultEnum.SUCCESS.getResult();
		}
		return ServiceResultEnum.DB_ERROR.getResult();
	}

	@Override
	public IndexConfig getIndexConfigById(Long id) {
		return indexConfigMapper.selectByPrimaryKey(id);
	}

	@Override
	public Boolean deleteBatch(Long[] ids) {
		if (ids.length < 1) {
			return false;
		}
		//删除数据
		return indexConfigMapper.deleteBatch(ids) > 0;
	}
}
