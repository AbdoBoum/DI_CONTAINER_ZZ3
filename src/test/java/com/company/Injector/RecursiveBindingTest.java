package com.company.Injector;

import com.company.Injector.Container;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RecursiveBindingTest {

    interface TestInterface {}

    class A implements TestInterface{
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
        public E() {}
    }

    Container container;

    @BeforeEach
    void setup() throws Exception {
        container = new Container();
    }

    @Test
    void recursive_binding_test() {

        container.bind(TestInterface.class, A.class);

        final TestInterface instance = container.getBeanInstance(TestInterface.class);
        assertThat(instance).isNotNull();
        assertThat(instance).isInstanceOf(A.class);
    }
}
