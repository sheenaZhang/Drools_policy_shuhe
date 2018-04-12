package cn.shuhe.risk.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import cn.shuhe.risk.kie.RoolEngine;
import cn.shuhe.risk.utils.ReadCsv;

public class CreditXModel extends SubScore{
	
	public static void exc_csv(String month) throws Exception{
		// 初始参数
		String score_2_path = "subScore4/score_4_bos_v1.1.2";
//		String score_2_path = "subScore6/score_6_bos_v1.0.0";
		
		String csv_path = "/Users/timberwang/Documents/javaworkspace/policy/files/model4/ascore_month_9.csv";
		String[] paths = new String[]{score_2_path};
		System.out.println(month + " read start...");
		List<Map<String, Object>> input_data = ReadCsv.read(csv_path);
		System.out.println(month + " read done...");
		List<String[]> res = new ArrayList<String[]>();
		int counts = 1;
		for(Map<String, Object> data : input_data){
			String uid = (String) data.get("uid");
//			if(!"3e97a35d-84c6-4f89-96fa-85470c42634c".equals(uid))
//				continue;
			// 对data进行处理
			Map<String, Object> facts = getFacts(data);
			System.out.println(JSON.toJSON(facts));
			// 执行drools
			for(String path : paths){
				try {
					Map<String, Object> check = RoolEngine.ruleexc(path, facts);
//				System.out.println(JSON.toJSONString(check));
					Double score_2 = Double.parseDouble(String.valueOf(check.get("score_6")));
					String group_2 = (String)check.get("group_6");
					String[] contents = new String[3];
					contents[0] = uid;
					contents[1] = String.valueOf(score_2);
					contents[2] = group_2;
					res.add(contents);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(facts);
					System.out.println(JSON.toJSON(data));
					e.printStackTrace();
				}
			}
			System.out.println(counts++);
		}
		csv_path = "/Users/timberwang/Documents/javaworkspace/policy/files/creditx/train_all_s_cut_1608_res_h5.csv";
		ReadCsv.write(csv_path, new String[]{"uid", "score", "group"}, res);
	}

}
