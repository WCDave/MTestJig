package console;

import java.util.Map;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class RocketConsole implements MessageListener {

  private String url = "tcp://daveaux:61616";
  private Connection connection;
  private MessageProducer producer;
  private Session session;
  private int count;
  private long start;
  private Topic topic;
  private Topic control;

  /**
   * @param args
   */
  public static void main(String[] args) {
    RocketConsole console = new RocketConsole();
    try {
      console.run();
    } catch (JMSException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    console.execute();
  }

  private void execute() {
    try {
      producer.send(session.createTextMessage("Hello"));
    } catch (JMSException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  public RocketConsole() {

  }

  public void run() throws JMSException {
    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
    connection = factory.createConnection();
    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    topic = session.createTopic("rocket.fromRocket");
    control = session.createTopic("rocket.toRocket");

    MessageConsumer consumer = session.createConsumer(topic);
    consumer.setMessageListener(this);

    connection.start();

    producer = session.createProducer(control);
  }

  @Override
  public void onMessage(Message arg0) {
    try {
      Map m = (Map) ((ObjectMessage) arg0).getObject();
      int hh = 0;
      System.out.println("got map");
    } catch (JMSException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
