package ltd.newbee.cloud.entity.param;

import ltd.newbee.cloud.entity.NewBeeCartItem;
import ltd.newbee.cloud.entity.NewBeeGoodsInfo;

import java.util.List;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : TODO </li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年10⽉23⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
public class ComplexObject {
	private int requestNum;

	private List<Integer> cartIds;

	private List<NewBeeGoodsInfo> newBeeGoodsInfos;

	private NewBeeCartItem newBeeCartItem;

	public void setRequestNum(int requestNum) {
		this.requestNum = requestNum;
	}

	public int getRequestNum() {
		return this.requestNum;
	}

	public void setCartIds(List<Integer> cartIds) {
		this.cartIds = cartIds;
	}

	public List<Integer> getCartIds() {
		return this.cartIds;
	}

	public void setNewBeeGoodsInfos(List<NewBeeGoodsInfo> newBeeGoodsInfos) {
		this.newBeeGoodsInfos = newBeeGoodsInfos;
	}

	public List<NewBeeGoodsInfo> getNewBeeGoodsInfos() {
		return this.newBeeGoodsInfos;
	}

	public void setNewBeeCartItem(NewBeeCartItem newBeeCartItem) {
		this.newBeeCartItem = newBeeCartItem;
	}

	public NewBeeCartItem getNewBeeCartItem() {
		return this.newBeeCartItem;
	}

	@Override
	public String toString() {
		return "ComplexObject{" +
				"requestNum=" + requestNum +
				", cartIds=" + cartIds +
				", newBeeGoodsInfos=" + newBeeGoodsInfos +
				", newBeeCartItem=" + newBeeCartItem +
				'}';
	}
}