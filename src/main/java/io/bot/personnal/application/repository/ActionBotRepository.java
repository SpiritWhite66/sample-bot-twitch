package io.bot.personnal.application.repository;

import io.bot.personnal.application.domain.ActionBot;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ActionBot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActionBotRepository extends JpaRepository<ActionBot, Long> {

}
