package com.hsshy.beam.web.modular.base.entity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.hsshy.beam.common.constant.DataBaseConstant;

import java.util.Date;

/**
 * 数据Entity类
 * 
 * @author hs
 * @date 2018-09-27
 *
 */
public abstract class DataEntity<ID> extends AbstractEntity<ID> {


	@TableField(value = "create_by",  fill = FieldFill.INSERT)
	private Long createBy; // 创建者
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime; // 创建日期
	@TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
	private Long updateBy; // 更新者
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime; // 更新日期
	@TableField(value = "del_flag", fill = FieldFill.INSERT)
	private Integer delFlag; // 删除标记（0：正常；1：删除 ）

	public DataEntity() {
		super();
		this.delFlag = DataBaseConstant.DEL_FLAG_NORMAL;
	}



	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}



	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}



	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
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
