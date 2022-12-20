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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
@RestController
@RequestMapping("/genericUser")
public class GenericUserController {

    @Autowired
    private GenericUserService genericUserService;

    @PostMapping("/getUserInfo")
    public Response getUserInfo(String token) {
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
    public Response queryGenericUserByConditions(@RequestBody List<ConditionVo> conditionVos) {
        List<GenericUser> user = genericUserService.queryGenericUserByConditions(conditionVos);
        return HttpResult.ok(user);
    }

    @GetMapping("/queryGenericUserList")
    public Response queryGenericUserList(GenericUser genericUser) {
        List<GenericUser> userList = genericUserService.queryGenericUserList(genericUser);
        return HttpResult.ok(userList);
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

    @GetMapping("/queryGenericMemberInProject")
    public Response queryGenericMemberInProject(@RequestParam String secretKey, @RequestParam(defaultValue = "1", required = false) Integer pageNo, @RequestParam(defaultValue = "50", required = false) Integer pageSize) {
        if(StringUtils.isBlank(secretKey)) {
            return HttpResult.fail("请携带有效的参数！");
        }
        List<GenericUser> userList = genericUserService.queryGenericMemberInProject(secretKey, pageNo, pageSize);
        List<GenericUserVo> userVoList = new ArrayList<>();
        userList.forEach( user -> {
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

    @GetMapping("/queryGenericUserRole/{secretKey}")
    public Response queryGenericUserRole(@PathVariable("secretKey") String secretKey) {
        if(StringUtils.isBlank(secretKey)) {
            return HttpResult.fail("无效的参数");
        }
        GenericRole role = genericUserService.queryGenericUserRole(secretKey);
        if(ObjectUtils.isEmpty(role) || ObjectUtils.isEmpty(role.getCode())) {
            return HttpResult.fail("无效的参数");
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
    public Response updateGenericUserRole(String username, String secretKey, String roleCode) {
        if(StringUtils.isBlank(username, secretKey, roleCode)) {
            return HttpResult.fail("无效的参数");
        }
        int i = genericUserService.updateGenericUserRole(username, secretKey, roleCode);
        if(i == 0) {
            return HttpResult.fail("更新角色失败");
        }
        return HttpResult.ok("更新角色成功");
    }
    
    public void convertModelToVo(GenericUser user, GenericUserVo userVo) {
        BeanUtils.copyProperties(user, userVo);
        if(user.getStatus() == 0) {
            userVo.setStatus("在线");
        }else if(user.getStatus() == 1) {
            userVo.setStatus("离线");
        }
        if(user.getGender() == 0) {
            userVo.setGender("保密");
        }else if(user.getStatus() == 1) {
            userVo.setGender("男");
        }
        else if(user.getStatus() == 2) {
            userVo.setGender("女");
        }
    }
}