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
"Commands"
})
public class ValidatedCommand {

@JsonProperty("networkId")
private String networkId;
@JsonProperty("Commands")
private Commands commands;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

public ValidatedCommand() {}
public ValidatedCommand(String networkId,Commands commands)
{
	this.networkId=networkId;
	this.commands = commands;
}

@JsonProperty("networkId")
public String getNetworkId() {
return networkId;
}

@JsonProperty("networkId")
public void setNetworkId(String networkId) {
this.networkId = networkId;
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