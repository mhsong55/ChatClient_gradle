package chatClient;

public class MsgInfo {
	private String nickName;
	private String message;
	private String time;
	
	public MsgInfo(String nickName, String message, String time) {
		this.nickName = nickName;
		this.message = message;
		this.time = time;
	}

	@Override
	public String toString() {
		return "MsgInfo [nickName=" + nickName + ", message=" + message + ", DateTime=" + time + "]";
	}
}