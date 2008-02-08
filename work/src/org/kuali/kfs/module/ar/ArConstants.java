/*
 * Copyright 2007 The Kuali Foundation.
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
package org.kuali.module.ar;

public class ArConstants {
    
    public static final String INSTITUTION_NAME = "INSTITUTION_NAME";
    public static final String ERROR_ORGANIZATION_DOC_NUMBER_CANNOT_BE_NULL_FOR_PAYMENT_MEDIUM_CASH = "error.ar.OrganizationDocNumberCannotBeNullforPaymentMediumCash";
    public static final String ERROR_REFERENCE_DOC_NUMBER_CANNOT_BE_NULL = "error.ar.ReferenceDocNumberCannotBeNull";
    
    //Valid number of days the invoice due date can be more than invoice creation date.
    public static final int VALID_NUMBER_OF_DAYS_INVOICE_DUE_DATE_PAST_INVOICE_DATE = 90;
    
    public static final String AR_SUPERVISOR_GROUP_NAME = "AR_ROLE_MAINTAINERS";
    
    public static final String NEW_CUSTOMER_INVOICE_DETAIL_ERROR_PATH_PREFIX = "newCustomerInvoiceDetail";
    
    public static final String CUSTOMER_INVOICE_DETAIL_UOM_DEFAULT = "EA";
}
