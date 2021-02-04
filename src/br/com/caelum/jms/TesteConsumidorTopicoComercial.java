package br.com.caelum.jms;

import br.com.caelum.modelo.Pedido;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class TesteConsumidorTopicoComercial {
    public static void main(String[] args) throws NamingException, JMSException {

        InitialContext context = new InitialContext();
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");

        Connection connection = factory.createConnection();
        connection.setClientID("comercial");

        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topico = (Topic) context.lookup("loja");
        MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura");


        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                //Pegar apenas a mensagem do envio
                ObjectMessage objectMessage = (ObjectMessage) message;
                try {
                    Pedido pedido = (Pedido) objectMessage.getObject();
                    System.out.println(pedido.getCodigo());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        new Scanner(System.in).nextLine();

        session.close();
        connection.close();
        context.close();
    }
}
