package pt.estgp.es.services;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.logging.Logger;

@Stateless
public class SqlService {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;


    public void popularBaseDeDados(){

        em.createNativeQuery("INSERT INTO aluno (id, email, nome, numero, password) VALUES (?,?,?,?,?)")
                .setParameter(1, 1)
                .setParameter(2, "21123@ipportalegre.pt")
                .setParameter(3, "Duarte Heitor Carita")
                .setParameter(4, 21123)
                .setParameter(5, 123)
                .executeUpdate();

        em.createNativeQuery("INSERT INTO aluno (id, email, nome, numero, password) VALUES (?,?,?,?,?)")
                .setParameter(1, 2)
                .setParameter(2, "21118@ipportalegre.pt")
                .setParameter(3, "Luis Reixa")
                .setParameter(4, 21118)
                .setParameter(5, 123)
                .executeUpdate();

        em.createNativeQuery("INSERT INTO aluno (id, email, nome, numero, password) VALUES (?,?,?,?,?)")
                .setParameter(1, 3)
                .setParameter(2, "21126@ipportalegre.pt")
                .setParameter(3, "Vasco Marmelo")
                .setParameter(4, 21126)
                .setParameter(5, 123)
                .executeUpdate();

        em.createNativeQuery("INSERT INTO unidadecurricular (id, name) VALUES (?,?)")
                .setParameter(1, 1)
                .setParameter(2, "Engenharia de Software")
                .executeUpdate();

        em.createNativeQuery("INSERT INTO professor (id, email, nome, password, unidadecurricular) VALUES (?,?,?,?,?)")
                .setParameter(1, 1)
                .setParameter(2, "jmachado@ipportalegre.pt")
                .setParameter(3, "Jorge Machado")
                .setParameter(4, 123)
                .setParameter(5, 1)
                .executeUpdate();

        em.createNativeQuery("INSERT INTO aluno_unidadecurricular (aluno_id, unidadecurricular_id) VALUES (?,?)")
                .setParameter(1, 1)
                .setParameter(2, 1)
                .executeUpdate();

        em.createNativeQuery("INSERT INTO aluno_unidadecurricular (aluno_id, unidadecurricular_id) VALUES (?,?)")
                .setParameter(1, 2)
                .setParameter(2, 1)
                .executeUpdate();

        em.createNativeQuery("INSERT INTO aluno_unidadecurricular (aluno_id, unidadecurricular_id) VALUES (?,?)")
                .setParameter(1, 3)
                .setParameter(2, 1)
                .executeUpdate();

    }

}
