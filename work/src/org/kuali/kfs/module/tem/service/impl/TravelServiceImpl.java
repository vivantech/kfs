/*
 * Copyright 2012 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kfs.module.tem.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kuali.kfs.integration.ar.AccountsReceivableModuleService;
import org.kuali.kfs.module.tem.TemConstants;
import org.kuali.kfs.module.tem.businessobject.PrimaryDestination;
import org.kuali.kfs.module.tem.businessobject.TEMProfile;
import org.kuali.kfs.module.tem.businessobject.TravelCardType;
import org.kuali.kfs.module.tem.dataaccess.TravelDocumentDao;
import org.kuali.kfs.module.tem.document.TravelDocument;
import org.kuali.kfs.module.tem.service.TEMRoleService;
import org.kuali.kfs.module.tem.service.TravelService;
import org.kuali.kfs.sys.context.SpringContext;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kns.datadictionary.validation.charlevel.RegexValidationPattern;
import org.kuali.rice.krad.service.BusinessObjectService;

/**
 * Travel Service Implementation
 */
public class TravelServiceImpl implements TravelService {

    private BusinessObjectService businessObjectService;
    private ParameterService parameterService;
    private AccountsReceivableModuleService accountsReceivableModuleService;
    private TEMRoleService temRoleService;
    private TravelDocumentDao travelDocumentDao;

    /**
     * Validate a phone number
     *
     * @param phoneNumber to validate
     */
    @Override
    public String validatePhoneNumber(final String phoneNumber, String error){
        return validatePhoneNumber("", phoneNumber, error);
    }

    /**
     * Validate a phone number
     *
     * @param countryCode to consider for validation
     * @param phoneNumber to validate
     */
    @Override
    public String validatePhoneNumber(final String countryCode, final String phoneNumber, String error){

        //Determine if the US phone format should be used or a very lax international format.
        if (StringUtils.isBlank(countryCode) || (!countryCode.equals("US"))){
            RegexValidationPattern pattern = new RegexValidationPattern();
            pattern.setPattern(TemConstants.INT_PHONE_PATTERN);
            if (phoneNumber != null && pattern.matches(phoneNumber)){
                return "";
            }
            else{
                return error;
            }
        }
        else{
            RegexValidationPattern pattern = new RegexValidationPattern();
            pattern.setPattern(TemConstants.US_PHONE_PATTERN);
            if (pattern.matches(phoneNumber)){
                return "";
            }
            else{
                return error;
            }
        }
    }

    /**
     * @see org.kuali.kfs.module.tem.service.TravelService#findTemProfileByPrincipalId(java.lang.String)
     */
    @Override
    public TEMProfile findTemProfileByPrincipalId(String principalId) {
        return SpringContext.getBean(TemProfileServiceImpl.class).findTemProfileByPrincipalId(principalId);
    }

    /**
     * @see org.kuali.kfs.module.tem.service.TravelService#isUserInitiatorOrArranger(org.kuali.kfs.module.tem.document.TravelDocument, org.kuali.rice.kim.bo.Person)
     */
    @Override
    public boolean isUserInitiatorOrArranger(TravelDocument document, Person user){
        boolean isUser = false;

        WorkflowDocument workflowDocument = document.getDocumentHeader().getWorkflowDocument();
        String initiator = workflowDocument.getInitiatorPrincipalId();
        String docType = document.getDocumentHeader().getWorkflowDocument().getDocumentTypeName();

        if(initiator.equals(user.getPrincipalId()) || temRoleService.isTravelDocumentArrangerForProfile(docType, user.getPrincipalId(), document.getProfileId()) ) {
            isUser = true;
        }
        return isUser;
    }

    /**
     * @see org.kuali.kfs.module.tem.service.TravelService#findAllDistinctPrimaryDestinations(java.lang.String)
     */
    @Override
    public List<PrimaryDestination> findAllDistinctPrimaryDestinations(String tripType){
        return travelDocumentDao.findAllDistinctPrimaryDestinations(tripType);
    }

    /**
     * @see org.kuali.kfs.module.tem.service.TravelService#getTravelCardTypes()
     */
    @Override
    public List<String> getTravelCardTypes() {
        List<String> travelCardTypes = new ArrayList<String>();
        for (TravelCardType cardType : (List<TravelCardType>)businessObjectService.findAll(TravelCardType.class)){
            travelCardTypes.add(cardType.getCode());
        }
        return travelCardTypes;
    }

    /**
     * @see org.kuali.kfs.module.tem.service.TravelService#findDefaultPrimaryDestinations(java.lang.Class, java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public List findDefaultPrimaryDestinations(Class clazz, String countryCode) {
        return travelDocumentDao.findDefaultPrimaryDestinations(clazz, countryCode);
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public void setParameterService(final ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    public void setAccountsReceivableModuleService(AccountsReceivableModuleService accountsReceivableModuleService) {
        this.accountsReceivableModuleService = accountsReceivableModuleService;
    }

    public void setTravelDocumentDao(TravelDocumentDao travelDocumentDao) {
        this.travelDocumentDao = travelDocumentDao;
    }

    public void setTemRoleService(TEMRoleService temRoleService){
        this.temRoleService = temRoleService;
    }

}