## **1. Installation**

### **1.1 Prerequisites**

The following prerequisites are necessary to correctly install and configure the **Parrot Stream Distribution** for Cloudera:

1. Cloudera Manager 5.5.3 or higher
2. CDH 5.4.x or higher
3. [Kafka](https://www.cloudera.com/documentation/kafka/latest/topics/kafka_install.html) 2.1.x or higher

### **1.2 CSDs download and install**

To install **Parrot Stream Distribution** CSDs you have to:

1. download your release of CSDs jars from the [**Parrot Stream CSD GitHub repository**](https://github.com/parrot-stream/parrot-cloudera-csds/releases);
2. copy them in the CSDs directory on the Cloudera Manager server `/opt/cloudera/csd` (the default).  For example, to install the `v1.0.0-alpha.1` release of the Parrot Stream CSDs do:

        PS_GIT_URL=https://github.com/parrot-stream/parrot-cloudera-csds/releases/download
        PS_VER=1.0.0-alpha.1
        CSD_HOME=/opt/cloudera/csd
        sudo wget -P $CSD_HOME $PS_GIT_URL/v$PS_VER/SCHEMA_REGISTRY-$PS_VER.jar
        sudo wget -P $CSD_HOME $PS_GIT_URL/v$PS_VER/KAFKA_REST-$PS_VER.jar
        sudo wget -P $CSD_HOME $PS_GIT_URL/v$PS_VER/PARROT_STREAM-$PS_VER.jar
        sudo wget -P $CSD_HOME $PS_GIT_URL/v$PS_VER/PARROT_MANAGER-$PS_VER.jar
        sudo chown cloudera-scm:cloudera-scm $CSD_HOME/*.jar
        sudo chmod 744 /opt/cloudera/csd/*.jar
          
3. restart the Cloudera Manager Server with:

        sudo service cloudera-scm-server restart
4. go to *Hosts -> Parcels* in Cloudera Manager and **Download**, **Distribute** and **Activate** the following parcels:

    - KAFKA_REST
    - SCHEMA_REGISTRY
    - PARROT_MANAGER
    - PARROT_STREAM

5.  return to the Cloudera Manager Home and select **Add Service** in the drop down menu just right the cluster name. Adding Parrot services remember that they depends on each other and on CDH and Kafka services, so you should install them in the following order:

| Order | Service          | Depends on       |
|-------|------------------|------------------|
|   1   | Schema Registry  | ZooKeeper (CDH)  |
|       |                  | Kafka            |
|   2   | Kafka REST Proxy | Schema Registry  |
|       |                  | Kafka            |
|   3   | Parrot Stream    | Schema Registry  |
|       |                  | Kafka            |
|   4   | Parrot Manager   | Schema Registry  |
|       |                  | Kafka REST Proxy |
|       |                  | Parrot Stream    |

In the following chapter you'll find how to configure each service in the cluster.

## **2. Configuration**

### **2.1 Schema Registry**

[**Confluent Schema Registry**](http://docs.confluent.io/current/schema-registry/docs/intro.html) provides a serving layer for your metadata. It provides a RESTful interface for storing and retrieving Avro schemas. It stores a versioned history of all schemas, provides multiple compatibility settings and allows evolution of schemas according to the configured compatibility setting. It provides serializers that plug into Kafka clients that handle schema storage and retrieval for Kafka messages that are sent in the Avro format.

To add a new Schema Registry role you have to set the correct **security protocol** used by Schema Registry to connect to a Kafka cluster.

| Parameter                    | Value     | Description
|------------------------------|-----------|----------------------------
| kafkastore.security.protocol | PLAINTEXT | If the Kafka cluster is not using neither Kerberos or SSL 
|                              | SSL       | If Kafka cluster is using only SSL
|                              | SASL      | If Kafka cluster is using only Kerberos
|                              | SASL_SSL  | If Kafka cluster is using both Kerberos and SSL

#### **2.1.1 SSL configuration to communicate with Kafka brokers**

In case of SSL is enabled in Kafka an you want your Schema Registry communicate with Kafka broker in SSL, using SSL or SASL_SSL security protocol, you have two cases:

1. parameter `ssl.client.auth` is `none` or `requested` in Kafka brokers: you need just to configure the **truststore** file location and password:

| Parameter                      | Value                                  |
|--------------------------------|----------------------------------------|
| kafkastore.ssl.truststore.location | [full path of the jks truststore file]
| kafkastore.ssl.truststore.password | [trustore password]

2. parameter `ssl.client.auth` is `required` in Kafka brokers: you need also to configure the **keystore** file location and password and the key password:

| Parameter                      | Value                                    |
|--------------------------------|------------------------------------------|
| kafkastore.ssl.keystore.location   | *[full path of the jks keystore file]*   |
| kafkastore.ssl.keystore.password   | *[keystore password]*                    |
| kafkastore.ssl.key.password        | *[ssl private key password]*             |

#### **2.1.2 SSL configuration for Schema Registry in HTTPS**

In case you want to expose Schema Registry using SSL you have to enable it setting true the property `ssl.enabled` and define the keystore file location and password and the SSL private key password:

| Parameter               | Value                                    |
|-------------------------|------------------------------------------|
| ssl.keystore.location   | *[full path of the jks keystore file]*   |
| ssl.keystore.password   | *[keystore password]*                    |
| ssl.key.password        | *[ssl private key password]*             |


### **2.2 Kafka REST Proxy**

[**Confluent Kafka REST Proxy**](http://docs.confluent.io/current/kafka-rest/docs/intro.html) provides a RESTful interface to a Kafka cluster. In the Parrot Distribution for Cloudera it is used by the Parrot Stream and Parrot Manager services.

#### **2.2.1 SSL configuration to communicate with Kafka brokers**

As explained for the Schema Registry configuration, you should define **truststore** and **keystore** following these rules:

1. if in Kafka brokers the parameter `ssl.client.auth` is `none` or `requested`:

| Parameter                      | Value                                    |
|--------------------------------|------------------------------------------|
| client.ssl.truststore.location | *[full path of the jks truststore file]* |
| client.ssl.truststore.password | *[trustore password]*                    |

2. if in Kafka broker the parameter `ssl.client.auth` is `required`:

| Parameter                      | Value                                    |
|--------------------------------|------------------------------------------|
| client.ssl.keystore.location   | *[full path of the jks keystore file]*   |
| client.ssl.keystore.password   | *[keystore password]*                    |
| client.ssl.key.password        | *[ssl private key password]*             |

You might also need to set the parameter `bootstrap.servers` which is a list of Kafka brokers to connect to, if different brokers use different security protocols. For example:

| Parameter         | Value                         |
|-------------------|-------------------------------|
| bootstrap.servers | PLAINTEXT://[hostname-1]:9092 |
|                   | SSL://[hostname-2]:9093       |
|                   | SASL_SSL://[hostname-3]:9093  |

This configuration is particularly important when Kafka security is enabled, because Kafka may expose multiple endpoints that all will be stored in ZooKeeper, but Kafka REST may need to be configured with just one of those endpoints. The client will make use of all servers irrespective of which servers are specified here for bootstrapping: this list only impacts the initial hosts used to discover the full set of servers. Since these servers are just used for the initial connection to discover the full cluster membership (which may change dynamically), this list need 
not contain the full set of servers (you may want more than one, though, in case a server is down).

#### **2.2.2 SSL configuration for Kafka REST Proxy in HTTPS**

In case you want to expose Kafka REST Proxy using SSL you have to enable it setting true the property `ssl.enabled` and define the keystore file location and password and the SSL private key password:

| Parameter               | Value                                    |
|-------------------------|------------------------------------------|
| ssl.keystore.location   | *[full path of the jks keystore file]*   |
| ssl.keystore.password   | *[keystore password]*                    |
| ssl.key.password        | *[ssl private key password]*             |

### **2.3 Parrot Stream**

[**Parrot Stream**](https://github.com/parrot-stream/parrot.git) is a distribution of [**Confluent Kafka Connect**](http://docs.confluent.io/current/kafka-connect/docs/intro.html) togheter with the [Confluent certified connectors](https://www.confluent.io/product/connectors/) and the [Parrot](https://github.com/parrot-stream/parrot.git) ones. **Kafka Connect** is a framework for scalably and reliably connecting Kafka with external systems such as databases, key-value stores, search indexes, and file systems.

You need to set the mandatory parameter `bootstrap.servers` which is a list of Kafka brokers to connect to. For example:

| Parameter         | Value                         |
|-------------------|-------------------------------|
| bootstrap.servers | PLAINTEXT://[hostname-1]:9092 |
|                   | SSL://[hostname-2]:9093       |
|                   | SASL_PLAINTEXT://[hostname-2]:9092
|                   | SASL_SSL://[hostname-3]:9093  |

> **NOTE**: If you have less then 3 Kafka brokers instances, reduce the replication factor which has a default value of 3: it cannot be larger then the number of Kafka brokers. So if you have just 1 Kafka broker change the following values to 1:

>| Parameter                       | Value
|---------------------------------|--------
|config.storage.replication.factor|1
|offset.storage.replication.factor|1
|status.storage.replication.factor|1
 
#### **2.3.1 SSL configuration to communicate with Kafka brokers**

In case of SSL is enabled in Kafka you have to cases:

1. parameter `ssl.client.auth` is `none` or `requested` in Kafka brokers: you need just to configure the **truststore** file location and password:

    | Parameter                      | Value                                    |
|--------------------------------|------------------------------------------|
| ssl.truststore.location | *[full path of the jks truststore file]* |
| ssl.truststore.password | *[trustore password]*                    |

2. parameter `ssl.client.auth` is `required` in Kafka brokers: you need also to configure the **keyststore** file location and password and the key password:

| Parameter                      | Value                                    |
|--------------------------------|------------------------------------------|
| client.ssl.keystore.location   | *[full path of the jks keystore file]*   |
| client.ssl.keystore.password   | *[keystore password]*                    |
| client.ssl.key.password        | *[ssl private key password]*             |

#### **2.3.2 SSL configuration for Parrot Stream in HTTPS**

In case you want to expose Kafka Connect using SSL you have to enable it setting true the property `ssl.enabled` and define the keystore file location and password and the SSL private key password:

| Parameter               | Value                                    |
|-------------------------|------------------------------------------|
| ssl.keystore.location   | *[full path of the jks keystore file]*   |
| ssl.keystore.password   | *[keystore password]*                    |
| ssl.key.password        | *[ssl private key password]*             |

### **2.4 Parrot Manager**

Parrot Manager is composed by the following UIs:

1. [**Schema Registry UI**](https://github.com/parrot-stream/schema-registry-ui.git): a tool to create / view / search / evolve / view history and configure Avro schemas of your Kafka cluster using the Confluent Schema Registry
2. [**Kafka Topics UI**](https://github.com/parrot-stream/kafka-topics-ui.git): a tool to browse Kafka topics
3. [**Kafka Connect UI**](https://github.com/parrot-stream/kafka-connect-ui.git): a tool for setting up and managing connectors in Parrot Stream

When choosing the roles to install you can choose just the Schema Registry UI and Kafka Topics UI: the Kafka Connect UI will be installed on the same node chosen for Kafka Topics UI.

#### **2.4.1 SSL configuration for HTTPS**

To configure SSL you have to enable `ssl.enabled` and define the **keystore** in the UI you want to get secured:
 
| Parameter               | Value                                    |
|-------------------------|------------------------------------------|
| ssl.keystore.location   | *[full path of the PEM keystore file]*   |
| ssl.keystore.password   | *[PEM keystore password]*                    |
| ssl.truststore.password | *[ PEM truststore password]*                 |

For developing purposes only you can instead enable the parameter `ssl.generate.self.signed` to get a self-signed certificate automatically created by Schema Registry UI.
Alternatively, if you want to generate a PEM certificate to use setting the previous parameters you can use the PEM certificate generator as described in the following chapter.

## **3 Self-signed certificates**

For developing purposes you can use self-signed certificate, JKS or PEM ones.

### **3.1 JKS certificates**

The script `jks-cert.sh` helps you to automate the creation and installation of JKS certificates on the Cloudera cluster nodes.

The following command generates a self-signed JKS certificate for the FQDN host **hostname.domainname** with the alias **kafka** and puts it under the `/var/private/ssl/kafka` directory in the keystore file `kafka-keystore.jks`. The password for the keystore and the key password is `password`.

    sudo -E ./jks-cert.sh create -a=kafka -h=hostname.domainname -sd=/var/private/ssl/kafka

You can use such certificate in the Parrot Manager:

| Parameter               | Value                                         |
|-------------------------|-----------------------------------------------|
| ssl.keystore.location   | /var/private/ssl/kafka/jks/kafka-keystore.jks
| ssl.keystore.password   | password
| ssl.key.password        | password
| ssl.truststore.location | /var/private/ssl/kafka/jks/kafka-keystore.jks
| ssl.truststore.password | password

### **3.2 PEM certificates**

The script `pem-cert.sh` helps you to automate the creation and installation of self-signed PEM certificates on the Cloudera cluster nodes.

The following command generates a self-signed PEM certificate with the alias **parrot-manager** and puts it under the `/var/private/ssl/parrot-manager` directory in the keystore file `parrot-manager-key.pem`. The password for the keystore and the key password is `password`.
  
   sudo -E ./pem-cert.sh create -a=parrot-manager -sd=/var/private/ssl/parrot-manager

setting the following configuration:

| Parameter               | Value
|-------------------------|-----
| ssl.enabled             | true
| ssl.self.signed.cert    | false
| ssl.keystore.location   | /var/private/ssl/parrot-manager/parrot-manager-key.pem
| ssl.keystore.password   | password
| ssl.truststore.location | /var/private/ssl/parrot-manager/parrot-manager-cert.pem

## **4 Cloudera Security**

In this chapter you can find some hints to quickly configure TLS and SASL in a Cloudera cluster with a single node: this can be useful to quickly set up a development environment.
For a production environment please refer to the [Cloudera Security](https://www.cloudera.com/documentation/enterprise/latest/topics/security.html)  official documentation.

### **4.1 TLS for Cloudera Manager**

1. generate a self-signed certificate in the keystore with alias `cloudera-manager`, for the host with FQDN hostname `[hostname.domainname]`:

        ./jks-cert.sh create -a=cloudera-manager -h=[hostname.domainname] -sd=/opt/cloudera/security
        
2. Configure Cloudera Manager Admin Security in *Administration -> Settings -> Security*:

    | Parameter | Value |
    |---------------|---------|
    | Use TLS Encryption for Admin Console | true |
    | Cloudera Manager TLS/SSL Server JKS Keystore File Location | /opt/cloudera/security/jks/cloudera-manager-keystore.jks |
    | Cloudera Manager TLS/SSL Server JKS Keystore File Password | password |
    | Cloudera Manager TLS/SSL Certificate Trust Store File | /opt/cloudera/security/jks/cloudera-manager-keystore.jks |
    | Cloudera Manager TLS/SSL Certificate Trust Store Password | password

3. Configure Cloudera Management Service in *Configuration -> Security*:

    | Parameter | Value |
    |---------------|---------|
    | ssl.client.truststore.location | /opt/cloudera/security/jks/cloudera-manager-keystore.jks |
    | ssl.client.truststore.password | password

4. Restart Cloudera Manager

        sudo service cloudera-scm-server restart

#### **4.1.1 Level 1: TLS for Cloudera Manager Agents**

1. Go to *Administration -> Settings -> Security*:

    | Parameter | Value |
    |---------------|--------|
    | Use TLS Encryption for Agents | true |

2. Edit `/etc/cloudera-scm-agent/config.ini` for each agent:

        server_host=[hostname.domainname]
        use_tls=1

3. Restart Cloudera Manager and Agents:

        sudo service cloudera-scm-server restart 
        sudo service cloudera-scm-agent restart

4. Verify the heartbeats occurring in the *Hosts* page in Cloudera Manager Admin Console

#### **4.1.2 Level 2: TLS Verification of Cloudera Manager Server by the Agents**

For each agent:

1. Copy the server certificate `cloudera-manager.pem` to all Agent in:

        /opt/cloudera/security/x509/

2. Edit `/etc/cloudera-scm-agent/config.ini` for each agent:

        verify_cert_file=/opt/cloudera/security/x509/cloudera-manager.pem

3. Install openssl-perl on each agent:

        sudo yum install -y openssl-perl

4. Generate symbolic links for the certificate on each agent:

        cd /opt/cloudera/security/x509/
        c_rehash .

5. Restart Cloudera Management Agents and Cloudera Management Services:

        sudo service cloudera-scm-agent restart

6. Verify the heartbeats occurring in the *Hosts* page in Cloudera Manager Admin Console

#### **4.1.3 Level 3: TLS Authentication of Agents to the Cloudera Manager Server**

For each agent:

1. Create a file with the kaystore password, for example `/etc/cloudera-scm-agent/agentkey.pw` which will contain the password `password`:

        chown cloudera-scm:cloudera-scm /etc/cloudera-scm-agent/agentkey.pw
        chmod 600 /etc/cloudera-scm-agent/agentkey.pw

2. Edit `/etc/cloudera-scm-agent/config.ini`:

        client_key_file=/opt/cloudera/security/x509/cloudera-manager.key
        client_keypw_file=/etc/cloudera-scm-agent/agentkey.pw
        client_cert_file=/opt/cloudera/security/x509/cloudera-manager.pem

3. Restart the agent:

        sudo service cloudera-scm-agent restart

4. Enable Agent Authentication in *Administration -> Settings -> Security*:

      | Parameter | Value |
      |---------------|--------|
      | Use TLS Authentication of Agents to Server | true|

5. Restart Cloudera Manager Server and Agents:

        sudo service cloudera-scm-server restart
        sudo service cloudera-scm-agent restart

### **4.2 SASL (Kerberos) for Cloudera Manager**

In this chapter you'll read how to install and configure a KDC server and how to enable Kerberos in Cloudera Manager.
 
### **4.2.1 Install a KDC server**

These instructions are mainly based on [Configuring the Kerberos KDC](https://access.redhat.com/documentation/en-US/Red_Hat_Enterprise_Linux/7/html/System-Level_Authentication_Guide/Configuring_a_Kerberos_5_Server.html).

1. Install packages:

        sudo yum -y install ntp krb5-server krb5-workstation krb5-libs openldap-clients

 > **NOTE**: following, replace `hostname`, `HOSTNAME`, `domainname` and `DOMAINNAME` with your real host name and domain name (lower and upper case).

2. Edit `/etc/krb5.conf` as root and update with:

        [logging]
        default = FILE:/var/log/krb5libs.log
        kdc = FILE:/var/log/krb5kdc.log
        admin_server = FILE:/var/log/kadmind.log

        [libdefaults]
        default_realm = DOMAINNAME
        dns_lookup_realm = false
        dns_lookup_kdc = false
        ticket_lifetime = 24h
        renew_lifetime = 7d
        forwardable = true

        [realms]
        DOMAINNAME = {
          kdc =  hostname.domainname:88
          admin_server = hostname.domainname
          default_domain = domainname
        }

        [domain_realm]
        .domainname = DOMAINNAME
        hostname.domainname = DOMAINNAME

3. Edit `/var/kerberos/krb5kdc/kdc.conf` as root and update with:

        default_realm = DOMAINNAME

        [kdcdefaults] 
        kdc_ports = 88
        kdc_tcp_ports = 88

        [realms]
        DOMAINNAME = {
          max_life = 1d
          max_renewable_life = 7d 0h 0m 0s
          acl_file = /var/kerberos/krb5kdc/kadm5.acl
          dict_file = /usr/share/dict/words
          admin_keytab = /var/kerberos/krb5kdc/kadm5.keytab
          supported_enctypes = rc4-hmac:normal
          default_principal_flags = +renewable, +forwardable
        }

4. Edit `/var/kerberos/krb5kdc/kadm5.acl` as root and update with:

        */admin@DOMAINNAME      *
        cloudera-scm@DOMAINNAME * flume/*@DOMAINNAME
        cloudera-scm@DOMAINNAME * hbase/*@DOMAINNAME
        cloudera-scm@DOMAINNAME * hdfs/*@DOMAINNAME
        cloudera-scm@DOMAINNAME * hive/*@DOMAINNAME
        cloudera-scm@DOMAINNAME * httpfs/*@DOMAINNAME
        cloudera-scm@DOMAINNAME * HTTP/*@DOMAINNAME
        cloudera-scm@DOMAINNAME * hue/*@DOMAINNAME
        cloudera-scm@DOMAINNAME * impala/*@DOMAINNAME
        cloudera-scm@DOMAINNAME * mapred/*@DOMAINNAME
        cloudera-scm@DOMAINNAME * oozie/*@DOMAINNAME
        cloudera-scm@DOMAINNAME * solr/*@DOMAINNAME
        cloudera-scm@DOMAINNAME * sqoop/*@DOMAINNAME
        cloudera-scm@DOMAINNAME * yarn/*@DOMAINNAME
        cloudera-scm@DOMAINNAME * zookeeper/*@DOMAINNAME
        cloudera-scm@DOMAINNAME * kafka/*@DOMAINNAME

5. Create a Kerberos database (with pwd `parrot`):

        sudo service krb5kdc stop
        sudo service kadmin stop
        sudo kdestroy -A
        sudo kdb5_util destroy -f
        sudo rm -rf /var/kerberos/krb5kdc/principal*
        sudo kdb5_util -r DOMAINNAME create -s

6. Create an admin principal:

        sudo kadmin.local -q "addprinc root/admin"

7. Start the Kerberos KDC and kadmin daemons:

        sudo service krb5kdc restart
        sudo service kadmin restart

8. Add the following lines at the bottom of `/etc/rc.d/rc.local`:

        service krb5kdc start
        service kadmin start
        
### **4.2.2 Configure Kerberos in Cloudera Manager**

Following instructions are base on [Cloudera Documentation](https://www.cloudera.com/documentation/enterprise/latest/topics/cm_sg_intro_kerb.html).

1. Create a Kerberos Principal for the Cloudera Manager Server:

        sudo kadmin -q "addprinc -pw parrot cloudera-scm/admin@DOMAINNAME"

  and test it with:

        kinit cloudera-scm/admin@CLOUDERA

2. Enable Kerberos using the Wizard in *Administration -> Security* and using the "cloudera-scm/admin@DOMAINNAME" user previously created

