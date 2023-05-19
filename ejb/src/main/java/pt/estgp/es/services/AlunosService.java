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
package pt.estgp.es.services;

import org.jboss.as.quickstarts.kitchensink_ear.model.Aluno;
import org.jboss.as.quickstarts.kitchensink_ear.model.Member;
import org.jboss.as.quickstarts.kitchensink_ear.model.Professor;
import org.jboss.as.quickstarts.kitchensink_ear.model.UnidadeCurricular;
import pt.estg.es.security.AuditAnnotation;
import pt.estg.es.security.Auditavel;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Null;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

//import org.jboss.as.quickstarts.kitchensink_ear.model.Aluno_;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Auditavel
@Stateless
public class AlunosService {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;


    public void criarNovoAluno(Aluno aluno) throws Exception
    {
        log.info("Registando " + aluno.getNome());
        em.persist(aluno);
    }


    public Aluno loadById(Long id) {
        return em.find(Aluno.class, id);
    }


    @AuditAnnotation(conf = "find all")
    public List<Aluno> findAll(){

        log.info("Listando todos os alunos");
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Aluno> criteria = cb.createQuery(Aluno.class);

        Root<Aluno> aluno = criteria.from(Aluno.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        //criteria.select(aluno).orderBy(cb.asc(aluno.get(Aluno_.nome)));
        criteria.select(aluno).orderBy(cb.asc(aluno.get("nome")));
        return em.createQuery(criteria).getResultList();
    }

    @AuditAnnotation(conf = "find all hibernate")
    public List<Aluno> findAllHibernate(){

        log.info("Listando todos os alunos modo hibernate");
        List<Aluno> list = obtainSessionFactory().getCurrentSession()
                .createCriteria(Aluno.class)
                .list();
        return list;
    }

    public Aluno addUc(Aluno aluno, UnidadeCurricular uc){

        Set<UnidadeCurricular> listUc = aluno.getUnidadeCurricular();
        listUc.add(uc);
        aluno.setUnidadeCurricular(listUc);

        return aluno;
    }

    public boolean isAutenticado(Aluno aluno, String user, String password){
        if(user.contains("@")){
            if(user.equals(aluno.getEmail()) && password.equals(aluno.getPassword()))
                return true;
        }
        else
            if(user.equals(aluno.getNome()) && password.equals(aluno.getPassword()))
                return true;

        return false;
    }

    @AuditAnnotation(conf = "find one by name")
    public Aluno findAlunoByName(String nome){

        List<Aluno> list = obtainSessionFactory().getCurrentSession()
                .createCriteria(Aluno.class)
                .list();

        for(int i = 0; i < list.size(); i++){
            Aluno aluno = list.get(i);
            if(aluno.getNome().equals(nome)){
                return aluno;
            }
        }

        //Objeto Vazio
        Aluno aluno = new Aluno();
        aluno.setId((long) -1);

        return aluno;
    }


    public void salvar(Aluno aluno) throws Exception {
        log.info("Registando " + aluno.getNome());

        //Usando o Entity Manager
        em.persist(aluno);

        //Usando o Hibernate
        //obtainSessionFactory().getCurrentSession().save(member);

    }

    public void atualizar(Aluno aluno) throws Exception {
        log.info("Atualizando " + aluno.getNome());

        em.merge(aluno);

    }

    public org.hibernate.SessionFactory obtainSessionFactory()
    {
        return em.getEntityManagerFactory()
                .unwrap(org.hibernate.SessionFactory.class);
    }

}
