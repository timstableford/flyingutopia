package flyingutopia.gui.editor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import flyingutopia.engine.resources.ImageResource;
import flyingutopia.engine.resources.ImageResources;

public class ResourcePanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 7151966261854717601L;
	private List<JToggleButton> buttons;
	private JButton removeButton;
	private JPanel resPanel;
	public ResourcePanel() {
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
		
		c.gridy = 2;
		removeButton = new JButton("Remove");
		removeButton.setActionCommand("remove");
		removeButton.addActionListener(this);
		this.add(removeButton, c);
	}
	
	private void setup() {
		resPanel.removeAll();
		List<ImageResource> reses = ImageResources.getInstance().getResources();
		for(ImageResource re: reses) {
			JToggleButton b = new JToggleButton(new ImageIcon(re.getImage()[0].getImage()));
			b.setName(re.getName());
			b.setActionCommand(re.getName());
			b.addActionListener(this);
			buttons.add(b);
			b.setPreferredSize(new Dimension(40,40));
			resPanel.add(b);
		}
		this.revalidate();
	}
	
	public ImageResource getSelectedResource() {
		for(JToggleButton b: buttons) {
			if(b.isSelected()) {
				return ImageResources.getInstance().getResource(b.getActionCommand());
			}
		}
		return null;
	}
	
	public void saveResources() {
		String json = Editor.JSON_FORMATTER.format(ImageResources.getInstance().getJson());
		try {
			PrintWriter writer = new PrintWriter(new FileOutputStream(new File(Editor.getResourceFile())));
			writer.write(json);
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			System.err.println("Could not save file");
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() instanceof JToggleButton) {
			JToggleButton source = (JToggleButton)arg0.getSource();
			if(source.isSelected()) {
				removeButton.setText("<html>Remove<br>"+source.getName()+"</html>");
				for(JToggleButton b: buttons) {
					if(b != source) {
						b.setSelected(false);
					}
				}
			} else {
				removeButton.setText("Remove");
			}
		} else if(arg0.getActionCommand().equals("add")) {
			String file = JOptionPane.showInputDialog(null, "Enter filename",
					"Resource location", 1);
			String str = JOptionPane.showInputDialog(null, "Enter name", 
						"Resource name", 1);
			
			if(str != null && file != null) {
				ImageResource r = new ImageResource(str, file);
			
				if(r.loadImage()) {
					ImageResources.getInstance().addResource(r);
					setup();
					saveResources();
				}
			}
		} else if(arg0.getActionCommand().equals("remove")) {
			ImageResource r = this.getSelectedResource();
			if(r != null) {
				ImageResources.getInstance().removeResource(r);
				setup();
				saveResources();
			}
		}

	}
}
