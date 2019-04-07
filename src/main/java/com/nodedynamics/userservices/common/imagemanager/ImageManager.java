package com.nodedynamics.userservices.common.imagemanager;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.bson.internal.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.StringUtils;
import com.nodedynamics.userservices.common.Global;
import com.nodedynamics.userservices.common.Util;
import com.nodedynamics.userservices.model.common.ImageModel;

public class ImageManager {
	
	Logger log = LoggerFactory.getLogger(ImageManager.class);
	
	private AmazonS3 conn;
	private ImageModel IM;
	
	public void Connect() {
		log.info("ImageManager->Connect");
		AWSCredentials credentials = new BasicAWSCredentials(Global.STORAGE.ACCESSKEY.key, Global.STORAGE.SECRETKEY.key);
		this.conn = new AmazonS3Client(credentials);
		this.conn.setEndpoint(Global.STORAGE.URL.key);
		
	}
	
	public ImageModel Commit(ImageModel imageModel) {
		
		//CHECK IF IMAGE EXIST
		if(!(imageModel.getImageLoc().isEmpty())) {
			
			try {
				byte[] imageByteArray=Base64.decode(imageModel.getImageLoc());
				
				InputStream in = new ByteArrayInputStream(imageByteArray);
				BufferedImage image = ImageIO.read(in);
	
				
				//GET IMAGE TYPE
				int type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
				
				//RESIZE IMAGE
				BufferedImage resizedImage = resize(image, 500, 500, type);
				
				//TEMP WRITE IMAGE
				File output =  new File("d:/imageTemp/"+imageModel.getImageName());
				
				String FileExt=Util.getFileExtension(imageModel.getImageName());
				ImageIO.write(resizedImage, FileExt, output);

				
				//COMMIT OBJECT
				this.conn.putObject(new PutObjectRequest(Global.STORAGE.FOLDER.key, imageModel.getImageName(), output).withCannedAcl(CannedAccessControlList.PublicRead));
				
				//BUILD MODEL
				IM=ImageModel.builder()
				.ImageLoc("https://"+Global.STORAGE.URL.key+"/"+Global.STORAGE.FOLDER.key+"/")
				.ImageName(imageModel.getImageName())
				.ImageType(Global.ImageType.PROFILE.key)
				.build();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}
		
		return IM;
	}
	
	private static BufferedImage resize(BufferedImage img, int IMG_HEIGHT, int IMG_WIDTH, int type) {
		
		int orgHeightImageSize=img.getHeight();
		int orgWidthImageSize=img.getWidth();
		
		int newHeight;
		int newWidth;
		  
		if(orgHeightImageSize > orgWidthImageSize){
			newHeight=IMG_HEIGHT;
			newWidth=Math.round((orgWidthImageSize*(IMG_HEIGHT/orgHeightImageSize)));			
		 }else{
			newWidth=IMG_WIDTH;
			
			float widthPercentage=(float)((float)IMG_WIDTH/(float)orgWidthImageSize);
			newHeight=(int)Math.round(((float)orgHeightImageSize*((float)widthPercentage))); 
		 }		 
		 BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, type);
		 Graphics2D g = resizedImage.createGraphics();
		 g.drawImage(img, 0, 0, newWidth, newHeight, null);
		 g.dispose();
				
		 return resizedImage;	    
	}

}

