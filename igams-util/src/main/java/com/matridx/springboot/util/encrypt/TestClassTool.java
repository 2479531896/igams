package com.matridx.springboot.util.encrypt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.util.ClassUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class TestClassTool  {
	
	public Connection CreateConnect(){
		//声明Connection对象
		 Connection con = null;
		 //驱动程序名
		 String driver = "org.postgresql.Driver";
		 //URL指向要访问的数据库名mydata
		 String url = "jdbc:postgresql://localhost:5432/matridx";
		 //MySQL配置时的用户名
		 String user = "postgres";
		 //MySQL配置时的密码
		 String password = "root";
		 
		 //遍历查询结果集
		 try {
		     //加载驱动程序
		     Class.forName(driver);
		     //1.getConnection()方法，连接MySQL数据库！！
		     con = DriverManager.getConnection(url,user,password);
		     if(!con.isClosed())
		         System.out.println("Succeeded connecting to the Database!");
		 }catch(Exception e){
			 
		 }
		 return con;
	}
	
	public boolean CreateInfo(String info){
		
		 //遍历查询结果集
		 try {
			 //声明Connection对象
			 Connection con = CreateConnect();
		     //2.创建statement类对象，用来执行SQL语句！！
		     Statement statement = con.createStatement();
		     
		     //要执行的SQL语句
		     String sql = "insert into test() values()";
		     boolean set = statement.execute(sql);
		     
		 }catch(Exception e){
			 
		 }
		 return true;
	}
	

	public static void main(String[] args){
		try{
			
			TestClassTool tool = new TestClassTool();
			//Model文件创建
			URL url = new URL("https://epaper.gmw.cn/gmrb/html/2022-03/30/nw.D110000gmrb_20220330_1-01.htm");

			URLConnection con = url.openConnection();

			con.setRequestProperty("User-Agent","略");

			InputStream in = con.getInputStream();
			
			//BufferedReader br = new BufferedReader(in);
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder=factory.newDocumentBuilder();
			Document doc = builder.parse(in);
			NodeList list = doc.getElementsByTagName("<div class=\"text_c\">");
			System.out.print(list.getLength());
		}catch(Exception e){
			
		}
    }
}