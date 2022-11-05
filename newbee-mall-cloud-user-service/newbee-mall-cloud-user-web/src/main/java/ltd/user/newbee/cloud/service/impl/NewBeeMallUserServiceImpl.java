/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.user.newbee.cloud.service.impl;

import ltd.common.newbee.cloud.common.Constants;
import ltd.common.newbee.cloud.common.ServiceResultEnum;
import ltd.common.newbee.cloud.dto.PageQueryUtil;
import ltd.common.newbee.cloud.dto.PageResult;
import ltd.common.newbee.cloud.exception.NewBeeMallException;
import ltd.common.newbee.cloud.pojo.MallUserToken;
import ltd.common.newbee.cloud.util.MD5Util;
import ltd.common.newbee.cloud.util.NumberUtil;
import ltd.common.newbee.cloud.util.SystemUtil;
import ltd.user.newbee.cloud.controller.param.MallUserUpdateParam;
import ltd.user.newbee.cloud.dao.MallUserMapper;
import ltd.user.newbee.cloud.dao.NewBeeMallUserTokenMapper;
import ltd.user.newbee.cloud.entity.MallUser;
import ltd.user.newbee.cloud.service.NewBeeMallUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class NewBeeMallUserServiceImpl implements NewBeeMallUserService {

	@Autowired
	private MallUserMapper mallUserMapper;
	@Autowired
	private NewBeeMallUserTokenMapper newBeeMallUserTokenMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public String register(String loginName, String password) {
		if (mallUserMapper.selectByLoginName(loginName) != null) {
			return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
		}
		MallUser registerUser = new MallUser();
		registerUser.setLoginName(loginName);
		registerUser.setNickName(loginName);
		registerUser.setIntroduceSign(Constants.USER_INTRO);
		String passwordMD5 = MD5Util.MD5Encode(password, "UTF-8");
		registerUser.setPasswordMd5(passwordMD5);
		if (mallUserMapper.insertSelective(registerUser) > 0) {
			return ServiceResultEnum.SUCCESS.getResult();
		}
		return ServiceResultEnum.DB_ERROR.getResult();
	}

	@Override
	public String login(String loginName, String passwordMD5) {
		MallUser user = mallUserMapper.selectByLoginNameAndPasswd(loginName, passwordMD5);
		if (user != null) {
			if (user.getLockedFlag() == 1) {
				return ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult();
			}
			//登录后即执行修改token的操作
			String token = getNewToken(System.currentTimeMillis() + "", user.getUserId());
			MallUserToken mallUserToken = new MallUserToken();
			mallUserToken.setUserId(user.getUserId());
			mallUserToken.setToken(token);
			ValueOperations<String, MallUserToken> valueOperations = redisTemplate.opsForValue();
			valueOperations.set(token, mallUserToken, 7 * 24 * 60 * 60, TimeUnit.SECONDS);
//			MallUserToken mallUserToken = newBeeMallUserTokenMapper.selectByPrimaryKey(user.getUserId());
//			//当前时间
//			Date now = new Date();
//			//过期时间
//			Date expireTime = new Date(now.getTime() + 2 * 24 * 3600 * 1000);//过期时间 48 小时
//			if (mallUserToken == null) {
//				mallUserToken = new MallUserToken();
//				mallUserToken.setUserId(user.getUserId());
//				mallUserToken.setToken(token);
//				mallUserToken.setUpdateTime(now);
//				mallUserToken.setExpireTime(expireTime);
//				//新增一条token数据
//				if (newBeeMallUserTokenMapper.insertSelective(mallUserToken) > 0) {
//					//新增成功后返回
//					return token;
//				}
//			} else {
//				mallUserToken.setToken(token);
//				mallUserToken.setUpdateTime(now);
//				mallUserToken.setExpireTime(expireTime);
//				//更新
//				if (newBeeMallUserTokenMapper.updateByPrimaryKeySelective(mallUserToken) > 0) {
//					//修改成功后返回
//					return token;
//				}
//			}
			return token;
		}
		return ServiceResultEnum.LOGIN_ERROR.getResult();
	}

	/**
	 * 获取token值
	 *
	 * @param timeStr
	 * @param userId
	 * @return
	 */
	private String getNewToken(String timeStr, Long userId) {
		String src = timeStr + userId + NumberUtil.genRandomNum(4);
		return SystemUtil.genToken(src);
	}

	@Override
	public Boolean updateUserInfo(MallUserUpdateParam mallUser, Long userId) {
		MallUser user = mallUserMapper.selectByPrimaryKey(userId);
		if (user == null) {
			NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
		}
		user.setNickName(mallUser.getNickName());
		//user.setPasswordMd5(mallUser.getPasswordMd5());
		//若密码为空字符，则表明用户不打算修改密码，使用原密码保存
		if (!MD5Util.MD5Encode("", "UTF-8").equals(mallUser.getPasswordMd5())) {
			user.setPasswordMd5(mallUser.getPasswordMd5());
		}
		user.setIntroduceSign(mallUser.getIntroduceSign());
		if (mallUserMapper.updateByPrimaryKeySelective(user) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean logout(String token) {
		redisTemplate.delete(token);
		return true;
//		return newBeeMallUserTokenMapper.deleteByPrimaryKey(userId) > 0;
	}

	@Override
	public PageResult getNewBeeMallUsersPage(PageQueryUtil pageUtil) {
		List<MallUser> mallUsers = mallUserMapper.findMallUserList(pageUtil);
		int total = mallUserMapper.getTotalMallUsers(pageUtil);
		PageResult pageResult = new PageResult(mallUsers, total, pageUtil.getLimit(), pageUtil.getPage());
		return pageResult;
	}

	@Override
	public Boolean lockUsers(Long[] ids, int lockStatus) {
		if (ids.length < 1) {
			return false;
		}
		return mallUserMapper.lockUserBatch(ids, lockStatus) > 0;
	}

	@Override
	public MallUser getByUserId(Long userId) {
		return mallUserMapper.selectByPrimaryKey(userId);
	}
}
