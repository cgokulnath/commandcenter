package edge.management.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;

import edge.commands.datatypes.ValidatedCommand;
import edge.commands.serdes.JsonPOJOSerializer;

public class CommandCenterUpdater {
	
	 static KafkaProducer<String, ValidatedCommand> producer = null;
	 static Properties props=null;
	 
	 public static void intializeCommandCenter(Properties properties)
	 {
		    props = properties;
		    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		   
	        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "edge.commands.serdes.JsonPOJODeserializer");
	        
	        final StringSerializer keySerializer = new StringSerializer();
	        final JsonPOJOSerializer<ValidatedCommand> valCmdSerializer = new JsonPOJOSerializer<ValidatedCommand>();
	              
	        producer = new KafkaProducer<>(props, keySerializer, valCmdSerializer); 
	 }
	 
	 
	 public static Producer<String, ValidatedCommand> getKafkaProducer() 
	 {
    	return  producer;
     }
    
    public static Properties loadConfig(final String configFile)  {
      final Properties cfg = new Properties();
      try (InputStream inputStream = new FileInputStream(configFile)) {
        cfg.load(inputStream);
      }
      catch(Exception ex)
      {
    	  ex.printStackTrace();
      }
      return cfg;
    }
    
	public static void upDateCommandCenter(ValidatedCommand valCommand)
	{
		producer.send(new ProducerRecord<String, ValidatedCommand>(props.getProperty("CommandStatusTopicName"),
							valCommand.getNetworkId(),
							valCommand));
		
	}
    
}
