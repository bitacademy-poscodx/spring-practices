package fileupload.service;

import java.io.File;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {
	private static String SAVE_PATH = "/fileupload-files";
	
	public String restore(MultipartFile file) {
		String url = null;
		
		File uploadDirectory = new File(SAVE_PATH);
		if(!uploadDirectory.exists()) {
			uploadDirectory.mkdirs();
		}
		
		if(file.isEmpty()) {
			return url;
		}
		
		String originFilename = file.getOriginalFilename();
		Long fileSize = file.getSize();
		
		System.out.println("#######" + originFilename);
		System.out.println("#######" + fileSize);
		
		
		
		return url;
	}

}
