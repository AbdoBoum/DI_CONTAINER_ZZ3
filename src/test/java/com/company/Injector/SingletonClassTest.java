package com.company.Injector;

import com.company.Anotations.Singleton;
import com.company.Injector.Container;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonClassTest {

    interface TestInterface {
    }

    @Singleton
    class ExampleImplementation implements TestInterface {
        public ExampleImplementation(){}
    }

    private Container container;

    @BeforeEach
    void setup() throws Exception {
        container = new Container();
    }

    @Test
    void bind_interface_test() {
        container.bind(TestInterface.class, ExampleImplementation.class);

        TestInterface firstInstance = container.getBeanInstance(TestInterface.class);
        TestInterface secondInstance = container.getBeanInstance(TestInterface.class);

        assertThat(firstInstance).isInstanceOf(ExampleImplementation.class);
        assertThat(secondInstance).isInstanceOf(ExampleImplementation.class);
        assertThat(firstInstance).isSameAs(secondInstance);
    }

}
