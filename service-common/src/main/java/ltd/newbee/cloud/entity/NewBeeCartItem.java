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
public class NewBeeCartItem {
	private int itemId;

	private String cartString;


	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getItemId() {
		return this.itemId;
	}

	public void setCartString(String cartString) {
		this.cartString = cartString;
	}

	public String getCartString() {
		return this.cartString;
	}


	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append(" itemId=").append(itemId);
		sb.append(", cartString=").append(cartString);
		sb.append("]");
		return sb.toString();
	}
}