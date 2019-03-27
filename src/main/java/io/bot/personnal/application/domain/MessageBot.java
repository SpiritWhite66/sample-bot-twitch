package io.bot.personnal.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A MessageBot.
 */
@Entity
@Table(name = "message_bot")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MessageBot implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_message")
    private String codeMessage;

    @Column(name = "label_message")
    private String labelMessage;

    @Column(name = "frequence")
    private Integer frequence;

    @ManyToOne
    @JsonIgnoreProperties("messageBots")
    private ActionBot actionBot;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeMessage() {
        return codeMessage;
    }

    public MessageBot codeMessage(String codeMessage) {
        this.codeMessage = codeMessage;
        return this;
    }

    public void setCodeMessage(String codeMessage) {
        this.codeMessage = codeMessage;
    }

    public String getLabelMessage() {
        return labelMessage;
    }

    public MessageBot labelMessage(String labelMessage) {
        this.labelMessage = labelMessage;
        return this;
    }

    public void setLabelMessage(String labelMessage) {
        this.labelMessage = labelMessage;
    }

    public Integer getFrequence() {
        return frequence;
    }

    public MessageBot frequence(Integer frequence) {
        this.frequence = frequence;
        return this;
    }

    public void setFrequence(Integer frequence) {
        this.frequence = frequence;
    }

    public ActionBot getActionBot() {
        return actionBot;
    }

    public MessageBot actionBot(ActionBot actionBot) {
        this.actionBot = actionBot;
        return this;
    }

    public void setActionBot(ActionBot actionBot) {
        this.actionBot = actionBot;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MessageBot messageBot = (MessageBot) o;
        if (messageBot.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), messageBot.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MessageBot{" +
            "id=" + getId() +
            ", codeMessage='" + getCodeMessage() + "'" +
            ", labelMessage='" + getLabelMessage() + "'" +
            ", frequence=" + getFrequence() +
            "}";
    }
}
