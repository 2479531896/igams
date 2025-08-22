package com.matridx.springboot.util.encrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CreateClassTool  {

	private static final Logger log = LoggerFactory.getLogger(CreateClassTool.class);
	
	public boolean createFile(String s_modelfile,String sampleFile,String module,String packname,boolean isWriteInfo) throws IOException{
		
		String s_path = s_modelfile.substring(0,s_modelfile.lastIndexOf("\\"));
		File s_file = new File(s_path);
		if(!s_file.exists())
			s_file.mkdirs();
		
		FileWriter out = new FileWriter(s_modelfile);
		
		BufferedReader read = new BufferedReader(new FileReader(sampleFile));
		
		String str;
		while((str = read.readLine()) != null){
			out.write(str.replaceAll("Example", module).replaceAll("packexp", packname)+"\r\n");
			if(isWriteInfo && str.indexOf("extends BaseModel") > 0)
				CreateInfo(module.toLowerCase(),out);
			else if(isWriteInfo && str.indexOf("post.IExampleDao") > 0)
				CreateMapInfo(module.toLowerCase(),out);
		}
		
		out.flush();
		out.close();
		
		read.close();
		
		return true;
	}
	
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
			 log.error(e.getMessage());
		 }
		 return con;
	}
	
	public boolean CreateInfo(String tablename,FileWriter out){
		
		 //不需要的字段
		 List<String> except_list = new ArrayList<>();
		 except_list.add("lrry");
		 except_list.add("lrsj");
		 except_list.add("xgry");
		 except_list.add("xgsj");
		 except_list.add("scry");
		 except_list.add("scsj");
		 except_list.add("scbj");
		 
		 List<String> column_list = new ArrayList<>();
		 List<String> comment_list = new ArrayList<>();
		 //遍历查询结果集
		 try {
			 //声明Connection对象
			 Connection con = CreateConnect();
		     //2.创建statement类对象，用来执行SQL语句！！
		     Statement statement = con.createStatement();
		     
		     System.out.print("请输入数据库前缀名：");
		     Scanner datascan = new Scanner(System.in);
		     String dataname = datascan.nextLine();
		     //要执行的SQL语句
		     String sql = "SELECT col_description(a.attrelid,a.attnum) as comment,a.attname as name  FROM pg_class as c,pg_attribute as a where c.relname = '"
		     + dataname +"_" +tablename + "' and a.attrelid = c.oid and a.attnum>0";
		     datascan.close();
		     ResultSet set = statement.executeQuery(sql);
		     
		     while(set.next())
		     {
		    	 String col = set.getString("name");
		    	 String comment = set.getString("comment");
		    	 boolean isFind = false;
                 for (String s : except_list) {
                     if (s.equals(col)) {
                         isFind = true;
                         break;
                     }
                 }
		    	 if(isFind)
		    		 continue;
		    	 column_list.add(col);
		    	 comment_list.add(comment);
		     }
		     
		     for(int i=0;i<column_list.size();i++){
		    	 out.write("\t//"+comment_list.get(i)+"\r\n");
		    	 out.write("\tprivate String "+column_list.get(i) + ";"+"\r\n");
		     }
		     for(int i=0;i<column_list.size();i++){
		    	 out.write("\t//"+comment_list.get(i)+"\r\n");
		    	 out.write("\tpublic String get"+changeModule(column_list.get(i)) + "() {"+"\r\n");
		    	 out.write("\t\treturn "+column_list.get(i)+";"+"\r\n");
		    	 out.write("\t}"+"\r\n");
		    	 
		    	 out.write("\tpublic void set"+changeModule(column_list.get(i)) + "(String "+column_list.get(i) +"){"+"\r\n");
		    	 out.write("\t\tthis."+column_list.get(i)+ " = " +column_list.get(i) +";"+"\r\n");
		    	 out.write("\t}"+"\r\n");
		     }
		 }catch(Exception e){
			 log.error(e.getMessage());
		 }
		 return true;
	}
	
	public boolean CreateMapInfo(String tablename,FileWriter out){
		 
		 List<String> column_list = new ArrayList<>();
		 List<String> comment_list = new ArrayList<>();
		 //遍历查询结果集
		 try {
			//声明Connection对象
			 Connection con = CreateConnect();
			 
		     //2.创建statement类对象，用来执行SQL语句！！
		     Statement statement = con.createStatement();
		     
		     System.out.print("请输入数据库前缀名：");
		     Scanner datascan = new Scanner(System.in);
		     String dataname = datascan.nextLine();
		     //要执行的SQL语句
		     String sql = "SELECT col_description(a.attrelid,a.attnum) as comment,a.attname as name  FROM pg_class as c,pg_attribute as a where c.relname = '"
		     + dataname +"_" +tablename + "' and a.attrelid = c.oid and a.attnum>0";
		     datascan.close();
		     ResultSet set = statement.executeQuery(sql);
		     
		     while(set.next())
		     {
		    	 String col = set.getString("name");
		    	 String comment = set.getString("comment");
                 column_list.add(col);
		    	 comment_list.add(comment);
		     }
		     
		     for(int i=0;i<column_list.size();i++){
		    	 out.write("\t//"+comment_list.get(i)+"\r\n");
		    	 out.write("\tprivate String "+column_list.get(i) + ";"+"\r\n");
		     }
		     for(int i=0;i<column_list.size();i++){
		    	 out.write("\t//"+comment_list.get(i)+"\r\n");
		    	 out.write("\tpublic String get"+changeModule(column_list.get(i)) + "() {"+"\r\n");
		    	 out.write("\t\treturn "+column_list.get(i)+";"+"\r\n");
		    	 out.write("\t}"+"\r\n");
		    	 
		    	 out.write("\tpublic void set"+changeModule(column_list.get(i)) + "(String "+column_list.get(i) +"){"+"\r\n");
		    	 out.write("\t\tthis."+column_list.get(i)+ " = " +column_list.get(i) +";"+"\r\n");
		    	 out.write("\t}"+"\r\n");
		     }
		 }catch(Exception e){
			 log.error(e.getMessage());
		 }
		 return true;
	}
	
	public String changeModule(String name){
		if(name == null || name.isEmpty())
			return null;
		name = name.toLowerCase();
		name = name.substring(0,1).toUpperCase()+ name.substring(1);
		return name;
	}

	public static void main(String[] args){
		String basePath;
		String modelpath = "dao\\entities\\";
		String mappath = "dao\\mapper\\";
		String daoPath = "dao\\post\\";
		String implPath = "service\\impl\\";
		String inPath = "service\\svcinterface\\";
		Scanner scan = new Scanner(System.in);

		Scanner packscan = new Scanner(System.in);
		
		Scanner modulescan = new Scanner(System.in);
		
		try{
			String t_path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
			basePath = t_path.replace("igams-util/target/classes/", "").substring(1);
			
			CreateClassTool tool = new CreateClassTool();
			
			System.out.print("请输入所在模块名：");
			
			String modulename = modulescan.nextLine();
			
			String proPath = modulename + "\\src\\main\\java\\com\\matridx\\igams\\";

			System.out.print("请输入要创建的包(package)名：");
			
			String packname = packscan.nextLine();
			
			proPath = proPath+packname+"\\";
			
			System.out.print("请输入要创建的功能名(如xtyh)：");
			String module = tool.changeModule(scan.nextLine());
			//Model文件创建
			String s_modelfile = basePath+ proPath + modelpath + module + "Model.java";
			File file = new File(s_modelfile);
			if(file.exists()){
				System.out.print("抱歉，文件已经存在，停止运行。");
				scan.close();
				packscan.close();
				modulescan.close();
				return;
			}
			String samplePath = basePath +"igams-util\\src\\main\\java\\com\\matridx\\springboot\\util\\file\\sample\\";
			
			String sampleFile = samplePath + "model.txt";
			tool.createFile(s_modelfile,sampleFile,module,packname,true);
			//Dto文件创建
			s_modelfile = basePath+ proPath + modelpath + module + "Dto.java";
			sampleFile = samplePath + "dto.txt";
			tool.createFile(s_modelfile,sampleFile,module,packname,false);
			//mapper文件创建
			s_modelfile = basePath+ proPath + mappath + module + "Mapper.xml";
			sampleFile = samplePath + "mapper.txt";
			tool.createFile(s_modelfile,sampleFile,module,packname,true);
			
			//dao文件创建
			s_modelfile = basePath+ proPath + daoPath + "I" + module + "Dao.java";
			sampleFile = samplePath + "dao.txt";
			tool.createFile(s_modelfile,sampleFile,module,packname,false);
			//impl文件创建
			s_modelfile = basePath+ proPath + implPath + module + "ServiceImpl.java";
			sampleFile = samplePath + "impl.txt";
			tool.createFile(s_modelfile,sampleFile,module,packname,false);
			
			//inf文件创建
			s_modelfile = basePath+ proPath + inPath + "I" + module + "Service.java";
			sampleFile = samplePath + "inter.txt";
			tool.createFile(s_modelfile,sampleFile,module,packname,false);
			
		}catch(Exception e){
			log.error(e.getMessage());
		}
		modulescan.close();
		packscan.close();
		scan.close();
		
    }
}