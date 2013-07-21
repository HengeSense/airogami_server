package com.airogami;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.airogami.application.ServiceUtils;
import com.airogami.application.exception.ApplicationException;
import com.airogami.common.constants.AccountConstants;
import com.airogami.exception.AirogamiException;
import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.Authenticate;
import com.airogami.persistence.entities.Category;
import com.airogami.persistence.entities.Chain;
import com.airogami.persistence.entities.ChainMessage;
import com.airogami.persistence.entities.Message;
import com.airogami.persistence.entities.Plane;
import com.airogami.presentation.logic.ManagerUtils;

public class Prepare {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private String[] emails = {"tianhuyang@gmail.com",
			"danali@gmail.com", "alexjiang@gmail.com", "xiangyuzhang@gmail.com", "linghaogu@gmail.com"};
	private String[] names = {"杨天虎", "Dana Li", "Alex Jiang", "张翔宇", "顾凌浩"};
	private String[][] locations = { {"中国", "四川", "成都"},
			{"USA", "California", "Sandiego"},
			{"USA", "California", "Los Angeles"},
			{"中国", "浙江", "杭州"},
			{"中国", "江苏", "徐州"},
			};
	private Short sexes[] = {AccountConstants.SexType_Female, AccountConstants.SexType_Male,
			AccountConstants.SexType_Male,AccountConstants.SexType_Male,
			AccountConstants.SexType_Male
			};
	
	@Test
	public void test() {
		try {
			this.createCategories();
			this.createAccount();
			this.sendPlanes();
			this.sendChains();
			this.matchPlanes();
			this.replyPlanes();
			this.matchChains();
			this.replyChains();
			
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}		
	}
	
	private void createCategories() throws AirogamiException{
		Category category = new Category("feeling", "What's on your mind?");
		ManagerUtils.planeManager.createCategory(category);
	}
	
	private void createAccount() throws AirogamiException{
		for(int i=0; i < names.length; ++i){				
			Authenticate authenticate = new Authenticate();				
			authenticate.setEmail(emails[i]);			
			authenticate.setPassword("12345678");
			Account account = new Account();
			account.setFullName(names[i]);
			account.setScreenName(names[i]);
			account.setAuthenticate(authenticate);
			account.setSex(sexes[i]);
			account.setStatus((short)0);
			account.setIcon("icon");
			account.setCity(locations[i][2]);
			account.setProvince(locations[i][1]);
			account.setCountry(locations[i][0]);
			account.setLatitude(0.0);
			account.setLongitude(0.0);
			ManagerUtils.accountManager.signup(account);
			/*for(int j = 0; j < 2; ++j){
				account.setFullName(names[i] + j);
				account.setScreenName(names[i] + j);
				account.setCity(null);
				account.setProvince(null);
				account.setCountry(null);
				if(Math.random() > 0.5){
					account.setCountry(locations[i][0]);
					if(Math.random() > 0.5){
						account.setProvince(locations[i][1]);
						if(Math.random() > 0.5){
							account.setCity(locations[i][2]);
						}
					}
					ManagerUtils.accountManager.signup(account);
				}
			}*/
			//ObjectUtils.printObject(account);
		}	
	}
	
	
	
    public  void sendPlanes() throws AirogamiException{
    	for(int i = 0; i < 5; ++i){
    		for(int j = 0; j < 4; ++j){
    			Plane plane = new Plane();
        		//plane.setCity("chengdu");
        		//plane.setProvince("sichuan");
        		//plane.setCountry("China");
        		plane.setLatitude(0.0);
        		plane.setLongitude(0.0);
        		plane.setCategory(new Category());
        		plane.getCategory().setCategoryId((short)1);
        		long ownerId = i + 1;
        		Message message = new Message();
        		message.setContent("hello" + i);
                message.setType((short) 0);
        		plane.getMessages().add(message);
        		plane = ManagerUtils.planeManager.sendPlane(plane, ownerId);
    		}
    		
    		//ObjectUtils.printObject(plane);
    	}
    	
			
    	
	}
    
    private void matchPlanes() throws AirogamiException{
    	try{
    		for(int i = 0; i < 20; ++i){
    		    ServiceUtils.planeService.matchPlane(i + 1);
        	}
    	}
    	catch(ApplicationException e){
    		throw new AirogamiException("Match failed");
    	}
    	
    }
    
    private void replyPlanes() throws AirogamiException{
    	for(int i = 0; i < 5; ++i){
    		Map<String, Object> result = ManagerUtils.planeManager.receivePlanes(i + 1, -1, null, null, 1000, true);
    		List<Plane> planes = (List<Plane>) result.get("planes");
    		Iterator<Plane> iter = planes.iterator();
    		while(iter.hasNext()){
    			Plane plane = iter.next();
    			Message message = new Message();
        		message.setContent("reply from" + (i + 1));
        		message.setType((short) 0);
        		long ownerId = i + 1;
        		long planeId = plane.getPlaneId();
        		message = ManagerUtils.planeManager.replyPlane(planeId, ownerId, message);
    		}
    		
    		//ObjectUtils.printObject(message);
    	}
	}
    
    public  void sendChains() throws AirogamiException{
    	for(int i = 0; i < 5; ++i){
    		for(int j = 0; j < 4; ++j){
    			Chain chain = new Chain();
    			//chain.setCity("shanghai");
    			//chain.setProvince("shanghai");
    			//chain.setCountry("China");
    			//chain.setMessageCount((short) 0);
    			long ownerId = i + 1;
    			ChainMessage chainMessage = new ChainMessage();
    			chainMessage.setContent("hello! " + j);
    			chainMessage.setType((short) 0);
    			chain.getChainMessages().add(chainMessage);
                chain = ManagerUtils.chainManager.sendChain(chain, ownerId);
    		}
    		
    		//ObjectUtils.printObject(plane);
    	}
	}
    
     private void matchChains() throws AirogamiException{
    	 for(int i = 0; i < 20; ++i){
  			try {
					ServiceUtils.chainService.matchChain(i + 1);
				} catch (ApplicationException e) {
					throw new AirogamiException(e.getMessage());
				}
  		}
	}
    
    private void replyChains() throws AirogamiException{
    	for(int i = 0; i < 5; ++i){
    		Map<String, Object> result = ManagerUtils.chainManager.receiveChains(i + 1, -1, null, null, 1000, true);
    		List<Chain> chains = (List<Chain>) result.get("chains");
    		Iterator<Chain> iter = chains.iterator();
    		while(iter.hasNext()){
    			Chain chain = iter.next();
    			String content = "hello! from" + (i + 1);
    			int type = 0;
    			long ownerId = i + 1;
    			long chainId = chain.getChainId();
    			ChainMessage chainMessage = ManagerUtils.chainManager.replyChain(ownerId, chainId, content, type);
    		}
    		
    		//ObjectUtils.printObject(message);
    	}
	}

}
