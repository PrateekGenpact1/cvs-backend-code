package com.cvsnewsletter.entities;

import com.cvsnewsletter.entities.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "member",
        indexes = {
                @Index(name = "idx_member_ohrid", columnList = "ohrId")
        }
)
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    @Column(length = 9, unique = true, nullable = false)
    private String ohrId;

    @Column(length = 100)
    private String applicationArea;

    @Column(length = 50)
    private String tower;

    @Column(length = 150)
    private String reportingManager;

    @Column(length = 9)
    private String reportingManagerOhrId;

    @Column(length = 150)
    private String genpactOnsiteSpoc;

    private String baseLocation;

    private String primarySkill;

    private String currentWorkingSkills;

    private String designationBand;

    private String cvsLead;

    private String clientManager;

    private String zid;

    private String overallExperience;

    private String cvsExperience;

    private String genpactExperience;

    private String technicalExpertise;

    private String contactNumber;

    private String genpactMailId;

    private String ssn;

    private String cvsEmpId;

    private String cvsMailId;

    private String highestDegree;

    private String birthday;

    private String anniversary;

    private String currentAddress;

    private String emergencyContactName;

    private String emergencyPhoneNumber;

    private String password;

    private Boolean isRegistrationDone = false;

    private Boolean isPasswordSet = false;

    private String imageName;

    private String imageType;

    @Lob
    private byte[] imageData;

    private String seatNumber;

    @Column(nullable = false)
    private int incorrectPasswordCount = 0;

    @Column
    private LocalDateTime lastIncorrectPasswordTimestamp;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member")
    private List<Token> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getPrefixedValue()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return ohrId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
