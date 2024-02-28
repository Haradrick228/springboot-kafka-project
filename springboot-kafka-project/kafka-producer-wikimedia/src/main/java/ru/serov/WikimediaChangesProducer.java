package ru.serov;

import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.background.BackgroundEventHandler;
import com.launchdarkly.eventsource.background.BackgroundEventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Service
public class WikimediaChangesProducer {

    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public WikimediaChangesProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage() throws InterruptedException {

        String topic = "wikimedia_recentchange";
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";

        //to read real time stream data from wikimedia, we use event source
        BackgroundEventHandler eventHandler =  new WikimediaChangesHandler(kafkaTemplate, topic);

        URI uriUrl = URI.create(url);
        EventSource.Builder esBuilder = new EventSource.Builder(uriUrl);
        BackgroundEventSource.Builder eventSource = new BackgroundEventSource.Builder(eventHandler, esBuilder);
        BackgroundEventSource source = eventSource.build();
        source.start();

        TimeUnit.MINUTES.sleep(10);

    }

}
