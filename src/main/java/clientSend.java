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
    private static final URL testFile = loadResource("/RSA.pem");
    private static final URL testImage = loadResource("/picture.jpeg");
    private static final URL privateKey = loadResource("/PRIVATE_KEY_2048.asc");
    private static final String passkey = "";

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
        File originalImage = new File(testImage.toURI());
        ByteArrayOutputStream outputStreamImage = new ByteArrayOutputStream();
        pgpEncryptionUtil.encrypt(outputStreamImage, Files.newInputStream(originalImage.toPath()), originalImage.length(),
                publicKey.openStream());
        byte[] encryptedImageBytes = outputStreamImage.toByteArray();

        // Encrypting the Image,本地测试可以过,但是网络传输时(665k测试)比较卡
//        PgpDecryptionUtil pgpDecryptionUtil = new PgpDecryptionUtil(privateKey.openStream(), passkey);
//        ByteArrayInputStream encryptedIn = new ByteArrayInputStream(encryptedImageBytes);
//        FileOutputStream fileOutputStream = new FileOutputStream("/home/junhu/temp/decrypt");
//        pgpDecryptionUtil.decrypt(encryptedIn, fileOutputStream);

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
