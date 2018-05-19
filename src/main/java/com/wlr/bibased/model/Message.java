package com.wlr.bibased.model;

import java.util.Date;

public class Message {
	private int id;
	private String sendid;
	private String acceptid;
	private String content;
	private Date senddate;
	private String appendixname;
	public String getAppendixname() {
		return appendixname;
	}
	public void setAppendixname(String appendixname) {
		this.appendixname = appendixname;
	}
	public Date getSenddate() {
		return senddate;
	}
	public void setSenddate(Date senddate) {
		this.senddate = senddate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSendid() {
		return sendid;
	}
	public void setSendid(String sendid) {
		this.sendid = sendid;
	}
	public String getAcceptid() {
		return acceptid;
	}
	public void setAcceptid(String acceptid) {
		this.acceptid = acceptid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}


}
