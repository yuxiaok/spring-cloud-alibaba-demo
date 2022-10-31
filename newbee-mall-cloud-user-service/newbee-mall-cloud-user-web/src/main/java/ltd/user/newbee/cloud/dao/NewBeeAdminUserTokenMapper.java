/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.user.newbee.cloud.dao;

import ltd.common.newbee.cloud.pojo.AdminUserToken;

public interface NewBeeAdminUserTokenMapper {
	int deleteByPrimaryKey(Long userId);

	int insert(AdminUserToken record);

	int insertSelective(AdminUserToken record);

	AdminUserToken selectByPrimaryKey(Long userId);

	AdminUserToken selectByToken(String token);

	int updateByPrimaryKeySelective(AdminUserToken record);

	int updateByPrimaryKey(AdminUserToken record);
}