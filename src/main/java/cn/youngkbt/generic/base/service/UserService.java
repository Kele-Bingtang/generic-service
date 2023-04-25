package cn.youngkbt.generic.base.service;

import cn.youngkbt.generic.base.dto.user.UserDeleteDTO;
import cn.youngkbt.generic.base.dto.user.UserInsertDTO;
import cn.youngkbt.generic.base.dto.user.UserQueryDTO;
import cn.youngkbt.generic.base.dto.user.UserUpdateDTO;
import cn.youngkbt.generic.base.model.GenericUser;
import cn.youngkbt.generic.base.vo.RoleVO;
import cn.youngkbt.generic.base.vo.UserVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
public interface UserService {
	
	/**
	 * 查询所有数据
	 * @return 所有数据的实体对象集合
	 */
	public List<Map<String, Object>> queryAllMemberNotInProject(String secretKey);
	
	/**
	 * 查询所有数据
	 * @return 所有数据的实体对象集合
	 */
	public List<UserVO> queryUserListPages(IPage<GenericUser> page, UserQueryDTO userQueryDTO);

	/**
	 * 通过用户名查询数据
	 */
	public UserVO findByUsername(String username);

	/**
	 * 查询用户所在的项目角色
	 */
	public RoleVO queryUserRole(String secretKey);

	/**
	 * 查询分页的项目成员数据
	 * @param secretKey 项目密钥
	 * @param pageNo 当前页
	 * @param pageSize 一页显示多少数据
	 * @return 所有数据的实体对象集合
	 */
	public List<UserVO> queryMemberInProject(String secretKey, Integer pageNo, Integer pageSize);

	/**
	 * 插入一条数据
	 * @param userInsertDTO 实体对象
	 * @return 插入的数据
	 */
	public String insertUser(UserInsertDTO userInsertDTO);
	
	/**
	 * 更新一条数据
	 * @param userUpdateDTO 实体对象
	 * @return 更新的数据
	 */
	public String updateUser(UserUpdateDTO userUpdateDTO);
	
	/**
	 * 根据 ID 删除一条数据
	 * @param userDeleteDTO 实体对象
	 * @return 删除的数据
	 */
	public String deleteUserById(UserDeleteDTO userDeleteDTO);

}