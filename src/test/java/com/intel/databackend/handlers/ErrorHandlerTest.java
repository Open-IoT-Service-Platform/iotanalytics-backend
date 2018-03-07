package com.intel.databackend.handlers;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.intel.databackend.exceptions.DataInquiryException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.http.HttpStatus;
import com.intel.databackend.exceptions.MissingDataSubmissionArgumentException;
import com.intel.databackend.exceptions.ServiceException;
import com.intel.databackend.exceptions.VcapEnvironmentException;
import org.springframework.validation.FieldError;

import java.util.ArrayList;


public class ErrorHandlerTest {
    private ErrorHandler errorHandler;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        errorHandler = new ErrorHandler();
    }

    @Test
    public void testHandleErrorWithDataInquiryException (){
        DataInquiryException dataInquiryException  = new DataInquiryException("mock error");
        ResponseEntity  responseEntity = errorHandler.handleError(dataInquiryException);
        Assert.assertEquals(dataInquiryException.getErrorCode(), responseEntity.getStatusCode().value());
    }

    @Test
    public void testHandleErrorWithServiceException (){
        ServiceException  serviceException = new ServiceException("mock error");
        ResponseEntity responseEntity = errorHandler.handleError(serviceException);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCode().value());
    }

    @Test
    public void testHandleErrorWithVcapEnvironmentException(){
        VcapEnvironmentException vcapEnvironmentException = new VcapEnvironmentException("mock error");
        ResponseEntity responseEntity = errorHandler.handleError(vcapEnvironmentException);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCode().value());
    }

    @Test
    public void testHandleErrorWithBindException(){
        FieldError fieldError = new FieldError("fielderror","field","mock error");
        ArrayList<FieldError> list = new ArrayList<>();
        list.add(fieldError);

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.getFieldErrors()).thenReturn(list);

        BindException bindException = new BindException(bindingResult);
        ResponseEntity responseEntity = errorHandler.handleError(bindException);
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCode().value());
    }

    @Test
    public void testHandleErrorMissingDataSubmissionArgumentException() throws  Exception{
        MissingDataSubmissionArgumentException missingDataSubmissionArgumentException = Mockito.
                mock(MissingDataSubmissionArgumentException.class);
        ResponseEntity responseEntity = errorHandler.handleError(missingDataSubmissionArgumentException);
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCode().value());
    }
}
