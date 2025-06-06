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
import npa.op.util.OPUtil;

public class OPDT_I_ANNOUNCE extends BaseDao {

    private static OPDT_I_ANNOUNCE instance = null;

    public static OPDT_I_ANNOUNCE getInstance() {
        if (instance == null) {
            instance = new OPDT_I_ANNOUNCE();
        }
        return instance;
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
    public JSONArray queryAnnounceByBasicId(int OP_BASIC_SEQ_NO, User user) throws SQLException, IOException, WriterException {
        ArrayList args = new ArrayList();
        JSONArray resultDataArray = new JSONArray();
        JSONObject jObject = new JSONObject();
        GetSerialNoDao daoGetSerialNo = GetSerialNoDao.getInstance();
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
        //發文字維護檔
        String sql = " SELECT *"
                + " FROM OPDT_DOC_WD_PARA"
                + " WHERE OP_B_UNIT_CD = ? ";
        args.clear();
        args.add(user.getUnitCd2());
        ArrayList<HashMap> docWdlist = this.pexecuteQuery(sql, args.toArray());

        sql = " SELECT *"
                + " FROM OPDT_I_ANNOUNCE"
                + " WHERE OP_BASIC_SEQ_NO = ? ";
        args.clear();
        args.add(OP_BASIC_SEQ_NO);

        ArrayList<HashMap> list = this.pexecuteQuery(sql, args.toArray());

        if (list.size() == 0) { //公告沒有資料
            jObject.put("ACTION_TYPE", "insertAnn1List");
            jObject.put("OP_SEQ_NO", "");

            sql = " SELECT *"
                    + " FROM OPDT_I_PU_DETAIL"
                    + " WHERE OP_BASIC_SEQ_NO = ? ";

            ArrayList<HashMap> detaillist = this.pexecuteQuery(sql, args.toArray());

            String puojNm = "";
            if (!detaillist.isEmpty()) {
                for (int i = 1; i <= detaillist.size(); i++) {
                  //20230202_拾得反應新增一案第一類新臺幣明細沒有變成若干元
                  //if (detaillist.get(i - 1).get("OP_TYPE_CD").equals("G001")) { //G001是其他  (在舊物品種類中，G001是新台幣)
                  //if (detaillist.get(i - 1).get("OP_TYPE_CD").equals("G001")  || detaillist.get(i - 1).get("OP_TYPE_CD").equals("A001") ) { //A001是新臺幣
                    if (detaillist.get(i - 1).get("OP_TYPE_CD").equals("A001") ) { //A001是新臺幣
                        puojNm = puojNm + detaillist.get(i - 1).get("OP_PUOJ_NM")
                              //+ "若干元";
                                + "若干" +  detaillist.get(i - 1).get("OP_QTY_UNIT");
                    } else {
                        String[] opQty = detaillist.get(i - 1).get("OP_QTY").toString().split("\\.");
                        if (opQty[1].equals("00")) {
                            puojNm = puojNm + detaillist.get(i - 1).get("OP_PUOJ_NM")
                                    + opQty[0]
                                    + detaillist.get(i - 1).get("OP_QTY_UNIT");
                            if (detaillist.get(i - 1).get("OP_PUOJ_NM").equals("手機")) {
                                if (!detaillist.get(i - 1).get("OP_PHONE_NUM").equals("")) {
                                    puojNm = puojNm + "(電信門號:" + detaillist.get(i - 1).get("OP_PHONE_NUM").toString().substring(0, 2) + "*****" + detaillist.get(i - 1).get("OP_PHONE_NUM").toString().substring(7, 10);
                                    if (!detaillist.get(i - 1).get("OP_PHONE_NUM_2").equals("")) {
                                        puojNm = puojNm + ", 電信門號2:" + detaillist.get(i - 1).get("OP_PHONE_NUM_2").toString().substring(0, 2) + "*****" + detaillist.get(i - 1).get("OP_PHONE_NUM_2").toString().substring(7, 10) + ")";
                                    } else {
                                        puojNm = puojNm + ")";
                                    }
                                }
                            }
                        } else {
                            puojNm = puojNm + detaillist.get(i - 1).get("OP_PUOJ_NM")
                                    + detaillist.get(i - 1).get("OP_QTY")
                                    + detaillist.get(i - 1).get("OP_QTY_UNIT");
                            if (detaillist.get(i - 1).get("OP_PUOJ_NM").equals("手機")) {
                                if (!detaillist.get(i - 1).get("OP_PHONE_NUM").equals("")) {
                                    puojNm = puojNm + "(電信門號:" + detaillist.get(i - 1).get("OP_PHONE_NUM").toString().substring(0, 2) + "*****" + detaillist.get(i - 1).get("OP_PHONE_NUM").toString().substring(7, 10);
                                    if (!detaillist.get(i - 1).get("OP_PHONE_NUM_2").equals("")) {
                                        puojNm = puojNm + ", 電信門號2:" + detaillist.get(i - 1).get("OP_PHONE_NUM_2").toString().substring(0, 2) + "*****" + detaillist.get(i - 1).get("OP_PHONE_NUM_2").toString().substring(7, 10) + ")";
                                    } else {
                                        puojNm = puojNm + ")";
                                    }
                                }
                            }
                        }
                    }
                    if (i <= detaillist.size() - 1) {
                        puojNm = puojNm + "、";
                    }
                }
            }
            jObject.put("OP_AN_CONTENT", "拾得人拾獲：" + puojNm + "，請失主於公告期間六個月內攜帶本人印章及身分證件前來認領。");

            if (!docWdlist.isEmpty()) {
                jObject.put("OP_DOC_WD", docWdlist.get(0).get("OP_DOC_WD1"));
                jObject.put("OP_YN_GET_NO1", docWdlist.get(0).get("OP_YN_GET_NO1"));
                if (docWdlist.get(0).get("OP_YN_GET_NO1").equals("1")) { //如果是自動取號
                    OP_MAX_PRMNO = daoGetSerialNo.querySerialNo(YYYYMM, user.getUnitCd2(), "1");
                    if (OP_MAX_PRMNO == 0) { // 代表序號目前沒有資料但是還是要帶出值來
                        GetSeqNo = YYYMM + "00001";
                        jObject.put("OP_DOC_NO", GetSeqNo);
                    } else {
                        GetSeqNo = YYYMM + String.format("%05d", OP_MAX_PRMNO + 1);
                        jObject.put("OP_DOC_NO", GetSeqNo);
                    }
                } else {
                    jObject.put("OP_DOC_NO", "");
                }
            } else {
                jObject.put("OP_YN_GET_NO1", "0");
                jObject.put("OP_DOC_WD", "");
                jObject.put("OP_DOC_NO", "");
            }

        } else {
            jObject.put("ACTION_TYPE", "updateAnn1List");
            jObject.put("OP_SEQ_NO", list.get(0).get("OP_SEQ_NO"));
            jObject.put("OP_YN_AN", list.get(0).get("OP_YN_AN"));
            jObject.put("OP_CABINET_NO", list.get(0).get("OP_CABINET_NO"));
            jObject.put("OP_YN_AN_END", list.get(0).get("OP_YN_AN_END"));
            jObject.put("OP_YN_CLAIM", list.get(0).get("OP_YN_CLAIM"));
            jObject.put("OP_AN_DATE_BEG", DateUtil.to7TwDateTime(list.get(0).get("OP_AN_DATE_BEG").toString()));
            jObject.put("OP_AN_DATE_END", DateUtil.to7TwDateTime(list.get(0).get("OP_AN_DATE_END").toString()));
            jObject.put("OP_DOC_DT", DateUtil.to7TwDateTime(list.get(0).get("OP_DOC_DT").toString()));
            if (!docWdlist.isEmpty()) {
                jObject.put("OP_YN_GET_NO1", docWdlist.get(0).get("OP_YN_GET_NO1"));
            } else {
                jObject.put("OP_YN_GET_NO1", "0");
            }
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
            jObject.put("OP_KP_UNIT_NM", list.get(0).get("OP_KP_UNIT_NM"));
            jObject.put("OP_KP_NM", list.get(0).get("OP_KP_NM"));
            jObject.put("OP_KP_DATE", DateUtil.to7TwDateTime(list.get(0).get("OP_KP_DATE").toString()));
            jObject.put("OP_KP_TIME", DateUtil.to4TwTime(list.get(0).get("OP_KP_TIME").toString()));
            jObject.put("OP_KP_REMARK", list.get(0).get("OP_KP_REMARK"));
            jObject.put("OP_KP_UNIT_NM1", list.get(0).get("OP_KP_UNIT_NM1"));
            jObject.put("OP_KP_NM1", list.get(0).get("OP_KP_NM1"));
            jObject.put("OP_KP_DATE1", DateUtil.to7TwDateTime(list.get(0).get("OP_KP_DATE1").toString()));
            jObject.put("OP_KP_TIME1", DateUtil.to4TwTime(list.get(0).get("OP_KP_TIME1").toString()));
            jObject.put("OP_KP_REMARK1", list.get(0).get("OP_KP_REMARK1"));
            jObject.put("OP_KP_UNIT_NM2", list.get(0).get("OP_KP_UNIT_NM2"));
            jObject.put("OP_KP_NM2", list.get(0).get("OP_KP_NM2"));
            jObject.put("OP_KP_DATE2", DateUtil.to7TwDateTime(list.get(0).get("OP_KP_DATE2").toString()));
            jObject.put("OP_KP_TIME2", DateUtil.to4TwTime(list.get(0).get("OP_KP_TIME2").toString()));
            jObject.put("OP_KP_REMARK2", list.get(0).get("OP_KP_REMARK2"));
        }
        sql = " SELECT *"
                + " FROM OPDT_I_PU_BASIC"
                + " WHERE OP_SEQ_NO = ? ";

        args.clear();
        args.add(OP_BASIC_SEQ_NO);

        ArrayList<HashMap> Basiclist = this.pexecuteQuery(sql, args.toArray());

        if (!Basiclist.isEmpty()) {
            jObject.put("OP_AC_D_UNIT_CD", Basiclist.get(0).get("OP_AC_D_UNIT_CD"));
            jObject.put("OP_AC_D_UNIT_NM", Basiclist.get(0).get("OP_AC_D_UNIT_NM"));
            jObject.put("OP_AC_B_UNIT_CD", Basiclist.get(0).get("OP_AC_B_UNIT_CD"));
            jObject.put("OP_AC_B_UNIT_NM", Basiclist.get(0).get("OP_AC_B_UNIT_NM"));
            jObject.put("OP_AC_UNIT_CD", Basiclist.get(0).get("OP_AC_UNIT_CD"));
            jObject.put("OP_AC_UNIT_NM", Basiclist.get(0).get("OP_AC_UNIT_NM"));
            jObject.put("OP_AC_DATE", DateUtil.to7TwDateTime(Basiclist.get(0).get("OP_AC_DATE").toString()));
        }

        resultDataArray.put(jObject);

        return resultDataArray;
    }

    /**
     * 查詢認領資料明細 .(DB原始資料)
     *
     * @param OP_BASIC_SEQ_NO
     * @return JSONArray
     * @throws SQLException
     * @throws WriterException
     * @throws IOException
     */
    public JSONArray queryAnnounceByBasicIdOriginal(int OP_BASIC_SEQ_NO) throws SQLException, IOException, WriterException {
        ArrayList args = new ArrayList();

        String sql = " SELECT *"
                + " FROM OPDT_I_ANNOUNCE"
                + " WHERE OP_BASIC_SEQ_NO = ? ";
        args.clear();
        args.add(OP_BASIC_SEQ_NO);

        ArrayList<HashMap> list = this.pexecuteQuery(sql, args.toArray());

        return arrayList2JsonArray(list);
    }

    /**
     * 顯示認領資料明細 .(內部公告)
     *
     * @param OP_BASIC_SEQ_NO
     * @return JSONArray
     * @throws SQLException
     * @throws WriterException
     * @throws IOException
     */
    public JSONArray showAnnounceByBasicId(int OP_BASIC_SEQ_NO) throws SQLException, IOException, WriterException {
        ArrayList args = new ArrayList();
        JSONArray resultDataArray = new JSONArray();
        JSONObject jObject = new JSONObject();
        CachedRowSet crs1, crs2;
        String sql = " SELECT *"
                + " FROM OPDT_I_ANNOUNCE"
                + " WHERE OP_BASIC_SEQ_NO = ? ";
        args.add(OP_BASIC_SEQ_NO);

        ArrayList<HashMap> list = this.pexecuteQuery(sql, args.toArray());

        if (list.size() == 0) { //公告沒有資料
            jObject.put("OP_SEQ_NO", "");
            jObject.put("OP_YN_AN", "N");
            jObject.put("OP_YN_AN_END", "");
            jObject.put("OP_YN_CLAIM", "");
            jObject.put("OP_AN_DATE_BEG", "");
            jObject.put("OP_AN_DATE_END", "");
            jObject.put("OP_DOC_DT", "");
            jObject.put("OP_CABINET_NO", "");
            jObject.put("OP_DOC_WD", "");
            jObject.put("OP_DOC_NO", "");
            jObject.put("OP_AN_D_UNIT_CD", "");
            jObject.put("OP_AN_D_UNIT_NM", "");
            jObject.put("OP_AN_B_UNIT_CD", "");
            jObject.put("OP_AN_B_UNIT_NM", "");
            jObject.put("OP_AN_UNIT_CD", "");
            jObject.put("OP_AN_UNIT_NM", "");
            jObject.put("OP_AN_CONTENT", "");
            jObject.put("OP_AN_REMARK", "");

            jObject.put("OP_AC_D_UNIT_CD", "");
            jObject.put("OP_AC_D_UNIT_NM", "");
            jObject.put("OP_AC_B_UNIT_CD", "");
            jObject.put("OP_AC_B_UNIT_NM", "");
            jObject.put("OP_AC_UNIT_CD", "");
            jObject.put("OP_AC_UNIT_NM", "");
            jObject.put("OP_AC_DATE", "");

            jObject.put("OP_KP_UNIT_NM", "");
            jObject.put("OP_KP_NM", "");
            jObject.put("OP_KP_DATE", "");
            jObject.put("OP_KP_TIME", "");
            jObject.put("OP_KP_REMARK", "");

            jObject.put("OP_KP_UNIT_NM1", "");
            jObject.put("OP_KP_NM1", "");
            jObject.put("OP_KP_DATE1", "");
            jObject.put("OP_KP_TIME1", "");
            jObject.put("OP_KP_REMARK1", "");

            jObject.put("OP_KP_UNIT_NM2", "");
            jObject.put("OP_KP_NM2", "");
            jObject.put("OP_KP_DATE2", "");
            jObject.put("OP_KP_TIME2", "");
            jObject.put("OP_KP_REMARK2", "");

        } else {
            jObject.put("OP_SEQ_NO", list.get(0).get("OP_SEQ_NO"));
            jObject.put("OP_YN_AN", list.get(0).get("OP_YN_AN"));
            jObject.put("OP_YN_AN_END", list.get(0).get("OP_YN_AN_END"));
            jObject.put("OP_YN_CLAIM", list.get(0).get("OP_YN_CLAIM"));
            jObject.put("OP_AN_DATE_BEG", DateUtil.to7TwDateTime(list.get(0).get("OP_AN_DATE_BEG").toString()));
            jObject.put("OP_AN_DATE_END", DateUtil.to7TwDateTime(list.get(0).get("OP_AN_DATE_END").toString()));
            jObject.put("OP_DOC_DT", DateUtil.to7TwDateTime(list.get(0).get("OP_DOC_DT").toString()));
            jObject.put("OP_CABINET_NO", list.get(0).get("OP_CABINET_NO"));
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
            jObject.put("OP_KP_UNIT_NM", list.get(0).get("OP_KP_UNIT_NM"));
            jObject.put("OP_KP_NM", list.get(0).get("OP_KP_NM"));
            jObject.put("OP_KP_DATE", DateUtil.to7TwDateTime(list.get(0).get("OP_KP_DATE").toString()));
            jObject.put("OP_KP_TIME", DateUtil.to4TwTime(list.get(0).get("OP_KP_TIME").toString()));
            jObject.put("OP_KP_REMARK", list.get(0).get("OP_KP_REMARK"));
            jObject.put("OP_KP_UNIT_NM1", list.get(0).get("OP_KP_UNIT_NM1"));
            jObject.put("OP_KP_NM1", list.get(0).get("OP_KP_NM1"));
            jObject.put("OP_KP_DATE1", DateUtil.to7TwDateTime(list.get(0).get("OP_KP_DATE1").toString()));
            jObject.put("OP_KP_TIME1", DateUtil.to4TwTime(list.get(0).get("OP_KP_TIME1").toString()));
            jObject.put("OP_KP_REMARK1", list.get(0).get("OP_KP_REMARK1"));
            jObject.put("OP_KP_UNIT_NM2", list.get(0).get("OP_KP_UNIT_NM2"));
            jObject.put("OP_KP_NM2", list.get(0).get("OP_KP_NM2"));
            jObject.put("OP_KP_DATE2", DateUtil.to7TwDateTime(list.get(0).get("OP_KP_DATE2").toString()));
            jObject.put("OP_KP_TIME2", DateUtil.to4TwTime(list.get(0).get("OP_KP_TIME2").toString()));
            jObject.put("OP_KP_REMARK2", list.get(0).get("OP_KP_REMARK2"));
            sql = " SELECT *"
                    + " FROM OPDT_I_PU_BASIC"
                    + " WHERE OP_SEQ_NO = ? ";

            ArrayList<HashMap> Basiclist = this.pexecuteQuery(sql, args.toArray());
            if (!Basiclist.isEmpty()) {
                jObject.put("OP_AC_D_UNIT_CD", Basiclist.get(0).get("OP_AC_D_UNIT_CD"));
                jObject.put("OP_AC_D_UNIT_NM", Basiclist.get(0).get("OP_AC_D_UNIT_NM"));
                jObject.put("OP_AC_B_UNIT_CD", Basiclist.get(0).get("OP_AC_B_UNIT_CD"));
                jObject.put("OP_AC_B_UNIT_NM", Basiclist.get(0).get("OP_AC_B_UNIT_NM"));
                jObject.put("OP_AC_UNIT_CD", Basiclist.get(0).get("OP_AC_UNIT_CD"));
                jObject.put("OP_AC_UNIT_NM", Basiclist.get(0).get("OP_AC_UNIT_NM"));
                jObject.put("OP_AC_DATE", DateUtil.to7TwDateTime(Basiclist.get(0).get("OP_AC_DATE").toString()));
            }
        }
        System.out.println(jObject);

        resultDataArray.put(jObject);

        return resultDataArray;
    }

    /**
     * 查詢公告資料明細 .
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
        CachedRowSet crs1, crs2;
        String sql = " SELECT *"
                + " FROM OPDT_I_ANNOUNCE"
                + " WHERE OP_BASIC_SEQ_NO = ? ";
        args.add(OP_BASIC_SEQ_NO);

        ArrayList<HashMap> list = this.pexecuteQuery(sql, args.toArray());

        if (list.size() == 0) { //公告沒有資料

        } else {
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

        ArrayList<HashMap> Basiclist = this.pexecuteQuery(sql, args.toArray());
        if (!Basiclist.isEmpty()) {
            jObject.put("OP_AC_D_UNIT_CD", Basiclist.get(0).get("OP_AC_D_UNIT_CD"));
            jObject.put("OP_AC_D_UNIT_NM", Basiclist.get(0).get("OP_AC_D_UNIT_NM"));
            jObject.put("OP_AC_B_UNIT_CD", Basiclist.get(0).get("OP_AC_B_UNIT_CD"));
            jObject.put("OP_AC_B_UNIT_NM", Basiclist.get(0).get("OP_AC_B_UNIT_NM"));
            jObject.put("OP_AC_UNIT_CD", Basiclist.get(0).get("OP_AC_UNIT_CD"));
            jObject.put("OP_AC_UNIT_NM", Basiclist.get(0).get("OP_AC_UNIT_NM"));
            jObject.put("OP_AC_DATE", DateUtil.to7TwDateTime(Basiclist.get(0).get("OP_AC_DATE").toString()));
            jObject.put("OP_PU_DATE", DateUtil.to7TwDateTime(Basiclist.get(0).get("OP_PU_DATE").toString()));
            jObject.put("OP_PU_TIME", DateUtil.to4TwTime(Basiclist.get(0).get("OP_PU_TIME").toString()));
            jObject.put("OP_PU_CITY_CD", Basiclist.get(0).get("OP_PU_CITY_CD"));
            jObject.put("OP_PU_TOWN_CD", Basiclist.get(0).get("OP_PU_TOWN_CD"));
            jObject.put("OP_PU_PLACE", Basiclist.get(0).get("OP_PU_PLACE"));
            jObject.put("OP_AC_UNIT_TEL", Basiclist.get(0).get("OP_AC_UNIT_TEL"));
        }

        resultDataArray.put(jObject);

        return resultDataArray;
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
        if (!jObj.get("OP_BASIC_SEQ_NO").equals("")) {
            sql += " AND OP_BASIC_SEQ_NO = ?";
            args.add(Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO")));
        }

        CachedRowSet crs = this.pexecuteQueryRowSet(sql, args.toArray());

        return crs;
    }

    /**
     * 顯示認領資料明細 .(網路公告)
     *
     * @param OP_BASIC_SEQ_NO
     * @return JSONArray
     * @throws SQLException
     * @throws WriterException
     * @throws IOException
     */
    public JSONArray showAnnounce2ByBasicId(int OP_BASIC_SEQ_NO) throws SQLException, IOException, WriterException {
        ArrayList args = new ArrayList();
        JSONArray resultDataArray = new JSONArray();
        JSONObject jObject = new JSONObject();
        CachedRowSet crs1, crs2;
        String sql = " SELECT *"
                + " FROM OPDT_I_ANNOUNCE"
                + " WHERE OP_BASIC_SEQ_NO = ? ";
        args.add(OP_BASIC_SEQ_NO);

        ArrayList<HashMap> list = this.pexecuteQuery(sql, args.toArray());

        if (list.size() == 0) { //公告沒有資料
            jObject.put("OP_SEQ_NO", "");
            jObject.put("OP_YN_AN", "N");
            jObject.put("OP_YN_AN_END", "");
            jObject.put("OP_YN_CLAIM", "");
            jObject.put("OP_AN_DATE_BEG", "");
            jObject.put("OP_AN_DATE_END", "");
            jObject.put("OP_DOC_DT", "");
            jObject.put("OP_DOC_WD", "");
            jObject.put("OP_DOC_NO", "");
            jObject.put("OP_AN_D_UNIT_CD", "");
            jObject.put("OP_AN_D_UNIT_NM", "");
            jObject.put("OP_AN_B_UNIT_CD", "");
            jObject.put("OP_AN_B_UNIT_NM", "");
            jObject.put("OP_AN_UNIT_CD", "");
            jObject.put("OP_AN_UNIT_NM", "");
            jObject.put("OP_AN_CONTENT", "");
            jObject.put("OP_AN_REMARK", "");
            jObject.put("OP_AC_D_UNIT_CD", "");
            jObject.put("OP_AC_D_UNIT_NM", "");
            jObject.put("OP_AC_B_UNIT_CD", "");
            jObject.put("OP_AC_B_UNIT_NM", "");
            jObject.put("OP_AC_UNIT_CD", "");
            jObject.put("OP_AC_UNIT_NM", "");
            jObject.put("OP_AC_DATE", "");
            jObject.put("OP_PU_DATE", "");
            jObject.put("OP_PU_TIME", "");
            jObject.put("OP_PU_CITY_CD", "");
            jObject.put("OP_PU_TOWN_CD", "");
            jObject.put("OP_PU_PLACE", "");
            jObject.put("OP_AC_UNIT_TEL", "");
        } else {
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

            sql = " SELECT *"
                    + " FROM OPDT_I_PU_BASIC"
                    + " WHERE OP_SEQ_NO = ? ";

            ArrayList<HashMap> Basiclist = this.pexecuteQuery(sql, args.toArray());
            if (!Basiclist.isEmpty()) {
                jObject.put("OP_AC_D_UNIT_CD", Basiclist.get(0).get("OP_AC_D_UNIT_CD"));
                jObject.put("OP_AC_D_UNIT_NM", Basiclist.get(0).get("OP_AC_D_UNIT_NM"));
                jObject.put("OP_AC_B_UNIT_CD", Basiclist.get(0).get("OP_AC_B_UNIT_CD"));
                jObject.put("OP_AC_B_UNIT_NM", Basiclist.get(0).get("OP_AC_B_UNIT_NM"));
                jObject.put("OP_AC_UNIT_CD", Basiclist.get(0).get("OP_AC_UNIT_CD"));
                jObject.put("OP_AC_UNIT_NM", Basiclist.get(0).get("OP_AC_UNIT_NM"));
                jObject.put("OP_AC_DATE", DateUtil.to7TwDateTime(Basiclist.get(0).get("OP_AC_DATE").toString()));
                jObject.put("OP_PU_DATE", DateUtil.to7TwDateTime(Basiclist.get(0).get("OP_PU_DATE").toString()));
                jObject.put("OP_PU_TIME", DateUtil.to4TwTime(Basiclist.get(0).get("OP_PU_TIME").toString()));
                jObject.put("OP_PU_CITY_CD", Basiclist.get(0).get("OP_PU_CITY_CD"));
                jObject.put("OP_PU_TOWN_CD", Basiclist.get(0).get("OP_PU_TOWN_CD"));
                jObject.put("OP_PU_PLACE", Basiclist.get(0).get("OP_PU_PLACE"));
                jObject.put("OP_AC_UNIT_TEL", Basiclist.get(0).get("OP_AC_UNIT_TEL"));
            }
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
            jObj.put("OP_CABINET_NO", jObj.get("OP_CABINET_NO"));
            jObj.put("OP_YN_AN", "1"); //1：已發內部公告
            jObj.put("OP_YN_AN_END", "0"); //0 or 空白or null：未期滿
            jObj.put("OP_YN_CLAIM", "0"); //0 or 空白or null：未被認領

            if (jObj.get("OP_AN_DATE_BEG").equals("")) {
                jObj.put("OP_AN_DATE_BEG", "");
            } else {
                jObj.put("OP_AN_DATE_BEG", DateUtil.get8UsDateFormatDB(jObj.get("OP_AN_DATE_BEG").toString()));
            }

            if (jObj.get("OP_AN_DATE_END").equals("")) {
                jObj.put("OP_AN_DATE_END", "");
            } else {
                jObj.put("OP_AN_DATE_END", DateUtil.get8UsDateFormatDB(jObj.get("OP_AN_DATE_END").toString()));
            }

            if (jObj.get("OP_DOC_DT").equals("")) {
                jObj.put("OP_DOC_DT", "");
            } else {
                jObj.put("OP_DOC_DT", DateUtil.get8UsDateFormatDB(jObj.get("OP_DOC_DT").toString()));
            }

            if (jObj.get("OP_YN_GET_NO1").equals("1")) { //自動取號
                jObj.put("OP_DOC_WD", jObj.get("OP_DOC_WD"));
                OP_MAX_PRMNO = daoGetSerialNo.getSerialNo(YYYYMM, voUser.getUnitCd2(), voUser.getUnitCd2Name(), "1");
                GetSeqNo = YYYMM + String.format("%05d", OP_MAX_PRMNO);
                jObj.put("OP_DOC_NO", GetSeqNo);
            } else {
                jObj.put("OP_DOC_WD", jObj.get("OP_DOC_WD"));
                jObj.put("OP_DOC_NO", jObj.get("OP_DOC_NO"));
            }

            jObj.put("OP_AN_D_UNIT_CD", jObj.get("OP_AN_D_UNIT_CD"));
            jObj.put("OP_AN_D_UNIT_NM", jObj.get("OP_AN_D_UNIT_NM"));
            jObj.put("OP_AN_B_UNIT_CD", jObj.get("OP_AN_B_UNIT_CD"));
            jObj.put("OP_AN_B_UNIT_NM", jObj.get("OP_AN_B_UNIT_NM"));
//                        jObj.put("OP_AN_UNIT_CD", jObj.get("OP_AN_UNIT_CD"));
//                        jObj.put("OP_AN_UNIT_NM", jObj.get("OP_AN_UNIT_NM"));
            jObj.put("OP_AN_UNIT_CD", voUser.getUnitCd());
            jObj.put("OP_AN_UNIT_NM", voUser.getUnitName());

            jObj.put("OP_AN_CONTENT", jObj.get("OP_AN_CONTENT"));
            jObj.put("OP_AN_REMARK", jObj.get("OP_AN_REMARK"));

            jObj.put("OP_ADD_ID", voUser.getUserId());
            jObj.put("OP_ADD_NM", voUser.getUserName());
            jObj.put("OP_ADD_UNIT_CD", voUser.getUnitCd());
            jObj.put("OP_ADD_UNIT_NM", voUser.getUnitName());
            jObj.put("OP_ADD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
            jObj.put("OP_UPD_ID", voUser.getUserId());
            jObj.put("OP_UPD_NM", voUser.getUserName());
            jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd());
            jObj.put("OP_UPD_UNIT_NM", voUser.getUnitName());
            jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
            jObj.put("OP_KP_UNIT_NM", jObj.get("OP_KP_UNIT_NM"));
            jObj.put("OP_KP_NM", jObj.get("OP_KP_NM"));
            jObj.put("OP_KP_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_KP_DATE").toString()));
            jObj.put("OP_KP_TIME", jObj.get("OP_KP_TIME").toString().replace(":", ""));
            jObj.put("OP_KP_REMARK", jObj.get("OP_KP_REMARK"));

            jObj.put("OP_KP_UNIT_NM1", jObj.get("OP_KP_UNIT_NM1"));
            jObj.put("OP_KP_NM1", jObj.get("OP_KP_NM1"));
            if (jObj.getString("OP_KP_DATE1").equals("")) {
                jObj.put("OP_KP_DATE1", "");
            } else {
                jObj.put("OP_KP_DATE1", DateUtil.get8UsDateFormatDB(jObj.get("OP_KP_DATE1").toString()));
            }
            jObj.put("OP_KP_TIME1", jObj.get("OP_KP_TIME1").toString().replace(":", ""));
            jObj.put("OP_KP_REMARK1", jObj.get("OP_KP_REMARK1"));

            jObj.put("OP_KP_UNIT_NM2", jObj.get("OP_KP_UNIT_NM2"));
            jObj.put("OP_KP_NM2", jObj.get("OP_KP_NM2"));
            if (jObj.getString("OP_KP_DATE1").equals("")) {
                jObj.put("OP_KP_DATE2", "");
            } else {
                jObj.put("OP_KP_DATE2", DateUtil.get8UsDateFormatDB(jObj.get("OP_KP_DATE2").toString()));
            }
            jObj.put("OP_KP_TIME2", jObj.get("OP_KP_TIME2").toString().replace(":", ""));
            jObj.put("OP_KP_REMARK2", jObj.get("OP_KP_REMARK2"));

            DaoUtil util = new DaoUtil();
            jObj = util.getStaticColumn(jObj, "ADD");
            jObj = util.getStaticColumn(jObj, "UPD");

            String arryString[] = {//"OP_SEQ_NO",
                "OP_BASIC_SEQ_NO",
                "OP_AC_RCNO",
                "OP_CABINET_NO",
                "OP_YN_AN",
                "OP_YN_AN_END", //5
                "OP_YN_CLAIM",
                "OP_AN_DATE_BEG",
                "OP_AN_DATE_END",
                "OP_DOC_DT",
                "OP_DOC_WD", //10
                "OP_DOC_NO",
                "OP_AN_D_UNIT_CD",
                "OP_AN_D_UNIT_NM",
                "OP_AN_B_UNIT_CD",
                "OP_AN_B_UNIT_NM", //15
                "OP_AN_UNIT_CD",
                "OP_AN_UNIT_NM",
                "OP_AN_CONTENT",
                "OP_AN_REMARK",
                "OP_ADD_ID", //20
                "OP_ADD_NM",
                "OP_ADD_UNIT_CD",
                "OP_ADD_UNIT_NM",
                "OP_ADD_DT_TM",
                "OP_UPD_ID", //25
                "OP_UPD_NM",
                "OP_UPD_UNIT_CD",
                "OP_UPD_UNIT_NM",
                "OP_UPD_DT_TM",
                "OP_KP_UNIT_NM",//30
                "OP_KP_NM",
                "OP_KP_DATE",
                "OP_KP_TIME",
                "OP_KP_REMARK",
                "OP_KP_UNIT_NM1",//35
                "OP_KP_NM1",
                "OP_KP_DATE1",
                "OP_KP_TIME1",
                "OP_KP_REMARK1",
                "OP_KP_UNIT_NM2",//40
                "OP_KP_NM2",
                "OP_KP_DATE2",
                "OP_KP_TIME2",
                "OP_KP_REMARK2",//44
            };

            sql.append("INSERT INTO OPDT_I_ANNOUNCE ( ");
            sql.append(Arrays.toString(arryString).substring(1, Arrays.toString(arryString).length() - 1));
            sql.append(" )")
                    .append(" VALUES  (")
                    .append(" ?,?,?,?,?,?,?,?,?,?,") //10
                    .append(" ?,?,?,?,?,?,?,?,?,?,") //20
                    .append(" ?,?,?,?,?,?,?,?,?,?,") //30
                    .append(" ?,?,?,?,?,?,?,?,?,?,") //40
                    .append(" ?,?,?,?)"); //44

            Object[] paraObject = new Object[arryString.length];

            int i = 0;
            for (String strKey : arryString) {
                paraObject[i] = new Object[i];
                if (jObj.has(strKey)) {
                    if ("OP_SEQ_NO".equals(strKey)) {
                        if (!"".equals(jObj.get(strKey).toString().trim())) {
                            paraObject[i] = Integer.parseInt(jObj.get(strKey).toString());
                        } else {
                            paraObject[i] = 0;
                        }
                    } else {
                        paraObject[i] = jObj.get(strKey);
                    }
                }
                i++;
            }

            int insetkey = getIdentityAfterInsert(sql.toString(), paraObject);
            System.out.println(insetkey);
            if (insetkey > 0) {
                jObj.put("OP_BASIC_SEQ_NO", jObj.get("OP_BASIC_SEQ_NO"));
                jObj.put("OP_CURSTAT_CD", "2");
                jObj.put("OP_CURSTAT_NM", "公告中");
                iPuBasicDao.updateBasicForStatus(jObj);
                returnValue = true;
            } else {
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
            jObj.put("OP_CABINET_NO", jObj.get("OP_CABINET_NO"));
            jObj.put("OP_YN_AN", "1"); //1：已發內部公告
            jObj.put("OP_YN_AN_END", "0"); //0 or 空白or null：未期滿
            jObj.put("OP_YN_CLAIM", "0"); //0 or 空白or null：未被認領

            if (jObj.get("OP_AN_DATE_BEG").equals("")) {
                jObj.put("OP_AN_DATE_BEG", "");
            } else {
                jObj.put("OP_AN_DATE_BEG", DateUtil.get8UsDateFormatDB(jObj.get("OP_AN_DATE_BEG").toString()));
            }

            if (jObj.get("OP_AN_DATE_END").equals("")) {
                jObj.put("OP_AN_DATE_END", "");
            } else {
                jObj.put("OP_AN_DATE_END", DateUtil.get8UsDateFormatDB(jObj.get("OP_AN_DATE_END").toString()));
            }

            if (jObj.get("OP_DOC_DT").equals("")) {
                jObj.put("OP_DOC_DT", "");
            } else {
                jObj.put("OP_DOC_DT", DateUtil.get8UsDateFormatDB(jObj.get("OP_DOC_DT").toString()));
            }
            jObj.put("OP_DOC_WD", jObj.get("OP_DOC_WD"));
            jObj.put("OP_DOC_NO", jObj.get("OP_DOC_NO"));

            jObj.put("OP_AN_D_UNIT_CD", jObj.get("OP_AN_D_UNIT_CD"));
            jObj.put("OP_AN_D_UNIT_NM", jObj.get("OP_AN_D_UNIT_NM"));
            jObj.put("OP_AN_B_UNIT_CD", jObj.get("OP_AN_B_UNIT_CD"));
            jObj.put("OP_AN_B_UNIT_NM", jObj.get("OP_AN_B_UNIT_NM"));
//                        jObj.put("OP_AN_UNIT_CD", jObj.get("OP_AN_UNIT_CD"));
//                        jObj.put("OP_AN_UNIT_NM", jObj.get("OP_AN_UNIT_NM"));
            jObj.put("OP_AN_UNIT_CD", voUser.getUnitCd());
            jObj.put("OP_AN_UNIT_NM", voUser.getUnitName());

            jObj.put("OP_AN_CONTENT", jObj.get("OP_AN_CONTENT"));
            jObj.put("OP_AN_REMARK", jObj.get("OP_AN_REMARK"));

            jObj.put("OP_UPD_ID", voUser.getUserId());
            jObj.put("OP_UPD_NM", voUser.getUserName());
            jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd());
            jObj.put("OP_UPD_UNIT_NM", voUser.getUnitName());
            jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
            jObj.put("OP_KP_UNIT_NM", jObj.get("OP_KP_UNIT_NM"));
            jObj.put("OP_KP_NM", jObj.get("OP_KP_NM"));
            jObj.put("OP_KP_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_KP_DATE").toString()));
            jObj.put("OP_KP_TIME", jObj.get("OP_KP_TIME").toString().replace(":", ""));
            jObj.put("OP_KP_REMARK", jObj.get("OP_KP_REMARK"));

            jObj.put("OP_KP_UNIT_NM1", jObj.get("OP_KP_UNIT_NM1"));
            jObj.put("OP_KP_NM1", jObj.get("OP_KP_NM1"));
            if (jObj.get("OP_KP_DATE1").equals("")) {
                jObj.put("OP_KP_DATE1", "");
            } else {
                jObj.put("OP_KP_DATE1", DateUtil.get8UsDateFormatDB(jObj.get("OP_KP_DATE1").toString()));
            }
            jObj.put("OP_KP_TIME1", jObj.get("OP_KP_TIME1").toString().replace(":", ""));
            jObj.put("OP_KP_REMARK1", jObj.get("OP_KP_REMARK1"));

            jObj.put("OP_KP_UNIT_NM2", jObj.get("OP_KP_UNIT_NM2"));
            jObj.put("OP_KP_NM2", jObj.get("OP_KP_NM2"));
            if (jObj.get("OP_KP_DATE2").equals("")) {
                jObj.put("OP_KP_DATE2", "");
            } else {
                jObj.put("OP_KP_DATE2", DateUtil.get8UsDateFormatDB(jObj.get("OP_KP_DATE2").toString()));
            }
            jObj.put("OP_KP_TIME2", jObj.get("OP_KP_TIME2").toString().replace(":", ""));
            jObj.put("OP_KP_REMARK2", jObj.get("OP_KP_REMARK2"));

            DaoUtil util = new DaoUtil();
            jObj = util.getStaticColumn(jObj, "UPD");

            String arryString[] = {//"OP_SEQ_NO",
                "OP_CABINET_NO",
                "OP_YN_AN",
                "OP_YN_AN_END",
                "OP_YN_CLAIM",
                "OP_AN_DATE_BEG",
                "OP_AN_DATE_END",
                "OP_DOC_DT",
                "OP_DOC_WD", //5
                "OP_DOC_NO",
                "OP_AN_D_UNIT_CD",
                "OP_AN_D_UNIT_NM",
                "OP_AN_B_UNIT_CD",
                "OP_AN_B_UNIT_NM", //10
                "OP_AN_UNIT_CD",
                "OP_AN_UNIT_NM",
                "OP_AN_CONTENT",
                "OP_AN_REMARK",
                "OP_UPD_ID", //15
                "OP_UPD_NM",
                "OP_UPD_UNIT_CD",
                "OP_UPD_UNIT_NM",
                "OP_UPD_DT_TM",
                "OP_KP_UNIT_NM",//20
                "OP_KP_NM",
                "OP_KP_DATE",
                "OP_KP_TIME",
                "OP_KP_REMARK",
                "OP_KP_UNIT_NM1",//25
                "OP_KP_NM1",
                "OP_KP_DATE1",
                "OP_KP_TIME1",
                "OP_KP_REMARK1",
                "OP_KP_UNIT_NM2",//30
                "OP_KP_NM2",
                "OP_KP_DATE2",
                "OP_KP_TIME2",
                "OP_KP_REMARK2",//34
            }; //23

            sql.append("UPDATE OPDT_I_ANNOUNCE SET ");

            for (int index = 0; index < arryString.length; index++) {
                sql.append(arryString[index] + "=?, ");
            }
            sql.replace(sql.lastIndexOf(","), sql.lastIndexOf(",") + 1, "");
            sql.append("WHERE OP_SEQ_NO =? ");

            Object[] paraObject = new Object[arryString.length + 1];

            // paramenter
            int i = 0;
            for (String strKey : arryString) {
                paraObject[i] = new Object[i];
                if (jObj.has(strKey)) {
                    paraObject[i] = jObj.get(strKey);
                }
                i++;
            }
            //paraObject[arryString.length+1] = new Object[arryString.length+1];
            paraObject[arryString.length] = jObj.getInt("OP_SEQ_NO");

            returnValue = this.pexecuteUpdate(sql.toString(), paraObject) > 0 ? true : false;

            if (returnValue) {
                jObj.put("OP_BASIC_SEQ_NO", jObj.get("OP_BASIC_SEQ_NO"));
                jObj.put("OP_CURSTAT_CD", "2");
                jObj.put("OP_CURSTAT_NM", "公告中");
                iPuBasicDao.updateBasicForStatus(jObj);
                returnValue = true;
            } else {
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
            jObj.put("OP_UPD_UNIT_NM", voUser.getUnitName());
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

            for (int index = 0; index < arryString.length; index++) {
                sql.append(arryString[index] + "=?, ");
            }
            sql.replace(sql.lastIndexOf(","), sql.lastIndexOf(",") + 1, "");
            sql.append("WHERE OP_SEQ_NO =? ");

            Object[] paraObject = new Object[arryString.length + 1];

            // paramenter
            int i = 0;
            for (String strKey : arryString) {
                paraObject[i] = new Object[i];
                if (jObj.has(strKey)) {
                    paraObject[i] = jObj.get(strKey);
                }
                i++;
            }
            //paraObject[arryString.length+1] = new Object[arryString.length+1];
            paraObject[arryString.length] = jObj.getInt("OP_SEQ_NO");

            returnValue = this.pexecuteUpdate(sql.toString(), paraObject) > 0 ? true : false;

            if (returnValue) {
                jObj.put("OP_BASIC_SEQ_NO", Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO")));
                jObj.put("OP_CURSTAT_CD", "2");
                jObj.put("OP_CURSTAT_NM", "公告中");
                iPuBasicDao.updateBasicForStatus(jObj);
                returnValue = true;
            } else {
                returnValue = false;
            }

            jObj.put("OP_SEQ_NO", jObj.get("OP_SEQ_NO"));

        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return returnValue;

    }

}
