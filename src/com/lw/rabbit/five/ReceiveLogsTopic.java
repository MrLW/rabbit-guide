package com.lw.rabbit.five;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class ReceiveLogsTopic {

	private static final String EXCHANGE_NAME = "topic_logs";

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		// 创建一个独一无二的不会持久化且自动删除的队列
		String queueName = channel.queueDeclare().getQueue();

		System.out.println("queueName:" + queueName);

		if (args.length < 1) {
			System.err.println("Usage: ReceiveLogsTopic [binding_key]...");
			System.exit(1);
		}
		// bindingKey如果为#,则接收所有消息,类似fanout
		// 当没有#和*的情况下,此时类似direct
		for (String bindingKey : args) {
			channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
		}

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
			}
		};

		channel.basicConsume(queueName, true, consumer);
	}
}
