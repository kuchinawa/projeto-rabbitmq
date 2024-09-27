package org.exemplo3;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class EmissorLog2 {

    private static final String EXCHANGE = "logs";

    public static void main(String[] argv) throws Exception {

        ConnectionFactory fabrica = new ConnectionFactory();
        fabrica.setHost("localhost");
        try (Connection conexao = fabrica.newConnection();
             Channel canal = conexao.createChannel()) {
            canal.exchangeDeclare(EXCHANGE, "fanout");

            String mensagem;
            for (int i = 0; i < 15; i++) {
                mensagem = "info: echo " + new Random().nextInt(100);
                canal.basicPublish(EXCHANGE, "", null, mensagem.getBytes(StandardCharsets.UTF_8));
                System.out.println(" Enviada --> '" + mensagem + "'");
                Thread.sleep(500);
            }
        }
    }
}
