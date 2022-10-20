package core.service.chat;

import java.io.*;
import java.util.Objects;

import core.server.MainServer;
import core.service.ObjectStreamService;
import log.Log;
import log.LogQueue;
import log.NeedLog;
import member.Member;
import message.chat.ChatRoom;
import message.chat.Message;

public class ChatService extends ObjectStreamService implements NeedLog {
	private final Member me;
	private final String chatRoomName;
	private final ChatRoom chatRoom;
//	private NeedLog needLog = new NeedLog();
	private LogQueue logQueue = LogQueue.getInstance();
	public ChatService(ObjectInputStream is, ObjectOutputStream os, Object... args) throws IOException {
		super(is, os);
		this.chatRoomName = (String) args[0];
		this.me = (Member) args[1];
		this.chatRoom = MainServer.chatRoomMap.get(chatRoomName);
	}

	@Override
	public void request() throws IOException {
		System.out.println("Chat Service");
		chatRoom.entrance(this);
		MainServer.threadPool.execute(() -> {
			try {
				while (true) {
					Message message = (Message) is.readObject();
					message.setChatRoom(chatRoom);
					message.push();
					System.out.println("[Server]" + message);
					logQueue.add(message); //LogQueue
				}
			} catch (IOException e) {
				chatRoom.exit(this);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		});
	}
	
	@Override
	public Log toLog() {
		return null;
	}
	
	public Member getMe() {
		return me;
	}
	
	public String nickname() {
		return me.nickname();
	}
	
	public ObjectOutputStream getOs() {
		return os;
	}
	
	public boolean equalsUser(String id) {
		return me.getUserId().equals(id);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(chatRoomName, me);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChatService other = (ChatService) obj;
		return Objects.equals(chatRoomName, other.chatRoomName) && Objects.equals(me, other.me);
	}



}

