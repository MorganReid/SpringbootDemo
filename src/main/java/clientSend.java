import com.example.demo.PgpDecryptionUtil;
import com.example.demo.PgpEncryptionUtil;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import org.bouncycastle.bcpg.CompressionAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.openpgp.PGPException;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.UUID;

/**
 * @author junhu
 * @date 2024/2/27
 */
public class clientSend {

    public static void main(String[] args) throws PGPException, IOException {
        testByteEncryption();
    }

    private static final URL publicKey = loadResource("/PUBLIC_KEY_2048.asc");
    private static final URL privateKey = loadResource("/PRIVATE_KEY_2048.asc");
    private static final String passkey = "";

    private static URL loadResource(String resourcePath) {
        return Optional.ofNullable(clientSend.class.getResource(resourcePath))
                .orElseThrow(() -> new IllegalArgumentException("Resource not found"));
    }

    public static void testByteEncryption() throws IOException, PGPException {

        PgpEncryptionUtil pgpEncryptionUtil = PgpEncryptionUtil.builder()
                .armor(true)
                .compressionAlgorithm(CompressionAlgorithmTags.ZIP)
                .symmetricKeyAlgorithm(SymmetricKeyAlgorithmTags.AES_128)
                .withIntegrityCheck(true)
                .build();

        // Encrypting the test bytes
        byte[] encryptedBytes = pgpEncryptionUtil.encrypt("testString".getBytes(Charset.defaultCharset()),
                publicKey.openStream());

        String s = new String(encryptedBytes, Charset.defaultCharset());
        System.out.println(s);

        //发送
        Mqtt5BlockingClient client = Mqtt5Client.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost("broker.hivemq.com")
                .buildBlocking();

        client.connect();
        client.publishWith().topic("test/topic").qos(MqttQos.AT_LEAST_ONCE).payload(encryptedBytes).send();
        client.disconnect();
        System.out.println("11");

        //本地验证解密
        PgpDecryptionUtil pgpDecryptionUtil = new PgpDecryptionUtil(privateKey.openStream(), passkey);
        byte[] decrypt = pgpDecryptionUtil.decrypt(encryptedBytes);
        String decryptString = new String(decrypt, Charset.defaultCharset());
        System.out.println("解密后数据" + decryptString);

    }
}
