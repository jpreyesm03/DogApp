package be.kuleuven.gt.dogapp.model;

public class CaesarCipher {

    private static final int SHIFT = 3; // You can adjust the shift value as needed

    // Encrypt plaintext password
    public static String encrypt(String plaintext) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : plaintext.toCharArray()) {
            if (Character.isLetter(c)) {
                char shifted = (char) (c + SHIFT);
                if ((Character.isUpperCase(c) && shifted > 'Z') ||
                        (Character.isLowerCase(c) && shifted > 'z')) {
                    shifted -= 26; // Wrap around if necessary
                }
                encrypted.append(shifted);
            } else {
                encrypted.append(c); // Keep non-letter characters unchanged
            }
        }
        return encrypted.toString();
    }

    // Decrypt encrypted password
    public static String decrypt(String encrypted) {
        StringBuilder decrypted = new StringBuilder();
        for (char c : encrypted.toCharArray()) {
            if (Character.isLetter(c)) {
                char shifted = (char) (c - SHIFT);
                if ((Character.isUpperCase(c) && shifted < 'A') ||
                        (Character.isLowerCase(c) && shifted < 'a')) {
                    shifted += 26; // Wrap around if necessary
                }
                decrypted.append(shifted);
            } else {
                decrypted.append(c); // Keep non-letter characters unchanged
            }
        }
        return decrypted.toString();
    }
}