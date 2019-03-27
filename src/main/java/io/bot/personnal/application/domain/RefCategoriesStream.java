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
 * A RefCategoriesStream.
 */
@Entity
@Table(name = "ref_categories_stream")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RefCategoriesStream implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_category")
    private String codeCategory;

    @Column(name = "label_category")
    private String labelCategory;

    @OneToMany(mappedBy = "refCategoriesStream")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProgramTwitch> programTwitches = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeCategory() {
        return codeCategory;
    }

    public RefCategoriesStream codeCategory(String codeCategory) {
        this.codeCategory = codeCategory;
        return this;
    }

    public void setCodeCategory(String codeCategory) {
        this.codeCategory = codeCategory;
    }

    public String getLabelCategory() {
        return labelCategory;
    }

    public RefCategoriesStream labelCategory(String labelCategory) {
        this.labelCategory = labelCategory;
        return this;
    }

    public void setLabelCategory(String labelCategory) {
        this.labelCategory = labelCategory;
    }

    public Set<ProgramTwitch> getProgramTwitches() {
        return programTwitches;
    }

    public RefCategoriesStream programTwitches(Set<ProgramTwitch> programTwitches) {
        this.programTwitches = programTwitches;
        return this;
    }

    public RefCategoriesStream addProgramTwitch(ProgramTwitch programTwitch) {
        this.programTwitches.add(programTwitch);
        programTwitch.setRefCategoriesStream(this);
        return this;
    }

    public RefCategoriesStream removeProgramTwitch(ProgramTwitch programTwitch) {
        this.programTwitches.remove(programTwitch);
        programTwitch.setRefCategoriesStream(null);
        return this;
    }

    public void setProgramTwitches(Set<ProgramTwitch> programTwitches) {
        this.programTwitches = programTwitches;
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
        RefCategoriesStream refCategoriesStream = (RefCategoriesStream) o;
        if (refCategoriesStream.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), refCategoriesStream.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RefCategoriesStream{" +
            "id=" + getId() +
            ", codeCategory='" + getCodeCategory() + "'" +
            ", labelCategory='" + getLabelCategory() + "'" +
            "}";
    }
}
