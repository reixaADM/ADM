package pt.estg.es.dao;
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

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.core.spi.InjectionPoint;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstarts.kitchensink_ear.model.*;
import org.jboss.as.quickstarts.kitchensink_ear.util.Resources;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import pt.estg.es.security.AuditAnnotation;
import pt.estg.es.security.Auditavel;
import pt.estg.es.security.AuditedInterceptor;
import pt.estgp.es.services.AlunosService;
import pt.estgp.es.services.ExerciciosService;
import pt.estgp.es.services.UnidadeCurricularService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.util.*;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;


@RunWith(Arquillian.class)
public class ExercicioBdIT {

    //Configuração do Arquilian para adicionar os recursos da Arquitetura EE
    @Deployment
    public static Archive<?> createTestArchive() {

        //O ShrinkWrap é o componente que faz a emulação do ambiente EE, neste caso vamos criar uma WAR emulada
        //apesar desta app ser um ejb jar não há problema de emularmos uma WAR
        //com as classes que nos interessam e adicionamos os recursos de configuração que nos interessam
        return ShrinkWrap.create(WebArchive.class, "test.war")

                .addClasses(Resources.class, Exercicio.class, ExerciciosService.class, UnidadeCurricularService.class, UnidadeCurricular.class, Professor.class, Aluno.class, AlunoExercicio.class, AlunosService.class)

                //Usa a BD em memória
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")

                //Usa o MySQL e requer que se crie o esquema primeiro
                .addAsResource("META-INF/test-persistence-mysql.xml", "META-INF/persistence.xml")


                //Usa os interceptores
                .addAsResource("META-INF/beans.xml", "META-INF/beans.xml")

                //Não usa os interceptores
                //.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")

                //Deploy our test datasource
                //.addAsWebInfResource("test-ds.xml", "test-ds.xml");

                //Adiciona o Datasource do MySql requer criação da base de dados ES no MySQL, username e pass no ficheiro
                .addAsWebInfResource("mysql-ds.xml", "mysql-ds.xml");
    }

    @Inject
    EntityManager em;

    @Inject
    Logger log;

    @Inject
    ExerciciosService exerciciosService;

    @Inject
    UnidadeCurricularService ucService;

    @Inject
    AlunosService alunosService;

    static Exercicio a  = new Exercicio();
    static AlunoExercicio b = new AlunoExercicio();

    static Aluno c = new Aluno();

    @Test
    public void testLoadByIdAlunoExe() throws Exception {

        log.info("A testar criação de exercicios");
        UnidadeCurricular uc = new UnidadeCurricular();
        uc.setName("UC_Teste");

        ucService.salvar(uc);
        assertNotNull(ucService.loadByIdUC(uc.getId()));

        a.setUnidadeCurricular(uc);
        a.setNome("Exercicio_Teste");
        a.setTerminaExercicio(false);

        exerciciosService.salvar(a);
        assertNotNull(exerciciosService.loadById(a.getId()));


        //Aluno
        c.setNome("Pedro");
        c.setEmail("dsf@dfsf");
        c.setNumero(21244);
        c.setPassword("123");

        Set<UnidadeCurricular> list = new HashSet<UnidadeCurricular>();
        list.add(uc);
        c.setUnidadeCurricular(list);
        alunosService.salvar(c);
        assertNotNull(alunosService.loadById(c.getId()));

        b.setExercicio(a);
        b.setAluno(c);
        b.setNota(0);
        b.setChamaDocente(false);
        exerciciosService.salvarTabelaExercicioAlunos(b);
        assertNotNull(exerciciosService.loadByIdAlunoExe(b.getId()));



    }
}



