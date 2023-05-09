package cn.youngkbt.generic.controller;

import cn.youngkbt.generic.common.dto.project.ProjectDeleteDTO;
import cn.youngkbt.generic.common.dto.project.ProjectInsertDTO;
import cn.youngkbt.generic.common.dto.project.ProjectQueryOwnerDTO;
import cn.youngkbt.generic.common.dto.project.ProjectUpdateDTO;
import cn.youngkbt.generic.common.http.HttpResult;
import cn.youngkbt.generic.common.http.Response;
import cn.youngkbt.generic.common.model.GenericProject;
import cn.youngkbt.generic.service.ProjectService;
import cn.youngkbt.generic.vo.ProjectVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 项目相关接口
 */
@RestController
@RequestMapping("/project")
@Validated
public class ProjectController {

    @Resource
    private ProjectService projectService;

    /**
     * 通过密钥查询一条项目数据
     * @param secretKey 项目密钥
     * @return 一条项目数据
     */
    @GetMapping("/queryOneProject/{secretKey}")
    public Response<ProjectVO> queryOneProjectBySecretKey(@NotBlank(message = "无效参数") @PathVariable("secretKey") String secretKey) {
        GenericProject project = projectService.queryOneProjectBySecretKey(secretKey);
        ProjectVO projectVO = new ProjectVO();
        BeanUtils.copyProperties(project, projectVO);
        return HttpResult.ok(projectVO);
    }

    @GetMapping("/queryProjectListOwner")
    public Response<List<ProjectVO>> queryProjectListOwner(ProjectQueryOwnerDTO projectQueryOwnerDTO) {
        List<GenericProject> projectList = projectService.queryProjectListOwner(projectQueryOwnerDTO);
        return HttpResult.ok(this.getProjectVOList(projectList));
    }

    @GetMapping("/queryProjectListPages")
    public Response<List<ProjectVO>> queryProjectListPages(ProjectQueryOwnerDTO projectQueryOwnerDTO,
                                                           @RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                           @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        IPage<GenericProject> page = new Page<>(pageNo, pageSize);
        List<GenericProject> projectList = projectService.queryProjectListPages(page, projectQueryOwnerDTO);
        return HttpResult.ok(this.getProjectVOList(projectList));
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

    /**
     * 查询项目所在的数据库集合
     * @param databaseName 数据库名
     * @return 项目所在的数据库集合
     */
    @GetMapping(value = {"/queryDatabaseName", "/queryDatabaseName/{databaseName}"})
    public Response<List<String>> queryDatabaseName(@PathVariable String databaseName) {
        List<String> databaseNameList = projectService.queryDatabaseName(databaseName);
        return HttpResult.okOrFail(databaseNameList, "查询成功");
    }

    public List<ProjectVO> getProjectVOList(List<GenericProject> projectList) {
        List<ProjectVO> projectVOList = new ArrayList<>();
        projectList.forEach(project -> {
            ProjectVO vo = new ProjectVO();
            BeanUtils.copyProperties(project, vo);
            projectVOList.add(vo);
        });
        return projectVOList;
    }

}