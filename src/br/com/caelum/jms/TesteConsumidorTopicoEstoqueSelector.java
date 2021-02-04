package br.com.caelum.jms;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

/**
Utilizando o selector, pode ser selecionado um critério de filtro.
 No produtor, é setado a propriedade com a regra para ser enviada.
 Exemplo no EstoqueSelector
 MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura-selector", "ebook is null OR ebook=false", false);
 Ou seja irá pegar ebook is null OR ebook=false. Conforme enviado pelo TesteProdutorTopico
 */
public class TesteConsumidorTopicoEstoqueSelector {
    public static void main(String[] args) throws NamingException, JMSException {

        InitialContext context = new InitialContext();
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");

        Connection connection = factory.createConnection();
        connection.setClientID("estoque");

        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topico = (Topic) context.lookup("loja");
        MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura-selector", "ebook is null OR ebook=false", false);


        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                //Pegar apenas a mensagem do envio
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println(textMessage.getText());
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
