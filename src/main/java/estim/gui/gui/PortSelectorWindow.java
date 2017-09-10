package estim.gui.gui;

import gnu.io.CommPortIdentifier;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import estim.gui.OutputPort;

public class PortSelectorWindow extends JFrame {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7902392867579734553L;
	
	protected final ObservableString resultObserver = new ObservableString();
	protected final JComboBox<String> portComboBox = new JComboBox<String>();
	
	protected final static boolean DEVEL_MODE = true;
	public final static String DEVELOPMENT_LOOPBACK = "DEVELOPMENT: Loopback";

	public PortSelectorWindow() {
		setTitle("Ausgabeport");
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
		
		okButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(final ActionEvent e) {
				final String portName = portComboBox.getSelectedItem().toString();
				
				resultObserver.setData(portName);
				resultObserver.notifyObservers();
								
				setVisible(false);
				dispose();
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(final ActionEvent e) {
				System.exit(0);
			}
		});
		
		final JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.add(okButton, BorderLayout.WEST);
		buttonPanel.add(cancelButton, BorderLayout.EAST);
		
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	protected void loadData() {
		final List<CommPortIdentifier> portList = OutputPort.getPortList();
		
		if(DEVEL_MODE) {
			portComboBox.addItem(DEVELOPMENT_LOOPBACK);
		}
		
		for(final CommPortIdentifier id : portList) {
			portComboBox.addItem(id.getName());
		}		
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