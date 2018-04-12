package cn.shuhe.risk.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

public class ReadCsv {
	private static boolean underlineReplace = true;;
	
	/**
	 * 给定csv路径，读取文件
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static List<Map<String, Object>> read(String path) throws IOException{
		CsvReader r = new CsvReader(path, ',',Charset.forName("utf-8"));
        //读取表头
		r.readHeaders();
		String[] headers = r.getHeaders();
		List<String> headers_list = new ArrayList<String>();
		for(String header: headers){
			headers_list.add(header);
		}
		List<Map<String, Object>> scoreResults = new ArrayList<>();
        //逐条读取记录，直至读完
        while (r.readRecord()) {
        	Map<String, Object> map = new HashMap<String, Object>();
        	r.getRawRecord();
        	for(String head : headers_list){
        		if("NULL".equals(String.valueOf(r.get(head))) || "null".equals(String.valueOf(r.get(head))) || "".equals(String.valueOf(r.get(head)))){
        			map.put(head, null);
        		}else{
        			map.put(head, r.get(head));
        		}	
        	}
        	scoreResults.add(map);
        }
        r.close();
		return scoreResults;
	}
	
	/**
	 * 将内容写到指定的csv文件中
	 * @param path
	 * @param header
	 * @param list
	 * @throws IOException
	 */
	public static void write(String path, String[] header, List<String[]> list) throws IOException{
		CsvWriter wr =new CsvWriter(path,',',Charset.forName("utf-8"));
//		String[] header = {"uid", "apply_at", "law_status", "identification_no", "score_b", "score_a", "score_d", "group_b", "group_a", "group_d", "education_degree", "academic"};
		wr.writeRecord(header);
		for(String[] contents : list){                  
			wr.writeRecord(contents);
		}
		System.out.println("文件写入结束");
		wr.close();
	}
	
	@SuppressWarnings("unused")
	private static String getName(String name,String  anotherName) {
    	/**
    	 * 去掉下划线，并将下划线后的首字母大写
    	 */
		name=anotherName;
		//如果最后一个是_ 不做转换
		if(name.indexOf("_") > 0 && name.length() != name.indexOf("_") + 1){
			int lengthPlace = name.indexOf("_");
			name = name.replaceFirst("_", "");
			String s = name.substring(lengthPlace, lengthPlace+1);
			s = s.toUpperCase();
			anotherName = name.substring(0,lengthPlace) + s + name.substring(lengthPlace + 1);
		}else{
			return anotherName;
		}
		return getName(name,anotherName);
	}

}
