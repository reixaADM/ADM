package pt.estg.es.rest;

import org.jboss.as.quickstarts.kitchensink_ear.model.Aluno;
import org.jboss.as.quickstarts.kitchensink_ear.model.UnidadeCurricular;
import pt.estgp.es.services.AlunosService;
import pt.estgp.es.services.UnidadeCurricularService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


@Path("/uc")
@RequestScoped
public class EsRestServiceUC {

    @Inject
    private Logger log;

    @Inject
    private UnidadeCurricularService servico;

    @Inject
    private AlunosService servicoAluno;

    @GET
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response criarUC(@QueryParam("name") String name){

        Response.ResponseBuilder builder;

        try{
            UnidadeCurricular newUc = new UnidadeCurricular();
            newUc.setName(name);

            servico.salvar(newUc);

            builder = Response.ok(newUc);


        } catch (Exception e){
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();

    }


    @GET
    @Path("/associate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response associarAlunoUc(@QueryParam("idAluno") long aluno, @QueryParam("idUc") long uc){

        Response.ResponseBuilder builder;

        try{
            UnidadeCurricular unidadeCurricular = servico.loadByIdUC(uc);
            Aluno alunoEscolhido = servicoAluno.loadById(aluno);

            //Atualizar tabela das Unidades Curriculares
            unidadeCurricular = servico.addAluno(unidadeCurricular, alunoEscolhido);

            //Atualizar tabela dos alunos
            alunoEscolhido = servicoAluno.addUc(alunoEscolhido, unidadeCurricular);

            servicoAluno.atualizar(alunoEscolhido);
            servico.atualizar(unidadeCurricular);

            builder = Response.ok(unidadeCurricular);
        }catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/associate")
    public Response associarAlunoUc(Aluno aluno, UnidadeCurricular uc){

        Response.ResponseBuilder builder;

        try {
            aluno = servicoAluno.addUc(aluno, uc);
            uc = servico.addAluno(uc, aluno);

            servico.atualizar(uc);
            servicoAluno.atualizar(aluno);

            builder = Response.ok();

        }catch (Exception e){
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return null;
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UnidadeCurricular> loadUCs() {
        log.info("Loading alunos");
        return servico.findAll();
    }




}
