package com.chaeking.api.service;

import com.chaeking.api.domain.value.data4library.Data4LibraryLibraryValue;
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
}
