/*
 * Copyright 2012 The Kuali Foundation.
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
package org.kuali.kfs.module.tem.businessobject.options;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.kuali.kfs.module.tem.TemConstants;
import org.kuali.kfs.module.tem.businessobject.PrimaryDestination;
import org.kuali.kfs.module.tem.service.TravelService;
import org.kuali.kfs.sys.context.SpringContext;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.BusinessObjectService;

public class PrimaryDestinationValuesFinder extends KeyValuesBase {

    /**
     * @see org.kuali.rice.krad.keyvalues.KeyValuesFinder#getKeyValues()
     */
    @SuppressWarnings("rawtypes")
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("", ""));
        BusinessObjectService service = SpringContext.getBean(BusinessObjectService.class);
        TravelService travelService = SpringContext.getBean(TravelService.class);
        Map<String,String> fieldValues = new HashMap<String, String>();

        List<PrimaryDestination> primaryDestinationsInternational = travelService.findAllDistinctPrimaryDestinations(TemConstants.TEMTripTypes.INTERNATIONAL);
        Collections.sort(primaryDestinationsInternational,new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                PrimaryDestination pd1 = (PrimaryDestination) o1;
                PrimaryDestination pd2 = (PrimaryDestination) o2;
                if (pd1.getCountryStateName() != null && pd2.getCountryStateName() != null){
                    return pd1.getCountryStateName().compareTo(pd2.getCountryStateName());
                }
                else{
                    return 0;
                }
            }
        });
        List<PrimaryDestination> primaryDestinationsDomestic = travelService.findAllDistinctPrimaryDestinations(TemConstants.TEMTripTypes.DOMESTIC);
        Collections.sort(primaryDestinationsDomestic,new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                PrimaryDestination pd1 = (PrimaryDestination) o1;
                PrimaryDestination pd2 = (PrimaryDestination) o2;
                if (pd1.getCountryStateName() != null && pd2.getCountryStateName() != null){
                    return pd1.getCountryStateName().compareTo(pd2.getCountryStateName());
                }
                else{
                    return 0;
                }
            }
        });

        Iterator<PrimaryDestination> it = primaryDestinationsDomestic.iterator();

        String key = "";
        while (it.hasNext()){
            PrimaryDestination primaryDestination = it.next();
            //skip dummy value for custom expenses
            if (primaryDestination.getId().intValue() == TemConstants.CUSTOM_PER_DIEM_ID
                    || primaryDestination.getCountryStateName().equals(TemConstants.CONUS)
                    || primaryDestination.getCountryState().equals(TemConstants.ALL_STATES)){
                continue;
            }
            String tempKey = primaryDestination.getCountryStateName();
            if (!tempKey.equals(key)){
                keyValues.add(new ConcreteKeyValue(primaryDestination.getCountryState().toUpperCase(), primaryDestination.getCountryStateName().toUpperCase()));
            }
            key = tempKey;
        }
        keyValues.add(new ConcreteKeyValue("---", "------------------------------------------"));
        it =primaryDestinationsInternational.iterator();
        while (it.hasNext()){
            PrimaryDestination primaryDestination = it.next();
            //skip dummy value for custom expenses
            if (primaryDestination.getId().intValue() == TemConstants.CUSTOM_PER_DIEM_ID
                    || primaryDestination.getCountryStateName().equals(TemConstants.CONUS)
                    || primaryDestination.getCountryState().equals(TemConstants.ALL_STATES)){
                continue;
            }
            String tempKey = primaryDestination.getCountryStateName();
            if (!tempKey.equals(key)){
                keyValues.add(new ConcreteKeyValue(primaryDestination.getCountryState().toUpperCase(), primaryDestination.getCountryStateName().toUpperCase()));
            }
            key = tempKey;
        }


        return keyValues;
    }
}