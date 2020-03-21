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

package com.oisp.databackend.datasources;


import com.oisp.databackend.config.oisp.OispConfig;
import com.oisp.databackend.datasources.objectstore.ObjectStoreAccess;
import com.oisp.databackend.datasources.tsdb.TsdbQuery;
import com.oisp.databackend.datastructures.Aggregation;
import com.oisp.databackend.datastructures.Observation;
import com.oisp.databackend.exceptions.ConfigEnvironmentException;
import com.oisp.databackend.datasources.tsdb.TsdbAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
@Configuration
public class DataDaoImpl implements DataDao {

    private static final Logger logger = LoggerFactory.getLogger(DataDaoImpl.class);

    private TsdbAccess tsdbAccess;
    private ObjectStoreAccess objectStoreAccess;

    private List<DataType.Types> supportedTsdbTypes;

    @Autowired
    private OispConfig oispConfig;

    @Autowired
    private ApplicationContext context;

    @Autowired
    DataDaoImpl() {
        logger.info("Dao created!");
    }

    @Autowired
    public void selectDAOPlugin() throws ConfigEnvironmentException {
        String tsdbName = oispConfig.getBackendConfig().getTsdbName();
        if (oispConfig.OISP_BACKEND_TSDB_NAME_DUMMY.equals(tsdbName)) {
            logger.info("TSDB backend: dummy");
            this.tsdbAccess = (TsdbAccess) context.getBean("tsdbAccessDummy");
        } else if (oispConfig.OISP_BACKEND_TSDB_NAME_HBASE.equals(tsdbName)) {
            logger.info("TSDB backend: hbase");
            this.tsdbAccess = (TsdbAccess) context.getBean("tsdbAccessHBase");
        } else if (oispConfig.OISP_BACKEND_TSDB_NAME_OPENTSDB.equals(tsdbName)) {
            logger.info("TSDB backend: openTSDB");
            this.tsdbAccess = (TsdbAccess) context.getBean("tsdbAccessOpenTsdb");
        } else if (oispConfig.OISP_BACKEND_TSDB_NAME_KAIROSDB.equals(tsdbName)) {
            logger.info("TSDB backend: kairosDB");
            this.tsdbAccess = (TsdbAccess) context.getBean("tsdbAccessKairosDb");
        } else {
            throw new ConfigEnvironmentException("Could not find the tsdb backend with name " + tsdbName);
        }
        supportedTsdbTypes = this.tsdbAccess.getSupportedDataTypes();
    }

    @Autowired
    public void selectObjectStoragePlugin() throws ConfigEnvironmentException {
        this.objectStoreAccess = null;
        String objectStoreName = oispConfig.getBackendConfig().getObjectStoreName();
        if (objectStoreName == null || objectStoreName.isEmpty()) {
            return;
        }
        if (oispConfig.OISP_BACKEND_OBJECT_STORE_MINIO.equals(objectStoreName)) {
            logger.info("Object store backend: minio");
            this.objectStoreAccess = (ObjectStoreAccess) context.getBean("objectAccessMinio");
        } else {
            throw new ConfigEnvironmentException("Could not find the object store backend with name " + objectStoreName);
        }
    }

    @Override
    public boolean put(final Observation[] observations) {

        List<String> supportedTsdbTypesAsString = DataType.getTypesStringList(supportedTsdbTypes);
        //filter out the observations which the tsdb backend supports
        Predicate<Observation> supportedPred = o -> supportedTsdbTypesAsString.stream().anyMatch(s -> s.equals(o.getDataType()));
        Predicate<Observation> unsupportedPred = o -> !supportedTsdbTypesAsString.stream().anyMatch(s -> s.equals(o.getDataType()));
        List<Observation> supportedObservations = Arrays.asList(observations).stream().filter(supportedPred)
                .collect(Collectors.toList());
        List<Observation> unsupportedObservations = Arrays.asList(observations).stream().filter(unsupportedPred)
                .collect(Collectors.toList());

        boolean result = true;
        if (!supportedObservations.isEmpty()) {
            result = tsdbAccess.put(supportedObservations, false);
        }
        if (!unsupportedObservations.isEmpty()) {
            tsdbAccess.put(unsupportedObservations, true);
            result &=  objectStoreAccess.put(unsupportedObservations);
        }

        return result;
    }

    @Override
    public Observation[] scan(String accountId, String componentId, String componentType, long start, long stop, Boolean gps, String[] attributeList) {
        logger.debug("Scanning TSDB: acc: {} cid: {} start: {} stop: {} gps: {}", accountId, componentId, start, stop, gps);
        TsdbQuery tsdbQuery = new TsdbQuery()
                .withAid(accountId)
                .withCid(componentId)
                .withComponentType(componentType)
                .withLocationInfo(gps)
                .withAttributes(attributeList)
                .withStart(start)
                .withStop(stop);
        Observation[] observations = tsdbAccess.scan(tsdbQuery);
        if (observations != null && observations.length > 0) {
            //Check whether dataType is not supported by tsdb
            DataType.Types type = DataType.getType(componentType);
            Boolean isUncovered = !DataType.getUncoveredDataTypes(supportedTsdbTypes)
                    .stream()
                    .filter(t -> t == type)
                    .collect(Collectors.toSet()).isEmpty();
            if (isUncovered) {
                objectStoreAccess.get(observations);
            }
        }
        return observations;
    }

    @Override
    public Observation[] scan(String accountId, String componentId, String componentType, long start, long stop, Boolean gps,
                              String[] attributeList, boolean forward, int limit) {
        logger.debug("Scanning TSDB: acc: {} cid: {} start: {} stop: {} gps: {} with limit: {}",
                accountId, componentId, start, stop, gps, limit);
        TsdbQuery tsdbQuery = new TsdbQuery()
                .withAid(accountId)
                .withCid(componentId)
                .withComponentType(componentType)
                .withLocationInfo(gps)
                .withAttributes(attributeList)
                .withStart(start)
                .withStop(stop);
        return tsdbAccess.scan(tsdbQuery, forward, limit);
    }

    @Override
    public String[] scanForAttributeNames(String accountId, String componentId, long start, long stop) throws IOException {

        logger.debug("Scanning TSD: acc: {} cid: {} start: {} stop: {}", accountId, componentId, start, stop);
        TsdbQuery tsdbQuery = new TsdbQuery()
                .withAid(accountId)
                .withCid(componentId)
                .withStart(start)
                .withStop(stop);
        return tsdbAccess.scanForAttributeNames(tsdbQuery);
    }

    @Override
    public List<DataType.Types> getSupportedDataTypes() {
        // if all is covered by TSDB - no need to look at object store
        // if no objectStore is defined, return tsdbAccess
        if (DataType.getUncoveredDataTypes(tsdbAccess.getSupportedDataTypes()).isEmpty() || objectStoreAccess == null) {
            return tsdbAccess.getSupportedDataTypes();
        } else {
            // if there are gaps in TSDB but there is an object store, it is used for all backup cases which TSDB
            // does not support, so all is covered
            return DataType.getAllTypes();
        }
    }

    @Override
    public Long count(String accountId, List<String> componentIds, List<String> componentTypes, long start, long stop, Boolean gps, String[] attributes) {
        TsdbQuery tsdbQuery = new TsdbQuery()
                .withAid(accountId)
                .withCids(componentIds)
                .withComponentTypes(componentTypes)
                .withLocationInfo(gps)
                .withAttributes(attributes)
                .withStart(start)
                .withStop(stop)
                .withAggregation(Aggregation.Type.COUNT);
        return tsdbAccess.count(tsdbQuery);
    }
}
