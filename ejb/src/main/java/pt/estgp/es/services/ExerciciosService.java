package pt.estgp.es.services;

import org.hibernate.criterion.Restrictions;
import org.jboss.as.quickstarts.kitchensink_ear.model.*;
import pt.estg.es.security.Auditavel;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Logger;

@Auditavel
@Stateless
public class ExerciciosService {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    public Exercicio loadById(Long id) {
        return em.find(Exercicio.class, id);
    }

    public AlunoExercicio loadByIdAlunoExe(Long id) {
        return em.find(AlunoExercicio.class, id);
    }

    public void criarExercicio(Exercicio exercicio){
        log.info("Registando " + exercicio.getNome());
        em.persist(exercicio);
    }


    public AlunoExercicio loadByIdAlunoExercicio(Aluno aluno, Exercicio exercicio){
        List<AlunoExercicio> list = obtainSessionFactory().getCurrentSession()
                .createCriteria(AlunoExercicio.class)
                .list();

        for(AlunoExercicio x : list){
            if(x.getExercicio().getId().equals(exercicio.getId()) && x.getAluno().getId().equals(aluno.getId())){
                return x;
            }
        }

        AlunoExercicio obj = new AlunoExercicio();
        return obj;
    }


    public void salvar(Exercicio exercicio) throws Exception {
        log.info("Registando " + exercicio.getNome());

        //Usando o Entity Manager
        em.persist(exercicio);

        //Usando o Hibernate
        //obtainSessionFactory().getCurrentSession().save(member);

    }

    public List<Exercicio> listaExercicios(Professor prof){

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Exercicio> criteria = cb.createQuery(Exercicio.class);

        Root<Exercicio> ex = criteria.from(Exercicio.class);

        criteria.select(ex).where(cb.equal(ex.get("unidadeCurricular"),prof.getUnidadeCurricular())).orderBy(cb.asc(ex.get("id")));

        return em.createQuery(criteria).getResultList();

    }

    public List<AlunoExercicio> listAeByEx(Exercicio exercicio){
        return obtainSessionFactory().getCurrentSession()
                .createCriteria(AlunoExercicio.class)
                .add(Restrictions.eq("exercicio",exercicio))
                .list();
    }

    public void atualizar(Exercicio exercicio) throws Exception {
        log.info("Atualizando " + exercicio.getNome());

        em.merge(exercicio);

    }

    public void salvarTabelaExercicioAlunos(AlunoExercicio alunoExercicio) throws Exception {
        log.info("Registando " + alunoExercicio.getId());

        //Usando o Entity Manager
        em.persist(alunoExercicio);

        //Usando o Hibernate
        //obtainSessionFactory().getCurrentSession().save(member);

    }

    public void atualizarTabelaExercicioAlunos(AlunoExercicio alunoExercicio) throws Exception {
        log.info("Atualizando " + alunoExercicio.getId());

        em.merge(alunoExercicio);

    }

    public org.hibernate.SessionFactory obtainSessionFactory()
    {
        return em.getEntityManagerFactory()
                .unwrap(org.hibernate.SessionFactory.class);
    }



}
