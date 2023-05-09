package cn.youngkbt.generic.service;

import cn.youngkbt.generic.common.dto.service.ServiceDeleteDTO;
import cn.youngkbt.generic.common.dto.service.ServiceInsertDTO;
import cn.youngkbt.generic.common.dto.service.ServiceQueryDTO;
import cn.youngkbt.generic.common.dto.service.ServiceUpdateDTO;
import cn.youngkbt.generic.common.dto.service.ServiceResultDTO;
import cn.youngkbt.generic.common.model.GenericService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
public interface ServiceService {

	/**
	 * 根据 ID 查询一条数据
	 * @param serviceId ID
	 * @return 根据 ID 查询出的实体对象
	 */
	public GenericService queryServiceById(Integer serviceId);
	
	public GenericService queryServiceBySecretKeyAndUrl(String secretKey, String serviceUrl);

	public GenericService queryOneGenericService(ServiceQueryDTO serviceQueryDTO);
	
	/**
	 * 查询所有数据
	 * @return 所有数据的实体对象集合
	 */
	public List<GenericService> queryServiceList(ServiceQueryDTO serviceQueryDTO);
	
	/**
	 * 查询分页数据
	 * @return 所有数据的实体对象集合
	 */
	public List<GenericService> queryServiceListPages(IPage<GenericService> page, ServiceQueryDTO serviceQueryDTO);

	public List<ServiceResultDTO> queryServiceAndServiceColList(Integer projectId);
	
	/**
	 * 插入一条数据
	 * @param serviceInsertDTO 实体对象
	 * @return 插入的数据
	 */
	public String insertService(ServiceInsertDTO serviceInsertDTO);
	
	/**
	 * 更新一条数据
	 * @param serviceUpdateDTO 实体对象
	 * @return 更新的数据
	 */
	public String updateService(ServiceUpdateDTO serviceUpdateDTO);
	
	/**
	 * 根据 ID 删除一条数据
	 * @param serviceDeleteDTO 实体对象
	 * @return 删除的数据
	 */
	public String deleteServiceById(ServiceDeleteDTO serviceDeleteDTO);

	/**
	 * 根据 指定字段名 删除一条数据
	 * @param queryWrapper 字段名的集合
	 * @return 删除的行数
	 */
	public int deleteServiceByColumns(QueryWrapper<GenericService> queryWrapper);

	/**
	 * 根据 ID集合 删除数据
	 * @param ids ID集合
	 * @return 删除的行数
	 */
	public int deleteServiceByIds(List<Integer> ids);

    public Map<String, List<String>> queryTableViewNameList(String databaseName);

	/**
	 * 验证 SQL 是否生效
	 * @param sql sql 语句
	 * @return 查询的数据长度
	 */
	public Integer verifySql(String sql);
}