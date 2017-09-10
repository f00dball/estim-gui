package estim.gui;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OutputHistory {
	protected Map<Date, Integer> history = new ConcurrentHashMap<Date, Integer>();
	
	public Map<Date, Integer> getHistory() {
		return history;
	}
	
	public void addValue(int value) {
		for(final Date date : history.keySet()) {
			if(date.before(new Date(System.currentTimeMillis() - (60 * 1000)))) {
				history.remove(date);
			}
  		}
		history.put(new Date(), value);
	}
}
