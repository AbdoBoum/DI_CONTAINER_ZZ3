package com.company.Injector;

import com.company.Anotations.Inject;
import com.company.Injector.Container;
import com.company.Injector.ContainerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BindInterfaceTest {

    interface TestInterface {
    }

    class ExampleImplementation implements TestInterface {
        public ExampleImplementation(){}
    }

    class AnotherExampleImplementation implements TestInterface {
        public AnotherExampleImplementation(){}
        public AnotherExampleImplementation(int x){}
    }

    class ThirdExemple implements TestInterface {
        @Inject
        public ThirdExemple(){}
        public ThirdExemple(int x){}
    }

    private Container container;

    @BeforeEach
    void setup() throws Exception {
        container = new Container();
    }

    @Test
    void bind_interface_test() {
        container.bind(TestInterface.class, ExampleImplementation.class);

        assertThat(container.getBeanInstance(TestInterface.class)).isInstanceOf(ExampleImplementation.class);
    }

    @Test
    void bind_interface_multiple_constructors_test() {
        container.bind(TestInterface.class, AnotherExampleImplementation.class);

        assertThrows(ContainerException.class, () -> {
            container.getBeanInstance(TestInterface.class);
        });
    }

    @Test
    void bind_interface_inject_test() {
        container.bind(TestInterface.class, ThirdExemple.class);

        assertThat(container.getBeanInstance(TestInterface.class)).isInstanceOf(ThirdExemple.class);
    }

}
