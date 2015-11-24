/**
 * Copyright (c) 2015 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intel.databackend.config.cloudfoundry;

import com.intel.databackend.config.DashboardCredentialsProvider;
import com.intel.databackend.config.cloudfoundry.utils.VcapReader;
import com.intel.databackend.exceptions.VcapEnvironmentException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


public class DashboardEndpointConfig implements DashboardCredentialsProvider {

    private static final String ENDPOINT_SERVICE_NAME = "dashboard-endpoint-ups";
    private static final String USER_SERVICE_NAME = "installer-backend-user-credentials-ups";
    private static final String HOST = "host";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private VcapReader vcapReaderServices = null;
    private String endpoint;
    private String username;
    private String password;

    public DashboardEndpointConfig() {
        vcapReaderServices = new VcapReader();
    }

    public void readEndpoint() throws VcapEnvironmentException {
        JSONObject dashboardEndpointConf = vcapReaderServices.getUserProvidedServiceCredentialsByName(ENDPOINT_SERVICE_NAME);
        try {
            if (dashboardEndpointConf != null) {
                endpoint = dashboardEndpointConf.getString(HOST);
            } else {
                throw new VcapEnvironmentException("Unable to find Dashboard url in VCAP env - " + ENDPOINT_SERVICE_NAME);
            }
            JSONObject userConf = vcapReaderServices.getUserProvidedServiceCredentialsByName(USER_SERVICE_NAME);
            if (userConf != null) {
                username = userConf.getString(USERNAME);
                password = userConf.getString(PASSWORD);
            } else {
                throw new VcapEnvironmentException("Unable to find Dashboard user config in VCAP env - " + USER_SERVICE_NAME);
            }
        } catch(JSONException e){
            throw new VcapEnvironmentException(e.getMessage());
        }
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
