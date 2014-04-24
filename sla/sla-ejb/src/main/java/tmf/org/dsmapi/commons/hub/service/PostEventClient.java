/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package tmf.org.dsmapi.commons.hub.service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import java.util.HashMap;
import javax.ejb.Stateless;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

/**
* Jersey REST client generated for REST resource:HubFacadeREST
* [tmf.org.dsmapi.hub.hub]<br>
* USAGE:
* <pre>
*        PostEventClient client = new PostEventClient();
*        Object response = client.XXX(...);
*        // do whatever with response
*        client.close();
* </pre>
*
* @author pierregauthier
*/
@Stateless
public class PostEventClient {

    private HashMap<String, WebResource> webResources = new HashMap<String, WebResource>();
    private static Client jaxrsClient;


    public void publishEvent(String callbackURL, Object requestEntity) throws UniformInterfaceException {
        System.out.println("publishEvent "+requestEntity);
        WebResource webResource = getWebResource(callbackURL);
        webResource.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(requestEntity);
    }

    public void close() {
        jaxrsClient.destroy();
    }
    
    private Client getJaxrsClient() {
        if (jaxrsClient == null) {
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(SerializationConfig.Feature.INDENT_OUTPUT, true).configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, true);
            JacksonJaxbJsonProvider jacksonProvider = new JacksonJaxbJsonProvider();
            jacksonProvider.setMapper(mapper);
            ClientConfig config = new DefaultClientConfig();
            config.getSingletons().add(jacksonProvider);
            config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
            config.getClasses().add(JacksonJsonProvider.class);
            jaxrsClient = Client.create(config);
        }
        return jaxrsClient;
    }

    // In memory caching, webResources and client are thread safe see jersey doc
    private WebResource getWebResource(String endpointURL) {
        if (!webResources.containsKey(endpointURL)) {
            WebResource webResource = getJaxrsClient().resource(endpointURL).path("");
            webResources.put(endpointURL, webResource);
        }
        return webResources.get(endpointURL);
    }
    
    
}

