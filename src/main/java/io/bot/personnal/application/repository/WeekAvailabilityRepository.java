package io.bot.personnal.application.repository;

import io.bot.personnal.application.domain.WeekAvailability;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WeekAvailability entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WeekAvailabilityRepository extends JpaRepository<WeekAvailability, Long> {

}
