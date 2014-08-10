package com.vteba.web.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 公共action，提供一些常用方法及变量定义。
 * @author yinlei 
 * date 2012-5-5 下午9:37:30
 */
public abstract class  BaseAction<T> extends ActionSupport implements ModelDriven<T> {
	private static final long serialVersionUID = 1740766986180297938L;
	private static final Random RANDOM = new Random();
	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected T entity;
	/**
	 * 存放任意对象list到view中
	 */
	protected List<?> list;
	/**
	 * 存放action的泛型参数的对象list到view中
	 */
	protected List<T> listResult;
	/**
	 * 封装页面的数据，即view到action中
	 */
	protected List<T> formList = new ArrayList<T>();
	/**
	 * 存放多个对象到view中
	 */
	protected Map<String, Object> map;
	/**
	 * 存放多个list到view中
	 */
	protected Map<String, List<T>> mapResult;
	/**
	 * 存放对象数组到view中
	 */
	protected Object[] objectResult;

	/**
	 * 当前action路径
	 */
	protected String currentActionPath = ServletActionContext.getRequest().getServletPath();
	/**
	 * 初始化，减少action method 数目，有利于控制跳转
	 */
	private boolean init;
	
	public static final String DETAIL = "detail";
	public static final String LIST = "list";
	
	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public List<T> getListResult() {
		return listResult;
	}

	public void setListResult(List<T> listResult) {
		this.listResult = listResult;
	}

	public List<T> getFormList() {
		return formList;
	}

	public void setFormList(List<T> formList) {
		this.formList = formList;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public Map<String, List<T>> getMapResult() {
		return mapResult;
	}

	public void setMapResult(Map<String, List<T>> mapResult) {
		this.mapResult = mapResult;
	}

	public Object[] getObjectResult() {
		return objectResult;
	}

	public void setObjectResult(Object[] objectResult) {
		this.objectResult = objectResult;
	}

	public String getCurrentActionPath() {
		return currentActionPath;
	}

	public void setCurrentActionPath(String currentActionPath) {
		this.currentActionPath = currentActionPath;
	}
	
	/**
	 * the jsp page initial。
	 */
	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public String execute() throws Exception{
		return this.initial();
	}
	
	/**
	 * action default method，常作为查询使用，或初始化。
	 */
	public abstract String initial() throws Exception;
	
	/**
	 * 将value保存到request。
	 * @param name
	 * @param value
	 * @author yinlei
	 * date 2012-7-5 下午9:38:26
	 */
	protected void setAttributeToRequest(String name, Object value) {
		ActionContext ctx = ActionContext.getContext();
		ctx.put(name, value);
	}
	
	/**
	 * 取得request中参数值。
	 * @param name
	 * @author yinlei
	 * date 2012-7-5 下午10:19:33
	 */
	protected Object getRequestParamater(String name) {
		ActionContext ctx = ActionContext.getContext();
		return ctx.get(name);
	}
	
	/**
	 * 将value保存到session。
	 * @param name
	 * @param value
	 * @author yinlei
	 * date 2012-7-5 下午10:05:32
	 */
	protected void setAttributeToSession(String name, Object value) {
		ActionContext ctx = ActionContext.getContext();
		Map<String, Object> session = ctx.getSession();
		session.put(name, value);
	}
	
	/**
	 * 取得session中参数值。
	 * @param name
	 * @author yinlei
	 * date 2012-7-5 下午10:21:44
	 */
	protected Object getSessionParameter(String name) {
		Map<String, Object> session = ActionContext.getContext().getSession();
		return session.get(name);
	}
	
	/**
	 * 产生全局唯一的ID。
	 * @author yinlei
	 * date 2012-7-5 下午9:35:37
	 */
	protected String generateGUID(){
        return (new BigInteger(165, RANDOM)).toString(36).toUpperCase();
    }
	
	public HttpServletResponse getHttpServletResponse() {
		return ServletActionContext.getResponse();
	}
	
	public HttpServletRequest getHttpServletRequest() {
		return ServletActionContext.getRequest();
	}
	
	public HttpSession getHttpSession() {
		return ServletActionContext.getRequest().getSession();
	}
	
	/**
	 * 向客户端输出指定格式的文档。
	 * @param response 		响应
	 * @param text   		要发的内容
	 * @param contentType   发送的格式
	 */
	public void response(HttpServletResponse response, String text, String contentType) {
		try {
			response.setContentType(contentType);
			response.getWriter().write(text);
		} catch (IOException e) {
			logger.info("向客户端输出时发生异常 。content=[" + text + "]", e);
		}
	}

	/**
	 * 直接输出json。
	 * @param text 要输出的json内容
	 */
	public void renderJson(String json) {
		response(ServletActionContext.getResponse(), json, "application/json;charset=UTF-8");
	}

	/**
	 * 直接输出纯HTML。
	 * @param text 要输出的html内容
	 */
	public void renderHtml(String text) {
		response(ServletActionContext.getResponse(), text, "text/html;charset=UTF-8");
	}

	/**
	 * 直接输出纯字符串。约定：返回1，刷新页面；返回文本，alert。
	 * @param text 要输出的内容
	 */
	public void renderText(String text) {
		response(ServletActionContext.getResponse(), text, "text/plain;charset=UTF-8");
	}
	
	public void renderSimple(String text) {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			//设置页面不缓存
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print(text);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("响应html代码错误[{}]", text);
		}
	}
}
