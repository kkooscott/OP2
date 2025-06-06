/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npa.op.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
//import org.apache.lucene.util.Version;

//import com.sybase.jdbc4.jdbc.SybDataSource;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;



public class luence {

//	public static SybDataSource getDataSource() throws Exception {
//		SybDataSource dataSource =null;
//        if(dataSource == null){
//            ResourceBundle resource = ResourceBundle.getBundle("npa");
////            System.out.println("Start load ASE Database setting!");
//        	 //properties
//            dataSource = new com.sybase.jdbc4.jdbc.SybDataSource();
//            dataSource.setDatabaseName(resource.getString("ASE_DB_NM"));
//            dataSource.setPortNumber(Integer.parseInt(resource.getString("ASE_PORT")));
//            dataSource.setServerName(resource.getString("ASE_SERVER"));
//            dataSource.setUser(resource.getString("ASE_USER"));
//            dataSource.setPassword(resource.getString("ASE_PASSWORD"));
//            dataSource.setCHARSET("utf8");
//        }
//        return dataSource;
//    }
        public static int LuenceQueryNumber = NpaConfig.getInt("LuenceQueryNumber"); //搜尋數字
	
	public static List searchKeyWord(String key){
        List result = null;
        try{
            result = new ArrayList();
            
            ResourceBundle resourceBundle = ResourceBundle.getBundle("npa");
            String luceneFolder = resourceBundle.getString("Lucene_Folder");
            
            Analyzer a = new StandardAnalyzer();
            Path path = Paths.get(luceneFolder);
            Directory directory = FSDirectory.open(path);
            // Now search the index:
            DirectoryReader ireader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(ireader);
            QueryParser parser = new QueryParser("OP_PU_PLACE", a);
            
            Query query = parser.parse(key);
            TopDocs topDocs = searcher.search(query, 1000);
//            System.out.println("key:"+key+" match count：" + topDocs.totalHits);
            ScoreDoc[] hits = topDocs.scoreDocs;
//            // 应该与topDocs.totalHits相同
//            System.out.println("totle hits：" + hits.length);
            for (ScoreDoc scoreDoc : hits) {
                Document document = searcher.doc(scoreDoc.doc);
                result.add(document.get("OP_SEQ_NO"));
//                System.out.println(document.get("CP_SEQ_NO")+":"+document.get("CP_CASE_GIST")+" match point：" + scoreDoc.score+" index ID：" + scoreDoc.doc);
            }
            ireader.close();
            directory.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
        
        /**        
        * 
        * @param colName: 索引欄位名稱 colValue: 索引欄位值 returnColName: 回傳欄位名稱
        * @return
        */
        public static String searchKeyWord(String indexColName, String indexColValue, String returnColName){
            List<String> resultList = null;
            try{
                resultList = new ArrayList<String>();

                ResourceBundle resourceBundle = ResourceBundle.getBundle("npa");
                String luceneFolder = resourceBundle.getString("Lucene_Folder_"+ indexColName);

                Analyzer a = new StandardAnalyzer();
                Path path = Paths.get(luceneFolder);
                Directory directory = FSDirectory.open(path);
                // Now search the index:
                DirectoryReader ireader = DirectoryReader.open(directory);
                IndexSearcher searcher = new IndexSearcher(ireader);
                QueryParser parser = new QueryParser(indexColName, a);

                Query query = parser.parse(indexColValue);
                TopDocs topDocs = searcher.search(query, LuenceQueryNumber);
    //            System.out.println("key:"+key+" match count：" + topDocs.totalHits);
                ScoreDoc[] hits = topDocs.scoreDocs;
    //            System.out.println("totle hits：" + hits.length);
                for (ScoreDoc scoreDoc : hits) {
                    Document document = searcher.doc(scoreDoc.doc);
                    resultList.add(document.get(returnColName));
    //                System.out.println(document.get("CP_SEQ_NO")+":"+document.get("CP_CASE_GIST")+" match point：" + scoreDoc.score+" index ID：" + scoreDoc.doc);
                }
                ireader.close();
                directory.close();
            } catch(Exception e){
                e.printStackTrace();
            }
            String resultStr = resultList.toString();
            return resultStr.substring(1, resultStr.length()-1);
        }
        
        /**        
        * 
        * @param colName: 索引欄位名稱陣列 colValue: 索引欄位值陣列 returnColName: 回傳欄位名稱
        * @return
        */
        public static String searchKeyWord(String[] indexColName, String[] indexColValue, String returnColName){
            List<String> resultList = null;
            try{
                resultList = new ArrayList<String>();

                ResourceBundle resourceBundle = ResourceBundle.getBundle("npa");
                String luceneFolder = resourceBundle.getString("Lucene_Folder_"+ indexColName[0]);

                Analyzer a = new StandardAnalyzer();
                Path path = Paths.get(luceneFolder);
                Directory directory = FSDirectory.open(path);
                // Now search the index:
                DirectoryReader ireader = DirectoryReader.open(directory);
                IndexSearcher searcher = new IndexSearcher(ireader);
//                QueryParser parser = new QueryParser(indexColName, a);
//                Query query = parser.parse(indexColValue);
//                TopDocs topDocs = searcher.search(query, LuenceQueryNumber);
                //多欄位搜尋                
                Query query = null;
                BooleanQuery booleanQuery = new BooleanQuery();
                for(int i=0; i<indexColName.length; i++){
                    //Query query = new TermQuery(new Term(indexColName[i], indexColValue[i]));                    
                    booleanQuery.add(
                        new QueryParser(indexColName[i], a).parse(indexColValue[i]),
                        BooleanClause.Occur.MUST);
                }
                TopDocs topDocs = searcher.search(booleanQuery, LuenceQueryNumber);
//                Hits hits = searcher.search(query);                                                  

    //            System.out.println("key:"+key+" match count：" + topDocs.totalHits);
                ScoreDoc[] hits = topDocs.scoreDocs;
    //            System.out.println("totle hits：" + hits.length);
                for (ScoreDoc scoreDoc : hits) {
                    Document document = searcher.doc(scoreDoc.doc);
                    resultList.add(document.get(returnColName));
    //                System.out.println(document.get("CP_SEQ_NO")+":"+document.get("CP_CASE_GIST")+" match point：" + scoreDoc.score+" index ID：" + scoreDoc.doc);
                }
                ireader.close();
                directory.close();
            } catch(Exception e){
                e.printStackTrace();
            }
            String resultStr = resultList.toString();
            return resultStr.substring(1, resultStr.length()-1);
        }
        
	//For Basic
	public static void loadIndex(String indexColName, String OP_BASIC_SEQ_NO, String returnColName){
        try{
            
            ResourceBundle resourceBundle = ResourceBundle.getBundle("npa");
            String luceneFolder = resourceBundle.getString("Lucene_Folder_"+ indexColName);
            
            Analyzer a = new StandardAnalyzer();
            Path path = Paths.get(luceneFolder);
            Directory directory = FSDirectory.open(path);
            
            IndexWriterConfig config = new IndexWriterConfig(a);
            IndexWriter writer = new IndexWriter(directory, config);
            
            //不管怎樣先刪除
            
            QueryParser parser = new QueryParser("OP_BASIC_SEQ_NO", a);
            Query query = parser.parse(OP_BASIC_SEQ_NO);
            writer.deleteDocuments(query);
            writer.commit();
            
           
            
            Document doc = new Document();
            if( indexColName.equals("OP_PU_PLACE") ){ //拾得地點
                doc.add(new Field("OP_PU_PLACE", returnColName, Field.Store.YES, Field.Index.ANALYZED));
            }else if( indexColName.equals("OP_PUOJ_NM") ){ //物品名稱
                doc.add(new Field("OP_PUOJ_NM", returnColName, Field.Store.YES, Field.Index.ANALYZED));
            }
            doc.add(new Field("OP_BASIC_SEQ_NO", OP_BASIC_SEQ_NO, Field.Store.YES, Field.Index.NOT_ANALYZED));
            writer.addDocument(doc);
            writer.commit();
            
           
            writer.close();
            directory.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    //For Detail
    public static void loadIndexForDetail(String indexColName, String OP_AC_D_UNIT_CD, String OP_AC_B_UNIT_CD, String OP_AC_UNIT_CD, String OP_SEQ_NO, String OP_BASIC_SEQ_NO,String returnColName){
        try{
            
            ResourceBundle resourceBundle = ResourceBundle.getBundle("npa");
            String luceneFolder = resourceBundle.getString("Lucene_Folder_"+ indexColName);
            
            Analyzer a = new StandardAnalyzer();
            Path path = Paths.get(luceneFolder);
            Directory directory = FSDirectory.open(path);
            
            IndexWriterConfig config = new IndexWriterConfig(a);
            IndexWriter writer = new IndexWriter(directory, config);
            
            //不管怎樣先刪除
            
            QueryParser parser = new QueryParser("OP_SEQ_NO", a);
            Query query = parser.parse(OP_SEQ_NO);
            writer.deleteDocuments(query);
            writer.commit();
  
            Document doc = new Document();
            if( indexColName.equals("OP_PU_PLACE") ){ //拾得地點
                doc.add(new Field("OP_PU_PLACE",    returnColName,  Field.Store.YES,    Field.Index.ANALYZED));
            }else if( indexColName.equals("OP_PUOJ_NM") ){ //物品名稱
                doc.add(new Field("OP_PUOJ_NM",     returnColName,  Field.Store.YES,    Field.Index.ANALYZED));
            }
            doc.add(new Field("OP_AC_D_UNIT_CD",    OP_AC_D_UNIT_CD,    Field.Store.YES,    Field.Index.ANALYZED));
            doc.add(new Field("OP_AC_B_UNIT_CD",    OP_AC_B_UNIT_CD,    Field.Store.YES,    Field.Index.ANALYZED));
            doc.add(new Field("OP_AC_UNIT_CD",    OP_AC_UNIT_CD,    Field.Store.YES,    Field.Index.ANALYZED));
            doc.add(new Field("OP_SEQ_NO",          OP_SEQ_NO,          Field.Store.YES,    Field.Index.NOT_ANALYZED));
            doc.add(new Field("OP_BASIC_SEQ_NO",    OP_BASIC_SEQ_NO,    Field.Store.YES,    Field.Index.NOT_ANALYZED));
            writer.addDocument(doc);
            writer.commit();
            
           
            writer.close();
            directory.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //For Detail
    public static void deleteIndexForDetail(String indexColName, String OP_SEQ_NO){
        try{
            
            ResourceBundle resourceBundle = ResourceBundle.getBundle("npa");
            String luceneFolder = resourceBundle.getString("Lucene_Folder_"+ indexColName);
            
            Analyzer a = new StandardAnalyzer();
            Path path = Paths.get(luceneFolder);
            Directory directory = FSDirectory.open(path);
            
            IndexWriterConfig config = new IndexWriterConfig(a);
            IndexWriter writer = new IndexWriter(directory, config);
            
            //不管怎樣先刪除
            
            QueryParser parser = new QueryParser("OP_SEQ_NO", a);
            Query query = parser.parse(OP_SEQ_NO);
            writer.deleteDocuments(query);
            writer.commit();

            writer.close();
            directory.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
	
}
