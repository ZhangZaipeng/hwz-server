package com.hwz.platform.mybatis;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hwz.platform.platform.Pagination;

import javax.xml.bind.PropertyException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;


/**
 * 分页工具类
 *
 * @author arrow
 */

@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class PagePlugin implements Interceptor {

    private static final Logger log = LoggerFactory.getLogger(PagePlugin.class);

    private String dataDialect = "";                                       // 数据库方言
    private String pageSqlId = "";                                       // mapper.xml中需要拦截的ID(正则匹配)

    public String getDataDialect() {
        return dataDialect;
    }

    public void setDataDialect(String dataDialect) {
        this.dataDialect = dataDialect;
    }

    public String getPageSqlId() {
        return pageSqlId;
    }

    public void setPageSqlId(String pageSqlId) {
        this.pageSqlId = pageSqlId;
    }

    /**
     * 检测字符串是否不为空(null,"","null")
     *
     * @param s
     * @return 不为空则返回true，否则返回false
     */
    public static boolean notEmpty(String s) {
        return s != null && !"".equals(s) && !"null".equals(s);
    }

    /**
     * 检测字符串是否为空(null,"","null")
     *
     * @param s
     * @return 为空则返回true，不否则返回false
     */
    public static boolean isEmpty(String s) {
        return s == null || "".equals(s) || "null".equals(s);
    }

    public Object intercept(Invocation ivk) throws Throwable {
        // TODO Auto-generated method stub
        if (ivk.getTarget() instanceof RoutingStatementHandler) {
            RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk.getTarget();
            BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler,
                    "delegate");
            MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate,
                    "mappedStatement");

            if (mappedStatement.getId().matches(pageSqlId)) { // 拦截需要分页的SQL
                BoundSql boundSql = delegate.getBoundSql();
                Object parameterObject = boundSql.getParameterObject();// 分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
                if (parameterObject == null) {
                    throw new NullPointerException("parameterObject尚未实例化！");
                } else {
                    Connection connection = (Connection) ivk.getArgs()[0];
                    String sql = boundSql.getSql();

                    // 此处sql server 的统计方式稍有区别， 子查询中不允许带有order by 子句
                    if ("sqlServer".equals(dataDialect)) {
                        if (sql.indexOf("order ") > 0) {
                            sql = sql.substring(0, sql.indexOf("order "));
                        }
                    }
                    String countSql = "select count(0) from (" + sql + ") tmp_count"; // 记录统计

                    PreparedStatement countStmt = connection.prepareStatement(countSql);
                    BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
                            boundSql.getParameterMappings(), parameterObject);
                    setParameters(countStmt, mappedStatement, countBS, parameterObject);
                    ResultSet rs = countStmt.executeQuery();
                    int count = 0;
                    if (rs.next()) {
                        count = rs.getInt(1);
                    }
                    rs.close();
                    countStmt.close();

                    Pagination pagination = loadPagination(parameterObject);
                    pagination.setCount(count);
                    String pageSql = generatePageSql(sql, pagination);
                    ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql); // 将分页sql语句反射回BoundSql.
                }
            }
        }
        return ivk.proceed();
    }

    /**
     * 找到分页对象
     */
    private Pagination loadPagination(Object parameterObject) throws SecurityException, IllegalArgumentException,
            NoSuchFieldException, IllegalAccessException {
        if (parameterObject instanceof Pagination) { // 参数就是Page实体
            Pagination pagination = (Pagination) parameterObject;
            return pagination;
        } else if (parameterObject instanceof Map) {
            for (Object value : ((Map<?, ?>) parameterObject).values()) {
                if (value instanceof Pagination) {
                    return (Pagination) value;
                }
            }
            return null;
        } else if (parameterObject instanceof Set) {
            for (Object value : (Set<?>) parameterObject) {
                if (value instanceof Pagination) {
                    return (Pagination) value;
                }
            }
            return null;
        } else {
            // 参数为某个实体，该实体拥有Page属性
            Field pageField = ReflectHelper.getFieldByFieldName(parameterObject, "pagination");
            if (pageField != null) {
                Pagination pagination = (Pagination) ReflectHelper.getValueByFieldName(parameterObject, "pagination");
                if (pagination == null) {
                    pagination = new Pagination();
                }
                // 通过反射，对实体对象设置分页对象
                ReflectHelper.setValueByFieldName(parameterObject, "pagination", pagination);
                return pagination;
            } else {
                throw new NoSuchFieldException(parameterObject.getClass().getName() + "不存在 pagination 属性！");
            }
        }
    }

    /**
     * 对SQL参数(?)设值,参考org.apache.ibatis.executor.parameter.DefaultParameterHandler
     *
     * @param ps
     * @param mappedStatement
     * @param boundSql
     * @param parameterObject
     * @throws java.sql.SQLException
     */
    @SuppressWarnings("unchecked")
    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
                               Object parameterObject) throws SQLException {
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            Configuration configuration = mappedStatement.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    PropertyTokenizer prop = new PropertyTokenizer(propertyName);
                    if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (propertyName.startsWith(org.apache.ibatis.scripting.xmltags.ForEachSqlNode.ITEM_PREFIX)
                            && boundSql.hasAdditionalParameter(prop.getName())) {
                        value = boundSql.getAdditionalParameter(prop.getName());
                        if (value != null) {
                            value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
                        }
                    } else {
                        value = metaObject == null ? null : metaObject.getValue(propertyName);
                    }
                    @SuppressWarnings("rawtypes")
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    if (typeHandler == null) {
                        throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName
                                + " of statement " + mappedStatement.getId());
                    }
                    typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
                }
            }
        }
    }

    /**
     * 根据数据库方言，生成特定的分页sql
     *
     * @param sql
     * @return
     */
    private String generatePageSql(String sql, Pagination pagination) {
        if (pagination == null) {
            return sql;
        }
        if ("mysql".equals(dataDialect)) {
            StringBuffer pageSql = new StringBuffer(sql);
            pageSql.append(" limit " + pagination.getBegin() + "," + pagination.getPageSize());
            return pageSql.toString();
        } else if ("oracle".equals(dataDialect)) {
            sql = sql.trim();
            String forUpdateClause = null;
            boolean isForUpdate = false;
            final int forUpdateIndex = sql.toLowerCase().lastIndexOf("for update");
            if (forUpdateIndex > -1) {
                // save 'for update ...' and then remove it
                forUpdateClause = sql.substring(forUpdateIndex);
                sql = sql.substring(0, forUpdateIndex - 1);
                isForUpdate = true;
            }
            boolean hasOffset = pagination.getBegin() > 0;
            StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
            if (hasOffset) {
                pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
            } else {
                pagingSelect.append("select * from ( ");
            }
            pagingSelect.append(sql);
            if (hasOffset) {
                pagingSelect.append(" ) row_ where rownum <= " + pagination.getEnd() + ") where rownum_ > "
                        + pagination.getBegin());
            } else {
                pagingSelect.append(" ) where rownum <= " + pagination.getEnd());
            }

            if (isForUpdate) {
                pagingSelect.append(" ");
                pagingSelect.append(forUpdateClause);
            }

            return pagingSelect.toString();
        } else if ("sqlServer".equals(dataDialect)) {
            sql = sql.trim().toLowerCase();
            StringBuffer pageSql = new StringBuffer(sql);
            if (sql.startsWith("select")) {

                boolean hasOffset = pagination.getBegin() > 0;
                if (!hasOffset) {
                    String replaceStr = "select top " + pagination.getEnd() + " ";
                    pageSql.replace(0, "select".length(), replaceStr);
                } else {
                    String replaceStr = "select * from (  select row_number() over (order by tempcolumn) temprownumber,*  from ( select top "
                            + pagination.getEnd() + " tempcolumn=0, ";
                    pageSql.replace(0, "select".length(), replaceStr);
                    pageSql.append(")t )tt  where temprownumber > " + pagination.getBegin());
                }

            }
            return pageSql.toString();
        }
        {
            return sql;
        }
    }

    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0, this);
    }

    public void setProperties(Properties p) {
        dataDialect = p.getProperty("dataDialect");
        if (isEmpty(dataDialect)) {
            try {
                throw new PropertyException("dialect property is not found!");
            } catch (PropertyException e) {
                log.error("error", e);
            }
        }
        pageSqlId = p.getProperty("pageSqlId");
        if (isEmpty(pageSqlId)) {
            try {
                throw new PropertyException("pageSqlId property is not found!");
            } catch (PropertyException e) {
                log.error("error", e);
            }
        }
    }

}
