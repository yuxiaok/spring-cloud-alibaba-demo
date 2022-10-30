package ltd.newbee.cloud.entity;

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
public class NewBeeGoodsInfo {
	private int goodsId;

	private String goodsName;

	private int stock;

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public int getGoodsId() {
		return this.goodsId;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getStock() {
		return this.stock;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsName() {
		return this.goodsName;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append(" goodsName=").append(goodsName);
		sb.append(", goodsId=").append(goodsId);
		sb.append(", stock=").append(stock);
		sb.append("]");
		return sb.toString();
	}
}