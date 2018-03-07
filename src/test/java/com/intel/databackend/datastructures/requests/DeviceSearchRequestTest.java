package com.intel.databackend.datastructures.requests;

import com.intel.databackend.datastructures.DeviceSearchCriterium;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class DeviceSearchRequestTest {
    private DeviceSearchRequest deviceSearchRequest;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
        deviceSearchRequest = new DeviceSearchRequest();
    }

    @Test
    public void testDeviceId(){
        DeviceSearchCriterium deviceId = Mockito.mock(DeviceSearchCriterium.class);
        deviceSearchRequest.setDeviceId(deviceId);
        Assert.assertEquals(deviceId, deviceSearchRequest.getDeviceId());
    }

    @Test
    public void testGatewayId(){
        DeviceSearchCriterium gatewayId = Mockito.mock(DeviceSearchCriterium.class);
        deviceSearchRequest.setGatewayId(gatewayId);
        Assert.assertEquals(gatewayId, deviceSearchRequest.getGatewayId());
    }

    @Test
    public void testName(){
        DeviceSearchCriterium name = Mockito.mock(DeviceSearchCriterium.class);
        deviceSearchRequest.setName(name);
        Assert.assertEquals(name, deviceSearchRequest.getName());
    }

    @Test
    public void testStatus(){
        DeviceSearchCriterium status = Mockito.mock(DeviceSearchCriterium.class);
        deviceSearchRequest.setStatus(status);
        Assert.assertEquals(status, deviceSearchRequest.getStatus());
    }

    @Test
    public void testTag(){
        DeviceSearchCriterium tags = Mockito.mock(DeviceSearchCriterium.class);
        deviceSearchRequest.setTags(tags);
        Assert.assertEquals(tags, deviceSearchRequest.getTags());
    }
}
