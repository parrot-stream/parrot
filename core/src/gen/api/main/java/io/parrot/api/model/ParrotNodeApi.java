package io.parrot.api.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;



public class ParrotNodeApi   {
  
  private Integer id = null;
  private String hostname = null;

  /**
   * Parrot Node id.
   **/
  
  @ApiModelProperty(example = "null", required = true, value = "Parrot Node id.")
  @JsonProperty("id")
  @NotNull
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Parrot Node hostname.
   **/
  
  @ApiModelProperty(example = "null", required = true, value = "Parrot Node hostname.")
  @JsonProperty("hostname")
  @NotNull
  public String getHostname() {
    return hostname;
  }
  public void setHostname(String hostname) {
    this.hostname = hostname;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ParrotNodeApi parrotNode = (ParrotNodeApi) o;
    return Objects.equals(id, parrotNode.id) &&
        Objects.equals(hostname, parrotNode.hostname);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, hostname);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ParrotNodeApi {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    hostname: ").append(toIndentedString(hostname)).append("\n");
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

