package com.lw.rabbit.second;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Recv {
	private final static String QUEUE_NAME = "hello";

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1");
		Connection connection = factory.newConnection();

		Channel channel = connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		System.out.println("等待消息...");
		
		channel.basicQos(1);
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");

				System.out.println(" [x] Received '" + message + "'");
				/*try {
					try {
						doWork(message);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} finally {
					System.out.println(" [x] Done");
					// 返回给
					channel.basicAck(envelope.getDeliveryTag(), false);
				}*/
				try {
					doWork(message);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					System.out.println(" [x] Done");
					// 1 
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};

		// boolean autoAck = true; // acknowledgment is covered below
		boolean autoAck = false;
		channel.basicConsume(QUEUE_NAME, autoAck, consumer);
		// 两个参数的方法中,autoAck默认为false,也就是说默认开启acknowledgment
		// channel.basicConsume(QUEUE_NAME, consumer)
	}

	private static void doWork(String task) throws InterruptedException {
		for (char ch : task.toCharArray()) {
			if (ch == '.')
				Thread.sleep(1000);
		}
	}
}
