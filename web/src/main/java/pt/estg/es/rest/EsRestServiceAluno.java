/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pt.estg.es.rest;

import org.jboss.as.quickstarts.kitchensink_ear.model.Aluno;
import org.jboss.as.quickstarts.kitchensink_ear.model.Professor;
import org.jboss.as.quickstarts.kitchensink_ear.model.UnidadeCurricular;
import pt.estgp.es.services.UnidadeCurricularService;
import pt.estgp.es.services.AlunosService;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import javax.sound.sampled.Port;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.logging.Logger;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/alunos")
@RequestScoped
public class EsRestServiceAluno {

    @Inject
    private Logger log;

    @Inject
    private AlunosService servico;

    @Inject
    private UnidadeCurricularService servicoUc;

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Aluno> loadAlunos() {
        log.info("Loading alunos");
        return servico.findAll();
    }

    @GET
    @Path("/listHibernate")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Aluno> loadAlunosHibernate() {
        log.info("Loading alunos mode hibernate");
        return servico.findAllHibernate();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Aluno loadAlunoById(@PathParam("id") long id)
    {
        log.info("Loading aluno: " + id);
        Aluno aluno = servico.loadById(id);
        if (servico == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return aluno;
    }


    /**
     * Creates a new member from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create")
    public Response criarAluno(Aluno aluno) {

        Response.ResponseBuilder builder = null;

        try {
            // Validates member using bean validation
            // validateMember(member); //usaria as anotações javax.validator que não estamos a usar no Aluno

            servico.salvar(aluno);

            // Create an "ok" response
            builder = Response.ok();
        /*} catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());*/
      /*  } catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "Email taken");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);*/
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }


    @GET
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response criarAluno(@QueryParam("nome") String nome,@QueryParam("numero") Integer numero) {

        Response.ResponseBuilder builder;

        try {
            // Validates member using bean validation
            // validateMember(member); //usaria as anotações javax.validator que não estamos a usar no Aluno

            Aluno aluno = new Aluno();
            aluno.setNome(nome);
            aluno.setNumero(numero);

            servico.salvar(aluno);

            // Create an "ok" response
            builder = Response.ok(aluno);
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }

    /*
    private void validateMember(Member member) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<>(violations));
        }

        // Check the uniqueness of the email address
        if (emailAlreadyExists(member.getEmail())) {
            throw new ValidationException("Unique Email Violation");
        }
    }*/

    @POST
    @Path("/associate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response associarAlunoUc(@QueryParam("idAluno") long aluno, @QueryParam("idUc") long uc){

        Response.ResponseBuilder builder;

        try{
            UnidadeCurricular unidadeCurricular = servicoUc.loadByIdUC(uc);
            Aluno alunoEscolhido = servico.loadById(aluno);

            //Atualizar tabela das Unidades Curriculares
            unidadeCurricular = servicoUc.addAluno(unidadeCurricular, alunoEscolhido);

            //Atualizar tabela dos alunos
            alunoEscolhido = servico.addUc(alunoEscolhido, unidadeCurricular);

            servico.atualizar(alunoEscolhido);
            servicoUc.atualizar(unidadeCurricular);

            builder = Response.ok("ok");
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
