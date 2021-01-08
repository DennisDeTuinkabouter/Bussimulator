package infoborden;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public  class ListenerStarter implements Runnable, ExceptionListener {
	private String selector="";
	private Infobord infobord;
	private Berichten berichten;
	
	public ListenerStarter() {
	}
	
	public ListenerStarter(String selector, Infobord infobord, Berichten berichten) {
		this.selector=selector;
		this.infobord=infobord;
		this.berichten=berichten;
	}

	public void run() {
        try {
            ActiveMQConnectionFactory connectionFactory = 
            		new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
			Connection connection = connectionFactory.createTopicConnection();
			connection.start();
			connection.setExceptionListener(this);
			Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
			Topic destination = session.createTopic("Infobord");
			MessageConsumer consumer = session.createConsumer(destination, selector);
			System.out.println("Produce, wait, consume"+ selector);
			QueueListener queueListener = new QueueListener(selector, infobord, berichten);
			queueListener.onMessage(consumer.receive());
			//consumer.setMessageListener(queueListener);

//			TODO maak de connection aan
//          Connection connection = ?????;
//          connection.start();
//          connection.setExceptionListener(this);
//			TODO maak de session aan
//          Session session = ?????;
//			TODO maak de destination aan
//          Destination destination = ?????;
//			TODO maak de consumer aan
//          MessageConsumer consumer = ?????;
//			TODO maak de Listener aan
//          consumer.?????;
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

    public synchronized void onException(JMSException ex) {
        System.out.println("JMS Exception occured.  Shutting down client.");
    }
}