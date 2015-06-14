package com.bob.digcsdn.bean;

/**
 * 博客内容都是临时在线获取的
 * 注意：这只是博客的一种条目，由state决定它起作用的是哪一个
 */
public class Blog {
	private String title;//标题
	private String content;//内容
	private String summary;//概要
	private String imgLink;//图片链接
	private String link;//文章链接
	private int state;//状态
	private String commentCount;//评论数量

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getImgLink() {
		return imgLink;
	}

	public void setImgLink(String imgLink) {
		this.imgLink = imgLink;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}
}
