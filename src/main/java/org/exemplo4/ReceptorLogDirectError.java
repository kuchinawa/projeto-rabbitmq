package org.exemplo4;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class ReceptorLogDirectError {
    private static final String EXCHANGE = "direct_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory fabrica = new ConnectionFactory();
        fabrica.setHost("localhost");
        Connection conexao = fabrica.newConnection();
        Channel canal = conexao.createChannel();

        canal.exchangeDeclare(EXCHANGE, "direct");
        String nomeFila = canal.queueDeclare().getQueue();

        String routingKey = "error";
        canal.queueBind(nomeFila, EXCHANGE, routingKey);

        System.out.println(" [*] Esperando mensagens...");

        DeliverCallback deliverEntrega = (tagConsumidor, entrega) -> {
            String mensagem = new String(entrega.getBody(), StandardCharsets.UTF_8);
            System.out.println(" Recebida <-- '" +
                    entrega.getEnvelope().getRoutingKey() + "':'" + mensagem + "'");
        };
        canal.basicConsume(nomeFila, true, deliverEntrega, tagConsumidor -> { });
    }
}
