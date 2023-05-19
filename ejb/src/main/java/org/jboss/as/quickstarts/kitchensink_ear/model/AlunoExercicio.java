package org.jboss.as.quickstarts.kitchensink_ear.model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Aluno_Exercicio")
public class AlunoExercicio implements Serializable{


    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idExercicio")
    private Exercicio exercicio;

    @ManyToOne
    @JoinColumn(name = "idAluno")
    private Aluno aluno;


    @Column(name = "chamaDocente")
    private Boolean chamaDocente;



    @Column
    private int nota;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Exercicio getExercicio() {
        return exercicio;
    }

    public void setExercicio(Exercicio exercicio) {
        this.exercicio = exercicio;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Boolean getChamaDocente() {
        return chamaDocente;
    }

    public void setChamaDocente(Boolean chamdaDocente) {
        this.chamaDocente = chamdaDocente;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }
}
