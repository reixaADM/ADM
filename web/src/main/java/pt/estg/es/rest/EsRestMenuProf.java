package pt.estg.es.rest;


import org.jboss.as.quickstarts.kitchensink_ear.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import pt.estgp.es.services.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Path("/professor")
@RequestScoped
public class EsRestMenuProf {

    @Inject
    private Logger log;

    @Inject
    private AlunosService servicoAluno;

    @Inject
    private EtapaService servicoEtapa;

    @Inject
    private ProfessorService servicoProfessor;

    @Inject
    private ExerciciosService servicoExercicio;


    @POST
    @Path("/listExByProf")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response listExsByProf(String prof){

        Response.ResponseBuilder builder;

        try{
            JSONObject json = new JSONObject(prof);

            String user = json.getString("user");

            Professor professor = servicoProfessor.findProfessorByName(user);

            List<Exercicio> exercicios = servicoExercicio.listaExercicios(professor);

            builder = Response.ok(exercicios);
        }
        catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }

    @POST
    @Path("/dadosExercicio")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response dadosExercicio(String id){

        Response.ResponseBuilder builder;

        try{
            JSONObject json = new JSONObject(id);

            long idEx = json.getLong("id");

            Exercicio exercicio = servicoExercicio.loadById(idEx);
            List<Etapa> etapas = servicoEtapa.findEtapaByEx(exercicio);

            JSONObject resposta = new JSONObject();

            resposta.put("exercicoNome", exercicio.getNome());

            JSONArray arrayetapas = new JSONArray();

            for(Etapa e : etapas){
                JSONObject aux = new JSONObject();
                aux.put("id", e.getId());
                aux.put("pergunta", e.getPergunta());
                arrayetapas.put(aux);
            }

            resposta.put("etapas",arrayetapas);

            resposta.put("exercicoTerminar", exercicio.getTerminaExercicio());

            builder = Response.ok(resposta.toString());
        }
        catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();

    }

    @POST
    @Path("/exercicioConteudo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response exercicioConteudo(String id){

        Response.ResponseBuilder builder;

        try{
            JSONObject json = new JSONObject(id);

            long idEx = json.getLong("id");


            Exercicio exercicio = servicoExercicio.loadById(idEx);

            List<AlunoExercicio> alunosInscritos = servicoExercicio.listAeByEx(exercicio);

            List<Etapa> etapas = servicoEtapa.findEtapasByExercicio(exercicio);

            JSONObject resposta = new JSONObject();
            JSONArray auxArray = new JSONArray();
            for(AlunoExercicio ae : alunosInscritos){

                JSONObject aux = new JSONObject();
                aux.put("idAluno",ae.getAluno().getId());
                aux.put("nomeAluno",ae.getAluno().getNome());
                aux.put("notaExercicio", ae.getNota());
                aux.put("chamaDocente", ae.getChamaDocente());
                JSONArray auxA = new JSONArray();

                for(Etapa e: etapas){

                    JSONObject etapasResponse = new JSONObject();
                    etapasResponse.put("etapaID",e.getId());
                    List<EtapaCheck> etapaCheck = servicoEtapa.findEtapaByIdAlunoExercicioEtapa(ae, e);
                    EtapaCheck etapaCheckResponse = etapaCheck.get(0);
                    etapasResponse.put("check", etapaCheckResponse.getCheck());

                    auxA.put(etapasResponse);
                }
                aux.put("etapas",auxA);
                auxArray.put(aux);
            }
            resposta.put("alunos",auxArray);

            for(AlunoExercicio alunoExercicio : alunosInscritos){
                alunoExercicio.setChamaDocente(false);
                servicoExercicio.atualizarTabelaExercicioAlunos(alunoExercicio);
            }

            builder = Response.ok(resposta.toString());
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