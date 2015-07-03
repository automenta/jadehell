/*
 * ManageMTPsDialog_.java
 *
 * Created on October 7, 2001, 6:24 PM
 */

package jade.tools.rma;

import java.awt.Frame;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author  rimassa
 */
public class ManageMTPsDialog extends JDialog {

    private static final Object[] EMPTY_LIST = new Object[0];


    private AbstractAction addMTPAction = new AbstractAction("Add MTP...") {

      public void actionPerformed(ActionEvent ev) {
	String name = (String)containers.getSelectedValue();
	myRMA.installMTP(name);
      }
    };

    private AbstractAction removeMTPAction = new AbstractAction("Remove MTP") {

      public void actionPerformed(ActionEvent ev) {
	int option = JOptionPane.showConfirmDialog(ManageMTPsDialog.this, "Are you sure you want to remove the selected MTP?", "Removing MTP", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	if(option == JOptionPane.YES_OPTION) {
	  String name = (String)containers.getSelectedValue();
	  String address = (String)addresses.getSelectedValue();
	  myRMA.uninstallMTP(name, address);
	}
      }

    };


    /** Creates new form ManageMTPsDialog */
    public ManageMTPsDialog(rma anRMA, Frame parent, boolean modal, Map m) {
      super(parent, "Platform MTPs Management", modal);
      myRMA = anRMA;
      owner = parent;
      data = m;
      initComponents();
    }

    public void showCentered() {
      setLocation(owner.getX() + (owner.getWidth() - getWidth()) / 2, owner.getY() + (owner.getHeight() - getHeight()) / 2);
      setVisible(true);
    }

    public void setData(List keys, Map m) {
      data = m;
      Object[] containerNames = keys.toArray();
      containers.setListData(containerNames);
      addresses.setListData(EMPTY_LIST);
      addMTP.setEnabled(false);
      removeMTP.setEnabled(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        lists = new JPanel();
        containers = new JList(EMPTY_LIST);
        addresses = new JList(EMPTY_LIST);
        buttons = new JPanel();
        addMTP = new JButton();

	// Manually configure this JButton with its Action object, for
	// JDK 1.2 compatibility
	addMTP.setText((String)addMTPAction.getValue(Action.NAME));
	addMTP.setIcon((Icon)addMTPAction.getValue(Action.SMALL_ICON));
	addMTP.setToolTipText((String)addMTPAction.getValue(Action.SHORT_DESCRIPTION));
	addMTP.addActionListener(addMTPAction);

        removeMTP = new JButton();

	// Manually configure this JButton with its Action object, for
	// JDK 1.2 compatibility
	removeMTP.setText((String)removeMTPAction.getValue(Action.NAME));
	removeMTP.setIcon((Icon)removeMTPAction.getValue(Action.SMALL_ICON));
	removeMTP.setToolTipText((String)removeMTPAction.getValue(Action.SHORT_DESCRIPTION));
	removeMTP.addActionListener(removeMTPAction);

        closeDlg = new JButton();
        
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        addWindowListener(new java.awt.event.WindowAdapter() {
	  public void windowClosing(java.awt.event.WindowEvent evt) {
	    closeDialog(evt);
	  }
        });
        
        lists.setLayout(new GridLayout(1, 2));

        lists.setBorder(new CompoundBorder(new EmptyBorder(new Insets(5, 5, 5, 5)),
					     new CompoundBorder(new BevelBorder(BevelBorder.LOWERED),
								new EmptyBorder(new Insets(5, 5, 5, 5)))));

	JPanel containersPane = new JPanel(new GridLayout(1, 1));
        containersPane.setBorder(new TitledBorder(new EtchedBorder(), " Containers "));
        containersPane.add(new JScrollPane(containers));
	lists.add(containersPane);

	JPanel addressesPane = new JPanel(new GridLayout(1, 1));
        addressesPane.setBorder(new TitledBorder(new EtchedBorder(), " Addresses "));
        addressesPane.add(new JScrollPane(addresses));
	lists.add(addressesPane);

        getContentPane().add(lists);

        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
	buttons.add(Box.createHorizontalGlue());
        
	buttons.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));

        addMTP.setToolTipText("Install a new MTP on the selected container");
        buttons.add(addMTP);

        removeMTP.setToolTipText("Uninstall the selected MTP");
        buttons.add(removeMTP);

	buttons.add(Box.createHorizontalStrut(10));

        closeDlg.setToolTipText("Close the MTP management dialog");
        closeDlg.setText("Close");
        buttons.add(closeDlg);

	buttons.add(Box.createHorizontalGlue());

        getContentPane().add(buttons, BorderLayout.SOUTH);
        
        pack();

	containers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	containers.addListSelectionListener(new ListSelectionListener() {
	  public void valueChanged(ListSelectionEvent e) {
	    // Skip event burst, apart from the last one
	    if(!e.getValueIsAdjusting()) {
		Object sel = containers.getSelectedValue();
		addMTP.setEnabled(sel != null);
		removeMTP.setEnabled(false);
		if(sel == null) {
		  addresses.setListData(EMPTY_LIST);
		  return;
		}
		else {
		  List addrs = (List)data.get(sel);
		  if(addrs != null)
		    addresses.setListData(addrs.toArray());
		  else
		    addresses.setListData(EMPTY_LIST);
		}
	    }
	  }
	});


	addresses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	addresses.addListSelectionListener(new ListSelectionListener() {
	  public void valueChanged(ListSelectionEvent e) {
	    // Skip event burst, apart from the last one
	    if(!e.getValueIsAdjusting()) {
	      // The 'Remove MTP' action is enabled only if some MTP is selected
	      Object sel = containers.getSelectedValue();
	      removeMTP.setEnabled(sel != null);
	    }
	  }

	});

	closeDlg.addActionListener(new ActionListener() {
	  public void actionPerformed(ActionEvent ae) {
	    setVisible(false);
	    dispose();
	  }
	});


    }//GEN-END:initComponents

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog


    private rma myRMA;
    private Frame owner;
    private Map data;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JPanel lists;
    private JList containers;
    private JList addresses;
    private JPanel buttons;
    private JButton addMTP;
    private JButton removeMTP;
    private JButton closeDlg;
    // End of variables declaration//GEN-END:variables

}