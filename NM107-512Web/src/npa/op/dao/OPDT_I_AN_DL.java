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

public class OPDT_I_AN_DL extends BaseDao {
	
	private static OPDT_I_AN_DL instance = null;
	public static OPDT_I_AN_DL getInstance() {
		if (instance == null) {
			instance = new OPDT_I_AN_DL();
		}
		return instance;
	}
        
        /**
	 * 查詢公告期滿資料明細 .(DB原始資料)
	 * 
	 * @param OP_BASIC_SEQ_NO
	 * @return JSONArray
	 * @throws SQLException 
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public JSONArray queryAnDlByBasicIdOriginal(int OP_BASIC_SEQ_NO) throws SQLException, IOException, WriterException {
		ArrayList args = new ArrayList();
                
                String sql = " SELECT *"
                    + " FROM OPDT_I_AN_DL"
                    + " WHERE OP_BASIC_SEQ_NO = ? ";
                args.clear();
                args.add(OP_BASIC_SEQ_NO);
				
		ArrayList<HashMap> list = this.pexecuteQuery(sql,args.toArray());
		
		return arrayList2JsonArray(list);
	}

	/**
	 * 查詢公告期滿資料明細 .
	 * 
	 * @param OP_BASIC_SEQ_NO
	 * @return JSONArray
	 * @throws SQLException 
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public JSONArray queryAnDlByBasicId(int OP_BASIC_SEQ_NO, User user) throws SQLException, IOException, WriterException {
		ArrayList args = new ArrayList();
                JSONArray resultDataArray = new JSONArray();
                JSONObject jObject = new JSONObject();
                GetSerialNoDao daoGetSerialNo = GetSerialNoDao.getInstance();
                int OP_MAX_PRMNO=0;
                // 目前時間
                java.util.Date now = new java.util.Date();
                // 設定日期格式
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
                String YYYYMM = sdf.format(now);
                String year = YYYYMM.substring(0, 4);
                String mm = YYYYMM.substring(4, 6);
                String YYYMM = DateUtil.getYearE2C(year) + mm;
                String GetSeqNo = "";
                //發文字維護檔
                String sql = " SELECT *"
                        + " FROM OPDT_DOC_WD_PARA"
                        + " WHERE OP_B_UNIT_CD = ? ";
                args.clear();
                args.add( user.getUnitCd2() );
                ArrayList<HashMap> docWdlist = this.pexecuteQuery(sql,args.toArray());
                
                //招領期滿
                sql = " SELECT *"
                    + " FROM OPDT_I_AN_DL"
                    + " WHERE OP_BASIC_SEQ_NO = ? ";
                args.clear();
                args.add( OP_BASIC_SEQ_NO );
				
		ArrayList<HashMap> list = this.pexecuteQuery(sql,args.toArray());
                
                if( list.size() == 0 ){ //招領期滿沒有資料
                    jObject.put("ACTION_TYPE", "insertIAnDlList");
                    jObject.put("OP_SEQ_NO", "");
                    
                    //明細
                    sql = " SELECT *"
                        + " FROM OPDT_I_PU_DETAIL"
                        + " WHERE OP_BASIC_SEQ_NO = ? ";
                
                    ArrayList<HashMap> detaillist = this.pexecuteQuery(sql,args.toArray());

                    String puojNm = "";
                    if(!detaillist.isEmpty()){
                        for(int i = 1; i <= detaillist.size(); i++){
                            puojNm = puojNm + detaillist.get(i-1).get("OP_PUOJ_NM");
                            if( i <= detaillist.size()-1 )
                                puojNm = puojNm + "、";
                        }
                    }
                    //受理基本資料
                    sql = " SELECT *"
                        + " FROM OPDT_I_PU_BASIC"
                        + " WHERE OP_SEQ_NO = ? ";
                    ArrayList<HashMap> basiclist = this.pexecuteQuery(sql,args.toArray());
                    
                    String dateStr = "";
                    if(!basiclist.isEmpty()){
                        dateStr = DateUtil.to7TwDateWithCB( basiclist.get(0).get("OP_PU_DATE").toString() );
                    }
                    
                    jObject.put("OP_PUPO_AN_CONT", " 台端" + dateStr + "拾得" + puojNm + 
                            "等一批，業經本局（分局）公告招領期滿，無人認領，該等拾得物應歸  台端所有。請持攜帶國民身分證、" + 
                            "拾得物收據至本局（分局）刑警大隊（偵查隊）領取。台端如於受通知或公告後三個月內未領取者，" + 
                            "其物或賣得之價金歸屬於保管地之地方自治團體。");
                    //招領
                    sql = " SELECT *"
                        + " FROM OPDT_I_ANNOUNCE"
                        + " WHERE OP_BASIC_SEQ_NO = ? ";
                    
                    ArrayList<HashMap> ann1list = this.pexecuteQuery(sql,args.toArray());
                    if(!ann1list.isEmpty()){
                        jObject.put("OP_AN_DATE_END", ann1list.get(0).get("OP_AN_DATE_END"));
                    }
                    jObject.put("OP_NTC_PUPO_DT", "");
                    jObject.put("OP_PUPO_ANDTEND", "");
                    
                    if(!docWdlist.isEmpty()){
                        jObject.put("OP_PUPO_DOC_WD", docWdlist.get(0).get("OP_DOC_WD2"));
                        jObject.put("OP_YN_GET_NO2", docWdlist.get(0).get("OP_YN_GET_NO2"));
                        if( docWdlist.get(0).get("OP_YN_GET_NO2").equals("1") ){ //如果是自動取號
                            OP_MAX_PRMNO = daoGetSerialNo.querySerialNo(YYYYMM, user.getUnitCd2(), "2");
                            if( OP_MAX_PRMNO == 0 ){ // 代表序號目前沒有資料但是還是要帶出值來
                                GetSeqNo = YYYMM + "00001";
                                jObject.put("OP_PUPO_DOC_NO", GetSeqNo);
                            }else{
                                GetSeqNo = YYYMM + String.format("%05d", OP_MAX_PRMNO+1);
                                jObject.put("OP_PUPO_DOC_NO", GetSeqNo);
                            }
                        }else{
                            jObject.put("OP_PUPO_DOC_NO", "");
                        }
                    }else{
                        jObject.put("OP_YN_GET_NO2", "0");
                        jObject.put("OP_PUPO_DOC_WD", "");
                        jObject.put("OP_PUPO_DOC_NO", "");
                    }
                    
                    resultDataArray.put(jObject);
                    return resultDataArray;
                }else{
                    list.get(0).put("ACTION_TYPE", "updateIAnDlList");
                    String OP_PR_DATE="",OP_NTC_PUPO_DT="",OP_PUPO_ANDTEND="";
                    if( !list.get(0).get("OP_PR_DATE").toString().equals("") ){
                        OP_PR_DATE = DateUtil.to7TwDateTime(list.get(0).get("OP_PR_DATE").toString());
                        list.get(0).put("OP_PR_DATE", OP_PR_DATE);
                    }
                    if( !list.get(0).get("OP_NTC_PUPO_DT").toString().equals("") ){
                        OP_NTC_PUPO_DT = DateUtil.to7TwDateTime(list.get(0).get("OP_NTC_PUPO_DT").toString());
                        list.get(0).put("OP_NTC_PUPO_DT", OP_NTC_PUPO_DT);
                    }
                    if( !list.get(0).get("OP_PUPO_ANDTEND").toString().equals("") ){
                        OP_PUPO_ANDTEND = DateUtil.to7TwDateTime(list.get(0).get("OP_PUPO_ANDTEND").toString());
                        list.get(0).put("OP_PUPO_ANDTEND", OP_PUPO_ANDTEND);
                    }
                    if(!docWdlist.isEmpty()){
                        list.get(0).put("OP_YN_GET_NO2", docWdlist.get(0).get("OP_YN_GET_NO2"));
                    }else{
                        list.get(0).put("OP_YN_GET_NO2", "0");
                    }
                    

                    return arrayList2JsonArray(list);
                }

	}
        
        /**
	 * 顯示認領期滿資料明細 .
	 * 
	 * @param OP_BASIC_SEQ_NO
	 * @return JSONArray
	 * @throws SQLException 
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public JSONArray showAnDlByBasicId(int OP_BASIC_SEQ_NO) throws SQLException, IOException, WriterException {
		ArrayList args = new ArrayList();
                JSONArray resultDataArray = new JSONArray();
                JSONObject jObject = new JSONObject();
		CachedRowSet crs1,crs2;
                //招領期滿
		String sql = " SELECT *"
                           + " FROM OPDT_I_AN_DL"
                           + " WHERE OP_BASIC_SEQ_NO = ? ";
                args.add( OP_BASIC_SEQ_NO );
				
		ArrayList<HashMap> list = this.pexecuteQuery(sql,args.toArray());
                
                if( list.size() == 0 ){ //招領期滿沒有資料
                    jObject.put("OP_SEQ_NO", "");
                    jObject.put("OP_PR_UNIT_CD", "");
                    jObject.put("OP_PR_STAFF_NM", "");
                    jObject.put("OP_PR_DATE", "");
                    jObject.put("OP_YN_AUCTION", "N");
                    jObject.put("OP_NTC_PUPO_DT", "");
                    jObject.put("OP_PUPOANUNITCD", "");
                    jObject.put("OP_PUPO_DOC_WD", "");
                    jObject.put("OP_PUPO_DOC_NO", "");
                    jObject.put("OP_PUPO_ANDTEND", "");
                    jObject.put("OP_PUPO_AN_CONT", "");
                    jObject.put("OP_PR_STAT_DESC", "");
                    
                    resultDataArray.put(jObject);
                    return resultDataArray;
                }else{
                    String OP_PR_DATE="",OP_NTC_PUPO_DT="",OP_PUPO_ANDTEND="";
                    if( !list.get(0).get("OP_PR_DATE").toString().equals("") ){
                        OP_PR_DATE = DateUtil.to7TwDateTime(list.get(0).get("OP_PR_DATE").toString());
                        list.get(0).put("OP_PR_DATE", OP_PR_DATE);
                    }
                    if( !list.get(0).get("OP_NTC_PUPO_DT").toString().equals("") ){
                        OP_NTC_PUPO_DT = DateUtil.to7TwDateTime(list.get(0).get("OP_NTC_PUPO_DT").toString());
                        list.get(0).put("OP_NTC_PUPO_DT", OP_NTC_PUPO_DT);
                    }
                    if( !list.get(0).get("OP_PUPO_ANDTEND").toString().equals("") ){
                        OP_PUPO_ANDTEND = DateUtil.to7TwDateTime(list.get(0).get("OP_PUPO_ANDTEND").toString());
                        list.get(0).put("OP_PUPO_ANDTEND", OP_PUPO_ANDTEND);
                    }

                    return arrayList2JsonArray(list);
                }

	}
        
        /**
	 * 查詢拾得人公告資料明細 .
	 * 
	 * @param OP_BASIC_SEQ_NO
	 * @return JSONArray
	 * @throws SQLException 
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public JSONArray queryIPuanAnnByBasicId(int OP_BASIC_SEQ_NO) throws SQLException, IOException, WriterException {
		ArrayList args = new ArrayList();
                JSONArray resultDataArray = new JSONArray();
                JSONObject jObject = new JSONObject();
		CachedRowSet crs1,crs2;
                //招領期滿
		String sql = " SELECT *"
                           + " FROM OPDT_I_AN_DL"
                           + " WHERE OP_BASIC_SEQ_NO = ? ";
                args.add( OP_BASIC_SEQ_NO );
				
		ArrayList<HashMap> list = this.pexecuteQuery(sql,args.toArray());
                
                if( list.size() == 0 ){ //招領期滿沒有資料
                    jObject.put("OP_PUPO_DOC_WD", "");
                    jObject.put("OP_PUPO_DOC_NO", "");
                    jObject.put("OP_NTC_PUPO_DT", "");
                    jObject.put("OP_PUPOANUNITCD", "");
                    jObject.put("OP_PUPO_AN_CONT", "");  
                    
                    jObject.put("OP_AC_UNIT_TEL", "");
                    jObject.put("OP_PU_DATE", "");
                    jObject.put("OP_PU_TIME", "");
                    jObject.put("OP_PU_CITY_CD", "");
                    jObject.put("OP_PU_TOWN_CD", "");
                    jObject.put("OP_PU_PLACE", "");
                    jObject.put("OP_PUPO_YNANEND", "");
                    
                    resultDataArray.put(jObject);
                    return resultDataArray;
                }else{
                    String OP_PR_DATE="",OP_NTC_PUPO_DT="",OP_PUPO_ANDTEND="";
                    if( !list.get(0).get("OP_PR_DATE").toString().equals("") ){
                        OP_PR_DATE = DateUtil.to7TwDateTime(list.get(0).get("OP_PR_DATE").toString());
                        list.get(0).put("OP_PR_DATE", OP_PR_DATE);
                    }
                    if( !list.get(0).get("OP_NTC_PUPO_DT").toString().equals("") ){
                        OP_NTC_PUPO_DT = DateUtil.to7TwDateTime(list.get(0).get("OP_NTC_PUPO_DT").toString());
                        list.get(0).put("OP_NTC_PUPO_DT", OP_NTC_PUPO_DT);
                    }
                    if( !list.get(0).get("OP_PUPO_ANDTEND").toString().equals("") ){
                        OP_PUPO_ANDTEND = DateUtil.to7TwDateTime(list.get(0).get("OP_PUPO_ANDTEND").toString());
                        list.get(0).put("OP_PUPO_ANDTEND", OP_PUPO_ANDTEND);
                    }
                    
                    //受理基本資料
                    sql = " SELECT *"
                        + " FROM OPDT_I_PU_BASIC"
                        + " WHERE OP_SEQ_NO = ? ";
                    ArrayList<HashMap> basiclist = this.pexecuteQuery(sql,args.toArray());

                    if(!basiclist.isEmpty()){
                        list.get(0).put("OP_AC_D_UNIT_CD", basiclist.get(0).get("OP_AC_D_UNIT_CD"));
                        list.get(0).put("OP_AC_D_UNIT_NM", basiclist.get(0).get("OP_AC_D_UNIT_NM"));
                        list.get(0).put("OP_AC_B_UNIT_CD", basiclist.get(0).get("OP_AC_B_UNIT_CD"));
                        list.get(0).put("OP_AC_B_UNIT_NM", basiclist.get(0).get("OP_AC_B_UNIT_NM"));
                        list.get(0).put("OP_AC_UNIT_CD", basiclist.get(0).get("OP_AC_UNIT_CD"));
                        list.get(0).put("OP_AC_UNIT_NM", basiclist.get(0).get("OP_AC_UNIT_NM"));
                        list.get(0).put("OP_AC_UNIT_TEL", basiclist.get(0).get("OP_AC_UNIT_TEL")); //受理電話
                        list.get(0).put("OP_PU_DATE", DateUtil.to7TwDateTime( basiclist.get(0).get("OP_PU_DATE").toString() )); //拾得日期
                        list.get(0).put("OP_PU_TIME", DateUtil.to4TwTime( basiclist.get(0).get("OP_PU_TIME").toString() )); //拾得時間
                        list.get(0).put("OP_PU_CITY_CD", basiclist.get(0).get("OP_PU_CITY_CD")); //拾得縣市
                        list.get(0).put("OP_PU_TOWN_CD", basiclist.get(0).get("OP_PU_TOWN_CD")); //拾得鄉鎮
                        list.get(0).put("OP_PU_PLACE", basiclist.get(0).get("OP_PU_PLACE")); //拾得地點
                    }

                    return arrayList2JsonArray(list);
                }

	}
        
        /**
        * 查詢該收據編號是否內部公告 .
        * 
        * @param jObj
        * @return CachedRowSet
        */
        public CachedRowSet QueryAllForAnn1(JSONObject jObj) {
                ArrayList args = new ArrayList();
                List listkey = null;
                String sql = " SELECT *"
                           + " FROM OPDT_I_ANNOUNCE"
                           + " WHERE 1 = 1 ";
                
                //遺失物事件基本資料序號
                if (!jObj.get("OP_BASIC_SEQ_NO").equals("")){
                    sql += " AND OP_BASIC_SEQ_NO = ?";
                    args.add( Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO")));
                }

                CachedRowSet crs = this.pexecuteQueryRowSet(sql,args.toArray());
                
                return crs;
        }
        
        /**
	 * 查詢認領資料明細 .
	 * 
	 * @param OP_BASIC_SEQ_NO
	 * @return JSONArray
	 * @throws SQLException 
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public JSONArray getAnnounceByBasicId(int OP_BASIC_SEQ_NO) throws SQLException, IOException, WriterException {
		ArrayList args = new ArrayList();
                JSONArray resultDataArray = new JSONArray();
                JSONObject jObject = new JSONObject();
		CachedRowSet crs1,crs2;
		String sql = " SELECT *"
                           + " FROM OPDT_I_ANNOUNCE"
                           + " WHERE OP_BASIC_SEQ_NO = ? ";
                args.add( OP_BASIC_SEQ_NO );
				
		ArrayList<HashMap> list = this.pexecuteQuery(sql,args.toArray());
                
                if( list.size() == 0 ){ //公告沒有資料
                    
                }else{
                    jObject.put("ACTION_TYPE", "updateAnn1List");
                    jObject.put("OP_SEQ_NO", list.get(0).get("OP_SEQ_NO"));
                    jObject.put("OP_YN_AN", list.get(0).get("OP_YN_AN"));
                    jObject.put("OP_YN_AN_END", list.get(0).get("OP_YN_AN_END"));
                    jObject.put("OP_YN_CLAIM", list.get(0).get("OP_YN_CLAIM"));
                    jObject.put("OP_AN_DATE_BEG", DateUtil.to7TwDateTime(list.get(0).get("OP_AN_DATE_BEG").toString()));
                    jObject.put("OP_AN_DATE_END", DateUtil.to7TwDateTime(list.get(0).get("OP_AN_DATE_END").toString()));
                    jObject.put("OP_DOC_DT", DateUtil.to7TwDateTime(list.get(0).get("OP_DOC_DT").toString()));
                    jObject.put("OP_DOC_WD", list.get(0).get("OP_DOC_WD"));
                    jObject.put("OP_DOC_NO", list.get(0).get("OP_DOC_NO"));
                    jObject.put("OP_AN_D_UNIT_CD", list.get(0).get("OP_AN_D_UNIT_CD"));
                    jObject.put("OP_AN_D_UNIT_NM", list.get(0).get("OP_AN_D_UNIT_NM"));
                    jObject.put("OP_AN_B_UNIT_CD", list.get(0).get("OP_AN_B_UNIT_CD"));
                    jObject.put("OP_AN_B_UNIT_NM", list.get(0).get("OP_AN_B_UNIT_NM"));
                    jObject.put("OP_AN_UNIT_CD", list.get(0).get("OP_AN_UNIT_CD"));
                    jObject.put("OP_AN_UNIT_NM", list.get(0).get("OP_AN_UNIT_NM"));
                    jObject.put("OP_AN_CONTENT", list.get(0).get("OP_AN_CONTENT"));
                    jObject.put("OP_AN_REMARK", list.get(0).get("OP_AN_REMARK"));
                }
                sql = " SELECT *"
                        + " FROM OPDT_I_PU_BASIC"
                        + " WHERE OP_SEQ_NO = ? ";
                
                ArrayList<HashMap> Basiclist = this.pexecuteQuery(sql,args.toArray());
                if(!Basiclist.isEmpty()){
                    jObject.put("OP_AC_D_UNIT_CD", Basiclist.get(0).get("OP_AC_D_UNIT_CD"));
                    jObject.put("OP_AC_D_UNIT_NM", Basiclist.get(0).get("OP_AC_D_UNIT_NM"));
                    jObject.put("OP_AC_B_UNIT_CD", Basiclist.get(0).get("OP_AC_B_UNIT_CD"));
                    jObject.put("OP_AC_B_UNIT_NM", Basiclist.get(0).get("OP_AC_B_UNIT_NM"));
                    jObject.put("OP_AC_UNIT_CD", Basiclist.get(0).get("OP_AC_UNIT_CD"));
                    jObject.put("OP_AC_UNIT_NM", Basiclist.get(0).get("OP_AC_UNIT_NM"));
                    jObject.put("OP_AC_DATE",  DateUtil.to7TwDateTime(Basiclist.get(0).get("OP_AC_DATE").toString()));
                    jObject.put("OP_PU_DATE",  DateUtil.to7TwDateTime(Basiclist.get(0).get("OP_PU_DATE").toString()));
                    jObject.put("OP_PU_TIME",  DateUtil.to4TwTime(Basiclist.get(0).get("OP_PU_TIME").toString()));
                    jObject.put("OP_PU_CITY_CD", Basiclist.get(0).get("OP_PU_CITY_CD"));
                    jObject.put("OP_PU_TOWN_CD", Basiclist.get(0).get("OP_PU_TOWN_CD"));
                    jObject.put("OP_PU_PLACE", Basiclist.get(0).get("OP_PU_PLACE"));
                    jObject.put("OP_AC_UNIT_TEL", Basiclist.get(0).get("OP_AC_UNIT_TEL"));
                }

                resultDataArray.put(jObject);
                
		return resultDataArray;
	}
        
        // region 新增 model Start
        /**
         * 新增遺失物招領資料 .
         * 
         * @param jObj
         * @return boolean
         */
        public boolean add(JSONObject jObj) {
                GetSerialNoDao daoGetSerialNo = GetSerialNoDao.getInstance();
                OPDT_I_PU_BASIC iPuBasicDao = new OPDT_I_PU_BASIC();
                boolean bool = false;
                boolean returnValue = false;
                StringBuilder sql = new StringBuilder();
                Date current = new Date();
                int OP_MAX_PRMNO = 0;
                // 目前時間
                java.util.Date now = new java.util.Date();
                // 設定日期格式
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
                String YYYYMM = sdf.format(now);
                String year = YYYYMM.substring(0, 4);
                String mm = YYYYMM.substring(4, 6);
                String YYYMM = DateUtil.getYearE2C(year) + mm;
                String GetSeqNo = "";

                try {
                        User voUser = new User();
                        OPUtil opUtil = new OPUtil();
                        voUser = (User) jObj.get("userVO");
                        jObj.put("OP_BASIC_SEQ_NO", Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO")));
                        jObj.put("OP_AC_RCNO", jObj.get("OP_AC_RCNO"));
                        jObj.put("OP_YN_INPUT_PR", "1"); //1：輸入處理

                        if (!jObj.get("OP_PR_UNITLEVEL4").equals("")){
                                jObj.put("OP_PR_UNIT_CD", jObj.get("OP_PR_UNITLEVEL4"));
                        }else if (!jObj.get("OP_PR_UNITLEVEL3").equals("")){
                                jObj.put("OP_PR_UNIT_CD", jObj.get("OP_PR_UNITLEVEL3"));
                        }else if (!jObj.get("OP_PR_UNITLEVEL2").equals("")){
                                jObj.put("OP_PR_UNIT_CD", jObj.get("OP_PR_UNITLEVEL2"));
                        }
                        jObj.put("OP_PR_UNIT_NM", jObj.get("OP_PR_UNITLEVEL_NM"));
                        
                        jObj.put("OP_PR_STAFF_ID", voUser.getUserId());
                        jObj.put("OP_PR_STAFF_NM", voUser.getUserName());
                        
                        
                        if( jObj.get("OP_PR_DATE").equals("") ){
                            jObj.put("OP_PR_DATE", "");
                        }else{
                            jObj.put("OP_PR_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_PR_DATE").toString()));
                        }
                        
                        jObj.put("OP_PR_STAT_DESC", jObj.get("OP_PR_STAT_DESC"));
                        jObj.put("OP_YN_AUCTION", jObj.get("OP_YN_AUCTION"));
                        jObj.put("OP_NTC_GB_REC", "0"); // 0 or 空白or null：未發通知領回紀錄 - default
                        jObj.put("OP_GIVEUP_REC", "0"); // 0 or 空白 or null：未寄回
                        
                        if( jObj.get("OP_NTC_PUPO_DT").equals("") ){
                            jObj.put("OP_NTC_PUPO_DT", "");
                        }else{
                            jObj.put("OP_NTC_PUPO_DT", DateUtil.get8UsDateFormatDB(jObj.get("OP_NTC_PUPO_DT").toString()));
                        }
                                     
                        if( jObj.get("OP_PUPO_ANDTEND").equals("") ){
                            jObj.put("OP_PUPO_ANDTEND", "");
                        }else{
                            jObj.put("OP_PUPO_ANDTEND", DateUtil.get8UsDateFormatDB(jObj.get("OP_PUPO_ANDTEND").toString()));
                        }
                        jObj.put("OP_PUPO_YNANEND", "0"); //0 or 空白or null：未期滿
                        
                        if( jObj.get("OP_YN_GET_NO2").equals("1") ){ //自動取號
                            jObj.put("OP_PUPO_DOC_WD", jObj.get("OP_PUPO_DOC_WD"));
                            OP_MAX_PRMNO = daoGetSerialNo.getSerialNo(YYYYMM, voUser.getUnitCd2(), voUser.getUnitCd2Name(), "2");
                            GetSeqNo = YYYMM + String.format("%05d", OP_MAX_PRMNO);
                            jObj.put("OP_PUPO_DOC_NO", GetSeqNo);
                        }else{
                            jObj.put("OP_PUPO_DOC_WD", jObj.get("OP_PUPO_DOC_WD"));
                            jObj.put("OP_PUPO_DOC_NO", jObj.get("OP_PUPO_DOC_NO"));
                        }
                        
//                        if (!jObj.get("OP_PUPOANUNIT_UNITLEVEL4").equals("")){
//                                jObj.put("OP_PUPOANUNITCD", jObj.get("OP_PUPOANUNIT_UNITLEVEL4"));
//                        }else 
                        if (!jObj.get("OP_PUPOANUNIT_UNITLEVEL3").equals("")){
                                jObj.put("OP_PUPOANUNITCD", jObj.get("OP_PUPOANUNIT_UNITLEVEL3"));
                        }else if (!jObj.get("OP_PUPOANUNIT_UNITLEVEL2").equals("")){
                                jObj.put("OP_PUPOANUNITCD", jObj.get("OP_PUPOANUNIT_UNITLEVEL2"));
                        }
                        jObj.put("OP_PUPOANUNITNM", jObj.get("OP_PUPOANUNIT_UNITLEVEL_NM"));
//                        jObj.put("OP_PUPOANUNITCD", voUser.getUnitCd2());
//                        jObj.put("OP_PUPOANUNITNM", voUser.getUnitCd2Name());
                        
                        jObj.put("OP_PUPO_AN_CONT", jObj.get("OP_PUPO_AN_CONT"));
                         
                        jObj.put("OP_ADD_ID", voUser.getUserId());
                        jObj.put("OP_ADD_NM", voUser.getUserName());
                        jObj.put("OP_ADD_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_ADD_UNIT_NM", voUser.getUnitName());
                        jObj.put("OP_ADD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
                        jObj.put("OP_UPD_ID", voUser.getUserId());
                        jObj.put("OP_UPD_NM", voUser.getUserName());
                        jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_UPD_UNIT_NM",  voUser.getUnitName());
                        jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
   

                        DaoUtil util = new DaoUtil();
                        jObj = util.getStaticColumn(jObj, "ADD");
                        jObj = util.getStaticColumn(jObj, "UPD");

                        String arryString[] = {//"OP_SEQ_NO",
                                        "OP_BASIC_SEQ_NO",
                                        "OP_AC_RCNO",
                                        "OP_YN_INPUT_PR",
                                        "OP_PR_UNIT_CD", //5
                                        "OP_PR_UNIT_NM",
                                        "OP_PR_STAFF_ID",
                                        "OP_PR_STAFF_NM",
                                        "OP_PR_DATE",
                                        "OP_PR_STAT_DESC", //10
                                        "OP_YN_AUCTION",
                                        "OP_NTC_GB_REC",
                                        "OP_GIVEUP_REC",
                                        "OP_NTC_PUPO_DT",
                                        "OP_PUPO_ANDTEND", //15
                                        "OP_PUPO_YNANEND",
                                        "OP_PUPO_DOC_WD",
                                        "OP_PUPO_DOC_NO",
                                        "OP_PUPOANUNITCD",
                                        "OP_PUPOANUNITNM", //20
                                        "OP_PUPO_AN_CONT",
                                        "OP_ADD_ID",
                                        "OP_ADD_NM",
                                        "OP_ADD_UNIT_CD",
                                        "OP_ADD_UNIT_NM", //25
                                        "OP_ADD_DT_TM",
                                        "OP_UPD_ID",
                                        "OP_UPD_NM",
                                        "OP_UPD_UNIT_CD",
                                        "OP_UPD_UNIT_NM", //30
                                        "OP_UPD_DT_TM"}; //31

                        sql.append("INSERT INTO OPDT_I_AN_DL ( ");
                        sql.append(Arrays.toString(arryString).substring(1, Arrays.toString(arryString).length()-1));
                        sql.append(" )")
                        .append(" VALUES  (")
                        .append(" ?,?,?,?,?,?,?,?,?,?,") //10
                        .append(" ?,?,?,?,?,?,?,?,?,?,") //20
                        .append(" ?,?,?,?,?,?,?,?,?,?)"); //30

                        Object[] paraObject = new Object[arryString.length];

                        int i = 0;
                        for (String strKey:arryString){
                                paraObject[i] = new Object[i];
                                if (jObj.has(strKey))
                                        if ("OP_SEQ_NO".equals(strKey)){
                                                if (!"".equals(jObj.get(strKey).toString().trim()))
                                                        paraObject[i] = Integer.parseInt(jObj.get(strKey).toString());
                                                else {
                                                        paraObject[i] = 0;
                                                }
                                        }
                                        else{
                                                paraObject[i] = jObj.get(strKey);
                                        }
                                i++;
                        }

                        int  insetkey =getIdentityAfterInsert(sql.toString(),paraObject);
                        System.out.println(insetkey);
                        if ( insetkey > 0 ){
                            jObj.put("OP_SEQ_NO", insetkey);
                            jObj.put("OP_BASIC_SEQ_NO", jObj.get("OP_BASIC_SEQ_NO"));
                            jObj.put("OP_CURSTAT_CD", "4");
                            jObj.put("OP_CURSTAT_NM", "拾得人領回公告中");
                            iPuBasicDao.updateBasicForStatus(jObj);
                            
                            //拾得人領回寄信
                            MailSender mailSender = new MailSender();
                            JSONObject jObject = new JSONObject();
                            CachedRowSet crs1 =null;
                            boolean mailSend = false;
                            String OP_PUPO_NAME="",OP_AC_RCNO="",OP_PUPO_EMAIL="",OP_MAIL_CONTENT="";
                            int basicSeq = jObj.getInt("OP_BASIC_SEQ_NO");
                            JSONObject basicObj = ((JSONArray)iPuBasicDao.queryBasicById(basicSeq)).getJSONObject(0);
                            if ( basicObj != null ){
                                OP_PUPO_NAME = basicObj.getString("OP_PUPO_NAME");
                                OP_AC_RCNO = basicObj.getString("OP_AC_RCNO");
                                OP_PUPO_EMAIL = basicObj.getString("OP_PUPO_EMAIL");
                            }
                            OP_MAIL_CONTENT = jObj.get("OP_PUPO_AN_CONT").toString();
                            if( !OP_PUPO_NAME.equals("") && !OP_AC_RCNO.equals("") && !OP_PUPO_EMAIL.equals("") && !OP_MAIL_CONTENT.equals("") ){
                                    mailSend = mailSender.doAnDlNotice(OP_PUPO_NAME, 
                                                                       OP_AC_RCNO, 
                                                                       OP_MAIL_CONTENT, 
                                                                       OP_PUPO_EMAIL);
                                    
                                    if( mailSend ){
                                        updateForNtcGbRec(jObj); //更新拾得人通知領回紀錄
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
        // endregion 新增 model END
        
        // region 更新 Model Start
        /**
         * 更新遺失物招領資料.
         * 
         * @param jObj 審核資料欄位
         * @return boolean.
         */
        public boolean update(JSONObject jObj) {
                OPDT_I_PU_BASIC iPuBasicDao = new OPDT_I_PU_BASIC();
                boolean returnValue = false;
                StringBuilder sql = new StringBuilder();
                Date current = new Date();

                try {
                        User voUser = new User();
                        OPUtil opUtil = new OPUtil();
                        voUser = (User) jObj.get("userVO");
                        
                        jObj.put("OP_BASIC_SEQ_NO", Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO")));
                        jObj.put("OP_AC_RCNO", jObj.get("OP_AC_RCNO"));
                        jObj.put("OP_YN_INPUT_PR", "1"); //1：輸入處理

                        if (!jObj.get("OP_PR_UNITLEVEL4").equals("")){
                                jObj.put("OP_PR_UNIT_CD", jObj.get("OP_PR_UNITLEVEL4"));
                        }else if (!jObj.get("OP_PR_UNITLEVEL3").equals("")){
                                jObj.put("OP_PR_UNIT_CD", jObj.get("OP_PR_UNITLEVEL3"));
                        }else if (!jObj.get("OP_PR_UNITLEVEL2").equals("")){
                                jObj.put("OP_PR_UNIT_CD", jObj.get("OP_PR_UNITLEVEL2"));
                        }
                        jObj.put("OP_PR_UNIT_NM", jObj.get("OP_PR_UNITLEVEL_NM"));
                        
                        jObj.put("OP_PR_STAFF_ID", voUser.getUserId());
                        jObj.put("OP_PR_STAFF_NM", voUser.getUserName());
                        
                        
                        if( jObj.get("OP_PR_DATE").equals("") ){
                            jObj.put("OP_PR_DATE", "");
                        }else{
                            jObj.put("OP_PR_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_PR_DATE").toString()));
                        }
                        
                        jObj.put("OP_PR_STAT_DESC", jObj.get("OP_PR_STAT_DESC"));
                        jObj.put("OP_YN_AUCTION", jObj.get("OP_YN_AUCTION"));
                        jObj.put("OP_NTC_GB_REC", "0"); // 0 or 空白or null：未發通知領回紀錄 - default
                        jObj.put("OP_GIVEUP_REC", "0"); // 0 or 空白 or null：未寄回
                        
                        if( jObj.get("OP_NTC_PUPO_DT").equals("") ){
                            jObj.put("OP_NTC_PUPO_DT", "");
                        }else{
                            jObj.put("OP_NTC_PUPO_DT", DateUtil.get8UsDateFormatDB(jObj.get("OP_NTC_PUPO_DT").toString()));
                        }
                                     
                        if( jObj.get("OP_PUPO_ANDTEND").equals("") ){
                            jObj.put("OP_PUPO_ANDTEND", "");
                        }else{
                            jObj.put("OP_PUPO_ANDTEND", DateUtil.get8UsDateFormatDB(jObj.get("OP_PUPO_ANDTEND").toString()));
                        }
                        jObj.put("OP_PUPO_YNANEND", "0"); //0 or 空白or null：未期滿
                        
                        jObj.put("OP_PUPO_DOC_WD", jObj.get("OP_PUPO_DOC_WD"));
                        jObj.put("OP_PUPO_DOC_NO", jObj.get("OP_PUPO_DOC_NO"));
                        
//                        if (!jObj.get("OP_PUPOANUNIT_UNITLEVEL4").equals("")){
//                                jObj.put("OP_PUPOANUNITCD", jObj.get("OP_PUPOANUNIT_UNITLEVEL4"));
//                        }else 
                        if (!jObj.get("OP_PUPOANUNIT_UNITLEVEL3").equals("")){
                                jObj.put("OP_PUPOANUNITCD", jObj.get("OP_PUPOANUNIT_UNITLEVEL3"));
                        }else if (!jObj.get("OP_PUPOANUNIT_UNITLEVEL2").equals("")){
                                jObj.put("OP_PUPOANUNITCD", jObj.get("OP_PUPOANUNIT_UNITLEVEL2"));
                        }
                        jObj.put("OP_PUPOANUNITNM", jObj.get("OP_PUPOANUNIT_UNITLEVEL_NM"));
//                        jObj.put("OP_PUPOANUNITCD", voUser.getUnitCd2());
//                        jObj.put("OP_PUPOANUNITNM", voUser.getUnitCd2Name());
                        
                        jObj.put("OP_PUPO_AN_CONT", jObj.get("OP_PUPO_AN_CONT"));
                        
                        jObj.put("OP_UPD_ID", voUser.getUserId());
                        jObj.put("OP_UPD_NM", voUser.getUserName());
                        jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_UPD_UNIT_NM",  voUser.getUnitName());
                        jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
                        

                        DaoUtil util = new DaoUtil();
                        jObj = util.getStaticColumn(jObj, "UPD");

                        String arryString[] = {//"OP_SEQ_NO",
                                        "OP_BASIC_SEQ_NO",
                                        "OP_AC_RCNO",
                                        "OP_YN_INPUT_PR",
                                        "OP_PR_UNIT_CD", //5
                                        "OP_PR_UNIT_NM",
                                        "OP_PR_STAFF_ID",
                                        "OP_PR_STAFF_NM",
                                        "OP_PR_DATE",
                                        "OP_PR_STAT_DESC", //10
                                        "OP_YN_AUCTION",
                                        "OP_NTC_GB_REC",
                                        "OP_GIVEUP_REC",
                                        "OP_NTC_PUPO_DT",
                                        "OP_PUPO_ANDTEND", //15
                                        "OP_PUPO_YNANEND",
                                        "OP_PUPO_DOC_WD",
                                        "OP_PUPO_DOC_NO",
                                        "OP_PUPOANUNITCD",
                                        "OP_PUPOANUNITNM", //20
                                        "OP_PUPO_AN_CONT",
                                        "OP_UPD_ID",
                                        "OP_UPD_NM",
                                        "OP_UPD_UNIT_CD",
                                        "OP_UPD_UNIT_NM", //25
                                        "OP_UPD_DT_TM"}; //26

                        sql.append("UPDATE OPDT_I_AN_DL SET ");

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
                        
                        if ( returnValue ){
                            jObj.put("OP_BASIC_SEQ_NO", jObj.get("OP_BASIC_SEQ_NO"));
                            jObj.put("OP_CURSTAT_CD", "4");
                            jObj.put("OP_CURSTAT_NM", "拾得人領回公告中");
                            iPuBasicDao.updateBasicForStatus(jObj);
                            
                            //拾得人領回寄信
                            MailSender mailSender = new MailSender();
                            JSONObject jObject = new JSONObject();
                            CachedRowSet crs1 =null;
                            boolean mailSend = false;
                            String OP_PUPO_NAME="",OP_AC_RCNO="",OP_PUPO_EMAIL="",OP_MAIL_CONTENT="";
                            int basicSeq = jObj.getInt("OP_BASIC_SEQ_NO");
                            JSONObject basicObj = ((JSONArray)iPuBasicDao.queryBasicById(basicSeq)).getJSONObject(0);
                            if ( basicObj != null ){
                                OP_PUPO_NAME = basicObj.getString("OP_PUPO_NAME");
                                OP_AC_RCNO = basicObj.getString("OP_AC_RCNO");
                                OP_PUPO_EMAIL = basicObj.getString("OP_PUPO_EMAIL");
                            }
                            OP_MAIL_CONTENT = jObj.get("OP_PUPO_AN_CONT").toString();
                            if( !OP_PUPO_NAME.equals("") && !OP_AC_RCNO.equals("") && !OP_PUPO_EMAIL.equals("") && !OP_MAIL_CONTENT.equals("") ){
                                    mailSend = mailSender.doAnDlNotice(OP_PUPO_NAME, 
                                                                       OP_AC_RCNO, 
                                                                       OP_MAIL_CONTENT, 
                                                                       OP_PUPO_EMAIL);
                                    
                                    if( mailSend ){
                                        updateForNtcGbRec(jObj); //更新拾得人通知領回紀錄
                                    }
                            }
                                returnValue = true;
                        }else{
                                returnValue = false;
                        }
                        
                        jObj.put("OP_SEQ_NO", jObj.get("OP_SEQ_NO"));

                } catch (Exception e) {
                        log.error(ExceptionUtil.toString(e));
                }
                return returnValue;

        }
        
        // region 更新 Model Start
        /**
         * 更新遺失物招領狀態.
         * 
         * @param jObj 審核資料欄位
         * @return boolean.
         */
        public boolean updateForStatus(JSONObject jObj) {
                OPDT_I_PU_BASIC iPuBasicDao = new OPDT_I_PU_BASIC();
                boolean returnValue = false;
                StringBuilder sql = new StringBuilder();
                Date current = new Date();

                try {
                        User voUser = new User();
                        OPUtil opUtil = new OPUtil();
                        voUser = (User) jObj.get("userVO");
                        
                        jObj.put("OP_YN_AN", "2"); //2：已發網路公告
                        jObj.put("OP_UPD_ID", voUser.getUserId());
                        jObj.put("OP_UPD_NM", voUser.getUserName());
                        jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_UPD_UNIT_NM",  voUser.getUnitName());
                        jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
                        

                        DaoUtil util = new DaoUtil();
                        jObj = util.getStaticColumn(jObj, "UPD");

                        String arryString[] = {//"OP_SEQ_NO",
                                        "OP_YN_AN",
                                        "OP_UPD_ID", //15
                                        "OP_UPD_NM",
                                        "OP_UPD_UNIT_CD",
                                        "OP_UPD_UNIT_NM",
                                        "OP_UPD_DT_TM"}; //19

                        sql.append("UPDATE OPDT_I_ANNOUNCE SET ");

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
                        
                        if ( returnValue ){
                            jObj.put("OP_BASIC_SEQ_NO", Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO")));
                            jObj.put("OP_CURSTAT_CD", "2");
                            jObj.put("OP_CURSTAT_NM", "公告中");
                            iPuBasicDao.updateBasicForStatus(jObj);
                                returnValue = true;
                        }else{
                                returnValue = false;
                        }
                        
                        
                        jObj.put("OP_SEQ_NO", jObj.get("OP_SEQ_NO"));

                } catch (Exception e) {
                        log.error(ExceptionUtil.toString(e));
                }
                return returnValue;

        }
        
        // region 更新 Model Start
        /**
         * 更新拾得人通知領回紀錄.
         * 
         * @param jObj 審核資料欄位
         * @return boolean.
         */
        public boolean updateForNtcGbRec(JSONObject jObj) {
                boolean returnValue = false;
                StringBuilder sql = new StringBuilder();
                Date current = new Date();

                try {
                        User voUser = new User();
                        OPUtil opUtil = new OPUtil();
                        voUser = (User) jObj.get("userVO");
                        
                        jObj.put("OP_NTC_GB_REC", "1");
                        jObj.put("OP_UPD_ID", voUser.getUserId());
                        jObj.put("OP_UPD_NM", voUser.getUserName());
                        jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_UPD_UNIT_NM",  voUser.getUnitName());
                        jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
                        

                        DaoUtil util = new DaoUtil();
                        jObj = util.getStaticColumn(jObj, "UPD");

                        String arryString[] = {//"OP_SEQ_NO",
                                        "OP_NTC_GB_REC",
                                        "OP_UPD_ID", //15
                                        "OP_UPD_NM",
                                        "OP_UPD_UNIT_CD",
                                        "OP_UPD_UNIT_NM",
                                        "OP_UPD_DT_TM"}; //19

                        sql.append("UPDATE OPDT_I_AN_DL SET ");

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
                        paraObject[arryString.length] = jObj.getInt("OP_SEQ_NO");

                        returnValue = this.pexecuteUpdate(sql.toString(), paraObject) > 0 ? true : false;

                } catch (Exception e) {
                        log.error(ExceptionUtil.toString(e));
                }
                return returnValue;
        }
        	
}
