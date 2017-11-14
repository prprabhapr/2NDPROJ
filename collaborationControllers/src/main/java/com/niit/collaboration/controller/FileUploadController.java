package com.niit.collaboration.controller;

import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.niit.collaboration.DAO.FileUploadDAO;
import com.niit.collaboration.model.FileUpload;

@RestController
public class FileUploadController
{
	private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);
	
	@Autowired
	private FileUploadDAO fileUploadDAO;
	
	@Autowired
	private HttpSession session;

	@PostMapping(value="/Upload")
	public ModelAndView uploadFile(@RequestParam("uploadedFile") MultipartFile uploadFile)  throws Exception
	{
		if (uploadFile != null ) 
		{
			String username = session.getAttribute("username").toString();
			MultipartFile aFile = uploadFile;
			FileUpload fileUpload = new FileUpload();
			System.out.println(aFile);
			fileUpload.setFileName(session.getAttribute("username").toString());
            fileUpload.setData(aFile.getBytes());
            fileUpload.setUsername(username);
            fileUploadDAO.save(fileUpload, username);
            
            FileUpload getFileUpload = fileUploadDAO.getFile(username);
         	String name = getFileUpload.getFileName();
         	System.out.println("Name = "+name);
         	System.out.println(getFileUpload.getData());
         	byte[] imagefiles = getFileUpload.getData();
         	try
         	{
         		String path="C:\\Users\\prabh\\Desktop\\SECPROJ\\collaborationFrontEnd\\WebContent\\images\\"+username;
         		File file=new File(path);
         		file.mkdirs();//
         		FileOutputStream fos = new FileOutputStream(file);
         		fos.write(imagefiles);
         		fos.close();
         		}catch(Exception e){
         		e.printStackTrace();
         	}
		}
		ModelAndView mv = new ModelAndView("backToHome");
		return mv;
	}
}
