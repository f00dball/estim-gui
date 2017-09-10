package estim.gui.graph;

import java.util.Date;

import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;

import estim.gui.AppCtx;
import estim.gui.OutputHistory;


public class PowerHistoryGraph extends AbstractGraph {

	@Override
	protected void calculateDataSeries() {
      
        seriesOut = new TimeSeries("Historie");

		final OutputHistory powerHistory = AppCtx.getInstance().getControllerWorker().getPowerHistory();
		for(final Date date : powerHistory.getHistory().keySet()) {
			final int value = powerHistory.getHistory().get(date);
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
		return "Power History";
	}

	@Override
	protected String getGraphXAxisLegend() {
		return "Zeit";
	}

	@Override
	protected String getGraphYAxisLegend() {
		return "Power";
	}
	
	

}
