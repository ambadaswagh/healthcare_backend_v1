package com.healthcare.api;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Service
public class S3Wrapper {

	 private static final Logger LOGGER = LoggerFactory.getLogger(S3Wrapper.class);
	 
	@Autowired
	private AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public PutObjectResult upload(InputStream inputStream, String uploadKey) throws IOException {
		//PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, uploadKey, inputStream, new ObjectMetadata());
		byte[] bytes = IOUtils.toByteArray(inputStream);
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(bytes.length);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, uploadKey, byteArrayInputStream, metadata);
		putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
		PutObjectResult putObjectResult = amazonS3Client.putObject(putObjectRequest);
		IOUtils.closeQuietly(inputStream);

		return putObjectResult;
	}

	public void delete(String key) {
		DeleteObjectRequest deleteObjReq = new DeleteObjectRequest(bucket, key);
		amazonS3Client.deleteObject(deleteObjReq);
	}

	public ResponseEntity<byte[]> download(String key) throws IOException {

		S3Object s3Object = amazonS3Client.getObject(bucket, key);

		S3ObjectInputStream objectInputStream = s3Object.getObjectContent();

		byte[] bytes = IOUtils.toByteArray(objectInputStream);

		String fileName = URLEncoder.encode(key, "UTF-8").replaceAll("\\+", "%20");

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		httpHeaders.setContentLength(bytes.length);
		httpHeaders.setContentDispositionFormData("attachment", fileName);

		return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
	}

	public List<S3ObjectSummary> list() {
		ObjectListing objectListing = amazonS3Client.listObjects(new ListObjectsRequest().withBucketName(bucket));

		List<S3ObjectSummary> s3ObjectSummaries = objectListing.getObjectSummaries();

		return s3ObjectSummaries;
	}
	
	public URL getURL(String key){
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, key);

		S3Object s3Object = amazonS3Client.getObject(getObjectRequest);
		try {
		URL url = s3Object.getObjectContent().getHttpRequest().getURI().toURL();
		s3Object.close();
		
			return url;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public URL getS3URL(){
		try {
			return new URL(String.format("https://%s.s3.amazonaws.com/", bucket));
		} catch (MalformedURLException e) {
			LOGGER.warn(e.getMessage());
			return null;
		}
	}
	
}