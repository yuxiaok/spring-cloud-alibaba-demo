package ltd.common.newbee.cloud.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : TODO </li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年10⽉31⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginAdminUser {
	private Long adminUserId;

	private String loginUserName;

	private String loginPassword;

	private String nickName;

	private Byte locked;
}