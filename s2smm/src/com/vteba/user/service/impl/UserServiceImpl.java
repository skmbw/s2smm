package com.vteba.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vteba.user.dao.UserDao;
import com.vteba.user.model.User;
import com.vteba.user.model.UserBean;
import com.vteba.user.service.UserService;

/**
 * 用户service业务实现。
 * @author yinlei
 * 2014-2-25 上午11:26:29
 */
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired//将UserDao的实现注入进来，也可以使用setter方法注入
	private UserDao userDao;

	@Override
	public int countByExample(UserBean userBean) {
		return userDao.countByExample(userBean);
	}

	@Override
	public int deleteByExample(UserBean userBean) {
		return userDao.deleteByExample(userBean);
	}

	@Override
	public int deleteById(String id) {
		return userDao.deleteByPrimaryKey(id);
	}

	@Override
	public int saveAll(User record) {
		return userDao.insert(record);
	}

	@Override
	public int save(User record) {
		return userDao.insertSelective(record);
	}

	@Override
	public List<User> queryUserList(UserBean userBean) {
		return userDao.selectByExample(userBean);
	}

	@Override
	public User queryById(String id) {
		return userDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByExample(User record, UserBean userBean) {
		return userDao.updateByExampleSelective(record, userBean);
	}

	@Override
	public int updateAllByExample(User record, UserBean userBean) {
		return userDao.updateByExample(record, userBean);
	}

	@Override
	public int updateById(User record) {
		return userDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateAllById(User record) {
		return userDao.updateByPrimaryKey(record);
	}

}
