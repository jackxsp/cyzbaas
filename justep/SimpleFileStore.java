package justep;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.action.ActionContext;

public class SimpleFileStore {
	
	public static JSONObject service(JSONObject params, ActionContext context) throws ServletException, IOException {
		
		HttpServletRequest request = (HttpServletRequest)context.get(ActionContext.REQUEST);
		HttpServletResponse response = (HttpServletResponse)context.get(ActionContext.RESPONSE);
		if(request.getMethod().equals("GET")){
			doGet(request, response);
		}else if(request.getMethod().equals("POST")){
			doPost(request, response);
		}
		return null;
	}
	
	static String docStorePath;
	static File docStoreDir;
	
	static{
		String baasPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + ".." + File.separator + "..";
		docStorePath = baasPath + File.separator + "data" + File.separator + "attachmentSimple";
		File file = new File(docStorePath);
		//兼容以前存储目录
		if(file.exists() && file.isDirectory()){
			docStoreDir = file;
		}
	}
	
	
	public static File getDocStoreDir(HttpServletRequest request) {
		String justepHome = System.getenv("JUSTEP_HOME");
		if(justepHome != null && docStoreDir == null ){
			docStorePath =  justepHome + "/data/attachmentSimple/";
			File file = new File(docStorePath);
			if(!file.exists()){
				file.mkdirs();
			}
			docStoreDir = file;
		}else if(docStoreDir == null){
			ServletContext context = request.getSession().getServletContext();
			String path = context.getRealPath("/");
			docStorePath =  path + "../../data/attachmentSimple/";
			File file = new File(docStorePath);
			if(!file.exists()){
				file.mkdirs();
			}
			docStoreDir = file;
		}
		return docStoreDir;
	}
	
	
	/**
		get为获取文件 浏览或者下载
	**/
	private static void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		getDocStoreDir(request);
		String operateType = request.getParameter("operateType");
		if("copy".equals(operateType)){
			copyFile(request,response);
		}else{
			getFile(request,response);
		}
	}
	
	
	private static void copyFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String ownerID = request.getParameter("ownerID");
		String targetOwnerID = request.getParameter("targetOwnerID");
		String storeFileName = request.getParameter("storeFileName");
		File file = new File(docStorePath + File.separator +ownerID + File.separator + storeFileName);
		File targetFile = new File(docStorePath + File.separator + targetOwnerID + File.separator + storeFileName);
		FileUtils.copyFile(file, targetFile);
	}
	
	
	private static final int BUFFER_SIZE = 32768 * 8;
	private static void getFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String ownerID = request.getParameter("ownerID");
		String realFileName = URLEncoder.encode(request.getParameter("realFileName"),"utf-8");
		String storeFileName = request.getParameter("storeFileName");
		String operateType = request.getParameter("operateType");
		/*String fileSize = request.getParameter("fileSize");*/
		
		File file = new File(docStorePath + File.separator +ownerID + File.separator + storeFileName);
		FileInputStream fis = new FileInputStream(file);
		
		/*response.setContentType(mimeType);*/
        /*response.setHeader("Content-Length", String.valueOf(part.getSize()));*/
		response.setHeader("Cache-Control", "pre-check=0, post-check=0, max-age=0");
		
		String fileNameKey = "filename";
		/*UserAgent ua = com.justep.ui.util.NetUtils.getUserAgent(request);
		if(Browser.FIREFOX.equals(ua.getBrowser().getGroup())){
			fileNameKey = "filename*";
		}*/
		if("download".equals(operateType)){
			response.addHeader("Content-Disposition", "attachment; "+fileNameKey+"=\"" + realFileName + "\"");
		}else{
			response.addHeader("Content-Disposition", "inline; "+fileNameKey+"=\"" + realFileName + "\"");
		}
		
		OutputStream os = response.getOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            int read;
            while ((read = fis.read(buffer)) != -1) {
            	os.write(buffer, 0, read);
            }
        } finally {
            fis.close();
        }
	}

	/**
		post为上传
	**/
	protected static  void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		getDocStoreDir(request);
		String contentType = request.getContentType();
		try {
			if("application/octet-stream".equals(contentType)){
				storeOctStreamFile(request,response);
			}else if(contentType !=null && contentType.startsWith("multipart/")){
				storeFile(request,response);
			}else{
				throw new RuntimeException("not supported contentType");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("storeFile异常");
		}
		response.getWriter().write("");
	}

	private static void storeOctStreamFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		InputStream in = null;
		FileOutputStream storeStream = null;
		try{
			String ownerID = request.getParameter("ownerID");
			String storeFileName = request.getParameter("storeFileName");
			
			in = request.getInputStream();
			String storePath = docStorePath + File.separator + ownerID;
			getOrCreateFile(storePath);
			File toStoreFile = new File(storePath + File.separator + storeFileName);
	        storeStream = new FileOutputStream(toStoreFile);
	        IOUtils.copy(in, storeStream);
		}finally{
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(storeStream);
		}
	}
	
	private static File getOrCreateFile(String path) {
		File storeDir = new File(path);
        if(!(storeDir.exists() && storeDir.isDirectory())){
        	storeDir.mkdirs();
        }
        return storeDir;
	}
	
	public static List<FileItem> parseMultipartRequest(HttpServletRequest request) throws FileUploadException{
		DiskFileItemFactory factory = new DiskFileItemFactory();

		ServletContext servletContext = request.getSession().getServletContext();
		File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(repository);

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Parse the request
		@SuppressWarnings("unchecked")
		List<FileItem> items = upload.parseRequest(request);
		return items;
	}
	
	private static void storeFile(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HashMap<String,String> params = new HashMap<String,String>();
		List<FileItem> items =  parseMultipartRequest(request);
		Iterator<FileItem> iter = items.iterator();
		FileItem fileItem = null;
		while (iter.hasNext()) {
		    FileItem item = iter.next();
		    if (item.isFormField()) {
		    	String name = item.getFieldName();
	            String value = item.getString();
		        params.put(name, value);
		    } else {
		    	/*String fieldName = item.getFieldName();
		        String fileName = item.getName();
		        String contentType = item.getContentType();
		        boolean isInMemory = item.isInMemory();
		        long sizeInBytes = item.getSize();*/
		    	fileItem = item;
		    }
		}
		if(fileItem != null){
			String storePath = docStorePath + File.separator + params.get("ownerID");
	        File storeDir = new File(storePath);
	        if(!(storeDir.exists() && storeDir.isDirectory())){
	        	storeDir.mkdirs();
	        }
	        File toStoreFile = new File(storePath + File.separator + params.get("storeFileName"));
	        fileItem.write(toStoreFile);
		}
				
	}

	
	
	
}
