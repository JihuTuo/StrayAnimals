package com.hsshy.beam.web.modular.base.controller;
import com.hsshy.beam.common.utils.support.HttpKit;
import com.hsshy.beam.common.utils.redis.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseBeanController {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());


	@Autowired
	private RedisUtil redisUtil;

	protected String getPara(String name) {
		return HttpKit.getRequest().getParameter(name);
	}

	protected void setAttr(String name, Object value) {
		HttpKit.getRequest().setAttribute(name, value);
	}

	public Object getAttr(String name) {
		return HttpKit.getRequest().getAttribute(name);
	}

	/**
	 * 通过请求头中的用户utoken，获取用户id
	 */
	public Long getUserId(){
		String uid = (String) getAttr("uid");
		return Long.parseLong(uid);
	}

	/**
	 * 小程序登录后的sessionKey
	 * @param
	 * @return
	 */
	public String getUserSessionKey(){
		String sessionKey = (String) redisUtil.get("cow-draw:sessionKey:"+getUserId());
		return sessionKey;
	}


}
