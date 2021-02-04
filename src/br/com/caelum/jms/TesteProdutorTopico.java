package br.com.caelum.jms;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
/*
Ao executar a mensagem seriá recebida pelo TesteConsumidorTopicoEstoque e
TesteConsumidorTopicoComercial. Assim, é produzido a mensagem, enviada
e os 2 irão ficar escutando e recebendo essas mensagens enviadas.
 */
public class TesteProdutorTopico {
    public static void main(String[] args) throws NamingException, JMSException {

        InitialContext context = new InitialContext();
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");

        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination topico = (Destination) context.lookup("loja");
        MessageProducer producer = session.createProducer(topico);

        Message message = session.createTextMessage("<pedido><id>222</id></pedido>");
        producer.send(message);

        session.close();
        connection.close();
        context.close();
    }
}
