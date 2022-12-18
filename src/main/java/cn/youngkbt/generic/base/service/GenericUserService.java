package cn.youngkbt.generic.base.service;

import cn.youngkbt.generic.base.model.GenericUser;
import cn.youngkbt.generic.vo.ConditionVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
public interface GenericUserService {
	
	/**
	 * 根据 ID 查询一条数据
	 * @param conditionVos 查询条件
	 * @return 根据 ID 查询出的实体对象
	 */
	public List<GenericUser> queryGenericUserByConditions(List<ConditionVo> conditionVos);
	
	/**
	 * 查询所有数据
	 * @return 所有数据的实体对象集合
	 */
	public List<GenericUser> queryGenericUserList(GenericUser genericUser);
	
	/**
	 * 查询所有数据
	 * @return 所有数据的实体对象集合
	 */
	public IPage<GenericUser> queryGenericUserListPages(IPage<GenericUser> page, GenericUser genericUser);

	/**
	 * 通过用户名查询数据
	 */
	public GenericUser findByUsername(String username);

	/**
	 * 查询分页数据
	 * @param page 分页信息
	 * @return 所有数据的实体对象集合
	 */
	public IPage<GenericUser> queryGenericUserConditionsPages(IPage<GenericUser> page, List<ConditionVo> conditionVos);

	/**
	 * 插入一条数据
	 * @param genericUser 实体对象
	 * @return 插入的数据
	 */
	public GenericUser insertGenericUser(GenericUser genericUser);
	
	/**
	 * 更新一条数据
	 * @param genericUser 实体对象
	 * @return 更新的数据
	 */
	public GenericUser updateGenericUser(GenericUser genericUser);
	
	/**
	 * 根据 ID 删除一条数据
	 * @param genericUser 实体对象
	 * @return 删除的数据
	 */
	public GenericUser deleteGenericUserById(GenericUser genericUser);

}