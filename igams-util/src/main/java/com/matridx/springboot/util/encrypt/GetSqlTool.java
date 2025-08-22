package com.matridx.springboot.util.encrypt;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetSqlTool  {
	
	
	public Connection CreateConnect(){
		//声明Connection对象
		 Connection con = null;
		 //驱动程序名
		 String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		 //URL指向要访问的数据库名mydata
		 String url = "jdbc:sqlserver://172.17.60.99:1433;databasename=UFDATA_001_2016";
		 //MySQL配置时的用户名
		 String user = "sa";
		 //MySQL配置时的密码
		 String password = "Ufida123";
		 
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
		 
	     Date date = new Date();
	     System.out.println("[" + date.toString() + "] "  + " Start!");
	     
		 //遍历查询结果集
		 try {
			 //声明Connection对象
			 Connection con = CreateConnect();
		     //2.创建statement类对象，用来执行SQL语句！！
		     Statement statement = con.createStatement();
		     
		     //要执行的SQL语句
		     String sql = "SELECT '[UFDATA_001_2016].[dbo].'+ name AS tabname FROM "
		     		+ "[UFDATA_001_2016].[dbo].sysobjects where type = 'U' order by name";
//		     String sql = "SELECT '[UFSystem].[dbo].'+ name AS tabname FROM "
//			     		+ "[UFSystem].[dbo].sysobjects where type = 'U' order by name";
		     
//		     String sql = "SELECT '[UFDATA_001_2016].[dbo].'+ a.name AS tabname FROM [UFDATA_001_2016].[dbo].sysobjects a "
//		       +" [UFDATA_001_2016].[dbo].syscolumns b WHERE a.id = b.id AND b.name = 'cInvCode' and type = 'U' order by name";
		     
		     ResultSet set = statement.executeQuery(sql);
		     
		     while(set.next())
		     {
		    	 String col = set.getString("tabname");
		    	 if(!"[UFSYSTEM].[dbo].UA_Messag".equals(col))
		    		 table_list.add(col);
		     }
		     
		     set.close();
		     
		     for(int i=0;i<table_list.size();i++){
//		    	 sql = "SELECT count(1) AS cnt FROM " + table_list.get(i) ;
//		    	 
//		    	 ResultSet cntset = statement.executeQuery(sql);
//		    	 while(cntset.next())
//			     {
//			    	 int col = cntset.getInt("cnt");
//			    	 out.write(table_list.get(i) + " : " + col +"\r\n");
//			     }
//		    	 cntset.close();
		    	 
		    	 //sql = "Select name from syscolumns Where ID=OBJECT_ID('" + table_list.get(i) + "') and name like '%VouchOutSignNum%'";
		    	 String t_sql ="";
		    	 try {
//			    	 sql = "Select name,xtype from [UFDATA_001_2016].[dbo].syscolumns Where ID=OBJECT_ID('" + table_list.get(i) + "') ";
//			    	 ResultSet cntset = statement.executeQuery(sql);
			    	 
			    	 t_sql = "Select count(1) cn from " + table_list.get(i) ;
//			    	 t_sql += " where 1=2 ";
//			    	 while(cntset.next())
//				     {
////			    		 if(( cntset.getString("xtype").equals("56") ||cntset.getString("xtype").equals("62") || cntset.getString("xtype").equals("104") )&& cntset.getString("name").indexOf(" ") <0) {
////				    		 t_sql += " or " + cntset.getString("name") + " = 22989";
////			    		 }
//			    		 
//			    		 if(( cntset.getString("xtype").equals("231") )&& cntset.getString("name").indexOf(" ") <0) {
//				    		 t_sql += " or " + cntset.getString("name") + " like '%22989'";
//			    		 }
//			    		 
//				     }

			    	 //System.out.println(t_sql);
			    	 ResultSet t_cntset = statement.executeQuery(t_sql);
			    	 if(t_cntset.next() && !"0".equals(t_cntset.getString("cn")) )
				     {
			    		 out.write(table_list.get(i) + " " + t_cntset.getString("cn") + "\r\n");
				     }
			    	 
			    	 t_cntset.close();
			    	 //cntset.close();
		    	 }catch(Exception e){
		    		 out.write(table_list.get(i) + " bbb: " + t_sql + " \r\n");
					 e.printStackTrace();
				 }
		     }
		     
		     statement.close();
		     
		     out.flush();
		     out.close();
		     
		     con.close();
		     date = new Date();
		     System.out.println("[" + date.toString() + "] "  + "Succeeded complete!");
		 }catch(Exception e){
			 e.printStackTrace();
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
			
			GetSqlTool tool = new GetSqlTool();
			
			FileWriter out = new FileWriter("D:/table-cnt_001.text");
			
			tool.GetSqlTableCount(out);
			
		}catch(Exception e){
			
		}
		
    }
}