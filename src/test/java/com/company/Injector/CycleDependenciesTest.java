package com.company.Injector;

import com.company.Injector.Container;
import com.company.Injector.ContainerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CycleDependenciesTest {

    interface TestInterface {}

    class A implements TestInterface {
        public A(B b, C c) {}
    }

    class B {
        public B(D d) {}
    }

    class C {
        public C(D d) {}
    }

    class D {
        public D(E e) {}
    }

    class E {
        public E(A a) {}
    }

    Container container;

    @BeforeEach
    void setup() throws Exception {
        container = new Container();
    }

    @Test
    void recursive_binding_test() {

        container.bind(TestInterface.class, A.class);
        assertThrows(ContainerException.class, () -> {
            container.getBeanInstance(TestInterface.class);
        });
    }

}
