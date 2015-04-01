package org.tmf.dsmapi;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(org.tmf.dsmapi.commons.jaxrs.BadUsageExceptionMapper.class);
        classes.add(org.tmf.dsmapi.commons.jaxrs.JacksonConfigurator.class);
        classes.add(org.tmf.dsmapi.commons.jaxrs.JsonMappingExceptionMapper.class);
        classes.add(org.tmf.dsmapi.commons.jaxrs.UnknowResourceExceptionMapper.class);
        classes.add(org.tmf.dsmapi.hub.HubResource.class);
        classes.add(org.tmf.dsmapi.sla.SlaAdminResource.class);
        classes.add(org.tmf.dsmapi.sla.SlaResource.class);
        classes.add(org.tmf.dsmapi.slaViolation.SlaViolationAdminResource.class);
        classes.add(org.tmf.dsmapi.slaViolation.SlaViolationResource.class);
        // following code can be used to customize Jersey 2.x JSON provider:
        try {
			Class jacksonProvider = Class.forName("org.glassfish.jersey.jackson.JacksonFeature");
			classes.add(jacksonProvider);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return classes;
    }

}
