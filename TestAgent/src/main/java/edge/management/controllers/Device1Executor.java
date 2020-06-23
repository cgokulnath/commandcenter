package edge.management.controllers;

import akka.actor.UntypedActor;
import edge.commands.datatypes.Device;


//Command Exeucutor for Device 1
public class Device1Executor extends UntypedActor {

  @Override
  public void onReceive(Object msg) {
	  
	  if (msg instanceof Device)
	  {
		  Device device = (Device)msg;
		  
		  device.setStatus(Device.DEVICESTATUS.RECIEVED.toString());
		  
		  System.out.println("Executing Device command on=>"+device.getDeviceId()+
				  			 " with "+device.getMethodName()+ " Operation" +
				  			 " with Inputs " + device.getPayload().getInput1() +
				  			  "," + device.getPayload().getInput2());
		  
		  device.setStatus(Device.DEVICESTATUS.COMPLETED.toString());
		  
	  }
  }
   

}