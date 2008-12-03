package holmes.view.comp {

import flash.display.DisplayObject;
import flash.events.Event;

import mx.controls.TileList;
import mx.events.ScrollEvent;
import mx.events.ScrollEventDetail;

/**
 * http://blogs.adobe.com/aharui/2008/03/smooth_scrolling_list.html
 */
public class HTileList extends TileList {
	public function HTileList() {
		super();

		this.direction = "vertical";
		this.rowCount = 1;
		this.liveScrolling = true;
		this.horizontalScrollPolicy = "on";

		this.offscreenExtraRowsOrColumns = 2;

	}

	protected override function configureScrollBars():void {
		super.configureScrollBars();

		if(this.horizontalScrollBar) {
			this.horizontalScrollBar.lineScrollSize = .125;  // should be inverse power of 2
		}
	}

	private var fudge: Number;

	public override function get horizontalScrollPosition() :Number {
		if(isNaN(fudge) == false) {
			const newHscrollPosition: Number = super.horizontalScrollPosition + fudge;
			fudge = NaN;
			return newHscrollPosition;
		}

		return Math.floor(super.horizontalScrollPosition);
	}

	protected override function scrollHandler(event: Event): void {
		// going backward is trickier.  When you cross from, for instance 2.1 to 1.9, you need to convince
		// the superclass that it is going from 2 to 1 so the delta is -1 and not -.2.
		// we do this by adding a fudge factor to the first return from verticalScrollPosition
		// which is used by the superclass logic.
		var last: Number = super.horizontalScrollPosition;
		var vsp:Number = this.horizontalScrollBar.scrollPosition;
		if(	vsp < last &&
			last != Math.floor(last) || vsp != Math.floor(vsp) &&
			Math.floor(vsp) < Math.floor(last)
		) {
			fudge = Math.floor(last) - Math.floor(this.horizontalScrollBar.scrollPosition);
			trace(last.toFixed(2), vsp.toFixed(2), fudge);
		}

		super.scrollHandler(event);
		var pos:Number = super.horizontalScrollPosition;
		// if we get a THUMB_TRACK, then we need to calculate the position
		// because it gets rounded to an int by the ScrollThumb code, and
		// we want fractional values.
		if(event is ScrollEvent) {
			var se:ScrollEvent = ScrollEvent(event);
			if(se.detail == ScrollEventDetail.THUMB_TRACK) {
				if(horizontalScrollBar.numChildren == 4) {
					var downArrow:DisplayObject = horizontalScrollBar.getChildAt(3);
					var thumb:DisplayObject = horizontalScrollBar.getChildAt(2);
					pos = (thumb.y - downArrow.height) / (downArrow.y - thumb.height - downArrow.height) * maxHorizontalScrollPosition;
					// round to nearest lineScrollSize;
					pos /= horizontalScrollBar.lineScrollSize;
					pos = Math.round(pos);
					pos *= horizontalScrollBar.lineScrollSize;
					//trace("faked", pos);
				}
			}
		}
		var fraction:Number = pos - horizontalScrollPosition;
		fraction *= columnWidth;
		//trace("was", listContent.y.toFixed(2));
		listContent.move(viewMetrics.left + listContent.leftOffset - fraction, listContent.y);
		//trace("now", listContent.y.toFixed(2), fraction.toFixed(2), listItems[0][0].data.lastName);
	}

}
}