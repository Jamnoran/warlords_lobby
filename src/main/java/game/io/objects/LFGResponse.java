package game.io.objects;

import game.vo.LFG;

/**
 * Created by Eric on 2017-03-10.
 */
public class LFGResponse {

	private String response_type = "LFG_RESPONSE";
	public LFG lfg;

	public LFGResponse(){
	}

	public LFGResponse(LFG lfg) {
		this.lfg = lfg;
	}

	public LFG getLfg() {
		return lfg;
	}

	public void setLfg(LFG lfg) {
		this.lfg = lfg;
	}

	@Override
	public String toString() {
		return "LFGResponse{" +
				"response_type='" + response_type + '\'' +
				", lfg=" + lfg +
				'}';
	}
}
