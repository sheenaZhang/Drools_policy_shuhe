package cn.shuhe.risk.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import cn.shuhe.risk.kie.RoolEngine;
import cn.shuhe.risk.utils.ReadCsv;

public class WrhPredict extends SubScore{
	
	/***
	 * 无人行模型测试
	 * @param month
	 * @throws Exception
	 */
	public static void exc_csv(String month) throws Exception{
		// 初始参数
		String score_2_path = "subScoreWrh/score_wrh_test_V1.0.0";
		String csv_path = "/Users/timberwang/Documents/javaworkspace/policy/files/no_rh/train_no_rh_new.csv";
		String[] paths = new String[]{score_2_path};
		System.out.println(month + " read start...");
		List<Map<String, Object>> input_data = ReadCsv.read(csv_path);
		System.out.println(month + " read done...");
		List<String[]> res = new ArrayList<String[]>();
		int counts = 1;
		for(Map<String, Object> data : input_data){
			String uid = (String) data.get("uid");
			if(!"d1c22000-45bf-4c46-8b91-bc10aaf94221".equals(uid))
				continue;
			// 对data进行处理
			Map<String, Object> facts = getFacts(data);
			System.out.println(JSON.toJSON(facts));
			// 执行drools
			for(String path : paths){
				Map<String, Object> check = RoolEngine.ruleexc(path, facts);
//				System.out.println(JSON.toJSONString(check));
				Double score_2 = Double.parseDouble(String.valueOf(check.get("score_wrh")));
				String group_2 = (String)check.get("group_wrh");
				String[] contents = new String[3];
				contents[0] = uid;
				contents[1] = String.valueOf(score_2);
				contents[2] = group_2;
				res.add(contents);
			}
			System.out.println(counts++);
		}
		csv_path = "/Users/timberwang/Documents/javaworkspace/policy/files/price_pred/train_no_rh_new_res_wrh.csv";
		ReadCsv.write(csv_path, new String[]{"uid", "score", "group"}, res);
	}

}
