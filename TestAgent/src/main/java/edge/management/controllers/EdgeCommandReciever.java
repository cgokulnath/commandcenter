package edge.management.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import edge.commands.datatypes.ValidatedCommand;
import edge.commands.serdes.JsonPOJODeserializer;
import edge.commands.serdes.JsonPOJOSerializer;


public class EdgeCommandReciever  {

	public static ActorSystem system = ActorSystem.create("DeviceManagement");
	public static ActorRef deviceMananger = system.actorOf(Props.create(DeviceManager.class), "deviceManager");
	static Properties props = null;
	
  public static void main(String[] args) throws IOException {
	
	  // Load kafka cluster properties from a local configuration file
      props = loadConfig("application.properties");
      CommandCenterUpdater.intializeCommandCenter(props);
      dispatchCommands();
  
  }
  
  public static void dispatchCommands() throws IOException
  {
      final Serde < String > stringSerde = Serdes.String();
      
      // Add additional properties.
      props.put(StreamsConfig.APPLICATION_ID_CONFIG, "EdgeManager");
      // Disable caching to print the aggregation value after each record
      props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
      props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);
      props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
      
      final String inputCommandstopic = props.getProperty("ValidatedCommandsTopicName");
      final String statusCommandTopic=  props.getProperty("CommandStatusTopicName");

      try
      {
	        final Serde<ValidatedCommand> validatedCmdSerde= getValidatedcommandJsonSerde();
	        final StreamsBuilder builder = new StreamsBuilder();
	        
	        try
	        {
		        //Dispatch the Received commands to DeviceManager
	        	final KStream<String, ValidatedCommand> records = builder.stream(inputCommandstopic, Consumed.with(stringSerde, validatedCmdSerde));
	                records.foreach((key, value) -> deviceMananger.tell(value, null));
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

  //Loading Kafka cluster Properties from configuration file
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
  
  //Serializer and Deserializer for ValidatedCommand
  public static Serde<ValidatedCommand> getValidatedcommandJsonSerde(){

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
  
  //For Shutting down the ActorSystem
  public static class Terminator extends UntypedActor {

	    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	    private final ActorRef ref;

	    public Terminator(ActorRef ref) {
	      this.ref = ref;
	      getContext().watch(ref);
	    }

	    @Override
	    public void onReceive(Object msg) {
	      if (msg instanceof Terminated) {
	        log.info("{} has terminated, shutting down system", ref.path());
	        getContext().system().shutdown();
	      } else {
	        unhandled(msg);
	      }
	    }

	  }
}
