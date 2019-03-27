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
 * A ProgramTwitch.
 */
@Entity
@Table(name = "program_twitch")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProgramTwitch implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "jhi_link")
    private String link;

    @OneToMany(mappedBy = "programTwitch")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Bot> bots = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("programTwitches")
    private RefCategoriesStream refCategoriesStream;

    @OneToOne(mappedBy = "programTwitch")
    @JsonIgnore
    private StreamCalendar streamCalendar;

    @ManyToMany(mappedBy = "programTwitches")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<AppUser> appUsers = new HashSet<>();

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

    public ProgramTwitch name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public ProgramTwitch link(String link) {
        this.link = link;
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Set<Bot> getBots() {
        return bots;
    }

    public ProgramTwitch bots(Set<Bot> bots) {
        this.bots = bots;
        return this;
    }

    public ProgramTwitch addBot(Bot bot) {
        this.bots.add(bot);
        bot.setProgramTwitch(this);
        return this;
    }

    public ProgramTwitch removeBot(Bot bot) {
        this.bots.remove(bot);
        bot.setProgramTwitch(null);
        return this;
    }

    public void setBots(Set<Bot> bots) {
        this.bots = bots;
    }

    public RefCategoriesStream getRefCategoriesStream() {
        return refCategoriesStream;
    }

    public ProgramTwitch refCategoriesStream(RefCategoriesStream refCategoriesStream) {
        this.refCategoriesStream = refCategoriesStream;
        return this;
    }

    public void setRefCategoriesStream(RefCategoriesStream refCategoriesStream) {
        this.refCategoriesStream = refCategoriesStream;
    }

    public StreamCalendar getStreamCalendar() {
        return streamCalendar;
    }

    public ProgramTwitch streamCalendar(StreamCalendar streamCalendar) {
        this.streamCalendar = streamCalendar;
        return this;
    }

    public void setStreamCalendar(StreamCalendar streamCalendar) {
        this.streamCalendar = streamCalendar;
    }

    public Set<AppUser> getAppUsers() {
        return appUsers;
    }

    public ProgramTwitch appUsers(Set<AppUser> appUsers) {
        this.appUsers = appUsers;
        return this;
    }

    public ProgramTwitch addAppUser(AppUser appUser) {
        this.appUsers.add(appUser);
        appUser.getProgramTwitches().add(this);
        return this;
    }

    public ProgramTwitch removeAppUser(AppUser appUser) {
        this.appUsers.remove(appUser);
        appUser.getProgramTwitches().remove(this);
        return this;
    }

    public void setAppUsers(Set<AppUser> appUsers) {
        this.appUsers = appUsers;
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
        ProgramTwitch programTwitch = (ProgramTwitch) o;
        if (programTwitch.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), programTwitch.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProgramTwitch{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", link='" + getLink() + "'" +
            "}";
    }
}
