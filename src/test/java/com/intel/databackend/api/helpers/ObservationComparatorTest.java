package com.intel.databackend.api.helpers;

import com.intel.databackend.datastructures.ComponentDataType;
import com.intel.databackend.datastructures.Observation;
import com.intel.databackend.api.helpers.ObservationComparator;
import org.junit.Before;
import org.junit.Test;
import scala.util.parsing.combinator.testing.Str;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObservationComparatorTest {
    private ObservationComparator observationComparator;
    private ObservationComparator observationComparatorWithNotNumberType;
    private Observation observation1;
    private Observation observation2;


    @Before
    public void mock(){
        List<Map<String, String>> sort = new ArrayList<>();
        String value = "Value";
        String timestamp = "Timestamp";
        String asc = "Asc";
        String desc = "Desc";
        String dataType = "Number";
        String notNumberDataType = "NotNumber";
        Map<String, String> value_asc = new HashMap<>();
        Map<String, String> value_desc = new HashMap<>();
        Map<String, String> timestamp_asc = new HashMap<>();
        Map<String, String> timestamp_desc = new HashMap<>();
        value_asc.put(value, asc);
        value_desc.put(value, desc);
        timestamp_asc.put(timestamp, asc);
        timestamp_desc.put(timestamp, desc);
        sort.add(value_asc);
        sort.add(value_desc);
        sort.add(timestamp_asc);
        sort.add(timestamp_desc);
        ComponentDataType componentDataType = new ComponentDataType();
        componentDataType.setDataType(dataType);
        observationComparator = new ObservationComparator(sort, componentDataType);
        componentDataType.setDataType(notNumberDataType);
        observationComparatorWithNotNumberType = new ObservationComparator(sort, componentDataType);
        observation1 = new Observation("aid", "cid", 10L, "200.0");
        observation2 = new Observation("aid", "cid", 11L, "201.0");
    }

    @Test
    public void compareTest(){
        observationComparator.compare(observation1, observation2);
    }

    @Test
    public void compareTestWithNotNumberType(){
        observationComparatorWithNotNumberType.compare(observation1, observation2);
    }
}
