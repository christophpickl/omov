package holmes.model {

import mx.collections.ArrayCollection;


[Bindable]
public class Plan {

	public var dateCreated: Date;

	/** ArrayCollection<PlannedExercise> */
	public var plannedExercises: ArrayCollection;


	public function Plan() {
	}

	public static function create(dateCreated: Date, plannedExercises: ArrayCollection): Plan {
		const x: Plan = new Plan();

		x.dateCreated = dateCreated;
		x.plannedExercises = plannedExercises;

		return x;
	}

}
}