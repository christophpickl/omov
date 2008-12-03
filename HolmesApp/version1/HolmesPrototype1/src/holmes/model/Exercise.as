package holmes.model {

import mx.collections.ArrayCollection;


[Bindable]
public class Exercise {

	public var idExercise: int;

	public var title: String;

	public var muscleRelations: ArrayCollection;


	public function Exercise() {
	}

	public static function create(idExercise: int, title: String, muscleRelations: ArrayCollection): Exercise {
		const x: Exercise = new Exercise();

		x.idExercise = idExercise;
		x.title = title;
		x.muscleRelations = muscleRelations;

		return x;
	}

}
}