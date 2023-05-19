package org.jboss.as.quickstarts.kitchensink_ear.model;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "EtapaCheck")
public class EtapaCheck {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    //@JoinColumn(name = "idAE")
    private AlunoExercicio alunoExercicio;

    @ManyToOne
    //@JoinColumn(name = "idEtapa")
    private Etapa etapa;

    @Column(name = "etapaCheck")
    private Boolean check;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AlunoExercicio getAlunoExercicio() {
        return alunoExercicio;
    }

    public void setAlunoExercicio(AlunoExercicio alunoExercicio) {
        this.alunoExercicio = alunoExercicio;
    }

    public Etapa getEtapa() {
        return etapa;
    }

    public void setEtapa(Etapa etapa) {
        this.etapa = etapa;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }
}
