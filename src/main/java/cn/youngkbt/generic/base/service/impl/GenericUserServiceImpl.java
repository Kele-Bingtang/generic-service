package cn.youngkbt.generic.base.service.impl;

import cn.youngkbt.generic.base.mapper.GenericUserMapper;
import cn.youngkbt.generic.base.mapper.UserProjectMapper;
import cn.youngkbt.generic.base.model.GenericProject;
import cn.youngkbt.generic.base.model.GenericUser;
import cn.youngkbt.generic.base.service.GenericProjectService;
import cn.youngkbt.generic.base.service.GenericUserService;
import cn.youngkbt.generic.exception.ConditionSqlException;
import cn.youngkbt.generic.utils.SearchUtils;
import cn.youngkbt.generic.vo.ConditionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
@Service
public class GenericUserServiceImpl implements GenericUserService {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Resource
	private GenericUserMapper genericUserMapper;
	@Resource
	private GenericProjectService genericProjectService;
	@Resource
	private UserProjectMapper userProjectMapper;

	@Override
	public List<GenericUser> queryGenericUserByConditions(List<ConditionVo> conditionVos) {
		QueryWrapper<GenericUser> queryWrapper = SearchUtils.parseWhereSql(conditionVos, GenericUser.class);
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
	public GenericUser findByUsername(String username) {
		QueryWrapper<GenericUser> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("username", username);
		return genericUserMapper.selectOne(queryWrapper);
	}

	@Override
	public IPage<GenericUser> queryGenericUserConditionsPages(IPage<GenericUser> page, List<ConditionVo> conditionVos) {
		QueryWrapper<GenericUser> queryWrapper = SearchUtils.parseWhereSql(conditionVos, GenericUser.class);
		try {
			return genericUserMapper.selectPage(page, queryWrapper);
		} catch (Exception e) {
			throw new ConditionSqlException();
		}
	}

	@Override
	public List<GenericUser> queryGenericMemberInProject(String secretKey, Integer pageNo, Integer pageSize) {
		Integer currentPage = (pageNo - 1) * pageSize;
		return userProjectMapper.queryGenericMemberInProject(secretKey, currentPage, pageSize);
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