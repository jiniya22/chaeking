package com.chaeking.api.service.data4library;

import com.chaeking.api.domain.entity.Library;
import com.chaeking.api.domain.value.data4library.Data4LibraryHotTrendValue;
import com.chaeking.api.domain.value.data4library.Data4LibraryLibraryValue;
import com.chaeking.api.domain.value.data4library.Data4LibraryLoanItemValue;
import com.chaeking.api.domain.value.data4library.Data4LibraryRecommandValue;
import com.chaeking.api.feignclient.Data4libraryApiClient;
import com.chaeking.api.repository.LibraryRepository;
import com.chaeking.api.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class LibraryService {

    private final Data4libraryApiClient data4libraryApiClient;
    private final LibraryRepository libraryRepository;

    public List<Data4LibraryLibraryValue.Res.Response.Lib> selectLibraries(Data4LibraryLibraryValue.Req req) {
        ResponseEntity<Data4LibraryLibraryValue.Res> entity = data4libraryApiClient.searchLibraries(req);
        return entity.getBody().getResponse() != null ? entity.getBody().getResponse().getLibs() : new ArrayList<>();
    }

    @Transactional
    public int mergeLibrary(String region) {
        int pageNo = 1, pageSize = 300, total = 0;
        while (true) {
            Data4LibraryLibraryValue.Req req = Data4LibraryLibraryValue.Req.builder()
                .pageNo(pageNo).pageSize(pageSize).region(region).build();
            ResponseEntity<Data4LibraryLibraryValue.Res> entity = data4libraryApiClient.searchLibraries(req);
            if(entity.getBody().getResponse() == null || entity.getBody().getResponse().getLibs().isEmpty())
                break;

            entity.getBody().getResponse().getLibs().forEach(lib -> {
                Library library = libraryRepository.findByCode(lib.getLib().getLibCode())
                        .orElse(lib.getLib().convertLibrary(region));
                if(library.getId() != null) return;
                libraryRepository.save(library);
            });

            total += entity.getBody().getResponse().getResultNum();
            if(entity.getBody().getResponse().getPageSize() > entity.getBody().getResponse().getResultNum())    // FIXME
                break;
            pageNo++;
        }

        return total;
    }

    public List<Data4LibraryRecommandValue.Res.Response.Doc> recommends() {
        String isbn13 = "9791191114225;9788954645614"; // FIXME
        Data4LibraryRecommandValue.Req req = Data4LibraryRecommandValue.Req.builder()
                .isbn13(isbn13).build();
        ResponseEntity<Data4LibraryRecommandValue.Res> entity = data4libraryApiClient.searchRecommends(req);
        return entity.getBody().getResponse() != null ? entity.getBody().getResponse().getDocs() : new ArrayList<>();
    }

    public List<Data4LibraryHotTrendValue.Res.Response.Results> hotTrend() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Data4LibraryHotTrendValue.Req req = Data4LibraryHotTrendValue.Req.builder()
                        .searchDt(DateTimeUtils.toString(yesterday)).build();
        ResponseEntity<Data4LibraryHotTrendValue.Res> entity = data4libraryApiClient.hotTrend(req);
        return entity.getBody().getResponse() != null ? entity.getBody().getResponse().getResults() : new ArrayList<>();
    }
}
