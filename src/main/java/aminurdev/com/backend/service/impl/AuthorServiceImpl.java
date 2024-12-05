package aminurdev.com.backend.service.impl;

import aminurdev.com.backend.domain.entity.Author;
import aminurdev.com.backend.domain.entity.City;
import aminurdev.com.backend.domain.entity.User;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.repository.AuthorRepository;
import aminurdev.com.backend.domain.request.AuthorRequest;
import aminurdev.com.backend.domain.response.pagination.Links;
import aminurdev.com.backend.domain.response.pagination.Meta;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public PaginationResponse<Author> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        Page<Author> authorPage = authorRepository.findAll(pageable);
        List<Author> authors = authorPage.getContent();

        PaginationResponse<Author> response = new PaginationResponse<>();
        response.setData(authors);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All authors fetched successfully");

        Meta meta = new Meta();
        meta.setCurrentPage(authorPage.getNumber() + 1);
        meta.setFrom(authorPage.getNumber() * authorPage.getSize() + 1);
        meta.setLastPage(authorPage.getTotalPages());
        meta.setPath("/author");
        meta.setPerPage(authorPage.getSize());
        meta.setTo((int) authorPage.getTotalElements());
        meta.setTotal((int) authorPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();
        links.setFirst("/author?page=1");
        links.setLast("/author?page=" + authorPage.getTotalPages());
        if (authorPage.hasPrevious()) {
            links.setPrev("/author?page=" + authorPage.previousPageable().getPageNumber());
        }
        if (authorPage.hasNext()) {
            links.setNext("/author?page=" + authorPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);
        return response;
    }

    @Override
    public List<Author> getAllAuthors() {

        return authorRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

    }

    @Override
    public Author store(AuthorRequest request) {

        try {

            Author author = new Author();

            author.setName_en(request.getName_en());
            author.setName_bn(request.getName_bn());
            author.setEmail(request.getEmail());
            author.setPhone_en(request.getPhone_en());
            author.setPhone_bn(request.getPhone_bn());
            author.setAddress_en(request.getAddress_en());
            author.setAddress_bn(request.getAddress_bn());
            author.setDob(request.getDob());
            author.setGender_en(request.getGender_en());
            author.setGender_bn(request.getGender_bn());
            author.setBiography_en(request.getBiography_en());
            author.setBiography_bn(request.getBiography_bn());

            // Retrieve the authenticated user's ID from the SecurityContext
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();
                Integer userId = user.getId();  // Get the authenticated user ID

                // Set the 'created_by' field with the authenticated user's ID
                author.setCreated_by(userId);
            } else {
                throw new GlobalException("Unable to retrieve authenticated user");
            }

            return authorRepository.save(author);

        }catch (Exception exception){

            throw new GlobalException("Error while storing author: " + exception.getMessage(), exception);
        }
    }

    @Override
    public Author edit(Integer authorId) {

        return authorRepository.findById(authorId).orElseThrow(() -> new RuntimeException("Author is not found " + authorId));
    }

    @Override
    public Author update(Integer authorId, AuthorRequest request) {

        try {

            Author author = authorRepository.findById(authorId).orElseThrow(() -> new RuntimeException("Author is not found " + authorId));

            author.setName_en(request.getName_en());
            author.setName_bn(request.getName_bn());
            author.setEmail(request.getEmail());
            author.setPhone_en(request.getPhone_en());
            author.setPhone_bn(request.getPhone_bn());
            author.setAddress_en(request.getAddress_en());
            author.setAddress_bn(request.getAddress_bn());
            author.setDob(request.getDob());
            author.setGender_en(request.getGender_en());
            author.setGender_bn(request.getGender_bn());
            author.setBiography_en(request.getBiography_en());
            author.setBiography_bn(request.getBiography_bn());

            // Retrieve the authenticated user's ID from the SecurityContext
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();
                Integer userId = user.getId();  // Get the authenticated user ID

                // Set the 'created_by' field with the authenticated user's ID
                author.setCreated_by(userId);
            } else {
                throw new GlobalException("Unable to retrieve authenticated user");
            }

            return authorRepository.save(author);

        }catch (Exception exception){

            throw new GlobalException("Error while update author: " + exception.getMessage(), exception);
        }
    }

    @Override
    public void destroy(Integer authorId) {

        authorRepository.deleteById(authorId);

    }
}
