package holmes.view {

import holmes.common.Model;
import holmes.model.Exercise;
import holmes.model.PlannedExercise;
import holmes.service.Wizard;

import logging.Logger;

import mx.controls.DataGrid;
import mx.controls.TileList;
import mx.controls.dataGridClasses.DataGridColumn;
import mx.core.DragSource;
import mx.events.DragEvent;
import mx.utils.ObjectUtil;

public class PlannedExercisesGrid extends DataGrid {

	private static const LOG: Logger = Logger.getLogger("holmes.view.PlannedExercisesGrid");

	public function PlannedExercisesGrid() {
		super();

		this.dropEnabled = true;
		this.dragMoveEnabled = true;

		this.addEventListener(DragEvent.DRAG_ENTER, this.onDragEnter);
		this.addEventListener(DragEvent.DRAG_DROP, this.onDragDrop);

		const cols: Array = new Array();
		const col1: DataGridColumn = new DataGridColumn();
		col1.dataField = "title";
		col1.headerText = "Exercise";
		const col2: DataGridColumn = new DataGridColumn();
		col2.dataField = "repetitions";
		col2.headerText = "Repetitions";

		cols.push(col1, col2);

		this.columns = cols;
	}

	/* ****************************************************************************************************** */
	//    DRAG'N'DROP
	/* ****************************************************************************************************** */

	/** this function validates the drop */
	private function onDragEnter(event: DragEvent):void {
		LOG.finer("onDragEnter(event="+event+")");

		// accept the drop from exercises list only
        if (isDragInitiatorExercisesList(event) == true && Model.instance.plan != null) {
			LOG.finer("DragManager.acceptDragDrop");
    		// DragManager.acceptDragDrop(UIComponent(event.currentTarget));

    		// TODO change cursor icon for drop-target-enabled
        }
	}
	private static function isDragInitiatorExercisesList(event: DragEvent):Boolean {
		if (event.dragInitiator is TileList) {
        	const list: TileList = event.dragInitiator as TileList;
        	if(list.id == "exercisesList") {
        		return true;
        	}
		}

		return false;
	}

	/** this function adds new items to the grid */
	private function onDragDrop(event: DragEvent):void {
		LOG.finest("onDragDrop(event="+event+")");

		var ds: DragSource = event.dragSource;
		if (isDragInitiatorExercisesList(event) && ds.hasFormat("items")) {
			LOG.finer("accepting drop object.");
			var dropData: Array = ds.dataForFormat("items") as Array;
			if(dropData.length != 1) {
				throw new Error("dropData.length != 1 but " + dropData.length + "! " + ObjectUtil.toString(dropData));
			}

			const droppedExercise: Exercise = dropData[0] as Exercise;
			const plannedExercise: PlannedExercise = Wizard.instance.createPlannedExercise(droppedExercise);
			Model.instance.plan.plannedExercises.addItem(plannedExercise); // TODO do not only add it at last position, but on proper dropped position!
			event.stopImmediatePropagation();
		}
	}

}
}