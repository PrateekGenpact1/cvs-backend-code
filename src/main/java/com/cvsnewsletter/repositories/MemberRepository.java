package com.cvsnewsletter.repositories;

import com.cvsnewsletter.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    Optional<Member> findByOhrId(String ohrId);

    Boolean existsByOhrIdAndIsRegistrationDoneTrue(String ohrId);

}
