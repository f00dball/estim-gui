package estim.gui.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;

import estim.gui.graph.AudioHistoryGraph;
import estim.gui.graph.FrequencyHistoryGraph;
import estim.gui.graph.PowerHistoryGraph;

public class HistoryWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8012050360257017067L;
	private ChartPanel powerChartPanel;
	private PowerHistoryGraph powerHistoryGraph;
	private FrequencyHistoryGraph frequencyHistoryGraph;
	private ChartPanel frequencyChartPanel;
	private AudioHistoryGraph audioHistoryGraph;
	private ChartPanel audioChartPanel;
	
	public HistoryWindow() {
		setTitle("History");
		
		final JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		
		powerHistoryGraph = new PowerHistoryGraph();
        powerChartPanel = new ChartPanel(powerHistoryGraph.getChart());
        powerChartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        powerChartPanel.setMouseZoomable(false);
        powerChartPanel.setMouseWheelEnabled(false);
        
        frequencyHistoryGraph = new FrequencyHistoryGraph();
        frequencyChartPanel = new ChartPanel(frequencyHistoryGraph.getChart());
        frequencyChartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        frequencyChartPanel.setMouseZoomable(false);
        frequencyChartPanel.setMouseWheelEnabled(false);
        
        audioHistoryGraph = new AudioHistoryGraph();
        audioChartPanel = new ChartPanel(audioHistoryGraph.getChart());
        audioChartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        audioChartPanel.setMouseZoomable(false);
        audioChartPanel.setMouseWheelEnabled(false);

        
        final JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(powerChartPanel, BorderLayout.NORTH);
        northPanel.add(frequencyChartPanel, BorderLayout.CENTER);
        northPanel.add(audioChartPanel, BorderLayout.SOUTH);

        final JPanel southPanel = new JPanel();
        southPanel.add(closeButton);
        
		add(northPanel, BorderLayout.NORTH);
		add(southPanel, BorderLayout.SOUTH);
		
	}
	
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
	
		if(b) {
			final UpdateThread updateThread = new UpdateThread();
			updateThread.start();
			updateThread.setPriority(Thread.MIN_PRIORITY);
		}
	}
	
	
	class UpdateThread extends Thread {
		@Override
		public void run() {
			while(isVisible()) {
				powerChartPanel.setChart(powerHistoryGraph.getChart());	
				Thread.yield();
				frequencyChartPanel.setChart(frequencyHistoryGraph.getChart());
				Thread.yield();
				audioChartPanel.setChart(audioHistoryGraph.getChart());
				Thread.yield();

		        try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}
	
}
