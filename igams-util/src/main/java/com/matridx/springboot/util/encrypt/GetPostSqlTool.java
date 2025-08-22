package com.matridx.springboot.util.encrypt;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GetPostSqlTool  {
	
	
	public Connection CreateConnect(){
		//声明Connection对象
		 Connection con = null;
		 //驱动程序名
		 String driver = "org.postgresql.Driver";
		 //URL指向要访问的数据库名mydata
		 String url = "jdbc:postgresql://172.17.60.185:5432/matridx";
		//MySQL配置时的用户名
		 String user = "matridx";
		 //MySQL配置时的密码
		 String password = "matridx2020!";
		 
		 //遍历查询结果集
		 try {
		     //加载驱动程序
		     Class.forName(driver);
		     //1.getConnection()方法，连接MySQL数据库！！
		     con = DriverManager.getConnection(url,user,password);
		     if(!con.isClosed())
		         System.out.println("Succeeded connecting to the Database!");
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 return con;
	}
	
	public boolean GetSqlTableCount(FileWriter out){
		
		 List<String> table_list = new ArrayList<>();
		 List<String> cnt_list = new ArrayList<>();
		 //遍历查询结果集
		 try {
			 //声明Connection对象
			 Connection con = CreateConnect();
		     //2.创建statement类对象，用来执行SQL语句！！
		     Statement statement = con.createStatement();
		     
		     //要执行的SQL语句
		     String sql = "select * from pg_tables  where schemaname ='public'";
		     
		     //String sql = "SELECT '[UFDATA_001_2016].[dbo].'+ a.name AS tabname FROM [UFDATA_001_2016].[dbo].sysobjects a "
		     // ", [UFDATA_001_2016].[dbo].syscolumns b WHERE a.id = b.id AND b.name = 'cInvCode' and type = 'U' order by name";
		     
		     ResultSet set = statement.executeQuery(sql);
		     
		     while(set.next())
		     {
		    	 String col = set.getString("tablename");
		    	 table_list.add(col);
		     }
		     
		     set.close();
		     
		     for(int i=0;i<table_list.size();i++){
		    	 sql = "SELECT count(1) AS cnt FROM " + table_list.get(i) ;
		    	 
		    	 ResultSet cntset = statement.executeQuery(sql);
		    	 while(cntset.next())
			     {
			    	 int col = cntset.getInt("cnt");
			    	 cnt_list.add(table_list.get(i) + " : " + col);
			    	 out.write(table_list.get(i) + " : " + col +"\r\n");
			     }
		    	 cntset.close();
		     }
		     
		     statement.close();
		     
		     out.flush();
		     out.close();
		     
		     con.close();
		 }catch(Exception e){
		     try {
				out.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 }
		 return true;
	}

	public static void main(String[] args){
		
		try{
			
			GetPostSqlTool tool = new GetPostSqlTool();
			
			FileWriter out = new FileWriter("D:/table-cnt_0011.text");
			
			tool.GetSqlTableCount(out);
			
		}catch(Exception e){
			
		}
		
    }
}