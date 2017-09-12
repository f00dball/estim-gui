package scripting;

public class Token {
	private final String type;
	private final String content;
	private final int position;
	private final int length;
	private final int column;
	private final int line;
	
	public Token(String type, String content, int position, int length, int column, int line) {
		this.type = type;
		this.content = content;
		this.position = position;
		this.length = length;
		this.column = column;
		this.line = line;
	}
	
	public String getType() {
		return type;
	}
	
	public String getContent() {
		return content;
	}
	
	public int getPosition() {
		return position;
	}
	
	public int getLength() {
		return length;
	}
	
	public int getColumn() {
		return column;
	}
	
	public int getLine() {
		return line;
	}
}
