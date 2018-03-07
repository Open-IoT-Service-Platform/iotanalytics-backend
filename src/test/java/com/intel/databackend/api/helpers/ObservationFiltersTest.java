package com.intel.databackend.api.helpers;

import com.intel.databackend.datastructures.Observation;
import com.intel.databackend.api.helpers.ObservationFilters;
import com.intel.databackend.datastructures.ComponentDataType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObservationFiltersTest {
    private Observation [] observationsWithAttributes;
    private ObservationFilters observationFilters;

    @Before
    public void mockObservationFilters(){
        observationFilters = new ObservationFilters();
    }

    @Before
    public void mock(){
        List<Double> loc_ = new ArrayList<Double>();
        loc_.add(1.1);
        loc_.add(1.2);
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("key0", "value0");
        attributes.put("key1", "value1");
        Observation[] observations1_ = {new Observation("aid", "cid", 10L, "200.0", loc_, attributes)};
        observationsWithAttributes  = observations1_;
    }

    @Test
    public void filterByMeasurementAttrsTest(){
        Map<String, List<String>> measurementAttributeFilter = new HashMap<>();
        List<String> values = new ArrayList<>();
        values.add("value0");
        values.add("value100");
        measurementAttributeFilter.put("key0", values);
        Observation [] filtered = observationFilters.filterByMeasurementAttrs(observationsWithAttributes,measurementAttributeFilter);
        Assert.assertEquals(1, filtered.length);
    }

    @Test
    public void filterByMeasurementAttrsTestWithNoFilters(){
        Map<String, List<String>> measurementAttributeFilter = new HashMap<>();
        List<String> values = new ArrayList<>();
        values.add("value10");
        values.add("value100");
        measurementAttributeFilter.put("key0", values);
        Observation [] filtered = observationFilters.filterByMeasurementAttrs(observationsWithAttributes,measurementAttributeFilter);
        Assert.assertEquals(0, filtered.length);
    }

    @Test
    public void filterByValueTest(){
        Map<String, List<String>> valueFilter = new HashMap<>();
        List<String> values = new ArrayList<>();
        values.add("200.0");
        values.add("201.0");
        valueFilter.put("value", values);
        ComponentDataType componentDataType = new ComponentDataType();
        Observation [] filtered = observationFilters.filterByValue(observationsWithAttributes,valueFilter,componentDataType);
        Assert.assertEquals(1, filtered.length);
    }

    @Test
    public void filterByValueTestWithNullComponentDataType(){
        Map<String, List<String>> valueFilter = new HashMap<>();
        List<String> values = new ArrayList<>();
        values.add("200.0");
        values.add("201.0");
        valueFilter.put("value", values);
        Observation [] filtered = observationFilters.filterByValue(observationsWithAttributes,valueFilter,null);
        Assert.assertEquals(1, filtered.length);
    }

    @Test
    public void filterByValueTestWithNullValueFilter(){
        Map<String, List<String>> valueFilter = new HashMap<>();
        List<String> values = new ArrayList<>();
        valueFilter.put("value", values);
        Observation [] filtered = observationFilters.filterByValue(observationsWithAttributes,valueFilter,null);
        Assert.assertEquals(0, filtered.length);
    }
}
