package io.bot.personnal.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A CommentAvailability.
 */
@Entity
@Table(name = "comment_availability")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CommentAvailability implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_comment")
    private String comment;

    @ManyToOne
    @JsonIgnoreProperties("commentAvailabilities")
    private WeekAvailability weekAvailability;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public CommentAvailability comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public WeekAvailability getWeekAvailability() {
        return weekAvailability;
    }

    public CommentAvailability weekAvailability(WeekAvailability weekAvailability) {
        this.weekAvailability = weekAvailability;
        return this;
    }

    public void setWeekAvailability(WeekAvailability weekAvailability) {
        this.weekAvailability = weekAvailability;
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
        CommentAvailability commentAvailability = (CommentAvailability) o;
        if (commentAvailability.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), commentAvailability.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CommentAvailability{" +
            "id=" + getId() +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
