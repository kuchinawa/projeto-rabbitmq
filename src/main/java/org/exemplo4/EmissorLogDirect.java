package org.exemplo4;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class EmissorLogDirect {

    private static final String EXCHANGE = "direct_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory fabrica = new ConnectionFactory();
        fabrica.setHost("localhost");
        try (Connection conexao = fabrica.newConnection();
             Channel canal = conexao.createChannel()) {
            canal.exchangeDeclare(EXCHANGE, "direct");
            /*
            exchange direct:
            O algoritmo de roteamento por trás é simples
            - uma mensagem vai para as filas cuja bindingKey
            corresponde exatamente à routingKey da mensagem.
             */

            String routingKey = getTipoLog(1);
            String mensagem = " echo #1";

            canal.basicPublish(
                    EXCHANGE,
                    routingKey,
                    null,
                    mensagem.getBytes(StandardCharsets.UTF_8));
            System.out.println(" Enviada --> '" + routingKey + "':'" + mensagem + "'");
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

}
