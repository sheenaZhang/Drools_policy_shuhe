package cn.shuhe.risk.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.shuhe.risk.kie.RoolEngine;
import cn.shuhe.risk.utils.ReadCsv;

public class PricePredict extends SubScore{
	
	public static void exc_csv(String month) throws Exception{
		// 初始参数
		String score_2_path = "subScore4/history/score_4_bos_v1.0.2";
//		String score_2_path = "subScore2/test/score_pred_bos_v3.0.0";
		String csv_path = "/Users/timberwang/Documents/javaworkspace/policy/files/no_rh/train_no_rh_new_" + month + ".csv";
		String[] paths = new String[]{score_2_path};
		System.out.println(month + " read start...");
		List<Map<String, Object>> input_data = ReadCsv.read(csv_path);
		System.out.println(month + " read done...");
		List<String[]> res = new ArrayList<String[]>();
		int counts = 1;
		for(Map<String, Object> data : input_data){
			String uid = (String) data.get("uid");
//			if(!"a5e09800-1da7-405c-bd7d-8c9418f29566".equals(uid))
//				continue;
			// 对data进行处理
			Map<String, Object> facts = getFacts(data);
			// 执行drools
			for(String path : paths){
				Map<String, Object> check = RoolEngine.ruleexc(path, facts);
//				System.out.println(JSON.toJSONString(check));
				Double score_2 = Double.parseDouble(String.valueOf(check.get("score_4")));
				String group_2 = (String)check.get("group_4");
				String[] contents = new String[3];
				contents[0] = uid;
				contents[1] = String.valueOf(score_2);
				contents[2] = group_2;
				res.add(contents);
			}
			System.out.println(counts++);
		}
		csv_path = "/Users/timberwang/Documents/javaworkspace/policy/files/price_check/train_no_rh_" + month + "_res_4.csv";
		ReadCsv.write(csv_path, new String[]{"uid", "score", "group"}, res);
	}

}
