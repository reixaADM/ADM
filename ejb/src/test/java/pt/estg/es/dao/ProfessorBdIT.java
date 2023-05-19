package pt.estg.es.dao;

import javassist.expr.Cast;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstarts.kitchensink_ear.model.*;
import org.jboss.as.quickstarts.kitchensink_ear.util.Resources;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import pt.estgp.es.services.*;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;


@RunWith(Arquillian.class)
public class ProfessorBdIT {


    //Configuração do Arquilian para adicionar os recursos da Arquitetura EE
    @Deployment
    public static Archive<?> createTestArchive() {

        //O ShrinkWrap é o componente que faz a emulação do ambiente EE, neste caso vamos criar uma WAR emulada
        //apesar desta app ser um ejb jar não há problema de emularmos uma WAR
        //com as classes que nos interessam e adicionamos os recursos de configuração que nos interessam
        return ShrinkWrap.create(WebArchive.class, "test.war")

                .addClasses(Resources.class,Professor.class,ProfessorService.class,UnidadeCurricular.class, UnidadeCurricularService.class, Aluno.class, Exercicio.class)

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
    ProfessorService servicoProfessor;

    @Inject
    UnidadeCurricularService servicoUnidadeCurricular;

    static Professor professor = new Professor();

    static UnidadeCurricular unidadeCurricular = new UnidadeCurricular();
    @Test
    public void testFindProfessorByName() throws Exception {

        log.info("A testar find do professor pelo nome");

        professor.setNome("João Martins");
        professor.setEmail("jmartins@ipportalegre.pt");
        professor.setPassword("123");
        servicoProfessor.salvar(professor);

        unidadeCurricular.setName("Unidade Currilcular Teste");
        unidadeCurricular.setProfessor(professor);
        servicoUnidadeCurricular.salvar(unidadeCurricular);

        professor.setUnidadeCurricular(unidadeCurricular);
        servicoProfessor.atualizar(professor);


        assertNotNull(servicoProfessor.findProfessorByName(professor.getNome()));

    }
}
