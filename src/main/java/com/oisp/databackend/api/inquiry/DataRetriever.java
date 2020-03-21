/**
 * Copyright (c) 2015 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oisp.databackend.api.inquiry;

import com.oisp.databackend.api.inquiry.advanced.filters.ObservationFilterSelector;
import com.oisp.databackend.datasources.DataDao;
import com.oisp.databackend.datastructures.ComponentDataType;
import com.oisp.databackend.datastructures.Observation;
import com.oisp.databackend.exceptions.IllegalDataInquiryArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DataRetriever {

    private static final String errINVCOMPTYPE = "Invalid ComponentType.";
    private static final Logger logger = LoggerFactory.getLogger(DataRetriever.class);

    private final DataDao hbase;

    private Map<String, Observation[]> componentObservations;

    private Long rowCount;

    private final DataRetrieveParams dataRetrieveParams;


    public DataRetriever(DataDao hbase, DataRetrieveParams dataRetrieveParams) {
        this.hbase = hbase;
        this.dataRetrieveParams = dataRetrieveParams;
    }

    public void retrieveAndCount(ObservationFilterSelector filter) throws IllegalDataInquiryArgumentException {
        Map<String, Observation[]> componentObservations = new HashMap<>();
        Collection<String> components = dataRetrieveParams.getComponentsMetadata().keySet();
        rowCount = 0L;
        for (String component : components) {
            if (dataRetrieveParams.getComponentsMetadata().get((String) component) == null
                     || !dataRetrieveParams.getComponentsMetadata().get((String) component).isValidType()) {
                throw new IllegalDataInquiryArgumentException(errINVCOMPTYPE);
            }
            Observation[] observations = hbase.scan(dataRetrieveParams.getAccountId(),
                    component,
                    dataRetrieveParams.getComponentsMetadata().get((String) component).getDataType(),
                    dataRetrieveParams.getStartDate(),
                    dataRetrieveParams.getEndDate(),
                    dataRetrieveParams.isQueryMeasureLocation(),
                    dataRetrieveParams.getComponentsAttributes());
            if (observations == null) {
                logger.debug("No observations retrieved for component: {}", component);
                continue;
            }
            observations = filter.filter(observations, getComponentMetadata(component));
            componentObservations.put(component, observations);
            updateRowCount(observations);
        }
        this.componentObservations = componentObservations;
    }

    public void countOnly(ObservationFilterSelector filter) throws IllegalDataInquiryArgumentException {

        List<String> components = new ArrayList<>(dataRetrieveParams.getComponentsMetadata().keySet());
        List<String> componentTypes = new ArrayList<>();
        rowCount = 0L;
        for (String component : components) {
            if (dataRetrieveParams.getComponentsMetadata().get((String) component) == null
                    || !dataRetrieveParams.getComponentsMetadata().get((String) component).isValidType()) {
                throw new IllegalDataInquiryArgumentException(errINVCOMPTYPE);
            }
            componentTypes.add(dataRetrieveParams.getComponentsMetadata().get((String) component).getDataType());
        }
        Long count = hbase.count(dataRetrieveParams.getAccountId(),
                components,
                componentTypes,
                dataRetrieveParams.getStartDate(),
                dataRetrieveParams.getEndDate(),
                dataRetrieveParams.isQueryMeasureLocation(),
                dataRetrieveParams.getComponentsAttributes());
        updateRowCountOnly(count);
        this.componentObservations = null;
    }

    public Map<String, Observation[]> getComponentObservations() {
        return componentObservations;
    }

    public Long getRowCount() {
        return rowCount;
    }

    private void updateRowCount(Observation[] obs) {
        rowCount += obs.length;
    }
    private void updateRowCountOnly(Long count) {
        rowCount += count;
    }

    private ComponentDataType getComponentMetadata(String component) {
        if (dataRetrieveParams.getComponentsMetadata() != null) {
            return dataRetrieveParams.getComponentsMetadata().get(component);
        }
        return null;
    }
}
