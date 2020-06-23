package edge.management.controllers;

import akka.actor.Props;
import akka.actor.UntypedActor;
import edge.commands.datatypes.Device;
import edge.commands.datatypes.ValidatedCommand;
import akka.actor.ActorRef;

//Device Manager for managing the devices in the network
//It instantiates device executors for each device and dispatches to each device

public class DeviceManager extends UntypedActor {

  ActorRef device1Executor=null;
  ActorRef device2Executor=null;
  @Override
  public void preStart() {
    // create the actors for each device
    device1Executor = getContext().actorOf(Props.create(Device1Executor.class), "device1executor");
    device2Executor = getContext().actorOf(Props.create(Device2Executor.class), "device2executor");
   
  }

  @Override
  public void onReceive(Object msg) 
  {
	  if (msg instanceof ValidatedCommand)
	  {
		  ValidatedCommand valCmd = (ValidatedCommand) msg;
		  for (Device device : valCmd.getCommands().getDevices())
		  {
			  device.setStatus(Device.DEVICESTATUS.DISPATCHED.toString());
			  
			  if (device.getDeviceId().equals("Device1"))
			  {
				  device1Executor.tell(device, getSelf());
				  CommandCenterUpdater.upDateCommandCenter(valCmd);
				  
			  }
			  else  if (device.getDeviceId().equals("Device2"))
			  {
				  device2Executor.tell(device, getSelf());	
				  CommandCenterUpdater.upDateCommandCenter(valCmd);
			  }
		  }
	  }
  }
    
}