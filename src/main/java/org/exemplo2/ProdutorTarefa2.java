package org.exemplo2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class ProdutorTarefa2 {
    private static final String FILA = "filaTarefas";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory fabrica = new ConnectionFactory();
        fabrica.setHost("localhost");
        try (Connection conexao = fabrica.newConnection();
             Channel canal = conexao.createChannel()) {
            canal.queueDeclare(FILA, true, false, false, null);

            String mensagem;
            for (int i = 0; i < 20; i++) {
                mensagem = "Executar: " + i + ". Tarefa " + new Random().nextInt(1000);
                canal.basicPublish("", FILA,
                        MessageProperties.PERSISTENT_TEXT_PLAIN,
                        mensagem.getBytes(StandardCharsets.UTF_8));
                System.out.println(" Enviada --> '" + mensagem + "'");
                Thread.sleep(500);
            }
        }
    }
}
