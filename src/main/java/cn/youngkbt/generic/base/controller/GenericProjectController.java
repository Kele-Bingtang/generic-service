package cn.youngkbt.generic.base.controller;

import cn.youngkbt.generic.base.model.GenericProject;
import cn.youngkbt.generic.base.service.GenericProjectService;
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

import java.util.List;

/**
 * @author Kele-Bingtang
 * @since 2022-12-03 22:45:22
 * @version 1.0
 */
@RestController
@RequestMapping("/genericProject")
public class GenericProjectController {

	@Autowired
	private GenericProjectService genericProjectService;

	@GetMapping("/queryGenericProjectByConditions")
	public Response queryGenericProjectByConditions(@RequestBody List<ConditionVo> conditionVos) {
		QueryWrapper<GenericProject> queryWrapper = SearchUtil.parseWhereSql(conditionVos, GenericProject.class);
		List<GenericProject> project = genericProjectService.queryGenericProjectByConditions(queryWrapper);
		return HttpResult.ok(project);
	}

	@GetMapping("/queryGenericProjectList")
	public Response queryGenericProjectList(GenericProject genericProject) {
		List<GenericProject> projectList = genericProjectService.queryGenericProjectList(genericProject);
		return HttpResult.ok(projectList);
	}

	@GetMapping("/queryGenericProjectListPages")
	public Response queryGenericProjectListPages(GenericProject genericProject, @RequestParam(defaultValue = "1", required = false) Integer pageNo, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
		IPage<GenericProject> page = new Page<>(pageNo, pageSize);
		IPage<GenericProject> projectListPages = genericProjectService.queryGenericProjectListPages(page, genericProject);
		return HttpResult.ok(projectListPages);
	}

	@GetMapping("/queryGenericProjectPages")
	public Response queryGenericProjectPages(@Validated @RequestBody(required = false) ValidList<ConditionVo> conditionVos, @RequestParam(defaultValue = "1", required = false) Integer pageNo, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
		IPage<GenericProject> page = new Page<>(pageNo, pageSize);
		QueryWrapper<GenericProject> queryWrapper = SearchUtil.parseWhereSql(conditionVos, GenericProject.class);
		IPage<GenericProject> categoryList = genericProjectService.queryGenericProjectConditionsPages(page, queryWrapper);
		return HttpResult.ok(categoryList);
	}

	@PostMapping("/insertGenericProject")
	public Response insertGenericProject(@Validated(GenericProject.ProjectInsert.class) @RequestBody GenericProject genericProject) {
		GenericProject project = genericProjectService.insertGenericProject(genericProject);
		return HttpResult.okOrFail(project);
	}

	@PostMapping("/updateGenericProject")
	public Response updateGenericProject(@Validated(GenericProject.ProjectUpdate.class) @RequestBody GenericProject genericProject) {
		GenericProject project = genericProjectService.updateGenericProject(genericProject);
		return HttpResult.okOrFail(project);
	}

	@PostMapping("/deleteGenericProjectById")
	public Response deleteGenericProjectById(@Validated(GenericProject.ProjectDelete.class) @RequestBody GenericProject genericProject) {
		GenericProject project = genericProjectService.deleteGenericProjectById(genericProject);
		return HttpResult.okOrFail(project);
	}

}