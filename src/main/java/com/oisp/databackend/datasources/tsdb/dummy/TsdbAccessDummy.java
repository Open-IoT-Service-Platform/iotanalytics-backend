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

package com.oisp.databackend.datasources.tsdb.dummy;

import com.oisp.databackend.datasources.DataType;
import com.oisp.databackend.datasources.tsdb.TsdbAccess;
import com.oisp.databackend.datasources.tsdb.TsdbQuery;
import com.oisp.databackend.datastructures.Observation;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository
public class TsdbAccessDummy implements TsdbAccess {

    @Override
    public boolean put(List<Observation> observations, boolean onlyMetadata) {
        return true;
    }

    @Override
    public boolean put(Observation observation, boolean onlyMetadata) {
        return true;
    }


    @Override
    public Observation[] scan(TsdbQuery tsdbQuery) {

        return new Observation[0];
    }

    @Override
    public Observation[] scan(TsdbQuery tsdbQuery, boolean forward, int limit) {
        return new Observation[0];
    }


    public String[] scanForAttributeNames(TsdbQuery tsdbQuery) throws IOException {

        return new String[0];
    }

    public Long count(TsdbQuery tsdbQuery) {
        return 0L;
    }

    @Override
    public List<DataType.Types> getSupportedDataTypes() {
        return Arrays.asList(DataType.Types.Number, DataType.Types.String, DataType.Types.Boolean, DataType.Types.ByteArray);
    }

}
