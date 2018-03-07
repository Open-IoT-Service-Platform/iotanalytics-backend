package com.intel.databackend.datastructures.requests;

import com.intel.databackend.datastructures.Observation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class DataSubmissionRequestTest {
    private DataSubmissionRequest dataSubmissionRequest;


    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
        dataSubmissionRequest = new DataSubmissionRequest();
    }

    @Test
    public void testAccountId(){
        String accountId = "accountId";
        dataSubmissionRequest.setAccountId(accountId);
        Assert.assertEquals(accountId, dataSubmissionRequest.getAccountId());
    }

    @Test
    public void testCount(){
        long count = 100;
        dataSubmissionRequest.setCount(count);
        Assert.assertEquals(Long.valueOf(count), dataSubmissionRequest.getCount());
    }

    @Test
    public void testData(){
        List<Observation> data = Mockito.mock(List.class);
        dataSubmissionRequest.setData(data);
        Assert.assertEquals(data, dataSubmissionRequest.getData());
    }

    @Test
    public void testDid(){
        String did = "did";
        dataSubmissionRequest.setDid(did);
        Assert.assertEquals(did, dataSubmissionRequest.getDid());
    }

    @Test
    public void testOn(){
        long on = 1;
        dataSubmissionRequest.setOn(on);
        Assert.assertEquals(Long.valueOf(on), dataSubmissionRequest.getOn());
    }

    @Test
    public void testsystemOn(){
        long systemOn = 1;
        dataSubmissionRequest.setSystemOn(systemOn);
        Assert.assertEquals(Long.valueOf(systemOn), dataSubmissionRequest.getSystemOn());
    }
}
