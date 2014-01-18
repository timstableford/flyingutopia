package flyingutopia.engine.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.LineBorder;

import flyingutopia.engine.Resource;
import flyingutopia.engine.Resources;

public class ResourcePanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 7151966261854717601L;
	private Resources res;
	private List<JToggleButton> buttons;
	public ResourcePanel(Resources r) {
		res = r;
		this.setBorder(new LineBorder(Color.black, 2));
		buttons = new ArrayList<JToggleButton>();
		List<Resource> reses = res.getResources();
		this.setLayout(new GridLayout(4,(reses.size()+3)/4));
		for(Resource re: reses) {
			JToggleButton b = new JToggleButton(re.getImage());
			b.setActionCommand(re.getName());
			b.addActionListener(this);
			buttons.add(b);
			b.setPreferredSize(new Dimension(36,36));
			this.add(b);
		}
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
		JToggleButton source = (JToggleButton)arg0.getSource();
		if(source.isSelected()) {
			for(JToggleButton b: buttons) {
				if(b != source) {
					b.setSelected(false);
				}
			}
		}
		
	}
}
