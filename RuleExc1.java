package cn.shuhe.risk.policy;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.shuhe.risk.kie.RoolEngine;

public class RuleExc {
	/***
	 * 测试
	 * @param args
	 * @throws Exception
	 */
	/**
	 * @param args
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static void main( String[] args ) throws Exception
	{
		// test
//		String path = "subScore2/score_2_bos_v3.1.2";
//		String path = "rh/rh_4bos_v1.3.2";
//		String path = "changeModel/cmOperation";
//		String path = "subScore4/score_final_b_test_01_v5.0.4";
//		String path = "subScore6/score_6_bos_v1.0.1";
//		String path = "subScore6/score_final_v6.0.2";
//		String path = "subScore7/score_final_v7.0.0";
		String path = "perm/perm_v1.2.1";
		Map<String, Object> facts = new HashMap<String, Object>();
		String json = JSON.toJSONString(facts);
//		json = "{\"rhMaxM0112\":0,\"firedRulesNum\":2,\"accounts\":[],\"limit_2\":0,\"additionCode\":[\"JXL07|JXL09\"],\"group_2\":\"GROUP_20_1\",\"group_4\":\"G_12\",\"rhCrusg\":0.394303976,\"params_initial_manual_count\":11,\"score_4\":0.09148421927113964,\"rhSumDueamnt\":0,\"rhApplDe6m\":1,\"score_2\":0.09690799927812643,\"lawInfo\":{\"applyAt\":1496912719000,\"applyBizType\":\"BALANCE_TRANSFER\",\"auditRouter\":\"HNBHFB0001\",\"batchId\":\"43af1ebc-a167-4bf0-98e5-573139a11ab7\",\"channel\":\"HNB\",\"cleanFlag\":false,\"createdAt\":1496912719000,\"delFlg\":\"0\",\"fundSource\":\"HFB\",\"idNumber\":\"23062419881021045X\",\"lawCode\":\"2017060800007745\",\"lawId\":\"5b36c497-4f5e-4f00-b3e2-b1cd4f1d155e\",\"lawStatus\":\"AUTO_AUDITING\",\"makeState\":\"1\",\"name\":\"周广会\",\"notice\":\"0\",\"phoneNumber\":\"18713439931\",\"processFlag\":\"0\",\"product\":\"BALANCE_TRANSFER\",\"uid\":\"0f18441e-9fc8-46a5-b539-faf6adbfab8c\",\"updatedAt\":1496912880000},\"flag\":\"0\",\"rhAppltimesCreditcard6mth\":1,\"var47\":0,\"rhDebitOverdueNoM12\":0,\"limit_4\":0,\"additionalCode\":\"[\\\"LBS03\\\"]\",\"thrMontApply\":4,\"retCode_2\":\"SCORE_2\",\"params_public_pool_count\":1,\"retCode_4\":\"SCORE_4\"}";
		json = "{\"FOTICLendPassAmount\":0,\"BOSCLendPassAmount\":320812.71,\"academic\":\"大专\",\"ZABLendPassCount\":0,\"BQDDefaultLimit\":5000000,\"ZYCFCLendTotalAmount\":520000,\"boscRespMsg\":\"认证失败,请稍后重试\",\"BTLimit\":2000000,\"SELFLendPassCount\":0,\"FOCUS_LOANLendPassAmount\":18891,\"SELFLendSuccessTotalAmount\":0,\"CREDIT_TRANSACTIONLimit\":2000000,\"BOCLendSuccessTotalAmount\":1177733,\"HFBLendSuccessTotalAmount\":0,\"tndPrsReportDay\":0,\"ZYCFC\":26,\"annualFeeRateMax\":35.99,\"phoneCity\":\"济南市\",\"businessType\":\"BALANCE_TRANSFER\",\"NOAHLendTotalAmount\":165046,\"osType\":\"ANDROID\",\"creditCardBankName\":\"浦发银行\",\"CREDIT_TRANSACTIONUserGroup\":\"CTAAA02\",\"LAST_ANDROID_loanStopedAPP_Count\":0,\"BOSCDefaultLimit\":1995000,\"SELFLendTotalAmount\":0,\"BALANCE_TRANSFERUserGroup\":\"VAAL03\",\"BOCCreditStatus\":\"UNCREATED\",\"hfbOrderCount\":0,\"CASHUserGroup\":\"\",\"ZYCFCLendSuccessTotalAmount\":117500,\"FOTICCreditStatus\":\"SUCCESS\",\"TRAFFIC_TRANSFERLimit\":\"\",\"cusFlg\":\"NORMAL\",\"NOAHLendPassAmount\":0,\"BQDLendTotalAmount\":3785495,\"FOTICLendPassCount\":0,\"ZABLendTotalAmount\":443991,\"TTAvailableLimit\":2900000,\"TTLimit\":3000000,\"HFB\":0,\"idCity\":\"济南市\",\"date\":\"20180326\",\"firstLendBank\":\"BQD\",\"rejectHour\":0,\"defaultDebitCardBank\":\"浦发银行\",\"BALANCE_TRANSFERLimit\":1000000,\"behaviorFlag\":\"0\",\"boscProcessingCount\":1,\"btAccountCreatedAt\":\"20170807\",\"boscRespCode\":\"S72_1004\",\"HFBLendPassAmount\":0,\"ZABDefaultLimit\":3000000,\"FOCUS_LOAN_CORELimit\":\"\",\"FOCUS_LOANLendPassCount\":5,\"FOTICDefaultLimit\":5000000,\"FANLI_BTLimit\":\"\",\"HFBLendTotalAmount\":0,\"ZRB\":1098,\"loanAppPackageCount\":0,\"HFBLendPassCount\":0,\"HFBDefaultLimit\":5000000,\"RONG360_BTUserGroup\":\"\",\"ZABLendSuccessTotalAmount\":443991,\"FOCUS_LOAN\":1,\"FOTICLendTotalAmount\":0,\"FOCUS_LOANLendTotalAmount\":18195,\"uid\":\"4bcda1bc-7dc3-4033-ab3d-e5249f506674\",\"ZYB\":0,\"BQDLendPassCount\":0,\"ZYCFCLendPassAmount\":402500,\"NOAHLendSuccessTotalAmount\":165046,\"CTAvailableLimit\":2000000,\"hourOfDay\":13,\"idProvince\":\"\",\"FOTICLendSuccessTotalAmount\":0,\"lendTotalAmount\":20322711,\"auditStatus\":\"ACCEPTED\",\"requestChannel\":\"hb\",\"TRAFFIC_TRANSFERUserGroup\":\"\",\"age\":33,\"BQDLendPassAmount\":0,\"ZRBLendPassCount\":226,\"bank\":\"ZYCFC\",\"HISTORY_ANDROID_loanAPP_Count\":1,\"bscGrp\":\"19\",\"BOC\":303,\"NOAHLendPassCount\":0,\"BOSCLendTotalAmount\":8049737,\"ZYBLendPassAmount\":0,\"multiLoanCode\":\"LOAN_PERM\",\"ZABCreditStatus\":\"SUCCESS\",\"LAST_ANDROID_loanAPP_Count\":1,\"BOSCCreditStatus\":\"SUCCESS\",\"BOCLendTotalAmount\":1555845,\"CASHLimit\":\"\",\"HISTORY_ANDROID_loanStopedAPP_Count\":0,\"ZYCFC_WFDefaultLimit\":3000000,\"ZRBLendSuccessTotalAmount\":4570615,\"maxExceedStatus\":\"M1\",\"ZRBLendPassAmount\":1194386.09,\"BOSCLendSuccessTotalAmount\":7687526,\"ZAB\":51,\"FOCUS_LOANLendSuccessTotalAmount\":18195,\"ZABLendPassAmount\":0,\"NOAH_1Balance\":-10326911893,\"FOTIC\":0,\"ZYBLendTotalAmount\":0,\"ZYCFCLendPassCount\":25,\"BQDCreditStatus\":\"SUCCESS\",\"ZYBLendPassCount\":0,\"BTAvailableLimit\":533328,\"ZYCFC_WFCreditStatus\":\"SUCCESS\",\"NOAH\":30,\"BOSCLendPassCount\":82,\"RONG360_BTLimit\":\"\",\"FOCUS_LOAN_COREUserGroup\":\"\",\"preLoanLabel\":\"P\",\"HFBCreditStatus\":\"SUCCESS\",\"BQD\":489,\"ZYBLendSuccessTotalAmount\":0,\"lendSuccessTotalAmount\":15311888,\"BOSC\":1153,\"CTLimit\":2000000,\"BQDLendSuccessTotalAmount\":1131282,\"lbsCityList\":\"济南市\",\"SELFLendPassAmount\":0,\"ZYCFC_DLCreditStatus\":\"UNCREATED\",\"multiLoanFlag\":\"0\",\"loanAppNameCount\":0,\"FOTIC-CDRCB-1Balance\":663093103,\"ZRBLendTotalAmount\":5784398,\"SELF\":0,\"FANLI_BTUserGroup\":\"\"}";
		facts = JSONObject.parseObject(json, Map.class);
		Map<String, Object> check = RoolEngine.ruleexc(path, facts);
		System.out.println(JSON.toJSON(check));
	}
}