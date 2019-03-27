package io.bot.personnal.application.repository;

import io.bot.personnal.application.domain.StreamCalendar;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StreamCalendar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StreamCalendarRepository extends JpaRepository<StreamCalendar, Long> {

}
