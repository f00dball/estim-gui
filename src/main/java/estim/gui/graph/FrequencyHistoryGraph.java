package estim.gui.graph;

import java.util.Date;

import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;

import estim.gui.AppCtx;
import estim.gui.OutputHistory;

public class FrequencyHistoryGraph extends AbstractGraph {
	
	protected void calculateDataSeries() {
      
        seriesOut = new TimeSeries("Historie");

		final OutputHistory history = AppCtx.getInstance().getControllerWorker().getFrequencyHistory();
		for(final Date date : history.getHistory().keySet()) {
			final int value = history.getHistory().get(date);
	        handleNewTimeValue(value);
	        seriesOut.addOrUpdate(new Millisecond(date), value);
		}
	}

	@Override
	protected boolean getLineVisibleInLegend() {
		return false;
	}
	
	@Override
	protected String getGraphTitle() {
		return "Frequenz History";
	}

	@Override
	protected String getGraphXAxisLegend() {
		return "Zeit";
	}

	@Override
	protected String getGraphYAxisLegend() {
		return "Frequenz";
	}
}
