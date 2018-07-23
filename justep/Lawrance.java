package justep;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import javax.naming.NamingException;
import sun.misc.BASE64Decoder;
import com.alibaba.fastjson.JSONObject;
import com.justep.baas.action.ActionContext;


public class Lawrance {
	public static JSONObject uploadImageAjax(JSONObject params, ActionContext context) throws SQLException, NamingException {
		// 参数序列化
		
				JSONObject jsonObj = new JSONObject();
				String image = params.getString("image"); // 拿到字符串格式的图片
				
				String baasPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + ".." + File.separator + ".."; 
				  
				String docStorePath = baasPath + File.separator + "model" +File.separator + "UI2" + File.separator + "demo" +File.separator + "picCut" +File.separator;//储存图片的位置
				String PicName = params.getString("PicName");
				
				// 去掉头部
				image = image.split(",")[1];
				// 写入磁盘
				String success = "fail";
				BASE64Decoder decoder = new BASE64Decoder();
				try {
					byte[] decodedBytes = decoder.decodeBuffer(image); // 将字符串格式的image转为二进制流（biye[])的decodedBytes
					String imgFilePath = docStorePath + PicName; // 指定图片要存放的位置
					FileOutputStream out = new FileOutputStream(imgFilePath); // 新建一个文件输出器，并为它指定输出位置imgFilePath
					out.write(decodedBytes); // 利用文件输出器将二进制格式decodedBytes输出
					out.close(); // 关闭文件输出器
					success = "上传文件成功！";
					System.out.println("上传文件成功！");

				} catch (Exception e) {
					success = "上传文件失败！|" + e.getMessage();
					e.printStackTrace();
				} finally {
					jsonObj.put("success", success);

					
				}
				return jsonObj;
	}
}
