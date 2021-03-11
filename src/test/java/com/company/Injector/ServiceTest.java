package com.company.Injector;

import com.company.Anotations.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {

    Container container;

    interface HttpService {}

    interface NewsService {
        HttpService getHttpService();
    }

    @Service
    static class DarkWebHttpService implements HttpService {
    }

    @Service
    static class RssNewsService implements NewsService {

        HttpService httpService;

        public RssNewsService(HttpService httpService) {
            this.httpService = httpService;
        }

        public HttpService getHttpService() { return httpService; }
    }

    @Service
    static class postWebService {
    }

    @BeforeEach
    void createInjector() {
        container = new Container();
    }

    @Test
    @DisplayName("Test interface injection with @Service annotation")
    void test_interface_service_injection() {
        try {
            NewsService newsService = container.getBeanInstance(NewsService.class);
            assertTrue(newsService.getHttpService() instanceof DarkWebHttpService);
        } catch (ContainerException e) {
            fail("Test fail because of binding error. " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test class injection with @Service annotation")
    void test_class_service_injection() {
        try {
            postWebService postWebService = container.getBeanInstance(postWebService.class);
            assertNotNull(postWebService);
        } catch (ContainerException e) {
            fail("Test fail because of binding error. " + e.getMessage());
        }
    }

}
