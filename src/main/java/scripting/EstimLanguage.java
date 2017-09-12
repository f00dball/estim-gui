package scripting;

public class EstimLanguage implements Language {
	private final static Matcher newLineMatcher = new RegExMatcher("New Line", "[\r\n|\n]");
	private final static Matcher programMatcher = new RegExMatcher("Program Definition", "PROGRAM");
	private final static Matcher setMatcher = new RegExMatcher("Set Definition", "SET");
	private final static Matcher varMatcher = new RegExMatcher("Variable Definition", "VAR");
	
	
	private final static Matcher variableNameMatcher = new RegExMatcher("Variable Name", "\\$\\p{Alnum}*");
	
	
	private final static Matcher whitespaceMatcher = new RegExMatcher("Whitespace", "\\s");
	private final static Matcher unknownMatcher = new RegExMatcher("Unknown", "");
	
	private final static Matcher[] matchers = new Matcher[] {
		newLineMatcher,
		programMatcher,
		setMatcher,
		varMatcher,
		
		
		variableNameMatcher,
		
		
		whitespaceMatcher,
		unknownMatcher
	};
	
	@Override
	public Matcher[] getMatchers() {
		return matchers;
	}

	@Override
	public Matcher getErrorMatcher() {
		return unknownMatcher;
	}

	@Override
	public Matcher getNewLineMatcher() {
		return newLineMatcher;
	}
}
