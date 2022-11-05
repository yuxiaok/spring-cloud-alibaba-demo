/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.user.newbee.cloud.config.handler;

import ltd.common.newbee.cloud.common.Constants;
import ltd.common.newbee.cloud.common.ServiceResultEnum;
import ltd.common.newbee.cloud.exception.NewBeeMallException;
import ltd.common.newbee.cloud.pojo.MallUserToken;
import ltd.user.newbee.cloud.config.annotation.TokenToMallUser;
import ltd.user.newbee.cloud.dao.MallUserMapper;
import ltd.user.newbee.cloud.dao.NewBeeMallUserTokenMapper;
import ltd.user.newbee.cloud.entity.MallUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class TokenToMallUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	private MallUserMapper mallUserMapper;

	@Autowired
	private NewBeeMallUserTokenMapper newBeeMallUserTokenMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	public TokenToMallUserMethodArgumentResolver() {
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (parameter.hasParameterAnnotation(TokenToMallUser.class)) {
			return true;
		}
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		if (parameter.getParameterAnnotation(TokenToMallUser.class) instanceof TokenToMallUser) {
			MallUser mallUser = null;
			String token = webRequest.getHeader("token");
			if (null != token && !"".equals(token) && token.length() == Constants.TOKEN_LENGTH) {
				ValueOperations<String, MallUserToken> valueOperations = redisTemplate.opsForValue();
				MallUserToken mallUserToken = valueOperations.get(token);
				if (mallUserToken == null) {
					NewBeeMallException.fail(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
				}
				mallUser = mallUserMapper.selectByPrimaryKey(mallUserToken.getUserId());
				if (mallUser == null) {
					NewBeeMallException.fail(ServiceResultEnum.USER_NULL_ERROR.getResult());
				}
				if (mallUser.getLockedFlag().intValue() == 1) {
					NewBeeMallException.fail(ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult());
				}
				return mallUserToken;
			} else {
				NewBeeMallException.fail(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
			}
		}
		return null;
	}

	public static byte[] getRequestPostBytes(HttpServletRequest request)
			throws IOException {
		int contentLength = request.getContentLength();
		if (contentLength < 0) {
			return null;
		}
		byte buffer[] = new byte[contentLength];
		for (int i = 0; i < contentLength; ) {
			int readlen = request.getInputStream().read(buffer, i,
					contentLength - i);
			if (readlen == -1) {
				break;
			}
			i += readlen;
		}
		return buffer;
	}

}
