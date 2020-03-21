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

package com.oisp.databackend.datasources.tsdb.kairosdb;

import com.oisp.databackend.config.oisp.OispConfig;
import com.oisp.databackend.datasources.DataFormatter;
import com.oisp.databackend.datasources.DataType;
import com.oisp.databackend.datasources.tsdb.TsdbAccess;
import com.oisp.databackend.datasources.tsdb.TsdbQuery;
import com.oisp.databackend.datasources.tsdb.kairosdb.kairosdbapi.Aggregator;
import com.oisp.databackend.datasources.tsdb.kairosdb.kairosdbapi.Query;
import com.oisp.databackend.datasources.tsdb.kairosdb.kairosdbapi.QueryResponse;
import com.oisp.databackend.datasources.tsdb.kairosdb.kairosdbapi.RestApi;
import com.oisp.databackend.datasources.tsdb.kairosdb.kairosdbapi.Sampling;
import com.oisp.databackend.datasources.tsdb.kairosdb.kairosdbapi.SubQuery;

import com.oisp.databackend.datastructures.Observation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Repository
public class TsdbAccessKairosDb implements TsdbAccess {

    private static final Logger logger = LoggerFactory.getLogger(TsdbAccessKairosDb.class);
    private static final String STAR = "*";
    private static final Long MAX_YEARS_COUNT_AGGREGATION = 10L; //max count over 10 years
    private static final Long MAX_NUMBER_OF_SAMPLES = 60L * 60 * 24 * 365 * 10; // 10 years of 1Hz samples
    private RestApi api;
    @Autowired
    private OispConfig oispConfig;


    @PostConstruct
    public void init() throws URISyntaxException {
        //Make sure that this bean is only initiated when needed
        if (!oispConfig.getBackendConfig()
                .getTsdbName()
                .equals(oispConfig.OISP_BACKEND_TSDB_NAME_KAIROSDB)) {
            return;
        }
        api = new RestApi(oispConfig);
    }

    @Override
    public boolean put(List<Observation> observations, boolean onlyMetadata) {
        List<TsdbObject> tsdbObjects = TsdbObjectBuilder.createTsdbObjectsFromObservations(observations, onlyMetadata);

        return api.put(tsdbObjects, true);
    }

    @Override
    public boolean put(Observation observation, boolean onlyMetadata) {

        List<Observation> list = new ArrayList<Observation>();
        list.add(observation);

        return put(list, onlyMetadata);
    }

    @Override
    public Observation[] scan(TsdbQuery tsdbQuery) {
        SubQuery subQuery = new SubQuery()
                //.withAggregator(SubQuery.AGGREGATOR_NONE)
                .withMetric(DataFormatter.createMetric(tsdbQuery.getAid(),
                        tsdbQuery.getCid()));
        List<String> types = new ArrayList<String>();
        List<String> tagNames = new ArrayList<String>();
        tagNames.add(TsdbObjectBuilder.TYPE);
        types.add(TsdbObjectBuilder.VALUE);
        if (tsdbQuery.isLocationInfo()) {
            types.add(DataFormatter.gpsValueToString(0));
            types.add(DataFormatter.gpsValueToString(1));
            types.add(DataFormatter.gpsValueToString(2));
        }
        if (!tsdbQuery.getAttributes().isEmpty()) {
            List<String> requestedTags = new ArrayList<String>();
            if (tsdbQuery.getAttributes().get(0).equals(STAR)) {
                // tags are not known yet, but all tags are requested
                // Therefore we need to query all relevant tags from Kairosdb
                Query query = new Query().withStart(tsdbQuery.getStart()).withEnd(tsdbQuery.getStop());
                query.addQuery(subQuery);
                QueryResponse queryResponses = api.queryTags(query);
                if (queryResponses != null) {
                    requestedTags = new ArrayList<String>(queryResponses.getQueries().get(0).getResults().get(0).getTags().keySet());
                }
            } else {
                // tags are given explicitly. No additional tag query needed
                requestedTags = tsdbQuery.getAttributes();
            }
            List<String> mergedNames = Stream.of(tagNames, requestedTags).flatMap(x -> x.stream()).collect(Collectors.toList());
            tagNames = mergedNames;
        }
        subQuery.withTag(TsdbObjectBuilder.TYPE, types);
        subQuery.withGroupByTags(tagNames);

        Query query = new Query().withStart(tsdbQuery.getStart()).withEnd(tsdbQuery.getStop());
        query.addQuery(subQuery);

        QueryResponse queryResponses = api.query(query);
        if (queryResponses == null) {
            return null;
        }
        return ObservationBuilder.createObservationFromQueryResponses(queryResponses, tsdbQuery);
    }

    @Override
    public Observation[] scan(TsdbQuery tsdbQuery, boolean forward, int limit) {
        return new Observation[0];
    }

    @Override
    public Long count(TsdbQuery tsdbQuery) {

        List<SubQuery> subQueries = tsdbQuery
                .getCids()
                .stream()
                .map(item -> new SubQuery()
                    .withAggregator(new Aggregator(Aggregator.AGGREGATOR_COUNT)
                        .withSampling(new Sampling(MAX_YEARS_COUNT_AGGREGATION, "years")))
                    .withMetric(DataFormatter.createMetric(tsdbQuery.getAid(),
                        item))).collect(Collectors.toList());


        // Tag list contains at least "type = value" to count only "true" values (and e.g. not gps coordinates)
        // When more attributes are given, only the samples additionally containing the attribute are count.
        // e.g. when sample1 has attribute(key1=value1) and sample2 (key2=value2) and sample3 (key2=value3) then if key2
        // is given only sample2 and sample3 are considered
        Set<String> tags = new HashSet<String>();
        tags.add(TsdbObjectBuilder.VALUE);

        for (SubQuery subquery : subQueries) {
            Set<String> tagSet = new HashSet<String>(tsdbQuery.getAttributes());
            tags.addAll(tagSet);
        }

        Query query = new Query()
                .withStart(tsdbQuery.getStart())
                .withEnd(tsdbQuery.getStop());
        query.addQueries(subQueries);
        QueryResponse queryResponses = api.query(query);
        if (queryResponses == null) {
            return 0L;
        }
        return queryResponses.getQueries().stream()
                .flatMap(x -> x.getResults().stream())
                .flatMap(qr -> qr.getValues().stream())
                .map(obj -> Long.valueOf((Integer) obj[1]))
                .reduce(0L, (e1, e2) -> e1 + e2);
    }
    
    public String[] scanForAttributeNames(TsdbQuery tsdbQuery) throws IOException {
        return new String[]{STAR};
    }

    @Override
    public List<DataType.Types> getSupportedDataTypes() {
        return Arrays.asList(DataType.Types.Boolean, DataType.Types.Number, DataType.Types.String);
    }
}
