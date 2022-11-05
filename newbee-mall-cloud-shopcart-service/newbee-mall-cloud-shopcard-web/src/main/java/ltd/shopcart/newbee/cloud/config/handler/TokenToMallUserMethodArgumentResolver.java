/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.shopcart.newbee.cloud.config.handler;

import ltd.common.newbee.cloud.common.Constants;
import ltd.common.newbee.cloud.common.ServiceResultEnum;
import ltd.common.newbee.cloud.dto.Result;
import ltd.common.newbee.cloud.exception.NewBeeMallException;
import ltd.common.newbee.cloud.pojo.MallUserDTO;
import ltd.common.newbee.cloud.pojo.MallUserToken;
import ltd.shopcart.newbee.cloud.config.annotation.TokenToMallUser;
import ltd.user.newbee.cloud.openfeign.NewBeeCloudUserServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class TokenToMallUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	private NewBeeCloudUserServiceFeign newBeeCloudUserServiceFeign;

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
			String token = webRequest.getHeader("token");
			if (null != token && !"".equals(token) && token.length() == Constants.TOKEN_LENGTH) {
				// 调用用户微服务，根据token获取商城用户的数据
				Result<MallUserDTO> result = newBeeCloudUserServiceFeign.getMallUserByToken(token);
				if (result == null || result.getResultCode() != 200 || result.getData() == null) {
					NewBeeMallException.fail(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
				}
				MallUserToken mallUserToken = new MallUserToken();
				mallUserToken.setToken(token);
				mallUserToken.setUserId(result.getData().getUserId());
				return mallUserToken;
			} else {
				NewBeeMallException.fail(ServiceResultEnum.ADMIN_NOT_LOGIN_ERROR.getResult());
			}
		}
		return null;
	}

}
