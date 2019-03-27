package io.bot.personnal.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A OverWriteCalendar.
 */
@Entity
@Table(name = "over_write_calendar")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OverWriteCalendar implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_date")
    private LocalDate date;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    @JsonIgnoreProperties("overWriteCalendars")
    private DefaultCalendar defaultCalendar;

    @ManyToOne
    @JsonIgnoreProperties("overWriteCalendars")
    private AppUser appUser;

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

    public OverWriteCalendar date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean isStatus() {
        return status;
    }

    public OverWriteCalendar status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public DefaultCalendar getDefaultCalendar() {
        return defaultCalendar;
    }

    public OverWriteCalendar defaultCalendar(DefaultCalendar defaultCalendar) {
        this.defaultCalendar = defaultCalendar;
        return this;
    }

    public void setDefaultCalendar(DefaultCalendar defaultCalendar) {
        this.defaultCalendar = defaultCalendar;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public OverWriteCalendar appUser(AppUser appUser) {
        this.appUser = appUser;
        return this;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
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
        OverWriteCalendar overWriteCalendar = (OverWriteCalendar) o;
        if (overWriteCalendar.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), overWriteCalendar.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OverWriteCalendar{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", status='" + isStatus() + "'" +
            "}";
    }
}
