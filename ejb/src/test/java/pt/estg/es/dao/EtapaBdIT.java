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
import pt.estgp.es.services.AlunosService;
import pt.estgp.es.services.EtapaService;
import pt.estgp.es.services.ExerciciosService;
import pt.estgp.es.services.UnidadeCurricularService;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;


@RunWith(Arquillian.class)
public class EtapaBdIT {


    //Configuração do Arquilian para adicionar os recursos da Arquitetura EE
    @Deployment
    public static Archive<?> createTestArchive() {

        //O ShrinkWrap é o componente que faz a emulação do ambiente EE, neste caso vamos criar uma WAR emulada
        //apesar desta app ser um ejb jar não há problema de emularmos uma WAR
        //com as classes que nos interessam e adicionamos os recursos de configuração que nos interessam
        return ShrinkWrap.create(WebArchive.class, "test.war")

                .addClasses(Resources.class, Exercicio.class, ExerciciosService.class, UnidadeCurricular.class, UnidadeCurricularService.class, Etapa.class, EtapaService.class, AlunoExercicio.class, Aluno.class, Professor.class, EtapaCheck.class, AlunosService.class)

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
    EtapaService etapaService;
    @Inject
    ExerciciosService exerciciosService;
    @Inject
    UnidadeCurricularService ucService;

    @Inject
    AlunosService alunosService;

    static Exercicio exercicio = new Exercicio();

    static Etapa etapa = new Etapa();

    static Aluno aluno = new Aluno();

    static  AlunoExercicio alunoExercicio = new AlunoExercicio();
    @Test
    public void testFindEtapaByEx() throws Exception {

        log.info("A testar find de etapas");
        UnidadeCurricular uc = new UnidadeCurricular();
        uc.setName("UC_Teste");

        ucService.salvar(uc);
        assertNotNull(ucService.loadByIdUC(uc.getId()));

        exercicio.setUnidadeCurricular(uc);
        exercicio.setNome("Exercicio_Teste");
        exercicio.setTerminaExercicio(false);

        exerciciosService.salvar(exercicio);
        assertNotNull(exerciciosService.loadById(exercicio.getId()));

        etapa.setExercicio(exercicio);
        etapa.setPergunta("teste");
        etapa.setResposta("Exemplo");
        etapaService.salvar(etapa);
        assertNotNull(etapaService.loadById(etapa.getId()));

        assertNotNull(etapaService.findEtapaByEx(exercicio));
    }

    @Test
    public void testFindEtapaByIdAlunoExercicioEtapa() throws Exception {

        log.info("A testar find de etapas");
        UnidadeCurricular uc = new UnidadeCurricular();
        Exercicio exercicio1 = new Exercicio();
        Etapa etapa1 = new Etapa();
        uc.setName("UC_Teste");

        ucService.salvar(uc);
        assertNotNull(ucService.loadByIdUC(uc.getId()));

        aluno.setNome("Pedro");
        aluno.setEmail("dsf@dfsf");
        aluno.setNumero(21244);
        aluno.setPassword("123");
        alunosService.salvar(aluno);
        assertNotNull(alunosService.loadById(aluno.getId()));

        exercicio1.setUnidadeCurricular(uc);
        exercicio1.setNome("Exercicio_Teste");
        exercicio1.setTerminaExercicio(false);

        exerciciosService.salvar(exercicio1);
        assertNotNull(exerciciosService.loadById(exercicio1.getId()));

        alunoExercicio.setAluno(aluno);
        alunoExercicio.setExercicio(exercicio1);
        alunoExercicio.setNota(20);
        alunoExercicio.setChamaDocente(false);

        exerciciosService.salvarTabelaExercicioAlunos(alunoExercicio);
        assertNotNull(exerciciosService.loadByIdAlunoExe(alunoExercicio.getId()));

        etapa1.setExercicio(exercicio1);
        etapa1.setPergunta("teste");
        etapa1.setResposta("Exemplo");
        etapaService.salvar(etapa1);
        assertNotNull(etapaService.loadById(etapa1.getId()));

        assertNotNull(etapaService.findEtapaByIdAlunoExercicioEtapa(alunoExercicio,etapa1));
    }
}
