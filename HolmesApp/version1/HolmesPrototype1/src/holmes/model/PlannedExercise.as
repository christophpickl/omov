package holmes.model {

[Bindable]
public class PlannedExercise extends Exercise {

	public var idPlannedExercise: int;

	public var repetitions: uint;

	public var weight: Number;

	public function PlannedExercise() {
		super();
	}

	public static function createByExercise(idPlannedExercise: int, exercise: Exercise, repetitions: uint, weight: Number): PlannedExercise {
		const x: PlannedExercise = new PlannedExercise();

		x.idPlannedExercise = idPlannedExercise;
		x.idExercise = exercise.idExercise;
		x.title = exercise.title;
		x.muscleRelations = exercise.muscleRelations;

		x.repetitions = repetitions;
		x.weight = weight;

		return x;
	}

}
}