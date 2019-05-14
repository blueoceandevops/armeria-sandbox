package info.matsumana.armeria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linecorp.armeria.server.logging.LoggingService;
import com.linecorp.armeria.server.throttling.ThrottlingHttpService;
import com.linecorp.armeria.server.tracing.HttpTracingService;
import com.linecorp.armeria.spring.AnnotatedServiceRegistrationBean;

import brave.Tracing;
import info.matsumana.armeria.handler.HelloHandler;
import info.matsumana.armeria.handler.RootHandler;
import info.matsumana.armeria.helper.ThrottlingHelper;

@Configuration
public class ArmeriaHttpServiceConfig {

    private final Tracing tracing;
    private final ThrottlingHelper throttlingHelper;

    ArmeriaHttpServiceConfig(ZipkinTracingFactory tracingFactory, ThrottlingHelper throttlingHelper) {
        tracing = tracingFactory.create("frontend");
        this.throttlingHelper = throttlingHelper;
    }

    @Bean
    public AnnotatedServiceRegistrationBean rootHandlerRegistrationBean(RootHandler handler) {
        return new AnnotatedServiceRegistrationBean()
                .setServiceName("rootService")
                .setService(handler)
                .setDecorators(HttpTracingService.newDecorator(tracing),
                               LoggingService.newDecorator());
    }

    @Bean
    public AnnotatedServiceRegistrationBean helloHandlerRegistrationBean(HelloHandler handler) {
        return new AnnotatedServiceRegistrationBean()
                .setServiceName("helloService")
                .setService(handler)
                .setDecorators(ThrottlingHttpService
                                       .newDecorator(throttlingHelper.newThrottlingStrategy("frontend")),
                               HttpTracingService.newDecorator(tracing),
                               LoggingService.newDecorator());
    }
}
