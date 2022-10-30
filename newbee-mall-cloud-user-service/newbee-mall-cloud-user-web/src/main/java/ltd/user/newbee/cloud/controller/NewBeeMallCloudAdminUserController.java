package ltd.user.newbee.cloud.controller;

import ltd.common.newbee.cloud.dto.Result;
import ltd.common.newbee.cloud.dto.ResultGenerator;
import ltd.user.newbee.cloud.common.Constants;
import ltd.user.newbee.cloud.common.ServiceResultEnum;
import ltd.user.newbee.cloud.config.annotation.TokenToAdminUser;
import ltd.user.newbee.cloud.controller.param.AdminLoginParam;
import ltd.user.newbee.cloud.controller.param.UpdateAdminNameParam;
import ltd.user.newbee.cloud.controller.param.UpdateAdminPasswordParam;
import ltd.user.newbee.cloud.entity.AdminUser;
import ltd.user.newbee.cloud.entity.AdminUserToken;
import ltd.user.newbee.cloud.service.AdminUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : TODO </li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年10⽉30⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
@RestController
public class NewBeeMallCloudAdminUserController {

	@GetMapping("/users/admin/test/{userId}")
	public String test(@PathVariable("userId") int userId) {
		String userName = "user:" + userId;
		// 返回信息给调用端
		return userName;
	}

	@Resource
	private AdminUserService adminUserService;

	private static final Logger logger = LoggerFactory.getLogger(NewBeeMallCloudAdminUserController.class);

	@RequestMapping(value = "/adminUser/login", method = RequestMethod.POST)
	public Result<String> login(@RequestBody @Valid AdminLoginParam adminLoginParam) {
		String loginResult = adminUserService.login(adminLoginParam.getUserName(), adminLoginParam.getPasswordMd5());
		logger.info("manage login api,adminName={},loginResult={}", adminLoginParam.getUserName(), loginResult);

		//登录成功
		if (!StringUtils.isEmpty(loginResult) && loginResult.length() == Constants.TOKEN_LENGTH) {
			Result result = ResultGenerator.genSuccessResult();
			result.setData(loginResult);
			return result;
		}
		//登录失败
		return ResultGenerator.genFailResult(loginResult);
	}

	@RequestMapping(value = "/adminUser/profile", method = RequestMethod.GET)
	public Result profile(@TokenToAdminUser AdminUserToken adminUser) {
		logger.info("adminUser:{}", adminUser.toString());
		AdminUser adminUserEntity = adminUserService.getUserDetailById(adminUser.getAdminUserId());
		if (adminUserEntity != null) {
			adminUserEntity.setLoginPassword("******");
			Result result = ResultGenerator.genSuccessResult();
			result.setData(adminUserEntity);
			return result;
		}
		return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
	}

	@RequestMapping(value = "/adminUser/password", method = RequestMethod.PUT)
	public Result passwordUpdate(@RequestBody @Valid UpdateAdminPasswordParam adminPasswordParam, @TokenToAdminUser AdminUserToken adminUser) {
		logger.info("adminUser:{}", adminUser.toString());
		if (adminUserService.updatePassword(adminUser.getAdminUserId(), adminPasswordParam.getOriginalPassword(), adminPasswordParam.getNewPassword())) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult(ServiceResultEnum.DB_ERROR.getResult());
		}
	}

	@RequestMapping(value = "/adminUser/name", method = RequestMethod.PUT)
	public Result nameUpdate(@RequestBody @Valid UpdateAdminNameParam adminNameParam, @TokenToAdminUser AdminUserToken adminUser) {
		logger.info("adminUser:{}", adminUser.toString());
		if (adminUserService.updateName(adminUser.getAdminUserId(), adminNameParam.getLoginUserName(), adminNameParam.getNickName())) {
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult(ServiceResultEnum.DB_ERROR.getResult());
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.DELETE)
	public Result logout(@TokenToAdminUser AdminUserToken adminUser) {
		logger.info("adminUser:{}", adminUser.toString());
		adminUserService.logout(adminUser.getAdminUserId());
		return ResultGenerator.genSuccessResult();
	}
}