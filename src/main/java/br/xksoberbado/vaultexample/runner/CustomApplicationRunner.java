package br.xksoberbado.vaultexample.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultMount;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomApplicationRunner implements CommandLineRunner {

    private final VaultTemplate vaultTemplate;


    @Override
    public void run(final String... args) throws Exception {
        final var path = "secret";
        final var application = "github";


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


        log.info(
            "\nPath: {}\nApplication: {}\nEncryption key name: {}\nEncrypted: {}\nDecrypted: {}",
            path, application, encryptionKeyName, ciphertext, decryptedText
        );

//        final var vaultResponse = vaultTemplate.opsForKeyValue(path, KeyValueBackend.KV_2)
//            .get(application);
//        final var keyKV = "github.oauth2.key";
//        final var valueKV = vaultResponse.getData().get(keyKV);

//        log.info(
//            "\nPath: {}\nApplication: {}\nKey KV: {}\nValue KV: {}\nEncryption key name: {}\nEncrypted: {}\nDecrypted: {}",
//            path, application, keyKV, valueKV, encryptionKeyName, ciphertext, decryptedText
//        );
    }
}
