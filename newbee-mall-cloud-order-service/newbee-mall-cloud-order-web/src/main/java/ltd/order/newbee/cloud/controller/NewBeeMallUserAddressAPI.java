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
import ltd.common.newbee.cloud.common.ServiceResultEnum;
import ltd.common.newbee.cloud.dto.Result;
import ltd.common.newbee.cloud.dto.ResultGenerator;
import ltd.common.newbee.cloud.pojo.MallUserToken;
import ltd.common.newbee.cloud.util.BeanUtil;
import ltd.order.newbee.cloud.config.annotation.TokenToMallUser;
import ltd.order.newbee.cloud.controller.param.SaveMallUserAddressParam;
import ltd.order.newbee.cloud.controller.param.UpdateMallUserAddressParam;
import ltd.order.newbee.cloud.controller.vo.NewBeeMallUserAddressVO;
import ltd.order.newbee.cloud.entity.MallUserAddress;
import ltd.order.newbee.cloud.service.NewBeeMallUserAddressService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "6.新蜂商城个人地址相关接口")
@RequestMapping("/mall/address")
public class NewBeeMallUserAddressAPI {

	@Resource
	private NewBeeMallUserAddressService mallUserAddressService;

	@GetMapping("")
	@ApiOperation(value = "我的收货地址列表", notes = "")
	public Result<List<NewBeeMallUserAddressVO>> addressList(@TokenToMallUser MallUserToken loginMallUser) {
		return ResultGenerator.genSuccessResult(mallUserAddressService.getMyAddresses(loginMallUser.getUserId()));
	}

	@PostMapping("")
	@ApiOperation(value = "添加地址", notes = "")
	public Result<Boolean> saveUserAddress(@RequestBody SaveMallUserAddressParam saveMallUserAddressParam,
										   @TokenToMallUser MallUserToken loginMallUser) {
		MallUserAddress userAddress = new MallUserAddress();
		BeanUtil.copyProperties(saveMallUserAddressParam, userAddress);
		userAddress.setUserId(loginMallUser.getUserId());
		Boolean saveResult = mallUserAddressService.saveUserAddress(userAddress);
		//添加成功
		if (saveResult) {
			return ResultGenerator.genSuccessResult();
		}
		//添加失败
		return ResultGenerator.genFailResult("添加失败");
	}

	@PutMapping("")
	@ApiOperation(value = "修改地址", notes = "")
	public Result<Boolean> updateMallUserAddress(@RequestBody UpdateMallUserAddressParam updateMallUserAddressParam,
												 @TokenToMallUser MallUserToken loginMallUser) {
		MallUserAddress mallUserAddressById = mallUserAddressService.getMallUserAddressById(updateMallUserAddressParam.getAddressId());
		if (!loginMallUser.getUserId().equals(mallUserAddressById.getUserId())) {
			return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
		}
		MallUserAddress userAddress = new MallUserAddress();
		BeanUtil.copyProperties(updateMallUserAddressParam, userAddress);
		userAddress.setUserId(loginMallUser.getUserId());
		Boolean updateResult = mallUserAddressService.updateMallUserAddress(userAddress);
		//修改成功
		if (updateResult) {
			return ResultGenerator.genSuccessResult();
		}
		//修改失败
		return ResultGenerator.genFailResult("修改失败");
	}

	@GetMapping("/{addressId}")
	@ApiOperation(value = "获取收货地址详情", notes = "传参为地址id")
	public Result<NewBeeMallUserAddressVO> getMallUserAddress(@PathVariable("addressId") Long addressId,
															  @TokenToMallUser MallUserToken loginMallUser) {
		MallUserAddress mallUserAddressById = mallUserAddressService.getMallUserAddressById(addressId);
		NewBeeMallUserAddressVO newBeeMallUserAddressVO = new NewBeeMallUserAddressVO();
		BeanUtil.copyProperties(mallUserAddressById, newBeeMallUserAddressVO);
		if (!loginMallUser.getUserId().equals(mallUserAddressById.getUserId())) {
			return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
		}
		return ResultGenerator.genSuccessResult(newBeeMallUserAddressVO);
	}

	@GetMapping("/default")
	@ApiOperation(value = "获取默认收货地址", notes = "无传参")
	public Result getDefaultMallUserAddress(@TokenToMallUser MallUserToken loginMallUser) {
		MallUserAddress mallUserAddressById = mallUserAddressService.getMyDefaultAddressByUserId(loginMallUser.getUserId());
		return ResultGenerator.genSuccessResult(mallUserAddressById);
	}

	@DeleteMapping("/{addressId}")
	@ApiOperation(value = "删除收货地址", notes = "传参为地址id")
	public Result deleteAddress(@PathVariable("addressId") Long addressId,
								@TokenToMallUser MallUserToken loginMallUser) {
		MallUserAddress mallUserAddressById = mallUserAddressService.getMallUserAddressById(addressId);
		if (!loginMallUser.getUserId().equals(mallUserAddressById.getUserId())) {
			return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
		}
		Boolean deleteResult = mallUserAddressService.deleteById(addressId);
		//删除成功
		if (deleteResult) {
			return ResultGenerator.genSuccessResult();
		}
		//删除失败
		return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
	}
}
