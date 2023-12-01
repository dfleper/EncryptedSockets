package com.dfleper.cliente;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Base64;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class App {

	final static int PORT = 40080;
	final static String HOST = "localhost";
	
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
		
		try {
			Socket sk = new Socket(HOST, PORT);
			enviarMensajesAlServidor(sk);
		} catch (IOException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private static void enviarMensajesAlServidor(Socket sk) {
		OutputStream os = null;
		try {
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);
			DataInputStream flujo_entrada = new DataInputStream(sk.getInputStream());
			DataOutputStream flujo_salida = new DataOutputStream(sk.getOutputStream());
			String lineaEscAlgo1;
			String lineaResp1;
			while (true) {
				System.out.println("Escribe algo: ");
				lineaEscAlgo1 = sc.nextLine();
				// Codificar a Base64 antes de enviar ----------
				String encodedMessage = Base64.getEncoder().encodeToString(lineaEscAlgo1.getBytes());
				flujo_salida.writeUTF(encodedMessage);
				System.out.println("Esperando mensaje del Servidor...");
				lineaResp1 = flujo_entrada.readUTF();
				// Decodificar Base64 antes de mostrar ----------
				String decodedMessage = new String(Base64.getDecoder().decode(lineaResp1));
				System.out.println("El Servidor dice: " + decodedMessage);
			}
		} catch (IOException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				if (os != null)
					os.close();
			} catch (IOException ex) {
				Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
