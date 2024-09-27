package org.exemplo3;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class EmissorLog {

    /*
    Exemplo publicar/assinar.
    Uma mensagem pode ser entregue
    a muitos consumidores.
     */

    /*
    Consirede um sistema de logs.
    Ele consistirá em dois programas:
     - o primeiro emitirá mensagens de log;
     - e o segundo as receberá e imprimirá.
     */
    private static final String EXCHANGE = "logs";

    public static void main(String[] argv) throws Exception {

        ConnectionFactory fabrica = new ConnectionFactory();
        fabrica.setHost("localhost");
        try (Connection conexao = fabrica.newConnection();
             Channel canal = conexao.createChannel()) {
            /*
            Quando um produtor publica uma mensagem, ele a envia
            para um Exchange, que decide para qual fila a
            mensagem será enviada, dependendo de sua configuração
            e da chave de roteamento associada à mensagem.
             */
            canal.exchangeDeclare(EXCHANGE, "fanout");
            /*
            Um Fanout Exchange roteia mensagens para todas as filas ligadas
             a ele.
             Ele é como um broadcast (difusão): qualquer mensagem enviada
             a um Fanout Exchange será copiada para todas as filas conectadas.
             */
            String mensagem = "info - log " + new Random().nextInt(100);

            canal.basicPublish(EXCHANGE, "", null, mensagem.getBytes(StandardCharsets.UTF_8));
            System.out.println(" Enviada --> '" + mensagem + "'");
        }
    }
}
