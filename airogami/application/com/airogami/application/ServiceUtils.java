package com.airogami.application;

public class ServiceUtils {

	public static final IAccountService accountService = new AccountService();
	public static final IPlaneService planeService = new PlaneService();
	public static final IChainService chainService = new ChainService();
	public static final AirogamiService airogamiService = new AirogamiService();
	static {
		airogamiService.start();
	}
}
