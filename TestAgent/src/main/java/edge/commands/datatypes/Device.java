package edge.commands.datatypes;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"deviceId",
"methodName",
"responseTimeoutInSeconds",
"payload",
"status"
})
public class Device {

@JsonProperty("deviceId")
private String deviceId;
@JsonProperty("methodName")
private String methodName;
@JsonProperty("responseTimeoutInSeconds")
private Integer responseTimeoutInSeconds;
@JsonProperty("payload")
private Payload payload;
@JsonProperty("status")
private String status;

public static enum DEVICESTATUS
{
	RECIEVED,
	DISPATCHED,
	EXEUCTING,
	COMPLETED,
	FAILED
	
}

@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("deviceId")
public String getDeviceId() {
return deviceId;
}

@JsonProperty("deviceId")
public void setDeviceId(String deviceId) {
this.deviceId = deviceId;
}

@JsonProperty("methodName")
public String getMethodName() {
return methodName;
}

@JsonProperty("methodName")
public void setMethodName(String methodName) {
this.methodName = methodName;
}

@JsonProperty("responseTimeoutInSeconds")
public Integer getResponseTimeoutInSeconds() {
return responseTimeoutInSeconds;
}

@JsonProperty("responseTimeoutInSeconds")
public void setResponseTimeoutInSeconds(Integer responseTimeoutInSeconds) {
this.responseTimeoutInSeconds = responseTimeoutInSeconds;
}

@JsonProperty("payload")
public Payload getPayload() {
return payload;
}

@JsonProperty("payload")
public void setPayload(Payload payload) {
this.payload = payload;
}

@JsonProperty("status")
public String getStatus() {
	return status;
}
@JsonProperty("status")
public void setStatus(String status) {
	this.status = status;
}

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}