/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.user.newbee.cloud.service.impl;

import ltd.common.newbee.cloud.common.ServiceResultEnum;
import ltd.common.newbee.cloud.pojo.AdminUserToken;
import ltd.common.newbee.cloud.util.NumberUtil;
import ltd.common.newbee.cloud.util.SystemUtil;
import ltd.user.newbee.cloud.dao.AdminUserMapper;
import ltd.user.newbee.cloud.dao.NewBeeAdminUserTokenMapper;
import ltd.user.newbee.cloud.entity.AdminUser;
import ltd.user.newbee.cloud.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AdminUserServiceImpl implements AdminUserService {

	@Resource
	private AdminUserMapper adminUserMapper;

	@Resource
	private NewBeeAdminUserTokenMapper newBeeAdminUserTokenMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public String login(String userName, String password) {
		AdminUser loginAdminUser = adminUserMapper.login(userName, password);
		if (loginAdminUser != null) {
			//登录后即执行修改token的操作
			String token = getNewToken(System.currentTimeMillis() + "", loginAdminUser.getAdminUserId());
			AdminUserToken adminUserToken = new AdminUserToken();
			adminUserToken.setAdminUserId(loginAdminUser.getAdminUserId());
			adminUserToken.setToken(token);
			ValueOperations<String, AdminUserToken> valueOperations = redisTemplate.opsForValue();
			valueOperations.set(token, adminUserToken, 2 * 24 * 60 * 60, TimeUnit.SECONDS);
			return token;
//					AdminUserToken adminUserToken = newBeeAdminUserTokenMapper.selectByPrimaryKey(loginAdminUser.getAdminUserId());
//			//当前时间
//			Date now = new Date();
//			//过期时间
//			Date expireTime = new Date(now.getTime() + 2 * 24 * 3600 * 1000);//过期时间 48 小时
//			if (adminUserToken == null) {
//				adminUserToken = new AdminUserToken();
//				adminUserToken.setAdminUserId(loginAdminUser.getAdminUserId());
//				adminUserToken.setToken(token);
//				adminUserToken.setUpdateTime(now);
//				adminUserToken.setExpireTime(expireTime);
//				//新增一条token数据
//				if (newBeeAdminUserTokenMapper.insertSelective(adminUserToken) > 0) {
//					//新增成功后返回
//					return token;
//				}
//			} else {
//				adminUserToken.setToken(token);
//				adminUserToken.setUpdateTime(now);
//				adminUserToken.setExpireTime(expireTime);
//				//更新
//				if (newBeeAdminUserTokenMapper.updateByPrimaryKeySelective(adminUserToken) > 0) {
//					//修改成功后返回
//					return token;
//				}
//			}

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
		String src = timeStr + userId + NumberUtil.genRandomNum(6);
		return SystemUtil.genToken(src);
	}


	@Override
	public AdminUser getUserDetailById(Long loginUserId) {
		return adminUserMapper.selectByPrimaryKey(loginUserId);
	}

	@Override
	public Boolean updatePassword(Long loginUserId, String originalPassword, String newPassword) {
		AdminUser adminUser = adminUserMapper.selectByPrimaryKey(loginUserId);
		//当前用户非空才可以进行更改
		if (adminUser != null) {
			//比较原密码是否正确
			if (originalPassword.equals(adminUser.getLoginPassword())) {
				//设置新密码并修改
				adminUser.setLoginPassword(newPassword);
				if (adminUserMapper.updateByPrimaryKeySelective(adminUser) > 0 && newBeeAdminUserTokenMapper.deleteByPrimaryKey(loginUserId) > 0) {
					//修改成功且清空当前token则返回true
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Boolean updateName(Long loginUserId, String loginUserName, String nickName) {
		AdminUser adminUser = adminUserMapper.selectByPrimaryKey(loginUserId);
		//当前用户非空才可以进行更改
		if (adminUser != null) {
			//设置新名称并修改
			adminUser.setLoginUserName(loginUserName);
			adminUser.setNickName(nickName);
			if (adminUserMapper.updateByPrimaryKeySelective(adminUser) > 0) {
				//修改成功则返回true
				return true;
			}
		}
		return false;
	}

	@Override
	public Boolean logout(Long adminUserId) {
		return newBeeAdminUserTokenMapper.deleteByPrimaryKey(adminUserId) > 0;
	}

	@Override
	public Map<String, Object> getAdminUserByToken(String token) {
		ValueOperations<String, AdminUserToken> valueOperations = redisTemplate.opsForValue();
		AdminUserToken adminUserToken = valueOperations.get(token);
		AdminUser adminUser = adminUserMapper.selectByPrimaryKey(adminUserToken.getAdminUserId());
		Map<String, Object> map = new HashMap<>();
		map.put("adminUserId", adminUser.getAdminUserId());
		map.put("loginUserName", adminUser.getLoginUserName());
		map.put("nickName", adminUser.getNickName());
		map.put("locked", adminUser.getLocked());
		return map;
	}
}
