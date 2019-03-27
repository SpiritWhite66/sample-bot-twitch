package io.bot.personnal.application.repository;

import io.bot.personnal.application.domain.ProgramTwitch;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProgramTwitch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProgramTwitchRepository extends JpaRepository<ProgramTwitch, Long> {

}
