package com.cvsnewsletter.services.implementation;

import com.cvsnewsletter.dtos.request.KudosRequest;
import com.cvsnewsletter.dtos.response.ApprovedKudosResponse;
import com.cvsnewsletter.dtos.response.KudosResponse;
import com.cvsnewsletter.entities.Kudos;
import com.cvsnewsletter.entities.Member;
import com.cvsnewsletter.entities.enums.DesignationType;
import com.cvsnewsletter.exception.BadRequestException;
import com.cvsnewsletter.repositories.KudosRepository;
import com.cvsnewsletter.repositories.MemberRepository;
import com.cvsnewsletter.services.KudosService;
import com.cvsnewsletter.utility.CvsUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KudosServiceImpl implements KudosService {

    private final KudosRepository repository;
    private final MemberRepository memberRepository;

    @Override
    public String giveKudos(KudosRequest request) {

        DesignationType designation;
        try {
            designation = DesignationType.valueOf(request.getDesignation());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid designation. Allowed values are: Peer, Client");
        }

        Member sender = memberRepository.findByOhrId(request.getOhrId())
                .orElseThrow(() -> new BadRequestException("Sender not found with OHR: " + request.getOhrId()));
        String senderName = sender.getFirstName() + " " + sender.getLastName();

        Member recipient = memberRepository.findByOhrId(request.getRecipientOhrId())
                .orElseThrow(() -> new BadRequestException("Recipient not found with OHR: " + request.getRecipientOhrId()));
        String recipientName = recipient.getFirstName() + " " + recipient.getLastName();

        Kudos kudos = Kudos.builder()
                .ohrId(sender.getOhrId())
                .name(senderName)
                .designation(designation)
                .recipientOhrId(recipient.getOhrId())
                .recipientName(recipientName)
                .message(request.getMessage())
                .date(LocalDate.now())
                .isApproved(false)
                .build();

        repository.save(kudos);
        return "Kudos submitted successfully!";
    }

    @Override
    public List<KudosResponse> getUnapprovedKudos() {
        return repository.findByIsApprovedFalse().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public String approveKudos(Integer id) {
        Kudos kudos = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Kudos not found with id: " + id));
        if (Boolean.TRUE.equals(kudos.getIsApproved())) {
            return "Kudos already approved.";
        }
        kudos.setIsApproved(true);
        repository.save(kudos);
        return "Kudos approved successfully.";
    }

    @Override
    public List<ApprovedKudosResponse> getApprovedKudosWithImages() {
        return repository.findByIsApprovedTrueOrderByDateDesc().stream()
                .limit(10)
                .map(kudos -> {
                    Member recipient = memberRepository.findByOhrId(kudos.getRecipientOhrId())
                            .orElse(null);

                    return ApprovedKudosResponse.builder()
                            .id(kudos.getId())
                            .ohrId(kudos.getOhrId())
                            .name(kudos.getName())
                            .designation(kudos.getDesignation().name())
                            .recipientOhrId(kudos.getRecipientOhrId())
                            .recipientName(kudos.getRecipientName())
                            .message(kudos.getMessage())
                            .date(CvsUtility.formatDate(kudos.getDate(), "dd-MM-yyyy"))
                            .imageName(recipient != null ? recipient.getImageName() : "")
                            .imageType(recipient != null ? recipient.getImageType() : "")
                            .imageData(recipient != null ? recipient.getImageData() : null)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public String deleteKudos(Integer id) {
        Kudos kudos = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Kudos not found with id: " + id));
        repository.delete(kudos);
        return "Kudos deleted successfully.";
    }

    private KudosResponse mapToResponse(Kudos kudos) {
        return KudosResponse.builder()
                .id(kudos.getId())
                .ohrId(kudos.getOhrId())
                .name(kudos.getName())
                .designation(kudos.getDesignation().name())
                .recipientOhrId(kudos.getRecipientOhrId())
                .recipientName(kudos.getRecipientName())
                .message(kudos.getMessage())
                .date(CvsUtility.formatDate(kudos.getDate(), "dd-MM-yyyy"))
                .isApproved(kudos.getIsApproved())
                .build();
    }

}
