package commands.dispatch;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import commands.datatypes.CommandCenter;
import commands.datatypes.Commands;
import commands.datatypes.Device;
import commands.datatypes.ValidatedCommand;
import commands.serdes.JsonPOJODeserializer;
import commands.serdes.JsonPOJOSerializer;


//This Program reads the Commands from testCenterCommands kafka topic, validates the message and produces the message into validatedCommands topic
//All Data types are pojos.

public class CommandDispatcher {

	public static void main(String[] args) throws Exception {

   
		//Configuration File for Kafka Cluster Properties and Topic Names
		
        final Properties props = loadConfig("application.properties");

        final String commandInputTopic =  props.getProperty("CommandCenterInputTopicName");
        final String validatedCommandTopic =  props.getProperty("ValidatedCommandsTopicName");
        final Serde < String > stringSerde = Serdes.String();
        final Serde < Long > longSerde = Serdes.Long();
        
        
        // Add additional properties.
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "command-reciever");
        // Disable caching to print the aggregation value after each record
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        try
        {
	        final Serde<CommandCenter> commandSerde = getcommandJsonSerde();
	        final Serde<ValidatedCommand> validatedCmdSerde= getValidatedcommandJsonSerde();
	        
	        final StreamsBuilder builder = new StreamsBuilder();
	        
	        try
	        {
		        final KStream<String, CommandCenter> records = builder.stream(commandInputTopic, Consumed.with(stringSerde, commandSerde));
		        
		              KStream<String, ValidatedCommand> transformedCommands = records.flatMap(
		        	    (key, value) -> {
		        	      List<KeyValue<String, ValidatedCommand>> result = new LinkedList<>();
		        	      List<Device> devices = value.getCommands().getDevices();
		        	      devices.forEach(v->{
		        	    	  List<Device> device = new ArrayList<Device>();
		        	    	  device.add(v);
		        	    	  result.add(new KeyValue<String,ValidatedCommand>(value.getNetworkId(),
		        	    			  new ValidatedCommand(value.getNetworkId(),new Commands(device))));
		        	    	});
		        	      
		        	      return result;
		        	    }
		        	  );
		        
		        transformedCommands.foreach((key, value) -> 
		        {
		        	value.getCommands().getDevices().forEach(v->{
		        	System.out.println("Inside Command Center::"+key + " => " + v.getDeviceId());
		        	});
		        });
		        
		        transformedCommands.to(validatedCommandTopic, Produced.with(Serdes.String(), validatedCmdSerde));
		        
	
	        }
	        
	        catch(Exception ex)
	       
	        {
	        	ex.printStackTrace();
	        }
	        
	       
	
	
	        KafkaStreams streams = new KafkaStreams(builder.build(), props);
	        streams.start();
	
	        // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
	        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        }
	catch (Exception  ex)
        {
			ex.printStackTrace();
        }
	}

   
    private static Serde<CommandCenter> getcommandJsonSerde(){

        Map<String, Object> serdeProps = new HashMap<>();
        
        final Serializer < CommandCenter > commandSerializer = new JsonPOJOSerializer  < > ();
        serdeProps.put("JsonPOJOClass", CommandCenter.class);
        commandSerializer.configure(serdeProps, false);
        
        final Deserializer < CommandCenter > commandDeserializer = new JsonPOJODeserializer < > ();
        serdeProps.put("JsonPOJOClass", CommandCenter.class);
        commandDeserializer.configure(serdeProps, false);
        
        final Serde < CommandCenter > commandSerde = Serdes.serdeFrom(commandSerializer, commandDeserializer);
        
        return commandSerde;

       
    }
    
    private static Serde<ValidatedCommand> getValidatedcommandJsonSerde(){

        Map<String, Object> serdeProps = new HashMap<>();
        
        final Serializer < ValidatedCommand > commandSerializer = new JsonPOJOSerializer  < > ();
        serdeProps.put("JsonPOJOClass", ValidatedCommand.class);
        commandSerializer.configure(serdeProps, false);
        
        final Deserializer < ValidatedCommand > commandDeserializer = new JsonPOJODeserializer < > ();
        serdeProps.put("JsonPOJOClass", ValidatedCommand.class);
        commandDeserializer.configure(serdeProps, false);
        
        final Serde < ValidatedCommand > commandSerde = Serdes.serdeFrom(commandSerializer, commandDeserializer);
        
        return commandSerde;

       
    }


    public static Properties loadConfig(final String configFile) throws IOException {
      if (!Files.exists(Paths.get(configFile))) {
        throw new IOException(configFile + " not found.");
      }
      final Properties cfg = new Properties();
      try (InputStream inputStream = new FileInputStream(configFile)) {
        cfg.load(inputStream);
      }
      return cfg;
    }

}
