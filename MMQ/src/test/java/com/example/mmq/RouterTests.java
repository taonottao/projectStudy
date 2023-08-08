package com.example.mmq;

import com.example.mmq.common.MQException;
import com.example.mmq.mqserver.core.Binding;
import com.example.mmq.mqserver.core.ExchangeType;
import com.example.mmq.mqserver.core.Message;
import com.example.mmq.mqserver.core.Router;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/7 11:56
 */
@SpringBootTest
public class RouterTests {

    private Router router = new Router();
    private Binding binding = null;
    private Message message = null;

    @BeforeEach
    public void setUp() {
        binding = new Binding();
        message = new Message();
    }

    @AfterEach
    public void TearDown() {
        binding = null;
        message = null;
    }

    @Test
    public void test1() throws MQException {
        binding.setBindingKey("aaa");
        message.setRoutingKey("aaa");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }
    @Test
    public void test2() throws MQException {
        binding.setBindingKey("aaa.bbb");
        message.setRoutingKey("aaa.bbb");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }
    @Test
    public void test3() throws MQException {
        binding.setBindingKey("aaa.bbb");
        message.setRoutingKey("aaa.bbb.ccc");
        Assertions.assertFalse(router.route(ExchangeType.TOPIC, binding, message));
    }
    @Test
    public void test4() throws MQException {
        binding.setBindingKey("aaa.bbb");
        message.setRoutingKey("aaa.ccc");
        Assertions.assertFalse(router.route(ExchangeType.TOPIC, binding, message));
    }
    @Test
    public void test5() throws MQException {
        binding.setBindingKey("aaa.bbb.ccc");
        message.setRoutingKey("aaa.bbb.ccc");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }
    @Test
    public void test6() throws MQException {
        binding.setBindingKey("aaa.*");
        message.setRoutingKey("aaa.bbb");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }
    @Test
    public void test7() throws MQException {
        binding.setBindingKey("aaa.*.bbb");
        message.setRoutingKey("aaa.bbb.ccc");
        Assertions.assertFalse(router.route(ExchangeType.TOPIC, binding, message));
    }
    @Test
    public void test8() throws MQException {
        binding.setBindingKey("*.aaa.bbb");
        message.setRoutingKey("aaa.bbb");
        Assertions.assertFalse(router.route(ExchangeType.TOPIC, binding, message));
    }
    @Test
    public void test9() throws MQException {
        binding.setBindingKey("#");
        message.setRoutingKey("aaa.bbb.ccc");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }
    @Test
    public void test10() throws MQException {
        binding.setBindingKey("aaa.#");
        message.setRoutingKey("aaa.bbb");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }
    @Test
    public void test11() throws MQException {
        binding.setBindingKey("aaa.#");
        message.setRoutingKey("aaa.bbb.ccc");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }
    @Test
    public void test12() throws MQException {
        binding.setBindingKey("aaa.#.ccc");
        message.setRoutingKey("aaa.ccc");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }
    @Test
    public void test13() throws MQException {
        binding.setBindingKey("aaa.#.ccc");
        message.setRoutingKey("aaa.bbb.ccc");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }
    @Test
    public void test14() throws MQException {
        binding.setBindingKey("aaa.#.ccc");
        message.setRoutingKey("aaa.aaa.bbb.ccc");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }
    @Test
    public void test15() throws MQException {
        binding.setBindingKey("#.ccc");
        message.setRoutingKey("ccc");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }
    @Test
    public void test16() throws MQException {
        binding.setBindingKey("#.ccc");
        message.setRoutingKey("aaa.bbb.ccc");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }
    @Test
    public void test17() throws MQException {
        binding.setBindingKey("aaa.#.ccc");
        message.setRoutingKey("aaa.bbb.ccc.bbb.ccc");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }

}
