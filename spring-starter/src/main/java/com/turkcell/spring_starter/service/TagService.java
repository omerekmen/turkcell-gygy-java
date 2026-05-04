package com.turkcell.spring_starter.service;

import java.util.List;
import java.util.UUID;
import java.util.Set;
import java.util.HashSet;

import com.turkcell.spring_starter.dto.CreateTagRequest;
import com.turkcell.spring_starter.dto.CreatedTagResponse;
import com.turkcell.spring_starter.dto.ListTagResponse;
import com.turkcell.spring_starter.dto.UpdateTagRequest;
import com.turkcell.spring_starter.entity.Tag;

public interface TagService {
    CreatedTagResponse create(CreateTagRequest request);
    List<ListTagResponse> getAll();
    CreatedTagResponse getById(UUID id);
    Set<Tag> getByIds(Set<UUID> ids);
    CreatedTagResponse update(UUID id, UpdateTagRequest request);
    void delete(UUID id);
}
