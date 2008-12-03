package holmes.service {

import holmes.common.Model;
import holmes.model.Exercise;
import holmes.model.Plan;
import holmes.model.PlannedExercise;


public class Wizard {


	public function createPlannedExercise(exercise: Exercise): PlannedExercise {

		var foundExercise: PlannedExercise = null;
		for each(var plan: Plan in Model.instance.plans) {
			for each(var plannedExercise: PlannedExercise in plan.plannedExercises) {
				if(exercise.idExercise == plannedExercise.idExercise) {
					foundExercise = plannedExercise;
				}
			}
		}

		const repetitions: uint = (foundExercise != null ? foundExercise.repetitions :  0);
		const weight: Number = (foundExercise != null ? foundExercise.weight : 0.0);

		return PlannedExercise.createByExercise(1, exercise, repetitions, weight);
	}


	private static var INSTANCE: Wizard;
	public function Wizard(singletonEnforcer: SingletonEnforcer) { }
	public static function get instance(): Wizard {
		if(INSTANCE == null) { INSTANCE = new Wizard(new SingletonEnforcer()); }
		return INSTANCE;
	}
}
}

class SingletonEnforcer { }