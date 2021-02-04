package br.com.caelum.jms;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;
/**
 DLQ = Dead Letter Qeue, quando há falha no envio, ele tentar enviar a mensagem
6 vezes, não obtendo sucesso, o ActiveMQ considera essas mensagens como 'venenosas'
ficando no DLQ
**/
public class TesteConsumidorDLQ {
    public static void main(String[] args) throws NamingException, JMSException {

        InitialContext context = new InitialContext();
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");

        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination fila = (Destination) context.lookup("DLQ");
        MessageConsumer consumer = session.createConsumer(fila);


        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                System.out.println(message);
            }
        });

        new Scanner(System.in).nextLine();

        session.close();
        connection.close();
        context.close();
    }
}
