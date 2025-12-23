package com.cvsnewsletter.repositories;

import com.cvsnewsletter.dtos.MemberHierarchy;
import com.cvsnewsletter.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    Optional<Member> findByOhrId(String ohrId);

    Optional<Member> findByOhrIdAndEmergencyPhoneNumber(String ohrId, String emergencyPhoneNumber);

    Boolean existsByOhrIdAndIsRegistrationDoneTrue(String ohrId);

    @Query("SELECT m FROM Member m WHERE m.baseLocation = :location")
    List<Member> findByBaseLocation(@Param("location") String location);

    List<Member> findByIsRegistrationDoneTrue();

    @Query("SELECT new com.cvsnewsletter.dtos.MemberHierarchy(" +
            "m.ohrId, CONCAT(m.firstName, ' ', m.lastName), m.reportingManagerOhrId) " +
            "FROM Member m WHERE m.ohrId = :ohrId")
    Optional<MemberHierarchy> findHierarchyByOhrId(@Param("ohrId") String ohrId);

}
