package cn.youngkbt.generic.base.controller;

import java.util.List;

import cn.youngkbt.generic.base.model.GenericUser;
import cn.youngkbt.generic.base.service.GenericUserService;
import cn.youngkbt.generic.http.HttpResult;
import cn.youngkbt.generic.http.Response;
import cn.youngkbt.generic.utils.SearchUtil;
import cn.youngkbt.generic.valid.ValidList;
import cn.youngkbt.generic.vo.ConditionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Kele-Bingtang
 * @since 2022-12-03 22:45:22
 * @version 1.0
 */
@RestController
@RequestMapping("/genericUser")
public class GenericUserController {

	@Autowired
	private GenericUserService genericUserService;

	@GetMapping("/queryGenericUserByConditions")
	public Response queryGenericUserByConditions(@RequestBody List<ConditionVo> conditionVos) {
		QueryWrapper<GenericUser> queryWrapper = SearchUtil.parseWhereSql(conditionVos, GenericUser.class);
		List<GenericUser> user = genericUserService.queryGenericUserByConditions(queryWrapper);
		return HttpResult.ok(user);
	}

	@GetMapping("/queryGenericUserList")
	public Response queryGenericUserList(GenericUser genericUser) {
		List<GenericUser> userList = genericUserService.queryGenericUserList(genericUser);
		return HttpResult.ok(userList);
	}

	@GetMapping("/queryGenericUserListPages")
	public Response queryGenericUserListPages(GenericUser genericUser,  @RequestParam(defaultValue = "1", required = false) Integer pageNo, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
		IPage<GenericUser> page = new Page<>(pageNo, pageSize);
		IPage<GenericUser> userIPage = genericUserService.queryGenericUserListPages(page, genericUser);
		return HttpResult.ok(userIPage);
	}

	@GetMapping("/queryGenericUserPages")
	public Response queryGenericUserPages(@Validated @RequestBody(required = false) ValidList<ConditionVo> conditionVos, @RequestParam(defaultValue = "1", required = false) Integer pageNo, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
		IPage<GenericUser> page = new Page<>(pageNo, pageSize);
		QueryWrapper<GenericUser> queryWrapper = SearchUtil.parseWhereSql(conditionVos, GenericUser.class);
		IPage<GenericUser> categoryList = genericUserService.queryGenericUserConditionsPages(page, queryWrapper);
		return HttpResult.ok(categoryList);
	}

	@PostMapping("/insertGenericUser")
	public Response insertGenericUser(@Validated(GenericUser.UserInsert.class) @RequestBody GenericUser genericUser) {
		GenericUser user = genericUserService.insertGenericUser(genericUser);
		return HttpResult.okOrFail(user);
	}

	@PostMapping("/updateGenericUser")
	public Response updateGenericUser(@Validated(GenericUser.UserUpdate.class) @RequestBody GenericUser genericUser) {
		GenericUser user = genericUserService.updateGenericUser(genericUser);
		return HttpResult.okOrFail(user);
	}

	@PostMapping("/deleteGenericUserById")
	public Response deleteGenericUserById(@Validated(GenericUser.UserDelete.class) @RequestBody GenericUser genericUser) {
		GenericUser user = genericUserService.deleteGenericUserById(genericUser);
		return HttpResult.okOrFail(user);
	}

}