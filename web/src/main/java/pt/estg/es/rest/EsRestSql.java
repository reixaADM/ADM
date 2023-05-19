package pt.estg.es.rest;


import org.jboss.as.quickstarts.kitchensink_ear.model.Aluno;
import pt.estgp.es.services.AlunosService;
import pt.estgp.es.services.SqlService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Logger;

@Path("/sql")
@RequestScoped
public class EsRestSql {

    @Inject
    private Logger log;

    @Inject
    private SqlService servicoSql;

    @GET
    @Path("/script")
    @Produces(MediaType.APPLICATION_JSON)
    public void runScript() {
        log.info("Popular Base de Dados");
        servicoSql.popularBaseDeDados();
    }

}
