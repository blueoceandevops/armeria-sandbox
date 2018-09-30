package info.matsumana.armeria.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linecorp.armeria.server.logging.LoggingService;
import com.linecorp.armeria.server.thrift.THttpService;
import com.linecorp.armeria.server.tracing.HttpTracingService;
import com.linecorp.armeria.spring.ThriftServiceRegistrationBean;

import brave.Tracing;
import info.matsumana.armeria.thrift.Hello3Service;
import info.matsumana.armeria.thrift.PingService;

@Configuration
public class ArmeriaThriftServiceConfig {

    private final Tracing tracing;

    ArmeriaThriftServiceConfig(ZipkinTracingFactory tracingFactory) {
        tracing = tracingFactory.create("backend3");
    }

    @Bean
    public ThriftServiceRegistrationBean pingService(PingService.AsyncIface service) {
        return new ThriftServiceRegistrationBean()
                .setPath("/thrift/ping")
                .setService(THttpService.of(service)
                                        .decorate(LoggingService.newDecorator())
                                        .decorate(HttpTracingService.newDecorator(tracing)))
                .setServiceName("PingService")
                .setExampleRequests(List.of(new PingService.ping_args()));
    }

    @Bean
    public ThriftServiceRegistrationBean hello3Service(Hello3Service.AsyncIface service) {
        return new ThriftServiceRegistrationBean()
                .setPath("/thrift/hello3")
                .setService(THttpService.of(service)
                                        .decorate(LoggingService.newDecorator())
                                        .decorate(HttpTracingService.newDecorator(tracing)))
                .setServiceName("Hello3Service")
                .setExampleRequests(List.of(new Hello3Service.hello_args("foo")));
    }
}
