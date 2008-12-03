package holmes.dao {

import holmes.common.Model;
import holmes.model.Catalog;
import holmes.model.Exercise;
import holmes.model.Muscle;
import holmes.model.MuscleRelation;
import holmes.model.Plan;
import holmes.model.PlannedExercise;

import logging.Logger;

import mx.collections.ArrayCollection;


internal class DummyDao implements IDao {

	private static const LOG: Logger = Logger.getLogger("holmes.dao.DummyDao");

	private const catalogs: ArrayCollection = new ArrayCollection();

	private const plans: ArrayCollection = new ArrayCollection();

	public function DummyDao() {
		const muscle_head: Muscle = Muscle.create("head", "Head");
		const muscle_body: Muscle = Muscle.create("body", "Body");
		const muscle_legs: Muscle = Muscle.create("legs", "Legs");

		var exercise1_1muscles: ArrayCollection = new ArrayCollection();
		var exercise1_2muscles: ArrayCollection = new ArrayCollection();
		var exercise2_1muscles: ArrayCollection = new ArrayCollection();
		exercise1_1muscles.addItem(MuscleRelation.create(muscle_head, 100));
		exercise1_1muscles.addItem(MuscleRelation.create(muscle_body, 40));
		exercise1_2muscles.addItem(MuscleRelation.create(muscle_body, 80));
		exercise2_1muscles.addItem(MuscleRelation.create(muscle_legs, 10));

		const excercise1_1: Exercise = Exercise.create(1, "oberes 1.1 exc", exercise1_1muscles);
		const excercise1_2: Exercise = Exercise.create(2, "mittlere 1.2 exc", exercise1_2muscles);
		const excercise2_1: Exercise = Exercise.create(3, "untere 2.1 exc", exercise2_1muscles);

		const catalog1exercises: ArrayCollection = new ArrayCollection();
		const catalog2exercises: ArrayCollection = new ArrayCollection();
		catalog1exercises.addItem(excercise1_1);
		catalog1exercises.addItem(excercise1_2);
		catalog2exercises.addItem(excercise2_1);
		catalogs.addItem(Catalog.create("Catalog 1", catalog1exercises));
		catalogs.addItem(Catalog.create("Catalog 2", catalog2exercises));

		const plan1exercises: ArrayCollection = new ArrayCollection();
		const plan2exercises: ArrayCollection = new ArrayCollection();
		plan1exercises.addItem(PlannedExercise.createByExercise(1, excercise1_1, 20, 45.5));
		plan2exercises.addItem(PlannedExercise.createByExercise(2, excercise2_1, 22,  5.5));
		plans.addItem(Plan.create(new Date(), plan1exercises));
		plans.addItem(Plan.create(new Date(), plan2exercises));

	}

	public function fetchData(): void {
		LOG.fine("fetchData(); catalogs.length=" + catalogs.length);
		Model.instance.catalogs = this.catalogs;
		Model.instance.plans = this.plans;
	}


}
}