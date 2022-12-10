package cn.youngkbt.generic.base.service.impl;

import java.util.List;

import cn.youngkbt.generic.base.service.GenericReportService;
import cn.youngkbt.generic.base.model.GenericReport;
import cn.youngkbt.generic.base.mapper.GenericReportMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kele-Bingtang
 * @since 2022-12-03 22:45:22
 * @version 1.0
 */
@Service
public class GenericReportServiceImpl implements GenericReportService {

	@Autowired
	private GenericReportMapper genericReportMapper;

	@Override
	public List<GenericReport> queryGenericReportByCondition(QueryWrapper<GenericReport> queryWrapper) {
		return genericReportMapper.selectList(queryWrapper);
	}

	@Override
	public List<GenericReport> queryGenericReportList(GenericReport genericReport) {
		QueryWrapper<GenericReport> queryWrapper = new QueryWrapper<>();
		// 如果 genericCategory 没有数据，则返回全部数据
		queryWrapper.setEntity(genericReport);
		return genericReportMapper.selectList(queryWrapper);
	}

	@Override
	public IPage<GenericReport> queryGenericReportPages(IPage<GenericReport> page, QueryWrapper<GenericReport> queryWrapper) {
		return genericReportMapper.selectPage(page, queryWrapper);
	}

	@Override
	public GenericReport insertGenericReport(GenericReport genericReport) {
		int i = genericReportMapper.insert(genericReport);
		if(i == 0) {
			return null;
		}
		return genericReport;
	}

	@Override
	public GenericReport updateGenericReport(GenericReport genericReport) {
		int i = genericReportMapper.updateById(genericReport);
		if(i == 0) {
			return null;
		}
		return genericReport;
	}

	@Override
	public GenericReport deleteGenericReportById(GenericReport genericReport) {
		int i = genericReportMapper.deleteById(genericReport);
		if(i == 0) {
			return null;
		}
		return genericReport;
	}

	@Override
	public int deleteGenericReportByColumns(QueryWrapper<GenericReport> queryWrapper) {
		return genericReportMapper.delete(queryWrapper);
	}

	@Override
	public int deleteGenericReportByIds(List<Integer> ids) {
		return genericReportMapper.deleteBatchIds(ids);
	}

}