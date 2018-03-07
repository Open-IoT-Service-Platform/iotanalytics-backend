package com.intel.databackend.handlers.requestvalidator;

import com.intel.databackend.datastructures.requests.AdvDataInquiryRequest;
import com.intel.databackend.exceptions.IllegalDataInquiryArgumentException;
import com.intel.databackend.datastructures.DeviceData;
import com.intel.databackend.exceptions.ErrorMsg;
import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class AdvanceDataRequestValidatorTest {
    private AdvDataInquiryRequest advDataInquiryRequest;
    private AdvanceDataRequestValidator advanceDataRequestValidator;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
        advDataInquiryRequest = Mockito.mock(AdvDataInquiryRequest.class);
        advanceDataRequestValidator = new AdvanceDataRequestValidator(advDataInquiryRequest);
    }

    @Test
    public void testValidate_With_HasStartOrEndTimestamp(){
        Mockito.when(advDataInquiryRequest.getStartTimestamp()).thenReturn(null);
        Mockito.when(advDataInquiryRequest.getEndTimestamp()).thenReturn(null);
        try {
            advanceDataRequestValidator.validate();
        }catch (IllegalDataInquiryArgumentException e){
            Assert.assertEquals(ErrorMsg.NO_START_OR_END_TIMESTAMP, e.getMessage());
        }
    }

    @Test
    public void testValidate_With_HasZeroComponentRowLimit(){
        Mockito.when(advDataInquiryRequest.getStartTimestamp()).thenReturn(10L);
        Mockito.when(advDataInquiryRequest.getEndTimestamp()).thenReturn(20L);
        Mockito.when(advDataInquiryRequest.getComponentRowLimit()).thenReturn(-1L);
        try {
            advanceDataRequestValidator.validate();
        }catch (IllegalDataInquiryArgumentException e){
            Assert.assertEquals(ErrorMsg.ZERO_COMPONENT_ROW_LIMIT, e.getMessage());
        }

    }

    @Test
    public void testValidate_With_HasEmptyDeviceDataList(){
        List<DeviceData> list = Mockito.mock(List.class);
        Mockito.when(list.isEmpty()).thenReturn(true);
        Mockito.when(advDataInquiryRequest.getStartTimestamp()).thenReturn(10L);
        Mockito.when(advDataInquiryRequest.getEndTimestamp()).thenReturn(20L);
        Mockito.when(advDataInquiryRequest.getComponentRowLimit()).thenReturn(null);
        Mockito.when(advDataInquiryRequest.getDeviceDataList()).thenReturn(list);
        try {
            advanceDataRequestValidator.validate();
        }catch (IllegalDataInquiryArgumentException e){
            Assert.assertEquals(ErrorMsg.NO_DEVICE_DATA, e.getMessage()) ;
        }
    }
}
