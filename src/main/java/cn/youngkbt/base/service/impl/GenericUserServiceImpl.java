package cn.youngkbt.base.service.impl;

import java.util.List;

import cn.youngkbt.base.model.GenericProject;
import cn.youngkbt.base.model.GenericService;
import cn.youngkbt.base.service.GenericProjectService;
import cn.youngkbt.base.service.GenericUserService;
import cn.youngkbt.base.model.GenericUser;
import cn.youngkbt.base.mapper.GenericUserMapper;
import cn.youngkbt.exception.ConditionSqlException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kele-Bingtang
 * @since 2022-12-03 22:45:22
 * @version 1.0
 */
@Service
public class GenericUserServiceImpl implements GenericUserService {

	@Autowired
	private GenericUserMapper genericUserMapper;
	@Autowired
	private GenericProjectService genericProjectService;

	@Override
	public List<GenericUser> queryGenericUserByConditions(QueryWrapper<GenericUser> queryWrapper) {
		try {
			return genericUserMapper.selectList(queryWrapper);
		} catch (Exception e) {
			throw new ConditionSqlException();
		}
	}

	@Override
	public List<GenericUser> queryGenericUserList(GenericUser genericUser) {
		QueryWrapper<GenericUser> queryWrapper = new QueryWrapper<>();
		// 如果 genericCategory 没有数据，则返回全部数据
		queryWrapper.setEntity(genericUser);
		return genericUserMapper.selectList(queryWrapper);
	}

	@Override
	public IPage<GenericUser> queryGenericUserListPages(IPage<GenericUser> page, GenericUser genericUser) {
		QueryWrapper<GenericUser> queryWrapper = new QueryWrapper<>();
		// 如果 genericCategory 没有数据，则返回全部数据
		queryWrapper.setEntity(genericUser);
		try {
			return genericUserMapper.selectPage(page, queryWrapper);
		} catch (Exception e) {
			throw new ConditionSqlException();
		}
	}

	@Override
	public IPage<GenericUser> queryGenericUserConditionsPages(IPage<GenericUser> page, QueryWrapper<GenericUser> queryWrapper) {
		try {
			return genericUserMapper.selectPage(page, queryWrapper);
		} catch (Exception e) {
			throw new ConditionSqlException();
		}
	}

	@Override
	public GenericUser insertGenericUser(GenericUser genericUser) {
		int i = genericUserMapper.insert(genericUser);
		if(i == 0) {
			return null;
		}
		return genericUser;
	}

	@Override
	public GenericUser updateGenericUser(GenericUser genericUser) {
		int i = genericUserMapper.updateById(genericUser);
		if(i == 0) {
			return null;
		}
		return genericUser;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public GenericUser deleteGenericUserById(GenericUser genericUser) {
		// 删除用户下的所有项目
		QueryWrapper<GenericProject> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("create_user", genericUser.getId());
		genericProjectService.deleteGenericProjectByColumns(queryWrapper);
		int i = genericUserMapper.deleteById(genericUser);
		if(i == 0) {
			return null;
		}
		return genericUser;
	}

}