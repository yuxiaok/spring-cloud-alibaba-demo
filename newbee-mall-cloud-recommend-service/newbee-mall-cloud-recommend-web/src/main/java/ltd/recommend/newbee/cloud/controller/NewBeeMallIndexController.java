package ltd.recommend.newbee.cloud.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ltd.common.newbee.cloud.common.IndexConfigTypeEnum;
import ltd.common.newbee.cloud.dto.Result;
import ltd.common.newbee.cloud.dto.ResultGenerator;
import ltd.recommend.newbee.cloud.controller.vo.IndexInfoVO;
import ltd.recommend.newbee.cloud.controller.vo.NewBeeMallIndexCarouselVO;
import ltd.recommend.newbee.cloud.controller.vo.NewBeeMallIndexConfigGoodsVO;
import ltd.recommend.newbee.cloud.service.NewBeeMallCarouselService;
import ltd.recommend.newbee.cloud.service.NewBeeMallIndexConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : TODO </li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年11⽉05⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
@RestController
@Api(value = "v1", tags = "新蜂商城首页接口")
@RequestMapping("/mall/index")
public class NewBeeMallIndexController {
	@Resource
	private NewBeeMallCarouselService newBeeMallCarouselService;

	@Resource
	private NewBeeMallIndexConfigService newBeeMallIndexConfigService;

	@GetMapping("/recommondInfos")
	@ApiOperation(value = "获取首页数据", notes = "轮播图、新品、推荐等")
	public Result<IndexInfoVO> indexInfo() {
		IndexInfoVO indexInfoVO = new IndexInfoVO();
		List<NewBeeMallIndexCarouselVO> carousels = newBeeMallCarouselService.getCarouselsForIndex(5);
		List<NewBeeMallIndexConfigGoodsVO> hotGoodses = newBeeMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), 4);
		List<NewBeeMallIndexConfigGoodsVO> newGoodses = newBeeMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), 6);
		List<NewBeeMallIndexConfigGoodsVO> recommendGoodses = newBeeMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), 10);
		indexInfoVO.setCarousels(carousels);
		indexInfoVO.setHotGoodses(hotGoodses);
		indexInfoVO.setNewGoodses(newGoodses);
		indexInfoVO.setRecommendGoodses(recommendGoodses);
		return ResultGenerator.genSuccessResult(indexInfoVO);
	}
}