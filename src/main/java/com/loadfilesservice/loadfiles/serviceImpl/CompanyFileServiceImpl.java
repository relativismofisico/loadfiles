package com.loadfilesservice.loadfiles.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.loadfilesservice.loadfiles.dao.ICompanyFileDao;
import com.loadfilesservice.loadfiles.entity.CompanyFile;
import com.loadfilesservice.loadfiles.entity.CompanyFileType;
import com.loadfilesservice.loadfiles.exceptions.ResourceNotFoundException;
import com.loadfilesservice.loadfiles.service.ICompanyFileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyFileServiceImpl implements ICompanyFileService{
	
	private final ICompanyFileDao companyFileDao;
	
	private final static String PATH_UPLOADS = "uploadfiles";
	
	@Override
	@Transactional(readOnly = true)
	public Optional<CompanyFile> findById(Long id) {
		
		if (!companyFileDao.existsById(id)) {
			log.error("[CompanyFileServiceImpl][findById][loadfiles]" + " El archivo no se encuentra en la base de datos");
			throw new ResourceNotFoundException("El archivo no se pudo encontrar en la base de datos");
		}
		
		return companyFileDao.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CompanyFile> findByCompanyAndCompanyFileType(Long companyId, CompanyFileType companyFileType) {
		return companyFileDao.findByCompanyAndCompanyFileType(companyId, companyFileType);
	}

	@Override
	@Transactional
	public CompanyFile save(CompanyFile companyFile) {
		return companyFileDao.save(companyFile);
	}

	@Override
	public Resource loadFile(String fileName) throws MalformedURLException {
		Path pathFile = getPath(fileName);
		
		Resource resource = new UrlResource(pathFile.toUri());
		
		if (!resource.exists() && !resource.isReadable()) {
			log.error("El archivo"+ fileName +" no se encuentra.");
		}
		
		return resource;
	}

	@Override
	public String copyFile(MultipartFile file) throws IOException {
		String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename().replace(" ", "");
		Path pathFile = getPath(fileName);
		
		Files.copy(file.getInputStream(), pathFile);
		
		return fileName;
	}

	@Override
	public boolean deleteFile(String fileName) {
		if (fileName != null && fileName.length() > 0) {
			Path pathOldFile = getPath(fileName);
			File oldFile = pathOldFile.toFile();
			
			if (oldFile.exists() && oldFile.canRead()) {
				if (oldFile.delete()) {
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public Path getPath(String fileName) {
		return Paths.get(PATH_UPLOADS).resolve(fileName).toAbsolutePath();
	}

}
