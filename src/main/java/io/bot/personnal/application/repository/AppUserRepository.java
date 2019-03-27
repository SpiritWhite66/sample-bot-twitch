package io.bot.personnal.application.repository;

import io.bot.personnal.application.domain.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the AppUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    @Query(value = "select distinct app_user from AppUser app_user left join fetch app_user.programTwitches",
        countQuery = "select count(distinct app_user) from AppUser app_user")
    Page<AppUser> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct app_user from AppUser app_user left join fetch app_user.programTwitches")
    List<AppUser> findAllWithEagerRelationships();

    @Query("select app_user from AppUser app_user left join fetch app_user.programTwitches where app_user.id =:id")
    Optional<AppUser> findOneWithEagerRelationships(@Param("id") Long id);

}
