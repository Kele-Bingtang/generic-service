package cn.youngkbt.generic.service;

import cn.youngkbt.generic.common.dto.project.ProjectDeleteDTO;
import cn.youngkbt.generic.common.dto.project.ProjectInsertDTO;
import cn.youngkbt.generic.common.dto.project.ProjectQueryOwnerDTO;
import cn.youngkbt.generic.common.dto.project.ProjectUpdateDTO;
import cn.youngkbt.generic.common.model.GenericProject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
public interface ProjectService {

	/**
	 * 根据项目的密钥查询项目信息
	 * @return 项目信息
	 */
	public GenericProject queryOneProjectBySecretKey(String secretKey);
	
	
	/**
	 * 查询个人的所有数据
	 * @return 所有数据的实体对象集合
	 */
	public List<GenericProject> queryProjectListOwner(ProjectQueryOwnerDTO projectQueryOwnerDTO);
	
	/**
	 * 查询分页数据
	 * @return 所有数据的实体对象集合
	 */
	public List<GenericProject> queryProjectListPages(IPage<GenericProject> page, ProjectQueryOwnerDTO projectQueryOwnerDTO);

	/**
	 * 插入一条数据
	 * @param projectInsertDTO 实体对象
	 * @return 插入的数据
	 */
	public String insertProject(ProjectInsertDTO projectInsertDTO);
	
	/**
	 * 更新一条数据
	 * @param projectUpdateDTO 实体对象
	 * @return 更新的数据
	 */
	public String updateProject(ProjectUpdateDTO projectUpdateDTO);
	
	/**
	 * 根据 ID 删除一条数据
	 * @param projectDeleteDTO 实体对象
	 * @return 删除的数据
	 */
	public String deleteProjectById(ProjectDeleteDTO projectDeleteDTO);
	/**
	 * 根据 指定字段名 删除一条数据
	 * @param queryWrapper 字段名的集合
	 * @return 删除的行数
	 */
	public int deleteProjectByColumns(QueryWrapper<GenericProject> queryWrapper);

	/**
	 * 查询数据库列表
	 * @param databaseName 指定的数据库
	 * @return 数据库列表
	 */
	public List<String> queryDatabaseName(String databaseName);
}