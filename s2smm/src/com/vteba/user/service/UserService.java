package com.vteba.user.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.vteba.user.model.User;
import com.vteba.user.model.UserBean;

/**
 * 用户相关的业务service接口。
 * @author yinlei
 * 2014-2-25 上午11:24:51
 */
public interface UserService {

	/**
     * 根据Criteria所携带条件进行count计数。
     * @param userBean 查询条件
     * @date 2014-02-27 17:59:34
     */
    int countByExample(UserBean userBean);

    /**
     * 根据Criteria所携带条件删除记录。
     * @param userBean 查询条件
     * @date 2014-02-27 17:59:34
     */
    int deleteByExample(UserBean userBean);

    /**
	 * 根据主键删除记录。
	 * @param id 主键id
     * @date 2014-02-27 17:59:34
	 */
	public int deleteById(String id);
    
    /**
     * 插入记录，所有字段都不能为空。
     * @param record 要被保存的数据
     * @date 2014-02-27 17:59:34
     */
    int saveAll(User record);

    /**
     * 插入记录，只有非空字段才会插入到数据库。
     * @param record 要被保存的数据
     * @date 2014-02-27 17:59:34
     */
    int save(User record);

    /**
     * 根据Criteria所携带条件查询数据，不含BLOB字段。
     * @param userBean 查询条件
     * @date 2014-02-27 17:59:34
     */
    List<User> queryUserList(UserBean userBean);

    /**
     * 根据主键查询数据。
     * @param id 主键
     * @date 2014-02-27 17:59:34
     */
    User queryById(String id);

    /**
     * 根据Criteria所携带条件更新指定字段。
     * @param record 要更新的数据
     * @param userBean update的where条件
     * @date 2014-02-27 17:59:34
     */
    int updateByExample(@Param("record") User record, @Param("example") UserBean userBean);

    /**
     * 根据Criteria所携带条件更新所有字段，不含BLOB字段。
     * @param record 要更新的数据
     * @param userBean update的where条件
     * @date 2014-02-27 17:59:34
     */
    int updateAllByExample(@Param("record") User record, @Param("example") UserBean userBean);

    /**
     * 根据主键更新指定字段的数据。
     * @param record 要更新的数据，含有Id
     * @date 2014-02-27 17:59:34
     */
    int updateById(User record);

    /**
     * 根据主键更新所有字段的数据，不含BLOB字段。
     * @param record 要更新的数据，含有Id
     * @date 2014-02-27 17:59:34
     */
    int updateAllById(User record);
}
