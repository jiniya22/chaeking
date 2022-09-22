package com.chaeking.api.service;

import com.chaeking.api.domain.value.data4library.Data4LibraryLibraryValue;
import com.chaeking.api.domain.value.data4library.Data4LibraryLoanItemValue;
import com.chaeking.api.domain.value.data4library.Data4LibraryRecommandValue;
import com.chaeking.api.feignclient.Data4libraryApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LibraryService {

    private final Data4libraryApiClient data4libraryApiClient;

    public List<Data4LibraryLibraryValue.Res.Response.Lib> mergeLibrary(String region) {
        Data4LibraryLibraryValue.Req req = Data4LibraryLibraryValue.Req.builder()
                .pageNo(1).pageSize(10).region(region).build();
        ResponseEntity<Data4LibraryLibraryValue.Res> entity = data4libraryApiClient.searchLibraries(req);
        return entity.getBody().getResponse() != null ? entity.getBody().getResponse().getLibs() : new ArrayList<>();
    }

    public List<Data4LibraryRecommandValue.Res.Response.Doc> recommends() {
        String isbn13 = "9791191114225;9788954645614"; // FIXME
        Data4LibraryRecommandValue.Req req = Data4LibraryRecommandValue.Req.builder()
                .isbn13(isbn13).build();
        ResponseEntity<Data4LibraryRecommandValue.Res> entity = data4libraryApiClient.searchRecommends(req);
        return entity.getBody().getResponse() != null ? entity.getBody().getResponse().getDocs() : new ArrayList<>();
    }
}
