package com.intel.databackend.api.helpers;

import com.intel.databackend.datastructures.Observation;
import com.intel.databackend.api.helpers.Common;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CommonTest {
    private Observation [] observationsWithTowLoc;
    private Observation [] observationsWithNoLoc;
    private Observation observation;
    private Observation observationWithNoLoc;

    @Before
    public void mock(){
        List<Double> loc_ = new ArrayList<Double>();
        loc_.add(1.1);
        loc_.add(1.2);
        Observation [] observations1_ = {new Observation("aid", "cid", 10L, "200.0", loc_)};
        observationsWithTowLoc = observations1_;
        Observation [] observations2_ = {new Observation("aid", "cid", 10L, "200.0")};
        observationsWithNoLoc = observations2_;
        observation = observations1_[0];
        observationWithNoLoc = observations2_[0];
    }

    @Test
    public void getMaxCoordinatesCountTestWith2Loc() {
        int retMaxCoordinatesCount = Common.getMaxCoordinatesCount(observationsWithTowLoc);
        Assert.assertEquals(2, retMaxCoordinatesCount);
    }

    @Test
    public void getMaxCoordinatesCountTestWithNoLoc() {
        int retMaxCoordinatesCount = Common.getMaxCoordinatesCount(observationsWithNoLoc);
        Assert.assertEquals(0, retMaxCoordinatesCount);
    }

    @Test
    public void addObservationLocationTest() {
        List<String> samples = new ArrayList<String>();
        List<String> expected = new ArrayList<String>();
        expected.add("1.1");
        expected.add("1.2");
        int maxCoodinatesCount = 2;
        Common.addObservationLocation(observation, samples, maxCoodinatesCount);
        Assert.assertTrue(expected.equals(samples));
    }
}
