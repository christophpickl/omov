package holmes.model {

[Bindable]
public class MuscleRelation {

	public var muscle: Muscle;

	/** 0 to 100 */
	public var weight: uint;


	public function MuscleRelation() {
		super();
	}

	public static function create(muscle: Muscle, weight: uint): MuscleRelation {
		const x: MuscleRelation = new MuscleRelation();

		x.muscle = muscle;
		x.weight = weight;

		return x;
	}

}
}