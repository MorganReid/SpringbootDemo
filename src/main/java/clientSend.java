import com.example.demo.PgpEncryptionUtil;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import org.bouncycastle.bcpg.CompressionAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

/**
 * @author junhu
 * @date 2024/2/27
 */
public class clientSend {

    private static final URL publicKey = loadResource("/PUBLIC_KEY_2048.asc");
    private static final URL testFile = loadResource("/test.txt");
    private static final URL testImage = loadResource("/picture.jpeg");


    public static void main(String[] args) throws Exception {
        testByteEncryption();
    }

    private static URL loadResource(String resourcePath) {
        return Optional.ofNullable(clientSend.class.getResource(resourcePath))
                .orElseThrow(() -> new IllegalArgumentException("Resource not found"));
    }

    public static void testByteEncryption() throws Exception {

        PgpEncryptionUtil pgpEncryptionUtil = PgpEncryptionUtil.builder()
                .armor(true)
                .compressionAlgorithm(CompressionAlgorithmTags.ZIP)
                .symmetricKeyAlgorithm(SymmetricKeyAlgorithmTags.AES_128)
                .withIntegrityCheck(true)
                .build();

        // Encrypting the String
        byte[] encryptedStringBytes = pgpEncryptionUtil.encrypt("0227test123".getBytes(Charset.defaultCharset()),
                publicKey.openStream());

        // Encrypting the File
        File originalFile = new File(testFile.toURI());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        pgpEncryptionUtil.encrypt(outputStream, Files.newInputStream(originalFile.toPath()), originalFile.length(),
                publicKey.openStream());
        byte[] encryptedFileBytes = outputStream.toByteArray();


        // Encrypting the Image
        File originalImage = new File(testFile.toURI());
        ByteArrayOutputStream outputStreamImage = new ByteArrayOutputStream();
        pgpEncryptionUtil.encrypt(outputStreamImage, Files.newInputStream(originalImage.toPath()), originalImage.length(),
                publicKey.openStream());
        byte[] encryptedImageBytes = outputStreamImage.toByteArray();


        //发送
        Mqtt5BlockingClient client = Mqtt5Client.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost("broker.hivemq.com")
                .buildBlocking();

        client.connect();
        client.publishWith().topic("test/topic098").qos(MqttQos.AT_LEAST_ONCE).payload(encryptedFileBytes).send();
        client.disconnect();
        System.out.println("完成加密数据发送");


    }
}
