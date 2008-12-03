package holmes.model {

import mx.collections.ArrayCollection;


[Bindable]
public class Catalog {

	public var title: String;

	/** ArrayCollection<Exercise> */
	public var exercises: ArrayCollection;


	public function Catalog() {

	}

	public static function create(title: String, exercises: ArrayCollection): Catalog {
		const x: Catalog = new Catalog();

		x.title = title;
		x.exercises = exercises;

		return x;
	}

}
}