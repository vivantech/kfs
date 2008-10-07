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
package org.kuali.kfs.module.cab.document.web;

import java.util.List;

import org.kuali.kfs.module.cab.CabPropertyConstants.Pretag;
import org.kuali.kfs.module.cab.businessobject.GeneralLedgerEntry;
import org.kuali.kfs.module.cab.businessobject.PurchasingAccountsPayableActionHistory;
import org.kuali.rice.kns.util.TypedArrayList;

public class PurApLineSession {
    private List<PurchasingAccountsPayableActionHistory> actionsTakenHistory;
    private List<GeneralLedgerEntry> glEntryUpdateList;

    public PurApLineSession() {
        actionsTakenHistory = new TypedArrayList(PurchasingAccountsPayableActionHistory.class);
        glEntryUpdateList = new TypedArrayList(GeneralLedgerEntry.class);
    }

    
    /**
     * Gets the glEntryList attribute. 
     * @return Returns the glEntryList.
     */
    public List<GeneralLedgerEntry> getGlEntryUpdateList() {
        return glEntryUpdateList;
    }


    /**
     * Sets the glEntryList attribute value.
     * @param glEntryList The glEntryList to set.
     */
    public void setGlEntryUpdateList(List<GeneralLedgerEntry> glEntryList) {
        this.glEntryUpdateList = glEntryList;
    }


    /**
     * Gets the actionsTakenHistory attribute.
     * 
     * @return Returns the actionsTakenHistory.
     */
    public List<PurchasingAccountsPayableActionHistory> getActionsTakenHistory() {
        return actionsTakenHistory;
    }

    /**
     * Sets the actionsTakenHistory attribute value.
     * 
     * @param actionsTakenHistory The actionsTakenHistory to set.
     */
    public void setActionsTakenHistory(List<PurchasingAccountsPayableActionHistory> actionsTakenHistory) {
        this.actionsTakenHistory = actionsTakenHistory;
    }
}
