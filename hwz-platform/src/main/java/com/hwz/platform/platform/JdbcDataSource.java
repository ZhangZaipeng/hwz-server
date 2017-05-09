package com.hwz.platform.platform;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 用来 Jdbc 链接数据库，并以游标的形式查询
 */
public class JdbcDataSource implements Iterable<DataRow>, Closeable {

    private static final int BATCH_SIZE = 50;
    private static final int MAX_ROWS = 0;

    private ResultSet resultSet;
    private PreparedStatement stmt = null;
    private Connection c = null;
    private DataSource ds;

    private List<String> colNames;

    private Iterator<DataRow> rSetIterator;
    private long excuteTime;

    public JdbcDataSource(DataSource ds, String sql, Collection<?> xs) throws SQLException {
        this.ds = ds;
        c = ds.getConnection();

        stmt = c.prepareStatement(sql);
        int idx = 1;
        if (xs != null) {
            for (Object obj : xs) {
                stmt.setObject(idx++, obj);
            }
        }
        stmt.setFetchSize(BATCH_SIZE);
        stmt.setMaxRows(MAX_ROWS);

        try {
            stmt.execute();
        } catch (Exception e) {
            try {
                this.close();
            } catch (Exception e2) {
                //igonre
            }
            throw e;
        }

        long start = System.currentTimeMillis();
        if (stmt.execute()) {
            resultSet = stmt.getResultSet();
        }
        excuteTime = System.currentTimeMillis() - start;
        colNames = readFieldNames(resultSet.getMetaData());
        if (resultSet == null) {
            rSetIterator = new ArrayList<DataRow>().iterator();
            return;
        }

        rSetIterator = new Iterator<DataRow>() {

            public boolean hasNext() {
                try {
                    return hasnext();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            public DataRow next() {
                try {
                    return getARow();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            public void remove() {/* do nothing */
            }
        };
    }


    private List<String> readFieldNames(ResultSetMetaData metaData) throws SQLException {
        List<String> colNames = new ArrayList<String>();
        int count = metaData.getColumnCount();
        for (int i = 0; i < count; i++) {
            colNames.add(metaData.getColumnLabel(i + 1));
        }
        return colNames;
    }

    @Override
    public Iterator<DataRow> iterator() {
        return rSetIterator;
    }

    private DataRow getARow() throws SQLException {

        if (resultSet == null) return null;
        DataRow result = new DataRow();
        for (String colName : colNames) {
            result.put(colName, resultSet.getObject(colName));
        }
        return result;
    }

    private boolean hasnext() throws SQLException, IOException {
        if (resultSet == null) {
            return false;
        }
        if (resultSet.next()) {
            return true;
        } else {
            close();
            return false;
        }
    }

    @Override
    public void close() throws IOException {
        try {
            if (this.resultSet != null) {
                this.resultSet.close();
                this.resultSet = null;
            }
            if (this.stmt != null) {
                this.stmt.close();
                this.stmt = null;
            }
        } catch (SQLException e) {
            throw new IOException("SQL error", e);
        }
//          if (this.c != null) {
//                this.c.close();
//                this.c = null;
//          }
        c = null;
        ds = null;
    }

    public long getExcuteTime() {
        return excuteTime;
    }

}
