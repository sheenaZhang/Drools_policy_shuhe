package cn.shuhe.risk.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

import cn.shuhe.risk.db.JdbcHelper;
import cn.shuhe.risk.kie.RoolEngine;
import cn.shuhe.risk.utils.ReadCsv; 

public class SubScore {
//	private static Logger logger = Logger.getLogger(SubScore.class);

	/**
	 * 根据数据库中数据执行drools文件，并将执行结果插入到数据库中
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static void exc() throws Exception{
		// 初始参数
		
		// 
		createTable();
		// 获取总的数据条数
		String sql = "select count(1) from datahub.ascore_features_sub_score_left;";
		Long length = (Long)JdbcHelper.getSingle(sql);
//		logger.info("共有数据：" + length + "条！");
		int step = 1000;
		// 每次读取1000条
		for(int i=0; i<Math.ceil(length * 1.0/step); i++){
			int offset = i*step;
//			logger.info("开始处理数据第：" + offset + "条");
			sql = "select * from datahub.ascore_features_sub_score_left order by etl_id asc limit " + offset + "," + step;
			//			sql = "select * from risk_model.ascore_features_sub_score_left where uid = '74de68fa-515a-4cbd-b2e1-f5d347c5d610'";
			List<Map<String, Object>> dataSet = JdbcHelper.query(sql);
			// 获取drools结果
			List<String> sqlList = inputInit(dataSet);
//			System.out.println(JSON.toJSON(sqlList));
			// 批量更新数据
			if(sqlList.size() > 0){
				JdbcHelper.batchUpdate(sqlList);
//				logger.info("保存数据：" + sqlList.size() + "条！");
			}

		}
	}

	public static void exc_csv(String month) throws Exception{
		// 初始参数
//		String score_2_path = "subScore4/score_4_bos_v1.1.2";
		String score_2_path = "subScore7/score_7_common_v1.0.2";
//		String score_2_path = "subScore6/score_6_common_v1.0.0";
//		String csv_path = "/Users/timberwang/Documents/javaworkspace/policy/files/no_rh/train_no_rh_new_" + month + ".csv";
		String csv_path = "/Users/timberwang/Documents/javaworkspace/policy/files/model_check/rh/creditx_rh_report_month4_fix.csv";
		String[] paths = new String[]{score_2_path};
		System.out.println(month + " read start...");
		List<Map<String, Object>> input_data = ReadCsv.read(csv_path);
		System.out.println(month + " read done...");
		List<String[]> res = new ArrayList<String[]>();
		int counts = 1;
		for(Map<String, Object> data : input_data){
			String uid = (String) data.get("uid");
			String group_2 = (String) data.get("plan_max_exceed_day");
			String score_2 = (String) data.get("score_2");
//			if(!"a5e09800-1da7-405c-bd7d-8c9418f29566".equals(uid))
//				continue;
			// 对data进行处理
			Map<String, Object> facts = getFacts(data);
			System.out.println(JSON.toJSONString(facts));
			// 执行drools
			for(String path : paths){
				Map<String, Object> check = RoolEngine.ruleexc(path, facts);
//				System.out.println(JSON.toJSONString(check));
				Double score_7 = Double.parseDouble(String.valueOf(check.get("score_7")));
				String group_7 = (String)check.get("group_7");
				String[] contents = new String[5];
				contents[0] = uid;
				contents[1] = String.valueOf(score_2);
				contents[2] = group_2;
				contents[3] = String.valueOf(score_7);
				contents[4] = group_7;
				res.add(contents);
			}
			System.out.println(counts++);
		}
		csv_path = "/Users/timberwang/Documents/javaworkspace/policy/files/model_check/rh/creditx_rh_report_month4_wrh_out.csv";
		ReadCsv.write(csv_path, new String[]{"uid", "plan_max_exceed_day", "group_2", "score_7", "group_7"}, res);
	}

	/**
	 * 执行drools并将结果写入到数据库中
	 * @param dataSet
	 * @param paths
	 * @return
	 * @throws Exception 
	 */
	public static List<String> inputInit(List<Map<String, Object>> dataSet) throws Exception{
		List<String> sqlList = new ArrayList<String>();

		Set<String> exc_uids = new HashSet<String>();

		for(Map<String, Object> data : dataSet){
			String score_2_path = "subScore2/test/score_2_bos_v3.0.0";
			String score_4_path = "subScore4/test/score_4_bos_v1.0.2";
			
			String uid = (String) data.get("uid");
			Object name = data.get("name");
			if(name == null){
//				logger.error(uid + " --> stepover--> " + JSON.toJSONString(data));
				continue;
			}
			String edulevel = String.valueOf(data.get("edulevel"));
			if(edulevel == null || "".equals(edulevel)){
				edulevel = String.valueOf(data.get("loanauditquerytimeslatest3m"));
			}
			if("A".equals(edulevel) || "B".equals(edulevel) || "C".equals(edulevel) || "D".equals(edulevel) || "E".equals(edulevel) || "F".equals(edulevel) || "G".equals(edulevel)){
				score_2_path = "subScore2/test/score_2_bos_v3.1.1";
				score_4_path = "subScore4/test/score_4_bos_v1.1.2";
			}
			
			String group_4 = data.get("group_4") == null ? "0":String.valueOf(data.get("group_4"));
			Double score_4 = data.get("score_4") == null ? 0 : Double.parseDouble(String.valueOf(data.get("score_4")));
			String group_2 = data.get("group_2") == null ? "0" : String.valueOf(data.get("group_2"));
			Double score_2 = data.get("score_2") == null ? 0 : Double.parseDouble(String.valueOf(data.get("score_2")));
			
			
			// 对data进行处理
			Map<String, Object> facts = getFacts(data);
			
			// 执行drools
			Map<String, Object> res = new HashMap<String, Object>();
			
			//System.out.println(JSON.toJSON(data));
			if("0".equals(group_4)){
				try {
					Map<String, Object> check = RoolEngine.ruleexc(score_4_path, facts);
					group_4 = String.valueOf(check.get("group_4"));
					score_4 = Double.parseDouble(String.valueOf(check.get("score_4")));
				} catch (Exception e) {
					// TODO Auto-generated catch block
//					logger.error(e.toString());
//					logger.error(score_4_path);
//					logger.error(JSON.toJSON(facts));
					group_4 = "G_-1";
					score_4 = -1.0;
				}
			}
			
			
			if("0".equals(group_2)){
				try {
					Map<String, Object> check = RoolEngine.ruleexc(score_2_path, facts);
					group_2 = String.valueOf(check.get("group_2"));
					score_2 = Double.parseDouble(String.valueOf(check.get("score_2")));
				} catch (Exception e) {
					// TODO Auto-generated catch block
//					logger.error(e.toString());
//					logger.error(score_2_path);
//					logger.error(JSON.toJSON(facts));
					group_2 = "G_-1";
					score_2 = -1.0;
				}
			}
			
			if(exc_uids.contains(uid)){
//				logger.error(uid + " --> stepover--> " + JSON.toJSONString(data));
				continue;
			}

			res.put("group_2", group_2);
			res.put("group_4", group_4);
			res.put("score_2", score_2);
			res.put("score_4", score_4);
			// 记录输出结果
			String sql = getSqlList(res, data);
			sqlList.add(sql);
		}
		return sqlList;
	}

	/**
	 * 拼接插入语句
	 * @param check
	 * @return
	 */
	public static String getSqlList(Map<String, Object> check, Map<String, Object> data){
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO `default`.wtm_risk_subscore_all_left (uid,score_2,group_2,score_4,group_4,apply_at) VALUES (");
		String uid = (String) data.get("uid");
		Double score_2 = Double.parseDouble(String.valueOf(check.get("score_2")));
		String group_2 = (String)check.get("group_2");
		Double score_4 = Double.parseDouble(String.valueOf(check.get("score_4")));
		String group_4 = (String)check.get("group_4");
		Timestamp apply_at = (Timestamp) data.get("apply_at");

		sb.append("'" + uid + "',");
		sb.append(score_2 + ",");
		sb.append("'" + group_2 + "',");
		sb.append(score_4 + ",");
		sb.append("'" + group_4 + "',");
		sb.append("'" + apply_at + "'");
		sb.append(")");
		return sb.toString();
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
		applyInfo.put("identificationNO", data.get("identification_no"));
		applyInfo.put("email", data.get("email"));
		applyInfo.put("academic", data.get("academic"));
		applyInfo.put("sensitiveUserContactCount", data.get("sensitive_user_contact_count"));
		applyInfo.put("marriage", data.get("marriage"));
		applyInfo.put("userContactCount", data.get("user_contact_count") == null ? 0 : data.get("user_contact_count"));
		// degree
		Map<String, Object> pyEdu = new HashMap<String, Object>();
		pyEdu.put("degree", data.get("education_degree"));
		pyEdu.put("graduateTime", data.get("graduate_time"));
		//cardInfo
		Map<String, Object> cardInfo = new HashMap<String, Object>();
		cardInfo.put("bankName", data.get("bank_name"));
		cardInfo.put("creditLimit", data.get("card_limit"));
		// hfbQryLoan
		Map<String, Object> hfbQryLoan = new HashMap<String, Object>();
		hfbQryLoan.put("rhNewcrTime6m", data.get("creditloannewcountlatest6m"));
		hfbQryLoan.put("rhContactsum", data.get("loancontractamounttotal"));
		// hfbQryCreditCard
		Map<String, Object> hfbQryCreditCard = new HashMap<String, Object>();
		hfbQryCreditCard.put("rhCardnmbrActive", data.get("circlecreditcardcount"));
		hfbQryCreditCard.put("rhCreditcardCrinfoMonth", data.get("firstcreditcardmonthtonow"));
		hfbQryCreditCard.put("rhCrusg", data.get("creditcardcreditlimitusedrate"));
		hfbQryCreditCard.put("rhVar36", data.get("creditcardcreditlimit"));
		// hfbQryOverdue
		Map<String, Object> hfbQryOverdue = new HashMap<String, Object>();
		hfbQryOverdue.put("rhMaxM0112", data.get("creditcardoverduemaxstagelatest12m"));
		hfbQryOverdue.put("var46", data.get("creditcardoverduetimeslatest6m"));
		hfbQryOverdue.put("rhSumDueamnt", data.get("curroverdueamounttotal"));
		hfbQryOverdue.put("var47", data.get("creditcardoverduetimeslatest12m"));
		hfbQryOverdue.put("rhDebitOverdueNoM12", data.get("rh_debit_overdue_no_m12"));
		// hfbQryQueryInfo
		Map<String, Object> hfbQryQueryInfo = new HashMap<String, Object>();
		hfbQryQueryInfo.put("rhAppltimesCreditcard6mth", data.get("creditcardauditquerytimeslatest6m"));
		hfbQryQueryInfo.put("rhApplDe6m", data.get("loanauditquerytimeslatest6m"));
		hfbQryQueryInfo.put("rhApplDe3m", data.get("loanauditquerytimeslatest3m"));
		// 构建上海银行和通用人行的输入项
		Map<String, Object> boscPortraitData = new HashMap<String, Object>();
		boscPortraitData.put("firstLoancardOpenMonthCount", data.get("firstcreditcardmonthtonow"));
		boscPortraitData.put("latest6CreMonthOverCount", data.get("creditcardoverduetimeslatest6m"));
		boscPortraitData.put("latest12CreMonthHighOverCount", data.get("creditcardoverduemaxstagelatest12m"));
		boscPortraitData.put("aliveCardCount", data.get("circlecreditcardcount"));
		boscPortraitData.put("cardTotalAmount", data.get("creditcardcreditlimit"));
		boscPortraitData.put("creditLimitUseRate", data.get("creditcardcreditlimitusedrate"));
		boscPortraitData.put("countGrant6months", data.get("creditloannewcountlatest6m"));
		boscPortraitData.put("maxCreditCurrOverdueAmount", data.get("curroverdueamounttotal"));
		
		boscPortraitData.put("maxCreditCurrOverdueAmount", data.get("curroverdueamounttotal"));
		boscPortraitData.put("allLoanAmount", data.get("loancontractamounttotal"));
		boscPortraitData.put("firstCreditRecordMonths", data.get("firstcreditcardmonthtonow"));
		
		// 上海模型
		boscPortraitData.put("countLatest3MonthsQueryRecordLoan", data.get("loanauditquerytimeslatest3m"));
		boscPortraitData.put("countLatest6MonthsQueryRecordLoanCard", data.get("creditcardauditquerytimeslatest6m"));
		boscPortraitData.put("sumFinanceCorpCount", data.get("creditcardfinanceorgcount"));
		boscPortraitData.put("eduLevel", data.get("edulevel"));
		boscPortraitData.put("perHouseLoanAmount", data.get("personalhousingloancontractamounttotal"));
//		boscPortraitData.put("loanOverdueAccount12M", data.get("firstcreditcardmonthtonow"));
		boscPortraitData.put("latest12CreMonthOverCount", data.get("creditcardoverduetimeslatest12m"));
		boscPortraitData.put("latest6CreMonthHighOverCount", data.get("creditcardoverduemaxstagelatest6m"));
		
		Map<String, Object> rhReport = new HashMap<String, Object>();
		rhReport.put("firstCreditCardMonthToNow", data.get("firstcreditcardmonthtonow"));
		rhReport.put("creditCardOverdueTimesLatest6M", data.get("creditcardoverduetimeslatest6m"));
		rhReport.put("creditCardOverdueMaxStageLatest12M", data.get("creditcardoverduemaxstagelatest12m"));
		rhReport.put("loanAuditQueryTimesLatest3M", data.get("loanauditquerytimeslatest3m"));
		rhReport.put("loanAuditQueryTimesLatest6M", data.get("loanauditquerytimeslatest6m"));
		rhReport.put("circleCreditCardCount", data.get("circlecreditcardcount"));
		rhReport.put("creditCardCreditLimit", data.get("creditcardcreditlimit"));
		rhReport.put("creditCardCreditLimitUsedRate", data.get("creditcardcreditlimitusedrate"));
		rhReport.put("creditLoanNewCountLatest6M", data.get("creditloannewcountlatest6m"));
		rhReport.put("latest6CreMonthHighOverCount", data.get("creditcardoverduemaxstagelatest6m"));
		
		// lbs_info
		Map<String, Object> lbsInfo = new HashMap<String, Object>();
		lbsInfo.put("val",  data.get("city_value"));
		
		// final_score
		Map<String, Object> tndPrsBasic = new HashMap<String, Object>();
		tndPrsBasic.put("finalScore",  data.get("final_score"));
		
		//lawInfo
		Map<String, Object> lawInfo = new HashMap<String, Object>();
		String apply_at = String.valueOf(data.get("apply_at"));
//		Timestamp.valueOf(apply_at);
////		System.out.println(Timestamp.valueOf(apply_at).getTime());
		lawInfo.put("applyAt", Timestamp.valueOf(apply_at).getTime());

		res.put("uid", uid);
		res.put("lbsInfo", lbsInfo);
		res.put("tndPrsBasic", tndPrsBasic);
		res.put("lawInfo", lawInfo);
		res.put("applyInfo", applyInfo);
		res.put("userApplyInfo", applyInfo);
		res.put("hfbQryOverdue", hfbQryOverdue);
		res.put("hfbQryCreditCard", hfbQryCreditCard);
		res.put("hfbQryLoan", hfbQryLoan);
		res.put("hfbQryQueryInfo", hfbQryQueryInfo);
		res.put("hfbQryQueryInfo", hfbQryQueryInfo);
		res.put("pyEdu", pyEdu);
		res.put("cardInfo", cardInfo);
		res.put("boscPortraitData", boscPortraitData);
		res.put("rhReport", rhReport);
		res.put("rhSource", "RH");
		
		Map<String, Object> creditX = getCreditX(data);
		res.put("creditX", creditX);

		// apply time line && thr_mont_apply
		Map<String, Object> listMap = new HashMap<String, Object>();
		listMap = getListMap(data);
		res.putAll(listMap);

		String json = JSON.toJSONString(res);
		//		System.out.println(json);
		res = JSON.parseObject(json, Map.class);
		return res;
	}
	
	 public static Map<String, Object> getCreditX(Map<String, Object> map){
		 Map<String, Object> creditX = new HashMap<String, Object>();
		 Object output_risklevel = map.get("output_risklevel");
		 Object output_riskscore = map.get("output_riskscore");
		 if(output_riskscore != null){
			 Map<String, Object> creditXScore = new HashMap<String, Object>();
			 creditXScore.put("score", output_riskscore);
			 creditX.put("creditXScore", creditXScore);
		 }
		 
		 if(output_risklevel != null){
			 Map<String, Object> creditXFraud = new HashMap<String, Object>();
			 creditXFraud.put("riskLevel", output_risklevel);
			 creditX.put("creditXFraud", creditXFraud);
		 }
		 
		 return creditX;
	 }

	/**
	 * 构建复杂的输入项
	 * @param map
	 * @return
	 */
	public static Map<String, Object> getListMap(Map<String, Object> map){
		Map<String, Object> res = new HashMap<String, Object>();

		// time line
		double contractDuration = 0;
		double autoAuditDuration = 0;
		double commonInfoDuration = 0;
		double creditCardDuration = 0;
		double identificationNoDuration = 0;
		double photoDuration = 0;
		if(map.get("auto_audit_duration") != null){
			autoAuditDuration = Double.parseDouble(String.valueOf(map.get("auto_audit_duration")));
		}
		if(map.get("contract_duration") != null){
			contractDuration = Double.parseDouble(String.valueOf(map.get("contract_duration")));
		}
		if(map.get("common_info_duration") != null){
			commonInfoDuration = Double.parseDouble(String.valueOf(map.get("common_info_duration")));
		}
		if(map.get("credit_card_duration") != null){
			creditCardDuration = Double.parseDouble(String.valueOf(map.get("credit_card_duration")));
		}
		if(map.get("identification_no_duration") != null){
			identificationNoDuration = Double.parseDouble(String.valueOf(map.get("identification_no_duration")));
		}
		if(map.get("photo_duration") != null){
			photoDuration = Double.parseDouble(String.valueOf(map.get("photo_duration")));
		}
		List<Map<String, Object>> timeLine = new ArrayList<Map<String, Object>>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("step", "CONTRACT");
		map1.put("duration", contractDuration);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("step", "PHOTO");
		map2.put("duration", photoDuration);
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("step", "IDENTIFICATION_NO");
		map3.put("duration", identificationNoDuration);
		Map<String, Object> map4 = new HashMap<String, Object>();
		map4.put("step", "CREDIT_CARD");
		map4.put("duration", creditCardDuration);
		Map<String, Object> map5 = new HashMap<String, Object>();
		map5.put("step", "COMMON_INFO");
		map5.put("duration", commonInfoDuration);
		Map<String, Object> map6 = new HashMap<String, Object>();
		map6.put("step", "AUTO_AUDIT");
		map6.put("duration", autoAuditDuration);

		timeLine.add(map1);
		timeLine.add(map2);
		timeLine.add(map3);
		timeLine.add(map4);
		timeLine.add(map5);
		timeLine.add(map6);

		// 3月申请次数
		Integer thr_mont_appy = 0;
		if(map.get("thr_mont_apply") != null){
			thr_mont_appy = (int) Double.parseDouble(String.valueOf(map.get("thr_mont_apply")));
		}
		List<Map<String, Object>> tndPrsRiskItemsList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map7 = new HashMap<String, Object>();
		map7.put("itemId", "1072224");
		map7.put("platformCount", thr_mont_appy);
		tndPrsRiskItemsList.add(map7);

		res.put("userApplyTimeLine", timeLine);
		res.put("tndPrsRiskItemsList", tndPrsRiskItemsList);
		return res;
	}

	/**
	 * 创建结果表
	 * @throws SQLException
	 */
	public static void createTable() throws SQLException{
		String sql = "DROP TABLE if exists `default`.wtm_risk_subscore_all_left;";
		JdbcHelper.update(sql);
		
		sql = "CREATE TABLE IF NOT EXISTS `default`.wtm_risk_subscore_all_left "
				+ "("
				+ "uid char(36), "
				+ "score_2 decimal(15, 12), "
				+ "group_2 varchar(45), "
				+ "score_4 decimal(15, 12), "
				+ "group_4 varchar(45), "
				+ "apply_at datetime"
				+ ")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
		JdbcHelper.update(sql);
	}

}
