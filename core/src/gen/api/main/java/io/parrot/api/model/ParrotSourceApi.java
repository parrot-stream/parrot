package io.parrot.api.model;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import javax.validation.constraints.*;
import io.swagger.annotations.*;



public class ParrotSourceApi   {
  
  private String logicalName = null;
  private String sourceClass = null;
  private String bootstrapServers = null;
  private String schemaWhitelist = null;
  private String schemaBlacklist = null;
  private String tableWhitelist = null;
  private String tableBlacklist = null;

  /**
   * This corrisponds to the <b>logical name</b> named of a Debezium Connector. | For example, if the Processor has a PostgreSQL Parrot source, the logical name is the value of the | database.server.name property in the config part of the Debezium Connector.
   **/
  
  @ApiModelProperty(example = "null", required = true, value = "This corrisponds to the <b>logical name</b> named of a Debezium Connector. | For example, if the Processor has a PostgreSQL Parrot source, the logical name is the value of the | database.server.name property in the config part of the Debezium Connector.")
  @JsonProperty("logical.name")
  @NotNull
  public String getLogicalName() {
    return logicalName;
  }
  public void setLogicalName(String logicalName) {
    this.logicalName = logicalName;
  }

  /**
   * The class which implements the Parrot Processor's Source
   **/
  
  @ApiModelProperty(example = "null", required = true, value = "The class which implements the Parrot Processor's Source")
  @JsonProperty("source.class")
  @NotNull
  public String getSourceClass() {
    return sourceClass;
  }
  public void setSourceClass(String sourceClass) {
    this.sourceClass = sourceClass;
  }

  /**
   * A list of host/port pairs to use for establishing the initial connection to the Kafka cluster. The Parrot Processor will make use of all servers irrespective of which servers are specified here for bootstrapping. This list only impacts the initial hosts used to discover the full set of servers. This list should be in the form 'host1:port1,host2:port2,...'. Since these servers are just used for the initial connection to discover the full cluster membership (which may change dynamically), this list need not contain the full set of servers (you may want more than one, though, in case a server is down).
   **/
  
  @ApiModelProperty(example = "null", required = true, value = "A list of host/port pairs to use for establishing the initial connection to the Kafka cluster. The Parrot Processor will make use of all servers irrespective of which servers are specified here for bootstrapping. This list only impacts the initial hosts used to discover the full set of servers. This list should be in the form 'host1:port1,host2:port2,...'. Since these servers are just used for the initial connection to discover the full cluster membership (which may change dynamically), this list need not contain the full set of servers (you may want more than one, though, in case a server is down).")
  @JsonProperty("bootstrap.servers")
  @NotNull
  public String getBootstrapServers() {
    return bootstrapServers;
  }
  public void setBootstrapServers(String bootstrapServers) {
    this.bootstrapServers = bootstrapServers;
  }

  /**
   * Schema whitelist (where supported).
   **/
  
  @ApiModelProperty(example = "null", value = "Schema whitelist (where supported).")
  @JsonProperty("schema.whitelist")
  public String getSchemaWhitelist() {
    return schemaWhitelist;
  }
  public void setSchemaWhitelist(String schemaWhitelist) {
    this.schemaWhitelist = schemaWhitelist;
  }

  /**
   * Schema blacklist (where supported).
   **/
  
  @ApiModelProperty(example = "null", value = "Schema blacklist (where supported).")
  @JsonProperty("schema.blacklist")
  public String getSchemaBlacklist() {
    return schemaBlacklist;
  }
  public void setSchemaBlacklist(String schemaBlacklist) {
    this.schemaBlacklist = schemaBlacklist;
  }

  /**
   * Table whitelist (where supported).
   **/
  
  @ApiModelProperty(example = "null", value = "Table whitelist (where supported).")
  @JsonProperty("table.whitelist")
  public String getTableWhitelist() {
    return tableWhitelist;
  }
  public void setTableWhitelist(String tableWhitelist) {
    this.tableWhitelist = tableWhitelist;
  }

  /**
   * Table blacklist (where supported).
   **/
  
  @ApiModelProperty(example = "null", value = "Table blacklist (where supported).")
  @JsonProperty("table.blacklist")
  public String getTableBlacklist() {
    return tableBlacklist;
  }
  public void setTableBlacklist(String tableBlacklist) {
    this.tableBlacklist = tableBlacklist;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ParrotSourceApi parrotSource = (ParrotSourceApi) o;
    return Objects.equals(logicalName, parrotSource.logicalName) &&
        Objects.equals(sourceClass, parrotSource.sourceClass) &&
        Objects.equals(bootstrapServers, parrotSource.bootstrapServers) &&
        Objects.equals(schemaWhitelist, parrotSource.schemaWhitelist) &&
        Objects.equals(schemaBlacklist, parrotSource.schemaBlacklist) &&
        Objects.equals(tableWhitelist, parrotSource.tableWhitelist) &&
        Objects.equals(tableBlacklist, parrotSource.tableBlacklist);
  }

  @Override
  public int hashCode() {
    return Objects.hash(logicalName, sourceClass, bootstrapServers, schemaWhitelist, schemaBlacklist, tableWhitelist, tableBlacklist);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ParrotSourceApi {\n");
    
    sb.append("    logicalName: ").append(toIndentedString(logicalName)).append("\n");
    sb.append("    sourceClass: ").append(toIndentedString(sourceClass)).append("\n");
    sb.append("    bootstrapServers: ").append(toIndentedString(bootstrapServers)).append("\n");
    sb.append("    schemaWhitelist: ").append(toIndentedString(schemaWhitelist)).append("\n");
    sb.append("    schemaBlacklist: ").append(toIndentedString(schemaBlacklist)).append("\n");
    sb.append("    tableWhitelist: ").append(toIndentedString(tableWhitelist)).append("\n");
    sb.append("    tableBlacklist: ").append(toIndentedString(tableBlacklist)).append("\n");
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

