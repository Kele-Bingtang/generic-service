package cn.youngkbt.generic.base.controller;

import cn.youngkbt.generic.base.dto.project.ProjectDeleteDTO;
import cn.youngkbt.generic.base.dto.project.ProjectInsertDTO;
import cn.youngkbt.generic.base.dto.project.ProjectQueryOwnerDTO;
import cn.youngkbt.generic.base.dto.project.ProjectUpdateDTO;
import cn.youngkbt.generic.base.model.GenericProject;
import cn.youngkbt.generic.base.service.ProjectService;
import cn.youngkbt.generic.base.vo.ProjectVO;
import cn.youngkbt.generic.http.HttpResult;
import cn.youngkbt.generic.http.Response;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
@RestController
@RequestMapping("/project")
@Validated
public class ProjectController {

    @Resource
    private ProjectService projectService;

    @GetMapping("/queryOneProject/{secretKey}")
    public Response<ProjectVO> queryOneProjectBySecretKey(@NotBlank(message = "无效参数") @PathVariable("secretKey") String secretKey) {
        ProjectVO projectVO = projectService.queryOneProjectBySecretKey(secretKey);
        return HttpResult.ok(projectVO);
    }

    @GetMapping("/queryProjectListOwner")
    public Response<List<ProjectVO>> queryProjectListOwner(ProjectQueryOwnerDTO projectQueryOwnerDTO) {
        List<ProjectVO> projectVOList = projectService.queryProjectListOwner(projectQueryOwnerDTO);
        return HttpResult.ok(projectVOList);
    }

    @GetMapping("/queryProjectListPages")
    public Response<List<ProjectVO>> queryProjectListPages(ProjectQueryOwnerDTO projectQueryOwnerDTO,
                                                           @RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                           @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        IPage<GenericProject> page = new Page<>(pageNo, pageSize);
        List<ProjectVO> projectVOList = projectService.queryProjectListPages(page, projectQueryOwnerDTO);
        return HttpResult.ok(projectVOList);
    }

    @PostMapping("/insertProject")
    public Response<String> insertProject(@Validated @RequestBody ProjectInsertDTO projectInsertDTO) {
        String result = projectService.insertProject(projectInsertDTO);
        return HttpResult.okMessage(result);
    }

    @PostMapping("/updateProject")
    public Response<String> updateProject(@Validated @RequestBody ProjectUpdateDTO projectUpdateDTO) {
        String result = projectService.updateProject(projectUpdateDTO);
        return HttpResult.okMessage(result);
    }

    @PostMapping("/deleteProjectById")
    public Response<String> deleteProjectById(@Validated @RequestBody ProjectDeleteDTO projectDeleteDTO) {
        String result = projectService.deleteProjectById(projectDeleteDTO);
        return HttpResult.okMessage(result);
    }
	
    @GetMapping(value = {"/queryDatabaseName", "/queryDatabaseName/{databaseName}"})
    public Response<List<String>> queryDatabaseName(@PathVariable String databaseName) {
        List<String> databaseNameList = projectService.queryDatabaseName(databaseName);
        return HttpResult.okOrFail(databaseNameList, "查询成功");
    }

}