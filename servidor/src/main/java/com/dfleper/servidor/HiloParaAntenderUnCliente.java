package com.dfleper.servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.Base64;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HiloParaAntenderUnCliente extends Thread{
	Socket sk;

	public HiloParaAntenderUnCliente(Socket sk) {
		this.sk = sk;
	}

	@Override
	public void run() {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		DataOutputStream flujo_salida = null;
		try {
			flujo_salida = new DataOutputStream(sk.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		String lineaEscAlgo1;
		InputStream is = null;
		try {
			DataInputStream flujo_entrada = new DataInputStream(this.sk.getInputStream());
			Inet4Address ip = (Inet4Address) sk.getInetAddress();
			String laIP = ip.getHostAddress();
			System.out.println("Cliente desde la IP: " + laIP);
			while (true) {
				System.out.println("Esperando mensaje del Cliente...");
				String linea1 = flujo_entrada.readUTF();
				// Decodificar Base64 antes de mostrar ----------
				String decodedMessage = new String(Base64.getDecoder().decode(linea1));
				System.out.println("El Cliente dice: " + decodedMessage);
				System.out.println("Escribe algo: ");
				lineaEscAlgo1 = sc.nextLine();
				// Codificar a Base64 antes de enviar ----------
				String encodedMessage = Base64.getEncoder().encodeToString(lineaEscAlgo1.getBytes());
				flujo_salida.writeUTF(encodedMessage);
			}
		} catch (IOException ex) {
			System.out.println("Cliente Desconectado...");
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException ex) {
				Logger.getLogger(HiloParaAntenderUnCliente.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
