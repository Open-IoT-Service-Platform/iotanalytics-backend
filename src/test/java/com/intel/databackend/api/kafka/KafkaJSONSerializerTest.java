package com.intel.databackend.api.kafka;

import com.intel.databackend.datastructures.Observation;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Arrays;

public class KafkaJSONSerializerTest {
    private KafkaJSONSerializer kafkaJSONSerializer;

    @Before
    public void initMocks() throws Exception {
        kafkaJSONSerializer = new KafkaJSONSerializer();
    }

    @Test
    public void testSerialize() throws  Exception{
        List<Observation> observation_list = Arrays.asList(new Observation());
        kafkaJSONSerializer.serialize("topic", observation_list);
    }
}
