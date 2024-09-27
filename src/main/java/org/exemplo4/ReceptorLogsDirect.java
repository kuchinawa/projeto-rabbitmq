package org.exemplo4;

import com.rabbitmq.client.*;

public class ReceptorLogsDirect {

    private static final String EXCHANGE = "direct_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory fabrica = new ConnectionFactory();
        fabrica.setHost("localhost");
        Connection conexao = fabrica.newConnection();
        Channel canal = conexao.createChannel();

        canal.exchangeDeclare(EXCHANGE, "direct");
        String nomeFila = canal.queueDeclare().getQueue();

        String routingKey = "info";
        canal.queueBind(nomeFila, EXCHANGE, routingKey);

        System.out.println(" [*] Esperando mensagens...");

        DeliverCallback deliverEntrega = (tagConsumidor, entrega) -> {
            String mensagem = new String(entrega.getBody(), "UTF-8");
            System.out.println(" Recebida <-- '" +
                    entrega.getEnvelope().getRoutingKey() + "':'" + mensagem + "'");
        };
        canal.basicConsume(nomeFila, true, deliverEntrega, tagConsumidor -> { });
    }
}
