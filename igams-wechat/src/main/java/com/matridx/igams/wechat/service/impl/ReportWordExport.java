package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.wechat.dao.entities.SjbgsmDto;
import com.matridx.igams.wechat.dao.entities.SjwzxxDto;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Service
public class ReportWordExport{
	
	/**
	 * 填充文档方法
	 * @param sjwzxxList //送检物种信息参数
	 * @param map	//送检信息参数
	 * @param fileName  //生成新的文档的名字
	 * @param sjbgsmDto  //送检报告参数
	 * @throws IOException 
	 */
	public static  void ExportWord(List<SjwzxxDto> sjwzxxList, Map<String, Object> map, String fileName,SjbgsmDto sjbgsmDto) throws IOException{
		ReportWordExport report=new ReportWordExport();
		List<String> codeList =report.codeKey();
		// 送检信息组装
		List<Map<String, String>> listMap = report.creatSjxx(sjwzxxList, map,sjbgsmDto);
		// 送检物种信息组装
		List<List<Map<String, String>>> wzflList = report.creatSjwzxxList(sjwzxxList);
		//参考文献数据
		// 把文件放入流
		FileInputStream fileInputStream = new FileInputStream("C:\\Users\\pc\\Desktop\\我的文档\\报告处理模板\\模板(自测)\\杰毅医诺报告单模板 v2.5（杰毅）DNA+RNA测序.docx");
		//读取word文件
		XWPFDocument document = new XWPFDocument(fileInputStream);
		//读取word中的单元格
		Iterator<XWPFTable> itTable = document.getTablesIterator();
		//迭代itTable 进行数据的替换
		 while (itTable.hasNext()) {
			 XWPFTable table = (XWPFTable)itTable.next();
			 //替换模板中的单个数据
			 report.replaceTemeplate(table, listMap);
			 //替换模板中的需要增加行的数据，
			 report. creatTable(table, codeList,wzflList);
			 //清除掉空的行（row），替换为 --
			 report.clearNullCell(table);
			 //合并相同的列
			 report.colspan(table);
		 }
		 //上边的操作是替换table，现在替换文本
		 Iterator<XWPFParagraph> iterators = document.getParagraphsIterator(); 
		 while (iterators.hasNext()) {
			 XWPFParagraph   paragraph = iterators.next(); 
			 //替换掉文本中的信息
			 report.replaceText(paragraph,listMap);
			 //出入图片
		}
		 //关闭流
		fileInputStream.close();
		// 写到目标文件
		OutputStream output = new FileOutputStream("C:\\Users\\pc\\Desktop\\我的文档\\报告处理模板\\报告\\" + fileName);
		document.write(output);
		document.close();
		output.close();
		System.out.println("success");
	}

	/**
	 * 替换模板中的单个数据
	 * @param table //当前迭代的表格
	 * @param listMap //需要替换的数据
	 */
	private  void replaceTemeplate(XWPFTable table, List<Map<String, String>> listMap){
		//获取到table 的行数 rows（多）
		int rcount = table.getNumberOfRows();
		for (int i = 0; i < rcount; i++){
			//拿到当前行的内容
			XWPFTableRow row = table.getRow(i);
			//获取当前行的列数
			List<XWPFTableCell> cells = row.getTableCells();
			//循环列（cells 多）
			for (XWPFTableCell cell : cells){
				//拿到当前列的 段落（多） 注意：poi操作word有bug，段落获取不完整。所以模板中需要直接粘贴，不能手动输入。
				List<XWPFParagraph> cellParList = cell.getParagraphs();
				//循环每个段落
				for (int p = 0; cellParList != null && p < cellParList.size(); p++){ 
					//拿到段落中的runs；
					List<XWPFRun> runs = cellParList.get(p).getRuns();
					//循环runs
					for (int q = 0; runs != null && q < runs.size(); q++){
						//获取到run；run为word文档中最小的变量
						String oneparaString = runs.get(q).getText(runs.get(q).getTextPosition());
						//过滤掉空的run
						if (oneparaString != null){
							// 循环替换的数据
							for (Map<String, String> sjxxparam : listMap){
								//迭代每个参数map，
								Iterator<Entry<String, String>> iter = sjxxparam.entrySet().iterator();
								Entry<String, String> entry = iter.next();
								//通过map的key与当前的run进行匹配，
								if (oneparaString.equals("«"+entry.getKey()+"»")){
									//如果相等，则进行替换
									oneparaString = oneparaString.replace(oneparaString, entry.getValue());
								}
							}
						}
						//把替换之后的数据放到run中。
						runs.get(q).setText(oneparaString, 0);
					}
				}
			}
		}
	}

	/**
	 * 判断哪些单元格需要增加一行
	 * @param table	   //当前的table
	 * @param strList  //需要进行添加一行的表格的标记
	 * @param lists    //所有的需要增加一行的数据
	 */
	private  void creatTable(XWPFTable table,List<String> strList,List<List<Map<String, String>>> lists){
		//循环标记，用来匹配表格
		for (int i = 0; i < strList.size(); i++){
			//获取当前table的行数 rows
			 int rcount = table.getNumberOfRows();
			 for (int j = 0; j < rcount; j++){
				 //拿到单行数据进行匹配
				 XWPFTableRow row=table.getRow(j);
				 creatRows(table,row,strList.get(i),lists);
			}
		}
	}

	/**
	 * 匹配哪些表格需要进行添加行
	 * @param table	//当前的table
	 * @param row   //用于进行匹配的行
	 * @param str   //用于进行匹配的标记
	 * @param lists //填充的数据
	 */
	private void creatRows(XWPFTable table, XWPFTableRow row,String str,List<List<Map<String, String>>> lists){
		//进行标记匹配只需要拿到模板的最后一行；同时也用于接下来的新增行的样式来使用
		XWPFTableRow tmpRow = table.getRows().get(table.getRows().size()-1);
		//获取到行的第一列 ；
		XWPFTableCell cell = row.getCell(0);
		List<XWPFParagraph> cellParList = cell.getParagraphs();
		for (int p = 0; cellParList != null && p < cellParList.size(); p++){
			List<XWPFRun> runs = cellParList.get(p).getRuns(); 
			for (int q = 0; runs != null && q < runs.size(); q++){
				String oneparaString = runs.get(q).getText(runs.get(q).getTextPosition());
				if (oneparaString != null){
					if (oneparaString.equals("«"+str+"»")){
						//循环lists，得到list，每一个list里边为一个表格的数据
						for (List<Map<String, String>> listMap: lists){
							//循环表格的List数据，得到Map
							for (Map<String, String> map:listMap){
								//迭代map数据拿到key value；
								Iterator<Entry<String, String>> iter = map.entrySet().iterator();
								Entry<String, String> entry = iter.next();
								//如果当前表格匹配上，进行行的添加和数据的插入
								if(entry.getKey().equals(str)) {
									try{
										insertValueToTabl(table,listMap,tmpRow);
									} catch (Exception e){
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									break;
								}
							}
						}
						break;
					}
				}
			}
		}

	}
	
	/**
	 * 行的添加和数据的插入
	 * @param table     //当前table
	 * @param listMap	//当前table需要填充的数据
	 * @param tmpRow    //模板的row，用于获取样式
	 * @throws Exception
	 */
	private void insertValueToTabl(XWPFTable table,List<Map<String, String>> listMap,XWPFTableRow tmpRow) throws Exception {
		//模板行，为了取到row 和cell的样式 
		List<XWPFTableCell> tmpCells = tmpRow.getTableCells();
		//循环需要插入的数据，组建为单个list<Map<String,String>>
		for (Map<String, String> map:listMap){
			List<Map<String,String>> list= new ArrayList<>();
			for (Map.Entry<String, String> entry:map.entrySet()){
				Map<String,String> t_map= new LinkedHashMap<>();
				t_map.put(entry.getKey(), entry.getValue());
				list.add(t_map);
			}
			//创建一个新的行  createRow
			XWPFTableRow row = table.createRow();
			//注意：createRow，新增一行的列数，是按照表格第一行的列数来创建的。因为模板中第一行只有2列，不符合我们的需求，所以需要再额外创建 列
			//int rowsize  为需要再添加的列数
			int rowsize=list.size()-row.getTableCells().size();
			for (int i = 0; i < rowsize && rowsize >0; i++){
				row.addNewTableCell();
			}
			//设置新建行的高度（同模板的高度一样）
			row.setHeight(tmpRow.getHeight());
			List<XWPFTableCell> cells = row.getTableCells();
            // 插入的行会填充与表格第一行相同的列数
			for (int i = 0; i < list.size(); i++){
				//由于 list的 size  和 List<XWPFTableCell> cells的 size 相等，所以可以直接用一个索引
				XWPFTableCell tmpCell = tmpCells.get(i);
				XWPFTableCell cell=cells.get(i); 
				//迭代map 把map的value 插入到数据中
				for (Map.Entry<String, String> entry:list.get(i).entrySet()) {
					setCellText(tmpCell, cell,entry.getValue());
				}
			}
        }
		// 删除模版行 
		int index=table.getRows().size()-listMap.size()-1;
		table.removeRow(index);
	}

	/**
	 * 往新建行中插入数据
	 * @param tmpCell	模板列，用于获取列的样式风格等等
	 * @param cell		需要插入数据的新建列
	 * @param text		需要插入的数据
	 * @throws Exception
	 */
	private void setCellText(XWPFTableCell tmpCell, XWPFTableCell cell, String text)throws Exception {
		// TODO Auto-generated method stub
		CTTc cttc2 = tmpCell.getCTTc();
		CTTcPr ctPr2 = cttc2.getTcPr();
		
		CTTc cttc = cell.getCTTc();
		CTTcPr ctPr = cttc.addNewTcPr();
		cell.setColor(tmpCell.getColor());
		cell.setVerticalAlignment(tmpCell.getVerticalAlignment());
		if (ctPr2.getTcW() != null) {
		    ctPr.addNewTcW().setW(ctPr2.getTcW().getW());
		}
		if (ctPr2.getVAlign() != null) {
		    ctPr.addNewVAlign().setVal(ctPr2.getVAlign().getVal());
		}
		if (cttc2.getPList().size() > 0) {
		    CTP ctp = cttc2.getPList().get(0);
		    if (ctp.getPPr() != null) {
		        if (ctp.getPPr().getJc() != null) {
		            cttc.getPList().get(0).addNewPPr().addNewJc().setVal(
		                    ctp.getPPr().getJc().getVal());
		        }
		    }
		}
		if (ctPr2.getTcBorders() != null) {
		    ctPr.setTcBorders(ctPr2.getTcBorders());
		}
		XWPFParagraph cellP = cell.getParagraphs().get(0);
		XWPFRun cellR = cellP.createRun();
		cellR.setText(text);
	}

	/**
	 * 根据物种信息进行数据组装
	 * @param sjwzxxList	物种信息参数
	 * @return
	 */
	private List<List<Map<String, String>>> creatSjwzxxList(List<SjwzxxDto> sjwzxxList){
		List<List<Map<String, String>>> creatTablelist = new ArrayList<>();
		// 区分DNA病毒，RNA病毒，细菌，真菌，寄生虫
		List<Map<String, String>> viruses_D_List = new ArrayList<>();// DNA病毒
																						// List<Map<String,String>>
		List<Map<String, String>> viruses_R_List = new ArrayList<>();// RNA病毒
																						// List<Map<String,String>>
		List<Map<String, String>> parasiteList = new ArrayList<>();// 寄生虫
																						// List<Map<String,String>>
		List<Map<String, String>> bacteriaList = new ArrayList<>();// 细菌
																						// List<Map<String,String>>
		List<Map<String, String>> fungiList = new ArrayList<>();// 真菌
																					// List<Map<String,String>>
		for (SjwzxxDto sjwzxxDto : sjwzxxList){
			if (sjwzxxDto.getWzfl().equals("Viruses") && sjwzxxDto.getWzfllx().equals("DNA")){// DNA病毒
				Map<String, String> maps = new LinkedHashMap<>();
				maps.put("genus_dna_virus_name_merged", sjwzxxDto.getSm());
				maps.put("genus_dna_virus_reads_accum", sjwzxxDto.getSdds());
				maps.put("genus_dna_virus_abundance", sjwzxxDto.getSfd());
				maps.put("dna_virus_name_merged", sjwzxxDto.getWzzwm() + sjwzxxDto.getWzywm());
				maps.put("dna_virus_reads_accum", sjwzxxDto.getDqs());
				maps.put("dna_virus_abundance", sjwzxxDto.getXdfd());
				viruses_D_List.add(maps);
			}
			if (sjwzxxDto.getWzfl().equals("Viruses") && sjwzxxDto.getWzfllx().equals("RNA")){// RNA病毒
				Map<String, String> maps = new LinkedHashMap<>();
				maps.put("genus_rna_virus_name_merged", sjwzxxDto.getSm());
				maps.put("genus_rna_virus_reads_accum", sjwzxxDto.getSdds());
				maps.put("genus_rna_virus_abundance", sjwzxxDto.getSfd());
				maps.put("rna_virus_name_merged", sjwzxxDto.getWzzwm() + sjwzxxDto.getWzywm());
				maps.put("rna_virus_reads_accum", sjwzxxDto.getDqs());
				maps.put("rna_virus_abundance", sjwzxxDto.getXdfd());
				viruses_R_List.add(maps);
			}
			if (sjwzxxDto.getWzfl().equals("Parasite")){// 寄生虫
				Map<String, String> maps = new LinkedHashMap<>();
				maps.put("genus_parasite_name_merged", sjwzxxDto.getSm());
				maps.put("enus_parasite_reads_accum", sjwzxxDto.getSdds());
				maps.put("genus_parasite_abundance", sjwzxxDto.getSfd());
				maps.put("parasite_name_merged", sjwzxxDto.getWzzwm() + sjwzxxDto.getWzywm());
				maps.put("parasite_reads_accum", sjwzxxDto.getDqs());
				maps.put("parasite_abundance", sjwzxxDto.getXdfd());
				parasiteList.add(maps);
			}
			if (sjwzxxDto.getWzfl().equals("Bacteria")){// 细菌
				Map<String, String> maps = new LinkedHashMap<>();
				maps.put("gram_stain", sjwzxxDto.getWzfllx());
				maps.put("genus_bac_name_merged", sjwzxxDto.getSm());
				maps.put("genus_bac_reads_accum", sjwzxxDto.getSdds());
				maps.put("genus_bac_abundance", sjwzxxDto.getSfd());
				maps.put("bac_name_merged", sjwzxxDto.getWzzwm() + sjwzxxDto.getWzywm());
				maps.put("bac_reads_accum", sjwzxxDto.getDqs());
				maps.put("bac_abundance", sjwzxxDto.getXdfd());
				bacteriaList.add(maps);
			}
			if (sjwzxxDto.getWzfl().equals("Fungi")){// 真菌
				Map<String, String> maps = new LinkedHashMap<>();
				maps.put("genus_fungi_name_merged", sjwzxxDto.getSm());
				maps.put("genus_fungi_reads_accum", sjwzxxDto.getSdds());
				maps.put("genus_fungi_abundance", sjwzxxDto.getSfd());
				maps.put("fungi_name_merged", sjwzxxDto.getWzzwm() + sjwzxxDto.getWzywm());
				maps.put("fungi_reads_accum", sjwzxxDto.getDqs());
				maps.put("fungi_abundance", sjwzxxDto.getXdfd());
				fungiList.add(maps);
			}
		}
		creatTablelist.add(viruses_D_List);
		creatTablelist.add(viruses_R_List);
		creatTablelist.add(parasiteList);
		creatTablelist.add(bacteriaList);
		creatTablelist.add(fungiList);
		return creatTablelist;
	}

	/**
	 * 送检信息数据的组装
	 * @param sjwzxxList	送检物种信息
	 * @param map			用于组装数据的map
	 * @param sjbgsmDto	  	送检报告信息
	 * @return
	 */
	private List<Map<String, String>> creatSjxx(List<SjwzxxDto> sjwzxxList, Map<String, Object> map,SjbgsmDto sjbgsmDto){
		List<Map<String, String>> listMap = new ArrayList<>();
		// 单行数据填充
		String pathogens = "";
		String possible = "";
		for (int i = 0; i < sjwzxxList.size(); i++){
			if (sjwzxxList.get(i).getJglx().equals("pathogen")){
				pathogens = pathogens + sjwzxxList.get(i).getWzzwm() + ";";
			} else if (sjwzxxList.get(i).getJglx().equals("possible")){
				possible = possible + sjwzxxList.get(i).getWzzwm() + ";";
			}
		}
		map.put("pathogens", pathogens);
		map.put("possible", possible);
		map.put("refs",sjbgsmDto.getCkwx());
		map.put("pathogen_comment",sjbgsmDto.getGgzdsm());
		map.put("possible_comment",sjbgsmDto.getYssm());
		for (String string : map.keySet()){
			Map<String, String> mapNew = new HashMap<>();
			if(map.get(string)!=null) {
				mapNew.put(string, map.get(string).toString());
				listMap.add(mapNew);
			}
		}
		return listMap;
	}
	
	
	/**
	 * 清除掉空的row，显示为 --
	 * @param table
	 */
	private void clearNullCell(XWPFTable table) {
		int countRow=table.getNumberOfRows();
		for (int i = 0; i < countRow; i++){
			XWPFTableRow row=table.getRow(i);
			List<XWPFTableCell> cells=row.getTableCells();
			for (XWPFTableCell cell:cells){
				List<XWPFParagraph> cellParagraph=cell.getParagraphs();
				for (int j = 0; j < cellParagraph.size()&&cellParagraph!=null; j++){
					List<XWPFRun> runs= cellParagraph.get(j).getRuns();
					for (int k = 0; k < runs.size()&& runs!=null; k++){
						String text=runs.get(k).getText(runs.get(k).getTextPosition());
						if(text!=null) {
							String heard=text.substring(0, 1);
							String boot=text.substring(text.length()-1,text.length());
							if(heard!=null&&boot!=null) {
								if(heard.equals("«")&&boot.equals("»")) {
									text=text.replace(text, "--");
								}
							}
						}
						runs.get(k).setText(text, 0);
					}
				}
			}
		}
	}
	
	/**
	 * 替换文本中的信息，不在table内的信息
	 * @param paragraph	当前段落
	 * @param listMap	替换数据	
	 */
	private void replaceText(XWPFParagraph  paragraph,List<Map<String, String>> listMap) {
		List<XWPFRun> runs=paragraph.getRuns();
		for (int i = 0; i < runs.size()&&runs!=null; i++){
			String text=runs.get(i).getText(runs.get(i).getTextPosition());
			if(text!=null) {
				for (Map<String, String> map:listMap){
					//迭代map数据拿到key value；
					Iterator<Entry<String, String>> iter = map.entrySet().iterator();
					Entry<String, String> entry = iter.next();
					//如果当前表格匹配上，进行行的添加和数据的插入
					if(text.equals("«"+entry.getKey()+"»")) {
						text=text.replace(text,entry.getValue());
					}
				}
				runs.get(i).setText(text, 0);
			}
		}
	}
	
	/**
	 * 合并相同的行
	 * @param table
	 */
	private void colspan(XWPFTable table) {
		int countRow=table.getNumberOfRows();
		for(int i =0 ;i < countRow; i++) {//每一行
			if(i==countRow-1) {
				break;
			}else {
				XWPFTableRow row=table.getRow(i);
				List<XWPFTableCell> cells=row.getTableCells();
				for (int j = 0; j < cells.size(); j++){
					XWPFTableCell cell0=table.getRow(i).getCell(j);
					XWPFTableCell cell1=table.getRow(i+1).getCell(j);
					if(cell0.getText().equals(cell1.getText())) {
						cell0.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
						cell1.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE); 
					}
				}
			}
		}
	}
	
	/**
	 * 需要添加多条的table（用于判断用，和模板中的字段进行匹配用）
	 * @return
	 */
	private  List<String> codeKey(){
		//只需要每行第一个来进行匹配就行
		List<String> codeList= new ArrayList<>();
		codeList.add("genus_dna_virus_name_merged");
		codeList.add("genus_rna_virus_name_merged");
		codeList.add("gram_stain");
		codeList.add("genus_fungi_name_merged");
		codeList.add("genus_parasite_name_merged");
		codeList.add("related_gene");
		codeList.add("bg_gram_stain");
		return codeList;
	}
}
