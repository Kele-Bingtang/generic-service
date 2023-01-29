package cn.youngkbt.generic.base.controller;

import cn.youngkbt.generic.base.model.GenericRole;
import cn.youngkbt.generic.base.model.GenericUser;
import cn.youngkbt.generic.base.service.GenericUserService;
import cn.youngkbt.generic.http.HttpResult;
import cn.youngkbt.generic.http.Response;
import cn.youngkbt.generic.utils.JwtTokenUtils;
import cn.youngkbt.generic.utils.ObjectUtils;
import cn.youngkbt.generic.utils.StringUtils;
import cn.youngkbt.generic.valid.ValidList;
import cn.youngkbt.generic.vo.ConditionVo;
import cn.youngkbt.generic.vo.GenericRoleVo;
import cn.youngkbt.generic.vo.GenericUserVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
@RestController
@RequestMapping("/genericUser")
@Validated
public class GenericUserController {

    @Resource
    private GenericUserService genericUserService;

    @PostMapping("/getUserInfo")
    public Response getUserInfo(@NotBlank(message = "无效参数") String token) {
        String username = JwtTokenUtils.getUsernameFromToken(token);
        if (StringUtils.isBlank(username)) {
            return HttpResult.fail("获取用户信息失败");
        }
        GenericUser genericUser = genericUserService.findByUsername(username);
        if (ObjectUtils.isEmpty(genericUser) || StringUtils.isBlank(genericUser.getUsername())) {
            return HttpResult.fail("用户信息不存在");
        }
        GenericUserVo userVo = new GenericUserVo();
        this.convertModelToVo(genericUser, userVo);
        return HttpResult.ok(userVo);
    }

    @GetMapping("/queryGenericUserByConditions")
    public Response queryGenericUserByConditions(@Validated @RequestBody ValidList<ConditionVo> conditionVos) {
        List<GenericUser> user = genericUserService.queryGenericUserByConditions(conditionVos);
        return HttpResult.ok(user);
    }

    @GetMapping("/queryGenericUserListPages")
    public Response queryGenericUserListPages(GenericUser genericUser, @RequestParam(defaultValue = "1", required = false) Integer pageNo, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        IPage<GenericUser> page = new Page<>(pageNo, pageSize);
        IPage<GenericUser> userIPage = genericUserService.queryGenericUserListPages(page, genericUser);
        return HttpResult.ok(userIPage.getRecords());
    }

    @GetMapping("/queryGenericUserConditionsPages")
    public Response queryGenericUserConditionsPages(@Validated @RequestBody(required = false) ValidList<ConditionVo> conditionVos, @RequestParam(defaultValue = "1", required = false) Integer pageNo, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        IPage<GenericUser> page = new Page<>(pageNo, pageSize);
        IPage<GenericUser> userPage = genericUserService.queryGenericUserConditionsPages(page, conditionVos);
        return HttpResult.ok(userPage.getRecords());
    }

    @GetMapping("/queryGenericMemberInProject/{secretKey}")
    public Response queryGenericMemberInProject(@NotBlank(message = "无效参数") @PathVariable("secretKey") String secretKey, @RequestParam(defaultValue = "1", required = false) Integer pageNo, @RequestParam(defaultValue = "50", required = false) Integer pageSize) {
        List<GenericUser> userList = genericUserService.queryGenericMemberInProject(secretKey, pageNo, pageSize);
        List<GenericUserVo> userVoList = new ArrayList<>();
        userList.forEach(user -> {
            GenericUserVo userVo = new GenericUserVo();
            GenericRoleVo roleVo = new GenericRoleVo();
            this.convertModelToVo(user, userVo);
            // user 对象有 role 对象，BeanUtils 无法转换对象里的对象，所以只能手动转
            roleVo.setCode(user.getGenericRole().getCode());
            roleVo.setName(user.getGenericRole().getName());
            userVo.setRole(roleVo);
            userVoList.add(userVo);
        });
        return HttpResult.ok(userVoList);
    }

    @GetMapping("/queryAllMemberNotInProject/{secretKey}")
    public Response queryAllMemberNotInProject(@NotBlank(message = "无效参数") @PathVariable("secretKey") String secretKey) {
        List<GenericUser> userList = genericUserService.queryAllMemberNotInProject(secretKey);
        List<Map<String, String>> userListMap = new ArrayList<>();
        userList.forEach(user -> {
            // 只传 username、email、status
            Map<String, String> map = new HashMap<>();
            map.put("username", user.getUsername());
            map.put("email", user.getEmail());
            if (user.getStatus() == 0) {
                map.put("status","离线");
            } else if (user.getStatus() == 1) {
                map.put("status","在线");
            }
            userListMap.add(map);
        });
        return HttpResult.ok(userListMap);
    }

    @GetMapping("/queryGenericUserRole/{secretKey}")
    public Response queryGenericUserRole(@NotBlank(message = "无效参数") @PathVariable("secretKey") String secretKey) {
        GenericRole role = genericUserService.queryGenericUserRole(secretKey);
        if (ObjectUtils.isEmpty(role) || ObjectUtils.isEmpty(role.getCode())) {
            return HttpResult.fail("无效参数");
        }
        GenericRoleVo genericRoleVo = new GenericRoleVo();
        genericRoleVo.setCode(role.getCode());
        genericRoleVo.setName(role.getName());
        return HttpResult.ok(genericRoleVo);
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

    @PostMapping("/updateGenericUserRole")
    public Response updateGenericUserRole(@NotBlank(message = "无效参数") String username, @NotNull(message = "无效参数") Integer projectId, @NotBlank(message = "无效参数") String roleCode) {
        boolean isSuccess = genericUserService.updateGenericUserRole(username, projectId, roleCode);
        if (!isSuccess) {
            return HttpResult.fail("更新角色失败");
        }
        return HttpResult.ok("更新角色成功");
    }

    @PostMapping("/insertGenericUserProject/{projectId}")
    public Response insertGenericUserProject(@NotNull(message = "无效参数") @PathVariable("projectId") Integer projectId, @RequestBody List<GenericUser> userList) {
        boolean isSuccess = genericUserService.insertGenericUserProject(projectId, userList);
        if (!isSuccess) {
            return HttpResult.fail("添加成员失败");
        }
        return HttpResult.ok("添加成员成功");
    }

    @PostMapping("/removeOneMember")
    public Response removeOneMember(@NotNull(message = "无效参数") String username, @NotNull(message = "无效参数")Integer projectId) {
        boolean isSuccess = genericUserService.removeOneMember(username, projectId);
        if (!isSuccess) {
            return HttpResult.fail("成员/个人离开项目失败");
        }
        return HttpResult.ok("成员/个人离开项目成功");
    }

    public void convertModelToVo(GenericUser user, GenericUserVo userVo) {
        BeanUtils.copyProperties(user, userVo);
        if (user.getStatus() == 0) {
            userVo.setStatus("离线");
        } else if (user.getStatus() == 1) {
            userVo.setStatus("在线");
        }
        if (user.getGender() == 0) {
            userVo.setGender("保密");
        } else if (user.getGender() == 1) {
            userVo.setGender("男");
        } else if (user.getGender() == 2) {
            userVo.setGender("女");
        }
    }
}