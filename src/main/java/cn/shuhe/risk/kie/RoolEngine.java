package cn.shuhe.risk.kie;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.time.StopWatch;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

public class RoolEngine {
	static final String SUFFIX = ".drl";
	static Map<String, KieBase> KIEBASE = new HashMap<String, KieBase>();
	
	public static Map<String, Object> ruleexc(String path, Map<String, Object> facts) throws Exception{
		Map<String, Object> check = new HashMap<String, Object>();
		readKnowledgeBase(path, check, facts);
		return check;
	}
	
	public static void readKnowledgeBase(String path, Object... objects) throws Exception{
		KieSession kSession = getKieBase(path).newKieSession();
		try {
			for(Object entry : objects){
				kSession.insert(entry);
			}
			kSession.fireAllRules();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
//			kSession.dispose();
			kSession.destroy();
		}
	}  
	
	public static KieBase getKieBase(String path) throws Exception{
		StopWatch watch = new StopWatch();
		watch.start();
		KieBase res = KIEBASE.get(path);
		if(res != null){
			return res;
		}
		// init KieBase
		String drlPath =  path + SUFFIX;
    	KieServices ks = KieServices.Factory.get();
    	KieFileSystem kfs = ks.newKieFileSystem();
    	Resource resource = ResourceFactory.newClassPathResource(drlPath);
        kfs.write(resource);
        KieBuilder kieBuilder = ks.newKieBuilder(kfs).buildAll();
        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
        	System.out.println("Build Errors:\n" + kieBuilder.getResults().toString());
        	throw new Exception("Build Errors:\n" + kieBuilder.getResults().toString());
        }
        KieContainer kc = ks.newKieContainer(ks.getRepository().getDefaultReleaseId());
        res = kc.getKieBase();
        KIEBASE.put(path, res);
        watch.stop();
        System.out.println("init#####:" + watch.getTime());
        return res;
	}
}
