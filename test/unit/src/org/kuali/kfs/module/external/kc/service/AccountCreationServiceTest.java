/*
 * Copyright 2010 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kfs.module.external.kc.service;

import static org.kuali.kfs.sys.fixture.UserNameFixture.khuntley;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.kuali.kfs.module.external.kc.dto.AccountCreationStatus;
import org.kuali.kfs.module.external.kc.dto.AccountParameters;
import org.kuali.kfs.sys.ConfigureContext;
import org.kuali.kfs.sys.context.KualiTestBase;
import org.kuali.kfs.sys.context.SpringContext;

@ConfigureContext(session = khuntley)
public class AccountCreationServiceTest extends KualiTestBase 
{
    private AccountCreationService accountCreationService;
    private AccountParameters accountParameters;
    
    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception 
    {
        // Initialize service objects.
        accountCreationService = 
            SpringContext.getBean(AccountCreationService.class);
        
        // Initialize objects.
        accountParameters = new AccountParameters();
        accountParameters.setAccountName("accountName");
        accountParameters.setAccountNumber("123456");
        
        super.setUp();
    }
    
    /**
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception 
    {
        super.tearDown();
    }
    
    /**
     * TODO
     * This method...
     */
    public void testCreateAccount() 
    {   AccountCreationStatus creationStatus =
            accountCreationService.createAccount(accountParameters);
    
        assertTrue(creationStatus.isSuccess());
    }

    /**
     * TODO
     * This method...
     */
    public void testCreateAccountWsClient() 
    {   
        try {
            URL url = new URL("http://localhost:8080/kfs-dev/remoting/accountCreationServiceSOAP?wsdl");
            QName qName = new QName("KFS", "accountCreationServiceSOAP");
            
            Service service = Service.create(url, qName);
            AccountCreationService accountService = (AccountCreationService)service.getPort(AccountCreationService.class);
                    
            AccountCreationStatus creationStatus = accountService.createAccount(accountParameters);
        
            System.out.println(creationStatus.getAccountNumber());
            
            assertTrue(creationStatus.isSuccess());
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
