package aminurdev.com.backend.service.impl;

import aminurdev.com.backend.domain.entity.TagEntity;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.repository.TagRepository;
import aminurdev.com.backend.domain.request.TagRequest;
import aminurdev.com.backend.domain.response.pagination.Links;
import aminurdev.com.backend.domain.response.pagination.Meta;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public PaginationResponse<TagEntity> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        Page<TagEntity> tagPage = tagRepository.findAll(pageable);

        List<TagEntity> tags = tagPage.getContent();

        PaginationResponse<TagEntity> response = new PaginationResponse<>();

        response.setData(tags);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All tag fetch successful");

        Meta meta = new Meta();

        meta.setCurrentPage(tagPage.getNumber() + 1);
        meta.setFrom(tagPage.getNumber() * tagPage.getSize() + 1);
        meta.setLastPage(tagPage.getTotalPages());
        meta.setPath("/tag");
        meta.setPerPage(tagPage.getSize());
        meta.setTo((int) tagPage.getTotalElements());
        meta.setTotal((int) tagPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();

        links.setFirst("/tag?page=1");
        links.setLast("/tag?page=" + tagPage.getTotalPages());
        if (tagPage.hasPrevious()) {
            links.setPrev("/tag?page=" + tagPage.previousPageable().getPageNumber());
        }
        if (tagPage.hasNext()) {
            links.setNext("/tag?page=" + tagPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);
        return response;
    }

    @Override
    public List<TagEntity> getAllTags() {

        return tagRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public TagEntity store(TagRequest request) {

        try {
            TagEntity tag = TagEntity.builder()
                    .name_en(request.getName_en())
                    .name_bn(request.getName_bn())
                    .status(request.getStatus() != null ? request.getStatus() : false)
                    .build();

            return tagRepository.save(tag);

        } catch (Exception exception) {

            throw new GlobalException("Error while storing tag: " + exception.getMessage(), exception);
        }
    }

    @Override
    public TagEntity edit(Integer tagId) {

        TagEntity tag = tagRepository.findById(tagId).orElseThrow(() -> new RuntimeException("Tag id is not found : " + tagId));
        return tag;
    }

    @Override
    public TagEntity update(TagRequest request, Integer tagId) {

        try {
            TagEntity tag = tagRepository.findById(tagId).orElseThrow(() -> new RuntimeException("Tag id is not found : " + tagId));

            if (request.getName_en() != null) {
                tag.setName_en(request.getName_en());
            }

            if (request.getName_bn() != null) {
                tag.setName_bn(request.getName_bn());
            }

            if (request.getStatus() != null) {
                tag.setStatus(request.getStatus());
            }

            return tagRepository.save(tag);

        } catch (Exception exception) {

            throw new GlobalException("Error while updating tag : " + exception.getMessage(), exception);
        }
    }

    @Override
    public void destroy(Integer tagId) {

        try {
            TagEntity tag = tagRepository.findById(tagId).orElseThrow(() -> new RuntimeException("Tag id is not found : " + tagId));

            tagRepository.delete(tag);

        } catch (Exception exception) {
            throw new GlobalException("Error while tag delete: " + exception.getMessage(), exception);
        }
    }
}