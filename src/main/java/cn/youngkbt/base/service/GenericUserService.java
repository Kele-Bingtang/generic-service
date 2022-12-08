package cn.youngkbt.base.service;

import java.util.List;

import cn.youngkbt.base.model.GenericUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author Kele-Bingtang
 * @since 2022-12-03 22:45:22
 * @version 1.0
 */
public interface GenericUserService {

	/**
	 * 根据 ID 查询一条数据
	 * @param queryWrapper 查询条件
	 * @return 根据 ID 查询出的实体对象
	 */
	public List<GenericUser> queryGenericUserByConditions(QueryWrapper<GenericUser> queryWrapper);
	
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
	 * 查询分页数据
	 * @param page 分页信息
	 * @return 所有数据的实体对象集合
	 */
	public IPage<GenericUser> queryGenericUserConditionsPages(IPage<GenericUser> page, QueryWrapper<GenericUser> queryWrapper);

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