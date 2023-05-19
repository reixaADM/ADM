package pt.estgp.es.services;

import org.hibernate.SessionFactory;
import org.jboss.as.quickstarts.kitchensink_ear.model.Aluno;
import org.jboss.as.quickstarts.kitchensink_ear.model.Professor;
import pt.estg.es.security.AuditAnnotation;
import pt.estg.es.security.Auditavel;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Logger;

@Auditavel
@Stateless
public class ProfessorService {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    public void criarNovoProfessor(Professor professor) throws Exception
    {
        log.info("Registando " + professor.getNome());
        em.persist(professor);
    }

    public Professor loadById(Long id) {
        return em.find(Professor.class, id);
    }

    @AuditAnnotation(conf = "find all")
    public List<Professor> findAll(){

        log.info("Listando todos os professores");
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Professor> criteria = cb.createQuery(Professor.class);

        Root<Professor> professor = criteria.from(Professor.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        //criteria.select(aluno).orderBy(cb.asc(aluno.get(Aluno_.nome)));
        criteria.select(professor).orderBy(cb.asc(professor.get("nome")));
        return em.createQuery(criteria).getResultList();
    }

    @AuditAnnotation(conf = "find one by name")
    public Professor findProfessorByName(String nome){

        List<Professor> list = obtainSessionFactory().getCurrentSession()
                .createCriteria(Professor.class)
                .list();

        for(int i = 0; i < list.size(); i++){
            Professor professor = list.get(i);
            if(professor.getNome().equals(nome)){
                return professor;
            }
        }

        //Objeto Vazio
        Professor professor = new Professor();
        professor.setId(-1);

        return professor;
    }

    @AuditAnnotation(conf = "find all hibernate")
    public List<Professor> findAllHibernate(){

        log.info("Listando todos os alunos modo hibernate");
        List<Professor> list = obtainSessionFactory().getCurrentSession()
                .createCriteria(Professor.class)
                .list();

        return list;
    }

    public boolean isAutenticado(Professor professor, String user, String password){
        if(user.contains("@")){
            if(user.equals(professor.getEmail()) && password.equals(professor.getPassword()))
                return true;
        }
        else
        if(user.equals(professor.getNome()) && password.equals(professor.getPassword()))
            return true;

        return false;
    }

    public org.hibernate.SessionFactory obtainSessionFactory()
    {
        return em.getEntityManagerFactory()
                .unwrap(org.hibernate.SessionFactory.class);
    }

    public void salvar(Professor professor) throws Exception {
        log.info("Registando " + professor.getNome());

        //Usando o Entity Manager
        em.persist(professor);

        //Usando o Hibernate
        //obtainSessionFactory().getCurrentSession().save(member);

    }

    public void atualizar(Professor professor) throws Exception {
        log.info("Atualizando " + professor.getNome());

        em.merge(professor);

    }


}
