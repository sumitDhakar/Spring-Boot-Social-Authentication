package com.dollop.userauth.fileservice;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;


@Service
public class FileServiceImpl implements FileService{

	@Value("${imagepath.file.path}")
	private String globalImagesPath  ;
	
	@Value("${userprofile.file.path}")
	private String DIRECTORY;
	
	@Autowired
	public Cloudinary  cloudinary;
		
	
	
	@Override
	public String uploadFileInFolder(MultipartFile myFile, String destinationPath) {
		String randomName= (UUID.randomUUID().toString() + myFile.getOriginalFilename());

		String fileName = StringUtils.cleanPath(randomName);
		Map uploadResponse;
		try {
			uploadResponse = cloudinary.uploader().upload(myFile.getBytes(),
					  ObjectUtils.asMap("public_id", destinationPath +"/"+fileName)); 
			return (String)uploadResponse.get("secure_url");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> uploadFileInFolder(List<MultipartFile> file, String destinationPath) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public InputStream getImages(String fileName,String destination){
		//System.out.println(System.getProperty("user.dir")+globalImagesPath+destination+File.separator+fileName);
		try {
				return new FileInputStream(System.getProperty("user.dir")+globalImagesPath+destination+File.separator+fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	private String getFileExtension(String filename) {
	    int lastDotIndex = filename.lastIndexOf(".");
	    if (lastDotIndex >= 0) {
	        return filename.substring(lastDotIndex + 1).toLowerCase();
	    }
	    return "";
	}

	
	public String uploadFileInFolde(MultipartFile file, String destinationPath) {
	    String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
	    String randomId = UUID.randomUUID().toString();
	    String randomName = randomId.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));

	    try {
	        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
	                "public_id", randomName,
	                "resource_type", "auto",  // auto-detect resource type
	                "folder", destinationPath,  // specify the folder
	                "overwrite", true,  // overwrite if file with the same name exists
	                "invalidate", true, // invalidate CDN cache
	                "use_filename", true, // use the specified filename
	                "unique_filename", false, // ensure a unique filename
	                "eager", Arrays.asList(new Transformation<Transformation>().width(100).height(100).crop("fill").gravity("face")) // specify eager transformations if needed
	        ));

	        System.err.println(uploadResult);
	        return (String) uploadResult.get("secure_url");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return null;
	}
	
	 public void deleteImageFromCloudServer(String imageUrl) throws IOException {  
	        // Extract public ID from the image URL
	        String publicId = extractPublicId(imageUrl);

	        if (publicId != null) {
	            Map<?, ?> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
	            System.out.println("Delete result: " + result.toString());
	        } else {
	            System.out.println("Invalid image URL");
	        }
	    }

	    // Method to extract public ID from Cloudinary image URL
	    private String extractPublicId(String imageUrl) {
	        // Assuming the Cloudinary image URL format is: https://res.cloudinary.com/{cloudName}/image/upload/{publicId}.{format}
	        // You may need to adjust this method depending on your image URL format
	        String[] parts = imageUrl.split("/");
	        if (parts.length >= 7) {
	            String publicIdWithFormat = parts[6]; // publicId with format, e.g., "publicId.jpg"
	            String[] publicIdParts = publicIdWithFormat.split("\\.");
	            if (publicIdParts.length >= 2) {
	                return publicIdParts[0]; // return the publicId without the format, e.g., "publicId"
	            }
	        }
	        return null;
	    }

		
}
