package cn.youngkbt.generic.base.service;

import cn.youngkbt.generic.base.model.GenericProject;
import cn.youngkbt.generic.base.model.UserProject;
import cn.youngkbt.generic.vo.ConditionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
public interface GenericProjectService {

	/**
	 * 根据 ID 查询一条数据
	 * @param conditionVos 查询条件
	 * @return 根据 ID 查询出的实体对象
	 */
	public List<GenericProject> queryGenericProjectByConditions(List<ConditionVo> conditionVos);

	/**
	 * 根据项目的密钥查询项目信息
	 * @return 项目信息
	 */
	public GenericProject queryGenericOneProjectBySecretKey(String secretKey);
	
	
	/**
	 * 查询个人的所有数据
	 * @return 所有数据的实体对象集合
	 */
	public List<GenericProject> queryGenericProjectListOwner(UserProject userProject);
	
	/**
	 * 查询分页数据
	 * @return 所有数据的实体对象集合
	 */
	public IPage<GenericProject> queryGenericProjectListPages(IPage<GenericProject> page, GenericProject genericProject);

	/**
	 * 查询分页数据
	 * @param page 分页信息
	 * @param conditionVos 查询条件
	 * @return 所有数据的实体对象集合
	 */
	public IPage<GenericProject> queryGenericProjectConditionsPages(IPage<GenericProject> page, List<ConditionVo> conditionVos);

	/**
	 * 插入一条数据
	 * @param genericProject 实体对象
	 * @return 插入的数据
	 */
	public GenericProject insertGenericProject(GenericProject genericProject);
	
	/**
	 * 更新一条数据
	 * @param genericProject 实体对象
	 * @return 更新的数据
	 */
	public GenericProject updateGenericProject(GenericProject genericProject);
	
	/**
	 * 根据 ID 删除一条数据
	 * @param genericProject 实体对象
	 * @return 删除的数据
	 */
	public GenericProject deleteGenericProjectById(GenericProject genericProject);
	/**
	 * 根据 指定字段名 删除一条数据
	 * @param queryWrapper 字段名的集合
	 * @return 删除的行数
	 */
	public int deleteGenericProjectByColumns(QueryWrapper<GenericProject> queryWrapper);
}