import com.example.demo.PgpDecryptionUtil;
import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.net.URL;
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


                System.out.println("开始加密数据接受");
                client.subscribeWith().topicFilter("test/topic098").qos(MqttQos.AT_LEAST_ONCE).send();
                byte[] payloadAsBytes = publishes.receive().getPayloadAsBytes();
                System.out.println("完成加密数据接受");

                System.out.println("开始解密");

                //解密为string
//                byte[] decrypt = pgpDecryptionUtil.decrypt(payloadAsBytes);
//                String decryptString = new String(decrypt, Charset.defaultCharset());
//                System.out.println("完成解密" + decryptString);

                //解密为file
                ByteArrayInputStream encryptedIn = new ByteArrayInputStream(payloadAsBytes);
                FileOutputStream fileOutputStream = new FileOutputStream("/home/junhu/temp/decrypt");
                pgpDecryptionUtil.decrypt(encryptedIn, fileOutputStream);

                System.out.println("完成解密");


                //解密为image

            }

        }
    }

}
