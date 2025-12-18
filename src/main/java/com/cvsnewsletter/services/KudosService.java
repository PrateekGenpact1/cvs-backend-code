package com.cvsnewsletter.services;

import com.cvsnewsletter.dtos.request.KudosRequest;
import com.cvsnewsletter.dtos.response.ApprovedKudosResponse;
import com.cvsnewsletter.dtos.response.KudosResponse;

import java.util.List;

public interface KudosService {
    String giveKudos(KudosRequest request);

    List<KudosResponse> getUnapprovedKudos();

    String approveKudos(Integer id);

    List<ApprovedKudosResponse> getApprovedKudosWithImages();

    String deleteKudos(Integer id);
}
