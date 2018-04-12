package cn.shuhe.risk.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import cn.shuhe.risk.kie.RoolEngine;
import cn.shuhe.risk.utils.ReadCsv;

public class RuleAdjust extends SubScore{
	public static void exc_csv() throws Exception{
		// 初始参数
		String score_2_path = "perm/perm_v1.2.0";
		String csv_path = "/Users/timberwang/Documents/javaworkspace/policy/files/ruleAdj/rule_check_perm.csv";
		String[] paths = new String[]{score_2_path};
		List<Map<String, Object>> input_data = ReadCsv.read(csv_path);
		List<String[]> res = new ArrayList<String[]>();
		int counts = 1;
		for(Map<String, Object> data : input_data){
			String uid = (String) data.get("uid");
//			if(!"a5e09800-1da7-405c-bd7d-8c9418f29566".equals(uid))
//				continue;
			// 对data进行处理
			Map<String, Object> facts = getFacts(data);
//			System.out.println(JSON.toJSONString(facts));
			// 执行drools
			for(String path : paths){
				Map<String, Object> check = RoolEngine.ruleexc(path, facts);
				//System.out.println(check);
				String flag = (String)check.get("flag");
				String retCode = (String)check.get("retCode");
				String[] contents = new String[3];
				contents[0] = uid;
				contents[1] = String.valueOf(flag);
				contents[2] = retCode;
				res.add(contents);
			}
			System.out.println(counts++);
		}
		csv_path = "/Users/timberwang/Documents/javaworkspace/policy/files/ruleAdj/rule_check_perm_res.csv";
		ReadCsv.write(csv_path, new String[]{"uid", "flag", "retCode"}, res);
	}
	
	
	/**
	 * 将数据转换为数据项
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getFacts(Map<String, Object> data){
		Map<String, Object> res = new HashMap<String, Object>();
		Object uid = data.get("uid");
//		System.out.println(uid + ":" + JSON.toJSONString(data));
		res.put("uid", uid);
		// applyInfo
		Map<String, Object> applyInfo = new HashMap<String, Object>();
		applyInfo.put("province", data.get("province"));
		applyInfo.put("city", data.get("city"));
		applyInfo.put("academic", data.get("academic"));
		applyInfo.put("identificationNo", data.get("identificationNO"));
		applyInfo.put("limits", Integer.parseInt(String.valueOf(data.get("limits"))));
		applyInfo.put("createdAt", Long.parseLong(String.valueOf(data.get("createdAt"))));
		applyInfo.put("mobile", data.get("mobile"));
		applyInfo.put("cardApplyDate", Long.parseLong(String.valueOf(data.get("cardApplyDate"))));
		applyInfo.put("cardDate", Long.parseLong(String.valueOf(data.get("cardDate"))));
		applyInfo.put("sensitiveUserContactCount", data.get("sensitiveUserContactCount"));
		applyInfo.put("sameDeviceUserCount", data.get("sameDeviceUserCount"));
		applyInfo.put("ifExistSensitiveWord", data.get("ifExistSensitiveWord"));
		applyInfo.put("userContactCount", data.get("userContactCount"));
		applyInfo.put("decisionSetId", data.get("decisionSetId"));
		
		// jxlRdtReport
		Map<String, Object> jxlRdtReport = new HashMap<String, Object>();
		jxlRdtReport.put("version", data.get("version"));

		// jxlApplicationCheckSum
		Map<String, Object> jxlApplicationCheckSum = new HashMap<String, Object>();
		jxlApplicationCheckSum.put("matchIdCard", data.get("matchIdCard"));
		jxlApplicationCheckSum.put("matchName", data.get("matchName"));
		jxlApplicationCheckSum.put("reliability", data.get("reliability"));
		jxlApplicationCheckSum.put("financialBlacklistArised", data.get("financialBlacklistArised"));
		jxlApplicationCheckSum.put("courtBlacklistArised", data.get("courtBlacklistArised"));
		
		// jxlRdtCheckPointSum
		Map<String, Object> jxlRdtCheckPointSum = new HashMap<String, Object>();
		jxlRdtCheckPointSum.put("lawyerContact", data.get("lawyerContact"));
		jxlRdtCheckPointSum.put("courtContact", data.get("courtContact"));
		jxlRdtCheckPointSum.put("phoneUsageTime", data.get("phoneUsageTime"));
		jxlRdtCheckPointSum.put("loanPhoneNum", data.get("loanPhoneNum"));
		jxlRdtCheckPointSum.put("loanCallPhoneNum", data.get("loanCallPhoneNum"));
		
		// jxlRdtContactSum
		Map<String, Object> jxlRdtContactSum = new HashMap<String, Object>();
		jxlRdtContactSum.put("diContactNum", data.get("diContactNum"));
		jxlRdtContactSum.put("contact1Num", data.get("contact1Num"));
		jxlRdtContactSum.put("contact2Num", data.get("contact2Num"));
		
		Object jxlTelMatch = data.get("jxlTelMatch");

		res.put("uid", uid);
		res.put("applyInfo", applyInfo);
		res.put("jxlRdtReport", jxlRdtReport);
		res.put("jxlApplicationCheckSum", jxlApplicationCheckSum);
		res.put("jxlRdtCheckPointSum", jxlRdtCheckPointSum);
		res.put("jxlRdtContactSum", jxlRdtContactSum);
		res.put("jxlTelMatch", jxlTelMatch);

		// apply time line && thr_mont_apply
		Map<String, Object> listMap = new HashMap<String, Object>();
		listMap = getListMap(data);
		res.putAll(listMap);

		String json = JSON.toJSONString(res);
		//		System.out.println(json);
		res = JSON.parseObject(json, Map.class);
		return res;
	}
	
	
	/**
	 * 构建复杂的输入项
	 * @param map
	 * @return
	 */
	public static Map<String, Object> getListMap(Map<String, Object> map){
		Map<String, Object> res = new HashMap<String, Object>();

		// 7天申请次数
		Integer muti_apply_07 = map.get("muti_apply_07") == null ? 0 : (int) Double.parseDouble(String.valueOf(map.get("muti_apply_07")));
		Integer muti_apply_30 = map.get("muti_apply_30") == null ? 0 : (int) Double.parseDouble(String.valueOf(map.get("muti_apply_30")));
		Integer muti_apply_90 = map.get("muti_apply_90") == null ? 0 : (int) Double.parseDouble(String.valueOf(map.get("muti_apply_90")));
		Integer level_high_cnts = map.get("level_high_cnts") == null ? 0 : (int) Double.parseDouble(String.valueOf(map.get("level_high_cnts")));
	
		List<Map<String, Object>> tndPrsRiskItemsList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map7 = new HashMap<String, Object>();
		map7.put("itemId", "1072220");
		map7.put("platformCount", muti_apply_07);
		tndPrsRiskItemsList.add(map7);
		
		Map<String, Object> map30 = new HashMap<String, Object>();
		map30.put("itemId", "1072222");
		map30.put("platformCount", muti_apply_30);
		tndPrsRiskItemsList.add(map30);
		
		Map<String, Object> map90 = new HashMap<String, Object>();
		map90.put("itemId", "1072224");
		map90.put("platformCount", muti_apply_90);
		tndPrsRiskItemsList.add(map90);
		
		if(level_high_cnts > 0){
			Map<String, Object> map_cnts = new HashMap<String, Object>();
			map_cnts.clear();
			map_cnts.put("itemId", "1072076");
			tndPrsRiskItemsList.add(map_cnts);
		}

		res.put("tndPrsRiskItemsList", tndPrsRiskItemsList);
		
		Integer finalScore = map.get("finalScore") == null ? 0 : (int) Double.parseDouble(String.valueOf(map.get("finalScore")));
		Map<String, Object> tndPrsBasic = new HashMap<String, Object>();
		tndPrsBasic.put("finalScore", finalScore);
		res.put("tndPrsBasic", tndPrsBasic);
		return res;
	}
}
