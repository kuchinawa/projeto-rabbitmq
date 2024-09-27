package org.exemplo3;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class ReceptorLogs {

    private static final String EXCHANGE = "logs";

    public static void main(String[] argv) throws Exception {

        ConnectionFactory fabrica = new ConnectionFactory();
        fabrica.setHost("localhost");
        Connection conexao = fabrica.newConnection();
        Channel canal = conexao.createChannel();

        canal.exchangeDeclare(EXCHANGE, "fanout");
        String nomeFila = canal.queueDeclare().getQueue();
        /*
        O método queueDeclare() sem argumentos cria uma fila temporária,
        que terá um nome gerado automaticamente pelo RabbitMQ.
        Essa fila será excluída automaticamente quando a conexão que a criou for fechada.
         */

        /*
        O método getQueue() retorna o nome da fila criada.
        Esse nome gerado automaticamente pode ser usado para outras operações,
        como o binding (ligação) da fila ao Exchange.
         */

        canal.queueBind(nomeFila, EXCHANGE, "");

        /*
         Liga a fila ao Exchange declarado anteriormente (EXCHANGE).
         Parâmetros:
            nomeFila: O nome da fila (neste caso, a fila temporária
            criada na etapa anterior).
            EXCHANGE: O nome do Exchange ao qual a fila está sendo conectada.
            "": A chave de roteamento. No caso do Fanout Exchange, a chave de
            roteamento é ignorada, pois as mensagens são enviadas para
            todas as filas ligadas, independentemente da chave.
         */

        System.out.println(" [*] Esperando mensagens...");

        DeliverCallback callbackEntrega = (tagConsumidor, entrega) -> {
            String mensagem = new String(entrega.getBody(), StandardCharsets.UTF_8);
            System.out.println(" Recebida <-- '" + mensagem + "'");
        };
        canal.basicConsume(nomeFila, true, callbackEntrega, tagConsumidor -> { });
    }
}
