package ltd.common.newbee.cloud.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : TODO </li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年11⽉05⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
@Data
public class MallUserDTO implements Serializable {
	private Long userId;

	private String nickName;

	private String loginName;

	private String passwordMd5;

	private String introduceSign;

	private Byte isDeleted;

	private Byte lockedFlag;

	private Date createTime;
}