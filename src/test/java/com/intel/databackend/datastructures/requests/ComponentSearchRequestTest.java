package com.intel.databackend.datastructures.requests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class ComponentSearchRequestTest {
    private ComponentSearchRequest componentSearchRequest;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
        componentSearchRequest = new ComponentSearchRequest();

    }

    @Test
    public void testComponentIds(){
        List<String> componentIds = Mockito.mock(List.class);
        componentSearchRequest.setComponentIds(componentIds);
        Assert.assertEquals(componentIds,componentSearchRequest.getComponentIds());
    }

    @Test
    public void testDeviceIds(){
        List<String> deviceIds = Mockito.mock(List.class);
        componentSearchRequest.setDeviceIds(deviceIds);
        Assert.assertEquals(componentSearchRequest.getDeviceIds(),deviceIds);
    }

    @Test
    public void testDeviceNames(){
        List<String> deviceNames = Mockito.mock(List.class);
        componentSearchRequest.setDeviceNames(deviceNames);
        Assert.assertEquals(componentSearchRequest.getDeviceNames(),deviceNames);
    }

    @Test
    public void testDeviceTags(){
        List<String> deviceTags = Mockito.mock(List.class);
        componentSearchRequest.setDeviceTags(deviceTags);
        Assert.assertEquals(componentSearchRequest.getDeviceTags(),deviceTags);
    }

    @Test
    public void testGatewayIds(){
        List<String> gatewayIds = Mockito.mock(List.class);
        componentSearchRequest.setGatewayIds(gatewayIds);
        Assert.assertEquals(componentSearchRequest.getGatewayIds(),gatewayIds);
    }
}
