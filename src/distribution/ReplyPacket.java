package distribution;

import java.io.Serializable;

public class ReplyPacket implements Serializable {

	private static final long serialVersionUID = 1L;
	private ReplyPacketHeader header;
	private ReplyPacketBody body;
	private Reply reply;

	public Reply getReply() {
		return reply;
	}

	public void setReply(Reply reply) {
		this.reply = reply;
	}

	public ReplyPacketHeader getHeader() {
		return header;
	}

	public void setHeader(ReplyPacketHeader header) {
		this.header = header;
	}

	public ReplyPacketBody getBody() {
		return body;
	}

	public void setBody(ReplyPacketBody body) {
		this.body = body;
	}
}
