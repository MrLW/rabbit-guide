package com.lw.rabbit.first;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {
	private final static String QUEUE_NAME = "hello";

	public static void main(String[] args) throws Exception {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		// 将消息发布到消息队列中
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		String message = "Hello World" ;
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		System.out.println("发送的消息：" + message );
		
		channel.close();
		connection.close();
	}
}
