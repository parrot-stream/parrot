package io.parrot.api.model;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.constraints.*;
import io.swagger.annotations.*;



public class ParrotProcessorNodeApi   {
  
  private String id = null;
  private String node = null;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    STARTED("STARTED"),

        STOPPED("STOPPED");
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

  private StatusEnum status = null;

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
   * Processor Node's ID
   **/
  
  @ApiModelProperty(example = "null", required = true, value = "Processor Node's ID")
  @JsonProperty("node")
  @NotNull
  public String getNode() {
    return node;
  }
  public void setNode(String node) {
    this.node = node;
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
    ParrotProcessorNodeApi parrotProcessorNode = (ParrotProcessorNodeApi) o;
    return Objects.equals(id, parrotProcessorNode.id) &&
        Objects.equals(node, parrotProcessorNode.node) &&
        Objects.equals(status, parrotProcessorNode.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, node, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ParrotProcessorNodeApi {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    node: ").append(toIndentedString(node)).append("\n");
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

