package com.hsshy.beam.web.modular.base.entity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

/**
 * 数据Entity类
 * 
 * @author hs
 * @date 2018-09-27
 *
 */
public abstract class RestEntity<ID> extends AbstractEntity<ID> {


	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime; // 创建日期
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime; // 更新日期

	public RestEntity() {
		super();

	}


	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
