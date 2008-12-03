package holmes.common {

import holmes.model.Catalog;
import holmes.model.Exercise;
import holmes.model.Plan;
import holmes.model.PlannedExercise;

import mx.collections.ArrayCollection;

[Bindable]
public class Model {

	/** ArrayCollection<Catalog> */
	public var catalogs: ArrayCollection = new ArrayCollection();

	public var catalog: Catalog = null;


	/** ArrayCollection<Exercise> */
	public var exercises: ArrayCollection = new ArrayCollection();

	public var exercise: Exercise = null;


	/** ArrayCollection<Plan> */
	public var plans: ArrayCollection = new ArrayCollection();

	public var plan: Plan = null;

	public var plannedExercise: PlannedExercise = null;


	private static var INSTANCE: Model;
	public function Model(singletonEnforcer: SingletonEnforcer) { }
	public static function get instance(): Model {
		if(INSTANCE == null) { INSTANCE = new Model(new SingletonEnforcer()); }
		return INSTANCE;
	}
}
}

class SingletonEnforcer { }