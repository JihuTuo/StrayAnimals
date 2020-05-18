package com.hsshy.beam.admin.modular.sys.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hsshy.beam.web.modular.base.entity.DataEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 管理员表
 * 
 * @author hs
 * @email 457030599@qq.com
 * @date 2018-10-07 18:03:20
 */
@NoArgsConstructor
@Data
@TableName("sys_user")
public class User extends DataEntity<Long> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 头像
	 */
	private String avatar;
	/**
	 * 账号
	 */
	private String account;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * md5密码盐
	 */
	private String salt;
	/**
	 * 名字
	 */
	private String name;
	/**
	 * 部门ID
	 */
	private Long deptId;
	/**
	 * 生日
	 */
	private Date birthday;
	/**
	 * 性别（1：男 2：女）
	 */
	private Integer sex;
	/**
	 * 电子邮件
	 */
	private String email;
	/**
	 * 电话
	 */
	private String phone;
	/**
	 * 状态(1：启用  2：冻结  3：删除）
	 */
	private Integer status;
	/**
	 * 保留字段
	 */
	private Integer version;

	@TableField(exist = false)
	private String deptName;
	//查询
	@TableField(exist = false)
	private String deptIds;

	@TableField(exist = false)
	private List<Long> roleIds;

	@TableField(exist = false)
	private String sexName;

	@TableField(exist = false)
	private String statusName;


	@Override
	protected Serializable pkVal() {
        return this.id;
	}
}
