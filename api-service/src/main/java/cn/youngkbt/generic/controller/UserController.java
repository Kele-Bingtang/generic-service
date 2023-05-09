package cn.youngkbt.generic.controller;

import cn.youngkbt.generic.common.dto.user.UserInsertDTO;
import cn.youngkbt.generic.common.dto.user.UserUpdateDTO;
import cn.youngkbt.generic.common.http.HttpResult;
import cn.youngkbt.generic.common.http.Response;
import cn.youngkbt.generic.common.http.ResponseStatusEnum;
import cn.youngkbt.generic.common.model.GenericRole;
import cn.youngkbt.generic.common.model.GenericUser;
import cn.youngkbt.generic.security.JwtTokenUtils;
import cn.youngkbt.generic.common.utils.StringUtils;
import cn.youngkbt.generic.service.UserProjectService;
import cn.youngkbt.generic.service.UserService;
import cn.youngkbt.generic.vo.RoleVO;
import cn.youngkbt.generic.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private UserProjectService userProjectService;

    @GetMapping("/login")
    public Response<Object> login() {
        return HttpResult.response(null, 401, "error", "身份失效");
    }

    /**
     * 获取用户信息
     * @param token 用户 token
     * @return 用户信息
     */
    @PostMapping("/getUserInfo")
    public Response<UserVO> getUserInfo(@NotBlank(message = "无效参数") String token) {
        String username = JwtTokenUtils.getUsernameFromToken(token);
        if (StringUtils.isBlank(username)) {
            return HttpResult.okOrFail(null, "获取用户信息失败");
        }
        GenericUser user = userService.findByUsername(username);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setPassword("");
        return HttpResult.ok(userVO);
    }

    /**
     * 查询项目里的成员列表
     * @param secretKey 项目密钥
     * @param pageNo 起始页
     * @param pageSize 页数
     * @return 项目里的成员列表
     */
    @GetMapping("/queryMemberInProject/{secretKey}")
    public Response<List<UserVO>> queryGenericMemberInProject(@NotBlank(message = "无效参数") @PathVariable("secretKey") String secretKey,
                                                              @RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                              @RequestParam(defaultValue = "50", required = false) Integer pageSize) {
        List<GenericUser> userList = userService.queryMemberInProject(secretKey, pageNo, pageSize);
        List<UserVO> userVoList = new ArrayList<>();
        userList.forEach(user -> {
            UserVO userVo = new UserVO();
            RoleVO roleVo = new RoleVO();
            BeanUtils.copyProperties(user, userVo);
            // user 对象有 role 对象，BeanUtils 无法转换对象里的对象，所以只能手动转
            roleVo.setCode(user.getGenericRole().getCode());
            roleVo.setName(user.getGenericRole().getName());
            userVo.setRole(roleVo);
            userVoList.add(userVo);
        });
        return HttpResult.ok(userVoList);
    }

    /**
     * 查询不在项目的成员列表
     * @param secretKey 项目密钥
     * @return 不在项目的成员列表
     */
    @GetMapping("/queryAllMemberNotInProject/{secretKey}")
    public Response<List<Map<String, Object>>> queryAllMemberNotInProject(@NotBlank(message = "无效参数") @PathVariable("secretKey") String secretKey) {
        List<Map<String, Object>> mapList = userService.queryAllMemberNotInProject(secretKey);
        return HttpResult.ok(mapList);
    }

    /**
     * 查询用户的角色
     * @param secretKey 项目密钥
     * @return 用户的角色
     */
    @GetMapping("/queryUserRole/{secretKey}")
    public Response<RoleVO> queryUserRole(@NotBlank(message = "无效参数") @PathVariable("secretKey") String secretKey) {
        GenericRole role = userService.queryUserRole(secretKey);
        RoleVO roleVO = new RoleVO();
        roleVO.setCode(role.getCode());
        roleVO.setName(role.getName());
        return HttpResult.ok(roleVO);
    }

    @PostMapping("/insertUser")
    public Response<String> insertUser(@Validated @RequestBody UserInsertDTO userInsertDTO) {
        String result = userService.insertUser(userInsertDTO);
        return HttpResult.okMessage(result);
    }

    @PostMapping("/updateUser")
    public Response<String> updateUser(@Validated @RequestBody UserUpdateDTO userUpdateDTO) {
        String result = userService.updateUser(userUpdateDTO);
        return HttpResult.okMessage(result);
    }

    // @PostMapping("/deleteUserById")
    // public Response<String> deleteGenericUserById(@Validated @RequestBody UserDeleteDTO userDeleteDTO) {
    //     String result = userService.deleteGenericUserById(userDeleteDTO);
    //     return HttpResult.okOrFail(null, result);
    // }

    @PostMapping("/updateUserRole")
    public Response<String> updateUserRole(@NotBlank(message = "无效参数") String username,
                                           @NotNull(message = "无效参数") Integer projectId,
                                           @NotBlank(message = "无效参数") String roleCode) {
        boolean isSuccess = userProjectService.updateUserRole(username, projectId, roleCode);
        if (!isSuccess) {
            return HttpResult.response(null, ResponseStatusEnum.FAIL, "更新角色失败");
        }
        return HttpResult.okMessage("更新角色成功");
    }

    @PostMapping("/insertUserProject/{projectId}")
    public Response<String> insertUserProject(@NotNull(message = "无效参数") @PathVariable("projectId") Integer projectId, @RequestBody List<UserInsertDTO> userInsertDTOList) {
        boolean isSuccess = userProjectService.insertUserProject(projectId, userInsertDTOList);
        if (!isSuccess) {
            return HttpResult.response(null, ResponseStatusEnum.FAIL, "添加成员失败");
        }
        return HttpResult.okMessage("添加成员成功");
    }

    /**
     * 移出项目成员
     * @param username 成员用户名
     * @param projectId 项目 id
     * @return 成功与否
     */
    @PostMapping("/removeOneMember")
    public Response<String> removeOneMember(@NotNull(message = "无效参数") String username, @NotNull(message = "无效参数") Integer projectId) {
        boolean isSuccess = userProjectService.removeOneMember(username, projectId);
        if (!isSuccess) {
            return HttpResult.response(null, ResponseStatusEnum.FAIL, "成员/个人离开项目失败");
        }
        return HttpResult.okMessage("成员/个人离开项目成功");
    }

    public List<UserVO> getUserVOList(List<GenericUser> userList) {
        List<UserVO> userVOList = new ArrayList<>();
        userList.forEach(user -> {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(user, vo);
            userVOList.add(vo);
        });
        return userVOList;
    }

}