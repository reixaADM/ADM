package org.jboss.as.quickstarts.kitchensink_ear.model;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Exercicio")
public class Exercicio implements Serializable{

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="idUc")
    @JsonBackReference
    private UnidadeCurricular unidadeCurricular;

    @Column(name = "name", nullable = false)
    private String nome;

    @Column(name = "terminaExercicio")
    private Boolean terminaExercicio;

    public Boolean getTerminaExercicio() {
        return terminaExercicio;
    }

    public void setTerminaExercicio(Boolean terminaExercicio) {
        this.terminaExercicio = terminaExercicio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public UnidadeCurricular getUnidadeCurricular() {
        return unidadeCurricular;
    }

    public void setUnidadeCurricular(UnidadeCurricular unidadeCurricular) {
        this.unidadeCurricular = unidadeCurricular;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


}