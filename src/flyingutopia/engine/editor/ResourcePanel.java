package flyingutopia.engine.editor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import flyingutopia.engine.Resource;
import flyingutopia.engine.Resources;

public class ResourcePanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 7151966261854717601L;
	private Resources res;
	private List<JToggleButton> buttons;
	private JPanel resPanel;
	public ResourcePanel(Resources r) {
		res = r;
		buttons = new ArrayList<JToggleButton>();
		resPanel = new JPanel();
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTH;
		c.weightx = 1;
		resPanel.setLayout(new GridLayout(0, 4));
		c.weighty = 1;
		setup();
		this.add(resPanel, c);

		c.gridy = 1;
		c.weighty = 0;
		JButton addRes = new JButton("Add Resource");
		addRes.setActionCommand("add");
		addRes.addActionListener(this);
		this.add(addRes, c);
	}
	
	private void setup() {
		resPanel.removeAll();
		List<Resource> reses = res.getResources();
		for(Resource re: reses) {
			JToggleButton b = new JToggleButton(re.getImage());
			b.setActionCommand(re.getName());
			b.addActionListener(this);
			buttons.add(b);
			b.setPreferredSize(new Dimension(40,40));
			resPanel.add(b);
		}
		this.revalidate();
	}
	
	public Resource getSelectedResource() {
		for(JToggleButton b: buttons) {
			if(b.isSelected()) {
				return res.getResource(b.getActionCommand());
			}
		}
		return null;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() instanceof JToggleButton) {
			JToggleButton source = (JToggleButton)arg0.getSource();
			if(source.isSelected()) {
				for(JToggleButton b: buttons) {
					if(b != source) {
						b.setSelected(false);
					}
				}
			}
		} else if(arg0.getActionCommand().equals("add")) {
			String file = JOptionPane.showInputDialog(null, "Enter filename",
					"Resource location", 1);
			String str = JOptionPane.showInputDialog(null, "Enter name", 
						"Resource name", 1);
			
			if(str != null && file != null) {
				Resource r = new Resource(str, file);
			
				if(r.loadImage()) {
					res.addResource(r);
					setup();
					res.save();
				}
			}
		}

	}
}