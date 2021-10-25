package dialog;

import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;

public class InputCheck extends DefaultStyledDocument {

	private static final long serialVersionUID = 1L;
	private boolean komma;
	private int nachkomma;
	private int STELLEN = 11;

	public InputCheck() {
	}

	protected void removeUpdate(DefaultDocumentEvent chng) {
		if (nachkomma > 0)
			nachkomma--;
		if (nachkomma == 0)
			komma = false;
	}

	// TODO Hier stehen die Stellen!!!
	public void insertString(int offset, String str, AttributeSet a) throws javax.swing.text.BadLocationException {
		if (offset > STELLEN)
			return;
		if (str.equals(",") && !komma) {
			komma = true;
			super.insertString(offset, str, a);
		} else if (nachkomma < 2) {
			try {
				Double.parseDouble(str);
				if (komma)
					nachkomma++;
				super.insertString(offset, str, a);
			} catch (Exception ex) {
				java.awt.Toolkit.getDefaultToolkit().beep();
			}
		}
	}
}
