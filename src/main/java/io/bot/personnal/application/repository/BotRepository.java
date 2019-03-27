package io.bot.personnal.application.repository;

import io.bot.personnal.application.domain.Bot;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Bot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BotRepository extends JpaRepository<Bot, Long> {

}
