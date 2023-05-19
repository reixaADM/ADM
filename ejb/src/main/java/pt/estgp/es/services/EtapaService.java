package pt.estgp.es.services;


import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.jboss.as.quickstarts.kitchensink_ear.model.*;
import pt.estg.es.security.Auditavel;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


@Auditavel
@Stateless
public class EtapaService {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;


    public Etapa loadById(Long id) {
        return em.find(Etapa.class, id);
    }
//---------------------
    public List<Etapa> findEtapasByExercicio(Exercicio exercicio) throws Exception{
        List<Etapa> list = obtainSessionFactory().getCurrentSession()
                .createCriteria(Etapa.class)
                .list();

        List<Etapa> retorno = new ArrayList<>();

        for(Etapa x : list){
            Exercicio exe1 = x.getExercicio();
            if(exe1.getId() == exercicio.getId()){
                retorno.add(x);
            }
        }

        return retorno;
    }
//-----------------------------



    public List<Etapa> findEtapaByEx(Exercicio exercicio){
        return obtainSessionFactory().getCurrentSession()
                .createCriteria(Etapa.class).add(Restrictions.eq("exercicio",exercicio))
                .list();
    }

    public List<EtapaCheck> findEtapaByIdAlunoExercicioEtapa(AlunoExercicio alunoExercicio, Etapa etapa){
        return obtainSessionFactory().getCurrentSession()
                .createCriteria(EtapaCheck.class)
                .add(Restrictions.eq("alunoExercicio", alunoExercicio))
                .add(Restrictions.eq("etapa", etapa))
                .list();

    }

    public List<EtapaCheck> findEtapaCheckByEx(AlunoExercicio alunoExercicio){
        return obtainSessionFactory().getCurrentSession()
                .createCriteria(EtapaCheck.class).add(Restrictions.eq("alunoExercicio",alunoExercicio))
                .list();
    }


    public void salvar(Etapa etapa) throws Exception {
        log.info("Registando " + etapa.getPergunta());

        //Usando o Entity Manager
        em.persist(etapa);

        //Usando o Hibernate
        //obtainSessionFactory().getCurrentSession().save(member);

    }

    public void atualizar(Etapa etapa) throws Exception {
        log.info("Atualizando " + etapa.getPergunta());

        em.merge(etapa);

    }

    public void salvarEtapasCheck(EtapaCheck etapa) throws Exception {
        log.info("Registando " + etapa.getCheck());

        //Usando o Entity Manager
        em.persist(etapa);

        //Usando o Hibernate
        //obtainSessionFactory().getCurrentSession().save(member);

    }

    public void atualizarEtapasCheck(EtapaCheck etapa) throws Exception {
        log.info("Atualizando " + etapa.getCheck());

        em.merge(etapa);

    }

    public org.hibernate.SessionFactory obtainSessionFactory()
    {
        return em.getEntityManagerFactory()
                .unwrap(org.hibernate.SessionFactory.class);
    }
}
