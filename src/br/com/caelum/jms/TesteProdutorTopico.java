package br.com.caelum.jms;

import br.com.caelum.modelo.Pedido;
import br.com.caelum.modelo.PedidoFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXB;
import java.io.StringWriter;

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

        Pedido pedido = new PedidoFactory().geraPedidoComValores();
        StringWriter writer = new StringWriter();
        JAXB.marshal(pedido, writer); //recebe o pedido e escreve para enviar a string xml

        String xml = writer.toString();
        Message message = session.createTextMessage(xml);
        //message.setBooleanProperty("ebook", true);
        producer.send(message);

        session.close();
        connection.close();
        context.close();
    }
}
