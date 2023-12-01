package com.dfleper.servidor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
		
		final int PORT = 40080;
		try {
			@SuppressWarnings("resource")
			ServerSocket sk = new ServerSocket(PORT);
			System.out.println("Servidor inicializado; Esperando Clientes por el puerto 40080...");
			while (true) {
				Socket socket = sk.accept();
				System.out.println("Cliente Conectado...");
				HiloParaAntenderUnCliente hilo = new HiloParaAntenderUnCliente(socket);
				hilo.start();
			}
		} catch (IOException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
