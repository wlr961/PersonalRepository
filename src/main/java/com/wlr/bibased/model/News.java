package com.wlr.bibased.model;

import java.util.Date;

public class News {
  private int  newid;
  private String  title;
  private Date publishdate;
  private String publisher;
  
public String getPublisher() {
	return publisher;
}
public void setPublisher(String publisher) {
	this.publisher = publisher;
}
public int getNewid() {
	return newid;
}
public void setNewid(int newid) {
	this.newid = newid;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}

public Date getPublishdate() {
	return publishdate;
}
public void setPublishdate(Date publishdate) {
	this.publishdate = publishdate;
}
}
