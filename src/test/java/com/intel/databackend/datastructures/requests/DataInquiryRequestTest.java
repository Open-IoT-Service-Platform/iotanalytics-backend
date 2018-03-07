package com.intel.databackend.datastructures.requests;

import com.intel.databackend.datastructures.ComponentDataType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.Map;

public class DataInquiryRequestTest {
    private DataInquiryRequest dataInquiryRequest;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
        dataInquiryRequest = new DataInquiryRequest();
    }

    @Test
    public void testComponentAttributes(){
        List<String> componentAttributes = Mockito.mock(List.class);
        dataInquiryRequest.setComponentAttributes(componentAttributes);
        Assert.assertEquals(componentAttributes, dataInquiryRequest.getComponentAttributes());
    }

    @Test
    public void testComponentsWithDataType(){
        Map<String, ComponentDataType> componentsWithDataType = Mockito.mock(Map.class);
        dataInquiryRequest.setComponentsWithDataType(componentsWithDataType);
        Assert.assertEquals(componentsWithDataType,dataInquiryRequest.getComponentsWithDataType());
    }

    @Test
    public void testCountOnly(){
        boolean countonly = true;
        dataInquiryRequest.setCountOnly(countonly);
        Assert.assertEquals(countonly,dataInquiryRequest.getCountOnly());
    }

    @Test
    public void testEndDate(){
        long endData = 100;
        dataInquiryRequest.setEndDate(endData);
        Assert.assertEquals(Long.valueOf(endData), dataInquiryRequest.getEndDate());
    }

    @Test
    public void testStartDate(){
        long startData = 10;
        dataInquiryRequest.setStartDate(startData);
        Assert.assertEquals(Long.valueOf(startData),dataInquiryRequest.getStartDate());
    }

    @Test
    public void testMaxPoints(){
        long maxPoint = 10;
        dataInquiryRequest.setMaxPoints(maxPoint);
        Assert.assertEquals(Long.valueOf(maxPoint),dataInquiryRequest.getMaxPoints());
    }

    @Test
    public void testMsgType(){
        String msgType = "msgType";
        dataInquiryRequest.setMsgType(msgType);
        Assert.assertEquals(msgType, dataInquiryRequest.getMsgType());
    }

    @Test
    public void testQueryMeasureLocation(){
        boolean queryMeasureLocation = true;
        dataInquiryRequest.setQueryMeasureLocation(queryMeasureLocation);
        Assert.assertEquals(queryMeasureLocation, dataInquiryRequest.getQueryMeasureLocation());
    }
}
