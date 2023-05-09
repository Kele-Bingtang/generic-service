package cn.youngkbt.generic.service;

import cn.youngkbt.generic.common.dto.serviceCol.*;
import cn.youngkbt.generic.common.model.ServiceCol;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.sql.ResultSetMetaData;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
public interface ServiceColService extends IService<ServiceCol> {

	/**
	 * 查询所有数据
	 * @return 所有数据的实体对象集合
	 */
	public List<ServiceCol> queryServiceColList(ServiceColQueryDTO serviceColQueryDTO);

	public List<ServiceCol> queryServiceColListPage(IPage<ServiceCol> page, ServiceColQueryDTO serviceColQueryDTO);
	
	/**
	 * 插入一条数据
	 * @param serviceColInsertDTO 实体对象
	 * @return 插入的数据
	 */
	public String insertServiceCol(ServiceColInsertDTO serviceColInsertDTO);
	
	/**
	 * 更新一条数据
	 *
	 * @param serviceColUpdateDTO 实体对象
	 * @return 更新的数据
	 */
	public String updateServiceCol(ServiceColUpdateDTO serviceColUpdateDTO);

	String updateBatchServiceCol(ServiceColBatchUpdateDTO batchUpdateDTO);
	
	/**
	 * 根据 ID 删除一条数据
	 * @param serviceColDeleteDTO 实体对象
	 * @return 删除的数据
	 */
	public String deleteServiceColById(ServiceColDeleteDTO serviceColDeleteDTO);
	
	/**
	 * 根据 指定字段名 删除一条数据
	 * @param queryWrapper 查询条件
	 * @return 删除的行数
	 */
	public int deleteServiceColByColumns(QueryWrapper<ServiceCol> queryWrapper);

	/**
	 * 根据 ID集合 删除数据
	 * @param ids ID集合
	 * @return 删除的行数
	 */
	public int deleteServiceColByIds(List<Integer> ids);

	public boolean queryColumnInfoAndInsert(Integer serviceId, String selectSql);
	
	public Integer queryColumnInfoAndUpdate(Integer serviceId, String selectSql);
	public Integer queryColumnInfoAndDelete(Integer serviceId, String selectSql);

	public ResultSetMetaData executeSql(String sql);

}