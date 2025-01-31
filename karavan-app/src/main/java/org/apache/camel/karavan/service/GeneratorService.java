/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.karavan.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.apicurio.datamodels.Library;
import io.apicurio.datamodels.openapi.models.OasDocument;
import org.apache.camel.CamelContext;
import org.apache.camel.generator.openapi.RestDslGenerator;
import org.apache.camel.impl.lw.LightweightCamelContext;
import org.apache.camel.karavan.model.Project;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import javax.enterprise.context.ApplicationScoped;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Map;

@ApplicationScoped
public class GeneratorService {

    @ConfigProperty(name = "karavan.config.group-id")
    String groupId;

    @ConfigProperty(name = "karavan.config.image-group")
    String imageGroup;

    @ConfigProperty(name = "karavan.config.runtime")
    String runtime;

    @ConfigProperty(name = "karavan.config.runtime-version")
    String runtimeVersion;

    private static final Logger LOGGER = Logger.getLogger(GeneratorService.class.getName());

    public String generate(String fileName, String openApi, boolean generateRoutes) throws Exception {
        final JsonNode node = fileName.endsWith("json") ? readNodeFromJson(openApi) : readNodeFromYaml(openApi);
        OasDocument document = (OasDocument) Library.readDocument(node);
        try (CamelContext context = new LightweightCamelContext()) {
            return RestDslGenerator.toYaml(document).generate(context, generateRoutes);
        }
    }

    private JsonNode readNodeFromJson(String openApi) throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(openApi);
    }

    private JsonNode readNodeFromYaml(String openApi) throws FileNotFoundException {
        final ObjectMapper mapper = new ObjectMapper();
        Yaml loader = new Yaml(new SafeConstructor());
        Map map = loader.load(openApi);
        return mapper.convertValue(map, JsonNode.class);
    }

    public String getDefaultApplicationProperties(Project project){
        StringBuilder s = new StringBuilder();
        s.append("camel.jbang.project-id=").append(project.getProjectId()).append(System.lineSeparator());
        s.append("camel.jbang.project-name=").append(project.getName()).append(System.lineSeparator());
        s.append("camel.jbang.project-description=").append(project.getDescription()).append(System.lineSeparator());
        s.append("camel.jbang.gav=").append(groupId).append(":").append(project.getProjectId()).append(":").append("1.0.0").append(System.lineSeparator());
        s.append("camel.jbang.runtime=").append(runtime.toLowerCase()).append(System.lineSeparator());
        s.append("camel.jbang.quarkusVersion=").append(runtimeVersion).append(System.lineSeparator());
        s.append("camel.jbang.dependencies=")
                .append("camel:microprofile-health,")
                .append("mvn:io.quarkus:quarkus-container-image-jib,")
                .append("mvn:io.quarkus:quarkus-openshift").append(System.lineSeparator());

        s.append("camel.health.enabled=true").append(System.lineSeparator());
        s.append("camel.health.exposure-level=full").append(System.lineSeparator());

        s.append("quarkus.kubernetes-client.trust-certs=true").append(System.lineSeparator());
        s.append("quarkus.container-image.group=").append(imageGroup).append(System.lineSeparator());
        s.append("quarkus.container-image.name=").append(project.getProjectId()).append(System.lineSeparator());
        s.append("quarkus.openshift.route.expose=false").append(System.lineSeparator());
        s.append("quarkus.openshift.part-of=").append(project.getProjectId()).append(System.lineSeparator());
        s.append("quarkus.openshift.replicas=1").append(System.lineSeparator());
        return s.toString();
    }
}
