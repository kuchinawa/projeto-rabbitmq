package org.exemplo1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class Receptor {

    private final static String FILA = "fila01";

    public static void main(String[] argv) throws Exception {

        ConnectionFactory fabrica = new ConnectionFactory();
        fabrica.setHost("localhost");

        Connection conexao = fabrica.newConnection();
        Channel canal = conexao.createChannel();
        canal.queueDeclare(FILA, false, false, false, null);
        System.out.println(" [*] Esperando mensagens.");

        DeliverCallback callbackEntrega = (tagConsumidor, entrega) -> {
            String mensagem = new String(entrega.getBody(), StandardCharsets.UTF_8);
            System.out.println("Recebida <-- '" + mensagem + "'");
        };
        /*
        A classe DeliverCallback é uma interface que faz parte da
        biblioteca RabbitMQ Java Client,
        utilizada para implementar um callback que é chamado toda vez
        que uma mensagem é entregue a um consumidor.

        A interface DeliverCallback define um método único chamado handle,
        que precisa ser implementado.
        Esse método é invocado automaticamente
        pelo consumidor sempre que uma nova mensagem é recebida da fila.
         */

        boolean autoAck = true;
        canal.basicConsume(FILA, autoAck, callbackEntrega, tagConsumidor -> { });
        /*
        Consumir a fila: O método basicConsume é chamado, associando o
        callback com a fila para que o RabbitMQ possa entregar
        mensagens ao consumidor.

        Parâmetros:
        FILA (String queue):
            Nome da fila da qual o consumidor vai consumir as mensagens.
            A variável FILA representa o nome da fila que já foi previamente
            declarada, e o consumidor ficará escutando mensagens dessa fila.
            Sempre que uma nova mensagem chegar nessa fila, o código definido
            no callbackEntrega será executado.

        true (boolean autoAck):
            Esse parâmetro indica se o acknowledgement automático
            (reconhecimento) está ativado ou não.
            true: O RabbitMQ automaticamente marcará a mensagem
            como entregue assim que for enviada ao consumidor,
            sem esperar que o consumidor confirme explicitamente.
            false: O consumidor precisa manualmente enviar um
            acknowledgement (usando channel.basicAck())
            para confirmar que a mensagem foi processada com sucesso.
            Isso é útil para garantir que a mensagem só seja removida
            da fila se o processamento for bem-sucedido.

        callbackEntrega (DeliverCallback deliverCallback):
            Esse é o callback que será chamado sempre que uma nova
            mensagem for recebida.
            O DeliverCallback é uma interface funcional,
            então pode-se implementar esse método usando
            uma expressão lambda ou uma classe anônima.
            Esse callback define o comportamento do consumidor
            quando uma mensagem chega na fila.
            Ele recebe como parâmetros o corpo da mensagem e
            algumas informações adicionais, como o número de
            entrega e a chave de roteamento.

        tagConsumidor -> { } (CancelCallback cancelCallback):
            Esse é o callback de cancelamento, que é chamado quando
            o consumidor é cancelado (por exemplo, se a fila for deletada,
            ou se a conexão for fechada).
            O CancelCallback é uma interface funcional que recebe o
            tag do consumidor (uma string que identifica o consumidor)
            e define o que fazer quando o consumidor for cancelado.
            No código, o callback de cancelamento está vazio
            (tagConsumidor -> { }), indicando que nenhuma ação está
            ocorrendo nessa situação de forma explícita.

         */

    }
}
