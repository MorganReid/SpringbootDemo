import com.example.demo.PgpDecryptionUtil;
import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

/**
 * @author junhu
 * @date 2024/2/27
 */
public class clientReceive {

    private static final URL privateKey = loadResource("/PRIVATE_KEY_2048.asc");
    private static final String passkey = "";

    private static URL loadResource(String resourcePath) {
        return Optional.ofNullable(clientReceive.class.getResource(resourcePath))
                .orElseThrow(() -> new IllegalArgumentException("Resource not found"));
    }

    public static void main(String[] args) throws Exception {
        PgpDecryptionUtil pgpDecryptionUtil = new PgpDecryptionUtil(privateKey.openStream(), passkey);

        while (true) {
            final Mqtt5BlockingClient client = Mqtt5Client.builder()
                    .identifier(UUID.randomUUID().toString())
                    .serverHost("broker.hivemq.com")
                    .buildBlocking();

            client.connect();

            try (Mqtt5BlockingClient.Mqtt5Publishes publishes = client.publishes(MqttGlobalPublishFilter.ALL)) {


                client.subscribeWith().topicFilter("test/topic").qos(MqttQos.AT_LEAST_ONCE).send();

                System.out.println(new String(publishes.receive().getPayloadAsBytes(), StandardCharsets.UTF_8));
                byte[] decrypt = pgpDecryptionUtil.decrypt(publishes.receive().getPayloadAsBytes());

                String decryptString = new String(decrypt, Charset.defaultCharset());
                System.out.println("解密后数据" + decryptString);

            }

        }
    }

}
