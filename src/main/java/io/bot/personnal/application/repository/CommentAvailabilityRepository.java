package io.bot.personnal.application.repository;

import io.bot.personnal.application.domain.CommentAvailability;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CommentAvailability entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentAvailabilityRepository extends JpaRepository<CommentAvailability, Long> {

}
