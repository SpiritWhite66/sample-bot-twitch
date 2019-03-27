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

import io.bot.personnal.application.domain.enumeration.Role;

/**
 * A ActionBot.
 */
@Entity
@Table(name = "action_bot")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ActionBot implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "command")
    private String command;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_role")
    private Role role;

    @OneToMany(mappedBy = "actionBot")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MessageBot> messageBots = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("actionBots")
    private Bot bot;

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

    public ActionBot name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public ActionBot command(String command) {
        this.command = command;
        return this;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Role getRole() {
        return role;
    }

    public ActionBot role(Role role) {
        this.role = role;
        return this;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<MessageBot> getMessageBots() {
        return messageBots;
    }

    public ActionBot messageBots(Set<MessageBot> messageBots) {
        this.messageBots = messageBots;
        return this;
    }

    public ActionBot addMessageBot(MessageBot messageBot) {
        this.messageBots.add(messageBot);
        messageBot.setActionBot(this);
        return this;
    }

    public ActionBot removeMessageBot(MessageBot messageBot) {
        this.messageBots.remove(messageBot);
        messageBot.setActionBot(null);
        return this;
    }

    public void setMessageBots(Set<MessageBot> messageBots) {
        this.messageBots = messageBots;
    }

    public Bot getBot() {
        return bot;
    }

    public ActionBot bot(Bot bot) {
        this.bot = bot;
        return this;
    }

    public void setBot(Bot bot) {
        this.bot = bot;
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
        ActionBot actionBot = (ActionBot) o;
        if (actionBot.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), actionBot.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActionBot{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", command='" + getCommand() + "'" +
            ", role='" + getRole() + "'" +
            "}";
    }
}
