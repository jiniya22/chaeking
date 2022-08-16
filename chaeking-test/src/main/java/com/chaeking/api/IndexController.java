package com.chaeking.api;

import com.chaeking.api.config.DatasourceConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class IndexController {

    private final DatasourceConfig datasourceConfig;

    @GetMapping("")
    public String index() {
        return datasourceConfig.toString();
    }
}
