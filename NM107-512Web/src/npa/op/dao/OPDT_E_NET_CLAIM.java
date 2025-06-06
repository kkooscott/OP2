package npa.op.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import npa.op.util.DaoUtil;
import npa.op.util.DateUtil;
import npa.op.util.ExceptionUtil;
import npa.op.util.NPALOG2Util;
import npa.op.util.luence;
import npa.op.util.NPALOG2Util.LOGOPTYPE;
import npa.op.vo.User;
import npa.op.base.BaseDao;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.zxing.WriterException;
import npa.op.util.MailSender;
import npa.op.util.OPUtil;
import npa.op.util.StringUtil;

public class OPDT_E_NET_CLAIM extends BaseDao {
	
	private static OPDT_E_NET_CLAIM instance = null;
	public static OPDT_E_NET_CLAIM getInstance() {
		if (instance == null) {
			instance = new OPDT_E_NET_CLAIM();
		}
		return instance;
	}
	
	/**
        * 查詢民眾網路認領訊息資料 .
        * 
        * @param jObj
        * @return CachedRowSet
        */
        public CachedRowSet PuNetQueryForDown(JSONObject jObj) {
                ArrayList args = new ArrayList();
                List listkey = null;
                //序號, 基本資料序號, 填單日期, 收據編號, 認領人姓名, 認領人證號,
                //遺失日期, 遺失時間, 遺失地點, 物品描述, 是否審核, 受理日期
                String sql = " SELECT A.OP_SEQ_NO, A.OP_BASIC_SEQ_NO, A.OP_FM_DATE, A.OP_AC_RCNO, A.OP_PUCP_NAME, A.OP_PUCP_IDN,"
                           + " A.OP_LOST_DATE, A.OP_LOST_TIME, A.OP_LOST_CITY_NM, A.OP_LOST_TOWN_NM, A.OP_LOST_PLACE, A.OP_LOST_OJ_DESC, A.OP_YN_LOSER, B.OP_AC_DATE"                             
                           + " FROM "
                           + " OPDT_E_NET_CLAIM A, OPDT_I_PU_BASIC B"
                           + " WHERE 1 = 1 "
                           + " AND A.OP_BASIC_SEQ_NO = B.OP_SEQ_NO";
                ///受理單位
                if(!StringUtil.nvl(jObj.getString("OP_UNITLEVEL4")).equals("")){
                    sql += " AND B.OP_AC_UNIT_CD = ?";
                    args.add( jObj.get("OP_UNITLEVEL4"));
                } else if(!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")){
                    sql += " AND B.OP_AC_B_UNIT_CD = ?";
                    args.add( jObj.get("OP_UNITLEVEL3"));
                } else if(!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")){
                    sql += " AND B.OP_AC_D_UNIT_CD = ?";
                    args.add( jObj.get("OP_UNITLEVEL2"));
                }
                
                //認領填單日期起迄
                if(!jObj.get("OP_FM_DATE_S").toString().equals("")){
                    String tmpdata_s = jObj.get("OP_FM_DATE_S").toString();
                    if (!jObj.get("OP_FM_DATE_S").equals("")){
                        sql += " AND A.OP_FM_DATE >= ?";
                        args.add( tmpdata_s);
                    }
                }
                if(!jObj.get("OP_FM_DATE_E").toString().equals("")){
                    String tmpdata_e =jObj.get("OP_FM_DATE_E").toString();
                    if (!jObj.get("OP_FM_DATE_E").equals("")){
                        sql += " AND A.OP_FM_DATE <= ?";
                        args.add( tmpdata_e);
                    }
                }
                
                //是否審核
                if ( jObj.has("OP_YN_LOSER") ){
                    if(jObj.get("OP_YN_LOSER").equals("Y"))
                        sql += " AND A.OP_YN_LOSER IN ('1','2','3')";
                    else
                        sql += " AND A.OP_YN_LOSER IN ('0', '', null)";
                }
                sql += " ORDER BY  A.OP_FM_DATE DESC";
                CachedRowSet crs = this.pexecuteQueryRowSetHasMaxRows(sql,args.toArray());
                
                return crs;
        }
        
        /**
        * 查詢該收據編號是否有認領權人 .
        * 
        * @param jObj
        * @return CachedRowSet
        */
        public CachedRowSet checkLostForPuNetClm(JSONObject jObj) {
                ArrayList args = new ArrayList();
                List listkey = null;
                String sql = " SELECT *"                           
                           + " FROM "
                           + " OPDT_E_NET_CLAIM "
                           + " WHERE 1 = 1 ";
                
                sql += " AND OP_YN_LOSER = '1'";
                
                //遺失物事件基本資料序號
                if (!jObj.get("OP_BASIC_SEQ_NO").equals("")){
                    sql += " AND OP_BASIC_SEQ_NO = ?";
                    args.add( Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO")));
                }
                
                //認領序號
                if (!jObj.get("OP_SEQ_NO").equals("") && jObj.get("OP_CLAIM_TP_CD").equals("1")){
                    sql += " AND OP_SEQ_NO <> ?";
                    args.add( Integer.valueOf((String) jObj.get("OP_SEQ_NO")));
                }

                CachedRowSet crs = this.pexecuteQueryRowSet(sql,args.toArray());
                
                return crs;
        }
        
        //region 明細
	/**
	 * 查詢民眾認領資料明細 .
	 * 
	 * @param OP_SEQ_NO
	 * @return JSONArray
	 * @throws SQLException 
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public JSONArray queryNetClmById(int OP_SEQ_NO) throws SQLException, IOException, WriterException {
		ArrayList args = new ArrayList();
		CachedRowSet crs1,crs2;
		String sql = " SELECT *"
                           + " FROM OPDT_E_NET_CLAIM"
                           + " WHERE OP_SEQ_NO = ? ";
                args.add(OP_SEQ_NO);
				
		ArrayList<HashMap> list = this.pexecuteQuery(sql,args.toArray());
		
                String OP_FM_DATE = "", OP_PUCP_BIRTHDT = "", OP_LOST_DATE = "", OP_LOST_TIME = "";
                OP_FM_DATE = DateUtil.to7TwDateTime(list.get(0).get("OP_FM_DATE").toString());
		list.get(0).put("OP_FM_DATE", OP_FM_DATE);
                if( list.get(0).get("OP_PUCP_BEFROC").toString().equals("2") ){ //西元
                    //直接出去
                }else{ //民前 民國
                    OP_PUCP_BIRTHDT = DateUtil.convertBirthdayTime(list.get(0).get("OP_PUCP_BIRTHDT").toString(), list.get(0).get("OP_PUCP_BEFROC").toString());
                    list.get(0).put("OP_PUCP_BIRTHDT", OP_PUCP_BIRTHDT);
                }
                OP_LOST_DATE = DateUtil.to7TwDateTime(list.get(0).get("OP_LOST_DATE").toString());
		list.get(0).put("OP_LOST_DATE", OP_LOST_DATE);
                OP_LOST_TIME = DateUtil.to4TwTime(list.get(0).get("OP_LOST_TIME").toString());
		list.get(0).put("OP_LOST_TIME", OP_LOST_TIME);
                
		return arrayList2JsonArray(list);
	}
        
        //region 明細
	/**
	 * 查詢民眾認領資料明細 .(DB原始資料)
	 * 
	 * @param OP_SEQ_NO
	 * @return JSONArray
	 * @throws SQLException 
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public JSONArray queryNetClmByIdOriginal(int OP_SEQ_NO) throws SQLException, IOException, WriterException {
		ArrayList args = new ArrayList();
		CachedRowSet crs1,crs2;
		String sql = " SELECT *"
                           + " FROM OPDT_E_NET_CLAIM"
                           + " WHERE OP_SEQ_NO = ? ";
                args.add(OP_SEQ_NO);
				
		ArrayList<HashMap> list = this.pexecuteQuery(sql,args.toArray());
		
		return arrayList2JsonArray(list);
	}
        
        // region 更新 Model Start
        /**
         * 更新遺失物認領資料.
         * 
         * @param jObj 審核資料欄位
         * @return boolean.
         */
        public boolean update(JSONObject jObj) {
                OPDT_I_PU_BASIC iPuBasicDao = new OPDT_I_PU_BASIC();
                OPDT_I_FNSH iFnshDao = new OPDT_I_FNSH();
                boolean returnValue = false;
                StringBuilder sql = new StringBuilder();
                Date current = new Date();

                try {
                        User voUser = new User();
                        OPUtil opUtil = new OPUtil();
                        voUser = (User) jObj.get("userVO");

                        jObj.put("OP_REMARK", jObj.get("OP_REMARK"));
                        jObj.put("OP_YN_LOSER", jObj.get("OP_YN_LOSER"));          
                        jObj.put("OP_UPD_ID", voUser.getUserId());
                        jObj.put("OP_UPD_NM", voUser.getUserName());
                        jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_UPD_UNIT_NM",  voUser.getUnitName());
                        jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
                        

                        DaoUtil util = new DaoUtil();
                        jObj = util.getStaticColumn(jObj, "UPD");

                        String arryString[] = {//"OP_SEQ_NO",
                                        "OP_REMARK",
                                        "OP_YN_LOSER",
                                        "OP_UPD_ID",
                                        "OP_UPD_NM", //5
                                        "OP_UPD_UNIT_CD",
                                        "OP_UPD_UNIT_NM",
                                        "OP_UPD_DT_TM"}; //8

                        sql.append("UPDATE OPDT_E_NET_CLAIM SET ");

                        for (int index = 0 ;index < arryString.length ;index++){
                                sql.append(arryString[index] + "=?, ");
                        }
                        sql.replace(sql.lastIndexOf(","), sql.lastIndexOf(",")+1, "");
                        sql.append("WHERE OP_SEQ_NO =? ");


                        Object[] paraObject = new Object[arryString.length+1];

                        // paramenter
                        int i = 0;
                        for (String strKey:arryString){
                                paraObject[i] = new Object[i];
                                if (jObj.has(strKey))
                                        paraObject[i] = jObj.get(strKey);
                                i++;
                        }
                        //paraObject[arryString.length+1] = new Object[arryString.length+1];
                        paraObject[arryString.length] = jObj.getInt("OP_SEQ_NO");

                        returnValue = this.pexecuteUpdate(sql.toString(), paraObject) > 0 ? true : false;
                        
                        jObj.put("OP_SEQ_NO", jObj.get("OP_SEQ_NO"));
                        jObj.put("OP_CLAIM_TP_CD", jObj.get("OP_CLAIM_TP_CD"));
                        
                        if ( returnValue ){
                            if( jObj.get("OP_YN_LOSER").equals("2") ){  //若確定不是有認領權人 寄Email
                                MailSender mailSender = new MailSender();
                                if( !jObj.get("OP_PUCP_EMAIL").toString().trim().equals("") ){
                                    String OP_MAIL_CONTENT = "";
                                    CachedRowSet crs1 =null;
                                    JSONObject jObject = new JSONObject();
                                    jObject.put("OP_D_UNIT_CD", voUser.getUnitCd1());
                                    jObject.put("OP_B_UNIT_CD", voUser.getUnitCd2());
                                    SystemDao systemDao = SystemDao.getInstance();
                                    crs1 = systemDao.checkUnitForMail(jObject);
                                    if ( crs1.next() ){
                                        OP_MAIL_CONTENT = crs1.getString("OP_MAIL_CONTENT");
                                    }
                                    if( !OP_MAIL_CONTENT.equals("") ){
                                        mailSender.doClmCancel(jObj.get("OP_PUCP_NAME").toString(), 
                                            jObj.get("OP_AC_RCNO").toString(), 
                                            OP_MAIL_CONTENT, 
                                            jObj.get("OP_PUCP_EMAIL").toString());
                                    }
                                }
                                
                            }
                            
                            if( jObj.has("OP_YN_FS") ){ //是否立即結案
                                if( jObj.get("OP_YN_FS").equals("1") ){ //是
                                    boolean fnshbol = false;
                                    //BASIC狀態更改成功後，新增一筆至結案table
                                    jObj.put("OP_YN_FS", "1");
                                    jObj.put("ACTION_FROM", "CLAIM"); //認領
                                    fnshbol = iFnshDao.add( jObj );
                                    
                                    if( fnshbol ){ //將未審核之有認領權人 更改為否
                                        OPDT_I_PU_CLAIM iPuClaimDao = new OPDT_I_PU_CLAIM();
                                        iPuClaimDao.updateClaimSetNo( jObj );
                                    }
                                }
                            }
                                returnValue = true;
                        }else{
                                returnValue = false;
                        }

                } catch (Exception e) {
                        log.error(ExceptionUtil.toString(e));
                }
                return returnValue;

        }
        
        public JSONArray countNetCase(JSONObject jObj) throws SQLException, IOException, WriterException {
		ArrayList args = new ArrayList();

                String sql = " SELECT COUNT(*) as NETCOUNT"                          
                           + " FROM "
                           + " OPDT_E_NET_CLAIM"
                           + " WHERE 1 = 1 ";

                //三層單位
                if( jObj.has("OP_PERMISSION") && jObj.get("OP_PERMISSION").equals("3") ){ //警局
                    if (!jObj.get("OP_UNITLEVEL2").equals("")){
                        sql += " AND substring(OP_AC_UNIT_CD,1,2) = ?";
                        args.add( jObj.getString("OP_UNITLEVEL2").substring(0, 2));
                    }
                }else if ( jObj.has("OP_PERMISSION") && jObj.get("OP_PERMISSION").equals("2") ){ //分局
                    if (!jObj.get("OP_UNITLEVEL3").equals("")){
                        sql += " AND substring(OP_AC_UNIT_CD,1,3) = ?";
                        args.add( jObj.getString("OP_UNITLEVEL3").substring(0, 3));
                    }else if (!jObj.get("OP_UNITLEVEL2").equals("")){
                        sql += " AND substring(OP_AC_UNIT_CD,1,2) = ?";
                        args.add( jObj.getString("OP_UNITLEVEL2").substring(0, 2));
                    }
                }else{
                    if (!jObj.get("OP_UNITLEVEL4").equals("")){
                        sql += " AND OP_AC_UNIT_CD = ?";
                        args.add( jObj.get("OP_UNITLEVEL4"));
                    }else if (!jObj.get("OP_UNITLEVEL3").equals("")){
                        sql += " AND substring(OP_AC_UNIT_CD,1,3) = ?";
                        args.add( jObj.getString("OP_UNITLEVEL3").substring(0, 3));
                    }else if (!jObj.get("OP_UNITLEVEL2").equals("")){
                        sql += " AND substring(OP_AC_UNIT_CD,1,2) = ?";
                        args.add( jObj.getString("OP_UNITLEVEL2").substring(0, 2));
                    }
                }
                
                
                sql += " AND OP_YN_LOSER IN ('0', '', null)";
                
		ArrayList<HashMap> list = this.pexecuteQueryNoLog(sql,args.toArray());
		     
		return arrayList2JsonArray(list);
	}
		
}
