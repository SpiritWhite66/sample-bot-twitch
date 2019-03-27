package io.bot.personnal.application.repository;

import io.bot.personnal.application.domain.OverWriteCalendar;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OverWriteCalendar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OverWriteCalendarRepository extends JpaRepository<OverWriteCalendar, Long> {

}
