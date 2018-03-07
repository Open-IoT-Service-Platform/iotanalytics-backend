package com.intel.databackend.datasources.hbase;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;


public class KerberosProperitiesTest {
    final static String user = "user";
    final static String password = "password";
    final static String kdc = "kdc";
    final static String realm = "realm";

    private KerberosProperties kerberosProperties;

    @Before
    public void SetUp() {
        kerberosProperties = new KerberosProperties();
    }

    @Test
    public void testPropertyUser(){
        kerberosProperties.setUser(user);
        Assert.assertEquals(user, kerberosProperties.getUser());
    }

    @Test
    public void testPropertyPassword(){
        kerberosProperties.setPassword(password);
        Assert.assertEquals(password, kerberosProperties.getPassword());
    }

    @Test
    public void testPropertyKdc(){
        kerberosProperties.setKdc(kdc);
        Assert.assertEquals(kdc,kerberosProperties.getKdc());
    }

    @Test
    public void testPropertyRealm(){
        kerberosProperties.setRealm(realm);
        Assert.assertEquals(realm, kerberosProperties.getRealm());
    }



}
