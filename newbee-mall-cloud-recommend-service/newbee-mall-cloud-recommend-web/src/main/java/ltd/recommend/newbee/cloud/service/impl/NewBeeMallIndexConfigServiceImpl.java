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
import ltd.common.newbee.cloud.util.BeanUtil;
import ltd.goods.newbee.cloud.openfeign.NewBeeCloudGoodsServiceFeign;
import ltd.recommend.newbee.cloud.controller.vo.NewBeeMallIndexConfigGoodsVO;
import ltd.recommend.newbee.cloud.dao.IndexConfigMapper;
import ltd.recommend.newbee.cloud.entity.IndexConfig;
import ltd.recommend.newbee.cloud.service.NewBeeMallIndexConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

	@Override
	public List<NewBeeMallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number) {
		List<NewBeeMallIndexConfigGoodsVO> newBeeMallIndexConfigGoodsVOS = new ArrayList<>(number);
		List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigsByTypeAndNum(configType, number);
		if (!CollectionUtils.isEmpty(indexConfigs)) {
			//取出所有的goodsId
			List<Long> goodsIds = indexConfigs.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
			//根据商品id列表查询对应的商品数据
			Result<List<NewBeeMallGoodsDTO>> result = newBeeCloudGoodsServiceFeign.getNewBeeMallGoodsByIds(goodsIds);
			if (result == null || result.getResultCode() != 200 || CollectionUtils.isEmpty(result.getData())) {
				// 未查询到数据 返回空链表（也可以直接在这里丢出异常）
				return newBeeMallIndexConfigGoodsVOS;
			}
			newBeeMallIndexConfigGoodsVOS = BeanUtil.copyList(result.getData(), NewBeeMallIndexConfigGoodsVO.class);
			// 转换为VO对象
			for (NewBeeMallIndexConfigGoodsVO newBeeMallIndexConfigGoodsVO : newBeeMallIndexConfigGoodsVOS) {
				String goodsName = newBeeMallIndexConfigGoodsVO.getGoodsName();
				// 字符串过长导致文字超出的问题
				if (goodsName.length() > 30) {
					goodsName = goodsName.substring(0, 30) + "...";
					newBeeMallIndexConfigGoodsVO.setGoodsName(goodsName);
				}
			}
		}
		return newBeeMallIndexConfigGoodsVOS;
	}

}
