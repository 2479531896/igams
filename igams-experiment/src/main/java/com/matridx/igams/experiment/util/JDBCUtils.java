package com.matridx.igams.experiment.util;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.experiment.dao.entities.WksjmxDto;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.*;

public class JDBCUtils {
    private final Logger log = LoggerFactory.getLogger(JDBCUtils.class);

    private static final String driverName = "org.postgresql.Driver";       // 数据库驱动名称

    private static final String url = "jdbc:postgresql://172.17.70.5:5432/bcl2fqc";


    private static final String userName = "readonlyuser";          // 用户名

    private static final String password = "cCHtfFmuSdZ4Fu";     // 用户密码

    private static Connection conn = null;                  // 数据库连接对象

    private static PreparedStatement preparedStatement = null;  // 执行操作对象

    public List<Map<String,String>> querylib(List<WksjmxDto> wksjmxDtoList){
        List<Map<String,String>> list =new ArrayList<>();
        try {
            Class.forName(driverName);      // 2、注册数据库驱动
            conn = DriverManager.getConnection(url, userName, password);    // 3、获取数据库连接
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        String uids="";
        for(WksjmxDto wksjmxDto:wksjmxDtoList){
            if(StringUtil.isBlank(uids)){
                uids+="'"+wksjmxDto.getWkbm()+"'";
            }else{
                uids+=","+"'"+wksjmxDto.getWkbm()+"'";
            }
        }
        ResultSet result = null;
        String sql = "SELECT " +
                "uid, " +
                "other_metrics " +
                "FROM " +
                "core_library " +
                "WHERE " +
                "uid in (" + uids+
                ")";

        try {
            preparedStatement = conn.prepareStatement(sql);         // 4、执行预编译SQL操作
            result = preparedStatement.executeQuery();  // 5、执行查询操作
            while (result.next()) {
                Map<String,String>map=new HashMap<>();
                JSONObject jsonObject=JSONObject.parseObject(result.getString(2));
                map.put("wkbh",result.getString(1));
                map.put("wknd",jsonObject.getString("con.lib"));

                list.add(map);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }finally {
            // 7、释放资源
            close(conn, preparedStatement, result);
        }
        return list;
    }



    /**
     * 释放资源
     * @param conn  关闭数据库连接
     * @param statement 关闭数据库操作对象
     * @param rs    关闭结果集操作对象
     */
    public static void close(Connection conn, Statement statement, ResultSet rs) {
        if (Objects.nonNull(rs)) {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (Objects.nonNull(statement)) {
            try {
                statement.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (Objects.nonNull(conn)) {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
