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

import com.intel.databackend.config.KafkaBrokerCredentialsProvider;
import com.intel.databackend.config.cloudfoundry.utils.VcapReader;
import com.intel.databackend.exceptions.VcapEnvironmentException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class KafkaBrokerConfig implements KafkaBrokerCredentialsProvider{

    private static final String SERVICE_NAME = "kafka";
    private static final String URI = "uri";

    private VcapReader vcapReaderServices = null;


    public KafkaBrokerConfig() {
        vcapReaderServices = new VcapReader();
    }

    public String getUri() throws VcapEnvironmentException {
        JSONObject zookeeperConf = vcapReaderServices.getVcapServiceCredentialsByType(SERVICE_NAME);
        if (zookeeperConf != null) {
            try {
                return zookeeperConf.getString(URI);
            } catch (JSONException e) {
                throw new VcapEnvironmentException("Unable to parse Kafka config from VCAP env - " + SERVICE_NAME, e);
            }
        } else {
            throw new VcapEnvironmentException("Unable to find Kafka config in VCAP env - " + SERVICE_NAME);
        }
    }
}
