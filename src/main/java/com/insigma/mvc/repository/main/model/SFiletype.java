package com.insigma.mvc.repository.main.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the S_FILETYPE database table.
 * 
 */
@TableName("S_FILETYPE")
@Data
@NoArgsConstructor
public class SFiletype implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String businesstype;

	private Integer filemaxnum;

	private Integer filemaxsize;

	private String typename;

}