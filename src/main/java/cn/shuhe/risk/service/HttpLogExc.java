package cn.shuhe.risk.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.shuhe.risk.kie.RoolEngine;
import cn.shuhe.risk.utils.ReadCsv;

public class HttpLogExc {
	
	@SuppressWarnings("unchecked")
	public static void exc_csv() throws Exception{
		// 初始参数
		String score_common_path = "subScore5/score_4_common_v1.0.1";
		String score_bosc_path = "subScore5/score_4_bos_v1.0.1";
		String score_hengfeng_path = "subScore5/score_4_hf_v1.0.0";
		
		Map<String, String> pathMapper = new HashMap<String, String>();
		pathMapper.put("RH", score_common_path);
		pathMapper.put("HENG_FENG", score_hengfeng_path);
		pathMapper.put("BOSC", score_bosc_path);
		
		String csv_path = "/Users/timberwang/Documents/gitprod/subscore/files/reaudit_input.csv";

		List<Map<String, Object>> input_data = ReadCsv.read(csv_path);
		List<String[]> res = new ArrayList<String[]>();
		int counts = 1;
		for(Map<String, Object> data : input_data){
			String day = (String) data.get("day");
			String strFacts = (String) data.get("facts");
			// 对data进行处理
			Map<String, Object> facts = (Map<String, Object>) JSONObject.parseObject(strFacts, Map.class);
			String rhSource = (String) facts.get("rhSource");
			
			Map<String, Object> cardInfo = (Map<String, Object>) facts.get("cardInfo");
			String uid = (String) cardInfo.get("uid");
			// 执行drools
			String path = pathMapper.get(rhSource);
			Map<String, Object> check = RoolEngine.ruleexc(path, facts);
			Double score_2 = Double.parseDouble(String.valueOf(check.get("score_4")));
			String group_2 = (String)check.get("group_4");
			String[] contents = new String[4];
			contents[0] = uid;
			contents[1] = String.valueOf(score_2);
			contents[2] = group_2;
			contents[3] = day;
			res.add(contents);
			System.out.println(counts++);
		}
		csv_path = "/Users/timberwang/Documents/gitprod/subscore/files/reaudit_input_results.csv";
		ReadCsv.write(csv_path, new String[]{"uid", "score_4", "group_4", "day"}, res);
	}

}
