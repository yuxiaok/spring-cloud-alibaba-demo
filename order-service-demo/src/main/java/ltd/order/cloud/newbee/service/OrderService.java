package ltd.order.cloud.newbee.service;

import io.seata.spring.annotation.GlobalTransactional;
import ltd.order.cloud.newbee.openfeign.NewBeeGoodsDemoService;
import ltd.order.cloud.newbee.openfeign.NewBeeShopCartDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : TODO </li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年10⽉26⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
@Service
public class OrderService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NewBeeGoodsDemoService newBeeGoodsDemoService;

	@Autowired
	private NewBeeShopCartDemoService newBeeShopCartDemoService;

	@Transactional
	@GlobalTransactional
	public boolean saveOrder(int cartId) {
		//简单的模拟下单流程，包括服务件的调用流程

		//调用购物车服务-获取即将操作的goodsId
		int goodsId = newBeeShopCartDemoService.getGoodsId(cartId);


		//调用商品服务-减库存（模拟网络波动情况：库存扣减成功，购物车删除失败，订单生成失败）
		Boolean goodsRes = newBeeGoodsDemoService.deStock(goodsId);

		//调用购物车服务-删除当前购物车数据
		Boolean cartRes = newBeeShopCartDemoService.deleteItem(cartId);

		//执行下单逻辑
		if (goodsRes && cartRes) {
			//向订单表中新增一条数据
			int orderResult = jdbcTemplate.update("insert into tb_order(`cart_id`) value (\"" + cartId + "\")");
			//模拟异常情况（库存扣减成功，购物车删除成功，订单生成失败）
//			int i = 1 / 0;
			if (orderResult > 0) {
				return true;
			}
			return false;
		}
		return false;
	}
}