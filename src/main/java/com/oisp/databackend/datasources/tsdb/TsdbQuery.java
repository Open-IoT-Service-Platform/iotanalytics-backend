package com.oisp.databackend.datasources.tsdb;

import com.oisp.databackend.datastructures.Aggregation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("PMD.TooManyMethods")
public class TsdbQuery {
    private String aid;
    private List<String> cid;
    private List<String> componentTypes;
    private List<String>  attributes;
    private boolean locationInfo;
    private long start;
    private long stop;
    private Aggregation.Type aggregation;

    public TsdbQuery(String aid, String cid, List<String> attributes, boolean locationInfo, long start, long stop) {
        this.aid = aid;
        this.cid = Arrays.asList(cid);
        this.componentTypes = new ArrayList<>();
        this.attributes = attributes;
        this.locationInfo = locationInfo;
        this.start = start;
        this.stop = stop;
        this.aggregation = Aggregation.Type.NONE;
    }
    public TsdbQuery(String aid, List<String> cids, List<String> attributes, boolean locationInfo, long start, long stop) {
        this.aid = aid;
        this.cid = cids;
        this.componentTypes = new ArrayList<>();
        this.attributes = attributes;
        this.locationInfo = locationInfo;
        this.start = start;
        this.stop = stop;
        this.aggregation = Aggregation.Type.NONE;
    }

    public TsdbQuery() {
        this.locationInfo = false;
        this.componentTypes = new ArrayList<>();
        this.attributes = new ArrayList<>();
        this.cid = new ArrayList<>();
    }

    public TsdbQuery withAid(String aid) {
        this.aid = aid;
        return this;
    }

    public TsdbQuery withCid(String cid) {
        this.cid.add(cid);
        return this;
    }

    public TsdbQuery withCids(List<String> cids) {
        this.cid = cids;
        return this;
    }

    public TsdbQuery withComponentType(String componentType) {
        this.componentTypes.add(componentType);
        return this;
    }

    public TsdbQuery withComponentTypes(List<String> componentTypes) {
        this.componentTypes = componentTypes;
        return this;
    }

    public List<String> getComponentTypes() {
        return componentTypes;
    }

    public void setComponentTypes(List<String> componentTypes) {
        this.componentTypes = componentTypes;
    }

    public TsdbQuery withAttributes(List<String> attributes) {
        this.attributes = attributes;
        return this;
    }

    public TsdbQuery withAttributes(String[] attributes) {
        if (attributes == null) {
            this.attributes = new ArrayList<>();
            return this;
        }
        this.attributes = new ArrayList<String>(Arrays.asList(attributes));
        return this;
    }

    public TsdbQuery withLocationInfo(boolean locationInfo) {
        this.locationInfo = locationInfo;
        return this;
    }

    public TsdbQuery withStart(long start) {
        this.start = start;
        return this;
    }

    public TsdbQuery withStop(long stop) {
        this.stop = stop;
        return this;
    }

    public TsdbQuery withAggregation(Aggregation.Type aggr) {
        this.aggregation = aggr;
        return this;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public void setCid(String cid) {
        this.cid.add(cid);
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public void setLocationInfo(boolean locationInfo) {
        this.locationInfo = locationInfo;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setStop(long stop) {
        this.stop = stop;
    }

    public String getAid() {

        return aid;
    }

    public String getCid() {
        return cid.get(0);
    }
    public List<String> getCids() {
        return cid;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public boolean isLocationInfo() {
        return locationInfo;
    }

    public long getStart() {
        return start;
    }

    public long getStop() {
        return stop;
    }

    public String getComponentType() {
        return componentTypes.get(0);
    }

    public void setComponentType(String componentType) {
        this.componentTypes.add(componentType);
    }

    public Aggregation.Type getAggregation() {
        return aggregation;
    }

    public void setAggregation(Aggregation.Type aggregation) {
        this.aggregation = aggregation;
    }
}
