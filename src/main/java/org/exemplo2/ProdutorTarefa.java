package org.exemplo2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class ProdutorTarefa {

    /*
    Exemplo: fila de tarefas.
    Cada tarefa é entrega a
    um consumidor
     */
    private static final String FILA = "filaTarefas";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory fabrica = new ConnectionFactory();
        fabrica.setHost("localhost");
        try (Connection conexao = fabrica.newConnection();
             Channel canal = conexao.createChannel()) {
            canal.queueDeclare(FILA, true, false, false, null);

            String mensagem = "tarefa: " + new Random().nextInt(1000);

            canal.basicPublish("", FILA,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    mensagem.getBytes(StandardCharsets.UTF_8));
            /*
            (AMQP.BasicProperties): As propriedades da mensagem,
            como tipo, modo de persistência, entre outras.

            MessageProperties.PERSISTENT_TEXT_PLAIN indica que a mensagem será:
            Persistente: Significa que a mensagem será armazenada
            em disco. Isso garante que a mensagem não será perdida
            caso o RabbitMQ falhe ou reinicie, desde que a fila
            também seja declarada como persistente.

            Textual e sem formatação: Indica que o tipo de conteúdo
            da mensagem é texto plano.
             */
            System.out.println(" Enviada --> '" + mensagem + "'");
        }
    }

}
