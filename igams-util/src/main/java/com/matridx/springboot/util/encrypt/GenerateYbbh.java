package com.matridx.springboot.util.encrypt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GenerateYbbh {

	public static Connection CreateConnect() {
		// 声明Connection对象
		Connection con = null;
		// 驱动程序名
		String driver = "org.postgresql.Driver";
		// URL指向要访问的数据库名mydata
		String url = "jdbc:postgresql://localhost:5432/matridx";
		// MySQL配置时的用户名
		String user = "postgres";
		// MySQL配置时的密码
		String password = "root";

		// 遍历查询结果集
		try {
			// 加载驱动程序
			Class.forName(driver);
			// 1.getConnection()方法，连接MySQL数据库！！
			con = DriverManager.getConnection(url, user, password);
			if (!con.isClosed())
				System.out.println("Succeeded connecting to the Database!");
		} catch (Exception e) {
			System.out.println("数据路链接异常："+ e.getMessage());
		}
		return con;
	}

	public List<String> SelectYbbh(Statement statement, String ybbh) {

		try {
			String sql = "SELECT ybbh FROM igams_ybbhgl WHERE ybbh = '" + ybbh + "' ";
			ResultSet set = statement.executeQuery(sql);
			List<String> selectList = new ArrayList<>();
			while (set.next()) {
				String selectybbh = set.getString("ybbh");
				System.out.println("Ybbh: " + selectybbh);
				selectList.add(selectybbh);
			}
			set.close();
			return selectList;
		} catch (SQLException e) {
			System.out.println("SQL异常："+ e.getMessage());
		}
		return null;
	}

	public boolean InsertYbbh(Statement statement, String ybbh) {

		try {
			String sql = "insert into igams_ybbhgl (ybbh, lrsj) values ('"+ ybbh + "', LOCALTIMESTAMP)";
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("SQL异常："+ e.getMessage());
		}
		return true;
	}

	// 获取八位随机字母或数字,与数据库对比
	public static void main(String[] args) {
		try {
			String url = "http://service.matridx.com/wechat/getWechatUserReport?ybbh=";
			// 声明Connection对象
			Connection con = CreateConnect();

			// 2.创建statement类对象，用来执行SQL语句！！
			Statement statement = con.createStatement();
			GenerateYbbh generateYbbh = new GenerateYbbh();
			Random random = new Random();
			Scanner scan = new Scanner(System.in);
			System.out.println("请输入生成条数：");
			String count = scan.nextLine();
			List<String> ybbhList = new ArrayList<>();
			//将结果存入txt文件
			File file = new File("D:\\标本编号.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(file,true));
			
			for (int j = 0; j < Integer.parseInt(count); j++) {
				StringBuilder val = new StringBuilder();
				for (int i = 0; i < 8; i++) {
					int a = random.nextInt(36);
					if (a < 10) {
						val.append(a);
					} else {
						val.append((char) (a - 10 + 65));
					}
				}
				List<String> selectYbbh = generateYbbh.SelectYbbh(statement, val.toString());
				if(selectYbbh != null && !selectYbbh.isEmpty()){
					System.out.println("编号重复："+val);
					j--;
					continue;
				}
				generateYbbh.InsertYbbh(statement, val.toString());
				ybbhList.add(val.toString());
				out.write(val + "\t" + url+val+"\r\n"); // \r\n即为换行
			}
			scan.close();
			statement.close();
			con.close();
			out.flush(); // 把缓存区内容压入文件
			out.close();
		} catch (SQLException e) {
			System.out.println("SQL异常："+ e.getMessage());
		} catch (IOException e) {
			System.out.println("IO异常："+ e.getMessage());
		}
	}
}
