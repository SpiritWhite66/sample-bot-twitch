package io.bot.personnal.application.repository;

import io.bot.personnal.application.domain.DefaultCalendar;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DefaultCalendar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DefaultCalendarRepository extends JpaRepository<DefaultCalendar, Long> {

}
