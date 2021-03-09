package com.company.Injector;

import com.company.Injector.Container;
import com.company.Injector.ContainerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class SimpleInjectionTest {

    Container container;

    interface HttpService {}

    interface NewsService {
        HttpService getHttpService();
    }

    static class DarkWebHttpService implements HttpService{
    }

    static class RssNewsService implements NewsService{

        HttpService httpService;

        public RssNewsService(HttpService httpService) {
            this.httpService = httpService;
        }

        public HttpService getHttpService() { return httpService; }
    }

    @BeforeEach
    void createInjector() {
        container = new Container();
    }

    @Test
    @DisplayName("Test simple injection")
    void testSimpleInjection() {
        try {
            container.bind(HttpService.class, DarkWebHttpService.class);
            container.bind(NewsService.class, RssNewsService.class);

            NewsService newsService = container.getBeanInstance(NewsService.class);
            assertTrue(newsService.getHttpService() instanceof DarkWebHttpService);
        } catch (ContainerException e) {
            fail("Test fail because of binding error. " + e.getMessage());
        }
    }


}
