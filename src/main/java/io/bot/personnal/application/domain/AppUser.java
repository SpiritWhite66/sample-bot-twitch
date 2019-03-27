package io.bot.personnal.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import io.bot.personnal.application.domain.enumeration.Role;

/**
 * A AppUser.
 */
@Entity
@Table(name = "app_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_role")
    private Role role;

    @OneToMany(mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WeekAvailability> weekAvailabilities = new HashSet<>();
    @OneToMany(mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OverWriteCalendar> overWriteCalendars = new HashSet<>();
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "app_user_program_twitch",
               joinColumns = @JoinColumn(name = "app_user_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "program_twitch_id", referencedColumnName = "id"))
    private Set<ProgramTwitch> programTwitches = new HashSet<>();

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

    public AppUser name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public AppUser email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public AppUser phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public AppUser role(Role role) {
        this.role = role;
        return this;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<WeekAvailability> getWeekAvailabilities() {
        return weekAvailabilities;
    }

    public AppUser weekAvailabilities(Set<WeekAvailability> weekAvailabilities) {
        this.weekAvailabilities = weekAvailabilities;
        return this;
    }

    public AppUser addWeekAvailability(WeekAvailability weekAvailability) {
        this.weekAvailabilities.add(weekAvailability);
        weekAvailability.setAppUser(this);
        return this;
    }

    public AppUser removeWeekAvailability(WeekAvailability weekAvailability) {
        this.weekAvailabilities.remove(weekAvailability);
        weekAvailability.setAppUser(null);
        return this;
    }

    public void setWeekAvailabilities(Set<WeekAvailability> weekAvailabilities) {
        this.weekAvailabilities = weekAvailabilities;
    }

    public Set<OverWriteCalendar> getOverWriteCalendars() {
        return overWriteCalendars;
    }

    public AppUser overWriteCalendars(Set<OverWriteCalendar> overWriteCalendars) {
        this.overWriteCalendars = overWriteCalendars;
        return this;
    }

    public AppUser addOverWriteCalendar(OverWriteCalendar overWriteCalendar) {
        this.overWriteCalendars.add(overWriteCalendar);
        overWriteCalendar.setAppUser(this);
        return this;
    }

    public AppUser removeOverWriteCalendar(OverWriteCalendar overWriteCalendar) {
        this.overWriteCalendars.remove(overWriteCalendar);
        overWriteCalendar.setAppUser(null);
        return this;
    }

    public void setOverWriteCalendars(Set<OverWriteCalendar> overWriteCalendars) {
        this.overWriteCalendars = overWriteCalendars;
    }

    public Set<ProgramTwitch> getProgramTwitches() {
        return programTwitches;
    }

    public AppUser programTwitches(Set<ProgramTwitch> programTwitches) {
        this.programTwitches = programTwitches;
        return this;
    }

    public AppUser addProgramTwitch(ProgramTwitch programTwitch) {
        this.programTwitches.add(programTwitch);
        programTwitch.getAppUsers().add(this);
        return this;
    }

    public AppUser removeProgramTwitch(ProgramTwitch programTwitch) {
        this.programTwitches.remove(programTwitch);
        programTwitch.getAppUsers().remove(this);
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
        AppUser appUser = (AppUser) o;
        if (appUser.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), appUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AppUser{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", role='" + getRole() + "'" +
            "}";
    }
}
