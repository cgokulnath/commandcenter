package commands.datatypes;
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
"networkId",
"etag",
"status",
"statusUpdateTime",
"connectionState",
"lastActivityTime",
"cloudToDeviceMessageCount",
"authenticationType",
"version",
"tags",
"Commands"
})
public class CommandCenter {

@JsonProperty("networkId")
private String networkId;
@JsonProperty("etag")
private String etag;
@JsonProperty("status")
private String status;
@JsonProperty("statusUpdateTime")
private String statusUpdateTime;
@JsonProperty("connectionState")
private String connectionState;
@JsonProperty("lastActivityTime")
private String lastActivityTime;
@JsonProperty("cloudToDeviceMessageCount")
private Integer cloudToDeviceMessageCount;
@JsonProperty("authenticationType")
private String authenticationType;
@JsonProperty("version")
private Integer version;
@JsonProperty("tags")
private Tags tags;
@JsonProperty("Commands")
private Commands commands;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("networkId")
public String getNetworkId() {
return networkId;
}

@JsonProperty("networkId")
public void setNetworkId(String networkId) {
this.networkId = networkId;
}

@JsonProperty("etag")
public String getEtag() {
return etag;
}

@JsonProperty("etag")
public void setEtag(String etag) {
this.etag = etag;
}

@JsonProperty("status")
public String getStatus() {
return status;
}

@JsonProperty("status")
public void setStatus(String status) {
this.status = status;
}

@JsonProperty("statusUpdateTime")
public String getStatusUpdateTime() {
return statusUpdateTime;
}

@JsonProperty("statusUpdateTime")
public void setStatusUpdateTime(String statusUpdateTime) {
this.statusUpdateTime = statusUpdateTime;
}

@JsonProperty("connectionState")
public String getConnectionState() {
return connectionState;
}

@JsonProperty("connectionState")
public void setConnectionState(String connectionState) {
this.connectionState = connectionState;
}

@JsonProperty("lastActivityTime")
public String getLastActivityTime() {
return lastActivityTime;
}

@JsonProperty("lastActivityTime")
public void setLastActivityTime(String lastActivityTime) {
this.lastActivityTime = lastActivityTime;
}

@JsonProperty("cloudToDeviceMessageCount")
public Integer getCloudToDeviceMessageCount() {
return cloudToDeviceMessageCount;
}

@JsonProperty("cloudToDeviceMessageCount")
public void setCloudToDeviceMessageCount(Integer cloudToDeviceMessageCount) {
this.cloudToDeviceMessageCount = cloudToDeviceMessageCount;
}

@JsonProperty("authenticationType")
public String getAuthenticationType() {
return authenticationType;
}

@JsonProperty("authenticationType")
public void setAuthenticationType(String authenticationType) {
this.authenticationType = authenticationType;
}

@JsonProperty("version")
public Integer getVersion() {
return version;
}

@JsonProperty("version")
public void setVersion(Integer version) {
this.version = version;
}

@JsonProperty("tags")
public Tags getTags() {
return tags;
}

@JsonProperty("tags")
public void setTags(Tags tags) {
this.tags = tags;
}

@JsonProperty("Commands")
public Commands getCommands() {
return commands;
}

@JsonProperty("Commands")
public void setCommands(Commands commands) {
this.commands = commands;
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