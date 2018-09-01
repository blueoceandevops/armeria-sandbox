package info.matsumana.armeria.controller;

import org.springframework.stereotype.Component;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Param;

@Component
public class HelloController {

    @Get("/hello/:name")
    public HttpResponse hello(@Param String name) {
        return HttpResponse.of("Hello, " + name);
    }
}
