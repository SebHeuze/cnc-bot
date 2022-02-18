package org.cnc.cncbot;

import org.cnc.cncbot.dto.UserSession;
import org.cnc.cncbot.service.AccountService;
import org.junit.Test;

public class AccountServiceTest {

	AccountService accountService = new AccountService();
	
	@Test
	public void testLogin() {
		UserSession userSession = new UserSession("xx", "xx", 432, 0, 0, "", "", "");
		this.accountService.connect(userSession);
	}
}
