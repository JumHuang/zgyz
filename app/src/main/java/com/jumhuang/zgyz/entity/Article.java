package com.jumhuang.zgyz.entity;

public class Article
{
	private String title;
	private String description;
	private String author;
	private int clickNum;
	private String time;
	private String url;

	public Article(String title,String description,String author,int clickNum,String time, String url)
	{
		this.title = title;
		this.description=description;
		this.author=author;
		this.clickNum=clickNum;
		this.time = time;
		this.url = url;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getDescription()
	{
		return description;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setClickNum(int clickNum)
	{
		this.clickNum = clickNum;
	}

	public int getClickNum()
	{
		return clickNum;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTime(String time)
	{
		this.time = time;
	}

	public String getTime()
	{
		return time;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getUrl()
	{
		return url;
	}
}
