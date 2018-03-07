package com.intel.databackend.datasources.hbase;

import com.intel.databackend.datastructures.Observation;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.TableName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Matchers;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.*;


@RunWith(MockitoJUnitRunner.class)
public class DataHbaseDaoTest {
    static final byte[] row = {'1','2','\0','1','2','\0','1','2','\0','1','2','\0'};
    static final String accountid = "accid";
    static final String componentid = "componentid";

    @Mock
    private Connection connection;

    @Mock
    private Admin admin;

    @Mock
    private ResultScanner resultScanner;

    @Mock
    private  Table table;

    @Mock
    private Iterator iterator;

    @Mock
    private  TableManager tableManager;

    @Mock
    Result result;

    @InjectMocks
    private DataHbaseDao dataHbaseDao = new DataHbaseDao("mock");

    @Before
    public void initMocks() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.when(connection.isClosed()).thenReturn(false);
        Mockito.when(connection.getAdmin()).thenReturn(admin);
        Mockito.when(connection.getTable(Matchers.any(TableName.class))).thenReturn(table);

        Mockito.when(table.getScanner(Matchers.any(Scan.class))).thenReturn(resultScanner);
        Mockito.when(resultScanner.iterator()).thenReturn(iterator);
        Mockito.when(iterator.hasNext()).thenReturn(true, false);
        Mockito.when(iterator.next()).thenReturn(result);
        Mockito.when(tableManager.createTables()).thenReturn(true);
        Mockito.doNothing().when(connection).close();
        Mockito.when(result.getRow()).thenReturn(row);
        Mockito.when(result.getValue(Matchers.anyObject(),Matchers.anyObject())).thenReturn(row);
    }

    @Test
    public void testCreateTable() throws  Exception{
        boolean ret = dataHbaseDao.createTables();
        Assert.assertTrue(ret);
    }

    @Test
    public void testCloseConnection() throws  Exception{
        dataHbaseDao.closeHbaseConnection();
    }

    @Test
    public void testPut(){
        Observation[] observations = {new Observation(accountid, componentid,10L, "200.0")};
        boolean ret =  dataHbaseDao.put(observations);
        Assert.assertTrue(ret);
    }

    @Test
    public void testScan(){
        String[] attrs = {"attr1"};
        Observation[] ret =  dataHbaseDao.scan(accountid,componentid,0L,10L,true,attrs);
        Assert.assertArrayEquals(row, ret[0].getValue().getBytes());
    }
}
