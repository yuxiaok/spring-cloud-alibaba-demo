/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.user.newbee.cloud.config.handler;

import ltd.common.newbee.cloud.exception.NewBeeMallException;
import ltd.common.newbee.cloud.pojo.AdminUserToken;
import ltd.user.newbee.cloud.common.Constants;
import ltd.user.newbee.cloud.common.ServiceResultEnum;
import ltd.user.newbee.cloud.config.annotation.TokenToAdminUser;
import ltd.user.newbee.cloud.dao.NewBeeAdminUserTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class TokenToAdminUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	private NewBeeAdminUserTokenMapper newBeeAdminUserTokenMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	public TokenToAdminUserMethodArgumentResolver() {
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (parameter.hasParameterAnnotation(TokenToAdminUser.class)) {
			return true;
		}
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		if (parameter.getParameterAnnotation(TokenToAdminUser.class) instanceof TokenToAdminUser) {
			String token = webRequest.getHeader("token");
			if (null != token && !"".equals(token) && token.length() == Constants.TOKEN_LENGTH) {
//				AdminUserToken adminUserToken = newBeeAdminUserTokenMapper.selectByToken(token);
//				if (adminUserToken == null) {
//					NewBeeMallException.fail(ServiceResultEnum.ADMIN_NOT_LOGIN_ERROR.getResult());
//				} else if (adminUserToken.getExpireTime().getTime() <= System.currentTimeMillis()) {
//					NewBeeMallException.fail(ServiceResultEnum.ADMIN_TOKEN_EXPIRE_ERROR.getResult());
//				}
				ValueOperations<String, AdminUserToken> valueOperations = redisTemplate.opsForValue();
				AdminUserToken adminUserToken = valueOperations.get(token);
				if (adminUserToken == null) {
					NewBeeMallException.fail("ADMIN_NOT_LOGIN_ERROR");
				}
				return adminUserToken;
			} else {
				NewBeeMallException.fail(ServiceResultEnum.ADMIN_NOT_LOGIN_ERROR.getResult());
			}
		}
		return null;
	}

}
