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
import java.util.Calendar;
import npa.op.util.NpaConfig;
import npa.op.util.OPUtil;
import npa.op.util.StringUtil;

public class OPDT_I_PU_BASIC extends BaseDao {

    private static OPDT_I_PU_BASIC instance = null;

    public static OPDT_I_PU_BASIC getInstance() {
        if (instance == null) {
            instance = new OPDT_I_PU_BASIC();
        }
        return instance;
    }

    /**
     * 查詢受理基本資料資料 .
     *
     * @param jObj
     * @return CachedRowSet
     */
    public CachedRowSet IPuBasicQueryForDown(JSONObject jObj) {
        ArrayList args = new ArrayList();
        List listkey = null;
        //基本資料序號, 收據編號, 受理單位, 拾得人類別, 物品屬性, 拾得人姓名,
        //身分證/其他證號, 拾得日期, 拾得時間, 拾得地點, 目前狀態代碼, 目前狀態名稱
        //刪除日期, 刪除時間, 刪除單位, 刪除人員
        //(遺失物招領) 是否公告
        String sql = " SELECT DISTINCT"
                + " basic.OP_SEQ_NO, basic.OP_AC_RCNO, basic.OP_AC_UNIT_NM, basic.OP_PUPO_TP_NM, basic.OP_PUOJ_ATTR_NM, basic.OP_PUPO_NAME,"
                + " basic.OP_PUPO_IDN, basic.OP_PU_DATE, basic.OP_PU_TIME, basic.OP_PU_CITY_NM, basic.OP_PU_TOWN_NM, basic.OP_PU_PLACE, basic.OP_CURSTAT_CD, basic.OP_CURSTAT_NM,"
                + " basic.OP_DEL_DATE, basic.OP_DEL_TIME, basic.OP_DEL_UNIT_NM, basic.OP_DEL_STAFF_NM"
                + " , CASE ann.OP_YN_AN WHEN '1' THEN '1' WHEN '2' THEN '2' ELSE '0' END AS OP_YN_AN";
//                //從哪個jsp進來的
//                if ( jObj.has("ACTION_TYPE") ){
//                    if ( jObj.get("ACTION_TYPE").equals("OP03A01Q_01") ){ //拾得遺失物招領
//                        sql += ", ann.OP_YN_AN";
//                    }
//                }
        sql += " FROM "
                + " OPDT_I_PU_BASIC basic";
        sql += " LEFT JOIN OPDT_I_ANNOUNCE ann ON basic.OP_SEQ_NO = ann.OP_BASIC_SEQ_NO";
        //從哪個jsp進來的
        if (jObj.has("ACTION_TYPE")) {
            if (jObj.get("ACTION_TYPE").equals("OP02A05Q_01")) { //拾得遺失物認領
                sql += " LEFT JOIN OPDT_I_PU_CLAIM puClm ON basic.OP_SEQ_NO = puClm.OP_BASIC_SEQ_NO";
                sql += " LEFT JOIN OPDT_E_NET_CLAIM netClm ON basic.OP_SEQ_NO = netClm.OP_BASIC_SEQ_NO";
            }
//                    if ( jObj.get("ACTION_TYPE").equals("OP03A01Q_01") ){ //拾得遺失物招領
//                        sql += " LEFT JOIN OPDT_I_ANNOUNCE ann ON basic.OP_SEQ_NO = ann.OP_BASIC_SEQ_NO";
//                    }
            if (jObj.get("ACTION_TYPE").equals("OP04A01Q_01")) { //拾得人領回公告
                sql += " LEFT JOIN OPDT_I_AN_DL anDl ON basic.OP_SEQ_NO = anDl.OP_BASIC_SEQ_NO";
            }
            if (jObj.get("ACTION_TYPE").equals("OP04A02Q_01")) { //拾得人領回公告期滿
                sql += " LEFT JOIN OPDT_I_AN_DL anDl ON basic.OP_SEQ_NO = anDl.OP_BASIC_SEQ_NO";
            }
        }

        sql += " WHERE 1 = 1 ";

        //從哪個jsp進來的
        if (jObj.has("ACTION_TYPE")) {
            if (jObj.get("ACTION_TYPE").equals("OP02A02Q_01")) { //拾得遺失物維護
                sql += " AND basic.OP_CURSTAT_CD = '1'";
            }
            if (jObj.get("ACTION_TYPE").equals("OP02A03Q_01")) { //拾得遺失物刪除
                sql += " AND basic.OP_CURSTAT_CD = '1'";
            }
            if (jObj.get("ACTION_TYPE").equals("OP02A05Q_01")) { //拾得遺失物認領
                if (jObj.has("OP_PUCP_NAME")) {
                    //認領人
                    if (!jObj.get("OP_PUCP_NAME").equals("")) {
                        sql += " AND (puClm.OP_PUCP_NAME = ?";
                        args.add(jObj.get("OP_PUCP_NAME"));
                        sql += " OR netClm.OP_PUCP_NAME = ?)";
                        args.add(jObj.get("OP_PUCP_NAME"));
                    }
                }
                sql += " AND basic.OP_CURSTAT_CD <= '2'";
            }
            if (jObj.get("ACTION_TYPE").equals("OP03A01Q_01")) { //拾得遺失物招領
                if (jObj.has("OP_YN_AN")) {
                    //是否公告
                    if (!jObj.get("OP_YN_AN").equals("0")) {
                        sql += " AND ann.OP_YN_AN = ?";
                        sql += " AND basic.OP_CURSTAT_CD = '2'";
                        args.add(jObj.get("OP_YN_AN"));
                    } else {
                        sql += " AND basic.OP_CURSTAT_CD = '1'";
                    }
                }
                //發文字
                if (jObj.has("OP_DOC_WD")) {
                    if (!jObj.get("OP_DOC_WD").equals("")) {
                        sql += " AND ann.OP_DOC_WD = ?";
                        args.add(jObj.get("OP_DOC_WD"));
                    }
                }
                //發文號
                if (jObj.has("OP_DOC_NO")) {
                    if (!jObj.get("OP_DOC_NO").equals("")) {
                        sql += " AND ann.OP_DOC_NO = ?";
                        args.add(jObj.get("OP_DOC_NO"));
                    }
                }
            }
            if (jObj.get("ACTION_TYPE").equals("OP03A02Q_01")) { //拾得遺失物招領期滿
                if (jObj.has("OP_CURSTAT_CD")) {
                    //目前狀態
                    if (!jObj.get("OP_CURSTAT_CD").equals("")) {
                        sql += " AND basic.OP_CURSTAT_CD = ?";
                        args.add(jObj.get("OP_CURSTAT_CD"));
                    } else {
                        sql += " AND basic.OP_CURSTAT_CD in ('3','4')";
                    }
                }
                //發文字
                if (jObj.has("OP_DOC_WD")) {
                    if (!jObj.get("OP_DOC_WD").equals("")) {
                        sql += " AND ann.OP_DOC_WD = ?";
                        args.add(jObj.get("OP_DOC_WD"));
                    }
                }
                //發文號
                if (jObj.has("OP_DOC_NO")) {
                    if (!jObj.get("OP_DOC_NO").equals("")) {
                        sql += " AND ann.OP_DOC_NO = ?";
                        args.add(jObj.get("OP_DOC_NO"));
                    }
                }
            }
            if (jObj.get("ACTION_TYPE").equals("OP04A01Q_01")) { //拾得人領回公告
                if (jObj.has("OP_NTC_GB_REC")) { //通知領回紀錄
                    if (!jObj.get("OP_NTC_GB_REC").equals("")) {
                        if (jObj.get("OP_NTC_GB_REC").equals("0")) {
                            sql += " AND anDl.OP_NTC_GB_REC = ?";
                            args.add(jObj.get("OP_NTC_GB_REC"));
                        } else {
                            sql += " AND anDl.OP_NTC_GB_REC in ('1','2')";
                        }
                    }
                    sql += " AND basic.OP_CURSTAT_CD = '4'";
                }
                //發文字
                if (jObj.has("OP_PUPO_DOC_WD")) {
                    if (!jObj.get("OP_PUPO_DOC_WD").equals("")) {
                        sql += " AND anDl.OP_PUPO_DOC_WD = ?";
                        args.add(jObj.get("OP_PUPO_DOC_WD"));
                    }
                }
                //發文號
                if (jObj.has("OP_PUPO_DOC_NO")) {
                    if (!jObj.get("OP_PUPO_DOC_NO").equals("")) {
                        sql += " AND anDl.OP_PUPO_DOC_NO = ?";
                        args.add(jObj.get("OP_PUPO_DOC_NO"));
                    }
                }
            }
            if (jObj.get("ACTION_TYPE").equals("OP04A02Q_01")) { //拾得人領回公告期滿
                sql += " AND basic.OP_CURSTAT_CD = '5'";
                //發文字
                if (jObj.has("OP_PUPO_DOC_WD")) {
                    if (!jObj.get("OP_PUPO_DOC_WD").equals("")) {
                        sql += " AND anDl.OP_PUPO_DOC_WD = ?";
                        args.add(jObj.get("OP_PUPO_DOC_WD"));
                    }
                }
                //發文號
                if (jObj.has("OP_PUPO_DOC_NO")) {
                    if (!jObj.get("OP_PUPO_DOC_NO").equals("")) {
                        sql += " AND anDl.OP_PUPO_DOC_NO = ?";
                        args.add(jObj.get("OP_PUPO_DOC_NO"));
                    }
                }

            }
            if (jObj.get("ACTION_TYPE").equals("OP05A01Q_01")) { //結案維護
                if (!jObj.get("OP_CURSTAT_CD").equals("")) {
                    sql += " AND basic.OP_CURSTAT_CD = ?";
                    args.add(jObj.get("OP_CURSTAT_CD"));
                }
            }
        }
        //受理單位
        if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL4")).equals("")) {
            sql += " AND basic.OP_AC_UNIT_CD = ?";
            args.add(jObj.get("OP_UNITLEVEL4"));
        } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")) {
            sql += " AND basic.OP_AC_B_UNIT_CD = ?";
            args.add(jObj.get("OP_UNITLEVEL3"));
        } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
            sql += " AND basic.OP_AC_D_UNIT_CD = ?";
            args.add(jObj.get("OP_UNITLEVEL2"));
        }
        //收據編號
        if (!jObj.get("OP_AC_RCNO").equals("")) {
            sql += " AND basic.OP_AC_RCNO = ?";
            args.add(jObj.get("OP_AC_RCNO"));
        }
        //拾得人類別
        if (!jObj.get("OP_PUPO_TP_CD").equals("")) {
            sql += " AND basic.OP_PUPO_TP_CD = ?";
            args.add(jObj.get("OP_PUPO_TP_CD"));
        }
        //拾得日期起迄
        if (!jObj.get("OP_PU_DATE_S").toString().equals("")) {
            String pudate_s = jObj.get("OP_PU_DATE_S").toString();
            if (!jObj.get("OP_PU_DATE_S").equals("")) {
                sql += " AND basic.OP_PU_DATE >= ?";
                args.add(pudate_s);
            }
        }
        if (!jObj.get("OP_PU_DATE_E").toString().equals("")) {
            String pudate_e = jObj.get("OP_PU_DATE_E").toString();
            if (!jObj.get("OP_PU_DATE_E").equals("")) {
                sql += " AND basic.OP_PU_DATE <= ?";
                args.add(pudate_e);
            }
        }
        //受理日期起迄
        if (!jObj.get("OP_AC_DATE_S").toString().equals("")) {
            String acdate_s = jObj.get("OP_AC_DATE_S").toString();
            if (!jObj.get("OP_AC_DATE_S").equals("")) {
                sql += " AND basic.OP_AC_DATE >= ?";
                args.add(acdate_s);
            }
        }
        if (!jObj.get("OP_AC_DATE_E").toString().equals("")) {
            String acdate_e = jObj.get("OP_AC_DATE_E").toString();
            if (!jObj.get("OP_AC_DATE_E").equals("")) {
                sql += " AND basic.OP_AC_DATE <= ?";
                args.add(acdate_e);
            }
        }
        //物品屬性
        if (jObj.has("OP_PUOJ_ATTR_CD")) {
            if (!jObj.get("OP_PUOJ_ATTR_CD").equals("")) {
                sql += " AND basic.OP_PUOJ_ATTR_CD = ?";
                args.add(jObj.get("OP_PUOJ_ATTR_CD"));
            }
        }
        //拾得物總價值是否超過500
        if (!jObj.get("OP_PU_YN_OV500").equals("")) {
            sql += " AND basic.OP_PU_YN_OV500 = ?";
            args.add(jObj.get("OP_PU_YN_OV500"));
        }
        //OP_DEL_FLAG
        if (!jObj.get("OP_DEL_FLAG").equals("")) {
            sql += " AND basic.OP_DEL_FLAG = ?";
            args.add(jObj.get("OP_DEL_FLAG"));
        }

        sql += " ORDER BY basic.OP_PU_DATE DESC, basic.OP_PU_TIME DESC";

        CachedRowSet crs = this.pexecuteQueryRowSetHasMaxRows(sql, args.toArray());

        return crs;
    }

    /**
     * 伍佰元專區受理基本資料資料 .
     *
     * @param jObj
     * @return CachedRowSet
     */
    public CachedRowSet IPuBasicQueryForValueUnder500(JSONObject jObj) {
        ArrayList args = new ArrayList();
        List listkey = null;
        //基本資料序號, 收據編號, 受理單位, 拾得人類別, 物品屬性, 拾得人姓名,
        //身分證/其他證號, 拾得日期, 拾得時間, 拾得地點, 目前狀態代碼, 目前狀態名稱
        //刪除日期, 刪除時間, 刪除單位, 刪除人員, 是否已經刪除
        //(遺失物招領) 是否公告
        String sql = " SELECT DISTINCT"
                + " A.OP_SEQ_NO, A.OP_AC_RCNO, A.OP_AC_UNIT_NM, A.OP_PUPO_TP_NM, A.OP_PUOJ_ATTR_NM, A.OP_PUPO_NAME,"
                + " A.OP_PUPO_IDN, A.OP_PU_DATE, A.OP_PU_TIME, A.OP_PU_CITY_NM, A.OP_PU_TOWN_NM, A.OP_PU_PLACE, A.OP_CURSTAT_CD, A.OP_CURSTAT_NM,"
                + " A.OP_DEL_DATE, A.OP_DEL_TIME, A.OP_DEL_UNIT_NM, A.OP_DEL_STAFF_NM, A.OP_DEL_FLAG";
        sql += " FROM "
                + " OPDT_I_PU_BASIC A";
        //從哪個jsp進來的
        if (jObj.has("ACTION_TYPE")) {
            if (jObj.get("ACTION_TYPE").equals("OP08A02Q_01")) { //伍佰元專區拾得遺失物認領
                sql += " LEFT JOIN OPDT_I_PU_CLAIM B ON A.OP_SEQ_NO = B.OP_BASIC_SEQ_NO";
            }
        }
        sql += " WHERE 1 = 1 ";

        //受理單位
        if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL4")).equals("")) {
            sql += " AND A.OP_AC_UNIT_CD = ?";
            args.add(jObj.get("OP_UNITLEVEL4"));
        } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")) {
            sql += " AND A.OP_AC_B_UNIT_CD = ?";
            args.add(jObj.get("OP_UNITLEVEL3"));
        } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
            sql += " AND A.OP_AC_D_UNIT_CD = ?";
            args.add(jObj.get("OP_UNITLEVEL2"));
        }
        //收據編號
        if (!jObj.get("OP_AC_RCNO").equals("")) {
            sql += " AND A.OP_AC_RCNO = ?";
            args.add(jObj.get("OP_AC_RCNO"));
        }
        //拾得人類別
        if (!jObj.get("OP_PUPO_TP_CD").equals("")) {
            sql += " AND A.OP_PUPO_TP_CD = ?";
            args.add(jObj.get("OP_PUPO_TP_CD"));
        }
        //拾得日期起迄
        if (!jObj.get("OP_PU_DATE_S").toString().equals("")) {
            String pudate_s = jObj.get("OP_PU_DATE_S").toString();
            if (!jObj.get("OP_PU_DATE_S").equals("")) {
                sql += " AND A.OP_PU_DATE >= ?";
                args.add(pudate_s);
            }
        }
        if (!jObj.get("OP_PU_DATE_E").toString().equals("")) {
            String pudate_e = jObj.get("OP_PU_DATE_E").toString();
            if (!jObj.get("OP_PU_DATE_E").equals("")) {
                sql += " AND A.OP_PU_DATE <= ?";
                args.add(pudate_e);
            }
        }
        //受理日期起迄
        if (!jObj.get("OP_AC_DATE_S").toString().equals("")) {
            String acdate_s = jObj.get("OP_AC_DATE_S").toString();
            if (!jObj.get("OP_AC_DATE_S").equals("")) {
                sql += " AND A.OP_AC_DATE >= ?";
                args.add(acdate_s);
            }
        }
        if (!jObj.get("OP_AC_DATE_E").toString().equals("")) {
            String acdate_e = jObj.get("OP_AC_DATE_E").toString();
            if (!jObj.get("OP_AC_DATE_E").equals("")) {
                sql += " AND A.OP_AC_DATE <= ?";
                args.add(acdate_e);
            }
        }
        //物品屬性
        if (jObj.has("OP_PUOJ_ATTR_CD")) {
            if (!jObj.get("OP_PUOJ_ATTR_CD").equals("")) {
                sql += " AND A.OP_PUOJ_ATTR_CD = ?";
                args.add(jObj.get("OP_PUOJ_ATTR_CD"));
            }
        }
        //拾得物總價值是否超過500
        if (!jObj.get("OP_PU_YN_OV500").equals("")) {
            sql += " AND A.OP_PU_YN_OV500 = ?";
            args.add(jObj.get("OP_PU_YN_OV500"));
        }
        //OP_DEL_FLAG
        if (!jObj.get("OP_DEL_FLAG").equals("")) {
            sql += " AND A.OP_DEL_FLAG = ?";
            args.add(jObj.get("OP_DEL_FLAG"));
        }
        //從哪個jsp進來的
        if (jObj.has("ACTION_TYPE")) {
            if (jObj.get("ACTION_TYPE").equals("OP08A01Q_01")) { //伍佰元專區 拾得遺失物維護
                sql += " AND A.OP_CURSTAT_CD = '1'";
            } else if (jObj.get("ACTION_TYPE").equals("OP08A02Q_01")) { //伍佰元專區 拾得遺失物認領
                if (jObj.has("OP_PUCP_NAME")) {
                    //認領人
                    if (!jObj.get("OP_PUCP_NAME").equals("")) {
                        sql += " AND B.OP_PUCP_NAME = ?";
                        args.add(jObj.get("OP_PUCP_NAME"));
                    }
                }
                sql += " AND A.OP_CURSTAT_CD = '1'";
            } else if (jObj.get("ACTION_TYPE").equals("OP08A03Q_01")) { //伍佰元專區 結案資料
                if (jObj.has("OP_CURSTAT_CD")) {
                    //目前狀態
                    if (!jObj.get("OP_CURSTAT_CD").equals("")) {
                        sql += " AND A.OP_CURSTAT_CD = ?";
                        args.add(jObj.get("OP_CURSTAT_CD"));
                    }
                }
            }
        }

        sql += " ORDER BY A.OP_PU_DATE DESC, A.OP_PU_TIME DESC";

        CachedRowSet crs = this.pexecuteQueryRowSetHasMaxRows(sql, args.toArray());

        return crs;
    }

    /**
     * 綜合查詢受理基本資料資料 .
     *
     * @param jObj
     * @return CachedRowSet
     */
    public ArrayList<HashMap> CompositeSearchForBasic(JSONObject jObj) {
        ArrayList args = new ArrayList();
        ArrayList<HashMap> tmpList = new ArrayList();
        List listkey = null;
        //基本資料序號, 收據編號, 受理單位, 拾得人類別, 物品屬性, 拾得人姓名,
        //身分證/其他證號, 拾得日期, 拾得時間, 拾得地點, 目前狀態代碼, 目前狀態名稱
        //刪除日期, 刪除時間, 刪除單位, 刪除人員
        String sql = " SELECT"
                + " A.OP_SEQ_NO, A.OP_AC_RCNO, A.OP_AC_UNIT_NM, A.OP_PUPO_TP_NM, A.OP_PUOJ_ATTR_NM, ISNULL(A.OP_PUPO_NAME,'') AS OP_PUPO_NAME,"
                + " ISNULL(A.OP_PUPO_IDN,'') AS OP_PUPO_IDN, A.OP_PUPO_PHONENO, A.OP_PUPO_BIRTHDT, A.OP_PU_DATE, A.OP_PU_TIME, A.OP_PU_CITY_NM, A.OP_PU_TOWN_NM, A.OP_PU_PLACE, A.OP_CURSTAT_CD, A.OP_CURSTAT_NM,"
                + " A.OP_DEL_DATE, A.OP_DEL_TIME, A.OP_DEL_UNIT_NM, A.OP_DEL_STAFF_NM, A.OP_PU_YN_OV500,"
                + " ISNULL(C.OP_PUOJ_NM,'') AS OP_PUOJ_NM";
        sql += " FROM "
                + " OPDT_I_PU_BASIC A";
        sql += " LEFT JOIN OPDT_I_PU_DETAIL C ON A.OP_SEQ_NO = C.OP_BASIC_SEQ_NO";
        sql += " LEFT JOIN OPDT_I_FNSH B ON A.OP_SEQ_NO = B.OP_BASIC_SEQ_NO";

        sql += " WHERE 1 = 1 ";
        sql += " AND A.OP_DEL_FLAG = '0' ";

        //受理單位
        if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL4")).equals("")) {
            sql += " AND A.OP_AC_UNIT_CD = ?";
            args.add(jObj.get("OP_UNITLEVEL4"));
        } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")) {
            sql += " AND A.OP_AC_B_UNIT_CD = ?";
            args.add(jObj.get("OP_UNITLEVEL3"));
        } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
            sql += " AND A.OP_AC_D_UNIT_CD = ?";
            args.add(jObj.get("OP_UNITLEVEL2"));
        }

        //受理人員
        if (!jObj.get("OP_AC_STAFF_NM").equals("")) {
            sql += " AND A.OP_AC_STAFF_NM = ?";
            args.add(jObj.get("OP_AC_STAFF_NM"));
        }
        //收據編號
        if (!jObj.get("OP_AC_RCNO").equals("")) {
            sql += " AND A.OP_AC_RCNO = ?";
            args.add(jObj.get("OP_AC_RCNO"));
        }
        //拾得人姓名
        if (!jObj.get("OP_PUPO_NAME").equals("")) {
            sql += " AND A.OP_PUPO_NAME = ?";
            args.add(jObj.get("OP_PUPO_NAME"));
        }
        //性別
        if (!jObj.get("OP_PUPO_GENDER").equals("")) {
            sql += " AND A.OP_PUPO_GENDER = ?";
            args.add(jObj.get("OP_PUPO_GENDER"));
        }
        //身分證/其他證號
        if (!jObj.get("OP_PUPO_IDN_TP").equals("")) {
            sql += " AND A.OP_PUPO_IDN_TP = ?";
            args.add(jObj.get("OP_PUPO_IDN_TP"));
        }
        //證號
        if (!jObj.get("OP_PUPO_IDN").equals("")) {
            sql += " AND A.OP_PUPO_IDN = ?";
            args.add(jObj.get("OP_PUPO_IDN"));
        }
        //拾得日期起迄
        if (!jObj.get("OP_PU_DATE_S").toString().equals("")) {
            String pudate_s = jObj.get("OP_PU_DATE_S").toString();
            if (!jObj.get("OP_PU_DATE_S").equals("")) {
                sql += " AND A.OP_PU_DATE >= ?";
                args.add(pudate_s);
            }
        }
        if (!jObj.get("OP_PU_DATE_E").toString().equals("")) {
            String pudate_e = jObj.get("OP_PU_DATE_E").toString();
            if (!jObj.get("OP_PU_DATE_E").equals("")) {
                sql += " AND A.OP_PU_DATE <= ?";
                args.add(pudate_e);
            }
        }
        //受理日期起迄
        if (!jObj.get("OP_AC_DATE_S").toString().equals("")) {
            String acdate_s = jObj.get("OP_AC_DATE_S").toString();
            if (!jObj.get("OP_AC_DATE_S").equals("")) {
                sql += " AND A.OP_AC_DATE >= ?";
                args.add(acdate_s);
            }
        }
        if (!jObj.get("OP_AC_DATE_E").toString().equals("")) {
            String acdate_e = jObj.get("OP_AC_DATE_E").toString();
            if (!jObj.get("OP_AC_DATE_E").equals("")) {
                sql += " AND A.OP_AC_DATE <= ?";
                args.add(acdate_e);
            }
        }
        //物品屬性
        if (jObj.has("OP_PUOJ_ATTR_CD")) {
            if (!jObj.get("OP_PUOJ_ATTR_CD").equals("")) {
                sql += " AND A.OP_PUOJ_ATTR_CD = ?";
                args.add(jObj.get("OP_PUOJ_ATTR_CD"));
            }
        }
        //目前狀態
        if (jObj.has("OP_CURSTAT_CD")) {
            if (!jObj.get("OP_CURSTAT_CD").equals("")) {
                sql += " AND A.OP_CURSTAT_CD = ?";
                args.add(jObj.get("OP_CURSTAT_CD"));
            }
        }
        //結案紀錄
        if (jObj.has("OP_FS_REC_CD")) {
            if (!jObj.get("OP_FS_REC_CD").equals("")) {
                sql += " AND B.OP_FS_REC_CD = ?";
                args.add(jObj.get("OP_FS_REC_CD"));
            }
        }
        //拾得物總價值是否超過500
        if (!jObj.get("OP_PU_YN_OV500").equals("")) {
            sql += " AND A.OP_PU_YN_OV500 = ?";
            args.add(jObj.get("OP_PU_YN_OV500"));
        }

        //發還人員
        if (!jObj.get("OP_RT_STAFF_NM").equals("")) {
            sql += " AND B.OP_RT_STAFF_NM = ?";
            args.add(jObj.get("OP_RT_STAFF_NM"));
        }

        CachedRowSet crs = null;

        //物品名稱
        if (!jObj.get("OP_PUOJ_NM").equals("")) {
            //String resultStr = luence.searchKeyWord("OP_PUOJ_NM", jObj.getString("OP_PUOJ_NM"), "OP_BASIC_SEQ_NO");
            //以 物品名稱 + 總局代碼 做搜尋
            String OP_PUOJ_NM = "", OP_AC_UNIT_CD = "";
            String OP_PUOJ_NM_VAL = "", OP_AC_UNIT_CD_VAL = "";
            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL4")).equals("")) {
                OP_PUOJ_NM = "OP_PUOJ_NM";
                OP_AC_UNIT_CD = "OP_AC_UNIT_CD";
                OP_PUOJ_NM_VAL = jObj.getString("OP_PUOJ_NM").trim().toString();
                OP_AC_UNIT_CD_VAL = jObj.getString("OP_UNITLEVEL4").trim();
            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")) {
                OP_PUOJ_NM = "OP_PUOJ_NM";
                OP_AC_UNIT_CD = "OP_AC_B_UNIT_CD";
                OP_PUOJ_NM_VAL = jObj.getString("OP_PUOJ_NM").trim().toString();
                OP_AC_UNIT_CD_VAL = jObj.getString("OP_UNITLEVEL3").trim();
            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
                OP_PUOJ_NM = "OP_PUOJ_NM";
                OP_AC_UNIT_CD = "OP_AC_D_UNIT_CD";
                OP_PUOJ_NM_VAL = jObj.getString("OP_PUOJ_NM").trim().toString();
                OP_AC_UNIT_CD_VAL = jObj.getString("OP_UNITLEVEL2").trim();
            }
            String[] indexColName = {OP_PUOJ_NM, OP_AC_UNIT_CD};
            String[] indexColValue = {OP_PUOJ_NM_VAL, OP_AC_UNIT_CD_VAL};
            String resultStr = luence.searchKeyWord(indexColName, indexColValue, "OP_BASIC_SEQ_NO");
            if (resultStr.equals("")) { //查無資料(索引檔無資料)
                sql += " AND 1 = 2 ";
                sql += " ORDER BY A.OP_SEQ_NO DESC, C.OP_SEQ_NO ASC";
//                        crs = this.pexecuteQueryRowSetHasMaxRows(sql,args.toArray());
                tmpList = this.pexecuteQuery(sql, args.toArray());
            } else {
                jObj.put("indexPUOJ_NM", resultStr);
                String[] seqNos = jObj.getString("indexPUOJ_NM").trim().split(",");
                System.out.println(seqNos.length);
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < seqNos.length; i++) {
                    if (!list.contains(seqNos[i].trim())) {
                        list.add(seqNos[i].trim());
                    }
                }
                System.out.println(list.size());
                int point = 0;
                int searchSpliteNumber = NpaConfig.getInt("searchSpliteNumber"); //切割數字
                int hitsDiv200 = list.size() / searchSpliteNumber;
                if (list.size() % searchSpliteNumber > 0) {
                    hitsDiv200++;
                }
                if (hitsDiv200 == 0) {
                    hitsDiv200 = 1;
                }
                for (int div = 1; div <= hitsDiv200; div++) {
                    String tempSeq = "";
                    String temqWhere = "";
                    ArrayList argsTemp = new ArrayList();
                    temqWhere += " AND A.OP_SEQ_NO IN ( ";
                    for (point = 0 + searchSpliteNumber * (div - 1); point < div * searchSpliteNumber; point++) {
                        tempSeq += ",?";
                        argsTemp.add(Integer.parseInt(list.get(point).trim()));
                        //最後一個號碼後跳出
                        if ((point) == (list.size() - 1)) {
                            break;
                        }
                    }
                    temqWhere += tempSeq.substring(1) + " ) ";
                    temqWhere += " ORDER BY A.OP_SEQ_NO DESC, C.OP_SEQ_NO ASC";
                    System.out.println(temqWhere);
                    argsTemp.addAll(0, args);
                    tmpList.addAll(this.pexecuteQuery(sql + temqWhere, argsTemp.toArray()));
                }
//                        System.out.println("物品名稱: "+ jObj.getString("OP_PUOJ_NM") +" seq: "+ resultStr);
//                        sql += " AND A.OP_SEQ_NO IN ( ";
//                        String[] seqNos = jObj.getString("indexPUOJ_NM").split(",");
//                        String tempSeq = "";
//                        for (String seqNo: seqNos) {
//                            tempSeq += ",?";
//                            args.add(Integer.parseInt(seqNo.trim()));
//                        }
//                        sql += tempSeq.substring(1) + " ) ";
            }
        } else {
            sql += " ORDER BY A.OP_SEQ_NO DESC, C.OP_SEQ_NO ASC";
//                    crs = this.pexecuteQueryRowSetHasMaxRows(sql,args.toArray());
            tmpList = this.pexecuteQuery(sql, args.toArray());
        }

        return tmpList;
    }

    //region 明細
    /**
     * 查詢受理基本資料明細 .
     *
     * @param OP_BASIC_SEQ_NO
     * @return JSONArray
     * @throws SQLException
     * @throws WriterException
     * @throws IOException
     */
    public JSONArray queryBasicById(int OP_SEQ_NO) throws SQLException, IOException, WriterException {
        ArrayList args = new ArrayList();
        CachedRowSet crs1, crs2;
        String sql = " SELECT *"
                + " FROM OPDT_I_PU_BASIC"
                + " WHERE OP_SEQ_NO = ? ";
        args.add(OP_SEQ_NO);

        ArrayList<HashMap> list = this.pexecuteQuery(sql, args.toArray());

        String OP_AC_DATE = "", OP_FM_DATE = "", opPuDTTM = "", OP_PU_DATE = "", OP_PU_TIME = "", OP_NTC_DATE = "", OP_DEL_DATE = "", OP_DEL_TIME = "";
        String OP_PUPO_BIRTHDT = "";
        if (list.get(0).get("OP_PUPO_BEFROC").toString().equals("2")) { //西元
            //直接出去
        } else { //民前 民國
            OP_PUPO_BIRTHDT = DateUtil.convertBirthdayTime(list.get(0).get("OP_PUPO_BIRTHDT").toString(), list.get(0).get("OP_PUPO_BEFROC").toString());
            list.get(0).put("OP_PUPO_BIRTHDT", OP_PUPO_BIRTHDT);
        }
        OP_AC_DATE = DateUtil.to7TwDateTime(list.get(0).get("OP_AC_DATE").toString());
        list.get(0).put("OP_AC_DATE", OP_AC_DATE);
        OP_FM_DATE = DateUtil.to7TwDateTime(list.get(0).get("OP_FM_DATE").toString());
        list.get(0).put("OP_FM_DATE", OP_FM_DATE);
        OP_PU_DATE = DateUtil.to7TwDateTime(list.get(0).get("OP_PU_DATE").toString());
        list.get(0).put("OP_PU_DATE", OP_PU_DATE);
        OP_PU_TIME = DateUtil.to4TwTime(list.get(0).get("OP_PU_TIME").toString());
        list.get(0).put("OP_PU_TIME", OP_PU_TIME);
        OP_NTC_DATE = DateUtil.to7TwDateTime(list.get(0).get("OP_NTC_DATE").toString());
        list.get(0).put("OP_NTC_DATE", OP_NTC_DATE);
        OP_DEL_DATE = DateUtil.to7TwDateTime(list.get(0).get("OP_DEL_DATE").toString());
        list.get(0).put("OP_DEL_DATE", OP_DEL_DATE);
        OP_DEL_TIME = DateUtil.to4TwTime(list.get(0).get("OP_DEL_TIME").toString());
        list.get(0).put("OP_DEL_TIME", OP_DEL_TIME);

        return arrayList2JsonArray(list);
    }

    //region 明細
    /**
     * 查詢受理基本資料明細 .(DB原始資料)
     *
     * @param OP_BASIC_SEQ_NO
     * @return JSONArray
     * @throws SQLException
     * @throws WriterException
     * @throws IOException
     */
    public JSONArray queryBasicByIdOriginal(int OP_SEQ_NO) throws SQLException, IOException, WriterException {
        ArrayList args = new ArrayList();
        CachedRowSet crs1, crs2;
        String sql = " SELECT *"
                + " FROM OPDT_I_PU_BASIC"
                + " WHERE OP_SEQ_NO = ? ";
        args.add(OP_SEQ_NO);

        ArrayList<HashMap> list = this.pexecuteQuery(sql, args.toArray());

        return arrayList2JsonArray(list);
    }

    // region 新增 model Start
    /**
     * 新增遺失物受理資料 .
     *
     * @param jObj
     * @return boolean
     */
    public boolean add(JSONObject jObj) {
        boolean returnValue = false;
        StringBuilder sql = new StringBuilder();
        Date current = new Date();
        OPDT_E0_NPAUNITDao daoNPAUNIT = OPDT_E0_NPAUNITDao.getInstance();

        try {
            User voUser = new User();
            OPUtil opUtil = new OPUtil();
            voUser = (User) jObj.get("userVO");
            jObj.put("OP_CURSTAT_CD", jObj.get("OP_CURSTAT_CD"));
            jObj.put("OP_CURSTAT_NM", jObj.get("OP_CURSTAT_NM"));
            jObj.put("OP_DEL_FLAG", "0");
            jObj.put("OP_DEL_UNIT_CD", "");
            jObj.put("OP_DEL_UNIT_NM", "");
            jObj.put("OP_DEL_STAFF_ID", "");
            jObj.put("OP_DEL_STAFF_NM", "");
            jObj.put("OP_DEL_DATE", "");
            jObj.put("OP_DEL_TIME", "");
            jObj.put("OP_DEL_RSN", "");
            jObj.put("OP_AC_RCNO", opUtil.getNewRCNO(voUser));
            jObj.put("OP_MANUAL_RCNO", jObj.get("OP_MANUAL_RCNO"));
            if (jObj.get("OP_AC_DATE").equals("")) {
                jObj.put("OP_AC_DATE", "");
            } else {
                jObj.put("OP_AC_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_AC_DATE").toString()));
            }
            if (jObj.get("OP_FM_DATE").equals("")) {
                jObj.put("OP_FM_DATE", "");
            } else {
                jObj.put("OP_FM_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_FM_DATE").toString()));
            }

            if (!jObj.get("OP_UNITLEVEL2").equals("")) {
                jObj.put("OP_AC_D_UNIT_CD", jObj.get("OP_UNITLEVEL2"));
                jObj.put("OP_AC_D_UNIT_CD_OLD", jObj.get("OP_UNITLEVEL2"));
                jObj.put("OP_AC_D_UNIT_NM", daoNPAUNIT.getUnitName(jObj.getString("OP_AC_D_UNIT_CD")));
            } else {
                jObj.put("OP_AC_D_UNIT_CD", "");
                jObj.put("OP_AC_D_UNIT_CD_OLD", "");
                jObj.put("OP_AC_D_UNIT_NM", "");
            }

            if (!jObj.get("OP_UNITLEVEL3").equals("")) {
                jObj.put("OP_AC_B_UNIT_CD", jObj.get("OP_UNITLEVEL3"));
                jObj.put("OP_AC_B_UNIT_CD_OLD", jObj.get("OP_UNITLEVEL3"));
                jObj.put("OP_AC_B_UNIT_NM", daoNPAUNIT.getUnitName(jObj.getString("OP_AC_B_UNIT_CD")));
            } else {
                jObj.put("OP_AC_B_UNIT_CD", "");
                jObj.put("OP_AC_B_UNIT_CD_OLD", "");
                jObj.put("OP_AC_B_UNIT_NM", "");
            }
            jObj.put("OP_AC_UNIT_CD", voUser.getUnitCd());
            jObj.put("OP_AC_UNIT_NM", voUser.getUnitName());
            jObj.put("OP_AC_UNIT_CD_OLD", voUser.getUnitCd());
            jObj.put("OP_AC_STAFF_ID", voUser.getUserId());
            jObj.put("OP_AC_STAFF_NM", voUser.getUserName());
            jObj.put("OP_AC_UNIT_TEL", jObj.get("OP_AC_UNIT_TEL"));
            jObj.put("OP_PUPO_TP_CD", jObj.get("OP_PUPO_TP_CD"));
            jObj.put("OP_PUPO_TP_NM", jObj.get("OP_PUPO_TP_NM"));
            jObj.put("OP_IS_CUST", jObj.get("OP_IS_CUST"));
            jObj.put("OP_IS_PUT_NM", jObj.get("OP_IS_PUT_NM"));
            jObj.put("OP_INCLUDE_CERT", jObj.get("OP_INCLUDE_CERT"));
            jObj.put("OP_PUPO_NAME", jObj.get("OP_PUPO_NAME"));
            if (jObj.has("OP_PUPO_RNAME")) {
                jObj.put("OP_PUPO_RNAME", jObj.get("OP_PUPO_RNAME"));
            } else {
                jObj.put("OP_PUPO_RNAME", "");
            }
            jObj.put("OP_PUPO_IDN_TP", jObj.get("OP_PUPO_IDN_TP"));
            jObj.put("OP_PUPO_IDN", jObj.get("OP_PUPO_IDN"));
            jObj.put("OP_PUPO_GENDER", jObj.get("OP_PUPO_GENDER"));
            jObj.put("OP_PUPO_BEFROC", jObj.get("OP_PUPO_BEFROC"));
            if (jObj.get("OP_PUPO_BIRTHDT").equals("")) {
                jObj.put("OP_PUPO_BIRTHDT", "");
            } else {
                if (jObj.get("OP_PUPO_BEFROC").equals("2")) { //西元
                    jObj.put("OP_PUPO_BIRTHDT", jObj.get("OP_PUPO_BIRTHDT"));
                } else { //民前 民國
                    jObj.put("OP_PUPO_BIRTHDT", DateUtil.toNormalDateTime(jObj.get("OP_PUPO_BIRTHDT").toString(), jObj.get("OP_PUPO_BEFROC").toString()));
                }
            }
            jObj.put("OP_PUPO_NAT_CD", jObj.get("OP_PUPO_NAT_CD"));
            jObj.put("OP_PUPO_NAT_NM", jObj.get("OP_PUPO_NAT_NM"));
            jObj.put("OP_PUPO_ZIPCODE", jObj.get("OP_PUPO_ZIPCODE"));
            jObj.put("OP_OC_ADDR_TYPE_CD", jObj.get("OP_OC_ADDR_TYPE_CD"));
            if (jObj.get("OP_OC_ADDR_TYPE_CD").equals("1")) { //一般地址
                jObj.put("OP_OC_ADDR_TYPE_NM", "一般地址");
                jObj.put("OP_PUPO_CITY_CD", jObj.get("OP_PUPO_CITY_CD"));
                jObj.put("OP_PUPO_CITY_NM", jObj.get("OP_PUPO_CITY_NM"));
                jObj.put("OP_PUPO_TOWN_CD", jObj.get("OP_PUPO_TOWN_CD"));
                jObj.put("OP_PUPO_TOWN_NM", jObj.get("OP_PUPO_TOWN_NM"));
                jObj.put("OP_PUPO_VILLAGE_CD", jObj.get("OP_PUPO_VILLAGE_CD"));
                jObj.put("OP_PUPO_VILLAGE_NM", jObj.get("OP_PUPO_VILLAGE_NM"));
                jObj.put("OP_PUPO_LIN", jObj.get("OP_PUPO_LIN"));
                jObj.put("OP_PUPO_ROAD", jObj.get("OP_PUPO_ROAD"));
                jObj.put("OP_PUPO_ADDR_OTH", "");
            } else { //自由輸入
                jObj.put("OP_OC_ADDR_TYPE_NM", "其他");
                jObj.put("OP_PUPO_CITY_CD", "");
                jObj.put("OP_PUPO_CITY_NM", "");
                jObj.put("OP_PUPO_TOWN_CD", "");
                jObj.put("OP_PUPO_TOWN_NM", "");
                jObj.put("OP_PUPO_VILLAGE_CD", "");
                jObj.put("OP_PUPO_VILLAGE_NM", "");
                jObj.put("OP_PUPO_LIN", "");
                jObj.put("OP_PUPO_ROAD", "");
                jObj.put("OP_PUPO_ADDR_OTH", jObj.get("OP_PUPO_ADDR_OTH"));
            }
            jObj.put("OP_PUPO_PHONENO", jObj.get("OP_PUPO_PHONENO"));
            jObj.put("OP_PUPO_EMAIL", jObj.get("OP_PUPO_EMAIL"));
            jObj.put("OP_NTC_FIND_PO", jObj.get("OP_NTC_FIND_PO"));
            jObj.put("OP_YN_NTC", jObj.get("OP_YN_NTC"));
            if (jObj.get("OP_NTC_DATE").equals("")) {
                jObj.put("OP_NTC_DATE", "");
            } else {
                jObj.put("OP_NTC_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_NTC_DATE").toString()));
            }
            jObj.put("OP_NTC_MODE", jObj.get("OP_NTC_MODE"));
            jObj.put("OP_NTC_PSN_TYPE", jObj.get("OP_NTC_PSN_TYPE"));
            jObj.put("OP_PU_REMARK", jObj.get("OP_PU_REMARK"));
            jObj.put("OP_PUOJ_ATTR_CD", jObj.get("OP_PUOJ_ATTR_CD"));
            jObj.put("OP_PUOJ_ATTR_NM", jObj.get("OP_PUOJ_ATTR_NM"));
            if (jObj.get("OP_PU_DATE").equals("")) {
                jObj.put("OP_PU_DATE", "");
            } else {
                jObj.put("OP_PU_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_PU_DATE").toString()));
            }
            jObj.put("OP_PU_TIME", jObj.get("OP_PU_TIME").toString().replace(":", ""));
            jObj.put("OP_PU_CITY_CD", jObj.get("OP_PU_CITY_CD"));
            jObj.put("OP_PU_CITY_NM", jObj.get("OP_PU_CITY_NM"));
            jObj.put("OP_PU_TOWN_CD", jObj.get("OP_PU_TOWN_CD"));
            jObj.put("OP_PU_TOWN_NM", jObj.get("OP_PU_TOWN_NM"));
            jObj.put("OP_PU_PLACE", jObj.get("OP_PU_PLACE"));
            jObj.put("OP_PU_YN_OV500", jObj.get("OP_PU_YN_OV500"));
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

            DaoUtil util = new DaoUtil();
            jObj = util.getStaticColumn(jObj, "ADD");
            jObj = util.getStaticColumn(jObj, "UPD");

            String arryString[] = {//"OP_SEQ_NO",
                "OP_CURSTAT_CD",
                "OP_CURSTAT_NM",
                "OP_DEL_FLAG",
                "OP_DEL_UNIT_CD", //5
                "OP_DEL_UNIT_NM",
                "OP_DEL_STAFF_ID",
                "OP_DEL_STAFF_NM",
                "OP_DEL_DATE",
                "OP_DEL_TIME", //10
                "OP_DEL_RSN",
                "OP_AC_RCNO",
                "OP_MANUAL_RCNO",
                "OP_AC_DATE",
                "OP_FM_DATE", //15
                "OP_AC_D_UNIT_CD",
                "OP_AC_D_UNIT_NM",
                "OP_AC_D_UNIT_CD_OLD",
                "OP_AC_B_UNIT_CD",
                "OP_AC_B_UNIT_NM", //20
                "OP_AC_B_UNIT_CD_OLD",
                "OP_AC_UNIT_CD",
                "OP_AC_UNIT_NM",
                "OP_AC_UNIT_CD_OLD",
                "OP_AC_STAFF_ID", //25
                "OP_AC_STAFF_NM",
                "OP_AC_UNIT_TEL",
                "OP_PUPO_TP_CD",
                "OP_PUPO_TP_NM",
                "OP_IS_CUST", //30
                "OP_IS_PUT_NM",
                "OP_INCLUDE_CERT",
                "OP_PUPO_NAME",
                "OP_PUPO_RNAME",
                "OP_PUPO_IDN_TP", //35
                "OP_PUPO_IDN",
                "OP_PUPO_GENDER",
                "OP_PUPO_BEFROC",
                "OP_PUPO_BIRTHDT",
                "OP_PUPO_NAT_CD", //40
                "OP_PUPO_NAT_NM",
                "OP_PUPO_ZIPCODE",
                "OP_OC_ADDR_TYPE_CD",
                "OP_OC_ADDR_TYPE_NM",
                "OP_PUPO_CITY_CD", //45
                "OP_PUPO_CITY_NM",
                "OP_PUPO_TOWN_CD",
                "OP_PUPO_TOWN_NM",
                "OP_PUPO_VILLAGE_CD",
                "OP_PUPO_VILLAGE_NM", //50
                "OP_PUPO_LIN",
                "OP_PUPO_ROAD",
                "OP_PUPO_ADDR_OTH",
                "OP_PUPO_PHONENO",
                "OP_PUPO_EMAIL", //55
                "OP_NTC_FIND_PO",
                "OP_YN_NTC",
                "OP_NTC_DATE",
                "OP_NTC_MODE",
                "OP_NTC_PSN_TYPE", //60
                "OP_PU_REMARK",
                "OP_PUOJ_ATTR_CD",
                "OP_PUOJ_ATTR_NM",
                "OP_PU_DATE",
                "OP_PU_TIME", //65
                "OP_PU_CITY_CD",
                "OP_PU_CITY_NM",
                "OP_PU_TOWN_CD",
                "OP_PU_TOWN_NM",
                "OP_PU_PLACE", //70
                "OP_PU_YN_OV500",
                "OP_ADD_ID",
                "OP_ADD_NM",
                "OP_ADD_UNIT_CD",
                "OP_ADD_UNIT_NM", //75
                "OP_ADD_DT_TM",
                "OP_UPD_ID",
                "OP_UPD_NM",
                "OP_UPD_UNIT_CD",
                "OP_UPD_UNIT_NM", //80
                "OP_UPD_DT_TM"}; //81

            sql.append("INSERT INTO OPDT_I_PU_BASIC ( ");
            sql.append(Arrays.toString(arryString).substring(1, Arrays.toString(arryString).length() - 1));
            sql.append(" )")
                    .append(" VALUES  (")
                    .append(" ?,?,?,?,?,?,?,?,?,?,") //10
                    .append(" ?,?,?,?,?,?,?,?,?,?,") //20
                    .append(" ?,?,?,?,?,?,?,?,?,?,") //30
                    .append(" ?,?,?,?,?,?,?,?,?,?,") //40
                    .append(" ?,?,?,?,?,?,?,?,?,?,") //50
                    .append(" ?,?,?,?,?,?,?,?,?,?,") //60
                    .append(" ?,?,?,?,?,?,?,?,?,?,") //70
                    .append(" ?,?,?,?,?,?,?,?,?,?)"); //80

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
                luence lue = new luence();
                jObj.put("OP_SEQ_NO", insetkey);
                if (jObj.get("OP_DEL_FLAG").equals("0")) {
                    lue.loadIndex("OP_PU_PLACE", Integer.toString(insetkey), jObj.get("OP_PU_PLACE").toString());
                }
            } else {
                returnValue = false;
            }

        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
             returnValue = false;  //20241001
        }
        return returnValue;
    }
    // endregion 新增 model END

    // region 更新 Model Start
    /**
     * 更新遺失物受理資料.
     *
     * @param jObj 審核資料欄位
     * @return boolean.
     */
    public boolean update(JSONObject jObj) {

        boolean returnValue = false;
        StringBuilder sql = new StringBuilder();
        Date current = new Date();

        try {
            User voUser = new User();
            voUser = (User) jObj.get("userVO");

            jObj.put("OP_CURSTAT_CD", jObj.get("OP_CURSTAT_CD"));
            jObj.put("OP_CURSTAT_NM", jObj.get("OP_CURSTAT_NM"));

            jObj.put("OP_DEL_FLAG", "0");
            jObj.put("OP_MANUAL_RCNO", jObj.get("OP_MANUAL_RCNO"));
            if (jObj.get("OP_AC_DATE").equals("")) {
                jObj.put("OP_AC_DATE", "");
            } else {
                jObj.put("OP_AC_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_AC_DATE").toString()));
            }
            if (jObj.get("OP_FM_DATE").equals("")) {
                jObj.put("OP_FM_DATE", "");
            } else {
                jObj.put("OP_FM_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_FM_DATE").toString()));
            }
            jObj.put("OP_AC_UNIT_TEL", jObj.get("OP_AC_UNIT_TEL"));
            jObj.put("OP_PUPO_TP_CD", jObj.get("OP_PUPO_TP_CD"));
            jObj.put("OP_PUPO_TP_NM", jObj.get("OP_PUPO_TP_NM"));
            jObj.put("OP_IS_CUST", jObj.get("OP_IS_CUST"));
            jObj.put("OP_IS_PUT_NM", jObj.get("OP_IS_PUT_NM"));
            jObj.put("OP_INCLUDE_CERT", jObj.get("OP_INCLUDE_CERT"));
            jObj.put("OP_PUPO_NAME", jObj.get("OP_PUPO_NAME"));
            if (jObj.has("OP_PUPO_RNAME")) {
                jObj.put("OP_PUPO_RNAME", jObj.get("OP_PUPO_RNAME"));
            } else {
                jObj.put("OP_PUPO_RNAME", "");
            }
            jObj.put("OP_PUPO_IDN_TP", jObj.get("OP_PUPO_IDN_TP"));
            jObj.put("OP_PUPO_IDN", jObj.get("OP_PUPO_IDN"));
            jObj.put("OP_PUPO_GENDER", jObj.get("OP_PUPO_GENDER"));
            jObj.put("OP_PUPO_BEFROC", jObj.get("OP_PUPO_BEFROC"));
            if (jObj.get("OP_PUPO_BIRTHDT").equals("")) {
                jObj.put("OP_PUPO_BIRTHDT", "");
            } else {
                if (jObj.get("OP_PUPO_BEFROC").equals("2")) { //西元
                    jObj.put("OP_PUPO_BIRTHDT", jObj.get("OP_PUPO_BIRTHDT"));
                } else { //民前 民國
                    jObj.put("OP_PUPO_BIRTHDT", DateUtil.toNormalDateTime(jObj.get("OP_PUPO_BIRTHDT").toString(), jObj.get("OP_PUPO_BEFROC").toString()));
                }
            }
            jObj.put("OP_PUPO_NAT_CD", jObj.get("OP_PUPO_NAT_CD"));
            jObj.put("OP_PUPO_NAT_NM", jObj.get("OP_PUPO_NAT_NM"));
            jObj.put("OP_PUPO_ZIPCODE", jObj.get("OP_PUPO_ZIPCODE"));
            jObj.put("OP_OC_ADDR_TYPE_CD", jObj.get("OP_OC_ADDR_TYPE_CD"));
            if (jObj.get("OP_OC_ADDR_TYPE_CD").equals("1")) { //一般地址
                jObj.put("OP_OC_ADDR_TYPE_NM", "一般地址");
                jObj.put("OP_PUPO_CITY_CD", jObj.get("OP_PUPO_CITY_CD"));
                jObj.put("OP_PUPO_CITY_NM", jObj.get("OP_PUPO_CITY_NM"));
                jObj.put("OP_PUPO_TOWN_CD", jObj.get("OP_PUPO_TOWN_CD"));
                jObj.put("OP_PUPO_TOWN_NM", jObj.get("OP_PUPO_TOWN_NM"));
                jObj.put("OP_PUPO_VILLAGE_CD", jObj.get("OP_PUPO_VILLAGE_CD"));
                jObj.put("OP_PUPO_VILLAGE_NM", jObj.get("OP_PUPO_VILLAGE_NM"));
                jObj.put("OP_PUPO_LIN", jObj.get("OP_PUPO_LIN"));
                jObj.put("OP_PUPO_ROAD", jObj.get("OP_PUPO_ROAD"));
                jObj.put("OP_PUPO_ADDR_OTH", "");
            } else { //自由輸入
                jObj.put("OP_OC_ADDR_TYPE_NM", "其他");
                jObj.put("OP_PUPO_CITY_CD", "");
                jObj.put("OP_PUPO_CITY_NM", "");
                jObj.put("OP_PUPO_TOWN_CD", "");
                jObj.put("OP_PUPO_TOWN_NM", "");
                jObj.put("OP_PUPO_VILLAGE_CD", "");
                jObj.put("OP_PUPO_VILLAGE_NM", "");
                jObj.put("OP_PUPO_LIN", "");
                jObj.put("OP_PUPO_ROAD", "");
                jObj.put("OP_PUPO_ADDR_OTH", jObj.get("OP_PUPO_ADDR_OTH"));
            }
            jObj.put("OP_PUPO_PHONENO", jObj.get("OP_PUPO_PHONENO"));
            jObj.put("OP_PUPO_EMAIL", jObj.get("OP_PUPO_EMAIL"));
            jObj.put("OP_NTC_FIND_PO", jObj.get("OP_NTC_FIND_PO"));
            jObj.put("OP_YN_NTC", jObj.get("OP_YN_NTC"));
            if (jObj.get("OP_NTC_DATE").equals("")) {
                jObj.put("OP_NTC_DATE", "");
            } else {
                jObj.put("OP_NTC_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_NTC_DATE").toString()));
            }
            jObj.put("OP_NTC_MODE", jObj.get("OP_NTC_MODE"));
            jObj.put("OP_NTC_PSN_TYPE", jObj.get("OP_NTC_PSN_TYPE"));
            jObj.put("OP_PU_REMARK", jObj.get("OP_PU_REMARK"));
            jObj.put("OP_PUOJ_ATTR_CD", jObj.get("OP_PUOJ_ATTR_CD"));
            jObj.put("OP_PUOJ_ATTR_NM", jObj.get("OP_PUOJ_ATTR_NM"));
            if (jObj.get("OP_PU_DATE").equals("")) {
                jObj.put("OP_PU_DATE", "");
            } else {
                jObj.put("OP_PU_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_PU_DATE").toString()));
            }
            jObj.put("OP_PU_TIME", jObj.get("OP_PU_TIME").toString().replace(":", ""));
            jObj.put("OP_PU_CITY_CD", jObj.get("OP_PU_CITY_CD"));
            jObj.put("OP_PU_CITY_NM", jObj.get("OP_PU_CITY_NM"));
            jObj.put("OP_PU_TOWN_CD", jObj.get("OP_PU_TOWN_CD"));
            jObj.put("OP_PU_TOWN_NM", jObj.get("OP_PU_TOWN_NM"));
            jObj.put("OP_PU_PLACE", jObj.get("OP_PU_PLACE"));
            jObj.put("OP_PU_YN_OV500", jObj.get("OP_PU_YN_OV500"));
            jObj.put("OP_UPD_ID", voUser.getUserId());
            jObj.put("OP_UPD_NM", voUser.getUserName());
            jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd());
            jObj.put("OP_UPD_UNIT_NM", voUser.getUnitName());
            jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());

            DaoUtil util = new DaoUtil();
            jObj = util.getStaticColumn(jObj, "UPD");

            String arryString[] = {//"OP_SEQ_NO",
                "OP_CURSTAT_CD",
                "OP_CURSTAT_NM",
                "OP_MANUAL_RCNO",
                "OP_AC_DATE", //5
                "OP_AC_UNIT_TEL",
                "OP_PUPO_TP_CD",
                "OP_PUPO_TP_NM",
                "OP_IS_CUST",
                "OP_IS_PUT_NM", //10
                "OP_INCLUDE_CERT",
                "OP_PUPO_NAME",
                "OP_PUPO_RNAME", //2019/8/14
                "OP_PUPO_IDN_TP",
                "OP_PUPO_IDN", //15
                "OP_PUPO_GENDER",
                "OP_PUPO_BEFROC",
                "OP_PUPO_BIRTHDT",
                "OP_PUPO_NAT_CD",
                "OP_PUPO_NAT_NM", //20
                "OP_PUPO_ZIPCODE",
                "OP_OC_ADDR_TYPE_CD",
                "OP_OC_ADDR_TYPE_NM",
                "OP_PUPO_CITY_CD",
                "OP_PUPO_CITY_NM", //25
                "OP_PUPO_TOWN_CD",
                "OP_PUPO_TOWN_NM",
                "OP_PUPO_VILLAGE_CD",
                "OP_PUPO_VILLAGE_NM",
                "OP_PUPO_LIN", //30
                "OP_PUPO_ROAD",
                "OP_PUPO_ADDR_OTH",
                "OP_PUPO_PHONENO",
                "OP_PUPO_EMAIL",
                "OP_NTC_FIND_PO", //35
                "OP_YN_NTC",
                "OP_NTC_DATE",
                "OP_NTC_MODE",
                "OP_NTC_PSN_TYPE",
                "OP_PU_REMARK", //40
                "OP_PUOJ_ATTR_CD",
                "OP_PUOJ_ATTR_NM",
                "OP_PU_DATE",
                "OP_PU_TIME",
                "OP_PU_CITY_CD", //45
                "OP_PU_CITY_NM",
                "OP_PU_TOWN_CD",
                "OP_PU_TOWN_NM",
                "OP_PU_PLACE",
                "OP_PU_YN_OV500", //50
                "OP_UPD_ID",
                "OP_UPD_NM",
                "OP_UPD_UNIT_CD",
                "OP_UPD_UNIT_NM",
                "OP_UPD_DT_TM"}; //55

            sql.append("UPDATE OPDT_I_PU_BASIC SET ");

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

            jObj.put("OP_SEQ_NO", jObj.get("OP_SEQ_NO"));
            jObj.put("OP_AC_RCNO", jObj.get("OP_AC_RCNO"));
            jObj.put("OP_PU_YN_OV500", jObj.get("OP_PU_YN_OV500"));
            luence lue = new luence();

            if (jObj.get("OP_DEL_FLAG").equals("0")) {
                lue.loadIndex("OP_PU_PLACE", Integer.toString(jObj.getInt("OP_SEQ_NO")), jObj.get("OP_PU_PLACE").toString());
            }

        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return returnValue;

    }

    /**
     * 查詢該受理基本資料目前狀態.
     *
     * @param jObj
     * @return CachedRowSet
     */
    public CachedRowSet checkStatusForBasic(JSONObject jObj) {
        ArrayList args = new ArrayList();
        List listkey = null;
        String sql = " SELECT *"
                + " FROM OPDT_I_PU_BASIC"
                + " WHERE 1 = 1 ";

        //遺失物事件基本資料序號
        if (!jObj.get("OP_BASIC_SEQ_NO").equals("")) {
            sql += " AND OP_SEQ_NO = ?";
            args.add(Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO")));
        }

        CachedRowSet crs = this.pexecuteQueryRowSet(sql, args.toArray());

        return crs;
    }

    /**
     * 查詢此筆狀態對不對
     *
     * @param jObj
     * @return CachedRowSet
     */
    public String checkStatusYNTrue(JSONObject jObj) throws SQLException, IOException, WriterException {
        String msg = "";
        String sql = "";
        ArrayList qsPara = new ArrayList();
        ArrayList<HashMap> AnnounceList = null;
        ArrayList<HashMap> AnDlList = null;
        //結案資料維護 OP300004 可以修改狀態，先刪除結案資料再更變狀態
        JSONArray OldBasic = queryBasicByIdOriginal(jObj.getInt("OP_BASIC_SEQ_NO"));
        String oldBasicCurstatCd = OldBasic.getJSONObject(0).get("OP_CURSTAT_CD").toString();
        qsPara.add(Integer.parseInt(jObj.getString("OP_BASIC_SEQ_NO")));

        if (!jObj.get("OP_CURSTAT_CD").equals(oldBasicCurstatCd)) { //狀態已經改變
            if (jObj.get("OP_CURSTAT_CD").equals("1")) { //處理中
                msg = "";
            } else if (jObj.get("OP_CURSTAT_CD").equals("2")) { //公告中
                sql = " SELECT *"
                        + " FROM OPDT_I_ANNOUNCE "
                        + " WHERE OP_BASIC_SEQ_NO = ? ";
                AnnounceList = this.pexecuteQuery(sql, qsPara.toArray());
                if (AnnounceList.isEmpty()) { //公告沒有資料
                    if (jObj.get("ACTION").equals("OP08A03Q_01.jsp")) {
                        msg = "此為五百元以下之案件，只有處理中及結案兩種狀態。";
                    } else {
                        msg = "此筆收據編號並無公告中資料，應更改為處理中。";
                    }
                } else {
                    msg = "";
                }
            } else if (jObj.get("OP_CURSTAT_CD").equals("3")) { //公告期滿
                sql = " SELECT *"
                        + " FROM OPDT_I_ANNOUNCE "
                        + " WHERE OP_BASIC_SEQ_NO = ? ";
                AnnounceList = this.pexecuteQuery(sql, qsPara.toArray());
                if (AnnounceList.isEmpty()) { //公告沒有資料
                    if (jObj.get("ACTION").equals("OP08A03Q_01.jsp")) {
                        msg = "此為五百元以下之案件，只有處理中及結案兩種狀態。";
                    } else {
                        msg = "此筆收據編號並無公告中資料，應更改為處理中。";
                    }
                } else {
                    String OP_AN_DATE_END = AnnounceList.get(0).get("OP_AN_DATE_END").toString();
                    if (OP_AN_DATE_END.equals("")) {
                        msg = "此筆收據編號有公告中資料，但無招領期滿日期，應更改為公告中。";
                    } else {
                        if (CompareToMoreNowDate(OP_AN_DATE_END)) {
                            msg = "此筆收據編號有公告中資料，但招領期滿日期大於今天，應更改為公告中。";
                        } else {
                            msg = "";
                        }
                    }
                }
            } else if (jObj.get("OP_CURSTAT_CD").equals("4")) { //領回公告中
                sql = " SELECT *"
                        + " FROM OPDT_I_AN_DL "
                        + " WHERE OP_BASIC_SEQ_NO = ? ";
                AnDlList = this.pexecuteQuery(sql, qsPara.toArray());
                if (AnDlList.isEmpty()) { //公告期滿沒有資料
                    if (jObj.get("ACTION").equals("OP08A03Q_01.jsp")) {
                        msg = "此為五百元以下之案件，只有處理中及結案兩種狀態。";
                    } else {
                        msg = "此筆收據編號並無招領期滿處理資料，應更改為公告期滿、公告中或處理中。";
                    }
                } else {
                    String OP_PUPO_ANDTEND = AnDlList.get(0).get("OP_PUPO_ANDTEND").toString();
                    if (OP_PUPO_ANDTEND.equals("")) {
                        msg = "此筆收據編號有招領期滿處理資料，但無拾得人領回公告期滿日期，應更改為公告期滿。";
                    } else {
                        if (CompareToMoreNowDate(OP_PUPO_ANDTEND)) {
                            msg = "此筆收據編號領回公告期滿日期大於今天，應更改為領回公告期滿。";
                        } else {
                            msg = "";
                        }
                    }
                }
            } else if (jObj.get("OP_CURSTAT_CD").equals("5")) { //領回公告期滿
                sql = " SELECT *"
                        + " FROM OPDT_I_AN_DL "
                        + " WHERE OP_BASIC_SEQ_NO = ? ";
                AnDlList = this.pexecuteQuery(sql, qsPara.toArray());
                if (AnDlList.isEmpty()) { //領回公告期滿沒有資料
                    if (jObj.get("ACTION").equals("OP08A03Q_01.jsp")) {
                        msg = "此為五百元以下之案件，只有處理中及結案兩種狀態。";
                    } else {
                        msg = "此筆收據編號並無領回公告期滿處理資料，應更改為領回公告中、公告期滿、公告中或處理中。";
                    }
                } else {
                    String OP_PUPO_ANDTEND = AnDlList.get(0).get("OP_PUPO_ANDTEND").toString();
                    if (OP_PUPO_ANDTEND.equals("")) {
                        msg = "此筆收據編號有招領期滿處理資料，但無拾得人領回公告期滿日期，應更改為公告期滿。";
                    } else {
                        if (CompareToMoreNowDate(OP_PUPO_ANDTEND)) {
                            msg = "";
                        } else {
                            msg = "此筆收據編號領回公告期滿日期小於今天，應更改為領回公告中。";
                        }
                    }
                }
            } else if (jObj.get("OP_CURSTAT_CD").equals("6")) { //結案
                msg = "";
            }

            if (msg.equals("") && oldBasicCurstatCd.equals("6")) {
                OPDT_I_FNSH iFnshDao = new OPDT_I_FNSH();
                iFnshDao.delete(jObj);
            }
        }
        System.out.println(msg);
        return msg;
    }

    /**
     * 查詢該受理基本資料是否重複開案.
     *
     * @param jObj
     * @return CachedRowSet
     */
    public CachedRowSet checkSameBasic(JSONObject jObj) {
        ArrayList args = new ArrayList();
        List listkey = null;
        String sql = " SELECT *"
                + " FROM OPDT_I_PU_BASIC"
                + " WHERE 1 = 1 ";

        //拾得人姓名
        if (!jObj.get("OP_PUPO_NAME").equals("")) {
            sql += " AND OP_PUPO_NAME = ?";
            args.add(jObj.get("OP_PUPO_NAME"));
        }

        //拾得人身分證
        if (!jObj.get("OP_PUPO_IDN").equals("")) {
            sql += " AND OP_PUPO_IDN = ?";
            args.add(jObj.get("OP_PUPO_IDN"));
        }

        //拾得日期
        if (!jObj.get("OP_PU_DATE").equals("")) {
            sql += " AND OP_PU_DATE = ?";
            args.add(DateUtil.get8UsDateFormatDB(jObj.get("OP_PU_DATE").toString()));
        }

        //拾得時間
        if (!jObj.get("OP_PU_TIME").equals("")) {
            sql += " AND OP_PU_TIME = ?";
            args.add(jObj.get("OP_PU_TIME").toString().replace(":", ""));
        }

        CachedRowSet crs = this.pexecuteQueryRowSet(sql, args.toArray());

        return crs;
    }

    // region 更新 Model Start
    /**
     * 更新遺失物受理資料狀態.
     *
     * @param jObj 審核資料欄位
     * @return boolean.
     */
    public boolean updateBasicForStatus(JSONObject jObj) {

        boolean returnValue = false;
        StringBuilder sql = new StringBuilder();
        Date current = new Date();

        try {
            User voUser = new User();
            voUser = (User) jObj.get("userVO");

            jObj.put("OP_CURSTAT_CD", jObj.get("OP_CURSTAT_CD"));
            jObj.put("OP_CURSTAT_NM", jObj.get("OP_CURSTAT_NM"));

            jObj.put("OP_UPD_ID", voUser.getUserId());
            jObj.put("OP_UPD_NM", voUser.getUserName());
            jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd());
            jObj.put("OP_UPD_UNIT_NM", voUser.getUnitName());
            jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());

            DaoUtil util = new DaoUtil();
            jObj = util.getStaticColumn(jObj, "UPD");

            String arryString[] = {//"OP_SEQ_NO",
                "OP_CURSTAT_CD",
                "OP_CURSTAT_NM",
                "OP_UPD_ID",
                "OP_UPD_NM", //5
                "OP_UPD_UNIT_CD",
                "OP_UPD_UNIT_NM",
                "OP_UPD_DT_TM"}; //8

            sql.append("UPDATE OPDT_I_PU_BASIC SET ");

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
            paraObject[arryString.length] = jObj.getInt("OP_BASIC_SEQ_NO");

            returnValue = this.pexecuteUpdate(sql.toString(), paraObject) > 0 ? true : false;

            jObj.put("OP_BASIC_SEQ_NO", jObj.get("OP_BASIC_SEQ_NO"));

        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return returnValue;

    }

    // region 刪除 Model Start
    /**
     * 更新 刪除註記 與 刪除原因
     *
     * @param jObj 審核資料欄位
     * @return boolean.
     */
    public int delete(JSONObject jObj) {

        int returnValue = 0;
        StringBuilder sql = new StringBuilder();
        Date current = new Date();

        try {
            User voUser = new User();
            voUser = (User) jObj.get("userVO");

            jObj.put("OP_SEQ_NO", jObj.get("OP_SEQ_NO"));
            String[] tmpSEQ_UPL = jObj.get("OP_SEQ_NO").toString().split(",");
            jObj.put("OP_DEL_RSN", jObj.get("OP_DEL_RSN"));
            jObj.put("OP_DEL_FLAG", "1");

            jObj.put("OP_DEL_STAFF_ID", voUser.getUserId());
            jObj.put("OP_DEL_STAFF_NM", voUser.getUserName());
            jObj.put("OP_DEL_UNIT_CD", voUser.getUnitCd());
            jObj.put("OP_DEL_UNIT_NM", voUser.getUnitName());
            jObj.put("OP_DEL_DATE", new SimpleDateFormat("yyyyMMdd").format(current).toString());
            jObj.put("OP_DEL_TIME", new SimpleDateFormat("HHmm").format(current).toString());

            for (int i = 0; i < tmpSEQ_UPL.length; i++) {
                //更新
                sql = new StringBuilder();
                String arryString[] = {//"OP_SEQ_NO",
                    "OP_DEL_RSN",
                    "OP_DEL_FLAG",
                    "OP_DEL_STAFF_ID",
                    "OP_DEL_STAFF_NM", //5
                    "OP_DEL_UNIT_CD",
                    "OP_DEL_UNIT_NM",
                    "OP_DEL_DATE",
                    "OP_DEL_TIME"}; //9

                sql.append("UPDATE OPDT_I_PU_BASIC SET ");

                for (int index = 0; index < arryString.length; index++) {
                    sql.append(arryString[index] + "=?, ");
                }
                sql.replace(sql.lastIndexOf(","), sql.lastIndexOf(",") + 1, "");
                sql.append("WHERE OP_SEQ_NO =? ");

                Object[] paraObject = new Object[arryString.length + 1];

                // paramenter
                int j = 0;
                for (String strKey : arryString) {
                    paraObject[j] = new Object[j];
                    if (jObj.has(strKey)) {
                        paraObject[j] = jObj.get(strKey);
                    }
                    j++;
                }
                paraObject[arryString.length] = Integer.valueOf(tmpSEQ_UPL[i]);

                returnValue = returnValue + this.pexecuteUpdate(sql.toString(), paraObject);
            }

//                        args.add(Integer.valueOf(SEQ[1]));
//                        returnValue = returnValue+ this.pexecuteUpdate(sql.toString(), args.toArray());		
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return returnValue;

    }
    // endregion 刪除 Model END

    /**
     * 更新遺失物受理伍佰元上下資料.
     *
     * @param jObj 審核資料欄位
     * @return boolean.
     */
    public boolean updateBasicYnOv500(JSONObject jObj) {

        boolean returnValue = false;
        StringBuilder sql = new StringBuilder();
        Date current = new Date();

        try {
            User voUser = new User();
            voUser = (User) jObj.get("userVO");

            jObj.put("OP_PU_YN_OV500", jObj.get("OP_PU_YN_OV500"));
            jObj.put("OP_UPD_ID", voUser.getUserId());
            jObj.put("OP_UPD_NM", voUser.getUserName());
            jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd());
            jObj.put("OP_UPD_UNIT_NM", voUser.getUnitName());
            jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());

            DaoUtil util = new DaoUtil();
            jObj = util.getStaticColumn(jObj, "UPD");

            String arryString[] = {//"OP_SEQ_NO",
                "OP_PU_YN_OV500",
                "OP_UPD_ID",
                "OP_UPD_NM",
                "OP_UPD_UNIT_CD", //5
                "OP_UPD_UNIT_NM",
                "OP_UPD_DT_TM"}; //7

            sql.append("UPDATE OPDT_I_PU_BASIC SET ");

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

            jObj.put("OP_SEQ_NO", jObj.get("OP_SEQ_NO"));
            jObj.put("OP_PU_YN_OV500", jObj.get("OP_PU_YN_OV500"));

        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return returnValue;

    }
    // endregion 更新 Model END

    /**
     * 未結案件,未公告案件,未通知領回案件 數量查詢.
     *
     * @param jObj 審核資料欄位
     * @return boolean.
     */
    public JSONArray countBasicCase(JSONObject jObj) throws SQLException, IOException, WriterException {
        ArrayList args = new ArrayList();

//                String sql = " SELECT DISTINCT COUNT(*) as CASECOUNT"                          
//                           + " FROM "
//                           + " OPDT_I_PU_BASIC A";
//                
//                sql += " WHERE 1 = 1 ";
//                sql += " AND A.OP_DEL_FLAG = '0' ";
//                sql += " AND A.OP_PU_YN_OV500 = '1' ";
//                    
//                //受理單位
//                if( jObj.has("OP_PERMISSION") && jObj.get("OP_PERMISSION").equals("3") ){ //警局
//                    if(!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")){
//                        sql += " AND A.OP_AC_D_UNIT_CD = ?";
//                        args.add( jObj.get("OP_UNITLEVEL2"));
//                    }
//                }else if ( jObj.has("OP_PERMISSION") && jObj.get("OP_PERMISSION").equals("2") ){ //分局
//                    if(!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")){
//                        sql += " AND A.OP_AC_B_UNIT_CD = ?";
//                        args.add( jObj.get("OP_UNITLEVEL3"));
//                    } else if(!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")){
//                        sql += " AND A.OP_AC_D_UNIT_CD = ?";
//                        args.add( jObj.get("OP_UNITLEVEL2"));
//                    }
//                }else{
//                    if(!StringUtil.nvl(jObj.getString("OP_UNITLEVEL4")).equals("")){
//                        sql += " AND A.OP_AC_UNIT_CD = ?";
//                        args.add( jObj.get("OP_UNITLEVEL4"));
//                    } else if(!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")){
//                        sql += " AND A.OP_AC_B_UNIT_CD = ?";
//                        args.add( jObj.get("OP_UNITLEVEL3"));
//                    } else if(!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")){
//                        sql += " AND A.OP_AC_D_UNIT_CD = ?";
//                        args.add( jObj.get("OP_UNITLEVEL2"));
//                    }
//                }
//                
//                if ( jObj.get("TYPE").equals("NoFinishCase") ){ //未結案件
//                    sql += " AND A.OP_CURSTAT_CD <> '6'";
//                }else if ( jObj.get("TYPE").equals("NoAnnounceCase") ){ //未公告案件
//                    sql += " AND A.OP_CURSTAT_CD = '1'";
//                }else if ( jObj.get("TYPE").equals("AnDlCase") ){ //招領期滿案件
//                    sql += " AND A.OP_CURSTAT_CD = '3'";
//                }else if ( jObj.get("TYPE").equals("PuanDlCase") ){ //領回期滿案件
//                    sql += " AND A.OP_CURSTAT_CD = '5'";
//                }
        String sql = " SELECT SUM(NoFinishCase) as NoFinishCase, SUM(NoAnnounceCase) as NoAnnounceCase, SUM(AnDlCase) as AnDlCase, SUM(PuanDlCase) as PuanDlCase"
                + " FROM (";

        //第一項 未結案件
        sql += " SELECT 'OP_QUERY_NM' as OP_QUERY_NM, 1 as NoFinishCase, 0 as NoAnnounceCase, 0 as AnDlCase, 0 as PuanDlCase FROM OPDT_I_PU_BASIC basic";
        sql += " WHERE 1 = 1 ";
        sql += " AND basic.OP_DEL_FLAG = '0' ";
        sql += " AND basic.OP_PU_YN_OV500 = '1' ";
            //受理單位
        if (jObj.has("OP_PERMISSION") && jObj.get("OP_PERMISSION").equals("3")) { //警局
            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
                sql += " AND basic.OP_AC_D_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL2"));
            }
        } else if (jObj.has("OP_PERMISSION") && jObj.get("OP_PERMISSION").equals("2")) { //分局
            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")) {
                sql += " AND basic.OP_AC_B_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL3"));
            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
                sql += " AND basic.OP_AC_D_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL2"));
            }
        } else {
            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL4")).equals("")) {
                sql += " AND basic.OP_AC_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL4"));
            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")) {
                sql += " AND basic.OP_AC_B_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL3"));
            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
                sql += " AND basic.OP_AC_D_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL2"));
            }
        }
        sql += " AND basic.OP_CURSTAT_CD <> '6'";
        sql += " UNION ALL ";

        //第二項 未公告案件
        sql += " SELECT 'OP_QUERY_NM' as OP_QUERY_NM, 0 as NoFinishCase, 1 as NoAnnounceCase, 0 as AnDlCase, 0 as PuanDlCase FROM OPDT_I_PU_BASIC basic";
        sql += " WHERE 1 = 1 ";
        sql += " AND basic.OP_DEL_FLAG = '0' ";
        sql += " AND basic.OP_PU_YN_OV500 = '1' ";
            //受理單位
        if (jObj.has("OP_PERMISSION") && jObj.get("OP_PERMISSION").equals("3")) { //警局
            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
                sql += " AND basic.OP_AC_D_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL2"));
            }
        } else if (jObj.has("OP_PERMISSION") && jObj.get("OP_PERMISSION").equals("2")) { //分局
            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")) {
                sql += " AND basic.OP_AC_B_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL3"));
            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
                sql += " AND basic.OP_AC_D_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL2"));
            }
        } else {
            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL4")).equals("")) {
                sql += " AND basic.OP_AC_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL4"));
            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")) {
                sql += " AND basic.OP_AC_B_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL3"));
            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
                sql += " AND basic.OP_AC_D_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL2"));
            }
        }
        sql += " AND basic.OP_CURSTAT_CD = '1'";
        sql += " UNION ALL ";

        //第三項 招領期滿案件
        sql += " SELECT 'OP_QUERY_NM' as OP_QUERY_NM, 0 as NoFinishCase, 0 as NoAnnounceCase, 1 as AnDlCase, 0 as PuanDlCase FROM OPDT_I_PU_BASIC basic";
        sql += " WHERE 1 = 1 ";
        sql += " AND basic.OP_DEL_FLAG = '0' ";
        sql += " AND basic.OP_PU_YN_OV500 = '1' ";
            //受理單位
        if (jObj.has("OP_PERMISSION") && jObj.get("OP_PERMISSION").equals("3")) { //警局
            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
                sql += " AND basic.OP_AC_D_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL2"));
            }
        } else if (jObj.has("OP_PERMISSION") && jObj.get("OP_PERMISSION").equals("2")) { //分局
            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")) {
                sql += " AND basic.OP_AC_B_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL3"));
            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
                sql += " AND basic.OP_AC_D_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL2"));
            }
        } else {
            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL4")).equals("")) {
                sql += " AND basic.OP_AC_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL4"));
            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")) {
                sql += " AND basic.OP_AC_B_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL3"));
            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
                sql += " AND basic.OP_AC_D_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL2"));
            }
        }
        sql += " AND basic.OP_CURSTAT_CD = '3'";
        sql += " UNION ALL ";

        //第四項 領回期滿案件
        sql += " SELECT 'OP_QUERY_NM' as OP_QUERY_NM, 0 as NoFinishCase, 0 as NoAnnounceCase, 0 as AnDlCase, 1 as PuanDlCase FROM OPDT_I_PU_BASIC basic";
        sql += " WHERE 1 = 1 ";
        sql += " AND basic.OP_DEL_FLAG = '0' ";
        sql += " AND basic.OP_PU_YN_OV500 = '1' ";
            //受理單位
        if (jObj.has("OP_PERMISSION") && jObj.get("OP_PERMISSION").equals("3")) { //警局
            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
                sql += " AND basic.OP_AC_D_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL2"));
            }
        } else if (jObj.has("OP_PERMISSION") && jObj.get("OP_PERMISSION").equals("2")) { //分局
            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")) {
                sql += " AND basic.OP_AC_B_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL3"));
            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
                sql += " AND basic.OP_AC_D_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL2"));
            }
        } else {
            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL4")).equals("")) {
                sql += " AND basic.OP_AC_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL4"));
            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")) {
                sql += " AND basic.OP_AC_B_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL3"));
            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
                sql += " AND basic.OP_AC_D_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL2"));
            }
        }
        sql += " AND basic.OP_CURSTAT_CD = '5'";
        sql += ")basic GROUP BY basic.OP_QUERY_NM";

        ArrayList<HashMap> list = this.pexecuteQuery(sql, args.toArray());
        if (list.size() < 1) {
            HashMap hMap = new HashMap();
            hMap.put("NoFinishCase", "0");
            hMap.put("NoAnnounceCase", "0");
            hMap.put("AnDlCase", "0");
            hMap.put("PuanDlCase", "0");
            list.add(hMap);
        }
        
        return arrayList2JsonArray(list);
    }
    
//上面四個統計數字(未結案;未公告案件;招領期滿案件;領回期滿案件)的範例SQL
//SELECT 
//	SUM(NoFinishCase) as NoFinishCase, 
//	SUM(NoAnnounceCase) as NoAnnounceCase, 
//	SUM(AnDlCase) as AnDlCase, 
//	SUM(PuanDlCase) as PuanDlCase 
//	FROM 
//	( 
//	SELECT 'OP_QUERY_NM' as OP_QUERY_NM, 
//		1 as NoFinishCase, 
//		0 as NoAnnounceCase, 
//		0 as AnDlCase, 
//		0 as PuanDlCase 
//		FROM OPDT_I_PU_BASIC basic WHERE 1 = 1  
//		AND basic.OP_DEL_FLAG = '0'  
//		AND basic.OP_PU_YN_OV500 = '1'  
//		AND basic.OP_AC_UNIT_CD = 'AD6Q1' 
//		AND basic.OP_CURSTAT_CD <> '6'  //未結案
//	
//	UNION ALL 
//		
//	SELECT 'OP_QUERY_NM' as OP_QUERY_NM, 
//		0 as NoFinishCase, 
//		1 as NoAnnounceCase, 
//		0 as AnDlCase, 
//		0 as PuanDlCase 
//		FROM OPDT_I_PU_BASIC basic WHERE 1 = 1  
//		AND basic.OP_DEL_FLAG = '0'  
//		AND basic.OP_PU_YN_OV500 = '1'  
//		AND basic.OP_AC_UNIT_CD = 'AD6Q1' 
//		AND basic.OP_CURSTAT_CD = '1'   //處理中
//	
//	UNION ALL  
//	
//	SELECT 'OP_QUERY_NM' as OP_QUERY_NM, 
//		0 as NoFinishCase, 
//		0 as NoAnnounceCase, 
//		1 as AnDlCase, 
//		0 as PuanDlCase 
//		FROM OPDT_I_PU_BASIC basic WHERE 1 = 1  
//		AND basic.OP_DEL_FLAG = '0'  
//		AND basic.OP_PU_YN_OV500 = '1'  
//		AND basic.OP_AC_UNIT_CD = 'AD6Q1' 
//		AND basic.OP_CURSTAT_CD = '3'   //招領公告到期
//		
//	UNION ALL  
//	
//	SELECT 'OP_QUERY_NM' as OP_QUERY_NM, 
//		0 as NoFinishCase, 
//		0 as NoAnnounceCase, 
//		0 as AnDlCase, 
//		1 as PuanDlCase 
//		FROM OPDT_I_PU_BASIC basic 
//		WHERE 1 = 1  
//		AND basic.OP_DEL_FLAG = '0'  
//		AND basic.OP_PU_YN_OV500 = '1'  
//		AND basic.OP_AC_UNIT_CD = 'AD6Q1' 
//		AND basic.OP_CURSTAT_CD = '5'   //領回公告到期
//	)
//basic GROUP BY basic.OP_QUERY_NM    
    
    
    
    
    //202311 拾得需求新增  招領公告即將(7日後)期滿案件  與 領回公告即將(7日後)期滿案件
    public JSONArray countAnnounceCase(JSONObject jObj) throws SQLException, IOException, WriterException {
        //招領即將(7日後)期滿案件 (6個月+3個月的6個月期滿)
        
//        String sql = "SELECT COUNT(*) AS AnDateEndCaseAfter7 "
//                + "FROM OPDT_I_ANNOUNCE announce "
//                + "WHERE OP_AN_DATE_END <= ?";
        
        String sql    = " SELECT COUNT(*) AS AnDateEndCaseAfter7 " ;
               sql   += " FROM OPDT_I_ANNOUNCE announce ";
               sql   += " RIGHT JOIN  OPDT_I_PU_BASIC basic " ;
               sql   += " ON announce.OP_AC_RCNO=basic.OP_AC_RCNO  " ;
               sql   += " WHERE announce.OP_AN_DATE_END <= ? " ;
               sql   += " AND   announce.OP_AN_DATE_END >  ? " ;                
               sql   += " AND basic.OP_DEL_FLAG = '0'     " ; 
               sql   += " AND basic.OP_PU_YN_OV500 = '1'  " ; 
               sql   += " AND basic.OP_CURSTAT_CD = '3'   " ; //招領公告到期 
        
          
               
               
        ArrayList args = new ArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date sevenDaysLater = calendar.getTime();
        String after7 = sdf.format(sevenDaysLater);

        calendar.add(Calendar.DAY_OF_YEAR, 0);
        Date DayToday = calendar.getTime();
        String daytoday = sdf.format(DayToday);
        
        args.add(after7);    
        args.add(daytoday);  
        
        
        //受理單位
//        if (jObj.has("OP_PERMISSION") && jObj.get("OP_PERMISSION").equals("3")) { //警局
//            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
//                sql += " AND announce.OP_AN_D_UNIT_CD = ?";
//                args.add(jObj.get("OP_UNITLEVEL2"));
//            }
//        } else if (jObj.has("OP_PERMISSION") && jObj.get("OP_PERMISSION").equals("2")) { //分局
//            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")) {
//                sql += " AND announce.OP_AN_B_UNIT_CD = ?";
//                args.add(jObj.get("OP_UNITLEVEL3"));
//            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
//                sql += " AND announce.OP_AN_D_UNIT_CD = ?";
//                args.add(jObj.get("OP_UNITLEVEL2"));
//            }
//        } else {
//            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL4")).equals("")) {
//                sql += " AND announce.OP_AN_UNIT_CD = ?";
//                args.add(jObj.get("OP_UNITLEVEL4"));
//            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")) {
//                sql += " AND announce.OP_AN_B_UNIT_CD = ?";
//                args.add(jObj.get("OP_UNITLEVEL3"));
//            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
//                sql += " AND announce.OP_AN_D_UNIT_CD = ?";
//                args.add(jObj.get("OP_UNITLEVEL2"));
//            }
//        }
        //受理單位
        if (jObj.has("OP_PERMISSION") && jObj.get("OP_PERMISSION").equals("3")) { //警局
            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
                sql += " AND basic.OP_AC_D_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL2"));
            }
        } else if (jObj.has("OP_PERMISSION") && jObj.get("OP_PERMISSION").equals("2")) { //分局
            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")) {
                sql += " AND basic.OP_AC_B_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL3"));
            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
                sql += " AND basic.OP_AC_D_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL2"));
            }
        } else {
            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL4")).equals("")) {
                sql += " AND basic.OP_AC_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL4"));
            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")) {
                sql += " AND basic.OP_AC_B_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL3"));
            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
                sql += " AND basic.OP_AC_D_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL2"));
            }
        }
        
        ArrayList<HashMap> list = this.pexecuteQuery(sql, args.toArray());
        
        return arrayList2JsonArray(list);
    }
    //202311 拾得需求新增  招領公告即將(7日後)期滿案件  與 領回公告即將(7日後)期滿案件
    public JSONArray countAnDlCase(JSONObject jObj) throws SQLException, IOException, WriterException {
        //領回即將(7日後)期滿案件  (6個月+3個月的3個月期滿)
        String sql = " SELECT COUNT(*) AS AnDlCaseAfter7 " ;
            sql   += " FROM OPDT_I_AN_DL andl ";
            sql   += " RIGHT JOIN  OPDT_I_PU_BASIC basic " ;
            sql   += " ON andl.OP_AC_RCNO=basic.OP_AC_RCNO  " ;
            sql   += " WHERE andl.OP_PUPO_ANDTEND <= ? " ;
            sql   += " AND   andl.OP_PUPO_ANDTEND >  ? " ;
            sql   += " AND basic.OP_DEL_FLAG = '0'     " ; 
            sql   += " AND basic.OP_PU_YN_OV500 = '1'  " ; 
            sql   += " AND basic.OP_CURSTAT_CD = '5'   " ; //領回公告到期 
            
            
        ArrayList args = new ArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date sevenDaysLater = calendar.getTime();
        String after7 = sdf.format(sevenDaysLater);
        calendar.add(Calendar.DAY_OF_YEAR, 0);
        Date DayToday = calendar.getTime();
        String daytoday = sdf.format(DayToday);
        
        args.add(after7);    
        args.add(daytoday);  
        //受理單位
//        if (jObj.has("OP_PERMISSION") && jObj.get("OP_PERMISSION").equals("3")) { //警局
//            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
//                sql += " AND andl.OP_PR_UNIT_CD = ?";
//                args.add(jObj.get("OP_UNITLEVEL2"));
//            }
//        } else if (jObj.has("OP_PERMISSION") && jObj.get("OP_PERMISSION").equals("2")) { //分局
//            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")) {
//                sql += " AND andl.OP_PR_UNIT_CD = ?";
//                args.add(jObj.get("OP_UNITLEVEL3"));
//            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
//                sql += " AND andl.OP_PR_UNIT_CD = ?";
//                args.add(jObj.get("OP_UNITLEVEL2"));
//            }
//        } else {
//            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL4")).equals("")) {
//                sql += " AND andl.OP_PR_UNIT_CD = ?";
//                args.add(jObj.get("OP_UNITLEVEL4"));
//            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")) {
//                sql += " AND andl.OP_PR_UNIT_CD = ?";
//                args.add(jObj.get("OP_UNITLEVEL3"));
//            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
//                sql += " AND andl.OP_PR_UNIT_CD = ?";
//                args.add(jObj.get("OP_UNITLEVEL2"));
//            }
//        }
        
        //受理單位
        if (jObj.has("OP_PERMISSION") && jObj.get("OP_PERMISSION").equals("3")) { //警局
            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
                sql += " AND basic.OP_AC_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL2"));
            }
        } else if (jObj.has("OP_PERMISSION") && jObj.get("OP_PERMISSION").equals("2")) { //分局
            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")) {
                sql += " AND basic.OP_AC_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL3"));
            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
                sql += " AND basic.OP_AC_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL2"));
            }
        } else {
            if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL4")).equals("")) {
                sql += " AND basic.OP_AC_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL4"));
            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL3")).equals("")) {
                sql += " AND basic.OP_AC_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL3"));
            } else if (!StringUtil.nvl(jObj.getString("OP_UNITLEVEL2")).equals("")) {
                sql += " AND basic.OP_AC_UNIT_CD = ?";
                args.add(jObj.get("OP_UNITLEVEL2"));
            }
        }        
        
        ArrayList<HashMap> list = this.pexecuteQuery(sql, args.toArray());
        
        return arrayList2JsonArray(list);
    }
    
    public boolean CompareToMoreNowDate(String DBDate) {
        boolean bool;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        int DBDateInt = Integer.parseInt(DBDate);
        int nowDate = Integer.parseInt(sdf.format(date));

        if (DBDateInt > nowDate) {
            bool = true;
        } else {
            bool = false;
        }
        return bool;
    }

}
