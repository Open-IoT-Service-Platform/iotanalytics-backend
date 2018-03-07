package com.intel.databackend.datastructures.requests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.List;

public class FirstLastTimestampRequestTest {
    private FirstLastTimestampRequest firstLastTimestampRequest;

    @Before
    public void initMocks(){
        firstLastTimestampRequest = new FirstLastTimestampRequest();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testComponents(){
        List<String> components = Mockito.mock(List.class);
        firstLastTimestampRequest.setComponents(components);
        Assert.assertEquals(components, firstLastTimestampRequest.getComponents());
    }

    @Test
    public void testMsgType(){
        String  msgType = "msgType";
        firstLastTimestampRequest.setMsgType(msgType);
        Assert.assertEquals(msgType, firstLastTimestampRequest.getMsgType());
    }
}
