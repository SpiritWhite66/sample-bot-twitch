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
 * A WeekAvailability.
 */
@Entity
@Table(name = "week_availability")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WeekAvailability implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "weekAvailability")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CommentAvailability> commentAvailabilities = new HashSet<>();
    @OneToMany(mappedBy = "weekAvailability")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Availability> availabilities = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("weekAvailabilities")
    private AppUser appUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<CommentAvailability> getCommentAvailabilities() {
        return commentAvailabilities;
    }

    public WeekAvailability commentAvailabilities(Set<CommentAvailability> commentAvailabilities) {
        this.commentAvailabilities = commentAvailabilities;
        return this;
    }

    public WeekAvailability addCommentAvailability(CommentAvailability commentAvailability) {
        this.commentAvailabilities.add(commentAvailability);
        commentAvailability.setWeekAvailability(this);
        return this;
    }

    public WeekAvailability removeCommentAvailability(CommentAvailability commentAvailability) {
        this.commentAvailabilities.remove(commentAvailability);
        commentAvailability.setWeekAvailability(null);
        return this;
    }

    public void setCommentAvailabilities(Set<CommentAvailability> commentAvailabilities) {
        this.commentAvailabilities = commentAvailabilities;
    }

    public Set<Availability> getAvailabilities() {
        return availabilities;
    }

    public WeekAvailability availabilities(Set<Availability> availabilities) {
        this.availabilities = availabilities;
        return this;
    }

    public WeekAvailability addAvailability(Availability availability) {
        this.availabilities.add(availability);
        availability.setWeekAvailability(this);
        return this;
    }

    public WeekAvailability removeAvailability(Availability availability) {
        this.availabilities.remove(availability);
        availability.setWeekAvailability(null);
        return this;
    }

    public void setAvailabilities(Set<Availability> availabilities) {
        this.availabilities = availabilities;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public WeekAvailability appUser(AppUser appUser) {
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
        WeekAvailability weekAvailability = (WeekAvailability) o;
        if (weekAvailability.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), weekAvailability.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WeekAvailability{" +
            "id=" + getId() +
            "}";
    }
}
