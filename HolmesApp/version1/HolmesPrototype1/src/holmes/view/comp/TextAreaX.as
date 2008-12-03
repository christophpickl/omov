package holmes.view.comp {

import mx.controls.TextArea;

public class TextAreaX extends TextArea {
	public function TextAreaX() {
		super();

		this.editable = false;
		this.selectable = false;
		this.setStyle("borderThickness", 0);
		this.setStyle("backgroundAlpha", 0);
		this.setStyle("textAlign", "left");

		this.wordWrap = true;

	}

}
}