package com.vteba.user.action;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.vteba.sequence.SequenceGenerator;
import com.vteba.user.model.User;
import com.vteba.user.service.UserService;
import com.vteba.web.action.BaseAction;

/**
 * 用户Action.
 * @author yinlei
 * 2014-1-8 下午12:28:25
 */
public class UserAction extends BaseAction<User> {

	private static final long serialVersionUID = -711566328469110412L;
	private User model = new User();

	@Autowired
	private UserService userServiceImpl;
	
	@Autowired
	private SequenceGenerator sequenceGenerator;
	
	@Override
	public User getModel() {
		return model;
	}

	@Override
	public String initial() throws Exception {
		listResult = userServiceImpl.queryUserList(null);//传null，查询所有
		
		return SUCCESS;
	}
	
	public String add() throws Exception {
		
		return SUCCESS;
	}
	
	public String doAdd() throws Exception {
		model.setCreateDate(new Date());
		model.setId(sequenceGenerator.nextGUID());
		userServiceImpl.save(model);
		return SUCCESS;
	}
	
	public String detail() throws Exception {
		model = userServiceImpl.queryById(model.getId());
		return SUCCESS;
	}
	
	public String edit() throws Exception {
		model = userServiceImpl.queryById(model.getId());
		return SUCCESS;
	}
	
	public String update() throws Exception {
		userServiceImpl.updateById(model);
		return SUCCESS;
	}
	
	public void delete() throws Exception {
		userServiceImpl.deleteById(model.getId());
		renderText("删除成功！");
	}
}
