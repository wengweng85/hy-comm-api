package com.insigma.dto;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import com.insigma.dto.validgroup.Query;

public class BaseDTO {
	
	@NotBlank(message = "page不能为空",groups = Query.class)
	@Pattern(regexp = "^[0-9]*$",message="页码必须为数字",groups = Query.class)
    private String page;
	
	@NotBlank(message = "size不能为空",groups = Query.class)
	@Pattern(regexp = "^[0-9]*$",message="size页码必须为数字",groups = Query.class)
	private String size;

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
	

}
