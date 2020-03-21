package com.oisp.databackend.datasources.tsdb.kairosdb.kairosdbapi;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubQuery {

    public static final Long MAX_NUMBER_OF_SAMPLES = 1024L * 10;

    private List<Aggregator> aggregators;
    private String name;
    private Long limit;
    private Map<String, List<String>> tags;
    private List<GroupBy> groupBy;

    @JsonProperty("group_by")
    public List<GroupBy> getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(List<GroupBy> groupBy) {
        this.groupBy = groupBy;
    }

    public SubQuery withGroupByTags(List<String> tags) {
        GroupBy gb = new GroupBy();
        gb.setName("tag");
        gb.setTags(tags);
        this.groupBy.add(gb);
        return this;
    }

    public SubQuery() {
        tags = new HashMap<String, List<String>>();
        aggregators = new ArrayList<>();
        limit = MAX_NUMBER_OF_SAMPLES;
        groupBy = new ArrayList<GroupBy>();
    }

    public SubQuery withMetric(String metric) {
        this.name = metric;
        return this;
    }

    public SubQuery withAggregator(Aggregator aggregator) {
        this.aggregators.add(aggregator);
        return this;
    }

    public SubQuery withTag(String tagK, List<String> tagV) {
        this.tags.put(tagK, tagV);
        return this;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTags(Map<String, List<String>> tags) {
        this.tags = tags;
    }

    public Map<String, List<String>> getTags() {
        return tags;
    }

    public List<Aggregator> getAggregators() {
        return aggregators;
    }

    public void setAggregators(List<Aggregator> aggregators) {
        this.aggregators = aggregators;
    }

    public String getName() {
        return name;
    }
}
