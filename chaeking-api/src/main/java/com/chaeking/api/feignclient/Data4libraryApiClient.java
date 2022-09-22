package com.chaeking.api.feignclient;

import com.chaeking.api.domain.value.data4library.Data4LibraryLibraryValue;
import com.chaeking.api.domain.value.data4library.Data4LibraryRecommandValue;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "data4libraryApi", url = "${chaeking.data4library.api-url}")
public interface Data4libraryApiClient {

    @GetMapping("/libSrch")
    ResponseEntity<Data4LibraryLibraryValue.Res> searchLibraries(@SpringQueryMap Data4LibraryLibraryValue.Req library);

    @GetMapping("/recommandList")
    ResponseEntity<Data4LibraryRecommandValue.Res> searchRecommends(@SpringQueryMap Data4LibraryRecommandValue.Req recommend);
}
