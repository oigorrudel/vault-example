package br.xksoberbado.vaultexample;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.vault.core.VaultKeyValueOperationsSupport.KeyValueBackend;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultMount;

@Slf4j
@SpringBootApplication
public class VaultExampleApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(VaultExampleApplication.class, args);
    }

    @Autowired
    private VaultTemplate vaultTemplate;

    @Override
    @SneakyThrows
    public void run(final String... args) {
        final var path = "secret";
        final var application = "github";

//        final var vaultResponse = vaultTemplate.opsForKeyValue(path, KeyValueBackend.KV_2)
//            .get(application);

        final var transitOperations = vaultTemplate.opsForTransit();
        final var sysOperations = vaultTemplate.opsForSys();

        final var encryptionKeyName = "my-custom-encryption";
        if (!sysOperations.getMounts().containsKey("transit/")) {
            sysOperations.mount("transit", VaultMount.create("transit"));
            transitOperations.createKey(encryptionKeyName);
        }

        final var textToEncrypt = "Secure message";
        final var ciphertext = transitOperations.encrypt(encryptionKeyName, textToEncrypt);
        final var decryptedText = transitOperations.decrypt(encryptionKeyName, ciphertext);

//        final var keyKV = "github.oauth2.key";
//        final var valueKV = vaultResponse.getData().get(keyKV);

//        log.info(
//            "\nPath: {}\nApplication: {}\nKey KV: {}\nValue KV: {}\nEncryption key name: {}\nEncrypted: {}\nDecrypted: {}",
//            path, application, keyKV, valueKV, encryptionKeyName, ciphertext, decryptedText
//        );

        log.info(
            "\nPath: {}\nApplication: {}\nEncryption key name: {}\nEncrypted: {}\nDecrypted: {}",
            path, application, encryptionKeyName, ciphertext, decryptedText
        );
    }
}
