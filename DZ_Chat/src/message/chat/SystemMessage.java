package message.chat;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import core.server.Server;

public class SystemMessage extends Message implements Serializable {
	private static final long serialVersionUID = 7033598494494691135L;
	private final String message;
	
	public SystemMessage(ChatRoom chatRoom, String message) {
		super(chatRoom);
		this.message = message;
	}
	
	@Override
	public void send(OutputStream os) throws IOException {
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(os));
		dos.writeUTF(message);
		dos.flush();
		dos.close();
	}
	
	@Override
	public void push() {
		System.out.println(this);
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
				.append("\t[System")
				.append("> ")
				.append(message)
				.append("]\t")
				.toString();
	}
}
