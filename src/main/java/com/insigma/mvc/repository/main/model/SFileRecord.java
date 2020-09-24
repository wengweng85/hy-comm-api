package com.insigma.mvc.repository.main.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 文件上传公共记录表
 */
@TableName("S_FILE_RECORD")
@Data
@NoArgsConstructor
public class SFileRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId("FILE_UUID")
	private String fileUuid;

	@Temporal(TemporalType.DATE)
	@TableField("BUS_ADDTIME")
	private Date busAddtime;

	@Temporal(TemporalType.DATE)
	@TableField("FILE_ADDTIME")
	private Date fileAddtime;

	@TableField("FILE_BUS_DESCRIPTION")
	private String fileBusDescription;

	@TableField("FILE_BUS_ID")
	private String fileBusId;

	@TableField("FILE_BUS_NAME")
	private String fileBusName;

	@TableField("FILE_BUS_TYPE")
	private String fileBusType;

	@TableField("FILE_LENGTH")
	private Long fileLength;

	@TableField("FILE_NAME")
	private String fileName;

	@TableField("FILE_PATH")
	private String filePath;

	@TableField("FILE_REL_PATH")
	private String fileRelPath;

	@TableField("FILE_STATUS")
	private String fileStatus;

	@TableField("FILE_TYPE")
	private String fileType;


}