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
import java.util.*;
import java.util.logging.Logger;


@Path("/exercicio")
@RequestScoped
public class EsRestExercicio {

    @Inject
    private Logger log;

    @Inject
    private ExerciciosService serviceExercicio;

    @Inject
    private ProfessorService serviceProfessor;

    @Inject
    private EtapaService serviceEtapa;

    @Inject
    private UnidadeCurricularService serviceUC;

    @Inject
    private AlunosService serviceAluno;

    @POST
    @Path("/criar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response criarExercicio(String data){

        Response.ResponseBuilder builder;

        try{
            JSONObject exercicio = new JSONObject(data);

            String nomeProfessor = exercicio.getString("professor");
            String nomeExercicio = exercicio.getString("nome");
            JSONArray etapas = exercicio.getJSONArray("etapas");

            String[] perguntas = new String[etapas.length()];

            for(int i = 0; i < etapas.length(); i++){
                String pergunta = etapas.getJSONObject(i).getString("pergunta");

                if(!pergunta.equals("")){
                    perguntas[i] = pergunta;
                }
            }

            //Criar exercicio com etapas
            Exercicio novoExercicio = new Exercicio();
            novoExercicio.setNome(nomeExercicio);
            novoExercicio.setTerminaExercicio(false);

            Professor professor = serviceProfessor.findProfessorByName(nomeProfessor);
            novoExercicio.setUnidadeCurricular(professor.getUnidadeCurricular());

            serviceExercicio.salvar(novoExercicio);


            //Criar Etapas

            List<Etapa> etapasTeste = new ArrayList<Etapa>();

            for(int i = 0; i < etapas.length(); i++){
                Etapa etapaNova = new Etapa();
                etapaNova.setPergunta(perguntas[i]);
                etapaNova.setExercicio(novoExercicio);
                etapasTeste.add(etapaNova);
                serviceEtapa.salvar(etapaNova);
            }
            List<AlunoExercicio> testeAlunosExercicio = new ArrayList<AlunoExercicio>();
            //Criar Aluno_Exercicio
            UnidadeCurricular uc = professor.getUnidadeCurricular();
            for(Aluno a : uc.getAluno()){
                AlunoExercicio auxiliar = new AlunoExercicio();
                auxiliar.setExercicio(novoExercicio);
                auxiliar.setAluno(a);
                auxiliar.setNota(0);
                auxiliar.setChamaDocente(false);
                testeAlunosExercicio.add(auxiliar);
                serviceExercicio.salvarTabelaExercicioAlunos(auxiliar);
            }

            //------------------------
            for(Etapa x : etapasTeste){
                for (AlunoExercicio a : testeAlunosExercicio) {
                    EtapaCheck auxiliar = new EtapaCheck();
                    auxiliar.setEtapa(x);
                    auxiliar.setCheck(false);
                    auxiliar.setAlunoExercicio(a);
                    serviceEtapa.salvarEtapasCheck(auxiliar);
                }
            }

            builder = Response.ok("T");
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
    @Path("/apresentar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExerciciosByAluno(String data){
        Response.ResponseBuilder builder;
        //Aluno, apanhar as ucs e depois nas ucs apanhar os exercicios
        try{
            JSONObject aluno = new JSONObject(data);

            String nomeAluno = aluno.getString("nome");

            Aluno aluno1 = serviceAluno.findAlunoByName(nomeAluno);
            if (aluno1.getId() == -1){
                builder = Response.ok("E");
                return builder.build();
            }
            Set<UnidadeCurricular> listaUcs = aluno1.getUnidadeCurricular();
            List<Exercicio> listaExerciciosTotal = new ArrayList<Exercicio>();

            for (UnidadeCurricular x : listaUcs) {

                Set<Exercicio> listaExercicios = x.getExercicios();

                for(Exercicio ex : listaExercicios){
                    listaExerciciosTotal.add(ex);
                }
            }

            JSONObject retorno = new JSONObject();
            retorno.put("idAluno", aluno1.getId());

            JSONArray array = new JSONArray();

            for (int i = 0; i < listaExerciciosTotal.size(); i++){
                JSONObject aux = new JSONObject();
                aux.put("nomeUc", listaExerciciosTotal.get(i).getUnidadeCurricular().getName());
                aux.put("nomeExercicio", listaExerciciosTotal.get(i).getNome());
                aux.put("idExercicio", listaExerciciosTotal.get(i).getId());
                aux.put("terminaExercicio", listaExerciciosTotal.get(i).getTerminaExercicio());

                array.put(aux);
            }

            retorno.put("exercicios", array);

            builder = Response.ok(retorno.toString());

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
    @Path("/exercicio_aluno")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response formAlunoExercicio(String data){

        Response.ResponseBuilder builder;

        try{
            JSONObject json = new JSONObject(data);
            Long idAluno = json.getLong("idAluno");
            Long idExercicio = json.getLong("idExe");

            Aluno aluno = serviceAluno.loadById(idAluno);
            Exercicio exercicio = serviceExercicio.loadById(idExercicio);

            AlunoExercicio alunoExercicio= serviceExercicio.loadByIdAlunoExercicio(aluno, exercicio);
            List<Etapa> etapas = serviceEtapa.findEtapaByEx(exercicio);

            JSONObject retorno = new JSONObject();

            JSONArray arrayEtapas = new JSONArray();

            for(Etapa e : etapas){
                JSONObject aux = new JSONObject();
                aux.put("id", e.getId());
                aux.put("pergunta", e.getPergunta());

                List<EtapaCheck> etapaChecks = serviceEtapa.findEtapaByIdAlunoExercicioEtapa(alunoExercicio, e);
                EtapaCheck etapaCheck = etapaChecks.get(0);
                Boolean check = etapaCheck.getCheck();
                aux.put("check", check);

                arrayEtapas.put(aux);
            }

            retorno.put("etapas", arrayEtapas);

            retorno.put("chamaDocente", alunoExercicio.getChamaDocente());

            builder = Response.ok(retorno.toString());

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
    @Path("/update_etapacheck")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateEtapaCheck(String data){

        Response.ResponseBuilder builder;

        try {
            JSONObject json = new JSONObject(data);
            long aluno = json.getLong("idAluno");
            long exercicio = json.getLong("idExercicio");
            JSONArray etapas = json.getJSONArray("etapas");

            Aluno alunoPedido = serviceAluno.loadById(aluno);
            Exercicio exercicioPedido = serviceExercicio.loadById(exercicio);
            AlunoExercicio alunoExercicio = serviceExercicio.loadByIdAlunoExercicio(alunoPedido, exercicioPedido);


            for(int i = 0; i < etapas.length(); i++){
                JSONObject etapaI = etapas.getJSONObject(i);
                Long idEtapa = etapaI.getLong("id");
                Etapa etapaPedido = serviceEtapa.loadById(idEtapa);

                List<EtapaCheck> catchEtapaCheck = serviceEtapa.findEtapaByIdAlunoExercicioEtapa(alunoExercicio, etapaPedido);
                EtapaCheck etapaCheck = catchEtapaCheck.get(0);

                Boolean check = etapaI.getBoolean("check");
                etapaCheck.setCheck(check);
                serviceEtapa.atualizarEtapasCheck(etapaCheck);
            }


            builder = Response.ok("T");
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
    @Path("/update_chamadocente")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateChamaDocente(String data){

        Response.ResponseBuilder builder;

        try {
            JSONObject json = new JSONObject(data);
            long aluno = json.getLong("idAluno");
            long exercicio = json.getLong("idExercicio");

            Aluno alunoPedido = serviceAluno.loadById(aluno);
            Exercicio exercicioPedido = serviceExercicio.loadById(exercicio);
            AlunoExercicio alunoExercicio = serviceExercicio.loadByIdAlunoExercicio(alunoPedido, exercicioPedido);

            alunoExercicio.setChamaDocente(true);

            serviceExercicio.atualizarTabelaExercicioAlunos(alunoExercicio);

            builder = Response.ok("T");
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
    @Path("/updateNota")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateNota (String update){
        Response.ResponseBuilder builder;

        try {
            JSONObject json = new JSONObject(update);
            int nota = json.getInt("nota");
            long idExercicios = json.getLong("idExercicio");
            long idAluno = json.getLong("idAluno");

            Aluno aluno = serviceAluno.loadById(idAluno);
            Exercicio exercicio = serviceExercicio.loadById(idExercicios);

            AlunoExercicio alunoExercicio = serviceExercicio.loadByIdAlunoExercicio(aluno, exercicio);
            alunoExercicio.setNota(nota);

            serviceExercicio.atualizarTabelaExercicioAlunos(alunoExercicio);

            builder = Response.ok("T");
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
    @Path("/terminaExercicio")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response terminaExercicio (String terminar){
        Response.ResponseBuilder builder;

        try {
            JSONObject json = new JSONObject(terminar);
            long idExercicios = json.getLong("idExercicio");

            Exercicio exercicio = serviceExercicio.loadById(idExercicios);

            exercicio.setTerminaExercicio(true);

            serviceExercicio.atualizar(exercicio);

            builder = Response.ok("T");
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


