package npa.op.util;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package npa.cp.util;
//
///**
// *
// * @author Administrator
// */
//import java.io.IOException;
// 
//import javax.sql.DataSource;
// 
//import org.apache.lucene.store.jdbc.JdbcDirectory;
//import org.apache.lucene.store.jdbc.JdbcDirectorySettings;
//import org.apache.lucene.store.jdbc.JdbcStoreException;
//import org.apache.lucene.store.jdbc.dialect.Dialect;
//import org.apache.lucene.store.jdbc.support.JdbcTable;
// 
///**
// * The Class MyJDBCDirectory.
// * 
// * @author prabhat.jha
// */
//public class MyJDBCDirectory extends JdbcDirectory {
// 
//    /**
//     * Instantiates a new my jdbc directory.
//     * 
//     * @param dataSource
//     *            the data source
//     * @param dialect
//     *            the dialect
//     * @param settings
//     *            the settings
//     * @param tableName
//     *            the table name
//     */
//    public MyJDBCDirectory(DataSource dataSource, Dialect dialect, JdbcDirectorySettings settings, String tableName) {
//        super(dataSource, dialect, settings, tableName);
//    }
// 
//    /**
//     * Instantiates a new my jdbc directory.
//     *
//     * @param dataSource the data source
//     * @param dialect the dialect
//     * @param tableName the table name
//     */
//    public MyJDBCDirectory(DataSource dataSource, Dialect dialect, String tableName) {
//        super(dataSource, dialect, tableName);
//    }
// 
//    /**
//     * Instantiates a new my jdbc directory.
//     *
//     * @param dataSource the data source
//     * @param settings the settings
//     * @param tableName the table name
//     * @throws JdbcStoreException the jdbc store exception
//     */
//    public MyJDBCDirectory(DataSource dataSource, JdbcDirectorySettings settings, String tableName) throws JdbcStoreException {
//        super(dataSource, settings, tableName);
//    }
// 
//    /**
//     * Instantiates a new my jdbc directory.
//     *
//     * @param dataSource the data source
//     * @param table the table
//     */
//    public MyJDBCDirectory(DataSource dataSource, JdbcTable table) {
//        super(dataSource, table);
//    }
// 
//    /**
//     * Instantiates a new my jdbc directory.
//     *
//     * @param dataSource the data source
//     * @param tableName the table name
//     * @throws JdbcStoreException the jdbc store exception
//     */
//    public MyJDBCDirectory(DataSource dataSource, String tableName) throws JdbcStoreException {
//        super(dataSource, tableName);
//    }
// 
//    /**
//     * (non-Javadoc).
//     *
//     * @return the string[]
//     * @throws IOException Signals that an I/O exception has occurred.
//     * @see org.apache.lucene.store.Directory#listAll()
//     */
//    @Override
//    public String[] listAll() throws IOException {
//        return super.list();
//    }
//}
