package npa.op.control;

import npa.op.dao.OPDT_CD_DATADao;
import npa.op.dao.OPDT_E0_NPAUNITDao;
import npa.op.dao.OPDT_E0_CITYDao;
import npa.op.dao.OPDT_E0_TOWNDao;
import npa.op.dao.OPDT_E0_VILLAGEDao;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.aspose.words.DataSet;
import com.aspose.words.DataTable;
import npa.op.dao.E0DT_APPUSEDao; 

import npa.op.util.ExceptionUtil;
import npa.op.util.ExportUtil;
import npa.op.vo.User;
import npa.op.base.AjaxBaseServlet;
import npa.op.dao.E8DT_GETDATADao;
import npa.op.dao.OPDT_E0_COLORDao;
import npa.op.dao.OPDT_E0_COUNTRYDao;
import npa.op.dao.OPDT_FUNCDao;
import npa.op.dao.OPDT_OBJECT_CD_COMPAREDao;
import npa.op.dao.OPDT_ROLEDao;
import npa.op.dao.SSDB_SSTB01Dao;
import npa.op.util.NpaConfig;
import org.json.JSONArray;

;

/**
 *
 */
@WebServlet("/UtilServlet")
public class UtilServlet extends AjaxBaseServlet {

    private static final long serialVersionUID = 1L;
    Logger log = Logger.getLogger(UtilServlet.class);

    @Override
    protected void executeAjax(HttpServletRequest request,
            HttpServletResponse response, HttpSession session, User user,
            JSONObject argJsonObj, JSONObject returnJasonObj) throws Exception {

        switch (argJsonObj.getString(AJAX_REQ_ACTION_KEY)) {
            case "NPAUNIT":
                OPDT_E0_NPAUNITDao daoNPAUNIT = OPDT_E0_NPAUNITDao.getInstance();
                this.setDDLData(
                        returnJasonObj,
                        daoNPAUNIT.getUnitByLevel(
                                argJsonObj.getString("QDDL_LEVEL"),
                                argJsonObj.getString("QDDL_LEVEL2"),
                                argJsonObj.getString("QDDL_LEVEL3")));
                break;
            case "CITY":
                OPDT_E0_CITYDao daoCITY = OPDT_E0_CITYDao.getInstance();
                this.setDDLData(returnJasonObj, daoCITY.getAllCity());
                break;
            case "TOWN":
                OPDT_E0_TOWNDao daoTOWN = OPDT_E0_TOWNDao.getInstance();
                this.setDDLData(returnJasonObj, daoTOWN.getCityByQS(argJsonObj.getString("OP_CITY_CD")));
                break;
            case "VILLAGE":
                OPDT_E0_VILLAGEDao daoVILLAGE = OPDT_E0_VILLAGEDao.getInstance();
                this.setDDLData(returnJasonObj, daoVILLAGE.getCityByQS(argJsonObj.getString("OP_TOWN_CD")));
                break;
            case "TYPE":
                OPDT_CD_DATADao daoTYPE = OPDT_CD_DATADao.getInstance();
                this.setDDLData(returnJasonObj, daoTYPE.getTYPEByQS(argJsonObj.getString("OP_CD_TYPE")));
                break;
            case "COUNTRY":
                OPDT_E0_COUNTRYDao daoCOUNTRY = OPDT_E0_COUNTRYDao.getInstance();
                this.setDDLData(returnJasonObj, daoCOUNTRY.getAllCountry());
                break;
            case "COLOR":
                OPDT_E0_COLORDao daoCOLOR = OPDT_E0_COLORDao.getInstance();
                this.setDDLData(returnJasonObj, daoCOLOR.getAllColor());
                break;
            case "FUNC":
                OPDT_ROLEDao daoFUNC = OPDT_ROLEDao.getInstance();
                this.setDDLData(returnJasonObj, daoFUNC.getROLE());
                break;
            case "FUNCGROUP":
                OPDT_FUNCDao daoFUNCGROUP = OPDT_FUNCDao.getInstance();
                this.setDDLData(returnJasonObj, daoFUNCGROUP.getFUNCGROUP());
                break;
            case "AUDITNAME":
                SSDB_SSTB01Dao SSTB = SSDB_SSTB01Dao.getInstance();
                this.setDDLData(returnJasonObj, SSTB.GetSSDBROLE(argJsonObj.getString("unit_D"), argJsonObj.getString("unit_B"), argJsonObj.getString("unit")));
                break;
            case "E8TYPE":
                E8DT_GETDATADao daoE8TYPE = E8DT_GETDATADao.getInstance();
                this.setDDLData(returnJasonObj, daoE8TYPE.getTYPEByQS(argJsonObj.getString("E8_CD_TYPE")));
                break;
            case "E8LIST":
                E8DT_GETDATADao daoE8LIST = E8DT_GETDATADao.getInstance();
                this.setDDLData(returnJasonObj, daoE8LIST.getLISTByQS(argJsonObj.getString("E8_LIST_TYPE")));
                break;
            case "BINDDATE"://OPDT_OBJECT_CD_COMPARE
                OPDT_OBJECT_CD_COMPAREDao daoBINDDATE = OPDT_OBJECT_CD_COMPAREDao.getInstance();
                this.setDDLData(returnJasonObj, daoBINDDATE.checkDate(argJsonObj.getString("OP_SEQ_NO")));
                break;
            case "E8LISTN":
                OPDT_OBJECT_CD_COMPAREDao daoE8LISTN = OPDT_OBJECT_CD_COMPAREDao.getInstance();
                this.setDDLData(returnJasonObj, daoE8LISTN.getTYPEByQS());
                break;
            case "UNIT":
                OPDT_E0_NPAUNITDao daoNPAUNIT1 = OPDT_E0_NPAUNITDao.getInstance();
                this.setDDLData(returnJasonObj, daoNPAUNIT1.getUnitCode(argJsonObj.getString("UNITCODE")));
                break;
            case "ROLE":
//			CPDT_ROLE daoRole = CPDT_ROLE.getInstance();
//			this.setDDLData(returnJasonObj, daoRole.getRoleByQS(argJsonObj.getString("CP_ROLE_CD")));
                break;
            case "GETONLINEDAY":
                JSONArray resultDataArray = new JSONArray();
                String DAY = NpaConfig.getString("Online_Day");
                JSONObject dayJObject = new JSONObject();
                dayJObject.put("DAY", DAY);
                resultDataArray.put(dayJObject);
                this.setFormData(returnJasonObj, resultDataArray);

                break;
            case "CLEARSESSION":
//                        session.setAttribute("ACTION", validator.Validator.validateAttr(""));
                this.setFormData(returnJasonObj, true);
                break;
            case "PERMISSION":
                if (user != null) {
                    JSONObject jObject = new JSONObject();
                    jObject.put("NAME", user.getUserName());
                    jObject.put("ID", user.getUserId());
                    jObject.put("TEL", user.getUnitTel());
                    jObject.put("TITLE", user.getUserTitle());
                    jObject.put("RolePermission", user.getRolePermission());
                    jObject.put("UNITLEVEL1", user.getUnitCd1());
                    jObject.put("UNITLEVEL2", user.getUnitCd2());
                    jObject.put("UNITLEVEL3", user.getUnitCd3());
                    jObject.put("Roles", user.getOwnRole());

                    this.setFormData(returnJasonObj, jObject);
                } else {
                    log.error("遺失使用者登入資訊");
                }
                break;
            case "getDBtime":
                daoNPAUNIT = OPDT_E0_NPAUNITDao.getInstance();
                resultDataArray = daoNPAUNIT.getDBTime();
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            //202403  『300行政類 301受處理報案』
            case "appendSelect":
                E0DT_APPUSEDao dao    = E0DT_APPUSEDao.getInstance();
                resultDataArray = dao.appendSelect(argJsonObj);
                this.setFormData(returnJasonObj, resultDataArray);
                break;
                
            case "appendSubSelect":
                E0DT_APPUSEDao daoSub = E0DT_APPUSEDao.getInstance();
                resultDataArray = daoSub.appendSubSelect(argJsonObj);
                this.setFormData(returnJasonObj, resultDataArray);
                break;
        }

    }
}
