package io.bot.personnal.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Bot.
 */
@Entity
@Table(name = "bot")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Bot implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "bot")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ActionBot> actionBots = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("bots")
    private ProgramTwitch programTwitch;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Bot name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ActionBot> getActionBots() {
        return actionBots;
    }

    public Bot actionBots(Set<ActionBot> actionBots) {
        this.actionBots = actionBots;
        return this;
    }

    public Bot addActionBot(ActionBot actionBot) {
        this.actionBots.add(actionBot);
        actionBot.setBot(this);
        return this;
    }

    public Bot removeActionBot(ActionBot actionBot) {
        this.actionBots.remove(actionBot);
        actionBot.setBot(null);
        return this;
    }

    public void setActionBots(Set<ActionBot> actionBots) {
        this.actionBots = actionBots;
    }

    public ProgramTwitch getProgramTwitch() {
        return programTwitch;
    }

    public Bot programTwitch(ProgramTwitch programTwitch) {
        this.programTwitch = programTwitch;
        return this;
    }

    public void setProgramTwitch(ProgramTwitch programTwitch) {
        this.programTwitch = programTwitch;
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
        Bot bot = (Bot) o;
        if (bot.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bot.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Bot{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
