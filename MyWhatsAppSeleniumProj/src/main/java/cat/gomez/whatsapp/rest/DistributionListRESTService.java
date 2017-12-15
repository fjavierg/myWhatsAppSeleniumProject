package cat.gomez.whatsapp.rest;

import java.net.URI;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import cat.gomez.whatsapp.model.DistributionList;
import cat.gomez.whatsapp.model.User;

@Path("/distributionlist")
@Produces ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Stateless
public class DistributionListRESTService {

    @PersistenceContext(unitName = "MyWhatsAppSeleniumProj")
    private EntityManager em;
 
    //Inject UriInfo to build the uri used in the POST response
    @Context
    private UriInfo uriInfo;
 
    @POST
    @Path("{userid}")
    public Response createDistributionList(DistributionList dl,@PathParam("userid") String userid){
        if(dl == null){
            throw new BadRequestException();
        }
        em.persist(dl);
 
        //Build a uri with the Item id appended to the absolute path
        //This is so the client gets the Item id and also has the path to the resource created
        URI dlUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(dl.getName())).build();
 
        //The created response will not have a body. The itemUri will be in the Header
        return Response.created(dlUri).build();
    }
 
    @GET
    @Path("{userid}")
    public Collection<DistributionList> getDistributionsList(@PathParam("userid") String userid){
        
        TypedQuery<DistributionList> query = em.createNamedQuery("findDLByUserid", DistributionList.class)
                .setParameter("userid", userid);
        return query.getResultList();
    }
    
    @GET
    @Path("{userid}/{dlname}")
    public Response getDistributionList(@PathParam("userid") String userid, @PathParam("dlname") String dlname){
        DistributionList dl = (DistributionList)em.createNamedQuery("findDlByName")
                .setParameter("userid", userid)
                .setParameter("name", dlname)
                .getSingleResult();
 
        if(dl == null){
            throw new NotFoundException();
        }
 
        return Response.ok(dl).build();
    }
 
 
    @PUT
    @Path("{userid}/{dlname}")
    public Response updateDistributionList(DistributionList dl, @PathParam("userid") String userid, @PathParam("dlname") String dlname){
        DistributionList existingDl = (DistributionList)em.createNamedQuery("findDlByName")
                .setParameter("userid", userid)
                .setParameter("name", dlname)
                .getSingleResult();;
 
        if(existingDl == null){
            throw new NotFoundException();
        }
 
        //Ideally we should check the id is a valid UUID. Not implementing for now
        //user.setId(id);
        em.merge(dl);
 
        return Response.ok().build();
    }
 
    @DELETE
    @Path("{userid}/{dlname}")
    public Response deleteDistributionList(@PathParam("userid") String userid, @PathParam("dlname") String dlname){
        DistributionList dl = (DistributionList)em.createNamedQuery("findDlByName")
                .setParameter("userid", userid)
                .setParameter("name", dlname)
                .getSingleResult();
 
        if(dl == null){
            throw new NotFoundException();
        }
        em.remove(dl);
        return Response.noContent().build();
    }
 
}
