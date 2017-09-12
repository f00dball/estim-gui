package estim.scripting;

public class SourceInfo {
	private final String content;
	
	private int column;
	private int offset;
	private int line;
	
	public SourceInfo(String content) {
		this.content = content;
		this.column = 0;
		this.offset = 0;
		this.line = 0;
	}
	
	public String getContent() {
		return content;
	}
	
	public int getLength() {
		return content.length();
	}
	
	public int getOffset() {
		return offset;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public int getColumn() {
		return column;
	}
	
	public void setColumn(int column) {
		this.column = column;
	}
	
	public int getLine() {
		return line;
	}
	
	public void setLine(int line) {
		this.line = line;
	}
	
	public boolean hasNext() {
		return this.getOffset() < this.getLength();
	}
}
