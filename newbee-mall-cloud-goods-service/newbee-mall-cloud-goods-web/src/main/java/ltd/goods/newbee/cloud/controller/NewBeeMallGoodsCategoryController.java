package ltd.goods.newbee.cloud.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ltd.common.newbee.cloud.common.ServiceResultEnum;
import ltd.common.newbee.cloud.dto.Result;
import ltd.common.newbee.cloud.dto.ResultGenerator;
import ltd.common.newbee.cloud.exception.NewBeeMallException;
import ltd.goods.newbee.cloud.controller.vo.NewBeeMallIndexCategoryVO;
import ltd.goods.newbee.cloud.service.NewBeeMallCategoryService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
@RestController
@Api(value = "v1", tags = "新蜂商城分类页面接口")
@RequestMapping("/categories/mall")
public class NewBeeMallGoodsCategoryController {
	@Resource
	private NewBeeMallCategoryService newBeeMallCategoryService;

	@GetMapping("/listAll")
	@ApiOperation(value = "获取分类数据", notes = "分类页面使用")
	public Result<List<NewBeeMallIndexCategoryVO>> getCategories() {
		List<NewBeeMallIndexCategoryVO> categories = newBeeMallCategoryService.getCategoriesForIndex();
		if (CollectionUtils.isEmpty(categories)) {
			NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
		}
		return ResultGenerator.genSuccessResult(categories);
	}
}