package com.oisp.databackend.datasources.tsdb.kairosdb.kairosdbapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class Query {
    private long startAbsolute;
    private long endAbsolute;
    private List<SubQuery> metrics;
    //private boolean msResolution;

    public Query() {
        this.metrics = new ArrayList<SubQuery>();
      //  this.msResolution = true;
    }
    public Query withStart(long start) {
        this.startAbsolute = start;
        return this;
    }

    public Query withEnd(long end) {
        this.endAbsolute = end;
        return this;
    }
    public void setEndAbsolute(long endAbsolute) {
        this.endAbsolute = endAbsolute;
    }

    public void setMetrics(List<SubQuery> metrics) {
        this.metrics = metrics;
    }

    public void setStartAbsolute(long startAbsolute) {
        this.startAbsolute = startAbsolute;
    }

    @JsonProperty("end_absolute")
    public long getEndAbsolute() {
        return endAbsolute;
    }

    @JsonProperty("start_absolute")
    public long getStartAbsolute() {
        return startAbsolute;
    }

    public List<SubQuery> getMetrics() {
        return metrics;
    }

    public void addQuery(SubQuery subQuery) {
        this.metrics.add(subQuery);
    }

    public void addQueries(List<SubQuery> subQueries) {
        this.metrics.addAll(subQueries);
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonObject = null;
        try {
            jsonObject = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
        return jsonObject;
    }
}
