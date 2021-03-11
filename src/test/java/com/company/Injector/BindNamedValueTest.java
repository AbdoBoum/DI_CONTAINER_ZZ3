package com.company.Injector;

import com.company.Anotations.Service;
import com.company.Anotations.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BindNamedValueTest {

    Container container;

    interface ServiceInterface {
        String getDbPath();
    }

    @Service
    static class TestService implements ServiceInterface{
        @Value("/ressources/properties")
        String dbPath;

        @Override
        public String getDbPath() {
            return dbPath;
        }
    }

    @BeforeEach
    void createInjector() {
        container = new Container();
    }

    @Test
    @DisplayName("Test binding named values")
    void test_binding_named_values() {
        try {
            ServiceInterface newsService = container.getBeanInstance(TestService.class);
            assertEquals(TestService.class, newsService.getClass());
            assertEquals("/ressources/properties", newsService.getDbPath());
        } catch (ContainerException e) {
            fail("Test fail because of binding error. " + e.getMessage());
        }
    }

}
