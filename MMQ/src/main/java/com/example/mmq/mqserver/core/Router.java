package com.example.mmq.mqserver.core;

import com.example.mmq.common.MQException;


/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/5 14:28
 */
public class Router {
    public boolean checkBindingKey(String bindingKey) {
        if (bindingKey.length() == 0) {
            // 空字符串 合法。比如在使用 direct/fanout  交换机时， bindingKey 用不上
            return true;
        }
        for (int i = 0; i < bindingKey.length(); i++) {
            char ch = bindingKey.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                continue;
            }
            if (ch >= 'a' && ch <= 'z') {
                continue;
            }
            if (ch >= '0' && ch <= '9') {
                continue;
            }
            if (ch == '_' || ch == '.' || ch == '*' || ch == '#') {
                continue;
            }
            return false;
        }
        // 检查 * 和 # 是否是独立的部分
        String[] words = bindingKey.split("\\.");
        for (String word : words) {
            // 检查 word 长度 > 1 并且包含了 * 或者 #，就是非法的格式了
            if (word.length() > 1 && (word.contains("*") || word.contains("#"))) {
                return false;
            }
        }

        // 约定一下，通配符之间的相邻关系（人为约定，为了实现起来方便）
        // 1. aaa.#.#.bbb => 非法
        // 2. aaa.#.*.bbb => 非法
        // 3. aaa.*.#.bbb => 非法
        // 4. aaa.*.*.bbb => 合法
        for (int i = 0; i < words.length - 1; i++) {
            if (words[i].equals("#") && words[i + 1].equals("#")) {
                return false;
            }
            if (words[i].equals("#") && words[i + 1].equals("*")) {
                return false;
            }
            if (words[i].equals("*") && words[i + 1].equals("#")) {
                return false;
            }
        }

        return true;
    }

    public boolean checkRoutingKey(String routingKey) {
        if (routingKey.length() == 0) {
            // 空字符串也是合法情况，比如在使用 fanout 交换机的时候， routingKey 用不上，就可以设为""
            return true;
        }
        for (int i = 0; i < routingKey.length(); i++) {
            char ch = routingKey.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                continue;
            }
            if (ch >= 'a' && ch <= 'z') {
                continue;
            }
            if (ch >= '0' && ch <= '9') {
                continue;
            }
            if (ch == '_' || ch == '.') {
                continue;
            }
            return false;
        }
        return true;
    }

    // 这个方法是用来判断该消息是否可以转发给这个绑定对应的队列
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

        String[] bindingTokens = binding.getBindingKey().split("\\.");
        String[] routingTokens = message.getRoutingKey().split("\\.");

        int bindingIndex = 0;
        int routingIndex = 0;
        while (bindingIndex < bindingTokens.length && routingIndex < routingTokens.length) {
            if (bindingTokens[bindingIndex].equals("*")) {
                bindingIndex++;
                routingIndex++;
                continue;
            } else if (bindingTokens[bindingIndex].equals("#")) {
                bindingIndex++;
                if (bindingIndex == bindingTokens.length) {
                    // 说明 # 后面没东西了，那么一定能够匹配成功
                    return true;
                }
                // # 后面还有东西，拿着这个内容，去 routingKey 中往后找，找到对应的位置
                // findNextMatch 这个方法用来查找改部分在 routingKey 的位置，返回下标，没找到就返回 -1
                routingIndex = findNextMatch(routingTokens, routingIndex, bindingTokens[bindingIndex]);
                if (routingIndex == -1) {
                    return false;
                }
                // 找到了，继续往后匹配
                bindingIndex++;
                routingIndex++;
            } else {
                if (!bindingTokens[bindingIndex].equals(routingTokens[routingIndex])) {
                    return false;
                }
                bindingIndex++;
                routingIndex++;
            }
        }
        // 判定是否是双方同时达到末尾
        if (bindingIndex == bindingTokens.length && routingIndex == routingTokens.length) {
            return true;
        }

        return false;
    }

    private int findNextMatch(String[] routingTokens, int routingIndex, String bindingToken) {
        for (int i = routingTokens.length - 1; i >= routingIndex; i--) {
            if (routingTokens[i].equals(bindingToken)) {
                return i;
            }
        }
        return -1;
    }
}
