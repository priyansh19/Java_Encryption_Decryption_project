import java.io.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.Scanner;

public class aes_encryption {
	public static void main(String[] args) throws Exception {

		FileInputStream inFile = new FileInputStream("Source_File\\File_To_Encrypted.txt"); //-->file to be encrypted.
		FileOutputStream outFile = new FileOutputStream("Encrypted_files\\encryptedfile.des"); //-->file after encryption.

		Scanner sc = new Scanner(System.in);
		String x;				//-->Scanning the pass key.
		System.out.println("----------Enter the Given Password Key----------");
		x = sc.next();
		System.out.println("Result -:> "); 
		String password = x;

		//--> Key1.enc file is used for encrypting the source file.
		byte[] salt = new byte[16]; //--> Creating a array of bytes. 
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(salt); 		//--> Generating a random Secure salt key.
		FileOutputStream saltOutFile = new FileOutputStream("Encrypted_files\\Key1.enc");
		saltOutFile.write(salt); 		//--> Writing Key1 file.
		saltOutFile.close(); 

		SecretKeyFactory factory = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA1"); //--> General Secretkey generated.
		KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536,
				256);
		SecretKey secretKey = factory.generateSecret(keySpec);
		SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secret);
		AlgorithmParameters params = cipher.getParameters();

		//--> iv adds randomness to the text and makes the mechanism of encryption more secure.
		FileOutputStream ivOutFile = new FileOutputStream("Encrypted_files\\Key2.enc");
		byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV(); 	
		ivOutFile.write(iv);
		ivOutFile.close();
 		
		//--> Writing the file to be encrypted in encrypted mode.	
		byte[] input = new byte[128];
		int bytesRead;

		while ((bytesRead = inFile.read(input)) != -1) {
			byte[] output = cipher.update(input, 0, bytesRead);
			if (output != null)
				outFile.write(output);
		}

		byte[] output = cipher.doFinal();
		if (output != null)
			outFile.write(output);

		inFile.close();
		outFile.flush();
		outFile.close();

		System.out.println("File Encrypted.");
		
	}

}