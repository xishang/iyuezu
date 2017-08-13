package com.iyuezu.platform.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iyuezu.common.beans.ResponseResult;
import com.iyuezu.common.beans.User;
import com.iyuezu.common.utils.ApplicationUtil;
import com.iyuezu.common.utils.SystemConstants;

import sun.misc.BASE64Decoder;

@SuppressWarnings("restriction")
@RestController
public class UploadController {
	
	private static Logger logger = Logger.getLogger(UploadController.class);
	
	@RequestMapping(value = "/upload", method=RequestMethod.POST, produces = "application/json")
	public ResponseResult<List<String>> uploadFile(HttpServletRequest request) {
		String userInfo = "";
		User user = (User) request.getAttribute("user");
		if (user != null) {
			userInfo = "账号[" + user.getAccount() + "]";
		} else {
			String userIp = ApplicationUtil.getIpAddr(request);
			userInfo = "用户ip[" + userIp + "]";
		}
		//判断提交上来的数据是否是上传表单的数据
		if (!ServletFileUpload.isMultipartContent(request)) {
			logger.error("上传文件：" + userInfo + ", 结果[上传失败，未选择文件上传]");
            return new ResponseResult<List<String>>("9", "请求出错，请选择文件上传");
        }
		List<String> pathList = new ArrayList<String>();
		String rootPath = SystemConstants.ROOT_PATH;
		String filePath = SystemConstants.UPLOAD_PATH;
		//上传文件时的临时目录
		String tempPath = SystemConstants.TEMP_PATH;
		File tempFile = new File(rootPath + tempPath);
		if (!tempFile.exists()) {
			tempFile.mkdirs();
		}
		try {
			//创建一个DiskFileItemFactory工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//设置工厂的缓冲区大小为2MB，当上传的文件大小超过缓冲区的大小时，就会生成一个临时文件存放到指定的临时目录当中
			factory.setSizeThreshold(1024 * 1024 * 2);
			//设置上传时生成的临时文件的保存目录
			factory.setRepository(tempFile);
			//创建一个文件上传解析器
			ServletFileUpload upload = new ServletFileUpload(factory);
			//解决上传文件名的中文乱码
			upload.setHeaderEncoding("UTF-8");
			//设置上传单个文件的大小的最大值，目前设置为2MB
			upload.setFileSizeMax(1024 * 1024 * 2);
			//设置上传文件总量的最大值，即同时上传的多个文件的大小的最大值的和，目前设置为20MB
			upload.setSizeMax(1024 * 1024 * 20);
			//使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
			List<FileItem> list = upload.parseRequest(request);
			for (FileItem item : list) {
				//如果fileitem中封装的是普通输入项的数据
				if (item.isFormField()) {
					logger.error("上传文件：" + userInfo + ", 结果[上传失败，未选择文件上传]");
					return new ResponseResult<List<String>>("1", "请求出错，请选择文件上传");
				} else {
					//如果fileitem中封装的是上传文件，得到上传的文件名称
					String filename = item.getName();
					if (filename == null || filename.trim().equals("")) {
						continue;
					}
					//注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
					//处理获取到的上传文件的文件名的路径部分，只保留文件名部分
					filename = filename.substring(filename.lastIndexOf("\\") + 1);
					//得到上传文件的扩展名
					String fileExtName = filename.substring(filename.lastIndexOf(".")+1);
					//保存文件名
					String saveFileName = UUID.randomUUID().toString() + "." + fileExtName;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					//保存文件相对路径，"user/resource/yyyyMMdd/filename"
					String saveFilePath = filePath + "/" + sdf.format(new Date()) + "/" + saveFileName;
					File saveFile = new File(rootPath + saveFilePath);
					saveFile.getParentFile().mkdirs();
					saveFile.createNewFile();
					//获取item中的上传文件的输入流
					InputStream in = item.getInputStream();
					//创建一个文件输出流
					FileOutputStream out = new FileOutputStream(saveFile);
					//创建一个缓冲区
					byte buffer[] = new byte[1024];
					//判断输入流中的数据是否已经读完的标识
					int len = 0;
					//循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
					while ((len = in.read(buffer)) > 0) {
						//使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
						out.write(buffer, 0, len);
					}
					//关闭输入流
					in.close();
					//关闭输出流
					out.close();
					//删除处理文件上传时生成的临时文件
					item.delete();
					pathList.add(saveFilePath);
				}
			}
			logger.info("上传文件：" + userInfo + ", 结果[上传成功]");
			return new ResponseResult<List<String>>("0", "上传图片成功", pathList);
		} catch (FileSizeLimitExceededException e) {
			logger.error("上传文件：" + userInfo + ", 结果[上传失败，单个文件超过2M]");
			return new ResponseResult<List<String>>("2", "上传文件大小超出限制，单个文件大小不能超过2M");
		} catch (SizeLimitExceededException e) {
			logger.error("上传文件：" + userInfo + ", 结果[上传失败，全部文件超过20M]");
			return new ResponseResult<List<String>>("3", "总上传大小超出限制，一次上传的全部文件大小不能超过20M");
		} catch (Exception e) {
			logger.error("上传文件：" + userInfo + ", 结果[上传失败，服务器处理文件出错]");
			return new ResponseResult<List<String>>("4", "服务器处理文件出错");
		}
	}
	
	@RequestMapping(value = "/uploadBase64", method=RequestMethod.POST, produces = "application/json")
	public ResponseResult<String> uploadBase64(String base64, HttpServletRequest request) {
		String userInfo = "";
		User user = (User) request.getAttribute("user");
		if (user != null) {
			userInfo = "账号[" + user.getAccount() + "]";
		} else {
			String userIp = ApplicationUtil.getIpAddr(request);
			userInfo = "用户ip[" + userIp + "]";
		}
		String rootPath = SystemConstants.ROOT_PATH;
		String filePath = SystemConstants.UPLOAD_PATH;
		String header = getHeader(base64);
		String image = base64.substring(header.length());
		BASE64Decoder decoder = new BASE64Decoder();
		String saveFilePath = null;
		try {
			String fileExtName = "." + header.substring(11).split(";")[0];
			String saveFileName = UUID.randomUUID().toString() + fileExtName;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			//保存文件相对路径，"user/resource/yyyyMMdd/filename"
			saveFilePath = filePath + "/" + sdf.format(new Date()) + "/" + saveFileName;
			File saveFile = new File(rootPath + saveFilePath);
			saveFile.getParentFile().mkdirs();
			saveFile.createNewFile();
			byte[] decodedBytes = decoder.decodeBuffer(image);
			FileOutputStream out = new FileOutputStream(saveFile);
			out.write(decodedBytes);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("上传文件[Base64]：" + userInfo + ", 结果[上传失败]");
			return new ResponseResult<String>("1", "上传图片失败");
		}
		logger.info("上传文件[Base64]：" + userInfo + ", 结果[上传成功]");
		return new ResponseResult<String>("0", "上传图片成功", saveFilePath);
	}
	
	private String getHeader(String image) {
		String[] headers = {"data:image/jpg;base64,", "data:image/jpeg;base64,", "data:image/png;base64,", "data:image/gif;base64,"};
		for (String header : headers) {
			if (image.indexOf(header) == 0) {
				return header;
			}
		}
		return null;
	}
	
}
