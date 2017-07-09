package at.bgoeschi.pogoraidar;


public class PogoRaidarTweet {

	private String userName;
	private String place;
	private String message;

	public PogoRaidarTweet(String userName, String place, String message) {
		this.userName = userName;
		this.place = place;
		this.message = message;
	}

	public String getUserName() {
		return userName;
	}

	public String getPlace() {
		return place;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "PogoRaidarTweet{" +
				"userName='" + userName + '\'' +
				", place='" + place + '\'' +
				", message='" + message + '\'' +
				'}';
	}
}
