package pt.estgp.es.services;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.as.quickstarts.kitchensink_ear.model.Aluno;
import org.jboss.as.quickstarts.kitchensink_ear.model.UnidadeCurricular;
import org.jboss.as.quickstarts.kitchensink_ear.model.Member;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Stateless
public class UnidadeCurricularService {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;


    public UnidadeCurricular loadByIdUC(Long id) {
        return em.find(UnidadeCurricular.class, id);
    }

    public UnidadeCurricular addAluno(UnidadeCurricular uc, Aluno aluno){

        Set<Aluno> lista = uc.getAluno();
        lista.add(aluno);
        uc.setAluno(lista);

        return uc;
    }

    public void salvar(UnidadeCurricular uc) throws Exception {

        log.info("Registando " + uc.getName());
        //Usando o Entity Manager
        em.persist(uc);
        //Usando o Hibernate
        //obtainSessionFactory().getCurrentSession().save(member);

    }

    public void atualizar(UnidadeCurricular uc) throws Exception {
        log.info("Atualizando " + uc.getName());

        em.merge(uc);

    }

    public List<UnidadeCurricular> findAll(){

        log.info("Listando todos os alunos");
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<UnidadeCurricular> criteria = cb.createQuery(UnidadeCurricular.class);

        Root<UnidadeCurricular> uc = criteria.from(UnidadeCurricular.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        //criteria.select(aluno).orderBy(cb.asc(aluno.get(Aluno_.nome)));
        criteria.select(uc).orderBy(cb.asc(uc.get("name")));
        return em.createQuery(criteria).getResultList();
    }


}
