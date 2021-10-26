package dialog;

import model.BankSettingsDTO;
import model.Language;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

public class SelectAccountDialog extends ScreenDialog {

    public SelectAccountDialog(JFrame mainFrame, Language language) {
        super(mainFrame, true);
        setTitle("Konto auswählen");
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        JLabel dopp = new JLabel("Bitte Auswahl mit Doppelklick oder ENTER bestätigen", SwingConstants.CENTER);
        dopp.setFont(new Font("", Font.BOLD, 14));
        dopp.setBackground(Color.WHITE);
        add(BorderLayout.PAGE_START, dopp);

        //Doppelschritt: 1.Nur die Kontonummern im Array
        //final long[] nums = controller.k_getNummern();

        //2. Schritt der komplette Datensatz
        final String[] kontos = new String[4]; //controller.k_listeKontos();

        DefaultTableModel dtm = new DefaultTableModel() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        dtm.addColumn("Konto");
        dtm.addColumn("Kontentyp");
        dtm.addColumn("Erstellt");
        dtm.addColumn("Saldo");

        for (String str : kontos) {
            String[] values = str.split("°°");
            //Alternative
            //dtm.addRow(new String[] { values[0],values[1],values[2],values[3]});
            dtm.addRow(values);
        }

        final JTable table = new JTable(dtm);
        //					row, col
        table.changeSelection(0, 0, false, false);
/*
        table.addMouseListener(
                new java.awt.event.MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            controller.ko_choose(nums[table.getSelectedRow()]);
                            kontoChosen = true;
                            mtextarea.append("\n" + language.trans("chokonto") + ": " + controller.ko_getNr());
                            statusBarText();
                            closeSecondScreen();
                        }
                    }
                }
        );

        table.addKeyListener(new java.awt.event.KeyAdapter() {
                                 public void keyPressed(KeyEvent ke) {
                                     if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                                         try {
                                             controller.ko_choose(nums[table.getSelectedRow()]);
                                         } catch (Exception e) {
                                         }

                                         kontoChosen = true;
                                         mtextarea.append("\n" + language.trans("chokonto") + ": " + controller.ko_getNr());
                                         statusBarText();
                                         closeSecondScreen();
                                     }
                                     if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                                         closeSecondScreen();
                                     }
                                 }
                             }
        );
*/
        table.setShowVerticalLines(false);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);

        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
        dtcr.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumn("Saldo").setCellRenderer(dtcr);

        JScrollPane scr = new JScrollPane(table);

        add("Center", scr);

        DecimalFormat df = new DecimalFormat("##,##0.00 \u00A4\u00A4");
        // controller.k_gesamtSaldo()
        String saldo = String.format("%16s\n", df.format("1000"));

        JLabel ssaldo = new JLabel("Liquide Mittel: " + saldo, SwingConstants.RIGHT);
        ssaldo.setFont(new Font("Lucida Console", Font.PLAIN, 16));
        ssaldo.setBackground(Color.WHITE);

        setBackground(Color.white);
        add("South", ssaldo);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(mainFrame.getX(), mainFrame.getY(), 600, 80 + kontos.length * 20);
        setVisible(true);
    }
}
