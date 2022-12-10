package cn.youngkbt.generic.base.service.impl;

import java.util.List;

import cn.youngkbt.generic.base.service.ServiceColService;
import cn.youngkbt.generic.base.model.ServiceCol;
import cn.youngkbt.generic.base.mapper.ServiceColMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kele-Bingtang
 * @since 2022-12-03 22:45:22
 * @version 1.0
 */
@Service
public class ServiceColServiceImpl implements ServiceColService {

	@Autowired
	private ServiceColMapper serviceColMapper;

	@Override
	public List<ServiceCol> queryServiceColByConditions(QueryWrapper<ServiceCol> queryWrapper) {
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