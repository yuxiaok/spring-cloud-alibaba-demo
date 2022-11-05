/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.shopcart.newbee.cloud.service.impl;

import ltd.common.newbee.cloud.common.Constants;
import ltd.common.newbee.cloud.common.ServiceResultEnum;
import ltd.common.newbee.cloud.dto.*;
import ltd.common.newbee.cloud.exception.NewBeeMallException;
import ltd.common.newbee.cloud.util.BeanUtil;
import ltd.goods.newbee.cloud.openfeign.NewBeeCloudGoodsServiceFeign;
import ltd.shopcart.newbee.cloud.controller.param.SaveCartItemParam;
import ltd.shopcart.newbee.cloud.controller.param.UpdateCartItemParam;
import ltd.shopcart.newbee.cloud.controller.vo.NewBeeMallShoppingCartItemVO;
import ltd.shopcart.newbee.cloud.dao.NewBeeMallShoppingCartItemMapper;
import ltd.shopcart.newbee.cloud.entity.NewBeeMallShoppingCartItem;
import ltd.shopcart.newbee.cloud.service.NewBeeMallShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NewBeeMallShoppingCartServiceImpl implements NewBeeMallShoppingCartService {

	@Autowired
	private NewBeeMallShoppingCartItemMapper newBeeMallShoppingCartItemMapper;

	@Autowired
	private NewBeeCloudGoodsServiceFeign newBeeCloudGoodsServiceFeign;

	@Override
	public String saveNewBeeMallCartItem(SaveCartItemParam saveCartItemParam, Long userId) {
		NewBeeMallShoppingCartItem temp = newBeeMallShoppingCartItemMapper.selectByUserIdAndGoodsId(userId, saveCartItemParam.getGoodsId());
		if (temp != null) {
			//已存在则修改该记录
			NewBeeMallException.fail(ServiceResultEnum.SHOPPING_CART_ITEM_EXIST_ERROR.getResult());
		}
		Result<NewBeeMallGoodsDTO> newBeeMallGoodsDTOResult = newBeeCloudGoodsServiceFeign.goodsDetail(saveCartItemParam.getGoodsId());
		// 商品为空
		if (newBeeMallGoodsDTOResult == null || newBeeMallGoodsDTOResult.getResultCode() != 200) {
			return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
		}
		int totalItem = newBeeMallShoppingCartItemMapper.selectCountByUserId(userId);
		//超出单个商品的最大数量
		if (saveCartItemParam.getGoodsCount() < 1) {
			return ServiceResultEnum.SHOPPING_CART_ITEM_NUMBER_ERROR.getResult();
		}
		//超出单个商品的最大数量
		if (saveCartItemParam.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
			return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
		}
		//超出最大数量
		if (totalItem > Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER) {
			return ServiceResultEnum.SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR.getResult();
		}
		NewBeeMallShoppingCartItem newBeeMallShoppingCartItem = new NewBeeMallShoppingCartItem();
		BeanUtil.copyProperties(saveCartItemParam, newBeeMallShoppingCartItem);
		newBeeMallShoppingCartItem.setUserId(userId);
		//保存记录
		if (newBeeMallShoppingCartItemMapper.insertSelective(newBeeMallShoppingCartItem) > 0) {
			return ServiceResultEnum.SUCCESS.getResult();
		}
		return ServiceResultEnum.DB_ERROR.getResult();
	}

	@Override
	public String updateNewBeeMallCartItem(UpdateCartItemParam updateCartItemParam, Long userId) {
		NewBeeMallShoppingCartItem newBeeMallShoppingCartItemUpdate = newBeeMallShoppingCartItemMapper.selectByPrimaryKey(updateCartItemParam.getCartItemId());
		if (newBeeMallShoppingCartItemUpdate == null) {
			return ServiceResultEnum.DATA_NOT_EXIST.getResult();
		}
		if (!newBeeMallShoppingCartItemUpdate.getUserId().equals(userId)) {
			NewBeeMallException.fail(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
		}
		//超出单个商品的最大数量
		if (updateCartItemParam.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
			return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
		}
		//当前登录账号的userId与待修改的cartItem中userId不同，返回错误
		if (!newBeeMallShoppingCartItemUpdate.getUserId().equals(userId)) {
			return ServiceResultEnum.NO_PERMISSION_ERROR.getResult();
		}
		//数值相同，则不执行数据操作
		if (updateCartItemParam.getGoodsCount().equals(newBeeMallShoppingCartItemUpdate.getGoodsCount())) {
			return ServiceResultEnum.SUCCESS.getResult();
		}
		newBeeMallShoppingCartItemUpdate.setGoodsCount(updateCartItemParam.getGoodsCount());
		newBeeMallShoppingCartItemUpdate.setUpdateTime(new Date());
		//修改记录
		if (newBeeMallShoppingCartItemMapper.updateByPrimaryKeySelective(newBeeMallShoppingCartItemUpdate) > 0) {
			return ServiceResultEnum.SUCCESS.getResult();
		}
		return ServiceResultEnum.DB_ERROR.getResult();
	}

	@Override
	public NewBeeMallShoppingCartItem getNewBeeMallCartItemById(Long newBeeMallShoppingCartItemId) {
		NewBeeMallShoppingCartItem newBeeMallShoppingCartItem = newBeeMallShoppingCartItemMapper.selectByPrimaryKey(newBeeMallShoppingCartItemId);
		if (newBeeMallShoppingCartItem == null) {
			NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
		}
		return newBeeMallShoppingCartItem;
	}

	@Override
	public Boolean deleteById(Long shoppingCartItemId, Long userId) {
		NewBeeMallShoppingCartItem newBeeMallShoppingCartItem = newBeeMallShoppingCartItemMapper.selectByPrimaryKey(shoppingCartItemId);
		if (newBeeMallShoppingCartItem == null) {
			return false;
		}
		//userId不同不能删除
		if (!userId.equals(newBeeMallShoppingCartItem.getUserId())) {
			return false;
		}
		return newBeeMallShoppingCartItemMapper.deleteByPrimaryKey(shoppingCartItemId) > 0;
	}

	@Override
	public List<NewBeeMallShoppingCartItemVO> getMyShoppingCartItems(Long newBeeMallUserId) {
		List<NewBeeMallShoppingCartItemVO> newBeeMallShoppingCartItemVOS = new ArrayList<>();
		List<NewBeeMallShoppingCartItem> newBeeMallShoppingCartItems = newBeeMallShoppingCartItemMapper.selectByUserId(newBeeMallUserId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
		return getNewBeeMallShoppingCartItemVOS(newBeeMallShoppingCartItemVOS, newBeeMallShoppingCartItems);
	}

	@Override
	public List<NewBeeMallShoppingCartItemVO> getCartItemsForSettle(List<Long> cartItemIds, Long newBeeMallUserId) {
		List<NewBeeMallShoppingCartItemVO> newBeeMallShoppingCartItemVOS = new ArrayList<>();
		if (CollectionUtils.isEmpty(cartItemIds)) {
			NewBeeMallException.fail("购物项不能为空");
		}
		List<NewBeeMallShoppingCartItem> newBeeMallShoppingCartItems = newBeeMallShoppingCartItemMapper.selectByUserIdAndCartItemIds(newBeeMallUserId, cartItemIds);
		if (CollectionUtils.isEmpty(newBeeMallShoppingCartItems)) {
			NewBeeMallException.fail("购物项不能为空");
		}
		if (newBeeMallShoppingCartItems.size() != cartItemIds.size()) {
			NewBeeMallException.fail("参数异常");
		}
		return getNewBeeMallShoppingCartItemVOS(newBeeMallShoppingCartItemVOS, newBeeMallShoppingCartItems);
	}

	/**
	 * 数据转换
	 *
	 * @param newBeeMallShoppingCartItemVOS
	 * @param newBeeMallShoppingCartItems
	 * @return
	 */
	private List<NewBeeMallShoppingCartItemVO> getNewBeeMallShoppingCartItemVOS(List<NewBeeMallShoppingCartItemVO> newBeeMallShoppingCartItemVOS, List<NewBeeMallShoppingCartItem> newBeeMallShoppingCartItems) {
		if (!CollectionUtils.isEmpty(newBeeMallShoppingCartItems)) {
			//查询商品信息并做数据转换
			List<Long> newBeeMallGoodsIds = newBeeMallShoppingCartItems.stream().map(NewBeeMallShoppingCartItem::getGoodsId).collect(Collectors.toList());
			Result<List<NewBeeMallGoodsDTO>> newBeeMallGoodsDTOResult = newBeeCloudGoodsServiceFeign.getNewBeeMallGoodsByIds(newBeeMallGoodsIds);
			// 远程调用返回数据为空
			if (newBeeMallGoodsDTOResult == null || newBeeMallGoodsDTOResult.getResultCode() != 200) {
				NewBeeMallException.fail(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
			}
			Map<Long, NewBeeMallGoodsDTO> newBeeMallGoodsMap = new HashMap<>();
			List<NewBeeMallGoodsDTO> newBeeMallGoods = newBeeMallGoodsDTOResult.getData();
			if (!CollectionUtils.isEmpty(newBeeMallGoods)) {
				newBeeMallGoodsMap = newBeeMallGoods.stream().collect(Collectors.toMap(NewBeeMallGoodsDTO::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
			}
			for (NewBeeMallShoppingCartItem newBeeMallShoppingCartItem : newBeeMallShoppingCartItems) {
				NewBeeMallShoppingCartItemVO newBeeMallShoppingCartItemVO = new NewBeeMallShoppingCartItemVO();
				BeanUtil.copyProperties(newBeeMallShoppingCartItem, newBeeMallShoppingCartItemVO);
				if (newBeeMallGoodsMap.containsKey(newBeeMallShoppingCartItem.getGoodsId())) {
					NewBeeMallGoodsDTO newBeeMallGoodsTemp = newBeeMallGoodsMap.get(newBeeMallShoppingCartItem.getGoodsId());
					newBeeMallShoppingCartItemVO.setGoodsCoverImg(newBeeMallGoodsTemp.getGoodsCoverImg());
					String goodsName = newBeeMallGoodsTemp.getGoodsName();
					// 字符串过长导致文字超出的问题
					if (goodsName.length() > 28) {
						goodsName = goodsName.substring(0, 28) + "...";
					}
					newBeeMallShoppingCartItemVO.setGoodsName(goodsName);
					newBeeMallShoppingCartItemVO.setSellingPrice(newBeeMallGoodsTemp.getSellingPrice());
					newBeeMallShoppingCartItemVOS.add(newBeeMallShoppingCartItemVO);
				}
			}
		}
		return newBeeMallShoppingCartItemVOS;
	}

	@Override
	public PageResult getMyShoppingCartItems(PageQueryUtil pageUtil) {
		List<NewBeeMallShoppingCartItemVO> newBeeMallShoppingCartItemVOS = new ArrayList<>();
		List<NewBeeMallShoppingCartItem> newBeeMallShoppingCartItems = newBeeMallShoppingCartItemMapper.findMyNewBeeMallCartItems(pageUtil);
		int total = newBeeMallShoppingCartItemMapper.getTotalMyNewBeeMallCartItems(pageUtil);
		PageResult pageResult = new PageResult(getNewBeeMallShoppingCartItemVOS(newBeeMallShoppingCartItemVOS, newBeeMallShoppingCartItems), total, pageUtil.getLimit(), pageUtil.getPage());
		return pageResult;
	}

	@Override
	public List<NewBeeMallShoppingCartItemDTO> getCartItemsByCartIds(List<Long> cartItemIds) {
		List<NewBeeMallShoppingCartItem> newBeeMallShoppingCartItems = newBeeMallShoppingCartItemMapper.selectByCartItemIds(cartItemIds);
		List<NewBeeMallShoppingCartItemVO> newBeeMallShoppingCartItemVOS = new ArrayList<>();
		getNewBeeMallShoppingCartItemVOS(newBeeMallShoppingCartItemVOS, newBeeMallShoppingCartItems);
		List<NewBeeMallShoppingCartItemDTO> newBeeMallShoppingCartItemDTOS = BeanUtil.copyList(newBeeMallShoppingCartItemVOS, NewBeeMallShoppingCartItemDTO.class);
		return newBeeMallShoppingCartItemDTOS;
	}

	@Override
	public Integer deleteCartItemsByCartIds(List<Long> cartItemIds) {
		return newBeeMallShoppingCartItemMapper.deleteBatch(cartItemIds);
	}
}
