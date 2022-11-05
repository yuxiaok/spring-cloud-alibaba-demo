/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.shopcart.newbee.cloud.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ltd.common.newbee.cloud.common.Constants;
import ltd.common.newbee.cloud.common.ServiceResultEnum;
import ltd.common.newbee.cloud.dto.*;
import ltd.common.newbee.cloud.exception.NewBeeMallException;
import ltd.common.newbee.cloud.pojo.MallUserToken;
import ltd.shopcart.newbee.cloud.config.annotation.TokenToMallUser;
import ltd.shopcart.newbee.cloud.controller.param.SaveCartItemParam;
import ltd.shopcart.newbee.cloud.controller.param.UpdateCartItemParam;
import ltd.shopcart.newbee.cloud.controller.vo.NewBeeMallShoppingCartItemVO;
import ltd.shopcart.newbee.cloud.entity.NewBeeMallShoppingCartItem;
import ltd.shopcart.newbee.cloud.service.NewBeeMallShoppingCartService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "v1", tags = "5.新蜂商城购物车相关接口")
@RequestMapping("/shop-cart")
public class NewBeeMallShoppingCartAPI {

	@Resource
	private NewBeeMallShoppingCartService newBeeMallShoppingCartService;

	@GetMapping("/page")
	@ApiOperation(value = "购物车列表(每页默认5条)", notes = "传参为页码")
	public Result<PageResult<List<NewBeeMallShoppingCartItemVO>>> cartItemPageList(Integer pageNumber,
																				   @TokenToMallUser MallUserToken loginMallUser) {
		Map params = new HashMap(8);
		if (pageNumber == null || pageNumber < 1) {
			pageNumber = 1;
		}
		params.put("userId", loginMallUser.getUserId());
		params.put("page", pageNumber);
		params.put("limit", Constants.SHOPPING_CART_PAGE_LIMIT);
		//封装分页请求参数
		PageQueryUtil pageUtil = new PageQueryUtil(params);
		return ResultGenerator.genSuccessResult(newBeeMallShoppingCartService.getMyShoppingCartItems(pageUtil));
	}

	@GetMapping("")
	@ApiOperation(value = "购物车列表(网页移动端不分页)", notes = "")
	public Result<List<NewBeeMallShoppingCartItemVO>> cartItemList(@TokenToMallUser MallUserToken loginMallUser) {
		return ResultGenerator.genSuccessResult(newBeeMallShoppingCartService.getMyShoppingCartItems(loginMallUser.getUserId()));
	}

	@PostMapping("")
	@ApiOperation(value = "添加商品到购物车接口", notes = "传参为商品id、数量")
	public Result saveNewBeeMallShoppingCartItem(@RequestBody SaveCartItemParam saveCartItemParam,
												 @TokenToMallUser MallUserToken loginMallUser) {
		String saveResult = newBeeMallShoppingCartService.saveNewBeeMallCartItem(saveCartItemParam, loginMallUser.getUserId());
		//添加成功
		if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
			return ResultGenerator.genSuccessResult();
		}
		//添加失败
		return ResultGenerator.genFailResult(saveResult);
	}

	@PutMapping("")
	@ApiOperation(value = "修改购物项数据", notes = "传参为购物项id、数量")
	public Result updateNewBeeMallShoppingCartItem(@RequestBody UpdateCartItemParam updateCartItemParam,
												   @TokenToMallUser MallUserToken loginMallUser) {
		String updateResult = newBeeMallShoppingCartService.updateNewBeeMallCartItem(updateCartItemParam, loginMallUser.getUserId());
		//修改成功
		if (ServiceResultEnum.SUCCESS.getResult().equals(updateResult)) {
			return ResultGenerator.genSuccessResult();
		}
		//修改失败
		return ResultGenerator.genFailResult(updateResult);
	}

	@DeleteMapping("/{newBeeMallShoppingCartItemId}")
	@ApiOperation(value = "删除购物项", notes = "传参为购物项id")
	public Result updateNewBeeMallShoppingCartItem(@PathVariable("newBeeMallShoppingCartItemId") Long newBeeMallShoppingCartItemId,
												   @TokenToMallUser MallUserToken loginMallUser) {
		NewBeeMallShoppingCartItem newBeeMallCartItemById = newBeeMallShoppingCartService.getNewBeeMallCartItemById(newBeeMallShoppingCartItemId);
		if (!loginMallUser.getUserId().equals(newBeeMallCartItemById.getUserId())) {
			return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
		}
		Boolean deleteResult = newBeeMallShoppingCartService.deleteById(newBeeMallShoppingCartItemId, loginMallUser.getUserId());
		//删除成功
		if (deleteResult) {
			return ResultGenerator.genSuccessResult();
		}
		//删除失败
		return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
	}

	@GetMapping("/settle")
	@ApiOperation(value = "根据购物项id数组查询购物项明细", notes = "确认订单页面使用")
	public Result<List<NewBeeMallShoppingCartItemVO>> toSettle(Long[] cartItemIds, @TokenToMallUser MallUserToken loginMallUser) {
		if (cartItemIds.length < 1) {
			NewBeeMallException.fail("参数异常");
		}
		int priceTotal = 0;
		List<NewBeeMallShoppingCartItemVO> itemsForSettle = newBeeMallShoppingCartService.getCartItemsForSettle(Arrays.asList(cartItemIds), loginMallUser.getUserId());
		if (CollectionUtils.isEmpty(itemsForSettle)) {
			//无数据则抛出异常
			NewBeeMallException.fail("参数异常");
		} else {
			//总价
			for (NewBeeMallShoppingCartItemVO newBeeMallShoppingCartItemVO : itemsForSettle) {
				priceTotal += newBeeMallShoppingCartItemVO.getGoodsCount() * newBeeMallShoppingCartItemVO.getSellingPrice();
			}
			if (priceTotal < 1) {
				NewBeeMallException.fail("价格异常");
			}
		}
		return ResultGenerator.genSuccessResult(itemsForSettle);
	}

	@GetMapping("/listByCartItemIds")
	@ApiOperation(value = "购物车列表", notes = "")
	public Result<List<NewBeeMallShoppingCartItemDTO>> cartItemListByIds(@RequestParam("cartItemIds") List<Long> cartItemIds) {
		if (CollectionUtils.isEmpty(cartItemIds)) {
			return ResultGenerator.genFailResult("error param");
		}
		return ResultGenerator.genSuccessResult(newBeeMallShoppingCartService.getCartItemsByCartIds(cartItemIds));
	}

	@DeleteMapping("/deleteByCartItemIds")
	@ApiOperation(value = "批量删除购物项", notes = "")
	public Result<Boolean> deleteByCartItemIds(@RequestParam("cartItemIds") List<Long> cartItemIds) {
		if (CollectionUtils.isEmpty(cartItemIds)) {
			return ResultGenerator.genFailResult("error param");
		}
		return ResultGenerator.genSuccessResult(newBeeMallShoppingCartService.deleteCartItemsByCartIds(cartItemIds) > 0);
	}
}
