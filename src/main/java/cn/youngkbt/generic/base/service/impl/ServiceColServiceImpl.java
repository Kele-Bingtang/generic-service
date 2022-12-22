package cn.youngkbt.generic.base.service.impl;

import cn.youngkbt.generic.base.mapper.ServiceColMapper;
import cn.youngkbt.generic.base.model.ServiceCol;
import cn.youngkbt.generic.base.service.ServiceColService;
import cn.youngkbt.generic.utils.SearchUtils;
import cn.youngkbt.generic.vo.ConditionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
public class ServiceColServiceImpl implements ServiceColService {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Resource
	private ServiceColMapper serviceColMapper;

	@Override
	public List<ServiceCol> queryServiceColByConditions(List<ConditionVo> conditionVos) {
		QueryWrapper<ServiceCol> queryWrapper = SearchUtils.parseWhereSql(conditionVos, ServiceCol.class);
		return serviceColMapper.selectList(queryWrapper);
	}

	@Override
	public List<ServiceCol> queryServiceColList(ServiceCol serviceCol) {
		QueryWrapper<ServiceCol> queryWrapper = new QueryWrapper<>();
		// 如果 genericCategory 没有数据，则返回全部数据
		queryWrapper.setEntity(serviceCol);
		return serviceColMapper.selectList(queryWrapper);
	}

	@Override
	public ServiceCol insertServiceCol(ServiceCol serviceCol) {
		int i = serviceColMapper.insert(serviceCol);
		if(i == 0) {
			return null;
		}
		return serviceCol;
	}

	@Override
	public ServiceCol updateServiceCol(ServiceCol serviceCol) {
		int i = serviceColMapper.updateById(serviceCol);
		if(i == 0) {
			return null;
		}
		return serviceCol;
	}

	@Override
	public ServiceCol deleteServiceColById(ServiceCol serviceCol) {
		int i = serviceColMapper.deleteById(serviceCol);
		if(i == 0) {
			return null;
		}
		return serviceCol;
	}

	@Override
	public int deleteServiceColByColumns(QueryWrapper<ServiceCol> queryWrapper) {
		return serviceColMapper.delete(queryWrapper);
	}

	@Override
	public int deleteServiceColByIds(List<Integer> ids) {
		return serviceColMapper.deleteBatchIds(ids);
	}

}