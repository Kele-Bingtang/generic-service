package cn.youngkbt.generic.base.controller;

import cn.youngkbt.generic.base.dto.user.UserInsertDTO;
import cn.youngkbt.generic.base.dto.user.UserUpdateDTO;
import cn.youngkbt.generic.base.service.UserProjectService;
import cn.youngkbt.generic.base.service.UserService;
import cn.youngkbt.generic.base.vo.RoleVO;
import cn.youngkbt.generic.base.vo.UserVO;
import cn.youngkbt.generic.http.HttpResult;
import cn.youngkbt.generic.http.Response;
import cn.youngkbt.generic.http.ResponseStatusEnum;
import cn.youngkbt.generic.utils.JwtTokenUtils;
import cn.youngkbt.generic.utils.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @PostMapping("/getUserInfo")
    public Response<UserVO> getUserInfo(@NotBlank(message = "无效参数") String token) {
        String username = JwtTokenUtils.getUsernameFromToken(token);
        if (StringUtils.isBlank(username)) {
            return HttpResult.okOrFail(null, "获取用户信息失败");
        }
        UserVO userVO = userService.findByUsername(username);
        userVO.setPassword("");
        return HttpResult.ok(userVO);
    }

    // @GetMapping("/queryUserListPages")
    //     public Response<List<UserVO>> queryGenericUserListPages(UserQueryDTO userQueryDTO,
    //                                                         @RequestParam(defaultValue = "1", required = false) Integer pageNo,
    //                                                         @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
    //     IPage<GenericUser> page = new Page<>(pageNo, pageSize);
    //     List<UserVO> userVOList = userService.queryGenericUserListPages(page, userQueryDTO);
    //     return HttpResult.ok(userVOList);
    // }

    @GetMapping("/queryMemberInProject/{secretKey}")
    public Response<List<UserVO>> queryGenericMemberInProject(@NotBlank(message = "无效参数") @PathVariable("secretKey") String secretKey,
                                                              @RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                              @RequestParam(defaultValue = "50", required = false) Integer pageSize) {
        List<UserVO> userVOList = userService.queryMemberInProject(secretKey, pageNo, pageSize);
        return HttpResult.ok(userVOList);
    }

    @GetMapping("/queryAllMemberNotInProject/{secretKey}")
    public Response<List<Map<String, Object>>> queryAllMemberNotInProject(@NotBlank(message = "无效参数") @PathVariable("secretKey") String secretKey) {
        List<Map<String, Object>> mapList = userService.queryAllMemberNotInProject(secretKey);
        return HttpResult.ok(mapList);
    }

    @GetMapping("/queryUserRole/{secretKey}")
    public Response<RoleVO> queryUserRole(@NotBlank(message = "无效参数") @PathVariable("secretKey") String secretKey) {
        RoleVO roleVO = userService.queryUserRole(secretKey);
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
        boolean isSuccess = userProjectService.updateGenericUserRole(username, projectId, roleCode);
        if (!isSuccess) {
            return HttpResult.response(null, ResponseStatusEnum.FAIL, "更新角色失败");
        }
        return HttpResult.okMessage("更新角色成功");
    }

    @PostMapping("/insertUserProject/{projectId}")
    public Response<String> insertUserProject(@NotNull(message = "无效参数") @PathVariable("projectId") Integer projectId, @RequestBody List<UserInsertDTO> userInsertDTOList) {
        boolean isSuccess = userProjectService.insertGenericUserProject(projectId, userInsertDTOList);
        if (!isSuccess) {
            return HttpResult.response(null, ResponseStatusEnum.FAIL, "添加成员失败");
        }
        return HttpResult.okMessage("添加成员成功");
    }

    @PostMapping("/removeOneMember")
    public Response<String> removeOneMember(@NotNull(message = "无效参数") String username, @NotNull(message = "无效参数") Integer projectId) {
        boolean isSuccess = userProjectService.removeOneMember(username, projectId);
        if (!isSuccess) {
            return HttpResult.response(null, ResponseStatusEnum.FAIL, "成员/个人离开项目失败");
        }
        return HttpResult.okMessage("成员/个人离开项目成功");
    }

}