package io.bot.personnal.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A StreamCalendar.
 */
@Entity
@Table(name = "stream_calendar")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StreamCalendar implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_activate")
    private Boolean activate;

    @OneToOne
    @JoinColumn(unique = true)
    private ProgramTwitch programTwitch;

    @OneToMany(mappedBy = "streamCalendar")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DefaultCalendar> defaultCalendars = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isActivate() {
        return activate;
    }

    public StreamCalendar activate(Boolean activate) {
        this.activate = activate;
        return this;
    }

    public void setActivate(Boolean activate) {
        this.activate = activate;
    }

    public ProgramTwitch getProgramTwitch() {
        return programTwitch;
    }

    public StreamCalendar programTwitch(ProgramTwitch programTwitch) {
        this.programTwitch = programTwitch;
        return this;
    }

    public void setProgramTwitch(ProgramTwitch programTwitch) {
        this.programTwitch = programTwitch;
    }

    public Set<DefaultCalendar> getDefaultCalendars() {
        return defaultCalendars;
    }

    public StreamCalendar defaultCalendars(Set<DefaultCalendar> defaultCalendars) {
        this.defaultCalendars = defaultCalendars;
        return this;
    }

    public StreamCalendar addDefaultCalendar(DefaultCalendar defaultCalendar) {
        this.defaultCalendars.add(defaultCalendar);
        defaultCalendar.setStreamCalendar(this);
        return this;
    }

    public StreamCalendar removeDefaultCalendar(DefaultCalendar defaultCalendar) {
        this.defaultCalendars.remove(defaultCalendar);
        defaultCalendar.setStreamCalendar(null);
        return this;
    }

    public void setDefaultCalendars(Set<DefaultCalendar> defaultCalendars) {
        this.defaultCalendars = defaultCalendars;
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
        StreamCalendar streamCalendar = (StreamCalendar) o;
        if (streamCalendar.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), streamCalendar.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StreamCalendar{" +
            "id=" + getId() +
            ", activate='" + isActivate() + "'" +
            "}";
    }
}
