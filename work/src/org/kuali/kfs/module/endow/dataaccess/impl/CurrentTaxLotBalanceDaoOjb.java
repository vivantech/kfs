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
package org.kuali.kfs.module.endow.dataaccess.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.kuali.kfs.module.endow.EndowConstants;
import org.kuali.kfs.module.endow.EndowPropertyConstants;
import org.kuali.kfs.module.endow.businessobject.CurrentTaxLotBalance;
import org.kuali.kfs.module.endow.businessobject.FeeClassCode;
import org.kuali.kfs.module.endow.businessobject.FeeMethod;
import org.kuali.kfs.module.endow.businessobject.FeeSecurity;
import org.kuali.kfs.module.endow.businessobject.HoldingHistory;
import org.kuali.kfs.module.endow.businessobject.HoldingTaxLot;
import org.kuali.kfs.module.endow.dataaccess.CurrentTaxLotBalanceDao;
import org.kuali.kfs.module.endow.document.service.MonthEndDateService;
import org.kuali.kfs.module.endow.util.KEMCalculationRoundingHelper;
import org.kuali.kfs.sys.context.SpringContext;
import org.kuali.rice.kns.dao.impl.PlatformAwareDaoBaseOjb;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.service.DataDictionaryService;

public class CurrentTaxLotBalanceDaoOjb extends PlatformAwareDaoBaseOjb implements CurrentTaxLotBalanceDao {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(CurrentTaxLotBalanceDaoOjb.class);

    private BusinessObjectService businessObjectService;
    
    /**
     * @see org.kuali.kfs.module.endow.dataaccess.CurrentTaxLotBalanceDao#getAllCurrentTaxLotBalanceEntriesForSecurity(java.lang.String)
     */
    public Collection<CurrentTaxLotBalance> getAllCurrentTaxLotBalanceEntriesForSecurity(String securityId) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(EndowPropertyConstants.CURRENT_TAX_LOT_SECURITY_ID, securityId);

        return (Collection<CurrentTaxLotBalance>) getPersistenceBrokerTemplate().getCollectionByQuery(QueryFactory.newQuery(CurrentTaxLotBalance.class, criteria));
    }

    /**
     * Prepares the criteria and selects the records from END_CRNT_TAX_LOT_BAL_T table
     */
    protected Collection<CurrentTaxLotBalance> getCurrentTaxLotBalances(FeeMethod feeMethod) {
        Collection<CurrentTaxLotBalance> currentTaxLotBalance = new ArrayList(); 
        
        Collection incomePrincipalValues = null;
        incomePrincipalValues.add(EndowConstants.FeeMethod.FEE_BASE_CODE_VALUE_FOR_INCOME);
        incomePrincipalValues.add(EndowConstants.FeeMethod.FEE_BASE_CODE_VALUE_FOR_PRINCIPAL);
        
        Criteria criteria = new Criteria();
        
        if (feeMethod.getFeeBaseCode().equalsIgnoreCase(EndowConstants.FeeMethod.FEE_BASE_CODE_VALUE_FOR_INCOME_AND_PRINCIPAL)) {
            criteria.addIn(EndowPropertyConstants.CURRENT_TAX_LOT_BALANCE_INCOME_PRINCIPAL_INDICATOR, incomePrincipalValues);
        }
        else {
            if (feeMethod.getFeeBaseCode().equalsIgnoreCase(EndowConstants.FeeMethod.FEE_BASE_CODE_VALUE_FOR_INCOME)) {
                criteria.addColumnEqualTo(EndowPropertyConstants.CURRENT_TAX_LOT_BALANCE_INCOME_PRINCIPAL_INDICATOR, EndowConstants.FeeMethod.FEE_BASE_CODE_VALUE_FOR_INCOME);
            }
            
            if (feeMethod.getFeeBaseCode().equalsIgnoreCase(EndowConstants.FeeMethod.FEE_BASE_CODE_VALUE_FOR_PRINCIPAL)) {
                criteria.addColumnEqualTo(EndowPropertyConstants.CURRENT_TAX_LOT_BALANCE_INCOME_PRINCIPAL_INDICATOR, EndowConstants.FeeMethod.FEE_BASE_CODE_VALUE_FOR_PRINCIPAL);
            }
        }

        if (feeMethod.getFeeByClassCode() && feeMethod.getFeeBySecurityCode()) {
            criteria.addIn(EndowPropertyConstants.CURRENT_TAX_LOT_BALANCE_SECURITY_CLASS_CODE, getSecurityClassCodes(feeMethod.getCode()));
            criteria.addIn(EndowPropertyConstants.CURRENT_TAX_LOT_BALANCE_SECURITY_ID, getSecurityIds(feeMethod.getCode()));
        }
        else {
            if (feeMethod.getFeeByTransactionType()) {
                criteria.addIn(EndowPropertyConstants.CURRENT_TAX_LOT_BALANCE_SECURITY_CLASS_CODE, getSecurityClassCodes(feeMethod.getCode()));
            }
            
            if (feeMethod.getFeeByETranCode()) {
                criteria.addIn(EndowPropertyConstants.CURRENT_TAX_LOT_BALANCE_SECURITY_ID, getSecurityIds(feeMethod.getCode()));
            }
        }
        
        QueryByCriteria query = QueryFactory.newQuery(CurrentTaxLotBalance.class, criteria);
            
        currentTaxLotBalance = getPersistenceBrokerTemplate().getCollectionByQuery(query);
        
        return currentTaxLotBalance;
    }
       
    /**
     * Gets the security codes for a given securityClassCode in END_FEE_CLS_CD_T table
     * @feeMethodCode FEE_MTH
     * @return securityCodes
     */
    protected Collection getSecurityClassCodes(String feeMethodCode) {
        Collection securityClassCodes = null;
        Collection<FeeClassCode> feeClassCodes = new ArrayList();        

        if (StringUtils.isNotBlank(feeMethodCode)) {        
            Map<String, String>  crit = new HashMap<String, String>();
            
            if (SpringContext.getBean(DataDictionaryService.class).getAttributeForceUppercase(FeeClassCode.class, EndowPropertyConstants.FEE_METHOD_CODE)) {
                feeMethodCode = feeMethodCode.toUpperCase();
            }
            
            Criteria criteria = new Criteria();
            criteria.addColumnEqualTo(EndowPropertyConstants.FEE_METHOD_CODE, feeMethodCode);
            criteria.addColumnEqualTo(EndowPropertyConstants.FEE_CLASS_CODE_INCLUDE, EndowConstants.YES);
            
            QueryByCriteria query = QueryFactory.newQuery(FeeClassCode.class, criteria);
            
            feeClassCodes = getPersistenceBrokerTemplate().getCollectionByQuery(query);
            for (FeeClassCode feeClassCode : feeClassCodes) {
                securityClassCodes.add(feeClassCode.getFeeClassCode());
            }
        }
        
        return securityClassCodes;
    }
    
    /**
     * Gets the security ids for a given securityClassCode in END_FEE_SEC_T table
     * @feeMethodCode FEE_MTH
     * @return securityIds
     */
    protected Collection getSecurityIds(String feeMethodCode) {
        Collection securityIds = null;
        Collection<FeeSecurity> feeSecuritys = new ArrayList();        

        if (StringUtils.isNotBlank(feeMethodCode)) {        
            Map<String, String>  crit = new HashMap<String, String>();
            
            if (SpringContext.getBean(DataDictionaryService.class).getAttributeForceUppercase(FeeSecurity.class, EndowPropertyConstants.FEE_METHOD_CODE)) {
                feeMethodCode = feeMethodCode.toUpperCase();
            }
            
            Criteria criteria = new Criteria();
            criteria.addColumnEqualTo(EndowPropertyConstants.FEE_METHOD_CODE, feeMethodCode);
            criteria.addColumnEqualTo(EndowPropertyConstants.FEE_SECURITY_INCLUDE, EndowConstants.YES);
            
            QueryByCriteria query = QueryFactory.newQuery(FeeClassCode.class, criteria);
            
            feeSecuritys = getPersistenceBrokerTemplate().getCollectionByQuery(query);
            for (FeeSecurity feeSecurity : feeSecuritys) {
                securityIds.add(feeSecurity.getSecurityCode());
            }
        }
        
        return securityIds;
    }
    
    /**
     * @see org.kuali.kfs.module.endow.dataaccess.CurrentTaxLotBalanceDao#getCurrentTaxLogBalanceTotalHoldingMarkeValue(FeeMethod)
     */
    public BigDecimal getCurrentTaxLotBalanceTotalHoldingUnits(FeeMethod feeMethod) {
        BigDecimal totalHoldingUnits = new BigDecimal("0");
        
        Collection <CurrentTaxLotBalance> currentTaxLotBalanceRecords = getCurrentTaxLotBalances(feeMethod);
        for (CurrentTaxLotBalance currentTaxLotBalance : currentTaxLotBalanceRecords) {
            totalHoldingUnits.add(currentTaxLotBalance.getUnits());    
        }
        
        return totalHoldingUnits;
    }
    
    /**
     * @see org.kuali.kfs.module.endow.dataaccess.CurrentTaxLotBalanceDao#getCurrentTaxLotBalanceTotalHoldingMarketValue(FeeMethod)
     */
    public BigDecimal getCurrentTaxLotBalanceTotalHoldingMarketValue(FeeMethod feeMethod) {
        BigDecimal totalHoldingMarkteValue = new BigDecimal("0");
        
        Collection <CurrentTaxLotBalance> currentTaxLotBalanceRecords = getCurrentTaxLotBalances(feeMethod);
        for (CurrentTaxLotBalance currentTaxLotBalance : currentTaxLotBalanceRecords) {
            totalHoldingMarkteValue.add(currentTaxLotBalance.getMarketValue());    
        }
        
        return totalHoldingMarkteValue;
    }

    /**
     * This method gets the businessObjectService.
     * 
     * @return businessObjectService
     */
    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }

    /**
     * This method sets the businessObjectService
     * 
     * @param businessObjectService
     */
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
}
