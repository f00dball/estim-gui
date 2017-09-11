package estim.gui.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import estim.gui.Const;
import gnu.io.CommPortIdentifier;

public class PortSelectorWindow extends JFrame {

	private static final long serialVersionUID = 7902392867579734553L;
	
	protected final ObservableString resultObserver = new ObservableString();
	
	protected final JComboBox<String> portComboBox = new JComboBox<>();
		
	public final static String DEVELOPMENT_LOOPBACK = "DEVELOPMENT: Loopback";

	public PortSelectorWindow() {
		setTitle("Output Port");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JLabel("Port:"), BorderLayout.WEST);
		getContentPane().add(portComboBox, BorderLayout.CENTER);
		
		setupButtons();
		
		loadData();
		
		pack();
		setLocationRelativeTo(null);
	}
	
	protected void setupButtons() {
		final JButton okButton = new JButton("Select");
		final JButton cancelButton = new JButton("Close");
		
		okButton.addActionListener((e) -> {
				final String portName = portComboBox.getSelectedItem().toString();
				
				resultObserver.setData(portName);
				resultObserver.notifyObservers();
								
				setVisible(false);
				dispose();
			}
		);
		
		cancelButton.addActionListener((e) -> {
				System.exit(0);
			}
		);
		
		final JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.add(okButton, BorderLayout.WEST);
		buttonPanel.add(cancelButton, BorderLayout.EAST);
		
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public static List<CommPortIdentifier> getPortList() {
		final List<CommPortIdentifier> resultList = new ArrayList<>();
		@SuppressWarnings("rawtypes")
		final Enumeration portList = CommPortIdentifier.getPortIdentifiers();

		while (portList.hasMoreElements()) {
			final CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();

			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				resultList.add(portId);
			}
		}
		
		return resultList;
	}

	protected void loadData() {
		final List<CommPortIdentifier> portList = getPortList();
		
		if(Const.DEVELOPMENT_MODE) {
			portComboBox.addItem(DEVELOPMENT_LOOPBACK);
		}
		
		portList.forEach((i) -> portComboBox.addItem(i.getName()));
	}
	
	public ObservableString getResultObserver() {
		return resultObserver;
	}

	//================
	// Test
	//================
	public static void main(final String[] args) {
		final PortSelectorWindow portSelectorWindow = new PortSelectorWindow();
		
		portSelectorWindow.getResultObserver().addObserver(new Observer() {
			
			public void update(final Observable o, final Object arg) {
				if(o instanceof ObservableString) {
					final ObservableString observableString = (ObservableString) o;
					System.out.println("Called : " + observableString.getData());
				}
			}
		});
		
		portSelectorWindow.setVisible(true);
	}
}