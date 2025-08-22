package com.matridx.las.netty.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="AutoResultModel")
public class AutoResultModel {
	
		private String Date;
		//文库id
		private String Action;
		//类型
		private String backdata;
		//样本结果集合
		private List<SampleResultModel> Result ;
		public String getDate() {
			return Date;
		}
		public void setDate(String date) {
			Date = date;
		}
		public String getAction() {
			return Action;
		}
		public void setAction(String action) {
			Action = action;
		}
		public String getBackdata() {
			return backdata;
		}
		public void setBackdata(String backdata) {
			this.backdata = backdata;
		}
		public List<SampleResultModel> getResult() {
			return Result;
		}
		public void setResult(List<SampleResultModel> result) {
			Result = result;
		}
		
}
