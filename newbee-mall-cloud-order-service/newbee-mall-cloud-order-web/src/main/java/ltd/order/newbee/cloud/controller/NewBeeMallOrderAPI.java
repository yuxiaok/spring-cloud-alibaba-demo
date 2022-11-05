/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.order.newbee.cloud.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ltd.common.newbee.cloud.common.Constants;
import ltd.common.newbee.cloud.common.ServiceResultEnum;
import ltd.common.newbee.cloud.dto.PageQueryUtil;
import ltd.common.newbee.cloud.dto.PageResult;
import ltd.common.newbee.cloud.dto.Result;
import ltd.common.newbee.cloud.dto.ResultGenerator;
import ltd.common.newbee.cloud.exception.NewBeeMallException;
import ltd.common.newbee.cloud.pojo.MallUserToken;
import ltd.order.newbee.cloud.config.annotation.TokenToMallUser;
import ltd.order.newbee.cloud.controller.param.SaveOrderParam;
import ltd.order.newbee.cloud.controller.vo.NewBeeMallOrderDetailVO;
import ltd.order.newbee.cloud.controller.vo.NewBeeMallOrderListVO;
import ltd.order.newbee.cloud.entity.MallUserAddress;
import ltd.order.newbee.cloud.service.NewBeeMallOrderService;
import ltd.order.newbee.cloud.service.NewBeeMallUserAddressService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "v1", tags = "7.新蜂商城订单操作相关接口")
@RequestMapping("/orders/mall")
public class NewBeeMallOrderAPI {

	@Resource
	private NewBeeMallOrderService newBeeMallOrderService;
	@Resource
	private NewBeeMallUserAddressService newBeeMallUserAddressService;

	@PostMapping("/saveOrder")
	@ApiOperation(value = "生成订单接口", notes = "传参为地址id和待结算的购物项id数组")
	public Result<String> saveOrder(@ApiParam(value = "订单参数") @RequestBody SaveOrderParam saveOrderParam,
									@TokenToMallUser MallUserToken loginMallUser) {
		if (saveOrderParam == null || saveOrderParam.getCartItemIds() == null || saveOrderParam.getAddressId() == null) {
			NewBeeMallException.fail(ServiceResultEnum.PARAM_ERROR.getResult());
		}
		if (saveOrderParam.getCartItemIds().length < 1) {
			NewBeeMallException.fail(ServiceResultEnum.PARAM_ERROR.getResult());
		}
		MallUserAddress address = newBeeMallUserAddressService.getMallUserAddressById(saveOrderParam.getAddressId());
		if (!loginMallUser.getUserId().equals(address.getUserId())) {
			return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
		}
		//保存订单并返回订单号
		String saveOrderResult = newBeeMallOrderService.saveOrder(loginMallUser.getUserId(), address, Arrays.asList(saveOrderParam.getCartItemIds()));
		Result result = ResultGenerator.genSuccessResult();
		result.setData(saveOrderResult);
		return result;
	}

	@GetMapping("/{orderNo}")
	@ApiOperation(value = "订单详情接口", notes = "传参为订单号")
	public Result<NewBeeMallOrderDetailVO> orderDetailPage(@ApiParam(value = "订单号") @PathVariable("orderNo") String orderNo,
														   @TokenToMallUser MallUserToken loginMallUser) {
		return ResultGenerator.genSuccessResult(newBeeMallOrderService.getOrderDetailByOrderNo(orderNo, loginMallUser.getUserId()));
	}

	@GetMapping("")
	@ApiOperation(value = "订单列表接口", notes = "传参为页码")
	public Result<PageResult<List<NewBeeMallOrderListVO>>> orderList(@ApiParam(value = "页码") @RequestParam(required = false) Integer pageNumber,
																	 @ApiParam(value = "订单状态:0.待支付 1.待确认 2.待发货 3:已发货 4.交易成功") @RequestParam(required = false) Integer status,
																	 @TokenToMallUser MallUserToken loginMallUser) {
		Map params = new HashMap(8);
		if (pageNumber == null || pageNumber < 1) {
			pageNumber = 1;
		}
		params.put("userId", loginMallUser.getUserId());
		params.put("orderStatus", status);
		params.put("page", pageNumber);
		params.put("limit", Constants.ORDER_SEARCH_PAGE_LIMIT);
		//封装分页请求参数
		PageQueryUtil pageUtil = new PageQueryUtil(params);
		return ResultGenerator.genSuccessResult(newBeeMallOrderService.getMyOrders(pageUtil));
	}

	@PutMapping("/{orderNo}/cancel")
	@ApiOperation(value = "订单取消接口", notes = "传参为订单号")
	public Result cancelOrder(@ApiParam(value = "订单号") @PathVariable("orderNo") String orderNo, @TokenToMallUser MallUserToken loginMallUser) {
		String cancelOrderResult = newBeeMallOrderService.cancelOrder(orderNo, loginMallUser.getUserId());
		if (ServiceResultEnum.SUCCESS.getResult().equals(cancelOrderResult)) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult(cancelOrderResult);
		}
	}

	@PutMapping("/{orderNo}/finish")
	@ApiOperation(value = "确认收货接口", notes = "传参为订单号")
	public Result finishOrder(@ApiParam(value = "订单号") @PathVariable("orderNo") String orderNo, @TokenToMallUser MallUserToken loginMallUser) {
		String finishOrderResult = newBeeMallOrderService.finishOrder(orderNo, loginMallUser.getUserId());
		if (ServiceResultEnum.SUCCESS.getResult().equals(finishOrderResult)) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult(finishOrderResult);
		}
	}

	@GetMapping("/paySuccess")
	@ApiOperation(value = "模拟支付成功回调的接口", notes = "传参为订单号和支付方式")
	public Result paySuccess(@ApiParam(value = "订单号") @RequestParam("orderNo") String orderNo, @ApiParam(value = "支付方式") @RequestParam("payType") int payType) {
		String payResult = newBeeMallOrderService.paySuccess(orderNo, payType);
		if (ServiceResultEnum.SUCCESS.getResult().equals(payResult)) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult(payResult);
		}
	}

}
