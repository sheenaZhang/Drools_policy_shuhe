package cn.shuhe.risk.policy;

import cn.shuhe.risk.service.AscoreV8;
import cn.shuhe.risk.service.RuleAdjust;
import cn.shuhe.risk.service.SubScore;


/**
 * start method
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	try {
//			SubScore.exc();
//    		List<String> months_list = new ArrayList<String>();
//    		months_list.add("master");
//    		months_list.add("1609_part_2");
//    		months_list.add("1610_part_1");
//    		months_list.add("1610_part_2");
//    		months_list.add("1703_part_1");
//    		months_list.add("1703_part_2");
//    		months_list.add("1704_part_1");
//    		months_list.add("1704_part_2");
//    		for(String month : months_list){
//    			SubScore.exc_csv(month);
//    			PricePredict.exc_csv(month);
//    			WrhPredict.exc_csv(month);
//    		}
//			HttpLogExc.exc_csv();
//    		RuleAdjust.exc_csv();
//    		SubScore.exc();
//    		SubScore.exc_csv("1709");
    		System.out.print("running...");
    		AscoreV8.exc_csv();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
