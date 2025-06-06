package npa.op.control;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.aspose.words.DataSet;
import com.aspose.words.DataTable;

import npa.op.base.AjaxBaseServlet;
import npa.op.dao.OPDT_E_NET_CLAIM;
import npa.op.dao.OPDT_I_PHOTO;
import npa.op.dao.OPDT_I_PU_BASIC;
import npa.op.dao.OPDT_I_PU_DETAIL;
//import npa.op.util.AesCrypt;
import npa.op.util.DateUtil;
import npa.op.util.ExceptionUtil;
import npa.op.util.ExportUtil;
import npa.op.util.NPALOG2Util;
import npa.op.util.NPALOG2Util.LOGOPTYPE;
import static npa.op.util.StringUtil.replaceWhiteChar;
import npa.op.vo.User;

/**
 * 照片處理
 */
@WebServlet("/PuPicServlet")
public class PuPicServlet extends AjaxBaseServlet {
	private static final long serialVersionUID = 1L;
	Logger log = Logger.getLogger(PuPicServlet.class);

	@Override
	protected void executeAjax(HttpServletRequest request, HttpServletResponse response, 
			HttpSession session, User user, JSONObject argJsonObj, JSONObject returnJasonObj) throws Exception {
		
		//取得登入人員資訊
		argJsonObj.put("userVO", user);
		ResourceBundle resource = ResourceBundle.getBundle("npa");
		//AesCrypt crypt = new AesCrypt(resource.getString("crypt"));
		JSONObject jObject;
		JSONArray resultDataArray = new JSONArray();
		JSONArray resultDataArray1 = new JSONArray();
		ExportUtil exportUtil = new ExportUtil();
		CachedRowSet crs1 = null;
		CachedRowSet crs2 = null;
		CachedRowSet crs3 = null;
		NPALOG2Util NPALOG2 = new NPALOG2Util();
                HttpSession tmpsession = null;
		HttpSession Opsession = null;
		OPDT_I_PHOTO iPhotoDao = new OPDT_I_PHOTO();
		JSONArray resultjArray  = new JSONArray();
		
		boolean result;
		switch (argJsonObj.getString(AJAX_REQ_ACTION_KEY)) {			
                    case "GET_PICTURE":			
                        resultDataArray = iPhotoDao.getPicture(argJsonObj);
                        for(int i=0;i<resultDataArray.length();i++){
                            String strPath = resultDataArray.getJSONObject(i).getString("OP_FILE_PATH")+resultDataArray.getJSONObject(i).getString("OP_UPL_FILE_NAME");
                            //String url = "PuPicServlet?file="+strPath;
                            String url = "ImageServlet?file="+strPath;
                            //url = java.net.URLEncoder.encode(url,"UTF-8");
                            resultDataArray.getJSONObject(i).put("OP_FILE_PATH_ORG", resultDataArray.getJSONObject(i).getString("OP_FILE_PATH"));
                            resultDataArray.getJSONObject(i).put("OP_FILE_PATH", url);
                            resultDataArray.getJSONObject(i).put("OP_SEQ_NO", resultDataArray.getJSONObject(i).getString("OP_SEQ_NO"));
                            resultDataArray.getJSONObject(i).put("OP_UPL_FILE_NAME", resultDataArray.getJSONObject(i).getString("OP_UPL_FILE_NAME"));
                            System.out.println("############# GET_PICTURE url "+url);
                        }
//                        tmpsession = request.getSession();
//                        tmpsession.invalidate();
//                        tmpsession = request.getSession(true);
                        
//                        tmpsession = request.getSession();
//                        tmpsession.setAttribute("beforeModifyPic", resultDataArray);
                        //Countermeasures
//                        tmpsession.invalidate();
//                        tmpsession=request.getSession(true);
                        
//                        tmpsession.setAttribute("beforeModifyPic", validator.Validator.validateAttr(resultDataArray));
                        this.setDDLData(returnJasonObj,resultDataArray);
                    break;
		}
	}
}
