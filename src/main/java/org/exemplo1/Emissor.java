package org.exemplo1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class Emissor {

    private final static String FILA = "fila01";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory fabrica = new ConnectionFactory();
        /*
        Cria uma instância da ConnectionFactory, que é usada para
        configurar e criar conexões com o servidor RabbitMQ.
        ConnectionFactory: É uma classe fornecida pela biblioteca
        RabbitMQ que abstrai os detalhes da conexão com o servidor
        de mensageria. Ela permite definir parâmetros como o host,
        porta, nome de usuário e senha.
         */
        fabrica.setHost("localhost");
        /*
        Configura o endereço do servidor RabbitMQ.
        "localhost": o servidor RabbitMQ
        está sendo executado na máquina local (localhost).
        Se o servidor RabbitMQ estivesse em outra máquina,
        seria necessário especificar o endereço IP ou o hostname.
         */
        try (Connection conexao = fabrica.newConnection();
             Channel canal = conexao.createChannel()) {
            /*
            conexao = fabrica.newConnection(): Abre uma nova conexão com
            o servidor RabbitMQ. Uma conexão é como uma sessão entre o
            programa Java e o servidor RabbitMQ.
            canal = conexao.createChannel(): Cria um canal dentro da conexão.
            Um canal é um caminho lógico dentro da conexão que permite o
            envio e recebimento de mensagens.
            No RabbitMQ, a maior parte do trabalho (publicar mensagens,
            consumir mensagens, etc.) é feita no canal.
             */
            canal.queueDeclare(FILA, false, false, false, null);
            /*
            Declara uma fila chamada FILA no servidor RabbitMQ.
            Se a fila não existir, ela será criada.
            Parâmetros:

            FILA: O nome da fila. A constante FILA deve ter sido
            definida anteriormente no código, contendo o nome da
            fila que você deseja usar.
            false: O parâmetro indica que a fila não é durável, ou seja,
            se o servidor RabbitMQ for reiniciado, a fila será apagada.
            Para ter uma fila durável (que sobrevivesse a reinicializações),
            é preciso passar true aqui.
            false: A fila não é exclusiva para esta conexão.
            Isso significa que outras conexões podem acessar a fila.
            false: A fila não é auto-deletável. Se fosse true,
            a fila seria apagada automaticamente quando não tivesse mais consumidores.
            null: Parâmetros adicionais (opções), que neste caso são nulos,
            indicando que não há parâmetros extras.
             */
            String mensagem = "Primeira mensagem";
            canal.basicPublish("", FILA, null, mensagem.getBytes(StandardCharsets.UTF_8));
            /*
            Publica a mensagem na fila.
            Parâmetros:

            "": O nome do Exchange (trocador) onde a mensagem será publicada.
            Aqui, não está usando um Exchange específico, então usa-se o Default Exchange ("").
            O Default Exchange envia as mensagens diretamente para a
            fila com o nome especificado.
            FILA: O nome da fila para a qual a mensagem será enviada.
            null: As propriedades da mensagem (cabeçalhos, prioridade, etc.).
            Nesse caso, null indica que não há propriedades especiais para a mensagem.
            mensagem.getBytes(StandardCharsets.UTF_8): O corpo da mensagem,
            convertido para um array de bytes. O RabbitMQ trabalha com bytes,
            então a string "Primeira mensagem" é convertida para bytes
            usando a codificação UTF-8.
             */
            System.out.println(" --> Enviada '" + mensagem + "'");
        }
    }
}
