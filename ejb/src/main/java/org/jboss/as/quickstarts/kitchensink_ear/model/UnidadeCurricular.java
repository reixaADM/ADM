package org.jboss.as.quickstarts.kitchensink_ear.model;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.mapping.Join;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "UnidadeCurricular")
public class UnidadeCurricular implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "unidadeCurricular", fetch = FetchType.EAGER)
    private Set<Aluno> aluno;

    @OneToOne(mappedBy = "unidadeCurricular")
    private Professor professor;

    // Fetchtype em eager por causa do erro failed to lazily initialize a collection of role
    // Mudado de List para Set por causa do erro  Caused by: javax.persistence.PersistenceException: [PersistenceUnit: primary] Unable to build Hibernate SessionFactory
    //[ERROR]     Caused by: org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags: [org.jboss.as.quickstarts.kitchensink_ear.model.UnidadeCurricular.aluno, org.jboss.as.quickstarts.kitchensink_ear.model.UnidadeCurricular.exercicios]"}}}}
    @OneToMany(mappedBy = "unidadeCurricular", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Exercicio> exercicios;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Aluno> getAluno() {
        return aluno;
    }

    public void setAluno(Set<Aluno> aluno) {
        this.aluno = aluno;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }


    public Set<Exercicio> getExercicios() {
        return exercicios;
    }

    public void setExercicios(Set<Exercicio> exercicios) {
        this.exercicios = exercicios;
    }
}
