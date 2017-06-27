package io.parrot.api.model;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.parrot.api.model.ParrotSinkApi;
import io.parrot.api.model.ParrotSourceApi;
import javax.validation.constraints.*;
import io.swagger.annotations.*;



public class ParrotProcessorApi   {
  
  private String id = null;
  private ParrotSourceApi source = null;
  private ParrotSinkApi sink = null;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ENABLED("ENABLED"),

        DISABLED("DISABLED");
    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }
  }

  private StatusEnum status = StatusEnum.DISABLED;

  /**
   * Processor's ID.
   **/
  
  @ApiModelProperty(example = "null", required = true, value = "Processor's ID.")
  @JsonProperty("id")
  @NotNull
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   **/
  
  @ApiModelProperty(example = "null", required = true, value = "")
  @JsonProperty("source")
  @NotNull
  public ParrotSourceApi getSource() {
    return source;
  }
  public void setSource(ParrotSourceApi source) {
    this.source = source;
  }

  /**
   **/
  
  @ApiModelProperty(example = "null", required = true, value = "")
  @JsonProperty("sink")
  @NotNull
  public ParrotSinkApi getSink() {
    return sink;
  }
  public void setSink(ParrotSinkApi sink) {
    this.sink = sink;
  }

  /**
   **/
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }
  public void setStatus(StatusEnum status) {
    this.status = status;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ParrotProcessorApi parrotProcessor = (ParrotProcessorApi) o;
    return Objects.equals(id, parrotProcessor.id) &&
        Objects.equals(source, parrotProcessor.source) &&
        Objects.equals(sink, parrotProcessor.sink) &&
        Objects.equals(status, parrotProcessor.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, source, sink, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ParrotProcessorApi {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    sink: ").append(toIndentedString(sink)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

