package com.insigma.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description ="�������")
public class Pub implements java.io.Serializable{
	
	@ApiModelProperty(required = false,value = "��������",name = "��������",example="330106" )
	private String area_code;
	@ApiModelProperty(required = false,value = "��������",name = "��������",example="������" )
	private String area_name;
	@ApiModelProperty(required = false,value = "���",name = "���" ,example="2019")
	private String year;
	@ApiModelProperty(required = false,value = "��ҳ��",name = "��ҳ��",example="10" )
	private String limit;
	@ApiModelProperty(required = false,value = "�����",name = "�����",example="sort_num" )
	private String area_code_fath;
	@ApiModelProperty(required = false,value = "area_code_fath",name = "",example =  "area_code_fath")
	private String area_code_grand;
	@ApiModelProperty(required = false,value = "�����ڵ�",name = "",example = "area_code_grand")
	private String sort_num;
	@ApiModelProperty(required = false,value = "�㼶",name = "",example = "cj")
	private String cj;
	@ApiModelProperty(required = false,value = "����",name = "",example = "1")
	private String type;
	public String getArea_code() {
		return area_code;
	}
	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}
	public String getArea_name() {
		return area_name;
	}
	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getLimit() {
		return limit;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}


	public String getSort_num() {
		return sort_num;
	}

	public void setSort_num(String sort_num) {
		this.sort_num = sort_num;
	}

	public String getArea_code_fath() {
		return area_code_fath;
	}

	public void setArea_code_fath(String area_code_fath) {
		this.area_code_fath = area_code_fath;
	}

	public String getArea_code_grand() {
		return area_code_grand;
	}

	public void setArea_code_grand(String area_code_grand) {
		this.area_code_grand = area_code_grand;
	}

	public String getCj() {
		return cj;
	}

	public void setCj(String cj) {
		this.cj = cj;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
