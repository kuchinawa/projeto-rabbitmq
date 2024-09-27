package org.exemplo1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EmissorComLoop {

    private final static String FILA = "fila01";

    public static void main(String[] args) throws Exception {

        final ConnectionFactory fabricaConexao = new ConnectionFactory();
        fabricaConexao.setHost("localhost");

        try (final Connection conexao = fabricaConexao.newConnection();
             final Channel canal = conexao.createChannel()) {
            canal.queueDeclare(FILA, false, false, false, null);
            enviarMensagem(canal);
        }
    }

    public static void enviarMensagem(Channel c) throws IOException, InterruptedException {

        String mensagem;

        for (int i = 1; i <= 15; i++) {
            mensagem = "Faça algo (" + i + ")";
            System.out.println(" [x] Enviando essa mensagem para a fila: " + mensagem);
            c.basicPublish("", FILA, null, mensagem.getBytes(StandardCharsets.UTF_8));
            Thread.sleep(1000);
        }
    }
}
