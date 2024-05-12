package be.kuleuven.gt.dogapp.model;

import java.util.ArrayList;
import java.util.Random;

public class EnhancedEncryption {

    private static String currentSalt;
    private static int numRounds = 5;

    private static final ArrayList<String> salts = new ArrayList<>();

    public EnhancedEncryption() {
        // Add predetermined salts to the ArrayList
        salts.add("rmssdfasdfdkf");
        salts.add("sytrykwjrkalt2");
        salts.add("salvdflkgnsdt3");
        salts.add("safasdflkjqweoilt4");
        salts.add("fjaskldfnasjksalt5");
        salts.add("saltvnxckmvcmx6");
        salts.add("savdlkfgnajekslt7");
        salts.add("savanjkfjawkefnlt8");
        salts.add("svnasdkjvnjakalt9");
        salts.add("salsdnabjksdft10");



    }



    public static String encrypt(String plaintext) {
        // Generate a random salt for each encryption
        CaesarCipher cipher = new CaesarCipher();
        Random random = new Random();
        int index = random.nextInt(salts.size());

        currentSalt = salts.get(index);
        String saltedPlaintext = plaintext + currentSalt;

        // Perform multiple encryption rounds
        String encryptedText = saltedPlaintext;
        for (int i = 0; i < numRounds; i++) {
            encryptedText = cipher.encrypt(encryptedText);
        }

        return encryptedText;
    }

    public static String decrypt(String ciphertext) {
        // Try to decrypt using all salts
        CaesarCipher cipher = new CaesarCipher();
        for (String salt : salts) {
            String decryptedText = ciphertext;
            for (int i = 0; i < numRounds; i++) {
                decryptedText = cipher.decrypt(decryptedText);
            }

            if (decryptedText.length() >= salt.length()) {
                String extractedSalt = decryptedText.substring(decryptedText.length() - salt.length());
                String extractedText = decryptedText.substring(0, decryptedText.length() - salt.length());
                if (salts.contains(extractedSalt)) {
                    // If the extracted salt matches one of the predetermined salts, return the extracted text
                    return extractedText;
                }
            }
        }

        // If no match is found, return null
        return null;
    }

}
