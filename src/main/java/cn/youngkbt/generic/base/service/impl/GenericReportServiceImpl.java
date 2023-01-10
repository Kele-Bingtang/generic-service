package cn.youngkbt.generic.base.service.impl;

import cn.youngkbt.generic.base.mapper.GenericReportMapper;
import cn.youngkbt.generic.base.model.GenericReport;
import cn.youngkbt.generic.base.service.GenericReportService;
import cn.youngkbt.generic.utils.SearchUtils;
import cn.youngkbt.generic.vo.ConditionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
@Service
public class GenericReportServiceImpl implements GenericReportService {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Resource
	private GenericReportMapper genericReportMapper;

	@Override
	public List<GenericReport> queryGenericReportByCondition(List<ConditionVo> conditionVos) {
		QueryWrapper<GenericReport> queryWrapper = SearchUtils.parseWhereSql(conditionVos, GenericReport.class);
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
	public GenericReport queryOneGenericReport(GenericReport genericReport) {
		QueryWrapper<GenericReport> queryWrapper = new QueryWrapper<>();
		queryWrapper.setEntity(genericReport);
		return genericReportMapper.selectOne(queryWrapper);
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