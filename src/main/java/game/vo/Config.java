package game.vo;

/**
 * Created by Eric on 2017-03-09.
 */
public class Config {
	private Integer port;

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "Config{" +
				"port=" + port +
				'}';
	}
}
