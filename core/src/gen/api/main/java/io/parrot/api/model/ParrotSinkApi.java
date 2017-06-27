package io.parrot.api.model;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.parrot.api.model.ParrotSinkConfigurationApi;
import javax.validation.constraints.*;
import io.swagger.annotations.*;



public class ParrotSinkApi   {
  
  private String id = null;
  private String sinkClass = null;
  private ParrotSinkConfigurationApi configurations = null;

  /**
   * Sink's ID.
   **/
  
  @ApiModelProperty(example = "null", required = true, value = "Sink's ID.")
  @JsonProperty("id")
  @NotNull
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   * The class which implements the Parrot Processor's Sink
   **/
  
  @ApiModelProperty(example = "null", required = true, value = "The class which implements the Parrot Processor's Sink")
  @JsonProperty("sink.class")
  @NotNull
  public String getSinkClass() {
    return sinkClass;
  }
  public void setSinkClass(String sinkClass) {
    this.sinkClass = sinkClass;
  }

  /**
   **/
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("configurations")
  public ParrotSinkConfigurationApi getConfigurations() {
    return configurations;
  }
  public void setConfigurations(ParrotSinkConfigurationApi configurations) {
    this.configurations = configurations;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ParrotSinkApi parrotSink = (ParrotSinkApi) o;
    return Objects.equals(id, parrotSink.id) &&
        Objects.equals(sinkClass, parrotSink.sinkClass) &&
        Objects.equals(configurations, parrotSink.configurations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, sinkClass, configurations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ParrotSinkApi {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    sinkClass: ").append(toIndentedString(sinkClass)).append("\n");
    sb.append("    configurations: ").append(toIndentedString(configurations)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

