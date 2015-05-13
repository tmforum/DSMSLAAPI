package org.tmf.dsmapi.sla;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.tmf.dsmapi.commons.exceptions.BadUsageException;
import org.tmf.dsmapi.commons.exceptions.UnknownResourceException;
import org.tmf.dsmapi.commons.jaxrs.Report;
import org.tmf.dsmapi.sla.model.Sla;
import org.tmf.dsmapi.sla.event.SlaEvent;
import org.tmf.dsmapi.sla.event.SlaEventFacade;
import org.tmf.dsmapi.sla.event.SlaEventPublisherLocal;

@Stateless
@Path("admin/sla")
public class SlaAdminResource {

    @EJB
    SlaFacade slaFacade;
    @EJB
    SlaEventFacade eventFacade;
    @EJB
    SlaEventPublisherLocal publisher;

    @GET
    @Produces({"application/json"})
    public List<Sla> findAll() {
        return slaFacade.findAll();
    }

    /**
     *
     * For test purpose only
     * @param entities
     * @return
     */
    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response post(List<Sla> entities) {

        if (entities == null) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        int previousRows = slaFacade.count();
        int affectedRows;

        // Try to persist entities
        try {
            affectedRows = slaFacade.create(entities);
            for (Sla entitie : entities) {
//                publisher.createNotification(entitie, new Date());
            }
        } catch (BadUsageException e) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        Report stat = new Report(slaFacade.count());
        stat.setAffectedRows(affectedRows);
        stat.setPreviousRows(previousRows);

        // 201 OK
        return Response.created(null).
                entity(stat).
                build();
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response update(@PathParam("id") long id, Sla entity) throws UnknownResourceException {
        Response response = null;
        Sla sla = slaFacade.find(id);
        if (sla != null) {
            entity.setId(id);
            slaFacade.edit(entity);
//            publisher.valueChangedNotification(entity, new Date());
            // 201 OK + location
            response = Response.status(Response.Status.CREATED).entity(entity).build();

        } else {
            // 404 not found
            response = Response.status(Response.Status.NOT_FOUND).build();
        }
        return response;
    }

    /**
     *
     * For test purpose only
     * @return
     * @throws org.tmf.dsmapi.commons.exceptions.UnknownResourceException
     */
    @DELETE
    public Report deleteAll() throws UnknownResourceException {

        eventFacade.removeAll();
        int previousRows = slaFacade.count();
        slaFacade.removeAll();
        List<Sla> pis = slaFacade.findAll();
        for (Sla pi : pis) {
            delete(pi.getId());
        }

        int currentRows = slaFacade.count();
        int affectedRows = previousRows - currentRows;

        Report stat = new Report(currentRows);
        stat.setAffectedRows(affectedRows);
        stat.setPreviousRows(previousRows);

        return stat;
    }

    /**
     *
     * For test purpose only
     * @param id
     * @return
     * @throws UnknownResourceException
     */
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) throws UnknownResourceException {
        try {
            int previousRows = slaFacade.count();
            Sla entity = slaFacade.find(id);

            // Event deletion
//            publisher.deleteNotification(entity, new Date());
            try {
                //Pause for 4 seconds to finish notification
                Thread.sleep(4000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SlaAdminResource.class.getName()).log(Level.SEVERE, null, ex);
            }
            // remove event(s) binding to the resource
            List<SlaEvent> events = eventFacade.findAll();
            for (SlaEvent event : events) {
                if (event.getResource().getId().equals(id)) {
                    eventFacade.remove(event.getId());
                }
            }
            //remove resource
            slaFacade.remove(id);

            int affectedRows = 1;
            Report stat = new Report(slaFacade.count());
            stat.setAffectedRows(affectedRows);
            stat.setPreviousRows(previousRows);

            // 200 
            Response response = Response.ok(stat).build();
            return response;
        } catch (UnknownResourceException ex) {
            Logger.getLogger(SlaAdminResource.class.getName()).log(Level.SEVERE, null, ex);
            Response response = Response.status(Response.Status.NOT_FOUND).build();
            return response;
        }
    }

    @GET
    @Produces({"application/json"})
    @Path("event")
    public List<SlaEvent> findAllEvents() {
        return eventFacade.findAll();
    }

    @DELETE
    @Path("event")
    public Report deleteAllEvent() {

        int previousRows = eventFacade.count();
        eventFacade.removeAll();
        int currentRows = eventFacade.count();
        int affectedRows = previousRows - currentRows;

        Report stat = new Report(currentRows);
        stat.setAffectedRows(affectedRows);
        stat.setPreviousRows(previousRows);

        return stat;
    }

    @DELETE
    @Path("event/{id}")
    public Response deleteEvent(@PathParam("id") String id) throws UnknownResourceException {

        int previousRows = eventFacade.count();
        List<SlaEvent> events = eventFacade.findAll();
        for (SlaEvent event : events) {
            if (event.getResource().getId().equals(id)) {
                eventFacade.remove(event.getId());

            }
        }
        int currentRows = eventFacade.count();
        int affectedRows = previousRows - currentRows;

        Report stat = new Report(currentRows);
        stat.setAffectedRows(affectedRows);
        stat.setPreviousRows(previousRows);

        // 200 
        Response response = Response.ok(stat).build();
        return response;
    }

    /**
     *
     * @return
     */
    @GET
    @Path("count")
    @Produces({"application/json"})
    public Report count() {
        return new Report(slaFacade.count());
    }
}
