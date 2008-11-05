<%--
 Copyright 2006-2007 The Kuali Foundation.
 
 Licensed under the Educational Community License, Version 1.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.opensource.org/licenses/ecl1.php
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ include file="/jsp/kfs/kfsTldHeader.jsp"%>
<%@ attribute name="hasRelatedCashControlDocument" required="true" description="If has related cash control document"%>
<%@ attribute name="readOnly" required="true" description="If document is in read only mode"%>
<%@ attribute name="isCustomerSelected" required="true" description="Whether or not the customer is set" %>


    <kul:tab tabTitle="Quick Apply to Invoice"
        defaultOpen="${isCustomerSelected}"
        tabErrorKey="${KFSConstants.PAYMENT_APPLICATION_DOCUMENT_ERRORS}">
        <div class="tab-container" align="center">

            <c:choose>
                <c:when
                    test="${!isCustomerSelected}">
                    No Customer Selected
                </c:when>
                <c:otherwise>
                    <table width="100%" cellpadding="0" cellspacing="0"
                        class="datatable">
                        <tr>
                            <th>
                                Invoice Number
                            </th>
                            <th>
                                Open Amount
                            </th>
                            <th>
                                Quick Apply
                            </th>
                        </tr>
                        <c:forEach items="${KualiForm.updatedBalanceInvoices}"
                            var="updatedBalanceInvoice">
                            <tr>
                                <td>
                                    <c:out value="${updatedBalanceInvoice.invoice.documentNumber}" />
                                </td>
                                <td style="text-align: right;">
                                    $
                                    <c:out value="${updatedBalanceInvoice.openAmount}" />
                                </td>
                                <td>
                                	<center>
	                                    <input type="checkbox" name="quickApply"
	                                        value="${updatedBalanceInvoice.invoice.documentNumber}" />
	                                </center>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td colspan='3' style='text-align: right;'>
                                <html:image property="methodToCall.quickApply"
                                    src="${ConfigProperties.externalizable.images.url}tinybutton-load.gif"
                                    alt="Quick Apply" title="Quick Apply" styleClass="tinybutton" />
                            </td>
                        </tr>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </kul:tab>