package estim.scripting;

import java.util.Stack;

public class Tokenizer {
	private final Language language;
	
	private SourceInfo source;
	private Stack<SourceInfo> sources;
	private int position;
	
	public Tokenizer(Language language, SourceInfo source) {
		this.language = language;
		this.source = source;
		this.position = 0;
		
		this.sources = new Stack<SourceInfo>();
	}
	
	public int getPosition() {
		return position;
	}
	
	public boolean hasNext() {
		return sources.peek().hasNext();
	}
	
	public Token getNext() {
		Token next = buildToken();
		
		if (next == null) {
			return null;
		}
		
		source.setOffset(source.getOffset() + next.getLength());
		position++;
		
		if (next.getType() == language.getNewLineMatcher().getName()) {
			source.setLine(source.getLine() + 1);
			source.setColumn(0);
		} else {
			source.setColumn(source.getColumn() + next.getLength());
		}
		
		reduce();
		
		return next;
	}
	
	public Token peek() {
		return buildToken();
	}
	
	public void discardNext() {
		Token next = buildToken();
		
		if (next != null) {
			source.setOffset(source.getOffset() + next.getLength());
			position++;
			
			if (next.getType() == language.getNewLineMatcher().getName()) {
				source.setLine(source.getLine() + 1);
				source.setColumn(0);
			} else {
				source.setColumn(source.getColumn() + next.getLength());
			}
			
			reduce();
		}
	}
	
	public void reset() {
		while (sources.size() > 0) {
			source = sources.pop();
		}
		
		source.setOffset(0);
		source.setColumn(0);
		source.setLine(0);
		
		position = 0;
	}
	
	public void expand(SourceInfo item) {
		if (source == null || source.getContent() == "") {
			return;
		}
		
		sources.push(source);
		source = item;
	}
	
	private Token buildToken() {
		if (!this.hasNext()) {
			return null;
		}
		
		MatchInfo mi = null;
		
		for (Matcher matcher : language.getMatchers()) {
			mi = matcher.match(source.getContent(), source.getOffset());
			
			if (mi.isSuccess()) {
				return new Token(matcher.getName(), mi.getContent(), mi.getLength(), source.getOffset(), source.getColumn(), source.getLine());
			}
		}
		
		return new Token(language.getErrorMatcher().getName(), source.getContent().substring(source.getOffset(), source.getOffset() + 1), 1, source.getOffset(), source.getColumn(), source.getLine());
	}
	
	private void reduce() {
		while (source.getOffset() >= source.getLength() && sources.size() > 0) {
			source = sources.pop();
		}
	}
}
