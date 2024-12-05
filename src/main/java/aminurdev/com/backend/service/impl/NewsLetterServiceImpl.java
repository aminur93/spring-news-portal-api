package aminurdev.com.backend.service.impl;

import aminurdev.com.backend.domain.entity.NewsLetter;
import aminurdev.com.backend.domain.entity.Permission;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.repository.NewsLetterRepository;
import aminurdev.com.backend.domain.request.NewsLetterRequest;
import aminurdev.com.backend.domain.response.pagination.Links;
import aminurdev.com.backend.domain.response.pagination.Meta;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.NewsLetterService;
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
public class NewsLetterServiceImpl implements NewsLetterService {

    private final NewsLetterRepository newsLetterRepository;

    @Override
    public PaginationResponse<NewsLetter> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction,"updatedAt"));

        Page<NewsLetter> newsLetterPage = newsLetterRepository.findAll(pageable);

        List<NewsLetter> newsLetters = newsLetterPage.getContent();

        PaginationResponse<NewsLetter> response = new PaginationResponse<>();

        response.setData(newsLetters);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All News-Letter fetch successful");

        Meta meta = new Meta();

        meta.setCurrentPage(newsLetterPage.getNumber() + 1);
        meta.setFrom(newsLetterPage.getNumber() * newsLetterPage.getSize() + 1);
        meta.setLastPage(newsLetterPage.getTotalPages());
        meta.setPath("/news-letter");
        meta.setPerPage(newsLetterPage.getSize());
        meta.setTo((int) newsLetterPage.getTotalElements());
        meta.setTotal((int) newsLetterPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();

        links.setFirst("/news-letter?page=1");
        links.setLast("/news-letter?page=" + newsLetterPage.getTotalPages());
        if (newsLetterPage.hasPrevious()) {
            links.setPrev("/news-letter?page=" + newsLetterPage.previousPageable().getPageNumber());
        }
        if (newsLetterPage.hasNext()) {
            links.setNext("/news-letter?page=" + newsLetterPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);

        return response;
    }

    @Override
    public List<NewsLetter> getAllNewsletter() {

        return newsLetterRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public NewsLetter store(NewsLetterRequest request) {

        try {

            NewsLetter newsLetter = new NewsLetter();

            newsLetter.setEmail(request.getEmail());

            newsLetterRepository.save(newsLetter);

            return newsLetter;

        }catch (Exception exception){

            throw new GlobalException("Error while storing news-letter : " + exception.getMessage(), exception);
        }
    }

    @Override
    public void destroy(Integer newsLetterId) {

        newsLetterRepository.deleteById(newsLetterId);
    }
}
