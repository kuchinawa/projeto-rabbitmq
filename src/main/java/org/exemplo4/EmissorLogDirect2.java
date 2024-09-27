package org.exemplo4;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class EmissorLogDirect2 {

    private static final String EXCHANGE = "direct_logs";

    public static void main(String[] argv) throws Exception {

        String routingKey, mensagem;
        ConnectionFactory fabrica = new ConnectionFactory();
        fabrica.setHost("localhost");
        try (Connection conexao = fabrica.newConnection();
             Channel canal = conexao.createChannel()) {
            canal.exchangeDeclare(EXCHANGE, "direct");

            for (int i = 0; i < 30; i++) {
                routingKey = getTipoLog(i % 3);
                mensagem = "log " + i + " - " + routingKey + " - " + new Random().nextInt(500);
                canal.basicPublish(
                        EXCHANGE,
                        routingKey,
                        null,
                        mensagem.getBytes(StandardCharsets.UTF_8));
                System.out.println(" Enviada --> '" + routingKey + "':'" + mensagem + "'");
                Thread.sleep(1000);
            }

        }
    }

    private static String getTipoLog(int chave) {

        if (chave == 1)
            return "info";
        else if(chave == 2)
            return "warning";
        else
            return "error";
    }
    //..
}
