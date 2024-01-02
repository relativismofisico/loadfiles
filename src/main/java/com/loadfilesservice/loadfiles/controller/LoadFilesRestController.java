package com.loadfilesservice.loadfiles.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.loadfilesservice.loadfiles.dto.CompanyFileDTORequest;
import com.loadfilesservice.loadfiles.dto.Converter;
import com.loadfilesservice.loadfiles.entity.CompanyFile;
import com.loadfilesservice.loadfiles.exceptions.InternalServerErrorException;
import com.loadfilesservice.loadfiles.service.ICompanyFileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class LoadFilesRestController {

	private final ICompanyFileService companyFileService;
	
	private final Converter converter;
	
	@PostMapping(value="/companyfile/upload", produces = "application/json")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile newfile, 
										@RequestParam("fileinfo") String jsonCompanyFile){
		
		Map<String, Object> response = new HashMap<>();
		CompanyFileDTORequest companyFileDTO = new Gson().fromJson(jsonCompanyFile, CompanyFileDTORequest.class);
		
		if (!newfile.isEmpty()) {
			String newFileName = null;
			
			try {
				newFileName = companyFileService.copyFile(newfile);
			} catch (Exception e) {
				log.error("[LoadFilesRestController][uploadFile][loadfiles]" + " Error al intentar guardar el archivo: " + newfile.getOriginalFilename());
				throw new InternalServerErrorException("Error al intentar guardar el archivo: " + newfile.getOriginalFilename());
			}
			
			CompanyFile newCompanyFile = converter.companyFileDTOtoCompanyFile(companyFileDTO);
			
			newCompanyFile.setFileName(newFileName);
			newCompanyFile.setFilePath(companyFileService.getPath(newFileName).toString());
			newCompanyFile.setLoadTime(LocalDateTime.now());
			
			System.out.println(newCompanyFile);
			
			try {
				companyFileService.save(newCompanyFile);
			} catch (Exception e) {
				log.error("[LoadFilesRestController][uploadFile][loadfiles]" + " Error al intentar guardar el registro del archivo en la base de datos: " + newFileName);
				throw new InternalServerErrorException("Error al intentar guardar el registro del archivo en la base de datos: " + newFileName);
			}
			
			response.put("savedFile", newCompanyFile);
		}
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
}
