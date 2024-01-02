package com.loadfilesservice.loadfiles.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.loadfilesservice.loadfiles.entity.CompanyFileType;

import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class CompanyFileDTOResponse implements Serializable{
	
	private String fileName;
	
	private Date loadTime;
	
	private Long company;
	
	private CompanyFileType companyFileType;
	
	private static final long serialVersionUID = 1L;

}
