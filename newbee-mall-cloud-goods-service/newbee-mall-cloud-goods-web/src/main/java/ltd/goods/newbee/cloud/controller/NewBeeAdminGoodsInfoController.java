/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.goods.newbee.cloud.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ltd.common.newbee.cloud.common.Constants;
import ltd.common.newbee.cloud.common.ServiceResultEnum;
import ltd.common.newbee.cloud.dto.NewBeeMallGoodsDTO;
import ltd.common.newbee.cloud.dto.PageQueryUtil;
import ltd.common.newbee.cloud.dto.Result;
import ltd.common.newbee.cloud.dto.ResultGenerator;
import ltd.common.newbee.cloud.pojo.LoginAdminUser;
import ltd.common.newbee.cloud.util.BeanUtil;
import ltd.goods.newbee.cloud.config.annotation.TokenToAdminUser;
import ltd.goods.newbee.cloud.controller.param.BatchIdParam;
import ltd.goods.newbee.cloud.controller.param.GoodsAddParam;
import ltd.goods.newbee.cloud.controller.param.GoodsEditParam;
import ltd.goods.newbee.cloud.entity.GoodsCategory;
import ltd.goods.newbee.cloud.entity.NewBeeMallGoods;
import ltd.goods.newbee.cloud.service.NewBeeMallCategoryService;
import ltd.goods.newbee.cloud.service.NewBeeMallGoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 13
 * @qq交流群 796794009
 * @email 2449207463@qq.com
 * @link https://github.com/newbee-ltd
 */
@RestController
@Api(value = "v1", tags = "8-3.后台管理系统商品模块接口")
@RequestMapping("/goods/admin")
public class NewBeeAdminGoodsInfoController {

	private static final Logger logger = LoggerFactory.getLogger(NewBeeAdminGoodsInfoController.class);

	@Resource
	private NewBeeMallGoodsService newBeeMallGoodsService;
	@Resource
	private NewBeeMallCategoryService newBeeMallCategoryService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ApiOperation(value = "商品列表", notes = "可根据名称和上架状态筛选")
	public Result list(@RequestParam(required = false) @ApiParam(value = "页码") Integer pageNumber,
					   @RequestParam(required = false) @ApiParam(value = "每页条数") Integer pageSize,
					   @RequestParam(required = false) @ApiParam(value = "商品名称") String goodsName,
					   @RequestParam(required = false) @ApiParam(value = "上架状态 0-上架 1-下架") Integer goodsSellStatus,
					   @TokenToAdminUser LoginAdminUser adminUser) {
		logger.info("adminUser:{}", adminUser.toString());
		if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10) {
			return ResultGenerator.genFailResult("分页参数异常！");
		}
		Map params = new HashMap(8);
		params.put("page", pageNumber);
		params.put("limit", pageSize);
		if (!StringUtils.isEmpty(goodsName)) {
			params.put("goodsName", goodsName);
		}
		if (goodsSellStatus != null) {
			params.put("goodsSellStatus", goodsSellStatus);
		}
		PageQueryUtil pageUtil = new PageQueryUtil(params);
		return ResultGenerator.genSuccessResult(newBeeMallGoodsService.getNewBeeMallGoodsPage(pageUtil));
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "新增商品信息", notes = "新增商品信息")
	public Result save(@RequestBody @Valid GoodsAddParam goodsAddParam, @TokenToAdminUser LoginAdminUser adminUser) {
		logger.info("adminUser:{}", adminUser.toString());
		NewBeeMallGoods newBeeMallGoods = new NewBeeMallGoods();
		BeanUtil.copyProperties(goodsAddParam, newBeeMallGoods);
		String result = newBeeMallGoodsService.saveNewBeeMallGoods(newBeeMallGoods);
		if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult(result);
		}
	}


	/**
	 * 修改
	 */
	@RequestMapping(value = "", method = RequestMethod.PUT)
	@ApiOperation(value = "修改商品信息", notes = "修改商品信息")
	public Result update(@RequestBody @Valid GoodsEditParam goodsEditParam, @TokenToAdminUser LoginAdminUser adminUser) {
		logger.info("adminUser:{}", adminUser.toString());
		NewBeeMallGoods newBeeMallGoods = new NewBeeMallGoods();
		BeanUtil.copyProperties(goodsEditParam, newBeeMallGoods);
		String result = newBeeMallGoodsService.updateNewBeeMallGoods(newBeeMallGoods);
		if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult(result);
		}
	}

	/**
	 * 详情
	 */
	@GetMapping("/{id}")
	@ApiOperation(value = "获取单条商品信息", notes = "根据id查询")
	public Result info(@PathVariable("id") Long id, @TokenToAdminUser LoginAdminUser adminUser) {
		logger.info("adminUser:{}", adminUser.toString());
		Map goodsInfo = new HashMap(8);
		NewBeeMallGoods goods = newBeeMallGoodsService.getNewBeeMallGoodsById(id);
		if (goods == null) {
			return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
		}
		goodsInfo.put("goods", goods);
		GoodsCategory thirdCategory;
		GoodsCategory secondCategory;
		GoodsCategory firstCategory;
		thirdCategory = newBeeMallCategoryService.getGoodsCategoryById(goods.getGoodsCategoryId());
		if (thirdCategory != null) {
			goodsInfo.put("thirdCategory", thirdCategory);
			secondCategory = newBeeMallCategoryService.getGoodsCategoryById(thirdCategory.getParentId());
			if (secondCategory != null) {
				goodsInfo.put("secondCategory", secondCategory);
				firstCategory = newBeeMallCategoryService.getGoodsCategoryById(secondCategory.getParentId());
				if (firstCategory != null) {
					goodsInfo.put("firstCategory", firstCategory);
				}
			}
		}
		return ResultGenerator.genSuccessResult(goodsInfo);
	}

	/**
	 * 批量修改销售状态
	 */
	@RequestMapping(value = "/status/{sellStatus}", method = RequestMethod.PUT)
	@ApiOperation(value = "批量修改销售状态", notes = "批量修改销售状态")
	public Result delete(@RequestBody BatchIdParam batchIdParam, @PathVariable("sellStatus") int sellStatus,
						 @TokenToAdminUser LoginAdminUser adminUser) {
		logger.info("adminUser:{}", adminUser.toString());
		if (batchIdParam == null || batchIdParam.getIds().length < 1) {
			return ResultGenerator.genFailResult("参数异常！");
		}
		if (sellStatus != Constants.SELL_STATUS_UP && sellStatus != Constants.SELL_STATUS_DOWN) {
			return ResultGenerator.genFailResult("状态异常！");
		}
		if (newBeeMallGoodsService.batchUpdateSellStatus(batchIdParam.getIds(), sellStatus)) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult("修改失败");
		}
	}

	@GetMapping("/detail")
	@ApiOperation(value = "获取单条商品信息", notes = "根据id查询")
	public Result<NewBeeMallGoodsDTO> goodsDetail(@RequestParam("goodsId") Long goodsId) {
		NewBeeMallGoods goods = newBeeMallGoodsService.getNewBeeMallGoodsById(goodsId);
		NewBeeMallGoodsDTO newBeeMallGoodsDTO = new NewBeeMallGoodsDTO();
		BeanUtil.copyProperties(goods, newBeeMallGoodsDTO);
		return ResultGenerator.genSuccessResult(newBeeMallGoodsDTO);
	}

	/**
	 * 根据ids查询商品列表
	 */
	@GetMapping("/listByGoodsIds")
	@ApiOperation(value = "根据ids查询商品列表", notes = "根据ids查询")
	public Result<List<NewBeeMallGoodsDTO>> getNewBeeMallGoodsByIds(@RequestParam("goodsIds") List<Long> goodsIds) {
		List<NewBeeMallGoods> newBeeMallGoods = newBeeMallGoodsService.getNewBeeMallGoodsById(goodsIds);
		List<NewBeeMallGoodsDTO> newBeeMallGoodsDTOS = BeanUtil.copyList(newBeeMallGoods, NewBeeMallGoodsDTO.class);
		return ResultGenerator.genSuccessResult(newBeeMallGoodsDTOS);
	}
}