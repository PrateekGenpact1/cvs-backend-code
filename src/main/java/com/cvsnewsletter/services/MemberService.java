package com.cvsnewsletter.services;

import com.cvsnewsletter.dtos.LimitedMemberDetailsDto;
import com.cvsnewsletter.dtos.MemberBasicInfo;
import com.cvsnewsletter.dtos.MemberHierarchy;
import com.cvsnewsletter.dtos.request.ChangePasswordRequest;
import com.cvsnewsletter.dtos.MemberDetailsDto;
import com.cvsnewsletter.dtos.request.PasswordRequest;
import com.cvsnewsletter.dtos.response.MemberLocationResponse;
import com.cvsnewsletter.dtos.response.MemberSummaryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface MemberService {
    LimitedMemberDetailsDto getMemberDetails(String ohrId, String emergencyPhoneNumber);

    String changePassword(ChangePasswordRequest request, Principal connectedUser);

    String saveMemberDetails(MemberDetailsDto memberDetails, MultipartFile image) throws IOException;

    String updateMemberDetails(MemberDetailsDto memberDetailsDto, MultipartFile image) throws IOException;

    String savePassword(PasswordRequest passwordRequest);

    MemberDetailsDto getFullMemberDetails(String ohrId);

    List<MemberLocationResponse> getMembersByLocation(String location);

    List<MemberSummaryResponse> getAllMembersSummary();

    List<MemberHierarchy> getMemberHierarchyFlat(String ohrId);

    List<MemberBasicInfo> getAllOhrIdsWithNames();
}
