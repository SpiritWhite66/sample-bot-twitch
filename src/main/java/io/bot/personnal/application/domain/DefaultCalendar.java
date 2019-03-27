package io.bot.personnal.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DefaultCalendar.
 */
@Entity
@Table(name = "default_calendar")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DefaultCalendar implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_date")
    private LocalDate date;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @ManyToOne
    @JsonIgnoreProperties("defaultCalendars")
    private StreamCalendar streamCalendar;

    @OneToMany(mappedBy = "defaultCalendar")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OverWriteCalendar> overWriteCalendars = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public DefaultCalendar date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public DefaultCalendar email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public DefaultCalendar phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public StreamCalendar getStreamCalendar() {
        return streamCalendar;
    }

    public DefaultCalendar streamCalendar(StreamCalendar streamCalendar) {
        this.streamCalendar = streamCalendar;
        return this;
    }

    public void setStreamCalendar(StreamCalendar streamCalendar) {
        this.streamCalendar = streamCalendar;
    }

    public Set<OverWriteCalendar> getOverWriteCalendars() {
        return overWriteCalendars;
    }

    public DefaultCalendar overWriteCalendars(Set<OverWriteCalendar> overWriteCalendars) {
        this.overWriteCalendars = overWriteCalendars;
        return this;
    }

    public DefaultCalendar addOverWriteCalendar(OverWriteCalendar overWriteCalendar) {
        this.overWriteCalendars.add(overWriteCalendar);
        overWriteCalendar.setDefaultCalendar(this);
        return this;
    }

    public DefaultCalendar removeOverWriteCalendar(OverWriteCalendar overWriteCalendar) {
        this.overWriteCalendars.remove(overWriteCalendar);
        overWriteCalendar.setDefaultCalendar(null);
        return this;
    }

    public void setOverWriteCalendars(Set<OverWriteCalendar> overWriteCalendars) {
        this.overWriteCalendars = overWriteCalendars;
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
        DefaultCalendar defaultCalendar = (DefaultCalendar) o;
        if (defaultCalendar.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), defaultCalendar.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DefaultCalendar{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}
