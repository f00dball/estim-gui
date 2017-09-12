package scripting;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class RegExMatcher implements Matcher {
	private final String name;
	private final Pattern pattern;
	
	public RegExMatcher(String name, String regex) {
		this.name = name;
		this.pattern = Pattern.compile(regex);
	}	
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isMatch(String input, int offset) {
		return pattern.matcher(input).find(offset);
	}

	@Override
	public MatchInfo match(String input, int offset) {
		java.util.regex.Matcher matcher = pattern.matcher(input);
		
		int length = 0;
		String content = "";
		boolean success = matcher.find(offset);
		
		if (success) {
			MatchResult result = matcher.toMatchResult();
			
			length = result.end() - result.start();
			content = input.substring(result.start(), result.end());
		}
		
		return new MatchInfo(content, success, length);
	}
}
