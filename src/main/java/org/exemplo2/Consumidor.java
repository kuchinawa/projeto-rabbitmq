package org.exemplo2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class Consumidor {

    private static final String FILA = "filaTarefas";

    public static void main(String[] argv) throws Exception {

        ConnectionFactory fabrica = new ConnectionFactory();
        fabrica.setHost("localhost");

        final Channel canal;
        Connection conexao = fabrica.newConnection();
        canal = conexao.createChannel();
        canal.queueDeclare(FILA, true, false, false, null);
        System.out.println(" [*] Esperando mensagens...");
        /*
        entregar apenas uma mensagem por vez a um consumidor,
        sem enviar outra mensagem até que a anterior
        tenha sido acknowledged (confirmada).
         */
        canal.basicQos(1);
        /*
        Aplicações:
            Em Filas de Trabalho, onde as tarefas podem
        ser intensivas em termos de recursos. É desejável que
        cada trabalhador processe uma tarefa por vez antes de pegar outra.

            Se um consumidor falhar ou for interrompido sem confirmar
        uma mensagem, o RabbitMQ pode redirecionar essa mensagem
        para outro consumidor, garantindo que a tarefa não seja perdida.
         */

        DeliverCallback callbackEntrega = (tagConsumidor, entrega) -> {
            String mensagem = new String(entrega.getBody(), StandardCharsets.UTF_8);

            System.out.println("Recebida <-- '" + mensagem + "'");
            try {
                fazerAlgo(mensagem);
            } finally {
                System.out.println(" [x] Feito!");
                canal.basicAck(entrega.getEnvelope().getDeliveryTag(), false);
                /*
                é utilizado no RabbitMQ para confirmar o processamento
                de uma mensagem. A função do acknowledge (ack) é informar
                ao RabbitMQ que a mensagem foi processada com sucesso e
                pode ser removida da fila.
                Sem essa confirmação, o RabbitMQ mantém a mensagem em
                estado pendente e pode reenviá-la a outro consumidor
                caso ocorra uma falha.
                 */
            }
        };
        canal.basicConsume(FILA, false, callbackEntrega, tagConsumidor -> { });
    }

    private static void fazerAlgo(String tarefa) {

        for (char ch : tarefa.toCharArray()) {
            if (ch == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /*
    Observação adicional:
     - Escalonamento round-robin
        Uma das vantagens de usar uma fila de tarefas
        é a capacidade de paralelizar facilmente o trabalho.
        Em um backlog de tarefas, pode-se simplesmente
        adicionar mais consumidores e, dessa forma,
        escalar facilmente.
     */
}

