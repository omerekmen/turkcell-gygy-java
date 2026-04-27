package com.turkcell.spring_starter.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.turkcell.spring_starter.dto.CreateTagRequest;
import com.turkcell.spring_starter.dto.CreatedTagResponse;
import com.turkcell.spring_starter.dto.ListTagResponse;
import com.turkcell.spring_starter.dto.UpdateTagRequest;
import com.turkcell.spring_starter.entity.Tag;
import com.turkcell.spring_starter.repository.TagRepository;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public CreatedTagResponse create(CreateTagRequest request) {
        String name = validateName(request.getName());
        if (tagRepository.existsByNameIgnoreCase(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tag already exists");
        }
        Tag tag = new Tag();
        tag.setName(name);
        return mapCreated(tagRepository.save(tag));
    }

    public List<ListTagResponse> getAll() {
        return tagRepository.findAll().stream().map(this::mapList).toList();
    }

    public CreatedTagResponse getById(UUID id) {
        Tag tag = findTag(id);
        return mapCreated(tag);
    }

    public CreatedTagResponse update(UUID id, UpdateTagRequest request) {
        Tag tag = findTag(id);
        String name = validateName(request.getName());
        if (!tag.getName().equalsIgnoreCase(name) && tagRepository.existsByNameIgnoreCase(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tag already exists");
        }
        tag.setName(name);
        return mapCreated(tagRepository.save(tag));
    }

    public void delete(UUID id) {
        Tag tag = findTag(id);
        tagRepository.delete(tag);
    }

    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag name is required");
        }
        return name.trim();
    }

    private Tag findTag(UUID id) {
        return tagRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
    }

    private CreatedTagResponse mapCreated(Tag tag) {
        CreatedTagResponse response = new CreatedTagResponse();
        response.setId(tag.getId());
        response.setName(tag.getName());
        return response;
    }

    private ListTagResponse mapList(Tag tag) {
        ListTagResponse response = new ListTagResponse();
        response.setId(tag.getId());
        response.setName(tag.getName());
        return response;
    }
}
