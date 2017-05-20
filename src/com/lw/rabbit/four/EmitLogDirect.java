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
		// ����2�� direct, topic, headers and fanout.
		// fanout:��㲥������Ϣ
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		String message = getMessage(args);
		
		// ��ȡ��ǰ��־����
		String severity = getSeverity(args) ;
		
		/**
		 * ����2��routingKey,ע�⣺������Ҫָ����,������Ϊ�ǹ㲥��Ϣ,��˲���ָ��
		 *  ����3��BasicProperties
		 */
		channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
		
		/**
		 * ֮ǰ�İ汾��EXCHANGE_NAMEĬ��Ϊ��,��Ϣ��ͨ��routingKey·�ɵ�queue
		 */
		// channel.basicPublish("", "hello", null, message.getBytes());

		System.out.println(" [x] Sent '" + message + "'");
		channel.close();
		connection.close();
	}
	
	/**
	 *  ��ȡ��־����
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