package com.intel.databackend.api.inquiry.advanced.aggregations;

import com.intel.databackend.api.inquiry.advanced.aggregations.AggregationCalculator;
import com.intel.databackend.datastructures.Observation;
import com.intel.databackend.datastructures.ComponentDataType;

import java.util.Map;
import java.util.HashMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;

public class AggregationCalculatorTest {
    private AggregationCalculator aggregationCalculator;

    @Before
    public void mock(){
        String componentId = "1";
        List<Double> loc_ = new ArrayList<Double>();
        loc_.add(1.1);
        loc_.add(1.2);
        Observation [] observations1 = {new Observation("aid", "cid", 10L, "200.0", loc_)};
        Map<String, ComponentDataType> map = new HashMap<>();
        ComponentDataType value_type = new ComponentDataType();
        value_type.setComponentId(componentId);
        value_type.setComponentName("component name");
        value_type.setDataType(ComponentDataType.NUMBER);
        value_type.setFormat("format");
        map.put(componentId, value_type);
        aggregationCalculator = new AggregationCalculator(componentId, observations1, map);
    }

    @Test
    public  void generateAggregationsTest(){
        long first = 0;
        long last = 1;
        aggregationCalculator.generateAggregations(first, last);
    }

    @Test
    public void includeAggregationTest(){
        boolean result = aggregationCalculator.includeAggregation(AggregationCalculator.AggregationModes.only.toString());
        Assert.assertTrue(result);
    }

    @Test
    public void returnAggregationOnlyTest(){
        boolean result = aggregationCalculator.returnsAggregationOnly(AggregationCalculator.AggregationModes.only.toString());
        Assert.assertTrue(result);
    }
}
