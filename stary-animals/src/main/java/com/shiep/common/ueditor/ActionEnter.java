package com.shiep.common.ueditor;

import com.alibaba.fastjson.JSONException;
import com.shiep.common.ueditor.define.ActionMap;
import com.shiep.common.ueditor.define.AppInfo;
import com.shiep.common.ueditor.define.BaseState;
import com.shiep.common.ueditor.define.State;
import com.shiep.common.ueditor.hunter.FileManager;
import com.shiep.common.ueditor.hunter.ImageHunter;
import com.shiep.common.ueditor.upload.Uploader;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;



public class ActionEnter {
	
	private HttpServletRequest request = null;
	
	private String rootPath = null;
	private String contextPath = null;
	
	private String actionType = null;
	
	private ConfigManager configManager = null;

	public ActionEnter ( HttpServletRequest request, String rootPath) {
		
		this.request = request;
		this.rootPath = rootPath;
		this.actionType = request.getParameter( "action" );
		this.contextPath = request.getContextPath();
		this.configManager = ConfigManager.getInstance( this.rootPath, this.contextPath, request.getRequestURI() );
		System.out.println(this.actionType + this.contextPath + this.configManager + "----") ;
		
	}
	
	public String exec () throws JSONException {
		
		String callbackName = this.request.getParameter("callback");
		System.out.println(callbackName + "callbackName") ;
		if ( callbackName != null ) {

			if ( !validCallbackName( callbackName ) ) {
				return new BaseState( false, AppInfo.ILLEGAL ).toJSONString();
			}
			
			return this.invoke();
			
		} else {
			return this.invoke();
		}

	}
	
	public String invoke() throws JSONException {
		
		if ( actionType == null || !ActionMap.mapping.containsKey( actionType ) ) {
			return new BaseState( false, AppInfo.INVALID_ACTION ).toJSONString();
		}
		
		if ( this.configManager == null || !this.configManager.valid() ) {
			return new BaseState( false, AppInfo.CONFIG_ERROR ).toJSONString();
		}
		
		State state = null;
		
		int actionCode = ActionMap.getType( this.actionType );
		
		Map<String, Object> conf = null;
		
		switch ( actionCode ) {
		
			case ActionMap.CONFIG:
				return this.configManager.getAllConfig().toString();
				
			case ActionMap.UPLOAD_IMAGE:
			case ActionMap.UPLOAD_SCRAWL:
			case ActionMap.UPLOAD_VIDEO:
			case ActionMap.UPLOAD_FILE:
				try {
					conf = this.configManager.getConfig( actionCode );
				} catch (org.springframework.boot.configurationprocessor.json.JSONException e) {
					e.printStackTrace();
				}
				state = new Uploader( request, conf ).doExec();
				break;
				
			case ActionMap.CATCH_IMAGE:
				try {
					conf = configManager.getConfig( actionCode );
				} catch (org.springframework.boot.configurationprocessor.json.JSONException e) {
					e.printStackTrace();
				}
				String[] list = this.request.getParameterValues( (String)conf.get( "fieldName" ) );
				state = new ImageHunter( conf ).capture( list );
				break;
				
			case ActionMap.LIST_IMAGE:
			case ActionMap.LIST_FILE:
				try {
					conf = configManager.getConfig( actionCode );
				} catch (org.springframework.boot.configurationprocessor.json.JSONException e) {
					e.printStackTrace();
				}
				int start = this.getStartIndex();
				state = new FileManager( conf ).listFile( start );
				break;
				
		}
		
		return state.toJSONString();
		
	}
	
	public int getStartIndex () {
		
		String start = this.request.getParameter( "start" );
		
		try {
			return Integer.parseInt( start );
		} catch ( Exception e ) {
			return 0;
		}
		
	}
	
	/**
	 * callback参数验证
	 */
	public boolean validCallbackName ( String name ) {
		
		if ( name.matches( "^[a-zA-Z_]+[\\w0-9_]*$" ) ) {
			return true;
		}
		
		return false;
		
	}
	
}