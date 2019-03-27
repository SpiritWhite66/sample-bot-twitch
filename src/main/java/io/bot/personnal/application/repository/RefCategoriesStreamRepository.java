package io.bot.personnal.application.repository;

import io.bot.personnal.application.domain.RefCategoriesStream;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RefCategoriesStream entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RefCategoriesStreamRepository extends JpaRepository<RefCategoriesStream, Long> {

}
