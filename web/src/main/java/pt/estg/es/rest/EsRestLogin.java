package pt.estg.es.rest;

import jdk.nashorn.internal.parser.JSONParser;
import org.codehaus.jackson.map.JsonSerializer;
import org.jboss.as.quickstarts.kitchensink_ear.model.Aluno;
import org.jboss.as.quickstarts.kitchensink_ear.model.Professor;
import pt.estgp.es.services.AlunosService;
import pt.estgp.es.services.ProfessorService;
import org.json.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Path("/log")
@RequestScoped
public class EsRestLogin {

    @Inject
    private Logger log;

    @Inject
    private AlunosService servicoAlunos;

    @Inject
    private ProfessorService servicoProfessor;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(String login){

        Response.ResponseBuilder builder;

        try{
            JSONObject json = new JSONObject(login);

            String user = json.getString("user");

            String password = json.getString("password");

            List<Aluno> listaAluno = servicoAlunos.findAll();
            char isUserAutenticado = 'F';

            for(Aluno l : listaAluno){
                if(servicoAlunos.isAutenticado(l, user, password)){
                    isUserAutenticado = 'A';
                    break;
                }
            }

            List<Professor> listaProfessor = servicoProfessor.findAll();

            for(Professor p : listaProfessor){
                if(servicoProfessor.isAutenticado(p, user, password)){
                    isUserAutenticado = 'P';
                    break;
                }
            }

            //JSONObject returnJson = new JSONObject(isUserAutenticado);

            builder = Response.ok(isUserAutenticado);
        }
        catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }
}
