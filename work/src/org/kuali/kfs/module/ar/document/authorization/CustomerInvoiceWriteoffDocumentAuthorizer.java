/*
 * Copyright 2008 The Kuali Foundation.
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
package org.kuali.kfs.module.ar.document.authorization;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.kfs.module.ar.ArAuthorizationConstants;
import org.kuali.kfs.module.ar.ArConstants;
import org.kuali.kfs.module.ar.ArKeyConstants;
import org.kuali.kfs.module.ar.ArPropertyConstants;
import org.kuali.kfs.module.ar.businessobject.OrganizationOptions;
import org.kuali.kfs.module.ar.document.CustomerInvoiceWriteoffDocument;
import org.kuali.kfs.sys.KFSConstants;
import org.kuali.kfs.sys.businessobject.ChartOrgHolder;
import org.kuali.kfs.sys.context.SpringContext;
import org.kuali.kfs.sys.document.authorization.FinancialSystemTransactionalDocumentActionFlags;
import org.kuali.kfs.sys.document.authorization.FinancialSystemTransactionalDocumentAuthorizerBase;
import org.kuali.kfs.sys.service.FinancialSystemUserService;
import org.kuali.rice.kns.bo.user.UniversalUser;
import org.kuali.rice.kns.document.Document;
import org.kuali.rice.kns.exception.DocumentInitiationAuthorizationException;
import org.kuali.rice.kns.exception.DocumentTypeAuthorizationException;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.util.ObjectUtils;

public class CustomerInvoiceWriteoffDocumentAuthorizer extends FinancialSystemTransactionalDocumentAuthorizerBase {

    @Override
    public Map getEditMode(Document document, UniversalUser user) {

        Map<String, String> editModeMap = super.getEditMode(document, user);
        CustomerInvoiceWriteoffDocument customerInvoiceWriteoffDocument = (CustomerInvoiceWriteoffDocument) document;

        if (StringUtils.equals(customerInvoiceWriteoffDocument.getStatusCode(), ArConstants.CustomerInvoiceWriteoffStatuses.INITIATE))
            editModeMap.put(ArAuthorizationConstants.CustomerCreditMemoEditMode.DISPLAY_INIT_TAB, "TRUE");
        else
            editModeMap.put(ArAuthorizationConstants.CustomerCreditMemoEditMode.DISPLAY_INIT_TAB, "FALSE");

        return editModeMap;
    }

    /**
     * @see org.kuali.rice.kns.document.authorization.DocumentAuthorizer#getDocumentActionFlags(Document, UniversalUser)
     */
    @Override
    public FinancialSystemTransactionalDocumentActionFlags getDocumentActionFlags(Document document, UniversalUser user) {
        FinancialSystemTransactionalDocumentActionFlags flags = super.getDocumentActionFlags(document, user);

        CustomerInvoiceWriteoffDocument customerInvoiceWriteoffDocument = (CustomerInvoiceWriteoffDocument) document;
        if (StringUtils.equals(customerInvoiceWriteoffDocument.getStatusCode(), ArConstants.CustomerInvoiceWriteoffStatuses.INITIATE)) {
            flags.setCanSave(false);
            flags.setCanClose(true);
            flags.setCanCancel(false);
        }
        return flags;
    }

    /**
     * @see org.kuali.rice.kns.document.authorization.DocumentAuthorizerBase#canInitiate(java.lang.String,
     *      org.kuali.rice.kns.bo.user.UniversalUser)
     */
    @Override
    public void canInitiate(String documentTypeName, UniversalUser user) throws DocumentTypeAuthorizationException {
        super.canInitiate(documentTypeName, user);
        // to initiate, the user must have the organization options set up.
        ChartOrgHolder chartUser = SpringContext.getBean(FinancialSystemUserService.class).getOrganizationByModuleId(KFSConstants.Modules.CHART);

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("chartOfAccountsCode", chartUser.getChartOfAccountsCode());
        criteria.put("organizationCode", chartUser.getOrganizationCode());
        OrganizationOptions organizationOptions = (OrganizationOptions) SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(OrganizationOptions.class, criteria);

        // if organization doesn't exist
        if (ObjectUtils.isNull(organizationOptions)) {
            throw new DocumentInitiationAuthorizationException(ArKeyConstants.ERROR_ORGANIZATION_OPTIONS_MUST_BE_SET_FOR_USER_ORG, new String[] {});
        }

        // if writeoff option is set up for to use organization accounting default FAU, those values must exist before a writeoff
        // document can be initiated
        /*
        String writeoffGenerationOption = SpringContext.getBean(ParameterService.class).getParameterValue(CustomerInvoiceWriteoffDocument.class, ArConstants.GLPE_WRITEOFF_GENERATION_METHOD);
        boolean isUsingOrgAcctDefaultWriteoffFAU = ArConstants.GLPE_WRITEOFF_GENERATION_METHOD_ORG_ACCT_DEFAULT.equals(writeoffGenerationOption);
        if (isUsingOrgAcctDefaultWriteoffFAU) {

            Integer currentUniversityFiscalYear = SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear();

            criteria = new HashMap<String, Object>();
            criteria.put("universityFiscalYear", currentUniversityFiscalYear);
            criteria.put("chartOfAccountsCode", chartUser.getChartOfAccountsCode());
            criteria.put("organizationCode", chartUser.getOrganizationCode());
            OrganizationAccountingDefault organizationAccountingDefault = (OrganizationAccountingDefault) SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(OrganizationAccountingDefault.class, criteria);

            // check if org. acct. default exists
            if (ObjectUtils.isNull(organizationAccountingDefault)) {
                throw new DocumentInitiationAuthorizationException(ArConstants.ERROR_ORG_ACCT_DEFAULT_FOR_USER_MUST_EXIST, new String[] {});
            }
            //check if org acct. default writeoff chart, object, or account number are empty
            else if (StringUtils.isEmpty(organizationAccountingDefault.getWriteoffAccountNumber()) || StringUtils.isEmpty(organizationAccountingDefault.getWriteoffChartOfAccountsCode()) || StringUtils.isEmpty(organizationAccountingDefault.getWriteoffFinancialObjectCode())) {
                throw new DocumentInitiationAuthorizationException(ArConstants.ERROR_ORG_ACCT_DEFAULT_WRITEOFF_MUST_EXIST, new String[] {});
            }
        }*/
    }

}
