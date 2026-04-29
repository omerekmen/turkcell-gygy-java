package com.turkcell.spring_starter.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.spring_starter.dto.ApiResult;
import com.turkcell.spring_starter.dto.CreateTagRequest;
import com.turkcell.spring_starter.dto.CreatedTagResponse;
import com.turkcell.spring_starter.dto.ListTagResponse;
import com.turkcell.spring_starter.dto.UpdateTagRequest;
import com.turkcell.spring_starter.service.TagService;

@RestController
@RequestMapping("/api/v{version:1}/tags")
public class TagsController {
    private final TagService tagService;

    public TagsController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ApiResult<CreatedTagResponse> create(@RequestBody CreateTagRequest request) {
        return ApiResult.success(HttpStatus.CREATED.value(), "Tag created successfully", tagService.create(request));
    }

    @GetMapping
    public ApiResult<List<ListTagResponse>> getAll() {
        return ApiResult.success("Tags retrieved successfully", tagService.getAll());
    }

    @GetMapping("/{id}")
    public ApiResult<CreatedTagResponse> getById(@PathVariable UUID id) {
        return ApiResult.success("Tag retrieved successfully", tagService.getById(id));
    }

    @PutMapping("/{id}")
    public ApiResult<CreatedTagResponse> update(@PathVariable UUID id, @RequestBody UpdateTagRequest request) {
        return ApiResult.success("Tag updated successfully", tagService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable UUID id) {
        tagService.delete(id);
        return ApiResult.success(HttpStatus.NO_CONTENT.value(), "Tag deleted successfully");
    }
}
