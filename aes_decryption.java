import java.io.*;
import java.security.spec.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.Scanner;

public class aes_decryption {
	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("----------Enter Password Key To Decrypt The Data.----------");
		
		String x;
		x=sc.next();
		String password = x;
		//--> Reading the Key1 and Key2 files from the folder specified.
		FileInputStream saltFis = new FileInputStream("Encrypted_files\\Key1.enc");
		byte[] salt = new byte[16];
		saltFis.read(salt);
		saltFis.close();

		FileInputStream ivFis = new FileInputStream("Encrypted_files\\Key2.enc");
		byte[] iv = new byte[16];
		ivFis.read(iv);
		ivFis.close();

		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"); //--> same sha key used at the time of encrytion.
		KeySpec keySpec1 = new PBEKeySpec(password.toCharArray(), salt, 65536,256);
		SecretKey tmp = factory.generateSecret(keySpec1);
		SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
		FileInputStream fis = new FileInputStream("Encrypted_files\\encryptedfile.des");
		FileOutputStream fos = new FileOutputStream("Source_File_After_Decryption\\Sourcefile_decrypted.txt");
		byte[] in = new byte[128];
		int read;
		//--> Decrypting the file and writing it into the folder specified.
		while ((read = fis.read(in)) != -1) {
			byte[] output = cipher.update(in, 0, read);
			if (output != null)
				fos.write(output);
		}

		byte[] output = cipher.doFinal();
		if (output != null)
			fos.write(output); //--> Writing the final text back into the file.
		fis.close();
		fos.flush();
		fos.close();
		System.out.println("File Decrypted.");
	}
}