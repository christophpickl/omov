package holmes.model {


[Bindable]
public class Muscle {

	public var id: String;
	public var title: String;


	public function Muscle() {
	}

	public static function create(id: String, title: String): Muscle {
		const x: Muscle = new Muscle();

		x.id = id;
		x.title = title;

		return x;
	}
}
}