package edu.iit.cs445.spring2022.buynothing;

public class ErrorResponse {
	private String type;
	private String title;
	private String detail;
	private String instance;
	private int status;
	
	public ErrorResponse(String type, String title, String detail, String instance, int status) {
		this.type = type;
		this.title = title;
		this.detail = detail;
		this.instance = instance;
		this.status = status;
	}
}
