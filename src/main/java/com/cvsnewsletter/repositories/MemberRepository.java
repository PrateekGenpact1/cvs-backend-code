package com.cvsnewsletter.repositories;

import com.cvsnewsletter.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    Optional<Member> findByOhrId(String ohrId);

    Boolean existsByOhrIdAndIsRegistrationDoneTrue(String ohrId);

    @Query("SELECT m FROM Member m WHERE m.baseLocation = :location")
    List<Member> findByBaseLocation(@Param("location") String location);

}
