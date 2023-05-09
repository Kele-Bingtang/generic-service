package cn.youngkbt.generic.service;

import cn.youngkbt.generic.common.dto.report.ReportQueryDTO;
import cn.youngkbt.generic.common.dto.report.ReportUpdateDTO;
import cn.youngkbt.generic.common.model.GenericReport;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
public interface ReportService {

	/**
	 * 查询所有数据
	 * @return 所有数据的实体对象集合
	 */
	public List<GenericReport> queryReportList(ReportQueryDTO reportQueryDTO);

	public GenericReport queryOneReport(ReportQueryDTO reportQueryDTO);

	/**
	 * 插入一条数据
	 * @param genericReport 实体对象
	 * @return 插入的数据
	 */
	public String insertReport(GenericReport genericReport);
	
	/**
	 * 更新一条数据
	 * @param reportUpdateDTO 实体对象
	 * @return 更新的数据
	 */
	public String updateReport(ReportUpdateDTO reportUpdateDTO);
	
	/**
	 * 根据 指定字段名 删除一条数据
	 * @param queryWrapper 字段名的集合
	 * @return 删除的行数
	 */
	public int deleteReportByColumns(QueryWrapper<GenericReport> queryWrapper);
	
	/**
	 * 根据 ID集合 删除数据
	 * @param ids ID集合
	 * @return 删除的行数
	 */
	public int deleteReportByIds(List<Integer> ids);
	
}