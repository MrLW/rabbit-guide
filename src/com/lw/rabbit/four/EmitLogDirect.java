package com.lw.rabbit.four;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLogDirect {

	private static final String EXCHANGE_NAME = "direct_logs";

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();

		Channel channel = connection.createChannel();
		// 参数2： direct, topic, headers and fanout.
		// fanout:会广播所有消息
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		String message = getMessage(args);
		
		// 获取当前日志级别
		String severity = getSeverity(args) ;
		
		/**
		 * 参数2：routingKey,注意：本来是要指定的,但是因为是广播消息,因此不用指定
		 *  参数3：BasicProperties
		 */
		channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
		
		/**
		 * 之前的版本：EXCHANGE_NAME默认为空,消息会通过routingKey路由到queue
		 */
		// channel.basicPublish("", "hello", null, message.getBytes());

		System.out.println(" [x] Sent '" + message + "'");
		channel.close();
		connection.close();
	}
	
	/**
	 *  获取日志级别
	 * @param strings
	 * @return
	 */
	 private static String getSeverity(String[] strings){
	    if (strings.length < 1)
	    	    return "info";
	    return strings[0];
	 }

	private static String getMessage(String[] strings) {
		if (strings.length < 1)
			return "Hello World!";
		return joinStrings(strings, " ");
	}

	private static String joinStrings(String[] strings, String delimiter) {
		int length = strings.length;
		if (length == 0)
			return "";
		StringBuilder words = new StringBuilder(strings[0]);
		for (int i = 1; i < length; i++) {
			words.append(delimiter).append(strings[i]);
		}
		return words.toString();
	}
}
