package com.intel.databackend.handlers.requestvalidator;

import com.intel.databackend.exceptions.ErrorMsg;
import com.intel.databackend.exceptions.IllegalDataInquiryArgumentException;
import com.intel.databackend.datastructures.requests.DataInquiryRequest;
import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class DataRequestValidatorTest {
    private DataRequestValidator dataRequestValidator;
    private DataInquiryRequest  dataInquiryRequest;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
        dataInquiryRequest = Mockito.mock(DataInquiryRequest.class);
        dataRequestValidator = new DataRequestValidator(dataInquiryRequest);
    }

    @Test
    public void testValidate_With_HasNotStartOrEndDate(){
        Mockito.when(dataInquiryRequest.getStartDate()).thenReturn(null);
        try{
            dataRequestValidator.validate();
        }catch (IllegalDataInquiryArgumentException e){
            Assert.assertEquals(ErrorMsg.NO_START_OR_END_DATE, e.getMessage());
        }

    }

    @Test
    public void testValidate_With_HasEmptyComponentsWithDataType(){
        Mockito.when(dataInquiryRequest.getStartDate()).thenReturn(10L);
        Mockito.when(dataInquiryRequest.getEndDate()).thenReturn(20L);
        Mockito.when(dataInquiryRequest.getComponentsWithDataType()).thenReturn(null);
        try{
            dataRequestValidator.validate();
        }catch (IllegalDataInquiryArgumentException e){
            Assert.assertEquals(ErrorMsg.NO_COMPONENTS_PROVIDED, e.getMessage());
        }
    }

    @Test
    public void testValidate_With_HasCountOnlyWithMaxPoints(){
        Mockito.when(dataInquiryRequest.getStartDate()).thenReturn(10L);
        Mockito.when(dataInquiryRequest.getEndDate()).thenReturn(20L);
        Map map=Mockito.mock(Map.class);
        Mockito.when(dataInquiryRequest.getComponentsWithDataType()).thenReturn(map);
        Mockito.when(dataInquiryRequest.getCountOnly()).thenReturn(true);
        Mockito.when(dataInquiryRequest.getMaxPoints()).thenReturn(20L);
        try{
            dataRequestValidator.validate();
        }catch (IllegalDataInquiryArgumentException e){
            Assert.assertEquals(ErrorMsg.COUNT_ONLY_WITH_MAX_POINTS_SELECTED, e.getMessage());
        }
    }

    @Test
    public void testValidate_With_IsMaxPointsRequest(){
        Mockito.when(dataInquiryRequest.getStartDate()).thenReturn(10L);
        Mockito.when(dataInquiryRequest.getEndDate()).thenReturn(20L);
        Map map=Mockito.mock(Map.class);
        Mockito.when(dataInquiryRequest.getComponentsWithDataType()).thenReturn(map);
        Mockito.when(dataInquiryRequest.getCountOnly()).thenReturn(false);
        Mockito.when(dataInquiryRequest.getMaxPoints()).thenReturn(20L);
        Mockito.when(dataInquiryRequest.getQueryMeasureLocation()).thenReturn(true);
        try{
            dataRequestValidator.validate();
        }catch (IllegalDataInquiryArgumentException e){
            Assert.assertEquals(ErrorMsg.QUERY_LOCATION_WITH_MAX_POINTS_SELECTED, e.getMessage());
        }
    }
}
