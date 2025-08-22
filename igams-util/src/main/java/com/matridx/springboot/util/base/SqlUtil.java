package com.matridx.springboot.util.base;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SQL处理工具
 * @author goofus
 *
 */
public class SqlUtil {

	private static final Logger log = LoggerFactory.getLogger(SqlUtil.class);
	
	/**
	 * 分区关键字
	 */
	private final static String PARTITION = "partition";
	/**
	 * 替换分区字符正则
	 */
	private final static String PARTITION_REG = "(?i)partition\\s*\\([^)]+\\)";
	
	/**
	 * 判断给定的SQL最外层是否存在WHERE
	 * @return 若存在WHERE，则返回true，否则返回false
	 */
	public static boolean whereExists(String sql) {
		boolean exists = false;
		try {
			//存在分区关键字则进行替换避免parse异常(因其无法识别)
			if(sql.toLowerCase().contains(PARTITION)){
				sql = sql.replaceAll(PARTITION_REG, "");
			}
			Statement stmt = CCJSqlParserUtil.parse(sql);
			Select select = (Select) stmt;
			SelectBody selectBody = select.getSelectBody();
			if (selectBody instanceof PlainSelect) {
				PlainSelect plainSelect = (PlainSelect) selectBody;
				//System.out.println(plainSelect.getWhere());
				if(null != plainSelect.getWhere()) exists = true;
			}
		} catch (JSQLParserException e) {
			log.error(e.getMessage());
			if(sql.toLowerCase().contains("where ")) {
				exists = true;
			}
		}
	    return exists;
	}

}
