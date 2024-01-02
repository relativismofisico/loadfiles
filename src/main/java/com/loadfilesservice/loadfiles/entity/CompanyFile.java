package com.loadfilesservice.loadfiles.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Data
@Table(name = "files")
public class CompanyFile implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ide_file")
	private Long id;
	
	@Column(name = "file_name")
	private String fileName;
	
	@Column(name = "file_path")
	private String filePath;
	
	@NotEmpty
	@Column(name = "ip_load")
	private String ipLoad;
	
	@Column(name = "load_time")
	private LocalDateTime loadTime;
	
	@Column(name = "company_ide")
	private Long company;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "file_type_ide", referencedColumnName = "ide_file_type")
	private CompanyFileType companyFileType;
	
	private static final long serialVersionUID = 1L;

}
