package com.example.mmq.mqserver.core;

import com.example.mmq.common.MQException;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/5 14:28
 */
public class Router {
    public boolean checkBindingKey(String bindingKey) {
        // todo
        return true;
    }

    public boolean checkRoutingKey(String routingKey) {
        // todo
        return true;
    }

    // 这个方法是用来判断
    public boolean route(ExchangeType exchangeType, Binding binding, Message message) throws MQException {
        // 根据不同的 exchangeType 使用不同的判定转发规则
        if (exchangeType == ExchangeType.TOPIC) {
            return routeTopic(binding, message);
        } else if (exchangeType == ExchangeType.FANOUT) {
            // 如果是 fanout，所有绑定的队列都要转发的
            return true;
        } else {
            // 其他情况是不应该存在的
            throw new MQException("[Router] 交换机类型非法！exchangeType=" + exchangeType);
        }
    }

    private boolean routeTopic(Binding binding, Message message) {

        return true;
    }
}
