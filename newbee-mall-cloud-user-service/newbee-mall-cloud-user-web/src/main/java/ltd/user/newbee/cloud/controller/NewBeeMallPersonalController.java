/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.user.newbee.cloud.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ltd.common.newbee.cloud.common.Constants;
import ltd.common.newbee.cloud.common.ServiceResultEnum;
import ltd.common.newbee.cloud.dto.Result;
import ltd.common.newbee.cloud.dto.ResultGenerator;
import ltd.common.newbee.cloud.pojo.MallUserDTO;
import ltd.common.newbee.cloud.pojo.MallUserToken;
import ltd.common.newbee.cloud.util.BeanUtil;
import ltd.common.newbee.cloud.util.NumberUtil;
import ltd.user.newbee.cloud.config.annotation.TokenToMallUser;
import ltd.user.newbee.cloud.controller.param.MallUserLoginParam;
import ltd.user.newbee.cloud.controller.param.MallUserRegisterParam;
import ltd.user.newbee.cloud.controller.param.MallUserUpdateParam;
import ltd.user.newbee.cloud.controller.vo.NewBeeMallUserVO;
import ltd.user.newbee.cloud.entity.MallUser;
import ltd.user.newbee.cloud.service.NewBeeMallUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@Api(value = "v1", tags = "2.新蜂商城用户操作相关接口")
@RequestMapping("/users/mall")
public class NewBeeMallPersonalController {

	@Resource
	private NewBeeMallUserService newBeeMallUserService;

	@Autowired
	private RedisTemplate redisTemplate;

	private static final Logger logger = LoggerFactory.getLogger(NewBeeMallPersonalController.class);

	@PostMapping("/login")
	@ApiOperation(value = "登录接口", notes = "返回token")
	public Result<String> login(@RequestBody @Valid MallUserLoginParam mallUserLoginParam) {
		if (!NumberUtil.isPhone(mallUserLoginParam.getLoginName())) {
			return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
		}
		String loginResult = newBeeMallUserService.login(mallUserLoginParam.getLoginName(), mallUserLoginParam.getPasswordMd5());

		logger.info("login api,loginName={},loginResult={}", mallUserLoginParam.getLoginName(), loginResult);

		//登录成功
		if (!StringUtils.isEmpty(loginResult) && loginResult.length() == Constants.TOKEN_LENGTH) {
			Result result = ResultGenerator.genSuccessResult();
			result.setData(loginResult);
			return result;
		}
		//登录失败
		return ResultGenerator.genFailResult(loginResult);
	}


	@PostMapping("/logout")
	@ApiOperation(value = "登出接口", notes = "清除token")
	public Result<String> logout(@TokenToMallUser MallUserToken mallUserToken) {
		Boolean logoutResult = newBeeMallUserService.logout(mallUserToken.getToken());

		logger.info("logout api,loginMallUser={}", mallUserToken.getUserId());

		//登出成功
		if (logoutResult) {
			return ResultGenerator.genSuccessResult();
		}
		//登出失败
		return ResultGenerator.genFailResult("logout error");
	}


	@PostMapping("/register")
	@ApiOperation(value = "用户注册", notes = "")
	public Result register(@RequestBody @Valid MallUserRegisterParam mallUserRegisterParam) {
		if (!NumberUtil.isPhone(mallUserRegisterParam.getLoginName())) {
			return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
		}
		String registerResult = newBeeMallUserService.register(mallUserRegisterParam.getLoginName(), mallUserRegisterParam.getPassword());

		logger.info("register api,loginName={},loginResult={}", mallUserRegisterParam.getLoginName(), registerResult);

		//注册成功
		if (ServiceResultEnum.SUCCESS.getResult().equals(registerResult)) {
			return ResultGenerator.genSuccessResult();
		}
		//注册失败
		return ResultGenerator.genFailResult(registerResult);
	}

	@PutMapping("/info")
	@ApiOperation(value = "修改用户信息", notes = "")
	public Result updateInfo(@RequestBody @ApiParam("用户信息") MallUserUpdateParam mallUserUpdateParam, @TokenToMallUser MallUserToken loginMallUser) {
		Boolean flag = newBeeMallUserService.updateUserInfo(mallUserUpdateParam, loginMallUser.getUserId());
		if (flag) {
			//返回成功
			Result result = ResultGenerator.genSuccessResult();
			return result;
		} else {
			//返回失败
			Result result = ResultGenerator.genFailResult("修改失败");
			return result;
		}
	}

	@GetMapping("/info")
	@ApiOperation(value = "获取用户信息", notes = "")
	public Result<NewBeeMallUserVO> getUserDetail(@TokenToMallUser MallUserToken loginMallUser) {
		//已登录则直接返回
		NewBeeMallUserVO mallUserVO = new NewBeeMallUserVO();
		MallUser mallUser = newBeeMallUserService.getByUserId(loginMallUser.getUserId());
		BeanUtil.copyProperties(mallUser, mallUserVO);
		return ResultGenerator.genSuccessResult(mallUserVO);
	}

	@GetMapping(value = "/getDetailByToken")
	public Result<MallUserDTO> getMallUserByToken(@RequestParam(value = "token") String token) {
		ValueOperations<String, MallUserToken> valueOperations = redisTemplate.opsForValue();
		MallUserToken mallUserToken = valueOperations.get(token);
		MallUser mallUser = newBeeMallUserService.getByUserId(mallUserToken.getUserId());
		MallUserDTO mallUserDTO = new MallUserDTO();
		BeanUtil.copyProperties(mallUser, mallUserDTO);
		return ResultGenerator.genSuccessResult(mallUserDTO);
	}
}
