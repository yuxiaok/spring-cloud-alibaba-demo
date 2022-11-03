/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.common.newbee.cloud.dto;

import lombok.Data;

import java.util.Date;

@Data
public class NewBeeMallGoodsDTO {
	private Long goodsId;

	private String goodsName;

	private String goodsIntro;

	private Long goodsCategoryId;

	private String goodsCoverImg;

	private String goodsCarousel;

	private Integer originalPrice;

	private Integer sellingPrice;

	private Integer stockNum;

	private String tag;

	private Byte goodsSellStatus;

	private Integer createUser;

	private Date createTime;

	private Integer updateUser;

	private Date updateTime;

	private String goodsDetailContent;
}