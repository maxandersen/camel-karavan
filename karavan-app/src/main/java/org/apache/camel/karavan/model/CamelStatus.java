package org.apache.camel.karavan.model;

import org.infinispan.protostream.annotations.ProtoEnumValue;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

public class CamelStatus {
    public static final String CACHE = "camel_statuses";
    @ProtoField(number = 1)
    String projectId;
    @ProtoField(number = 2)
    Status contextStatus;
    @ProtoField(number = 3)
    Status consumerStatus;
    @ProtoField(number = 4)
    Status routesStatus;
    @ProtoField(number = 5)
    Status registryStatus;
    @ProtoField(number = 6)
    String contextVersion;

    public enum Status {
        @ProtoEnumValue(number = 0, name = "DOWN")
        DOWN,
        @ProtoEnumValue(number = 1, name = "UP")
        UP,
        @ProtoEnumValue(number = 2, name = "NA")
        NA
    }

    @ProtoFactory
    public CamelStatus(String projectId, Status contextStatus, Status consumerStatus, Status routesStatus, Status registryStatus, String contextVersion) {
        this.projectId = projectId;
        this.contextStatus = contextStatus;
        this.consumerStatus = consumerStatus;
        this.routesStatus = routesStatus;
        this.registryStatus = registryStatus;
        this.contextVersion = contextVersion;
    }

    public CamelStatus(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Status getContextStatus() {
        return contextStatus;
    }

    public void setContextStatus(Status contextStatus) {
        this.contextStatus = contextStatus;
    }

    public Status getConsumerStatus() {
        return consumerStatus;
    }

    public void setConsumerStatus(Status consumerStatus) {
        this.consumerStatus = consumerStatus;
    }

    public Status getRoutesStatus() {
        return routesStatus;
    }

    public void setRoutesStatus(Status routesStatus) {
        this.routesStatus = routesStatus;
    }

    public Status getRegistryStatus() {
        return registryStatus;
    }

    public void setRegistryStatus(Status registryStatus) {
        this.registryStatus = registryStatus;
    }

    public String getContextVersion() {
        return contextVersion;
    }

    public void setContextVersion(String contextVersion) {
        this.contextVersion = contextVersion;
    }
}
