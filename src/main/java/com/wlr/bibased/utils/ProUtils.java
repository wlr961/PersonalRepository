package com.wlr.bibased.utils;

import java.util.ResourceBundle;

/**
 * 获取文件值
 *
 */
public class ProUtils {
	
	
	/**
	 * @param filename  资源文件名
	 * @param configname key字段
	 * @return
	 */
	public static String getConfigStr(String filename,String configname)
	{
		//ResourceBundle读取properties文件
		ResourceBundle rb=ResourceBundle.getBundle(filename);
		if(null!=rb)
		{
		  String value=rb.getString(configname);
		  if(!"".equals(value)&&null!=value)
		  {
			return value.trim();
		  }
		}
	    return null;

	}
	
	
	/**
	 * @param configname key字段（默认资源文件为profile）
	 * @return
	 */
	public static String getConfigStr(String configname)
	{
		ResourceBundle r= ResourceBundle.getBundle("profile");
		String value=r.getString(configname);
		if(!"".equals(value)&&null!=value)
		{
			return value.trim();
		}
		return null;
	}
}
