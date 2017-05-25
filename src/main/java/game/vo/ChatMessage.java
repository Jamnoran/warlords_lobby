package game.vo;

/**
 * Created by Eric on 2017-03-31.
 */
public class ChatMessage {
	private int id;
	private int userId;
	private String message;
	private int groupId;
	private long time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "ChatMessage{" +
				"id=" + id +
				", userId=" + userId +
				", message='" + message + '\'' +
				", groupId=" + groupId +
				", time=" + time +
				'}';
	}
}
