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
package org.kuali.kfs.module.bc.document.validation.event;

import org.kuali.kfs.module.bc.businessobject.PendingBudgetConstructionGeneralLedger;
import org.kuali.kfs.module.bc.document.BudgetConstructionDocument;
import org.kuali.kfs.module.bc.document.validation.AddBudgetConstructionDocumentRule;
import org.kuali.kfs.module.bc.document.validation.AddPendingBudgetGeneralLedgerLineRule;
import org.kuali.rice.kns.document.Document;
import org.kuali.rice.kns.rule.BusinessRule;
import org.kuali.rice.kns.rule.event.KualiDocumentEventBase;

/**
 * Defines the add (create) Budget Construction document event for the BC Selection screen.
 */
public class AddBudgetConstructionDocumentEvent extends KualiDocumentEventBase {
    private BudgetConstructionDocument budgetConstructionDocument;
    
    /**
     * Constructs a AddBudgetConstructionDocumentEvent.java.
     * @param errorPathPrefix
     * @param document
     */
    public AddBudgetConstructionDocumentEvent(String errorPathPrefix, Document document){
        super("", errorPathPrefix, document);
        this.budgetConstructionDocument = (BudgetConstructionDocument) document;
        
    }

    /**
     * @see org.kuali.rice.kns.rule.event.KualiDocumentEvent#getRuleInterfaceClass()
     */
    public Class getRuleInterfaceClass() {
        return AddPendingBudgetGeneralLedgerLineRule.class;
    }

    /**
     * @see org.kuali.rice.kns.rule.event.KualiDocumentEvent#invokeRuleMethod(org.kuali.rice.kns.rule.BusinessRule)
     */
    public boolean invokeRuleMethod(BusinessRule rule) {
        return ((AddBudgetConstructionDocumentRule) rule).processAddBudgetConstructionDocumentRules(budgetConstructionDocument);
    }

    /**
     * Gets the budgetConstructionDocument attribute. 
     * @return Returns the budgetConstructionDocument.
     */
    public BudgetConstructionDocument getBudgetConstructionDocument() {
        return budgetConstructionDocument;
    }

    /**
     * Sets the budgetConstructionDocument attribute value.
     * @param budgetConstructionDocument The budgetConstructionDocument to set.
     */
    public void setBudgetConstructionDocument(BudgetConstructionDocument budgetConstructionDocument) {
        this.budgetConstructionDocument = budgetConstructionDocument;
    }

}
