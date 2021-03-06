/*
 * The Kuali Financial System, a comprehensive financial management system for higher education.
 * 
 * Copyright 2005-2014 The Kuali Foundation
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.kuali.kfs.gl.dataaccess;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.kuali.kfs.gl.businessobject.Encumbrance;
import org.kuali.kfs.gl.businessobject.Transaction;

/**
 * A DAO interface that declares methods needed for Encumbrances to interact with the database
 */
public interface EncumbranceDao {
    /**
     * Returns an encumbrance that would be affected by the given transaction
     *
     * @param t the transaction to find the affected encumbrance for
     * @return an Encumbrance that would be affected by the posting of the transaction, or null
     */
    public Encumbrance getEncumbranceByTransaction(Transaction t);

    /**
     * Returns an Iterator of all encumbrances that need to be closed for the fiscal year
     *
     * @param fiscalYear a fiscal year to find encumbrances for
     * @return an Iterator of encumbrances to close
     */
    public Iterator getEncumbrancesToClose(Integer fiscalYear);

    /**
     * Returns an Iterator of all encumbrances that need to be closed for the fiscal year and specified charts
     *
     * @param fiscalYear a fiscal year to find encumbrances for
     * @param charts charts to find encumbrances for
     * @return an Iterator of encumbrances to close
     */
    public Iterator getEncumbrancesToClose(Integer fiscalYear, List<String> charts);

    /**
     * Purges the database of all those encumbrances with the given chart and year
     *
     * @param chartOfAccountsCode the chart of accounts code purged encumbrances will have
     * @param year the university fiscal year purged encumbrances will have
     */
    public void purgeYearByChart(String chartOfAccountsCode, int year);

    /**
     * fetch all encumbrance records from GL open encumbrance table
     *
     * @return an Iterator with all encumbrances currently in the database
     */
    public Iterator getAllEncumbrances();

    /**
     * group all encumbrances with/without the given document type code by fiscal year, chart, account, sub-account, object code,
     * sub object code, and balance type code, and summarize the encumbrance amount and the encumbrance close amount.
     *
     * @param documentTypeCode the given document type code
     * @param included indicate if all encumbrances with the given document type are included in the results or not
     * @return an Iterator of arrays of java.lang.Objects holding summarization data about qualifying encumbrances
     */
    public Iterator getSummarizedEncumbrances(String documentTypeCode, boolean included);

    /**
     * This method finds the open encumbrances according to input fields and values
     *
     * @param fieldValues the input fields and values
     * @param includeZeroEncumbrances should the query include encumbrances which have zeroed out?
     * @return a collection of open encumbrances
     */
    public Iterator findOpenEncumbrance(Map fieldValues, boolean includeZeroEncumbrances);

    /**
     * Counts the number of the open encumbrances according to input fields and values
     *
     * @param fieldValues the input fields and values
     * @param includeZeroEncumbrances should the query include encumbrances which have zeroed out?
     * @return the number of the open encumbrances
     */
    public Integer getOpenEncumbranceRecordCount(Map fieldValues, boolean includeZeroEncumbrances);

    /**
     * Builds query of open encumbrances that have the keys given in the map summarized by balance type codes
     * where the sum(ACCOUNT_LINE_ENCUMBRANCE_AMOUNT  - sum(ACCOUNT_LINE_ENCUMBRANCE_CLOSED_AMOUNT ) != 0
     * and returns true if there are any results.
     *
     * @param fieldValues the input fields and values
     * @param includeZeroEncumbrances
     * @return true if there any open encumbrances when summarized by balance type
     * @see org.kuali.kfs.gl.dataaccess.EncumbranceDao#hasSummarizedOpenEncumbranceRecords(java.util.Map)
     */
    public boolean hasSummarizedOpenEncumbranceRecords(Map fieldValues, boolean includeZeroEncumbrances);

    /**
     * @param year the given university fiscal year
     * @return count of rows for the given fiscal year
     */
    public Integer findCountGreaterOrEqualThan(Integer year);
}
