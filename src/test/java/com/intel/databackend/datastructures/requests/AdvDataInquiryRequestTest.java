package com.intel.databackend.datastructures.requests;

import com.intel.databackend.datastructures.DeviceData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class AdvDataInquiryRequestTest {
    private AdvDataInquiryRequest advDataInquiryRequest;

    @Before
    public void initMocks(){
        advDataInquiryRequest = new AdvDataInquiryRequest();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAggregation(){
        String aggregation = "aggregation ";
        advDataInquiryRequest.setAggregations(aggregation);
        Assert.assertEquals(aggregation,advDataInquiryRequest.getAggregations() );
    }

    @Test
    public void testComponentRowLimit(){
        long rowLimit = 100L;
        advDataInquiryRequest.setComponentRowLimit(rowLimit);
        Assert.assertEquals(Long.valueOf(rowLimit),advDataInquiryRequest.getComponentRowLimit());
    }

    @Test
    public void testComponentRowStart(){
        long rowStart = 1;
        advDataInquiryRequest.setComponentRowStart(rowStart);
        Assert.assertEquals(Long.valueOf(rowStart), advDataInquiryRequest.getComponentRowStart());
    }

    @Test
    public void testCountOnly(){
        boolean countOnly=true;
        advDataInquiryRequest.setCountOnly(countOnly);
        Assert.assertEquals(countOnly, advDataInquiryRequest.isCountOnly());
    }

    @Test
    public void testShowMeasureLocation(){
        boolean showMeasureLocation = true;
        advDataInquiryRequest.setShowMeasureLocation(showMeasureLocation);
        Assert.assertEquals(showMeasureLocation, advDataInquiryRequest.getShowMeasureLocation());
    }

    @Test
    public void testMsgType(){
        String msgType = "msgType";
        advDataInquiryRequest.setMsgType(msgType);
        Assert.assertEquals(msgType, advDataInquiryRequest.getMsgType());
    }

    @Test
    public void testDevCompAttributeFilter(){
        HashMap<String, List<String>> devCompAttributeFilter = Mockito.mock(HashMap.class);
        advDataInquiryRequest.setDevCompAttributeFilter(devCompAttributeFilter);
        Assert.assertEquals(devCompAttributeFilter, advDataInquiryRequest.getDevCompAttributeFilter());
    }

    @Test
    public void testDeviceDataList(){
        List<DeviceData> deviceDataList = Mockito.mock(List.class);
        advDataInquiryRequest.setDeviceDataList(deviceDataList);
        Assert.assertEquals(deviceDataList, advDataInquiryRequest.getDeviceDataList());
    }

    @Test
    public void testReturnedMeasureAttributes(){
        List<String> returnedMeasureAttributes = Mockito.mock(List.class);
        advDataInquiryRequest.setReturnedMeasureAttributes(returnedMeasureAttributes);
        Assert.assertEquals(returnedMeasureAttributes, advDataInquiryRequest.getReturnedMeasureAttributes());
    }

    @Test
    public void testMeasurementAttributeFilter(){
        Map<String, List<String>> measurementAttributeFilter = Mockito.mock(Map.class);
        advDataInquiryRequest.setMeasurementAttributeFilter(measurementAttributeFilter);
        Assert.assertEquals(measurementAttributeFilter, advDataInquiryRequest.getMeasurementAttributeFilter());
    }

    @Test
    public void testValueFilter(){
        Map<String, List<String>> valueFilter = Mockito.mock(Map.class);
        advDataInquiryRequest.setValueFilter(valueFilter);
        Assert.assertEquals(valueFilter, advDataInquiryRequest.getValueFilter());
    }

    @Test
    public void testSort() {
        List<Map<String, String>> sort = Mockito.mock(List.class);
        advDataInquiryRequest.setSort(sort);
        Assert.assertEquals(sort, advDataInquiryRequest.getSort());
    }
}
