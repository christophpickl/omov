package holmes.dao {

public class Dao {

	private static var INSTANCE: Dao;

	private const dao: IDao = new DummyDao();


	public function Dao(singletonEnforcer: SingletonEnforcer) {

	}

	public static function get instance(): IDao {
		if(INSTANCE == null) {
			INSTANCE = new Dao(new SingletonEnforcer());
		}
		return INSTANCE.dao;
	}

}
}

class SingletonEnforcer { }