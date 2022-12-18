package cn.youngkbt.generic.base.controller;

import cn.youngkbt.generic.base.model.GenericUser;
import cn.youngkbt.generic.base.service.GenericUserService;
import cn.youngkbt.generic.http.HttpResult;
import cn.youngkbt.generic.http.Response;
import cn.youngkbt.generic.utils.JwtTokenUtils;
import cn.youngkbt.generic.utils.ObjectUtils;
import cn.youngkbt.generic.utils.StringUtils;
import cn.youngkbt.generic.valid.ValidList;
import cn.youngkbt.generic.vo.ConditionVo;
import cn.youngkbt.generic.vo.GenericUserVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
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
        BeanUtils.copyProperties(genericUser, userVo);
        if(genericUser.getStatus() == 0) {
            userVo.setStatus("在线");
        }else if(genericUser.getStatus() == 1) {
            userVo.setStatus("离线");
        }
        if(genericUser.getGender() == 0) {
            userVo.setGender("无");
        }else if(genericUser.getStatus() == 1) {
            userVo.setGender("男");
        }
        else if(genericUser.getStatus() == 2) {
            userVo.setGender("女");
        }
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
        IPage<GenericUser> categoryList = genericUserService.queryGenericUserConditionsPages(page, conditionVos);
        return HttpResult.ok(categoryList.getRecords());
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