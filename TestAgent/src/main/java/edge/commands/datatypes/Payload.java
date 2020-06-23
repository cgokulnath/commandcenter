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
"input1",
"input2"
})
public class Payload {

@JsonProperty("input1")
private String input1;
@JsonProperty("input2")
private String input2;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("input1")
public String getInput1() {
return input1;
}

@JsonProperty("input1")
public void setInput1(String input1) {
this.input1 = input1;
}

@JsonProperty("input2")
public String getInput2() {
return input2;
}

@JsonProperty("input2")
public void setInput2(String input2) {
this.input2 = input2;
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