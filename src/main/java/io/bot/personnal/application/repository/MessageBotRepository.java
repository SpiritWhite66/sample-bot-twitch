package io.bot.personnal.application.repository;

import io.bot.personnal.application.domain.MessageBot;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MessageBot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageBotRepository extends JpaRepository<MessageBot, Long> {

}
