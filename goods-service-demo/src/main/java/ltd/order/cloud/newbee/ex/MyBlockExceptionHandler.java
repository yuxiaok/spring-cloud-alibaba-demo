package ltd.order.cloud.newbee.ex;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : sentinel全局异常处理器 </li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年10⽉29⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
//@Component
public class MyBlockExceptionHandler implements BlockExceptionHandler {
	@Override
	public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
		Logger logger = LoggerFactory.getLogger(MyBlockExceptionHandler.class);
		//Sentinel规则的详细信息
		logger.info("BlockExceptionHandler BlockException ============ " + e.getRule());

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode objectNode = objectMapper.createObjectNode();

		if (e instanceof FlowException) {
			objectNode.put("100", "接口限流了");
		} else if (e instanceof DegradeException) {
			objectNode.put("101", "服务降级了");
		} else if (e instanceof ParamFlowException) {
			objectNode.put("102", "热点参数限流了");
		} else if (e instanceof SystemBlockException) {
			objectNode.put("103", "触发系统保护规则了");
		} else if (e instanceof AuthorityException) {
			objectNode.put("104", "授权规则不通过");
		}

		//返回json数据
		httpServletResponse.setStatus(500);
		httpServletResponse.setCharacterEncoding("utf-8");
		httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
		new ObjectMapper().writeValue(httpServletResponse.getWriter(), objectNode);
	}
}