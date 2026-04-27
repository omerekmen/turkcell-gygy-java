package com.turkcell.spring_starter.service;

import java.util.List;
import java.util.UUID;

import com.turkcell.spring_starter.dto.CreateTagRequest;
import com.turkcell.spring_starter.dto.CreatedTagResponse;
import com.turkcell.spring_starter.dto.ListTagResponse;
import com.turkcell.spring_starter.dto.UpdateTagRequest;

public interface TagService {
    CreatedTagResponse create(CreateTagRequest request);
    List<ListTagResponse> getAll();
    CreatedTagResponse getById(UUID id);
    CreatedTagResponse update(UUID id, UpdateTagRequest request);
    void delete(UUID id);
}
