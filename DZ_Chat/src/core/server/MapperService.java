package core.server;

import java.io.*;
import java.net.Socket;

import core.mapper.Command;

public class MapperService extends ObjectStreamService {
	public MapperService(ObjectInputStream is, ObjectOutputStream os) throws IOException {
		super(is, os);
	}

	@Override
	public void request() throws IOException {
		System.out.println("Mapper Service");
		try {
			Command cmd = (Command) is.readObject();
			
			System.out.println("Receive Command: "+ cmd.getCommandType());
			ObjectStreamService mapping = cmd.response(is, os);
			if (mapping == null) System.out.println("Mapping Fail");
			else System.out.println("Mapping");
			mapping.request();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

}
