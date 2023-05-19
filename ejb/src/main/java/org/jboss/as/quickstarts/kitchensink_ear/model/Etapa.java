package org.jboss.as.quickstarts.kitchensink_ear.model;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "Etapa")
public class Etapa implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="idExercicio")
    private Exercicio exercicio;

    @Column(name = "pergunta")
    private String pergunta;

    @Column(name = "resposta")
    private String resposta;

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

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }
}
