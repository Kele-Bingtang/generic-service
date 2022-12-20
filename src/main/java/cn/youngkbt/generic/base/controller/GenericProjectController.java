package cn.youngkbt.generic.base.controller;

import cn.youngkbt.generic.base.model.GenericProject;
import cn.youngkbt.generic.base.model.UserProject;
import cn.youngkbt.generic.base.service.GenericProjectService;
import cn.youngkbt.generic.http.HttpResult;
import cn.youngkbt.generic.http.Response;
import cn.youngkbt.generic.utils.ObjectUtils;
import cn.youngkbt.generic.utils.StringUtils;
import cn.youngkbt.generic.valid.ValidList;
import cn.youngkbt.generic.vo.ConditionVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
@RestController
@RequestMapping("/genericProject")
public class GenericProjectController {

	@Autowired
	private GenericProjectService genericProjectService;

	@GetMapping("/queryGenericProjectByConditions")
	public Response queryGenericProjectByConditions(@RequestBody List<ConditionVo> conditionVos) {
		List<GenericProject> project = genericProjectService.queryGenericProjectByConditions(conditionVos);
		return HttpResult.ok(project);
	}

	@GetMapping("/queryGenericOneProject/{secretKey}")
	public Response queryGenericProjectList(@PathVariable("secretKey") String secretKey) {
		if(StringUtils.isBlank(secretKey)) {
			return HttpResult.fail("无效的参数");
		}
		GenericProject project = genericProjectService.queryGenericOneProject(secretKey);
		if(ObjectUtils.isEmpty(project) || ObjectUtils.isEmpty(project.getId())) {
			return HttpResult.fail("无效的参数");
		}
		return HttpResult.ok(project);
	}

	@GetMapping("/queryGenericProjectListOwner")
	public Response queryGenericProjectListOwner(UserProject userProject) {
		List<GenericProject> projectList = genericProjectService.queryGenericProjectListOwner(userProject);
		return HttpResult.ok(projectList);
	}

	@GetMapping("/queryGenericProjectListPages")
	public Response queryGenericProjectListPages(GenericProject genericProject, @RequestParam(defaultValue = "1", required = false) Integer pageNo, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
		IPage<GenericProject> page = new Page<>(pageNo, pageSize);
		IPage<GenericProject> projectListPages = genericProjectService.queryGenericProjectListPages(page, genericProject);
		return HttpResult.ok(projectListPages.getRecords());
	}

	@GetMapping("/queryGenericProjectConditionsPages")
	public Response queryGenericProjectConditionsPages(@Validated @RequestBody(required = false) ValidList<ConditionVo> conditionVos, @RequestParam(defaultValue = "1", required = false) Integer pageNo, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
		IPage<GenericProject> page = new Page<>(pageNo, pageSize);
		IPage<GenericProject> categoryList = genericProjectService.queryGenericProjectConditionsPages(page, conditionVos);
		return HttpResult.ok(categoryList.getRecords());
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